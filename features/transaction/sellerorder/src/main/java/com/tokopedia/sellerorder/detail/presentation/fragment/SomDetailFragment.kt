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
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
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
import com.tokopedia.kotlin.extensions.view.*
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
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderEditAwbBottomSheet
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderRequestCancelBottomSheet
import com.tokopedia.sellerorder.common.presenter.dialogs.SomOrderHasRequestCancellationDialog
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.presenter.model.SomPendingAction
import com.tokopedia.sellerorder.common.util.SomConnectionMonitor
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.ACTION_OK
import com.tokopedia.sellerorder.common.util.SomConsts.ATTRIBUTE_ID
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_HEADER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PAYMENT_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PRODUCTS_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_SHIPPING_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ASK_BUYER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_BATALKAN_PESANAN
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CHANGE_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_PRINT_AWB
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RESPOND_TO_CANCELLATION
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
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.setUserNotAllowedToViewSom
import com.tokopedia.sellerorder.detail.analytic.performance.SomDetailLoadTimeMonitoring
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailBookingCodeActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailLogisticInfoActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomSeeInvoiceActivity
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.*
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailLogisticInfoFragment.Companion.KEY_ID_CACHE_MANAGER_INFO_ALL
import com.tokopedia.sellerorder.detail.presentation.model.LogisticInfoAllWrapper
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*
import kotlinx.android.synthetic.main.dialog_accept_order_free_shipping_som.view.*
import kotlinx.android.synthetic.main.fragment_som_detail.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**x
 * Created by fwidjaja on 2019-09-30.
 */
open class SomDetailFragment : BaseDaggerFragment(),
        RefreshHandler.OnRefreshHandlerListener,
        SomBottomSheetRejectOrderAdapter.ActionListener,
        SomDetailAdapter.ActionListener,
        SomBottomSheetRejectReasonsAdapter.ActionListener,
        SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener {

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
    private var listDetailData: ArrayList<SomDetailData> = arrayListOf()
    private var somDetailLoadTimeMonitoring: SomDetailLoadTimeMonitoring? = null
    private lateinit var somDetailAdapter: SomDetailAdapter
    private var refreshHandler: RefreshHandler? = null

    private var somOrderHasCancellationRequestDialog: SomOrderHasRequestCancellationDialog? = null
    private var secondaryBottomSheet: SomDetailSecondaryActionBottomSheet? = null
    private var orderRequestCancelBottomSheet: SomOrderRequestCancelBottomSheet? = null
    private var somRejectReasonBottomSheet: SomRejectReasonBottomSheet? = null
    private var somProductEmptyBottomSheet: SomBottomSheetProductEmpty? = null
    private var somShopClosedBottomSheet: SomBottomSheetShopClosed? = null
    private var bottomSheetCourierProblems: SomBottomSheetCourierProblem? = null
    private var bottomSheetBuyerNoResponse: SomBottomSheetBuyerNoResponse? = null
    private var bottomSheetBuyerOtherReason: SomBottomSheetBuyerOtherReason? = null

    private var pendingAction: SomPendingAction? = null

    protected var orderId = ""
    protected var detailResponse: SomDetailOrder.Data.GetSomDetail? = SomDetailOrder.Data.GetSomDetail()

    protected val somDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomDetailViewModel::class.java]
    }
    private val chatIcon: IconUnify by lazy {
        createChatIcon(requireContext())
    }

    private fun createChatIcon(context: Context): IconUnify {
        return IconUnify(requireContext(), IconUnify.CHAT).apply {
            setOnClickListener {
                doClickChat()
            }
            layoutParams = LinearLayout.LayoutParams(
                    context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toInt(),
                    context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toInt()).apply {
                setMargins(0, 0, context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toInt(), 0)
            }
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            setBackgroundResource(outValue.resourceId)
            gone()
        }
    }

    protected val connectionMonitor by lazy { context?.run { SomConnectionMonitor(this) } }

    companion object {

        private const val ERROR_GET_ORDER_DETAIL = "Error when get order detail."
        private const val ERROR_ACCEPTING_ORDER = "Error when accepting order."
        private const val ERROR_GET_ORDER_REJECT_REASONS = "Error when get order reject reasons."
        private const val ERROR_WHEN_SET_DELIVERED = "Error when set order status to delivered."
        private const val ERROR_EDIT_AWB = "Error when edit AWB."
        private const val ERROR_REJECT_ORDER = "Error when rejecting order."
        private const val PAGE_NAME = "seller order detail page."

        private const val TAG_BOTTOMSHEET = "bottomSheet"

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
        val urlInvoice = detailResponse?.invoiceUrl.orEmpty()
        val invoiceUri = Uri.parse(urlInvoice)
        val invoiceId = invoiceUri.getQueryParameter(ATTRIBUTE_ID)
        val intent = RouteManager.getIntent(activity,
                ApplinkConst.TOPCHAT_ASKBUYER,
                detailResponse?.customer?.id.orEmpty(), "",
                PARAM_SOURCE_ASK_BUYER, detailResponse?.customer?.name, detailResponse?.customer?.image).apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, invoiceId)
            putExtra(ApplinkConst.Chat.INVOICE_CODE, detailResponse?.invoice)

            if (detailResponse?.listProduct?.isNotEmpty() == true) {
                putExtra(ApplinkConst.Chat.INVOICE_TITLE, detailResponse?.listProduct?.firstOrNull()?.name.orEmpty())
                putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, detailResponse?.listProduct?.firstOrNull()?.thumbnail.orEmpty())
            }
            putExtra(ApplinkConst.Chat.INVOICE_DATE, detailResponse?.paymentDate)
            putExtra(ApplinkConst.Chat.INVOICE_URL, detailResponse?.invoiceUrl)
            putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, detailResponse?.statusCode?.toString())
            putExtra(ApplinkConst.Chat.INVOICE_STATUS, detailResponse?.statusText)
            putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, dynamicPriceResponse?.paymentData?.value)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityPltPerformanceMonitoring()
        if (arguments != null) {
            orderId = arguments?.getString(PARAM_ORDER_ID).toString()
            somDetailLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
            checkUserRole()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_som_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
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
    }

    override fun onPause() {
        super.onPause()
        dismissBottomSheets()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionMonitor?.end()
    }

    override fun onShowBuyerRequestCancelReasonBottomSheet(it: SomDetailOrder.Data.GetSomDetail.Button) {
        showBuyerRequestCancelBottomSheet(it)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        som_detail_toolbar?.addCustomRightContent(chatIcon)
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
        refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        refreshHandler?.setPullEnabled(true)
        somDetailAdapter = SomDetailAdapter().apply {
            setActionListener(this@SomDetailFragment)
        }
        rv_detail?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = somDetailAdapter
        }
        somGlobalError?.setActionClickListener {
            if (isShowDetailEligible(somDetailViewModel.somDetailChatEligibility.value)) {
                loadDetail()
            } else {
                checkUserRole()
            }
        }
    }

    protected open fun loadDetail() {
        showLoading()
        if (connectionMonitor?.isConnected == true) {
            activity?.let {
                SomAnalytics.sendScreenName(SomConsts.DETAIL_ORDER_SCREEN_NAME + orderId)
                it.resources?.let { r ->
                    somDetailViewModel.loadDetailOrder(orderId)
                }
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
        somDetailViewModel.orderDetailResult.observe(viewLifecycleOwner, Observer {
            somDetailLoadTimeMonitoring?.startRenderPerformanceMonitoring()
            when (it) {
                is Success -> {
                    isDetailChanged = if (detailResponse == null) false else detailResponse != it.data.getSomDetail
                    detailResponse = it.data.getSomDetail
                    dynamicPriceResponse = it.data.somDynamicPriceResponse
                    renderDetail()
                }
                is Fail -> {
                    it.throwable.showGlobalError()
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_DETAIL)
                    stopLoadTimeMonitoring()
                }
            }
        })
    }

    private fun observingAcceptOrder() {
        somDetailViewModel.acceptOrderResult.observe(viewLifecycleOwner, Observer {
            btn_primary?.isLoading = false
            when (it) {
                is Success -> {
                    SomAnalytics.eventClickAcceptOrderPopup(true)
                    acceptOrderResponse = it.data.acceptOrder
                    if (acceptOrderResponse.success == 1) {
                        onSuccessAcceptOrder()
                    } else {
                        showToasterError(acceptOrderResponse.listMessage.first(), view)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_ACCEPTING_ORDER)
                    SomAnalytics.eventClickAcceptOrderPopup(false)
                    it.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun observingRejectReasons() {
        somDetailViewModel.rejectReasonResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRejectReason(it.data)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_REJECT_REASONS)
                    it.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun onSuccessRejectReason(data: SomReasonRejectData.Data) {
        showRejectReasonBottomSheet(data)
    }

    private fun showRejectReasonBottomSheet(data: SomReasonRejectData.Data) {
        context?.let { context ->
            view.let { view ->
                if (view is ViewGroup) {
                    val bottomSheetRejectReason = somRejectReasonBottomSheet ?: SomRejectReasonBottomSheet(context, this)
                    somRejectReasonBottomSheet = bottomSheetRejectReason
                    somRejectReasonBottomSheet?.apply {
                        init(view)
                        setupTicker(detailResponse?.penaltyRejectInfo?.isPenaltyReject ?: false, detailResponse?.penaltyRejectInfo?.penaltyRejectWording.orEmpty())
                        setReasons(data.listSomRejectReason.toMutableList())
                        show()
                    }
                }
            }
        }
    }

    private fun observingSetDelivered() {
        somDetailViewModel.setDelivered.observe(viewLifecycleOwner, Observer {
            setLoadingIndicator(false)
            when (it) {
                is Success -> onSuccessSetDelivered(it.data.setDelivered)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_WHEN_SET_DELIVERED)
                    it.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun observingUserRoles() {
        somDetailViewModel.somDetailChatEligibility.observe(viewLifecycleOwner, Observer { result ->
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
        somDetailViewModel.rejectCancelOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    if (result.data.rejectCancelRequest.success == 1) {
                        onSuccessRejectCancelOrder()
                    }
                    showCommonToaster(result.data.rejectCancelRequest.message)
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(result.throwable, SomConsts.ERROR_REJECT_CANCEL_ORDER)
                    result.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun onUserNotAllowedToViewSOM() {
        progressBarSom?.hide()
        setLoadingIndicator(false)
        refreshHandler?.run {
            setPullEnabled(false)
            finishRefresh()
        }
        containerBtnDetail?.hide()
        rv_detail?.hide()
        somDetailAdminPermissionView?.setUserNotAllowedToViewSom {
            doOnUserNotAllowedToViewSOM()
        }
    }

    private fun onUserAllowedToViewSOM() {
        somDetailAdminPermissionView?.gone()
        somDetailLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
        loadDetail()
    }

    protected open fun doOnUserNotAllowedToViewSOM() {
        activity?.finish()
    }

    protected open fun renderDetail() {
        showSuccessState()
        listDetailData = arrayListOf()
        somDetailAdapter.listDataDetail = arrayListOf()
        renderHeader()
        renderProducts()
        renderShipment()
        renderPayment()
        renderButtons()

        somDetailAdapter.listDataDetail = listDetailData.toMutableList()
        rv_detail.addOneTimeGlobalLayoutListener {
            stopLoadTimeMonitoring()
        }
        somDetailAdapter.notifyDataSetChanged()
    }

    private fun renderHeader() {
        // header
        detailResponse?.run {
            val dataHeader = SomDetailHeader(
                    statusCode,
                    statusText,
                    statusIndicatorColor,
                    buyerRequestCancel.isRequestCancel,
                    invoice,
                    invoiceUrl,
                    paymentDate,
                    customer.name,
                    deadline.text,
                    deadline.color,
                    listLabelInfo,
                    orderId,
                    shipment.awbUploadUrl,
                    shipment.awbUploadProofText,
                    bookingInfo.onlineBooking.bookingCode,
                    bookingInfo.onlineBooking.state,
                    bookingInfo.onlineBooking.barcodeType,
                    warehouse.fullFillBy,
                    flagOrderMeta.isWareHouse,
                    tickerInfo)
            listDetailData.add(SomDetailData(dataHeader, DETAIL_HEADER_TYPE))
        }
    }

    private fun renderProducts() {
        // products
        detailResponse?.run {
            val dataProducts = SomDetailProducts(listProduct, flagOrderMeta.isTopAds, flagOrderMeta.isBroadcastChat)
            listDetailData.add(SomDetailData(dataProducts, DETAIL_PRODUCTS_TYPE))
        }
    }

    private fun renderShipment() {
        // shipping
        detailResponse?.run {
            val dataShipping = SomDetailShipping(
                    shipment.name + " - " + shipment.productName,
                    receiverName = receiver.name,
                    receiverPhone = receiver.phone,
                    receiverStreet = receiver.street,
                    receiverDistrict = receiver.district + ", " + receiver.city + " " + receiver.postal,
                    receiverProvince = receiver.province,
                    isFreeShipping = flagOrderMeta.flagFreeShipping,
                    driverPhoto = bookingInfo.driver.photo,
                    driverName = bookingInfo.driver.name,
                    driverPhone = bookingInfo.driver.phone,
                    dropshipperName = dropshipper.name,
                    dropshipperPhone = dropshipper.phone,
                    driverLicense = bookingInfo.driver.licenseNumber,
                    onlineBookingCode = bookingInfo.onlineBooking.bookingCode,
                    onlineBookingState = bookingInfo.onlineBooking.state,
                    onlineBookingMsg = bookingInfo.onlineBooking.message,
                    onlineBookingMsgArray = bookingInfo.onlineBooking.messageArray,
                    onlineBookingType = bookingInfo.onlineBooking.barcodeType,
                    isRemoveAwb = onlineBooking.isRemoveInputAwb,
                    awb = shipment.awb,
                    awbTextColor = shipment.awbTextColor,
                    isShippingPrinted = flagOrderMeta.isShippingPrinted,
                    logisticInfo = logisticInfo
            )
            listDetailData.add(SomDetailData(dataShipping, DETAIL_SHIPPING_TYPE))
        }

    }

    private fun renderPayment() {
        val paymentData = SomDetailPayments.PaymentDataUiModel(
                label = dynamicPriceResponse?.paymentData?.label.orEmpty(),
                value = dynamicPriceResponse?.paymentData?.value.orEmpty(),
                textColor = dynamicPriceResponse?.paymentData?.textColor.orEmpty())

        val paymentMethodList = mutableListOf<SomDetailPayments.PaymentMethodUiModel>()
        dynamicPriceResponse?.paymentMethod?.map {
            paymentMethodList.add(SomDetailPayments.PaymentMethodUiModel(label = it.label, value = it.value))
        }

        val pricingList = mutableListOf<SomDetailPayments.PricingData>()
        dynamicPriceResponse?.pricingData?.map {
            pricingList.add(SomDetailPayments.PricingData(label = it.label, value = it.value))
        }

        val dataPayments = SomDetailPayments(paymentDataUiModel = paymentData,
                paymentMethodUiModel = paymentMethodList, pricingData = pricingList)

        listDetailData.add(SomDetailData(dataPayments, DETAIL_PAYMENT_TYPE))
    }

    private fun renderButtons() {
        // buttons
        if (detailResponse?.button?.isNotEmpty() == true) {
            containerBtnDetail?.visibility = View.VISIBLE
            detailResponse?.button?.firstOrNull()?.let { buttonResp ->
                btn_primary?.apply {
                    text = buttonResp.displayName
                    setOnClickListener {
                        eventClickCtaActionInOrderDetail(buttonResp.displayName, detailResponse?.statusText.orEmpty())
                        when {
                            buttonResp.key.equals(KEY_ACCEPT_ORDER, true) -> {
                                btn_primary?.isLoading = true
                                setActionAcceptOrder(buttonResp.displayName, orderId, skipOrderValidation())
                            }
                            buttonResp.key.equals(KEY_TRACK_SELLER, true) -> setActionGoToTrackingPage(buttonResp)
                            buttonResp.key.equals(KEY_REQUEST_PICKUP, true) -> {
                                btn_primary?.isLoading = true
                                setActionRequestPickup(buttonResp.displayName)
                            }
                            buttonResp.key.equals(KEY_CONFIRM_SHIPPING, true) -> {
                                btn_primary?.isLoading = true
                                setActionConfirmShipping(buttonResp.displayName)
                            }
                            buttonResp.key.equals(KEY_VIEW_COMPLAINT_SELLER, true) -> setActionSeeComplaint(buttonResp.url)
                            buttonResp.key.equals(KEY_BATALKAN_PESANAN, true) -> setActionRejectOrder()
                            buttonResp.key.equals(KEY_ASK_BUYER, true) -> goToAskBuyer()
                            buttonResp.key.equals(KEY_REJECT_ORDER, true) -> setActionRejectOrder()
                            buttonResp.key.equals(KEY_RESPOND_TO_CANCELLATION, true) -> onShowBuyerRequestCancelReasonBottomSheet(buttonResp)
                            buttonResp.key.equals(KEY_UBAH_NO_RESI, true) -> setActionUbahNoResi()
                            buttonResp.key.equals(KEY_CHANGE_COURIER, true) -> setActionChangeCourier()
                            buttonResp.key.equals(KEY_PRINT_AWB, true) -> SomNavigator.goToPrintAwb(activity, view, listOf(detailResponse?.orderId.orEmpty()), true)
                        }
                    }
                }
            }

            if (detailResponse?.button?.size.orZero() > 1) {
                btn_secondary?.visibility = View.VISIBLE
                btn_secondary?.setOnClickListener {
                    val actions = HashMap<String, String>()
                    detailResponse?.button?.filterIndexed { index, _ -> (index != 0) }?.forEach { btn ->
                        actions[btn.key] = btn.displayName
                    }
                    showSecondaryActionBottomSheet(actions)
                }
                setupSecondaryButtonBackground()
            } else {
                btn_secondary?.visibility = View.GONE
            }

        } else {
            containerBtnDetail?.visibility = View.GONE
        }
    }

    private fun showSecondaryActionBottomSheet(actions: HashMap<String, String>) {
        view?.let { view ->
            if (view is ViewGroup) {
                secondaryBottomSheet = secondaryBottomSheet?.apply {
                } ?: initSecondaryActionBottomSheet(view)
                secondaryBottomSheet?.init(view)
                secondaryBottomSheet?.setActions(actions)
                secondaryBottomSheet?.show()
            }
        }
    }

    private fun initSecondaryActionBottomSheet(view: ViewGroup): SomDetailSecondaryActionBottomSheet {
        return SomDetailSecondaryActionBottomSheet(view.context, this)
    }

    private fun setupSecondaryButtonBackground() {
        btn_secondary?.apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(ContextCompat.getColor(context, android.R.color.transparent))
                cornerRadius = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
                setStroke(resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color))
            }
            setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N200))
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
                btn_primary?.isLoading = true
                if (orderId.isNotBlank()) {
                    somDetailViewModel.acceptOrder(orderId)
                }
            }
            somDetailViewModel.validateOrders(listOf(orderId))
        } else {
            btn_primary?.isLoading = true
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

    private fun showSetDeliveredDialog() {
        secondaryBottomSheet?.dismiss()
        context?.let { ctx ->
            val dialog = DialogUnify(ctx, HORIZONTAL_ACTION, NO_IMAGE).apply {
                if (DeviceScreenInfo.isTablet(context)) {
                    dialogMaxWidth = getScreenWidth() / 2
                }
            }
            val gqlQuery = GraphqlHelper.loadRawString(resources, R.raw.som_set_delivered)

            val dialogView = View.inflate(ctx, R.layout.dialog_set_delivered, null).apply {
                val receiverEditText = findViewById<TextInputEditText>(R.id.et_receiver)
                findViewById<View>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }
                findViewById<View>(R.id.btn_ok).setOnClickListener {
                    val name = receiverEditText.text.toString()
                    if (name.isBlank()) {
                        receiverEditText.error = ctx.getString(R.string.et_empty_error)
                    } else {
                        dialog.dismiss()
                        setLoadingIndicator(true)
                        somDetailViewModel.setDelivered(gqlQuery, orderId, receiverEditText.text.toString())
                    }
                }
            }

            with(dialog) {
                setUnlockVersion()
                setChild(dialogView)
                show()
            }
        }
    }

    private fun setLoadingIndicator(active: Boolean) {
        swipe_refresh_layout?.isRefreshing = active
    }

    private fun showFreeShippingAcceptOrderDialog(orderId: String) {
        view?.context?.let {
            val dialogUnify = DialogUnify(it, HORIZONTAL_ACTION, NO_IMAGE).apply {
                if (DeviceScreenInfo.isTablet(context)) {
                    dialogMaxWidth = getScreenWidth() / 2
                }
                setUnlockVersion()
                val dialogView = View.inflate(it, R.layout.dialog_accept_order_free_shipping_som, null).apply {
                    val msgReguler1 = getString(R.string.confirm_msg_1a)
                    val msgBold1 = getString(R.string.confirm_msg_1b)
                    val str1 = SpannableString("$msgReguler1 $msgBold1")
                    str1.setSpan(StyleSpan(Typeface.BOLD), msgReguler1.length + 1, str1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    label_confirmation_msg_1?.text = str1

                    val msgReguler2 = getString(R.string.confirm_msg_2a)
                    val msgBold2 = getString(R.string.confirm_msg_2b)
                    val str2 = SpannableString("$msgReguler2 $msgBold2")
                    str2.setSpan(StyleSpan(Typeface.BOLD), msgReguler2.length + 1, str2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    label_confirmation_msg_2?.text = str2

                    val msg3 = getString(R.string.confirm_msg_3)
                    label_confirmation_msg_3?.text = msg3

                    btn_batal?.setOnClickListener { dismiss() }
                    btn_terima?.setOnClickListener {
                        if (orderId.isNotBlank()) {
                            somDetailViewModel.acceptOrder(orderId)
                            dismiss()
                        }
                    }
                }
                setChild(dialogView)
            }
            dialogUnify.show()
        }
    }

    private fun setActionGoToTrackingPage(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", detailResponse?.orderId?.toString().orEmpty())
        val uriBuilder = Uri.Builder()
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, buttonResp.url)
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
        btn_primary?.isLoading = true
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

    private fun confirmShipping() {
        context?.let { context ->
            val view = view
            if (view is ViewGroup) {
                btn_primary?.isLoading = true
                if (detailResponse?.onlineBooking?.isRemoveInputAwb == true) {
                    val btSheet = SomConfirmShippingBottomSheet(context)
                    childFragmentManager.let {
                        btSheet.init(view)
                        btSheet.setInfoText(detailResponse?.onlineBooking?.infoText.orEmpty())
                        btSheet.setOnDismiss {
                            btn_primary?.isLoading = false
                        }
                        btSheet.show()
                    }
                } else {
                    createIntentConfirmShipping(false)
                }
            }
        }
    }

    override fun onBottomSheetItemClick(key: String) {
        detailResponse?.button?.forEach {
            if (key.equals(it.key, true)) {
                eventClickSecondaryActionInOrderDetail(it.displayName, detailResponse?.statusCode.toString(), detailResponse?.statusText.orEmpty())
                when {
                    key.equals(KEY_TRACK_SELLER, true) -> setActionGoToTrackingPage(it)
                    key.equals(KEY_REJECT_ORDER, true) -> setActionRejectOrder()
                    key.equals(KEY_BATALKAN_PESANAN, true) -> setActionRejectOrder()
                    key.equals(KEY_UBAH_NO_RESI, true) -> setActionUbahNoResi()
                    key.equals(KEY_UPLOAD_AWB, true) -> setActionUploadAwb(it)
                    key.equals(KEY_CHANGE_COURIER, true) -> setActionChangeCourier()
                    key.equals(KEY_ACCEPT_ORDER, true) -> setActionAcceptOrder(it.displayName, orderId, skipOrderValidation())
                    key.equals(KEY_ASK_BUYER, true) -> goToAskBuyer()
                    key.equals(KEY_SET_DELIVERED, true) -> showSetDeliveredDialog()
                    key.equals(KEY_PRINT_AWB, true) -> SomNavigator.goToPrintAwb(activity, view, listOf(detailResponse?.orderId.orEmpty()), true)
                }
            }
        }
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
        somDetailViewModel.getRejectReasons(GraphqlHelper.loadRawString(resources, R.raw.gql_som_reject_reason))
    }

    private fun checkReasonRejectIsNotEmpty(reason: String): Boolean {
        var isNotEmpty = true
        if (reason.isEmpty()) isNotEmpty = false
        return isNotEmpty
    }

    private fun setActionUbahNoResi() {
        view?.let {
            if (it is ViewGroup) {
                SomOrderEditAwbBottomSheet(it.context).apply {
                    setListener(object : SomOrderEditAwbBottomSheet.SomOrderEditAwbBottomSheetListener {
                        override fun onEditAwbButtonClicked(cancelNotes: String) {
                            doEditAwb(cancelNotes)
                        }
                    })
                    init(it)
                    setTitle(SomConsts.TITLE_UBAH_RESI)
                    hideKnob()
                    showCloseButton()
                    show()
                }
                return
            }
        }
        showErrorToaster("Terjadi kesalahan, silahkan coba lagi.")
    }

    private fun doEditAwb(shippingRef: String) {
        somDetailViewModel.editAwb(orderId, shippingRef)
    }

    private fun observingEditAwb() {
        somDetailViewModel.editRefNumResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    successEditAwbResponse = it.data
                    if (successEditAwbResponse.mpLogisticEditRefNum.listMessage.isNotEmpty()) {
                        onSuccessEditAwb()
                    } else {
                        showToasterError(getString(R.string.global_error), view)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_EDIT_AWB)
                    failEditAwbResponse.message = it.throwable.message.toString()
                    if (failEditAwbResponse.message.isNotEmpty()) {
                        showToasterError(failEditAwbResponse.message, view)
                    } else {
                        it.throwable.showErrorToaster()
                    }
                }
            }
        })
    }

    private fun showBuyerRequestCancelBottomSheet(button: SomDetailOrder.Data.GetSomDetail.Button) {
        view?.let { view ->
            if (view is ViewGroup) {
                orderRequestCancelBottomSheet = orderRequestCancelBottomSheet?.apply {
                    setupBuyerRequestCancelBottomSheet(this, view, button.popUp)
                } ?: SomOrderRequestCancelBottomSheet(view.context).apply {
                    setupBuyerRequestCancelBottomSheet(this, view, button.popUp)
                }

                orderRequestCancelBottomSheet?.init(view)
                orderRequestCancelBottomSheet?.show()
                return
            }
        }
        showErrorToaster("Terjadi kesalahan, silahkan coba lagi.")
    }

    private fun setupBuyerRequestCancelBottomSheet(bottomSheet: SomOrderRequestCancelBottomSheet, view: ViewGroup, popUp: PopUp) {
        bottomSheet.apply {
            setListener(object : SomOrderRequestCancelBottomSheet.SomOrderRequestCancelBottomSheetListener {
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
            })
            init(popUp, Utils.getL2CancellationReason(detailResponse?.buyerRequestCancel?.reason.orEmpty()), detailResponse?.statusCode.orZero())
            setTitle(view.context.getString(R.string.som_request_cancel_bottomsheet_title))
            hideKnob()
            showCloseButton()
        }
    }

    override fun onRejectReasonItemClick(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        when (rejectReason.reasonCode) {
            1 -> setProductEmpty(rejectReason)
            4 -> setShopClosed(rejectReason)
            7 -> setCourierProblems(rejectReason)
            15 -> setBuyerNoResponse(rejectReason)
            14 -> setOtherReason(rejectReason)
        }
    }

    override fun onTextCopied(label: String, str: String, readableDataName: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, str))
        showCommonToaster(getString(R.string.message_success_copy, readableDataName))
    }

    override fun onInvalidResiUpload(awbUploadUrl: String) {
        openWebview(awbUploadUrl)
    }

    override fun onSeeInvoice(url: String, invoice: String) {
        SomAnalytics.eventClickViewInvoice(detailResponse?.statusCode?.toString().orEmpty(), detailResponse?.statusText.orEmpty())
        Intent(activity, SomSeeInvoiceActivity::class.java).apply {
            putExtra(KEY_URL, url)
            putExtra(PARAM_INVOICE, invoice)
            putExtra(KEY_TITLE, resources.getString(R.string.title_som_invoice))
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

    private fun setProductEmpty(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        view.let { view ->
            if (view is ViewGroup) {
                somProductEmptyBottomSheet = reInitProductEmptyBottomSheet(rejectReason)
                        ?: initProductEmptyBottomSheet(rejectReason)
                somProductEmptyBottomSheet?.init(view)
                somProductEmptyBottomSheet?.show()
            }
        }
    }

    private fun reInitProductEmptyBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetProductEmpty? {
        return somProductEmptyBottomSheet?.apply {
            setProducts(detailResponse?.listProduct.orEmpty())
            setOrderId(orderId)
            setRejectReason(rejectReason)
        }
    }

    private fun initProductEmptyBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetProductEmpty? {
        return context?.let { context ->
            SomBottomSheetProductEmpty(context, rejectReason, orderId, this).apply {
                setProducts(detailResponse?.listProduct.orEmpty())
            }
        }
    }

    private fun setShopClosed(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        somShopClosedBottomSheet = reInitShopClosedBottomSheet(rejectReason)
                ?: initShopClosedBottomSheet(rejectReason)

        view?.let {
            if (it is ViewGroup) {
                somShopClosedBottomSheet?.apply {
                    init(it)
                    show()
                }
            }
        }
    }

    private fun reInitShopClosedBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetShopClosed? {
        return somShopClosedBottomSheet?.apply {
            setOrderId(orderId)
            setRejectReason(rejectReason)
        }
    }

    private fun initShopClosedBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetShopClosed? {
        return context?.let { context ->
            SomBottomSheetShopClosed(context, childFragmentManager, rejectReason, orderId, this)
        }
    }

    private fun setCourierProblems(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        bottomSheetCourierProblems = reInitCourierProblemBottomSheet(rejectReason)
                ?: initCourierProblemBottomSheet(rejectReason)

        view?.let {
            if (it is ViewGroup) {
                bottomSheetCourierProblems?.apply {
                    init(it)
                    show()
                }
            }
        }
    }

    private fun reInitCourierProblemBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetCourierProblem? {
        return bottomSheetCourierProblems?.apply {
            setOrderId(orderId)
            setRejectReason(rejectReason)
        }
    }

    private fun initCourierProblemBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetCourierProblem? {
        return context?.let { context ->
            SomBottomSheetCourierProblem(context, rejectReason, orderId, this)
        }
    }

    private fun setBuyerNoResponse(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        bottomSheetBuyerNoResponse = reInitBuyerNoResponseBottomSheet(rejectReason)
                ?: initBuyerNoResponseBottomSheet(rejectReason)

        view?.let {
            if (it is ViewGroup) {
                bottomSheetBuyerNoResponse?.apply {
                    init(it)
                    show()
                }
            }
        }
    }

    private fun reInitBuyerNoResponseBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetBuyerNoResponse? {
        return bottomSheetBuyerNoResponse?.apply {
            setOrderId(orderId)
            setRejectReason(rejectReason)
        }
    }

    private fun initBuyerNoResponseBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetBuyerNoResponse? {
        return context?.let { context ->
            SomBottomSheetBuyerNoResponse(context, rejectReason, orderId, this)
        }
    }

    private fun setOtherReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        bottomSheetBuyerOtherReason = reInitBuyerOtherResponseBottomSheet(rejectReason)
                ?: initBuyerOtherResponseBottomSheet(rejectReason)

        view?.let {
            if (it is ViewGroup) {
                bottomSheetBuyerOtherReason?.apply {
                    init(it)
                    show()
                }
            }
        }
    }

    private fun reInitBuyerOtherResponseBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetBuyerOtherReason? {
        return bottomSheetBuyerOtherReason?.apply {
            setOrderId(orderId)
            setRejectReason(rejectReason)
        }
    }

    private fun initBuyerOtherResponseBottomSheet(rejectReason: SomReasonRejectData.Data.SomRejectReason): SomBottomSheetBuyerOtherReason? {
        return context?.let { context ->
            SomBottomSheetBuyerOtherReason(context, rejectReason, orderId, this)
        }
    }

    private fun doRejectOrder(orderRejectRequestParam: SomRejectRequestParam) {
        activity?.resources?.let {
            somDetailViewModel.rejectOrder(orderRejectRequestParam)
        }
        SomAnalytics.eventClickTolakPesanan(detailResponse?.statusText.orEmpty(), orderRejectRequestParam.reason)
    }

    private fun observingRejectOrder() {
        somDetailViewModel.rejectOrderResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRejectOrder(it.data.rejectOrder)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_REJECT_ORDER)
                    it.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun showCommonToaster(message: String) {
        view?.run {
            if (this@SomDetailFragment.somToaster?.isShown == true)
                this@SomDetailFragment.somToaster?.dismiss()

            this@SomDetailFragment.somToaster = Toaster.build(
                    this,
                    message,
                    LENGTH_SHORT,
                    TYPE_NORMAL)
            this@SomDetailFragment.somToaster?.show()
        }
    }

    override fun onDialPhone(strPhoneNo: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val phone = "tel:$strPhoneNo"
        intent.data = Uri.parse(phone)
        startActivity(intent)
    }

    override fun onShowInfoLogisticAll(logisticInfoList: List<SomDetailOrder.Data.GetSomDetail.LogisticInfo.All>) {
        startActivity(Intent(activity, SomDetailLogisticInfoActivity::class.java).apply {
            val logisticInfo = LogisticInfoAllWrapper(ArrayList(logisticInfoList))
            val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
            cacheManager?.put(SomDetailLogisticInfoFragment.KEY_SOM_LOGISTIC_INFO_ALL, logisticInfo)
            putExtra(KEY_ID_CACHE_MANAGER_INFO_ALL, cacheManager?.id)
        })
    }

    override fun onShowBookingCode(bookingCode: String, bookingType: String) {
        Intent(activity, SomDetailBookingCodeActivity::class.java).apply {
            putExtra(PARAM_BOOKING_CODE, detailResponse?.bookingInfo?.onlineBooking?.bookingCode)
            putExtra(PARAM_BARCODE_TYPE, detailResponse?.bookingInfo?.onlineBooking?.barcodeType)
            startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        btn_primary?.isLoading = false
        if (requestCode == SomNavigator.REQUEST_CONFIRM_REQUEST_PICKUP && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
                    val resultProcessReqPickup = data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(RESULT_PROCESS_REQ_PICKUP)
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_PROCESS_REQ_PICKUP, resultProcessReqPickup)
                    })
                    activity?.finish()
                }
            }
        } else if (requestCode == SomNavigator.REQUEST_CONFIRM_SHIPPING && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_CONFIRM_SHIPPING)) {
                    val resultConfirmShippingMsg = data.getStringExtra(RESULT_CONFIRM_SHIPPING)
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_CONFIRM_SHIPPING, resultConfirmShippingMsg)
                    })
                    activity?.finish()
                }
            }
        }
    }

    override fun onClickProduct(orderDetailId: Int) {
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
        dismissBottomSheets()
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

    private fun Throwable.showErrorToaster() {
        if (this is UnknownHostException || this is SocketTimeoutException) {
            showNoInternetConnectionToaster()
        } else {
            showServerErrorToaster()
        }
    }

    private fun showNoInternetConnectionGlobalError() {
        somGlobalError?.setType(GlobalError.NO_CONNECTION)
        somGlobalError?.show()
    }

    private fun showServerErrorGlobalError() {
        somGlobalError?.setType(GlobalError.SERVER_ERROR)
        somGlobalError?.show()
    }

    private fun showErrorToaster(message: String) {
        view?.run {
            if (this@SomDetailFragment.somToaster?.isShown == true)
                this@SomDetailFragment.somToaster?.dismiss()

            this@SomDetailFragment.somToaster = Toaster.build(
                    this,
                    message,
                    LENGTH_SHORT,
                    TYPE_ERROR)
            this@SomDetailFragment.somToaster?.show()
        }
    }

    private fun showNoInternetConnectionToaster() {
        showErrorToaster(getString(R.string.som_error_message_no_internet_connection))
    }

    private fun showServerErrorToaster() {
        showErrorToaster(getString(R.string.som_error_message_server_fault))
    }

    protected fun showLoading() {
        progressBarSom?.show()
        rv_detail?.hide()
        somGlobalError?.hide()
        containerBtnDetail?.hide()
        setLoadingIndicator(true)
        refreshHandler?.finishRefresh()
    }

    protected fun showErrorState(type: Int) {
        when (type) {
            GlobalError.NO_CONNECTION -> showNoInternetConnectionGlobalError()
            GlobalError.SERVER_ERROR -> showServerErrorGlobalError()
            else -> showNoInternetConnectionGlobalError()
        }
        progressBarSom?.hide()
        rv_detail?.hide()
        containerBtnDetail?.hide()
        setLoadingIndicator(false)
        refreshHandler?.finishRefresh()
    }

    private fun showSuccessState() {
        rv_detail?.show()
        containerBtnDetail?.show()
        progressBarSom?.hide()
        somGlobalError?.hide()
        setLoadingIndicator(false)
        refreshHandler?.finishRefresh()
    }

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                som_detail_toolbar?.title = getString(R.string.title_som_detail)
                som_detail_toolbar?.isShowBackButton = showBackButton()
                som_detail_toolbar?.setNavigationOnClickListener {
                    onBackPressed()
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
        somDetailViewModel.validateOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onSuccessValidateOrder(result.data)
                is Fail -> onFailedValidateOrder()
            }
        })
    }

    private fun onFailedValidateOrder() {
        showToasterError(getString(R.string.som_error_validate_order), view)
    }

    private fun onSuccessValidateOrder(valid: Boolean) {
        val pendingAction = pendingAction ?: return
        if (valid) {
            pendingAction.action.invoke()
        } else {
            context?.let { context ->
                btn_primary?.isLoading = false
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

    protected open fun onGoToOrderDetailButtonClicked() {
        loadDetail()
    }

    protected open fun onSuccessAcceptOrder() {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(RESULT_ACCEPT_ORDER, this@SomDetailFragment.acceptOrderResponse)
        })
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
            activity?.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(RESULT_REJECT_ORDER, rejectOrderData)
            })
            activity?.finish()
        } else {
            showToasterError(rejectOrderData.message.first(), view)
        }
    }

    protected open fun onSuccessSetDelivered(deliveredData: SetDelivered) {
        val message = deliveredData.message.joinToString().takeIf { it.isNotBlank() }
                ?: getString(R.string.global_error)
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(RESULT_SET_DELIVERED, message)
        })
        activity?.finish()
    }

    protected open fun showBackButton(): Boolean = true

    protected fun dismissBottomSheets() {
        (fragmentManager?.findFragmentByTag(TAG_BOTTOMSHEET) as? BottomSheetUnify)?.let {
            if (it.isVisible) it.dismiss()
        }
        secondaryBottomSheet?.dismiss()
        orderRequestCancelBottomSheet?.dismiss()
        somRejectReasonBottomSheet?.dismiss()
        somProductEmptyBottomSheet?.dismiss()
        somShopClosedBottomSheet?.dismiss()
        bottomSheetCourierProblems?.dismiss()
        bottomSheetBuyerNoResponse?.dismiss()
        bottomSheetBuyerOtherReason?.dismiss()
    }

    protected fun showToasterError(message: String, view: View?) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, LENGTH_SHORT, TYPE_ERROR, ACTION_OK)
        }
    }
}
