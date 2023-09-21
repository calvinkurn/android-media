package com.tokopedia.universal_sharing.view.bottomsheet

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.di.UniversalSharingComponentFactory
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.util.Result
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseAction
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseViewModel
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.UniversalSharingPostPurchaseAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UniversalSharingPostPurchaseBottomSheet :
    BottomSheetUnify(),
    UniversalSharingPostPurchaseBottomSheetListener {

    @Inject
    lateinit var viewModel: UniversalSharingPostPurchaseViewModel

    private val typeFactory = UniversalSharingTypeFactoryImpl(this)
    private val adapter = UniversalSharingPostPurchaseAdapter(typeFactory)
    private var loaderLayout: ConstraintLayout? = null
    private var data: UniversalSharingPostPurchaseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setupBottomSheetConfig()
        setBottomSheetChildView()
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
        isFullpage = true
    }

    private fun setBottomSheetChildView() {
        val view = LayoutInflater.from(context).inflate(
            R.layout.universal_sharing_post_purchase_bottomsheet,
            null,
            false
        )
        loaderLayout = view.findViewById(R.id.universal_sharing_layout_loading)
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetTitle()
        setRecyclerView(view)
        setupObserver()
        setupInitialData()
    }

    private fun setBottomSheetTitle() {
        setTitle(getString(R.string.universal_sharing_post_purchase_bottomsheet_title))
    }

    private fun setRecyclerView(view: View) {
        val recyclerView: RecyclerView? = view.findViewById(R.id.universal_sharing_post_purchase_rv)
        recyclerView?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    private fun setupObserver() {
        viewModel.setupViewModelObserver()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    when (it) {
                        is Result.Success -> {
                            adapter.setVisitables(it.data)
                        }
                        is Result.Error -> {
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
                        Result.Loading -> Unit // no-op
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharingState.collectLatest {
                    println(it)
                }
            }
        }
    }

    private fun setupInitialData() {
        data?.let {
            viewModel.processAction(UniversalSharingPostPurchaseAction.RefreshData(it))
        }
    }

    override fun onClickShare(productId: String) {
        if (productId.isNotBlank()) {
            loaderLayout?.show()
            viewModel.processAction(UniversalSharingPostPurchaseAction.ClickShare(productId))
        } else {
            // todo: Handle
        }
    }

    fun updateData(data: UniversalSharingPostPurchaseModel) {
        this.data = data
    }
}
