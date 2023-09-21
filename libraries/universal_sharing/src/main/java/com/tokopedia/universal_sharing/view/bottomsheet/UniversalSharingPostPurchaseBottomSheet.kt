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
import com.tokopedia.universal_sharing.databinding.UniversalSharingPostPurchaseBottomsheetBinding
import com.tokopedia.universal_sharing.di.UniversalSharingComponentFactory
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.util.NetworkUtil
import com.tokopedia.universal_sharing.util.Result
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseAction
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseViewModel
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.UniversalSharingPostPurchaseAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactoryImpl
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UniversalSharingPostPurchaseBottomSheet :
    BottomSheetUnify(),
    UniversalSharingPostPurchaseBottomSheetListener {

    @Inject
    lateinit var viewModel: UniversalSharingPostPurchaseViewModel

    @Inject
    lateinit var networkUtil: NetworkUtil

    private var _binding: UniversalSharingPostPurchaseBottomsheetBinding? = null
    private val binding: UniversalSharingPostPurchaseBottomsheetBinding get() = _binding!!

    private val typeFactory = UniversalSharingTypeFactoryImpl(this)
    private val adapter = UniversalSharingPostPurchaseAdapter(typeFactory)
    private var data = UniversalSharingPostPurchaseModel()

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
        viewModel.observeDetailProductFlow().collectLatest {
            when (it) {
                is Result.Success -> {
                    binding.universalSharingLayoutLoading.hide()
                    // todo: ask rama how to use branch link
                }
                is Result.Error -> {
                    binding.universalSharingLayoutLoading.hide()
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    view?.let { view ->
                        Toaster.build(
                            view,
                            errorMessage,
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
                Result.Loading -> {
                    binding.universalSharingLayoutLoading.show()
                }
                else -> Unit // no-op
            }
            println(it)
        }
    }

    private fun setupInitialData() {
        viewModel.processAction(UniversalSharingPostPurchaseAction.RefreshData(data))
    }

    override fun onClickShare(productId: String) {
        if (productId.isNotBlank()) {
            viewModel.processAction(UniversalSharingPostPurchaseAction.ClickShare(productId))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
