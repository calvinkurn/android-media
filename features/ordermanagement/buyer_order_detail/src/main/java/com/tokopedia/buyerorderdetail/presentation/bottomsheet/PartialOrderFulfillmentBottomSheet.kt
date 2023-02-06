package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerPartialOrderFulfillmentTracker
import com.tokopedia.buyerorderdetail.databinding.PartialOrderFulfillmentBottomsheetBinding
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.PartialOrderFulfillmentAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.PartialOrderFulfillmentListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.PartialOrderFulfillmentViewModel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PartialOrderFulfillmentBottomSheet : BottomSheetUnify(), PartialOrderFulfillmentListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PartialOrderFulfillmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PartialOrderFulfillmentViewModel::class.java)
    }

    private val orderId by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(POF_ORDER_ID_KEY)
    }

    private val partialOrderFulfillmentTypeFactoryImpl by lazy(LazyThreadSafetyMode.NONE) {
        PartialOrderFulfillmentTypeFactoryImpl(this)
    }

    private val partialOrderFulfillmentAdapter by lazy {
        PartialOrderFulfillmentAdapter(partialOrderFulfillmentTypeFactoryImpl)
    }

    private var binding by autoClearedNullable<PartialOrderFulfillmentBottomsheetBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        binding = PartialOrderFulfillmentBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(
            getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_bottomsheet_title)
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvPartialOrderFulfillment()
        setupCta()
        loadPofInfo()
        observePartialOrderFulfillmentInfo()
        observeApprovePartialOrderFulfillment()
    }

    override fun onPofFulfilledToggleClicked(
        isExpanded: Boolean,
        item: PofFulfilledToggleUiModel
    ) {
        BuyerPartialOrderFulfillmentTracker.eventClickTotalAvailableItemPof()

        val oldItem = partialOrderFulfillmentAdapter.filterUiModel<PofFulfilledToggleUiModel>()
        oldItem?.let {
            val newItem = it.copy(isExpanded = isExpanded)
            partialOrderFulfillmentAdapter.updateItem(oldItem, newItem)
        }

        if (isExpanded) {
            partialOrderFulfillmentAdapter.expandFulfilledProducts(item.pofProductFulfilledList)
        } else {
            partialOrderFulfillmentAdapter.collapseFulfilledProducts()
        }
    }

    override fun onRefundEstimateInfoClicked(estimateInfoUiModel: EstimateInfoUiModel) {
        BuyerPartialOrderFulfillmentTracker.eventClickEstimateIconInPopupPof()

        val refundEstimateInfoBottomSheet = PofEstimateRefundInfoBottomSheet.newInstance(
            estimateInfoUiModel.title,
            estimateInfoUiModel.info
        )
        refundEstimateInfoBottomSheet.show(childFragmentManager)
    }

    override fun onFooterHyperlinkClicked(linkUrl: String) {
        BuyerPartialOrderFulfillmentTracker.eventClickTermsAndConditionsInPopupPof()
        goToWebView(linkUrl)
    }

    override fun onHeaderHyperlinkClicked(linkUrl: String) {
        goToWebView(linkUrl)
    }

    override fun onErrorActionClicked() {
        partialOrderFulfillmentAdapter.run {
            hideError()
            hideLoadingShimmer()
        }
        loadPofInfo()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun goToWebView(linkUrl: String) {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalGlobal.WEBVIEW, linkUrl)
        }
    }

    private fun setupRvPartialOrderFulfillment() {
        binding?.rvPartialOrderFulfillment?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = partialOrderFulfillmentAdapter
        }
    }

    private fun observePartialOrderFulfillmentInfo() {
        observe(viewModel.partialOrderFulfillmentRespondInfo) {
            partialOrderFulfillmentAdapter.hideLoadingShimmer()
            when (it) {
                is Success -> {
                    showPartialOrderFulfillment(it.data)
                }
                is Fail -> {
                    showPartialOrderFulfillmentError(it.throwable)
                }
            }
        }
    }

    private fun showPartialOrderFulfillment(data: PartialOrderFulfillmentWrapperUiModel) {
        partialOrderFulfillmentAdapter.updateItems(data.partialOrderFulfillmentUiModelList)
        binding?.run {
            viewPofShadow.show()
            btnPrimaryConfirm.show()
            btnSecondaryCancelOrder.show()
        }
    }

    private fun observeApprovePartialOrderFulfillment() {
        observe(viewModel.approvePartialOrderFulfillment) {
            hideLoadingBtnPrimaryConfirm()
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        dismiss()
                        setupAfterPofSuccess()
                    } else {
                        showToasterError()
                    }
                }
                is Fail -> {
                    showToasterError()
                }
            }
        }
    }

    private fun setupAfterPofSuccess() {
        activity?.let {
            val message = it.getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_success_message)
            val intent = it.intent?.apply {
                putExtra(
                    ApplinkConstInternalOrder.PartialOrderFulfillmentKey.TOASTER_MESSAGE,
                    message
                )
            }
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    private fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerBuyerOrderDetailComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    private fun showPartialOrderFulfillmentError(throwable: Throwable) {
        partialOrderFulfillmentAdapter.showError(PofErrorUiModel(throwable))
    }

    private fun loadPofInfo() {
        partialOrderFulfillmentAdapter.showLoadingShimmer()
        orderId?.let {
            viewModel.fetchPartialOrderFulfillment(it)
        }
    }

    private fun setupCta() = binding?.run {
        btnPrimaryConfirm.setOnClickListener {
            BuyerPartialOrderFulfillmentTracker.eventClickConfirmationInPopupPof()
            orderId?.let { orderId ->
                btnPrimaryConfirm.isLoading = true
                viewModel.approvePartialOrderFulfillment(orderId)
            }
        }
        btnSecondaryCancelOrder.setOnClickListener {
            showConfirmedCancelledOrderBottomSheet()
        }
    }

    private fun hideLoadingBtnPrimaryConfirm() {
        binding?.btnPrimaryConfirm?.isLoading = false
    }

    private fun showConfirmedCancelledOrderBottomSheet() {
        BuyerPartialOrderFulfillmentTracker.eventClickRejectOrderInPopupPof()
        orderId?.let {
            val bottomSheet = PofConfirmRejectBottomSheet.newInstance(it)
            bottomSheet.show(childFragmentManager)
        }
    }

    private fun showToasterError() {
        val message =
            context?.getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_error_request)

        if (message?.isNotBlank() == true) {
            view?.run {
                Toaster.build(
                    view = this,
                    text = message,
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {

        const val POF_ORDER_ID_KEY = "pof_order_id_key"
        private val TAG = PartialOrderFulfillmentBottomSheet::class.java.simpleName

        fun newInstance(orderId: String): PartialOrderFulfillmentBottomSheet {
            return PartialOrderFulfillmentBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(POF_ORDER_ID_KEY, orderId)
                }
            }
        }
    }
}
