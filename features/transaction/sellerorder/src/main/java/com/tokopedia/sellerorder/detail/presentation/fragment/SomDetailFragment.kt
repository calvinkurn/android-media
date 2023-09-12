package com.tokopedia.sellerorder.detail.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickCtaActionInOrderDetail
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickSecondaryActionInOrderDetail
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomEditRefNumResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.navigator.SomNavigator
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToChangeCourierPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToConfirmShippingPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToReschedulePickupPage
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomConfirmShippingBottomSheet
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderEditAwbBottomSheet
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderRequestCancelBottomSheet
import com.tokopedia.sellerorder.common.presenter.dialogs.SomOrderHasRequestCancellationDialog
import com.tokopedia.sellerorder.common.presenter.model.SomPendingAction
import com.tokopedia.sellerorder.common.util.SomConnectionMonitor
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.ACTION_OK
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ASK_BUYER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_BATALKAN_PESANAN
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CHANGE_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING_AUTO
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ORDER_EXTENSION_REQUEST
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_PRINT_AWB
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RESCHEDULE_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RESPOND_TO_CANCELLATION
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RETURN_TO_SHIPPER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_SEARCH_NEW_DRIVER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_SET_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_TRACK_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UBAH_NO_RESI
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UPLOAD_AWB
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_VIEW_COMPLAINT_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BARCODE_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INVOICE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SOURCE_ASK_BUYER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_SET_DELIVERED
import com.tokopedia.sellerorder.common.util.Utils.setUserNotAllowedToViewSom
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.databinding.DialogAcceptOrderFreeShippingSomBinding
import com.tokopedia.sellerorder.databinding.FragmentSomDetailBinding
import com.tokopedia.sellerorder.detail.analytic.performance.SomDetailLoadTimeMonitoring
import com.tokopedia.sellerorder.detail.data.model.GetResolutionTicketStatusResponse
import com.tokopedia.sellerorder.detail.data.model.SetDelivered
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceResponse
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailBookingCodeActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailLogisticInfoActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomSeeInvoiceActivity
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailAddOnViewHolder
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.BottomSheetManager
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBaseRejectOrderBottomSheet
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectOrderAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectReasonsAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetSetDelivered
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailLogisticInfoFragment.Companion.KEY_ID_CACHE_MANAGER_INFO_ALL
import com.tokopedia.sellerorder.detail.presentation.mapper.SomDetailMapper
import com.tokopedia.sellerorder.detail.presentation.model.LogisticInfoAllWrapper
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.viewmodel.SomOrderExtensionViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.net.UnknownHostException
import javax.inject.Inject

/**x
 * Created by fwidjaja on 2019-09-30.
 */
open class SomDetailFragment :
    BaseDaggerFragment(),
    RefreshHandler.OnRefreshHandlerListener,
    SomBottomSheetRejectOrderAdapter.ActionListener,
    SomDetailAdapterFactoryImpl.ActionListener,
    SomBottomSheetRejectReasonsAdapter.ActionListener,
    SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener,
    SomBottomSheetSetDelivered.SomBottomSheetSetDeliveredListener,
    SomOrderEditAwbBottomSheet.SomOrderEditAwbBottomSheetListener,
    SomOrderRequestCancelBottomSheet.SomOrderRequestCancelBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    var isDetailChanged: Boolean = false

    private var somToaster: Snackbar? = null

    private var dynamicPriceResponse: SomDynamicPriceResponse.GetSomDynamicPrice? = SomDynamicPriceResponse.GetSomDynamicPrice()
    private var acceptOrderResponse = SomAcceptOrderResponse.Data.AcceptOrder()
    private var successEditAwbResponse = SomEditRefNumResponse.Data()
    private var failEditAwbResponse = SomEditRefNumResponse.Error()
    private var somDetailLoadTimeMonitoring: SomDetailLoadTimeMonitoring? = null
    private var recyclerViewSharedPool = RecyclerView.RecycledViewPool()
    private var somDetailAdapter = BaseAdapter(getAdapterTypeFactory())

    private var refreshHandler: RefreshHandler? = null
    private var pendingAction: SomPendingAction? = null

    private var somOrderHasCancellationRequestDialog: SomOrderHasRequestCancellationDialog? = null
    private val chatIcon: IconUnify by lazy {
        createChatIcon(requireContext())
    }
    protected val orderExtensionViewModel: SomOrderExtensionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SomOrderExtensionViewModel::class.java)
    }

    protected var orderId = ""

    protected var detailResponse: SomDetailOrder.Data.GetSomDetail? = SomDetailOrder.Data.GetSomDetail()
    protected val somDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SomDetailViewModel::class.java]
    }
    protected val bottomSheetManager by lazy {
        view?.let { if (it is ViewGroup) BottomSheetManager(it, childFragmentManager) else null }
    }

    private fun createChatIcon(context: Context): IconUnify {
        return IconUnify(requireContext(), IconUnify.CHAT).apply {
            setOnClickListener {
                doClickChat()
            }
            layoutParams = LinearLayout.LayoutParams(
                context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toInt(),
                context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toInt()
            ).apply {
                setMargins(0, 0, context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toInt(), 0)
            }
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            setBackgroundResource(outValue.resourceId)
            gone()
        }
    }

    protected val connectionMonitor by lazy { context?.run { SomConnectionMonitor(this) } }

    private var binding by autoClearedNullable<FragmentSomDetailBinding> {
        bottomSheetManager?.clearViewBindings()
    }

    companion object {

        private const val ERROR_GET_ORDER_DETAIL = "Error when get order detail."
        private const val ERROR_ACCEPTING_ORDER = "Error when accepting order."
        private const val ERROR_GET_ORDER_REJECT_REASONS = "Error when get order reject reasons."
        private const val ERROR_WHEN_SET_DELIVERED = "Error when set order status to delivered."
        private const val ERROR_EDIT_AWB = "Error when edit AWB."
        private const val ERROR_REJECT_ORDER = "Error when rejecting order."
        private const val PAGE_NAME = "seller order detail page."

        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailFragment {
            return SomDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, bundle.getString(PARAM_ORDER_ID))
                }
            }
        }
    }

    private fun doClickChat() {
        SomAnalytics.eventClickChatOnHeaderDetail(detailResponse?.statusCode.toString(), detailResponse?.statusText.toString())
        goToAskBuyer()
    }

    private fun goToAskBuyer() {
        val orderId = orderId.takeIf { it.isNotBlank() } ?: getOrderIdExtra()
        val firstProduct = detailResponse?.getFirstProduct()
        val intent = RouteManager.getIntent(
            activity,
            ApplinkConst.TOPCHAT_ASKBUYER,
            detailResponse?.customer?.id.orEmpty(),
            "",
            PARAM_SOURCE_ASK_BUYER,
            detailResponse?.customer?.name,
            detailResponse?.customer?.image
        ).apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, orderId) // it's actually require the id of the order
            putExtra(ApplinkConst.Chat.INVOICE_CODE, detailResponse?.invoice)
            if (firstProduct != null) {
                putExtra(ApplinkConst.Chat.INVOICE_TITLE, firstProduct.name)
                putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, firstProduct.thumbnail)
            }
            putExtra(ApplinkConst.Chat.INVOICE_DATE, detailResponse?.paymentDate)
            putExtra(ApplinkConst.Chat.INVOICE_URL, detailResponse?.invoiceUrl)
            putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, detailResponse?.statusCode?.toString())
            putExtra(ApplinkConst.Chat.INVOICE_STATUS, detailResponse?.statusText)
            putExtra(
                ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT,
                dynamicPriceResponse?.paymentData?.value
            )
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityPltPerformanceMonitoring()
        if (arguments != null) {
            orderId = getOrderIdExtra()
            somDetailLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
            checkUserRole()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(false)
        binding = FragmentSomDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupBackgroundColor()
        setupToolbar()
        prepareLayout()
        observingDetail()
        observingAcceptOrder()
        observingRejectReasons()
        observingRejectOrder()
        observingEditAwb()
        observingSetDelivered()
        observingUserRoles()
        observeRejectCancelOrder()
        observeValidateOrder()
        observeOrderExtensionRequestInfo()
    }

    override fun onResume() {
        super.onResume()
        doOnResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionMonitor?.end()
    }

    override fun onFragmentBackPressed(): Boolean {
        return bottomSheetManager?.dismissBottomSheets().orFalse()
    }

    override fun onShowBuyerRequestCancelReasonBottomSheet(it: SomDetailOrder.Data.GetSomDetail.Button) {
        bottomSheetManager?.showSomOrderRequestCancelBottomSheet(it, detailResponse, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        binding?.somDetailToolbar?.addCustomRightContent(chatIcon)
    }

    override fun doSetDelivered(receiverName: String) {
        setLoadingIndicator(true)
        somDetailViewModel.setDelivered(orderId, receiverName)
    }

    override fun onEditAwbButtonClicked(cancelNotes: String) {
        doEditAwb(cancelNotes)
    }

    override fun onAcceptOrder(actionName: String) {
        setActionAcceptOrder(actionName, orderId, true)
    }

    override fun onRejectOrder(reasonBuyer: String) {
        SomAnalytics.eventClickButtonTolakPesananPopup("${detailResponse?.statusCode.orZero()}", detailResponse?.statusText.orEmpty())
        val orderRejectRequest = SomRejectRequestParam(
            orderId = detailResponse?.orderId.orEmpty(),
            rCode = "0",
            reason = reasonBuyer
        )
        doRejectOrder(orderRejectRequest)
    }

    override fun onRejectCancelRequest() {
        SomAnalytics.eventClickButtonTolakPesananPopup("${detailResponse?.statusCode.orZero()}", detailResponse?.statusText.orEmpty())
        rejectCancelOrder()
    }

    private fun checkUserRole() {
        showLoading()
        if (connectionMonitor?.isConnected == true) {
            somDetailViewModel.getAdminPermission()
        } else {
            showErrorState(GlobalError.NO_CONNECTION)
        }
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(binding?.swipeRefreshLayout, this)
        refreshHandler?.setPullEnabled(true)
        binding?.rvDetail?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = somDetailAdapter
        }
        binding?.somGlobalError?.setActionClickListener {
            if (isShowDetailEligible(somDetailViewModel.somDetailChatEligibility.value)) {
                loadDetail()
            } else {
                checkUserRole()
            }
        }
        recyclerViewSharedPool.setMaxRecycledViews(SomDetailAddOnViewHolder.RES_LAYOUT, SomDetailAddOnViewHolder.MAX_RECYCLED_VIEWS)
    }

    protected open fun loadDetail() {
        showLoading()
        if (connectionMonitor?.isConnected == true) {
            activity?.let {
                SomAnalytics.sendScreenName(SomConsts.DETAIL_ORDER_SCREEN_NAME + orderId)
                somDetailViewModel.loadDetailOrder(orderId)
            }
        } else {
            showErrorState(GlobalError.NO_CONNECTION)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomDetailComponent::class.java).inject(this)
    }

    private fun observingDetail() {
        somDetailViewModel.orderDetailResult.observe(viewLifecycleOwner, {
            somDetailLoadTimeMonitoring?.startRenderPerformanceMonitoring()
            when (it) {
                is Success -> {
                    isDetailChanged = if (detailResponse == null) false else detailResponse != it.data.getSomDetail
                    detailResponse = it.data.getSomDetail
                    dynamicPriceResponse = it.data.somDynamicPriceResponse
                    renderDetail(it.data.getSomDetail, it.data.somDynamicPriceResponse, it.data.somResolution)
                }
                is Fail -> {
                    it.throwable.showGlobalError()
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_DETAIL)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_ORDER_DETAIL_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    stopLoadTimeMonitoring()
                }
            }
        })
    }

    private fun observingAcceptOrder() {
        somDetailViewModel.acceptOrderResult.observe(viewLifecycleOwner, {
            binding?.btnPrimary?.isLoading = false
            when (it) {
                is Success -> {
                    SomAnalytics.eventClickAcceptOrderPopup(true)
                    acceptOrderResponse = it.data.acceptOrder
                    if (acceptOrderResponse.success == 1) {
                        onSuccessAcceptOrder()
                    } else {
                        showToaster(acceptOrderResponse.listMessage.first(), view, TYPE_ERROR)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_ACCEPTING_ORDER)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.ACCEPT_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    SomAnalytics.eventClickAcceptOrderPopup(false)
                    it.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun observingRejectReasons() {
        somDetailViewModel.rejectReasonResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessRejectReason(it.data)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_REJECT_REASONS)
                    it.throwable.showErrorToaster()
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_REJECT_REASON_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        })
    }

    private fun onSuccessRejectReason(data: SomReasonRejectData.Data) {
        bottomSheetManager?.showSomRejectReasonBottomSheet(data, detailResponse, this)
    }

    private fun observingSetDelivered() {
        somDetailViewModel.setDelivered.observe(viewLifecycleOwner, {
            setLoadingIndicator(false)
            when (it) {
                is Success -> onSuccessSetDelivered(it.data.setDelivered)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_WHEN_SET_DELIVERED)
                    it.throwable.showErrorToaster()
                    bottomSheetManager?.getSomBottomSheetSetDelivered()?.onFailedSetDelivered()
                }
            }
        })
    }

    private fun observingUserRoles() {
        somDetailViewModel.somDetailChatEligibility.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    result.data.let { (isSomDetailEligible, isReplyChatEligible) ->
                        setChatButtonEnabled(isReplyChatEligible)
                        if (isSomDetailEligible) {
                            onUserAllowedToViewSOM()
                        } else {
                            onUserNotAllowedToViewSOM()
                        }
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(result.throwable, String.format(SomConsts.ERROR_GET_USER_ROLES, PAGE_NAME))
                    result.throwable.showErrorToaster()
                    result.throwable.showGlobalError()
                }
            }
        })
    }

    private fun observeRejectCancelOrder() {
        somDetailViewModel.rejectCancelOrderResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    if (result.data.rejectCancelRequest.success == 1) {
                        onSuccessRejectCancelOrder()
                    }
                    showCommonToaster(result.data.rejectCancelRequest.message)
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(result.throwable, SomConsts.ERROR_REJECT_CANCEL_ORDER)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.REJECT_CANCEL_REQUEST_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    result.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun onUserNotAllowedToViewSOM() {
        binding?.progressBarSom?.hide()
        setLoadingIndicator(false)
        refreshHandler?.run {
            setPullEnabled(false)
            finishRefresh()
        }
        binding?.run {
            containerBtnDetail.hide()
            rvDetail.hide()
            somDetailAdminPermissionView.setUserNotAllowedToViewSom {
                doOnUserNotAllowedToViewSOM()
            }
        }
    }

    private fun onUserAllowedToViewSOM() {
        binding?.somDetailAdminPermissionView?.gone()
        somDetailLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
        loadDetail()
    }

    protected open fun doOnUserNotAllowedToViewSOM() {
        activity?.finish()
    }

    protected open fun renderDetail(
        somDetail: SomDetailOrder.Data.GetSomDetail?,
        somDynamicPriceResponse: SomDynamicPriceResponse.GetSomDynamicPrice?,
        resolutionTicketStatusResponse: GetResolutionTicketStatusResponse
        .ResolutionGetTicketStatus.ResolutionData?
    ) {
        showSuccessState()
        renderButtons()
        binding?.rvDetail?.addOneTimeGlobalLayoutListener {
            stopLoadTimeMonitoring()
        }
        somDetailAdapter.setElements(
            SomDetailMapper.mapSomGetOrderDetailResponseToVisitableList(
                somDetail,
                somDynamicPriceResponse,
                resolutionTicketStatusResponse
            )
        )
    }

    private fun renderButtons() {
        // buttons
        if (detailResponse?.button?.isNotEmpty() == true) {
            binding?.containerBtnDetail?.visibility = View.VISIBLE
            detailResponse?.button?.firstOrNull()?.let { buttonResp ->
                binding?.btnPrimary?.apply {
                    text = buttonResp.displayName
                    setOnClickListener {
                        eventClickCtaActionInOrderDetail(buttonResp.displayName, detailResponse?.statusText.orEmpty())
                        when {
                            buttonResp.key.equals(KEY_ACCEPT_ORDER, true) -> {
                                binding?.btnPrimary?.isLoading = true
                                setActionAcceptOrder(buttonResp.displayName, orderId, skipOrderValidation())
                            }
                            buttonResp.key.equals(KEY_TRACK_SELLER, true) -> setActionGoToTrackingPage(buttonResp)
                            buttonResp.key.equals(KEY_REQUEST_PICKUP, true) -> {
                                binding?.btnPrimary?.isLoading = true
                                setActionRequestPickup(buttonResp.displayName)
                            }
                            buttonResp.key.equals(KEY_CONFIRM_SHIPPING, true) -> {
                                binding?.btnPrimary?.isLoading = true
                                setActionConfirmShipping(buttonResp.displayName)
                            }
                            buttonResp.key.equals(KEY_CONFIRM_SHIPPING_AUTO, true) -> {
                                binding?.btnPrimary?.isLoading = true
                                setActionConfirmShippingAuto(buttonResp)
                            }
                            buttonResp.key.equals(KEY_VIEW_COMPLAINT_SELLER, true) -> setActionSeeComplaint(buttonResp.url)
                            buttonResp.key.equals(KEY_BATALKAN_PESANAN, true) -> setActionRejectOrder()
                            buttonResp.key.equals(KEY_ASK_BUYER, true) -> goToAskBuyer()
                            buttonResp.key.equals(KEY_REJECT_ORDER, true) -> setActionRejectOrder()
                            buttonResp.key.equals(KEY_RESPOND_TO_CANCELLATION, true) -> onShowBuyerRequestCancelReasonBottomSheet(buttonResp)
                            buttonResp.key.equals(KEY_UBAH_NO_RESI, true) -> bottomSheetManager?.showSomOrderEditAwbBottomSheet(this@SomDetailFragment)
                            buttonResp.key.equals(KEY_CHANGE_COURIER, true) -> setActionChangeCourier()
                            buttonResp.key.equals(KEY_PRINT_AWB, true) -> SomNavigator.goToPrintAwb(activity, view, listOf(detailResponse?.orderId.orEmpty()), true)
                            buttonResp.key.equals(KEY_ORDER_EXTENSION_REQUEST, true) -> setActionRequestExtension()
                            buttonResp.key.equals(
                                KEY_RETURN_TO_SHIPPER,
                                true
                            ) -> SomNavigator.goToReturnToShipper(this@SomDetailFragment, orderId)
                        }
                    }
                }
            }

            if (detailResponse?.button?.size.orZero() > 1) {
                binding?.btnSecondary?.visibility = View.VISIBLE
                binding?.btnSecondary?.setOnClickListener {
                    val actions = HashMap<String, String>()
                    detailResponse?.button?.filterIndexed { index, _ -> (index != 0) }?.forEach { btn ->
                        actions[btn.key] = btn.displayName
                    }
                    bottomSheetManager?.showSomDetailSecondaryActionBottomSheet(actions, this)
                }
                setupSecondaryButtonBackground()
            } else {
                binding?.btnSecondary?.visibility = View.GONE
            }
        } else {
            binding?.containerBtnDetail?.visibility = View.GONE
        }
    }

    private fun setupSecondaryButtonBackground() {
        binding?.btnSecondary?.apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(ContextCompat.getColor(context, android.R.color.transparent))
                cornerRadius = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
                setStroke(resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width), ContextCompat.getColor(context, R.color._dms_secondary_button_stroke_color))
            }
            setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color._dms_secondary_button_icon_color
                )
            )
        }
    }

    private fun setActionSeeComplaint(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    private fun setActionAcceptOrder(actionName: String, orderId: String, skipOrderValidation: Boolean) {
        if (detailResponse?.flagOrderMeta?.flagFreeShipping == true) {
            showFreeShippingAcceptOrderDialog(orderId)
        } else {
            acceptOrder(actionName, orderId, skipOrderValidation)
        }
    }

    private fun acceptOrder(actionName: String, orderId: String, skipOrderValidation: Boolean) {
        if (!skipOrderValidation) {
            pendingAction = SomPendingAction(actionName, orderId) {
                binding?.btnPrimary?.isLoading = true
                if (orderId.isNotBlank()) {
                    somDetailViewModel.acceptOrder(orderId)
                }
            }
            somDetailViewModel.validateOrders(listOf(orderId))
        } else {
            binding?.btnPrimary?.isLoading = true
            if (orderId.isNotBlank()) {
                somDetailViewModel.acceptOrder(orderId)
            }
        }
    }

    private fun skipOrderValidation(): Boolean {
        return detailResponse?.buyerRequestCancel?.isRequestCancel == true && detailResponse?.buyerRequestCancel?.status == 0
    }

    private fun rejectCancelOrder() {
        if (orderId.isNotBlank()) {
            somDetailViewModel.rejectCancelOrder(orderId)
        }
    }

    private fun setLoadingIndicator(active: Boolean) {
        binding?.swipeRefreshLayout?.isRefreshing = active
    }

    private fun showFreeShippingAcceptOrderDialog(orderId: String) {
        view?.context?.let {
            val dialogUnify = DialogUnify(it, HORIZONTAL_ACTION, NO_IMAGE).apply {
                if (DeviceScreenInfo.isTablet(context)) {
                    dialogMaxWidth = getScreenWidth() / 2
                }
                setUnlockVersion()
                val dialogView = View.inflate(it, R.layout.dialog_accept_order_free_shipping_som, null).apply {
                    val binding = DialogAcceptOrderFreeShippingSomBinding.bind(this)
                    val msgReguler1 = getString(R.string.confirm_msg_1a)
                    val msgBold1 = getString(R.string.confirm_msg_1b)
                    val str1 = SpannableString("$msgReguler1 $msgBold1")
                    str1.setSpan(StyleSpan(Typeface.BOLD), msgReguler1.length + 1, str1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.labelConfirmationMsg1.text = str1

                    val msgReguler2 = getString(R.string.confirm_msg_2a)
                    val msgBold2 = getString(R.string.confirm_msg_2b)
                    val str2 = SpannableString("$msgReguler2 $msgBold2")
                    str2.setSpan(StyleSpan(Typeface.BOLD), msgReguler2.length + 1, str2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.labelConfirmationMsg2.text = str2

                    val msg3 = getString(R.string.confirm_msg_3)
                    binding.labelConfirmationMsg3.text = msg3

                    binding.btnBatal.setOnClickListener { dismiss() }
                    binding.btnTerima.setOnClickListener {
                        if (orderId.isNotBlank()) {
                            somDetailViewModel.acceptOrder(orderId)
                            dismiss()
                        }
                    }
                }
                setChild(dialogView)

                setAcceptOrderFreeShippingDialogDismissListener()
            }
            dialogUnify.show()
        }
    }

    private fun setActionGoToTrackingPage(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", detailResponse?.orderId.orEmpty())
        val uriBuilder = Uri.Builder()
        val decodedUrl = if (buttonResp.url.startsWith(SomConsts.PREFIX_HTTPS)) {
            buttonResp.url
        } else {
            URLDecoder.decode(buttonResp.url, SomConsts.ENCODING_UTF_8)
        }
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, decodedUrl)
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_CALLER, PARAM_SELLER)
        routingAppLink += uriBuilder.toString()
        RouteManager.route(context, routingAppLink)
    }

    private fun setActionRequestPickup(actionName: String) {
        if (!skipOrderValidation()) {
            pendingAction = SomPendingAction(actionName, orderId) {
                requestPickUp()
            }
            somDetailViewModel.validateOrders(listOf(orderId))
        } else {
            requestPickUp()
        }
    }

    protected open fun requestPickUp() {
        binding?.btnPrimary?.isLoading = true
        SomNavigator.goToRequestPickupPage(this, orderId)
    }

    private fun setActionConfirmShipping(actionName: String) {
        if (!skipOrderValidation()) {
            pendingAction = SomPendingAction(actionName, orderId) {
                confirmShipping()
            }
            somDetailViewModel.validateOrders(listOf(orderId))
        } else {
            confirmShipping()
        }
    }
    private fun setActionConfirmShippingAuto(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        val popUp = buttonResp.popUp
        if (popUp.template?.code != null) {
            SomConfirmShippingBottomSheet.instance(context, view, popUp)
        }
        binding?.btnPrimary?.isLoading = false
    }

    private fun confirmShipping() {
        context?.let { context ->
            binding?.btnPrimary?.isLoading = true
            createIntentConfirmShipping(false)
        }
    }

    override fun onBottomSheetItemClick(key: String) {
        bottomSheetManager?.getSecondaryActionBottomSheet()?.setOneTimeOnDismiss {
            detailResponse?.button?.forEach {
                if (key.equals(it.key, true)) {
                    eventClickSecondaryActionInOrderDetail(
                        it.displayName,
                        detailResponse?.statusCode.toString(),
                        detailResponse?.statusText.orEmpty(),
                        userSession.shopId
                    )
                    when {
                        key.equals(KEY_TRACK_SELLER, true) -> setActionGoToTrackingPage(it)
                        key.equals(KEY_REJECT_ORDER, true) -> setActionRejectOrder()
                        key.equals(KEY_BATALKAN_PESANAN, true) -> setActionRejectOrder()
                        key.equals(KEY_UBAH_NO_RESI, true) -> bottomSheetManager?.showSomOrderEditAwbBottomSheet(this)
                        key.equals(KEY_UPLOAD_AWB, true) -> setActionUploadAwb(it)
                        key.equals(KEY_CHANGE_COURIER, true) -> setActionChangeCourier()
                        key.equals(KEY_ACCEPT_ORDER, true) -> setActionAcceptOrder(it.displayName, orderId, skipOrderValidation())
                        key.equals(KEY_ASK_BUYER, true) -> goToAskBuyer()
                        key.equals(KEY_SET_DELIVERED, true) -> bottomSheetManager?.showSomBottomSheetSetDelivered(this)
                        key.equals(KEY_PRINT_AWB, true) -> SomNavigator.goToPrintAwb(activity, view, listOf(detailResponse?.orderId.orEmpty()), true)
                        key.equals(KEY_ORDER_EXTENSION_REQUEST, true) -> setActionRequestExtension()
                        key.equals(KEY_RESCHEDULE_PICKUP, true) -> goToReschedulePickupPage(this, orderId)
                        key.equals(
                            KEY_RETURN_TO_SHIPPER,
                            true
                        ) -> SomNavigator.goToReturnToShipper(this@SomDetailFragment, orderId)
                        key.equals(KEY_SEARCH_NEW_DRIVER, true) -> SomNavigator.goToFindNewDriver(this, orderId, detailResponse?.invoice)
                    }
                }
            }
        }
        bottomSheetManager?.dismissSecondaryActionBottomSheet()
    }

    private fun setActionRequestExtension() {
        orderExtensionViewModel.getSomOrderExtensionRequestInfoLoadingState()
    }

    private fun setActionChangeCourier() {
        createIntentConfirmShipping(true)
    }

    private fun setActionUploadAwb(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        openWebview(buttonResp.url)
    }

    protected open fun createIntentConfirmShipping(isChangeShipping: Boolean) {
        if (isChangeShipping) {
            goToChangeCourierPage(this, orderId)
        } else {
            goToConfirmShippingPage(this, orderId)
        }
    }

    private fun setActionRejectOrder() {
        somDetailViewModel.getRejectReasons()
    }

    private fun doEditAwb(shippingRef: String) {
        somDetailViewModel.editAwb(orderId, shippingRef)
    }

    private fun observingEditAwb() {
        somDetailViewModel.editRefNumResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    successEditAwbResponse = it.data
                    if (successEditAwbResponse.mpLogisticEditRefNum.listMessage.isNotEmpty()) {
                        onSuccessEditAwb()
                    } else {
                        showToaster(getString(R.string.global_error), view, TYPE_ERROR)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_EDIT_AWB)
                    failEditAwbResponse.message = context?.run {
                        SomErrorHandler.getErrorMessage(it.throwable, this)
                    }.orEmpty()
                    if (failEditAwbResponse.message.isNotEmpty()) {
                        showToaster(failEditAwbResponse.message, view, TYPE_ERROR)
                    } else {
                        it.throwable.showErrorToaster()
                    }
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.CHANGE_AWB_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        })
    }

    override fun onRejectReasonItemClick(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        bottomSheetManager?.getSomRejectReasonBottomSheet()?.setOneTimeOnDismiss {
            when (rejectReason.reasonCode) {
                SomBottomSheetRejectReasonsAdapter.REJECT_REASON_PRODUCT_EMPTY -> {
                    bottomSheetManager?.showSomBottomSheetProductEmpty(
                        rejectReason,
                        detailResponse,
                        orderId,
                        this
                    )
                }
                SomBottomSheetRejectReasonsAdapter.REJECT_REASON_SHOP_CLOSED -> {
                    bottomSheetManager?.showSomBottomSheetShopClosed(
                        rejectReason,
                        orderId,
                        this,
                        childFragmentManager
                    )
                }
                SomBottomSheetRejectReasonsAdapter.REJECT_REASON_COURIER_PROBLEMS -> {
                    bottomSheetManager?.showSomBottomSheetCourierProblem(
                        rejectReason,
                        orderId,
                        this
                    )
                }
                SomBottomSheetRejectReasonsAdapter.REJECT_REASON_BUYER_NO_RESPONSE -> {
                    bottomSheetManager?.showSomBottomSheetBuyerNoResponse(
                        rejectReason,
                        orderId,
                        this
                    )
                }
                SomBottomSheetRejectReasonsAdapter.REJECT_REASON_OTHER_REASON -> {
                    bottomSheetManager?.showSomBottomSheetBuyerOtherReason(
                        rejectReason,
                        orderId,
                        this
                    )
                }
            }
        }
        bottomSheetManager?.getSomRejectReasonBottomSheet()?.dismiss()
    }

    override fun onTextCopied(label: String, str: String, readableDataName: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, str))
        showCommonToaster(getString(R.string.message_success_copy, readableDataName))
    }

    override fun onInvalidResiUpload(awbUploadUrl: String) {
        openWebview(awbUploadUrl)
    }

    override fun onSeeInvoice(invoiceUrl: String, invoice: String) {
        SomAnalytics.eventClickViewInvoice(detailResponse?.statusCode?.toString().orEmpty(), detailResponse?.statusText.orEmpty())
        Intent(activity, SomSeeInvoiceActivity::class.java).apply {
            putExtra(KEY_URL, invoiceUrl)
            putExtra(PARAM_INVOICE, invoice)
            putExtra(KEY_TITLE, requireActivity().getString(R.string.title_som_invoice))
            putExtra(PARAM_ORDER_CODE, detailResponse?.statusCode.toString())
            startActivity(this)
        }
    }

    override fun onCopiedInvoice(invoice: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(invoice, str))
        showCommonToaster(getString(R.string.invoice_tersalin))
    }

    override fun onCopiedAddress(address: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(address, str))
        showCommonToaster(getString(R.string.alamat_pengiriman_tersalin))
    }

    override fun onCopyAddOnDescription(label: String, description: CharSequence) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, description))
        showCommonToaster(getString(R.string.som_detail_add_on_description_copied_message))
    }

    override fun onResoClicked(redirectPath: String) {
        SomNavigator.openAppLink(context, redirectPath)
        SomAnalytics.sendClickOnResolutionWidgetEvent(userSession.userId)
    }

    private fun doRejectOrder(orderRejectRequestParam: SomRejectRequestParam) {
        activity?.resources?.let {
            somDetailViewModel.rejectOrder(orderRejectRequestParam)
        }
        SomAnalytics.eventClickTolakPesanan(detailResponse?.statusText.orEmpty(), orderRejectRequestParam.reason)
    }

    private fun observingRejectOrder() {
        somDetailViewModel.rejectOrderResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessRejectOrder(it.data.rejectOrder)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_REJECT_ORDER)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.REJECT_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    it.throwable.showErrorToaster()
                }
            }
        }
    }

    override fun onDialPhone(strPhoneNo: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            val phone = "tel:$strPhoneNo"
            intent.data = Uri.parse(phone)
            startActivity(intent)
        } catch (t: Throwable) {
            t.showErrorToaster()
        }
    }

    override fun onShowInfoLogisticAll(logisticInfoList: List<SomDetailOrder.Data.GetSomDetail.LogisticInfo.All>) {
        startActivity(
            Intent(activity, SomDetailLogisticInfoActivity::class.java).apply {
                val logisticInfo = LogisticInfoAllWrapper(ArrayList(logisticInfoList))
                val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
                cacheManager?.put(SomDetailLogisticInfoFragment.KEY_SOM_LOGISTIC_INFO_ALL, logisticInfo)
                putExtra(KEY_ID_CACHE_MANAGER_INFO_ALL, cacheManager?.id)
            }
        )
    }

    override fun onShowBookingCode(bookingCode: String, bookingType: String) {
        Intent(activity, SomDetailBookingCodeActivity::class.java).apply {
            putExtra(PARAM_BOOKING_CODE, detailResponse?.bookingInfo?.onlineBooking?.bookingCode)
            putExtra(PARAM_BARCODE_TYPE, detailResponse?.bookingInfo?.onlineBooking?.barcodeType)
            startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding?.btnPrimary?.isLoading = false
        if (requestCode == SomNavigator.REQUEST_CONFIRM_REQUEST_PICKUP) {
            handleRequestPickUpResult(resultCode, data)
        } else if (requestCode == SomNavigator.REQUEST_CONFIRM_SHIPPING || requestCode == SomNavigator.REQUEST_CHANGE_COURIER) {
            handleChangeCourierAndConfirmShippingResult(resultCode, data)
        } else if (requestCode == SomNavigator.REQUEST_RESCHEDULE_PICKUP) {
            handleReschedulePickupResult(resultCode, data)
        } else if (requestCode == SomNavigator.REQUEST_RETURN_TO_SHIPPER) {
            handleReturnToShipperResult(resultCode, data)
        } else if (requestCode == SomNavigator.REQUEST_FIND_NEW_DRIVER) {
            handleFindNewDriverResult(resultCode, data)
        }
    }

    protected open fun handleReturnToShipperResult(resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                finishAndRefreshSomList()
            }
            Activity.RESULT_FIRST_USER -> {
                loadDetail()
            }
        }
    }

    private fun finishAndRefreshSomList() {
        activity?.run {
            val result = Intent().putExtra(SomConsts.RESULT_REFRESH_ORDER, true)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    override fun onClickProduct(orderDetailId: Long) {
        val appLinkSnapShot = "${ApplinkConst.SNAPSHOT_ORDER}/$orderId/$orderDetailId"
        val intent = RouteManager.getIntent(activity, appLinkSnapShot)
        intent.putExtra(ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM, true)
        startActivity(intent)
        SomAnalytics.clickProductNameToSnapshot(detailResponse?.statusText.orEmpty(), userSession.userId.orEmpty())
    }

    override fun onRefresh(view: View?) {
        if (isShowDetailEligible(somDetailViewModel.somDetailChatEligibility.value)) {
            loadDetail()
        } else {
            checkUserRole()
        }
    }

    override fun onDoRejectOrder(orderRejectRequest: SomRejectRequestParam) {
        bottomSheetManager?.dismissBottomSheets()
        doRejectOrder(orderRejectRequest)
    }

    private fun openWebview(url: String) {
        startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, url))
    }

    private fun isShowDetailEligible(value: Result<Pair<Boolean, Boolean>>?): Boolean = (value as? Success)?.data?.first == true

    private fun Throwable.showGlobalError() {
        val type = if (this is UnknownHostException || this is SocketTimeoutException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
        showErrorState(type)
    }

    protected fun Throwable.showErrorToaster() {
        if (this is UnknownHostException || this is SocketTimeoutException) {
            showNoInternetConnectionToaster()
        } else {
            showServerErrorToaster()
        }
    }

    private fun showNoInternetConnectionGlobalError() {
        binding?.run {
            somGlobalError.setType(GlobalError.NO_CONNECTION)
            somGlobalError.show()
        }
    }

    private fun showServerErrorGlobalError() {
        binding?.run {
            somGlobalError.setType(GlobalError.SERVER_ERROR)
            somGlobalError.show()
        }
    }

    protected fun showErrorToaster(message: String) {
        view?.run {
            this@SomDetailFragment.somToaster = Toaster.build(
                this,
                message,
                LENGTH_SHORT,
                TYPE_ERROR
            ).setAnchorView(binding?.containerBtnDetail)
            this@SomDetailFragment.somToaster?.show()
        }
    }

    private fun showNoInternetConnectionToaster() {
        showErrorToaster(getString(R.string.som_error_message_no_internet_connection))
    }

    private fun showServerErrorToaster() {
        showErrorToaster(getString(R.string.som_error_message_server_fault))
    }

    protected fun showCommonToaster(message: String) {
        view?.run {
            this@SomDetailFragment.somToaster = Toaster.build(
                this,
                message,
                LENGTH_SHORT,
                TYPE_NORMAL
            ).setAnchorView(binding?.containerBtnDetail)
            this@SomDetailFragment.somToaster?.show()
        }
    }

    protected fun showLoading() {
        binding?.run {
            progressBarSom.show()
            rvDetail.hide()
            somGlobalError.hide()
            containerBtnDetail.hide()
        }
        setLoadingIndicator(true)
        refreshHandler?.finishRefresh()
    }

    protected fun showErrorState(type: Int) {
        when (type) {
            GlobalError.NO_CONNECTION -> showNoInternetConnectionGlobalError()
            GlobalError.SERVER_ERROR -> showServerErrorGlobalError()
            else -> showNoInternetConnectionGlobalError()
        }
        binding?.run {
            progressBarSom.hide()
            rvDetail.hide()
            containerBtnDetail.hide()
        }
        setLoadingIndicator(false)
        refreshHandler?.finishRefresh()
    }

    protected open fun handleRequestPickUpResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
            val message = data.getStringExtra(RESULT_PROCESS_REQ_PICKUP).orEmpty()
            showCommonToaster(message)
        }

        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
            val message = data.getStringExtra(RESULT_PROCESS_REQ_PICKUP).orEmpty()
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(RESULT_PROCESS_REQ_PICKUP, message)
                }
            )
            activity?.finish()
        }
    }

    protected open fun handleChangeCourierAndConfirmShippingResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(RESULT_CONFIRM_SHIPPING)) {
            val resultConfirmShippingMsg = data.getStringExtra(RESULT_CONFIRM_SHIPPING)
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(RESULT_CONFIRM_SHIPPING, resultConfirmShippingMsg)
                }
            )
            activity?.finish()
        }
    }

    protected open fun handleReschedulePickupResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent())
                finish()
            }
        }
    }

    protected open fun handleFindNewDriverResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            loadDetail()
        }
    }

    private fun showSuccessState() {
        binding?.run {
            rvDetail.show()
            containerBtnDetail.show()
            progressBarSom.hide()
            somGlobalError.hide()
        }
        setLoadingIndicator(false)
        refreshHandler?.finishRefresh()
    }

    private fun setupBackgroundColor() {
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                binding?.run {
                    somDetailToolbar.title = getString(R.string.title_som_detail)
                    somDetailToolbar.isShowBackButton = showBackButton()
                    somDetailToolbar.setNavigationOnClickListener {
                        onBackPressed()
                    }
                }
            }
        }
    }

    private fun setChatButtonEnabled(isEnabled: Boolean) {
        chatIcon.showWithCondition(isEnabled)
    }

    private fun getActivityPltPerformanceMonitoring() {
        somDetailLoadTimeMonitoring = (activity as? SomDetailActivity)?.somDetailLoadTimeMonitoring
    }

    private fun stopLoadTimeMonitoring() {
        somDetailLoadTimeMonitoring?.stopRenderPerformanceMonitoring()
        (activity as? SomDetailActivity)?.somLoadTimeMonitoringListener?.onStopPltMonitoring()
    }

    private fun observeValidateOrder() {
        somDetailViewModel.validateOrderResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> onSuccessValidateOrder(result.data)
                is Fail -> {
                    onFailedValidateOrder()
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.VALIDATE_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        })
    }

    protected open fun observeOrderExtensionRequestInfo() {
        orderExtensionViewModel.orderExtensionRequestInfo.observe(viewLifecycleOwner) { result ->
            if (result.message.isNotBlank() || result.throwable != null) {
                if (result.success) {
                    showCommonToaster(result.message)
                } else {
                    if (result.throwable == null) {
                        showErrorToaster(result.message)
                    } else {
                        result.throwable?.showErrorToaster()
                    }
                }
            }
            if (result.completed && result.refreshOnDismiss) {
                loadDetail()
            }
            onRequestExtensionInfoChanged(result)
        }
    }

    protected fun onRequestExtensionInfoChanged(data: OrderExtensionRequestInfoUiModel) {
        bottomSheetManager?.showSomBottomSheetOrderExtensionRequest(
            data,
            orderId,
            orderExtensionViewModel
        )
    }

    private fun onFailedValidateOrder() {
        showToaster(getString(R.string.som_error_validate_order), view, TYPE_ERROR)
    }

    private fun onSuccessValidateOrder(valid: Boolean) {
        val pendingAction = pendingAction ?: return
        if (valid) {
            pendingAction.action.invoke()
        } else {
            context?.let { context ->
                binding?.btnPrimary?.isLoading = false
                val somOrderHasCancellationRequestDialog = somOrderHasCancellationRequestDialog
                    ?: SomOrderHasRequestCancellationDialog(context)
                this.somOrderHasCancellationRequestDialog = somOrderHasCancellationRequestDialog
                somOrderHasCancellationRequestDialog.apply {
                    setupActionButton(pendingAction.actionName, pendingAction.action)
                    setupGoToOrderDetailButton(::onGoToOrderDetailButtonClicked)
                    show()
                }
            }
        }
    }

    private fun DialogUnify.setAcceptOrderFreeShippingDialogDismissListener() {
        setOnDismissListener {
            binding?.btnPrimary?.isLoading = false
        }
    }

    private fun getOrderIdExtra(): String {
        return arguments?.getString(PARAM_ORDER_ID)?.takeIf { it.isNotBlank() } ?: SomConsts.DEFAULT_INVALID_ORDER_ID
    }

    protected open fun onGoToOrderDetailButtonClicked() {
        loadDetail()
    }

    protected open fun onSuccessAcceptOrder() {
        activity?.setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(RESULT_ACCEPT_ORDER, this@SomDetailFragment.acceptOrderResponse)
            }
        )
        activity?.finish()
    }

    protected open fun onSuccessRejectCancelOrder() {
        loadDetail()
    }

    protected open fun onSuccessEditAwb() {
        showCommonToaster(successEditAwbResponse.mpLogisticEditRefNum.listMessage.first())
        loadDetail()
    }

    protected open fun onSuccessRejectOrder(rejectOrderData: SomRejectOrderResponse.Data.RejectOrder) {
        if (rejectOrderData.success == 1) {
            // if success = 1 : finishActivity, then show toaster
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(RESULT_REJECT_ORDER, rejectOrderData)
                }
            )
            activity?.finish()
        } else {
            showToaster(rejectOrderData.message.firstOrNull() ?: getString(R.string.global_error), view, TYPE_ERROR)
        }
    }

    protected open fun onSuccessSetDelivered(deliveredData: SetDelivered) {
        if (deliveredData.success == SomConsts.SOM_SET_DELIVERED_SUCCESS_CODE) {
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(RESULT_SET_DELIVERED, getString(R.string.message_set_delivered_success))
                }
            )
            activity?.finish()
        } else {
            val message = deliveredData.message.joinToString().takeIf { it.isNotBlank() }
                ?: getString(R.string.global_error)
            showToaster(message, view, TYPE_ERROR, "")
            bottomSheetManager?.getSomBottomSheetSetDelivered()?.onFailedSetDelivered()
        }
    }

    protected open fun showBackButton(): Boolean = true

    protected fun showToaster(message: String, view: View?, type: Int, action: String = ACTION_OK) {
        val toasterError = Toaster
        view?.let { v ->
            if (action.isBlank()) {
                toasterError.build(v, message, LENGTH_SHORT, type).show()
            } else {
                toasterError.build(v, message, LENGTH_SHORT, type, action).show()
            }
        }
    }

    protected open fun doOnResume() {
        updateShopActive()
    }

    protected fun getAdapterTypeFactory(): SomDetailAdapterFactoryImpl {
        return SomDetailAdapterFactoryImpl(this, recyclerViewSharedPool)
    }
}
