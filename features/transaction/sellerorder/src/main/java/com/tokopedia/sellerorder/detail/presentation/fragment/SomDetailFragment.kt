package com.tokopedia.sellerorder.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.*
import android.widget.ProgressBar
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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickMainActionInOrderDetail
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickSecondaryActionInOrderDetail
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.ACTION_OK
import com.tokopedia.sellerorder.common.util.SomConsts.ATTRIBUTE_ID
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_HEADER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PAYMENT_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PRODUCTS_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_SHIPPING_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.INPUT_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.INPUT_SHIPPING_REF
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ASK_BUYER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_BATALKAN_PESANAN
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CHANGE_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_SET_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_TRACK_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UBAH_NO_RESI
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UPLOAD_AWB
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_VIEW_COMPLAINT_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BARCODE_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SHOP_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SOURCE_ASK_BUYER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_USER_ROLES
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_SET_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_ATUR_TOKO_TUTUP
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_BATALKAN_PESANAN_PENALTY
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_COURIER_PROBLEM
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_PILIH_PENOLAKAN
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_PILIH_PRODUK_KOSONG
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_TOLAK_PESANAN_INI
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_UBAH_RESI
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_COURIER_PROBLEM_OTHERS
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_REASON_BUYER_NO_RESPONSE
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_REASON_OTHER
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailBookingCodeActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomSeeInvoiceActivity
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailItemDecoration
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetCourierProblemsAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectOrderAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectReasonsAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetStockEmptyAdapter
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.bottomsheet_buyer_request_cancel_order.view.*
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.btn_cancel_order_canceled
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.btn_cancel_order_confirmed
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.tf_cancel_notes
import kotlinx.android.synthetic.main.bottomsheet_cancel_order_penalty.view.*
import kotlinx.android.synthetic.main.bottomsheet_secondary.*
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*
import kotlinx.android.synthetic.main.bottomsheet_shop_closed.view.*
import kotlinx.android.synthetic.main.dialog_accept_order_free_shipping_som.view.*
import kotlinx.android.synthetic.main.fragment_som_detail.*
import kotlinx.android.synthetic.main.fragment_som_detail.btn_primary
import kotlinx.android.synthetic.main.partial_info_layout.view.*
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


/**x
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailFragment : BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener, SomBottomSheetRejectOrderAdapter.ActionListener, SomDetailAdapter.ActionListener, SomBottomSheetRejectReasonsAdapter.ActionListener,
    SomBottomSheetCourierProblemsAdapter.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val coachMark: CoachMark by lazy {
        CoachMarkBuilder().build()
    }

    private val coachMarkEdit: CoachMarkItem by lazy {
        CoachMarkItem(btn_secondary, getString(R.string.coachmark_edit), getString(R.string.coachmark_edit_info))
    }

    private val coachMarkAccept: CoachMarkItem by lazy {
        CoachMarkItem(btn_primary, getString(R.string.coachmark_terima_pesanan), getString(R.string.coachmark_terima_pesanan_info))
    }

    private val coachMarkChat: CoachMarkItem by lazy {
        CoachMarkItem(view?.rootView?.findViewById(R.id.som_action_chat),
            getString(R.string.coachmark_chat),
            getString(R.string.coachmark_chat_info)
        )
    }

    private var errorToaster: Snackbar? = null

    private var orderId = ""
    private var detailResponse = SomDetailOrder.Data.GetSomDetail()
    private var acceptOrderResponse = SomAcceptOrder.Data.AcceptOrder()
    private var rejectOrderResponse = SomRejectOrder.Data.RejectOrder()
    private var successEditAwbResponse = SomEditAwbResponse.Data()
    private var failEditAwbResponse = SomEditAwbResponse.Error()
    private var rejectReasonResponse = listOf<SomReasonRejectData.Data.SomRejectReason>()
    private var listDetailData: ArrayList<SomDetailData> = arrayListOf()
    private lateinit var somDetailAdapter: SomDetailAdapter
    private lateinit var somBottomSheetRejectOrderAdapter: SomBottomSheetRejectOrderAdapter
    private lateinit var somBottomSheetRejectReasonsAdapter: SomBottomSheetRejectReasonsAdapter
    private lateinit var somBottomSheetStockEmptyAdapter: SomBottomSheetStockEmptyAdapter
    private lateinit var somBottomSheetCourierProblemsAdapter: SomBottomSheetCourierProblemsAdapter
    private val FLAG_CONFIRM_REQ_PICKUP = 3535
    private val FLAG_CONFIRM_SHIPPING = 3553
    private var reasonCourierProblemText: String = ""
    private val tagConfirm = "tag_confirm"
    private var refreshHandler: RefreshHandler? = null
    private var bottomSheetCourierProblems: BottomSheetUnify? = null

    private val coachMarkItems: ArrayList<CoachMarkItem> = arrayListOf()

    private var secondaryBottomSheet: BottomSheetUnify? = null
    private var progressBar: ProgressBar? = null

    private val somDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomDetailViewModel::class.java]
    }

    private var userNotAllowedDialog: DialogUnify? = null

    companion object {
        private val TAG_COACHMARK_DETAIL = "coachmark"

        private const val ERROR_GET_ORDER_DETAIL = "Error when get order detail."
        private const val ERROR_ACCEPTING_ORDER = "Error when accepting order."
        private const val ERROR_GET_ORDER_REJECT_REASONS = "Error when get order reject reasons."
        private const val ERROR_WHEN_SET_DELIVERED = "Error when set order status to delivered."
        private const val ERROR_EDIT_AWB = "Error when edit AWB."
        private const val ERROR_REJECT_ORDER = "Error when rejecting order."
        private const val PAGE_NAME = "seller order detail page."

        private val allowedRoles = listOf(Roles.MANAGE_SHOPSTATS, Roles.MANAGE_INBOX, Roles.MANAGE_TA, Roles.MANAGE_TX)

        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailFragment {
            return SomDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, bundle.getString(PARAM_ORDER_ID))
                    putParcelable(PARAM_USER_ROLES, bundle.getParcelable(PARAM_USER_ROLES))
                }
            }
        }
    }

    fun doClickChat() {
        SomAnalytics.eventClickChatOnHeaderDetail(detailResponse.statusCode.toString())
        goToAskBuyer()
    }

    fun goToAskBuyer() {
        val urlInvoice = detailResponse.invoiceUrl
        val invoiceUri = Uri.parse(urlInvoice)
        val invoiceId = invoiceUri.getQueryParameter(ATTRIBUTE_ID)
        val intent = RouteManager.getIntent(activity,
            ApplinkConst.TOPCHAT_ASKBUYER,
            detailResponse.customer.id.toString(), "",
            PARAM_SOURCE_ASK_BUYER, detailResponse.customer.name, detailResponse.customer.image).apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, invoiceId)
            putExtra(ApplinkConst.Chat.INVOICE_CODE, detailResponse.invoice)

            if (detailResponse.listProduct.isNotEmpty()) {
                putExtra(ApplinkConst.Chat.INVOICE_TITLE, detailResponse.listProduct.first().name)
                putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, detailResponse.listProduct.first().thumbnail)
            }
            putExtra(ApplinkConst.Chat.INVOICE_DATE, detailResponse.paymentDate)
            putExtra(ApplinkConst.Chat.INVOICE_URL, detailResponse.invoiceUrl)
            putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, detailResponse.statusCode.toString())
            putExtra(ApplinkConst.Chat.INVOICE_STATUS, detailResponse.statusText)
            putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, detailResponse.paymentSummary.totalPriceText)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            orderId = arguments?.getString(PARAM_ORDER_ID).toString()
            val userRoles = arguments?.getParcelable<SomGetUserRoleUiModel?>(PARAM_USER_ROLES)
            if (userRoles != null) {
                somDetailViewModel.setUserRoles(userRoles)
            } else {
                checkUserRole()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progress_bar)
        prepareLayout()
        observingDetail()
        observingAcceptOrder()
        observingRejectReasons()
        observingRejectOrder()
        observingEditAwb()
        observingSetDelivered()
        observingUserRoles()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_menu, menu)
    }

    override fun onAddedCoachMarkHeader(coachMarkItemHeader: CoachMarkItem) {
        coachMarkItems.add(coachMarkChat)
        coachMarkItems.add(coachMarkItemHeader)
    }

    override fun onAddedCoachMarkProducts(coachMarkItemProduct: CoachMarkItem) {
        coachMarkItems.add(coachMarkItemProduct)
    }

    override fun onAddedCoachMarkShipping(coachMarkItemShipping: CoachMarkItem) {
        coachMarkItems.add(coachMarkItemShipping)
        addedCoachMark()
    }

    private fun checkUserRole() {
        progressBarSom?.show()
        if (Utils.isConnectedToInternet(context)) {
            somDetailViewModel.loadUserRoles(userSession.userId.toIntOrZero())
        } else {
            showNoInternetConnectionGlobalError()
            progressBarSom?.hide()
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
            addItemDecoration(SomDetailItemDecoration())
        }
        somGlobalError?.setActionClickListener {
            loadDetail()
        }
    }

    private fun loadDetail() {
        somGlobalError?.hide()
        progressBarSom?.show()
        if (Utils.isConnectedToInternet(context)) {
            activity?.let {
                SomAnalytics.sendScreenName(it, SomConsts.DETAIL_ORDER_SCREEN_NAME + orderId)
                it.resources?.let { r ->
                    somDetailViewModel.loadDetailOrder(GraphqlHelper.loadRawString(r, R.raw.gql_som_detail), orderId)
                }
            }
        } else {
            showNoInternetConnectionGlobalError()
            progressBarSom?.hide()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomDetailComponent::class.java).inject(this)
    }

    private fun observingDetail() {
        somDetailViewModel.orderDetailResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    detailResponse = it.data
                    renderDetail()
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException) {
                        showNoInternetConnectionGlobalError()
                    } else {
                        showServerErrorGlobalError()
                    }
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_DETAIL)
                }
            }
            setLoadingIndicator(false)
            progressBarSom?.hide()
        })
    }

    private fun observingAcceptOrder() {
        somDetailViewModel.acceptOrderResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    SomAnalytics.eventClickAcceptOrderPopup(true)
                    acceptOrderResponse = it.data.acceptOrder
                    if (acceptOrderResponse.success == 1) {
                        activity?.setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(RESULT_ACCEPT_ORDER, acceptOrderResponse)
                        })
                        activity?.finish()

                    } else {
                        showToasterError(acceptOrderResponse.listMessage.first(), view)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_ACCEPTING_ORDER)
                    SomAnalytics.eventClickAcceptOrderPopup(false)
                    if (it.throwable is UnknownHostException) {
                        showNoInternetConnectionToaster()
                    } else {
                        showServerErrorToaster()
                    }
                }
            }
        })
    }

    private fun observingRejectReasons() {
        val bottomSheetRejectReason = BottomSheetUnify()
        somDetailViewModel.rejectReasonResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    rejectReasonResponse = it.data.listSomRejectReason
                    somBottomSheetRejectReasonsAdapter = SomBottomSheetRejectReasonsAdapter(this).apply {
                        listRejectReasons = rejectReasonResponse.toMutableList()
                        notifyDataSetChanged()
                    }

                    val viewBottomSheetRejectReason = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
                        if (detailResponse.penaltyRejectInfo.isPenaltyReject) {
                            ticker_penalty_secondary?.apply {
                                visibility = View.VISIBLE
                                setHtmlDescription(detailResponse.penaltyRejectInfo.penaltyRejectWording)
                                setDescriptionClickEvent(object : TickerCallback {
                                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                                    }

                                    override fun onDismiss() {}
                                })
                            }
                        } else {
                            ticker_penalty_secondary?.visibility = View.GONE
                        }

                        rv_bottomsheet_secondary?.apply {
                            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                            adapter = somBottomSheetRejectReasonsAdapter
                        }
                        tf_extra_notes?.visibility = View.GONE
                    }

                    bottomSheetRejectReason.apply {
                        setChild(viewBottomSheetRejectReason)
                        setCloseClickListener { dismiss() }
                        setTitle(TITLE_PILIH_PENOLAKAN)
                    }
                    fragmentManager?.let { it1 -> bottomSheetRejectReason.show(it1, getString(R.string.show_bottomsheet)) }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_REJECT_REASONS)
                    if (it.throwable is UnknownHostException) {
                        showNoInternetConnectionToaster()
                    } else {
                        showServerErrorToaster()
                    }
                }
            }
        })
    }

    private fun observingSetDelivered() {
        somDetailViewModel.setDelivered.observe(this, Observer {
            setLoadingIndicator(false)
            when (it) {
                is Success -> {
                    view?.let { v ->
                        val message = it.data.setDelivered.message.joinToString()
                        activity?.setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(RESULT_SET_DELIVERED, message)
                        })
                        activity?.finish()
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_WHEN_SET_DELIVERED)
                    if (it.throwable is UnknownHostException) {
                        showNoInternetConnectionToaster()
                    } else {
                        showServerErrorToaster()
                    }
                }
            }
        })
    }

    private fun observingUserRoles() {
        somDetailViewModel.userRoleResult.observe(viewLifecycleOwner, Observer { result ->
            setLoadingIndicator(false)
            when (result) {
                is Success -> {
                    if (result.data.roles.any { allowedRoles.contains(it) }) {
                        onUserAllowedToViewSOM()
                    } else {
                        onUserNotAllowedToViewSOM()
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(result.throwable, String.format(SomConsts.ERROR_GET_USER_ROLES, PAGE_NAME))
                    if (result.throwable is UnknownHostException) {
                        showNoInternetConnectionToaster()
                    } else {
                        showServerErrorToaster()
                    }
                    refreshHandler?.finishRefresh()
                }
            }
        })
    }

    private fun onUserNotAllowedToViewSOM() {
        context?.run {
            if (userNotAllowedDialog == null) {
                Utils.createUserNotAllowedDialog(this)
            }
            userNotAllowedDialog?.show()
        }
    }

    private fun onUserAllowedToViewSOM() {
        loadDetail()
    }

    private fun renderDetail() {
        rv_detail?.show()
        refreshHandler?.finishRefresh()
        listDetailData = arrayListOf()
        somDetailAdapter.listDataDetail = arrayListOf()
        renderHeader()
        renderProducts()
        renderShipment()
        renderPayment()
        renderButtons()

        somDetailAdapter.listDataDetail = listDetailData.toMutableList()
        somDetailAdapter.notifyDataSetChanged()
    }

    private fun addedCoachMark() {
        if (detailResponse.button.isNotEmpty()) {
            coachMarkItems.add(coachMarkEdit)
            coachMarkItems.add(coachMarkAccept)
            showCoachMark()
        } else {
            showCoachMark()
        }
    }

    private fun showCoachMark() {
        if (!coachMark.hasShown(activity, TAG_COACHMARK_DETAIL)) {
            coachMark.show(activity, TAG_COACHMARK_DETAIL, coachMarkItems)
        }
    }

    private fun renderHeader() {
        // header
        val dataHeader = SomDetailHeader(
            detailResponse.statusCode,
            detailResponse.statusText,
            detailResponse.buyerRequestCancel.isRequestCancel,
            detailResponse.invoice,
            detailResponse.invoiceUrl,
            detailResponse.paymentDate,
            detailResponse.customer.name,
            detailResponse.deadline.text,
            detailResponse.deadline.color,
            detailResponse.listLabelInfo,
            detailResponse.orderId.toString(),
            detailResponse.shipment.awb,
            detailResponse.shipment.awbTextColor,
            detailResponse.shipment.awbUploadUrl,
            detailResponse.shipment.awbUploadProofText,
            detailResponse.bookingInfo.onlineBooking.bookingCode,
            detailResponse.bookingInfo.onlineBooking.state,
            detailResponse.bookingInfo.onlineBooking.barcodeType,
            detailResponse.tickerInfo)

        listDetailData.add(SomDetailData(dataHeader, DETAIL_HEADER_TYPE))
    }

    private fun renderProducts() {
        // products
        val dataProducts = SomDetailProducts(detailResponse.listProduct)
        listDetailData.add(SomDetailData(dataProducts, DETAIL_PRODUCTS_TYPE))
    }

    private fun renderShipment() {
        // shipping
        val dataShipping = SomDetailShipping(
            detailResponse.shipment.name + " - " + detailResponse.shipment.productName,
            detailResponse.paymentSummary.shippingPriceText,
            detailResponse.receiver.name,
            detailResponse.receiver.phone,
            detailResponse.receiver.street,
            detailResponse.receiver.district + ", " + detailResponse.receiver.city + " " + detailResponse.receiver.postal,
            detailResponse.receiver.province,
            detailResponse.flagOrderMeta.flagFreeShipping,
            detailResponse.bookingInfo.driver.photo,
            detailResponse.bookingInfo.driver.name,
            detailResponse.bookingInfo.driver.phone,
            detailResponse.dropshipper.name,
            detailResponse.dropshipper.phone,
            detailResponse.bookingInfo.driver.licenseNumber,
            detailResponse.bookingInfo.onlineBooking.bookingCode,
            detailResponse.bookingInfo.onlineBooking.state,
            detailResponse.bookingInfo.onlineBooking.message,
            detailResponse.bookingInfo.onlineBooking.messageArray,
            detailResponse.bookingInfo.onlineBooking.barcodeType,
            isRemoveAwb = detailResponse.onlineBooking.isRemoveInputAwb)

        listDetailData.add(SomDetailData(dataShipping, DETAIL_SHIPPING_TYPE))
    }

    private fun renderPayment() {
        val dataPayments = SomDetailPayments(
            detailResponse.paymentSummary.productsPriceText,
            detailResponse.paymentSummary.totalItem,
            detailResponse.paymentSummary.totalWeightText,
            detailResponse.paymentSummary.shippingPriceText,
            detailResponse.paymentSummary.insurancePrice,
            detailResponse.paymentSummary.insurancePriceText,
            detailResponse.paymentSummary.additionalPrice,
            detailResponse.paymentSummary.additionalPriceText,
            detailResponse.paymentSummary.totalPriceText)

        listDetailData.add(SomDetailData(dataPayments, DETAIL_PAYMENT_TYPE))
    }

    private fun renderButtons() {
        // buttons
        if (detailResponse.button.isNotEmpty()) {
            containerBtnDetail?.visibility = View.VISIBLE
            val shouldShowBuyerRequestCancelButton = (detailResponse.statusCode == 220 || detailResponse.statusCode == 400) && detailResponse.buyerRequestCancel.isRequestCancel
            detailResponse.button.first().let { buttonResp ->
                btn_primary?.apply {
                    text = if (shouldShowBuyerRequestCancelButton) "Tanggapi Pembatalan"
                    else buttonResp.displayName
                    setOnClickListener {
                        eventClickMainActionInOrderDetail(buttonResp.displayName, detailResponse.statusText)
                        performButtonAction(buttonResp, shouldShowBuyerRequestCancelButton)
                    }
                }
            }

            if (detailResponse.button.size > 1) {
                btn_secondary?.visibility = View.VISIBLE
                btn_secondary?.setOnClickListener {
                    somBottomSheetRejectOrderAdapter = SomBottomSheetRejectOrderAdapter(this, hasRadioBtn = false)
                    showTextOnlyBottomSheet()
                    val mapKey = HashMap<String, String>()
                    detailResponse.button.filterIndexed { index, _ -> (index != 0) }.forEach { btn ->
                        mapKey[btn.key] = btn.displayName
                    }
                    somBottomSheetRejectOrderAdapter?.mapKey = mapKey
                    somBottomSheetRejectOrderAdapter?.notifyDataSetChanged()
                }
            } else {
                btn_secondary?.visibility = View.GONE
            }

        } else {
            containerBtnDetail?.visibility = View.GONE
        }
    }

    private fun performButtonAction(buttonResp: SomDetailOrder.Data.GetSomDetail.Button?, shouldShowBuyerRequestCancelButton: Boolean) {
        buttonResp?.let {
            when {
                shouldShowBuyerRequestCancelButton -> showBuyerRequestCancelBottomSheet()
                it.key.equals(KEY_ACCEPT_ORDER, true) -> setActionAcceptOrder(it)
                it.key.equals(KEY_TRACK_SELLER, true) -> setActionGoToTrackingPage(it)
                it.key.equals(KEY_REQUEST_PICKUP, true) -> setActionRequestPickup()
                it.key.equals(KEY_CONFIRM_SHIPPING, true) -> setActionConfirmShipping()
                it.key.equals(KEY_VIEW_COMPLAINT_SELLER, true) -> setActionSeeComplaint(it.url)
                it.key.equals(KEY_BATALKAN_PESANAN, true) -> setActionRejectOrder()
                it.key.equals(KEY_ASK_BUYER, true) -> goToAskBuyer()
                it.key.equals(KEY_REJECT_ORDER, true) -> setActionRejectOrder()
            }
        }
    }

    private fun setActionSeeComplaint(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    private fun setActionAcceptOrder(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        val mapParam = buttonResp.param.convertStrObjToHashMap()
        val orderId = mapParam[PARAM_ORDER_ID].toString()
        val shopId = mapParam[PARAM_SHOP_ID].toString()
        setActionAcceptOrder(orderId, shopId)
    }

    private fun setActionAcceptOrder(orderId: String, shopId: String) {
        if (detailResponse.flagOrderMeta.flagFreeShipping) {
            showFreeShippingAcceptOrderDialog(orderId, shopId)
        } else {
            acceptOrder(orderId, shopId)
        }
    }

    private fun acceptOrder(orderId: String, shopId: String) {
        if (orderId.isNotBlank() && shopId.isNotBlank()) {
            somDetailViewModel.acceptOrder(GraphqlHelper.loadRawString(resources, R.raw.gql_som_accept_order),
                    orderId, shopId)
        }
    }

    private fun showSetDeliveredDialog() {
        secondaryBottomSheet?.dismiss()
        context?.let { ctx ->
            val dialog = DialogUnify(ctx, HORIZONTAL_ACTION, NO_IMAGE)
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
        if (active) {
            progressBar?.visibility = View.VISIBLE
        } else {
            progressBar?.visibility = View.GONE
        }
    }

    private fun showFreeShippingAcceptOrderDialog(orderId: String, shopId: String) {
        view?.context?.let {
            val dialogUnify = DialogUnify(it, HORIZONTAL_ACTION, NO_IMAGE).apply {
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
                        if (orderId.isNotBlank() && shopId.isNotBlank()) {
                            somDetailViewModel.acceptOrder(GraphqlHelper.loadRawString(resources, R.raw.gql_som_accept_order),
                                orderId, shopId)
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
        var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", detailResponse.orderId.toString())
        val uriBuilder = Uri.Builder()
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, buttonResp.url)
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_CALLER, PARAM_SELLER)
        routingAppLink += uriBuilder.toString()
        RouteManager.route(context, routingAppLink)
    }

    private fun setActionRequestPickup() {
        Intent(activity, SomConfirmReqPickupActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            startActivityForResult(this, FLAG_CONFIRM_REQ_PICKUP)
        }
    }

    private fun setActionConfirmShipping() {
        if (detailResponse.onlineBooking.isRemoveInputAwb) {
            val btSheet = BottomSheetUnify()
            val infoLayout = View.inflate(context, R.layout.partial_info_layout, null)
            infoLayout.tv_confirm_info?.text = detailResponse.onlineBooking.infoText
            infoLayout.button_understand?.setOnClickListener { btSheet.dismiss() }

            fragmentManager?.let {
                btSheet.setTitle(context?.getString(R.string.automatic_shipping) ?: "")
                btSheet.setChild(infoLayout)
                btSheet.setCloseClickListener { btSheet.dismiss() }
                btSheet.show(it, tagConfirm)
            }
        } else {
            createIntentConfirmShipping(false)
        }
    }

    private fun showTextOnlyBottomSheet() {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetRejectOrderAdapter
            }
            fl_btn_primary?.visibility = View.GONE
            tf_extra_notes?.visibility = View.GONE
        }

        secondaryBottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            clearClose(true)
            clearHeader(true)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { secondaryBottomSheet?.show(it, getString(R.string.show_bottomsheet)) }
    }

    override fun onBottomSheetItemClick(key: String) {
        detailResponse.button.forEach {
            if (key.equals(it.key, true)) {
                eventClickSecondaryActionInOrderDetail(it.displayName, detailResponse.statusText)
                secondaryBottomSheet?.dismiss()
                when {
                    key.equals(KEY_REJECT_ORDER, true) -> setActionRejectOrder()
                    key.equals(KEY_BATALKAN_PESANAN, true) -> setActionRejectOrder()
                    key.equals(KEY_UBAH_NO_RESI, true) -> setActionUbahNoResi()
                    key.equals(KEY_UPLOAD_AWB, true) -> setActionUploadAwb(it)
                    key.equals(KEY_CHANGE_COURIER, true) -> setActionChangeCourier()
                    key.equals(KEY_ACCEPT_ORDER, true) -> setActionAcceptOrder(it)
                    key.equals(KEY_ASK_BUYER, true) -> goToAskBuyer()
                    key.equals(KEY_SET_DELIVERED, true) -> showSetDeliveredDialog()
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

    private fun createIntentConfirmShipping(isChangeShipping: Boolean) {
        Intent(activity, SomConfirmShippingActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            putExtra(PARAM_CURR_IS_CHANGE_SHIPPING, isChangeShipping)
            startActivityForResult(this, FLAG_CONFIRM_SHIPPING)
        }
    }

    private fun setActionRejectOrder() {
        if (detailResponse.buyerRequestCancel.isRequestCancel) {
            showBuyerRequestCancelBottomSheet()
        } else {
            somDetailViewModel.getRejectReasons(GraphqlHelper.loadRawString(resources, R.raw.gql_som_reject_reason))
        }
    }

    private fun setActionCancelOrder() {
        // bottomSheetUnify.dismiss()
        if (detailResponse.buyerRequestCancel.isRequestCancel) {
            showBuyerRequestCancelBottomSheet()
        } else {
            showCancelOrderPenaltyBottomSheet()
        }
    }

    private fun showCancelOrderPenaltyBottomSheet() {
        val bottomSheetPenalty = BottomSheetUnify()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_cancel_order_penalty, null).apply {
            tf_cancel_notes?.apply {
                setLabelStatic(true)
                setMessage(getString(R.string.cancel_order_notes_max))
                textFiedlLabelText.text = getString(R.string.cancel_order_notes_hint)
                textFieldInput.hint = getString(R.string.cancel_order_notes_hint)
            }
            ticker_penalty_explanation?.apply {
                setHtmlDescription(getString(R.string.cancel_order_penalty_warning_content))
                closeButtonVisibility = View.GONE
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {}
                })
            }
            btn_cancel_order_canceled?.setOnClickListener { bottomSheetPenalty.dismiss() }
            btn_cancel_order_confirmed?.setOnClickListener {
                bottomSheetPenalty.dismiss()
                val orderRejectRequest = SomRejectRequest(
                    orderId = detailResponse.orderId.toString(),
                    rCode = "0",
                    reason = tf_cancel_notes?.textFieldInput?.text.toString()
                )
                if (checkReasonRejectIsNotEmpty(tf_cancel_notes?.textFieldInput?.text.toString())) {
                    doRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(getString(R.string.cancel_order_notes_empty_warning), bottomSheetPenalty.view)
                }
            }
        }
        bottomSheetPenalty.apply {
            setTitle(TITLE_BATALKAN_PESANAN_PENALTY)
            setFullPage(true)
            setChild(viewBottomSheet)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetPenalty.show(it, getString(R.string.show_bottomsheet))
        }
    }

    private fun checkReasonRejectIsNotEmpty(reason: String): Boolean {
        var isNotEmpty = true
        if (reason.isEmpty()) isNotEmpty = false
        return isNotEmpty
    }

    private fun setActionUbahNoResi() {
        val bottomSheetUbahResi = BottomSheetUnify()
        val viewBottomSheetUbahResi = View.inflate(context, R.layout.bottomsheet_cancel_order, null).apply {
            tf_cancel_notes?.setLabelStatic(true)
            tf_cancel_notes?.setMessage(getString(R.string.change_no_resi_notes))
            tf_cancel_notes?.textFieldInput?.hint = getString(R.string.change_no_resi_hint)
            btn_cancel_order_canceled?.setOnClickListener { bottomSheetUbahResi.dismiss() }
            btn_cancel_order_confirmed?.text = getString(R.string.change_no_resi_btn_ubah)
            btn_cancel_order_confirmed?.setOnClickListener {
                secondaryBottomSheet?.dismiss()
                bottomSheetUbahResi.dismiss()
                doEditAwb(tf_cancel_notes?.textFieldInput?.text.toString())
            }
        }

        bottomSheetUbahResi.apply {
            setTitle(TITLE_UBAH_RESI)
            setFullPage(false)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetUbahResi)
        }
        fragmentManager?.let {
            bottomSheetUbahResi.show(it, getString(R.string.show_bottomsheet))
        }
    }

    private fun doEditAwb(shippingRef: String) {
        val rawQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_som_edit_awb)
        val queryString = rawQuery.replace(INPUT_ORDER_ID, orderId, true)
            .replace(INPUT_SHIPPING_REF, shippingRef, true)
        somDetailViewModel.editAwb(queryString)
    }

    private fun observingEditAwb() {
        somDetailViewModel.editRefNumResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    successEditAwbResponse = it.data
                    if (successEditAwbResponse.mpLogisticEditRefNum.listMessage.isNotEmpty()) {
                        showCommonToaster(successEditAwbResponse.mpLogisticEditRefNum.listMessage.first())
                        loadDetail()
                    } else {
                        showToasterError(getString(R.string.global_error), view)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_EDIT_AWB)
                    failEditAwbResponse.message = it.throwable.message.toString()
                    if(failEditAwbResponse.message.isNotEmpty()) {
                        showToasterError(failEditAwbResponse.message, view)
                    } else {
                        if (it.throwable is UnknownHostException) {
                            showNoInternetConnectionToaster()
                        } else {
                            showServerErrorToaster()
                        }
                    }
                }
            }
        })
    }

    override fun onShowBottomSheetInfo(title: String, resIdDesc: Int) {
        val bottomSheetUnify = BottomSheetUnify()
        val childView = View.inflate(context, R.layout.bottomsheet_som_info, null)

        val bottomSheetDesc: Typography = childView.findViewById(R.id.bottomsheet_desc)
        bottomSheetDesc.setText(resIdDesc)

        val childBtn: UnifyButton = childView.findViewById(R.id.btn_mengerti)
        childBtn.setOnClickListener { bottomSheetUnify.dismiss() }

        fragmentManager?.let {
            bottomSheetUnify.apply {
                clearClose(false)
                clearHeader(false)
                setTitle(title)
                setOnDismissListener { this.dismiss() }
                setCloseClickListener { this.dismiss() }
                setChild(childView)
            }
            bottomSheetUnify.show(it, "")
        }
    }

    override fun onShowBuyerRequestCancelReasonBottomSheet() {
        showBuyerRequestCancelBottomSheet()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun showBuyerRequestCancelBottomSheet() {
        val bottomSheetReqCancel = BottomSheetUnify()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel_order, null).apply {
            tickerPerformanceInfo?.setTextDescription(getString(R.string.som_shop_performance_info))

            val reasonBuyer = detailResponse.buyerRequestCancel.reason
            tvBuyerRequestCancelNotes?.text = reasonBuyer.replace("\\n", System.getProperty("line.separator") ?: "")

            setupBuyerRequestCancelBottomSheetButtons(this, bottomSheetReqCancel, reasonBuyer)
        }

        bottomSheetReqCancel.apply {
            setFullPage(false)
            setTitle(TITLE_TOLAK_PESANAN_INI)
            setChild(viewBottomSheet)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetReqCancel.show(it, getString(R.string.show_bottomsheet))
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

    override fun onTextCopied(label: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, str))
        showCommonToaster(getString(R.string.resi_tersalin))
    }

    override fun onInvalidResiUpload(awbUploadUrl: String) {
        openWebview(awbUploadUrl)
    }

    override fun onSeeInvoice(url: String) {
        SomAnalytics.eventClickViewInvoice(detailResponse.statusCode.toString())
        Intent(activity, SomSeeInvoiceActivity::class.java).apply {
            putExtra(KEY_URL, url)
            putExtra(KEY_TITLE, resources.getString(R.string.title_som_invoice))
            putExtra(PARAM_ORDER_CODE, detailResponse.statusCode.toString())
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
        somBottomSheetStockEmptyAdapter = SomBottomSheetStockEmptyAdapter()
        val bottomSheetProductEmpty = BottomSheetUnify()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetStockEmptyAdapter
            }

            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_secondary?.visibility = View.VISIBLE
                ticker_penalty_secondary?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_secondary?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_secondary?.visibility = View.GONE
            }

            tf_extra_notes?.visibility = View.VISIBLE
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = getString(R.string.empty_stock_extra_note)
            tf_extra_notes?.setPlaceholder(getString(R.string.empty_stock_extra_placeholder))

            fl_btn_primary?.visibility = View.VISIBLE
            fl_btn_primary?.setOnClickListener {
                bottomSheetProductEmpty.dismiss()
                val orderRejectRequest = SomRejectRequest()
                orderRejectRequest.orderId = detailResponse.orderId.toString()
                orderRejectRequest.rCode = rejectReason.reasonCode.toString()
                var strListPrd = ""
                var indexPrd = 0
                somBottomSheetStockEmptyAdapter.getListProductEmptied().forEach {
                    if (indexPrd > 0) strListPrd += "~"
                    strListPrd += it.id
                    indexPrd++
                }
                orderRejectRequest.listPrd = strListPrd
                orderRejectRequest.reason = tf_extra_notes?.textFieldInput?.text.toString()
                if (checkReasonRejectIsNotEmpty(tf_cancel_notes?.textFieldInput?.text.toString())) {
                    doRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(getString(R.string.cancel_order_notes_empty_warning), bottomSheetProductEmpty.view)
                }
            }
        }

        bottomSheetProductEmpty.apply {
            setFullPage(true)
            setTitle(TITLE_PILIH_PRODUK_KOSONG)
            setChild(viewBottomSheet)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetProductEmpty.show(it, getString(R.string.show_bottomsheet))
        }

        somBottomSheetStockEmptyAdapter.listProduct = detailResponse.listProduct.toMutableList()
        somBottomSheetStockEmptyAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setShopClosed(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        val bottomSheetShopClosed = BottomSheetUnify()
        val viewBottomSheetShopClosed = View.inflate(context, R.layout.bottomsheet_shop_closed, null).apply {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_shop_closed?.visibility = View.VISIBLE
                ticker_penalty_shop_closed?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_shop_closed?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_shop_closed?.visibility = View.GONE
            }

            val defaultDateNow = Date().toFormattedString("dd/MM/yyyy")
            // setup closing start date
            tf_start_shop_closed?.textFieldWrapper?.hint = getString(R.string.start_shop_closed_label)
            tf_start_shop_closed?.textFieldInput?.setText(defaultDateNow)
            tf_start_shop_closed?.textFieldInput?.isEnabled = false

            // setup closing end date
            tf_end_shop_closed?.textFieldWrapper?.hint = getString(R.string.end_shop_closed_label)
            tf_end_shop_closed?.textFieldInput?.apply {
                setText(defaultDateNow)
            }
            tf_end_shop_closed?.textFieldInput?.isEnabled = false
            tf_end_shop_closed?.setFirstIcon(R.drawable.ic_som_filter_calendar)
            tf_end_shop_closed?.textFieldIcon1?.setOnClickListener {
                showDatePicker(tf_end_shop_closed, this)
            }
            updateClosingEndDate(convertFormatDate(defaultDateNow, "dd/MM/yyyy", "dd MMM yyyy"), this)

            // setup closing additional notes
            tf_shop_closed_notes?.setLabelStatic(true)
            tf_shop_closed_notes?.textFiedlLabelText?.text = getString(R.string.shop_closed_note_label)
            tf_shop_closed_notes?.textFieldInput?.hint = getString(R.string.shop_closed_note_placeholder)

            btn_reject_shop_closed?.setOnClickListener {
                bottomSheetShopClosed.dismiss()
                val orderRejectRequest = SomRejectRequest(
                    orderId = detailResponse.orderId.toString(),
                    rCode = rejectReason.reasonCode.toString(),
                    closedNote = tf_shop_closed_notes?.textFieldInput?.text.toString(),
                    closeEnd = tf_end_shop_closed?.textFieldInput?.text.toString()
                )
                if (checkReasonRejectIsNotEmpty(tf_cancel_notes?.textFieldInput?.text.toString())) {
                    doRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(getString(R.string.cancel_order_notes_empty_warning), bottomSheetShopClosed.view)
                }
            }
        }

        bottomSheetShopClosed.apply {
            setFullPage(true)
            setTitle(TITLE_ATUR_TOKO_TUTUP)
            setChild(viewBottomSheetShopClosed)
            setCloseClickListener { dismiss() }
        }
        fragmentManager?.let { bottomSheetShopClosed.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun updateClosingEndDate(endDate: String, viewBottomSheet: View) {
        val endNote1 = getString(R.string.shop_closed_endnote1)
        val endNote2 = getString(R.string.shop_closed_endnote2)
        val endNote3 = getString(R.string.shop_closed_endnote3)

        context?.let {
            val customString = HtmlLinkHelper(it, "$endNote1 <b>$endNote2</b>$endNote3 $endDate")
            viewBottomSheet.shop_closed_endnotes?.text = customString.spannedString
        }
    }

    private fun setCourierProblems(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        somBottomSheetCourierProblemsAdapter = SomBottomSheetCourierProblemsAdapter(this)
        somBottomSheetCourierProblemsAdapter.reasonCode = rejectReason.reasonCode.toString()

        bottomSheetCourierProblems = BottomSheetUnify()
        val viewBottomSheetCourierProblems = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetCourierProblemsAdapter
            }

            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_secondary?.visibility = View.VISIBLE
                ticker_penalty_secondary?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_secondary?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_secondary?.visibility = View.GONE
            }

            fl_btn_primary?.visibility = View.VISIBLE
            fl_btn_primary?.setOnClickListener {
                bottomSheetCourierProblems?.dismiss()
                val orderRejectRequest = SomRejectRequest()
                orderRejectRequest.orderId = detailResponse.orderId.toString()
                orderRejectRequest.rCode = rejectReason.reasonCode.toString()

                if (tf_extra_notes?.visibility == View.VISIBLE) {
                    orderRejectRequest.reason = tf_extra_notes?.textFieldInput?.text.toString()
                    if (checkReasonRejectIsNotEmpty(tf_extra_notes?.textFieldInput?.text.toString())) {
                        doRejectOrder(orderRejectRequest)
                    } else {
                        showToasterError(getString(R.string.cancel_order_notes_empty_warning), bottomSheetCourierProblems?.view)
                    }
                } else {
                    orderRejectRequest.reason = reasonCourierProblemText
                    doRejectOrder(orderRejectRequest)
                }
            }
        }

        bottomSheetCourierProblems?.apply {
            setFullPage(true)
            setTitle(TITLE_COURIER_PROBLEM)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetCourierProblems)
        }

        fragmentManager?.let {
            bottomSheetCourierProblems?.show(it, getString(R.string.show_bottomsheet))
        }

        somBottomSheetCourierProblemsAdapter.listChildCourierProblems = rejectReason.listChild.toMutableList()
        somBottomSheetCourierProblemsAdapter.notifyDataSetChanged()
    }

    private fun setBuyerNoResponse(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        val bottomSheetBuyerNoResponse = BottomSheetUnify()

        val viewBottomSheetBuyerNoResponse = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_secondary?.visibility = View.VISIBLE
                ticker_penalty_secondary?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_secondary?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_secondary?.visibility = View.GONE
            }
            rv_bottomsheet_secondary?.visibility = View.GONE
            tf_extra_notes?.visibility = View.VISIBLE
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = getString(R.string.buyer_no_resp_label)
            tf_extra_notes?.setPlaceholder(getString(R.string.buyer_no_resp_placeholder))
            fl_btn_primary?.visibility = View.VISIBLE
            fl_btn_primary?.setOnClickListener {
                bottomSheetBuyerNoResponse.dismiss()

                val orderRejectRequest = SomRejectRequest().apply {
                    orderId = detailResponse.orderId.toString()
                    rCode = rejectReason.reasonCode.toString()
                    reason = tf_extra_notes?.textFieldInput?.text.toString()
                }
                if (checkReasonRejectIsNotEmpty(tf_extra_notes?.textFieldInput?.text.toString())) {
                    doRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(getString(R.string.cancel_order_notes_empty_warning), bottomSheetBuyerNoResponse.view)
                }
            }
        }

        bottomSheetBuyerNoResponse.apply {
            setFullPage(true)
            setOnDismissListener { dismiss() }
            setCloseClickListener { dismiss() }
            setTitle(VALUE_REASON_BUYER_NO_RESPONSE)
            setChild(viewBottomSheetBuyerNoResponse)
        }
        fragmentManager?.let { bottomSheetBuyerNoResponse.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun setOtherReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        val bottomSheetBuyerOtherReason = BottomSheetUnify()
        val viewBottomSheetOtherReason = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_secondary?.visibility = View.VISIBLE
                ticker_penalty_secondary?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_secondary?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_secondary?.visibility = View.GONE
            }

            rv_bottomsheet_secondary?.visibility = View.GONE
            tf_extra_notes?.visibility = View.VISIBLE
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = getString(R.string.other_reason_resp_label)
            tf_extra_notes?.setPlaceholder(getString(R.string.other_reason_resp_placeholder))
            fl_btn_primary?.visibility = View.VISIBLE
            fl_btn_primary?.setOnClickListener {
                bottomSheetBuyerOtherReason.dismiss()

                val orderRejectRequest = SomRejectRequest().apply {
                    orderId = detailResponse.orderId.toString()
                    rCode = rejectReason.reasonCode.toString()
                    reason = tf_extra_notes?.textFieldInput?.text.toString()
                }
                if (checkReasonRejectIsNotEmpty(tf_extra_notes?.textFieldInput?.text.toString())) {
                    doRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(getString(R.string.cancel_order_notes_empty_warning), bottomSheetBuyerOtherReason.view)
                }
            }
        }

        bottomSheetBuyerOtherReason.apply {
            setFullPage(true)
            setOnDismissListener { dismiss() }
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetOtherReason)
            setTitle(VALUE_REASON_OTHER)
        }

        fragmentManager?.let {
            bottomSheetBuyerOtherReason.show(it, getString(R.string.show_bottomsheet))
        }
    }

    override fun onChooseOptionCourierProblem(optionCourierProblem: SomReasonRejectData.Data.SomRejectReason.Child) {
        if (optionCourierProblem.reasonText.equals(VALUE_COURIER_PROBLEM_OTHERS, ignoreCase = true)) {
            bottomSheetCourierProblems?.tf_extra_notes?.visibility = View.VISIBLE
            bottomSheetCourierProblems?.tf_extra_notes?.setLabelStatic(true)
            bottomSheetCourierProblems?.tf_extra_notes?.setPlaceholder(getString(R.string.placeholder_reject_reason))
        } else {
            reasonCourierProblemText = optionCourierProblem.reasonText
            bottomSheetCourierProblems?.tf_extra_notes?.visibility = View.GONE
        }
    }

    private fun doRejectOrder(orderRejectRequest: SomRejectRequest) {
        activity?.resources?.let {
            somDetailViewModel.rejectOrder(GraphqlHelper.loadRawString(it, R.raw.gql_som_reject_order), orderRejectRequest)
        }
        SomAnalytics.eventClickTolakPesanan(detailResponse.statusText, orderRejectRequest.reason)
    }

    private fun observingRejectOrder() {
        somDetailViewModel.rejectOrderResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    rejectOrderResponse = it.data.rejectOrder
                    if (rejectOrderResponse.success == 1) {
                        // if success = 1 : finishActivity, then show toaster
                        activity?.setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(RESULT_REJECT_ORDER, rejectOrderResponse)
                        })
                        activity?.finish()

                    } else {
                        showToasterError(rejectOrderResponse.message.first(), view)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_REJECT_ORDER)
                    if (it.throwable is UnknownHostException) {
                        showNoInternetConnectionToaster()
                    } else {
                        showServerErrorToaster()
                    }
                }
            }
        })
    }

    private fun showToasterError(message: String, view: View?) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, LENGTH_SHORT, TYPE_ERROR, ACTION_OK)
        }
    }

    private fun showCommonToaster(message: String) {
        val toasterCommon = Toaster
        view?.let { v ->
            toasterCommon.make(v, message, LENGTH_SHORT, TYPE_NORMAL)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(tfEndShopClosed: TextFieldUnify, viewBottomSheet: View) {
        context?.let { context ->
            val dateNow = Calendar.getInstance()
            val maxDate = Calendar.getInstance()
            maxDate.add(Calendar.YEAR, 100)

            fragmentManager?.let {
                val datePicker = DatePickerUnify(context, dateNow, dateNow, maxDate)
                datePicker.setTitle(getString(R.string.end_shop_closed_label))
                datePicker.show(it, "")
                datePicker.datePickerButton.setOnClickListener {
                    val resultDate = datePicker.getDate()
                    tfEndShopClosed.textFieldInput.setText("${resultDate[0]}/${resultDate[1] + 1}/${resultDate[2]}")
                    updateClosingEndDate("${resultDate[0]} ${convertMonth(resultDate[1])} ${resultDate[2]}", viewBottomSheet)
                    datePicker.dismiss()
                }
                datePicker.setCloseClickListener { datePicker.dismiss() }
            }
        }
    }

    override fun onDialPhone(strPhoneNo: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val phone = "tel:$strPhoneNo"
        intent.data = Uri.parse(phone)
        startActivity(intent)
    }

    override fun onShowBookingCode(bookingCode: String, bookingType: String) {
        Intent(activity, SomDetailBookingCodeActivity::class.java).apply {
            putExtra(PARAM_BOOKING_CODE, detailResponse.bookingInfo.onlineBooking.bookingCode)
            putExtra(PARAM_BARCODE_TYPE, detailResponse.bookingInfo.onlineBooking.barcodeType)
            startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLAG_CONFIRM_REQ_PICKUP && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
                    val resultProcessReqPickup = data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(RESULT_PROCESS_REQ_PICKUP)
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_PROCESS_REQ_PICKUP, resultProcessReqPickup)
                    })
                    activity?.finish()
                }
            }
        } else if (requestCode == FLAG_CONFIRM_SHIPPING && resultCode == Activity.RESULT_OK) {
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

    override fun onClickProduct(productId: Int) {
        startActivity(RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId.toString()))
    }

    override fun onRefresh(view: View?) {
        rv_detail?.hide()
        containerBtnDetail ?.hide()
        if (isUserRoleFetched(somDetailViewModel.userRoleResult.value)) {
            loadDetail()
        } else {
            checkUserRole()
        }
    }

    private fun openWebview(url: String) {
        startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, url))
    }

    private fun isUserRoleFetched(value: Result<SomGetUserRoleUiModel>?): Boolean = value is Success

    private fun setupBuyerRequestCancelBottomSheetButtons(view: View, bottomSheetReqCancel: BottomSheetUnify, reasonBuyer: String) = with (view) {
        when {
            detailResponse.statusCode == 220 -> {
                btnNegative?.text = getString(R.string.bottomsheet_order_cancellation_response_negative_button_code_220)
                btnPositive?.text = getString(R.string.bottomsheet_order_cancellation_response_positive_button_code_220)
                btnNegative?.setOnClickListener {
                    showBuyerRequestCancelOnClickButtonDialog(
                            title = "Konfirmasi terima pesanan?",
                            description = "Lanjut memproses pesanan tanpa konfirmasi dari pembeli dapat menyebabkan pengajuan komplain",
                            primaryButtonText = "Lanjut Terima",
                            secondaryButtonText = "Kembali",
                            primaryButtonClickAction = {
                                bottomSheetReqCancel.dismiss()
                                acceptOrder(orderId, userSession.userId)
                            }
                    )
                }
                btnPositive?.setOnClickListener {
                    SomAnalytics.eventClickButtonTolakPesananPopup("${detailResponse.statusCode}")
                    showPositiveButtonBuyerRequestCancelOnClickButtonDialog(bottomSheetReqCancel, reasonBuyer)
                }
            }
            detailResponse.statusCode == 400 -> {
                btnNegative?.text = getString(R.string.bottomsheet_order_cancellation_response_negative_button_code_400)
                btnPositive?.text = getString(R.string.bottomsheet_order_cancellation_response_positive_button_code_400)
                btnNegative?.setOnClickListener {
                    showBuyerRequestCancelOnClickButtonDialog(
                            title = "Kirim pesanan ini?",
                            description = "Pastikan kamu sudah berdiskusi dengan pembeli. Pesanan yang diproses secara paksa berpotensi komplain.",
                            primaryButtonText = "Kirim Pesanan",
                            secondaryButtonText = "Kembali",
                            primaryButtonClickAction = {
                                bottomSheetReqCancel.dismiss()
                                performButtonAction(detailResponse.button.firstOrNull(), false)
                            }
                    )
                }
                btnPositive?.setOnClickListener {
                    SomAnalytics.eventClickButtonTolakPesananPopup("${detailResponse.statusCode}")
                    showPositiveButtonBuyerRequestCancelOnClickButtonDialog(bottomSheetReqCancel, reasonBuyer)
                }
            }
            else -> containerButtonBuyerRequestCancel?.gone()
        }
    }

    private fun showPositiveButtonBuyerRequestCancelOnClickButtonDialog(bottomSheetReqCancel: BottomSheetUnify, reasonBuyer: String) {
        showBuyerRequestCancelOnClickButtonDialog(
                title = "Batalkan pesanan ini?",
                description = "Skor performa tokomu tidak akan dikurangi untuk pembatalan ini.",
                primaryButtonText = "Batalkan Pesanan",
                secondaryButtonText = "Kembali",
                primaryButtonClickAction = {
                    bottomSheetReqCancel.dismiss()
                    val orderRejectRequest = SomRejectRequest(
                            orderId = detailResponse.orderId.toString(),
                            rCode = "0",
                            reason = reasonBuyer
                    )
                    doRejectOrder(orderRejectRequest)
                }
        )
    }

    private fun showBuyerRequestCancelOnClickButtonDialog(
            title: String,
            description: String,
            primaryButtonText: String,
            secondaryButtonText: String,
            primaryButtonClickAction: () -> Unit) {
        context?.run {
            DialogUnify(this, HORIZONTAL_ACTION, NO_IMAGE).apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryButtonText)
                setSecondaryCTAText(secondaryButtonText)
                setPrimaryCTAClickListener {
                    primaryButtonClickAction()
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
                show()
            }
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
            if (this@SomDetailFragment.errorToaster?.isShown == true)
                this@SomDetailFragment.errorToaster?.dismiss()

            this@SomDetailFragment.errorToaster = Toaster.build(
                        this,
                        message,
                        LENGTH_SHORT,
                        TYPE_ERROR)
            this@SomDetailFragment.errorToaster?.show()
        }
    }

    private fun showNoInternetConnectionToaster() {
        showErrorToaster("Oops, koneksi internetmu terganggu. Silahkan cek koneksi internet dan coba lagi, ya.")
    }

    private fun showServerErrorToaster() {
        showErrorToaster("Oops, ada gangguan yang perlu kami bereskan. Silahkan coba lagi, ya.")
    }
}
