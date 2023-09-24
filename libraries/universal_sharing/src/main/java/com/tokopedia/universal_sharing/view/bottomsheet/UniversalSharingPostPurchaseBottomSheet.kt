package com.tokopedia.universal_sharing.view.bottomsheet

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.databinding.UniversalSharingPostPurchaseBottomsheetBinding
import com.tokopedia.universal_sharing.di.UniversalSharingComponentFactory
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.util.NetworkUtil
import com.tokopedia.universal_sharing.util.Result
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseAction
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseViewModel
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.UniversalSharingPostPurchaseAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase.UniversalSharingPostPurchaseProductListener
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactoryImpl
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class UniversalSharingPostPurchaseBottomSheet :
    BottomSheetUnify(),
    UniversalSharingPostPurchaseProductListener {

    @Inject
    lateinit var viewModel: UniversalSharingPostPurchaseViewModel

    @Inject
    lateinit var networkUtil: NetworkUtil

    @Inject
    lateinit var analytics: UniversalSharebottomSheetTracker

    private var _binding: UniversalSharingPostPurchaseBottomsheetBinding? = null
    private val binding: UniversalSharingPostPurchaseBottomsheetBinding get() = _binding!!

    private var listener: UniversalSharingPostPurchaseBottomSheetListener? = null

    private val typeFactory = UniversalSharingTypeFactoryImpl(this)
    private val adapter = UniversalSharingPostPurchaseAdapter(typeFactory)

    private var data = UniversalSharingPostPurchaseModel()
    private var shouldClosePage: Boolean = true
    private val orderIdList = mutableSetOf<String>()
    private val productIdList = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        getBundle()
        setupBottomSheetConfig()
        setBottomSheetChildView()
    }

    @SuppressLint("DeprecatedMethod")
    private fun getBundle() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(
                DATA_KEY,
                UniversalSharingPostPurchaseModel::class.java
            ) ?: UniversalSharingPostPurchaseModel()
        } else {
            arguments?.getParcelable(DATA_KEY) ?: UniversalSharingPostPurchaseModel()
        }
    }

    private fun initInjector() {
        UniversalSharingComponentFactory.instance.createComponent(
            requireContext().applicationContext as Application
        ).inject(this)
    }

    private fun setupBottomSheetConfig() {
        showHeader = true // show title
        showCloseIcon = true // show close button
        clearContentPadding = true // remove default margin
        isDragable = false // should be not draggable
        this.setCloseClickListener {
            trackClose()
            listener?.onClickClose()
        }
        this.setOnDismissListener {
            listener?.onDismiss(shouldClosePage)
        }
    }

    private fun setBottomSheetChildView() {
        _binding = UniversalSharingPostPurchaseBottomsheetBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetTitle()
        setRecyclerView()
        setupObserver()
        setupInitialData()
    }

    private fun setBottomSheetTitle() {
        setTitle(getString(R.string.universal_sharing_post_purchase_bottomsheet_title))
    }

    private fun setRecyclerView() {
        binding.universalSharingPostPurchaseRv.adapter = adapter
        binding.universalSharingPostPurchaseRv.layoutManager = LinearLayoutManager(context)
        binding.universalSharingPostPurchaseRv.setHasFixedSize(true)
    }

    private fun setupObserver() {
        viewModel.setupViewModelObserver()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeUiState()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeSharingState()
            }
        }
    }

    private suspend fun observeUiState() {
        viewModel.uiState.collectLatest {
            when (it) {
                is Result.Success -> {
                    adapter.updateData(it.data)
                }
                is Result.Error -> {
                    setErrorView()
                }
                Result.Loading -> Unit // no-op
            }
        }
    }

    private fun setErrorView() {
        val errorType = if (networkUtil.isNetworkAvailable(requireContext())) {
            UniversalSharingGlobalErrorUiModel.ErrorType.ERROR_NETWORK
        } else {
            UniversalSharingGlobalErrorUiModel.ErrorType.ERROR_GENERAL
        }
        val errorUi = UniversalSharingGlobalErrorUiModel(errorType)
        adapter.updateData(listOf(errorUi))
    }

    private suspend fun observeSharingState() {
        viewModel.sharingUiState.collectLatest {
            if (it.isLoading) {
                binding.universalSharingLayoutLoading.show()
            } else {
                binding.universalSharingLayoutLoading.hide()
                when {
                    (it.productData != null) -> handleOnSuccessShare(
                        orderId = it.orderId,
                        productData = it.productData
                    )
                    (it.error != null) -> handleOnErrorShare(
                        productId = it.productId,
                        orderId = it.orderId,
                        error = it.error
                    )
                }
            }
        }
    }

    private fun handleOnSuccessShare(
        orderId: String,
        productData: UniversalSharingPostPurchaseProductResponse
    ) {
        if (productData.status == "ACTIVE" || productData.stock > 0) {
            listener?.onOpenShareBottomSheet(orderId, productData)
            shouldClosePage = false // close bottom sheet but not the activity
            dismiss()
        } else {
            showToaster(
                text = getString(R.string.universal_sharing_post_purchase_product_unavailable),
                ctaText = getString(R.string.universal_sharing_post_purchase_ok),
                type = Toaster.TYPE_NORMAL,
                onClick = {}
            )
        }
    }

    private fun handleOnErrorShare(
        productId: String,
        orderId: String,
        error: Throwable
    ) {
        val errorMessage = if (error is UnknownHostException) {
            getString(R.string.universal_sharing_post_purchase_error_network)
        } else {
            ErrorHandler.getErrorMessage(context, error)
        }
        showToaster(
            text = errorMessage,
            ctaText = getString(R.string.universal_sharing_post_purchase_try_again),
            type = Toaster.TYPE_ERROR,
            onClick = { onClickShare(orderId, productId) }
        )
    }

    private fun setupInitialData() {
        viewModel.processAction(UniversalSharingPostPurchaseAction.RefreshData(data))
        trackProductListImpression()
    }

    override fun onClickShare(orderId: String, productId: String) {
        if (productId.isNotBlank() && orderId.isNotBlank()) {
            viewModel.processAction(
                UniversalSharingPostPurchaseAction.ClickShare(
                    orderId = orderId,
                    productId = productId
                )
            )
            analytics.onClickShareProductPostPurchase(
                userShareType = "",
                productId = productId,
                orderId = orderId,
                orderStatus = ""
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setListener(listener: UniversalSharingPostPurchaseBottomSheetListener) {
        this.listener = listener
    }

    private fun showToaster(text: String, ctaText: String, type: Int, onClick: () -> Unit) {
        dialog?.window?.decorView?.let {
            Toaster.build(
                view = it,
                text = text,
                duration = Toaster.LENGTH_LONG,
                type = type,
                actionText = ctaText,
                clickListener = { onClick() }
            ).show()
        }
    }

    private fun trackProductListImpression() {
        data.shopList.forEach { shopList ->
            shopList.productList.forEach {
                orderIdList.add(it.orderId)
                productIdList.add(it.productId)
            }
        }
        analytics.onViewProductListPostPurchase(
            userShareType = "",
            productIdList = productIdList.joinToString(","),
            orderIdList = orderIdList.joinToString(","),
            orderStatus = ""
        )
    }

    private fun trackClose() {
        analytics.onClickCloseProductListPostPurchase(
            userShareType = "",
            orderIdList = orderIdList.joinToString(","),
            orderStatus = ""
        )
    }

    companion object {
        private const val DATA_KEY = "data_key"

        fun newInstance(
            data: UniversalSharingPostPurchaseModel
        ): UniversalSharingPostPurchaseBottomSheet {
            val bottomSheet = UniversalSharingPostPurchaseBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(DATA_KEY, data)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
