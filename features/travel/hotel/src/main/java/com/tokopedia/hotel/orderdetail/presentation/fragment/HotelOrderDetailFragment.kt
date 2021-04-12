package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.TravelCrossSellingGQLQuery
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.presentation.adapter.TravelCrossSellAdapter
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.common.travel.utils.TrackingCrossSellUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.fragment.HotelBookingFragment
import com.tokopedia.hotel.booking.presentation.widget.HotelBookingBottomSheets
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_ORDER_DETAIL
import com.tokopedia.hotel.evoucher.presentation.activity.HotelEVoucherActivity
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.data.model.TitleContent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_CATEGORY
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_ID
import com.tokopedia.hotel.orderdetail.presentation.activity.SeeInvoiceActivity
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter
import com.tokopedia.hotel.orderdetail.presentation.adapter.TitleTextAdapter
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelContactPhoneBottomSheet
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelRefundBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_hotel_order_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.view.*
import kotlinx.android.synthetic.main.layout_order_detail_payment_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_transaction_detail.*
import javax.inject.Inject


/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailFragment : HotelBaseFragment(), ContactAdapter.OnClickCallListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var orderDetailViewModel: HotelOrderDetailViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false
    private var isCrossSellingLoaded = false
    private var isOrderDetailLoaded = false

    @Inject
    lateinit var trackingCrossSellUtil: TrackingCrossSellUtil

    lateinit var remoteConfig: RemoteConfig

    private var orderId: String = ""
    private var orderCategory: String = ""

    override fun getScreenName(): String = getString(R.string.hotel_order_detail_title)

    override fun initInjector() = getComponent(HotelOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_ORDER_DETAIL)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            orderDetailViewModel = viewModelProvider.get(HotelOrderDetailViewModel::class.java)
        }

        arguments?.let {
            orderId = it.getString(KEY_ORDER_ID, "")
            orderCategory = it.getString(KEY_ORDER_CATEGORY, "")
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderDetailViewModel.orderDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderTransactionDetail(it.data)
                    renderConditionalInfo(it.data)
                    renderCancellationInfo(it.data.hotelTransportDetails)
                    if (it.data.hotelTransportDetails.propertyDetail.isNotEmpty())
                        renderHotelDetail(it.data.hotelTransportDetails.propertyDetail.first())
                    renderGuestDetail(it.data.hotelTransportDetails.guestDetail)
                    renderPaymentDetail(it.data.payMethod, it.data.pricing, it.data.paymentsData)
                    renderFooter(it.data)
                    loadingState.visibility = View.GONE
                }
                is Fail -> {
                    showErrorState(it.throwable)
                    loadingState.visibility = View.GONE
                }
            }
            isOrderDetailLoaded = true
            stopTrace()
        })

        orderDetailViewModel.crossSellData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderCrossSelling(it.data)
                is Fail -> {
                }
            }
            isCrossSellingLoaded = true
            stopTrace()
        })
    }


    override fun onErrorRetryClicked() {
        getOrderDetailData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_KEY_ORDER_ID) &&
                savedInstanceState.containsKey(SAVED_KEY_ORDER_CATEGORY)) {
            orderId = savedInstanceState.getString(SAVED_KEY_ORDER_ID) ?: ""
            orderCategory = savedInstanceState.getString(SAVED_KEY_ORDER_CATEGORY) ?: ""
        }

        loadingState.visibility = View.VISIBLE

        getOrderDetailData()
    }

    private fun getOrderDetailData() {
        if (userSessionInterface.isLoggedIn) {
            if (remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_TRAVEL_ENABLE_CROSS_SELL)) {
                orderDetailViewModel.getOrderDetail(
                        HotelGqlQuery.ORDER_DETAILS,
                        TravelCrossSellingGQLQuery.QUERY_CROSS_SELLING,
                        orderId, orderCategory)
            } else {
                orderDetailViewModel.getOrderDetail(
                        HotelGqlQuery.ORDER_DETAILS,
                        null,
                        orderId, orderCategory)
            }


        } else RouteManager.route(context, ApplinkConst.LOGIN)
    }

    private fun renderConditionalInfo(hotelOrderDetail: HotelOrderDetail) {

        top_conditional_text.visibility = if (hotelOrderDetail.conditionalInfo.title.isNotBlank()) View.VISIBLE else View.GONE
        top_conditional_text.text = hotelOrderDetail.conditionalInfo.title

        bottom_conditional_text.visibility = if (hotelOrderDetail.conditionalInfoBottom.title.isNotBlank()) View.VISIBLE else View.GONE
        bottom_conditional_text.text = Html.fromHtml(hotelOrderDetail.conditionalInfoBottom.title)
    }

    private fun renderCancellationInfo(hotelTransportDetail: HotelTransportDetail) {

        if (hotelTransportDetail.cancellation.title.isEmpty()) {
            refund_ticker.visibility = View.GONE
        } else {
            refund_ticker.visibility = View.VISIBLE
            refund_ticker.tickerTitle = hotelTransportDetail.cancellation.title
            if (hotelTransportDetail.cancellation.isClickable)
                refund_ticker.setHtmlDescription(getString(R.string.hotel_order_detail_refund_ticker, hotelTransportDetail.cancellation.content))
            else refund_ticker.setHtmlDescription(hotelTransportDetail.cancellation.content)
            refund_ticker.closeButtonVisibility = View.GONE

            refund_ticker.setOnClickListener {
                if (hotelTransportDetail.cancellation.isClickable)
                    showRefundInfo(hotelTransportDetail.cancellation.cancellationPolicies)
            }
        }

        if (hotelTransportDetail.contactInfo.isNotEmpty()) {
            call_hotel_layout.setOnClickListener { showCallButtonSheet(hotelTransportDetail.contactInfo) }
        } else call_hotel_layout.visibility = View.GONE

    }

    private fun renderTransactionDetail(orderDetail: HotelOrderDetail) {

        transaction_status.text = orderDetail.status.statusText
        if (orderDetail.status.textColor.isNotEmpty()){
            transaction_status.setTextColor(Color.parseColor(orderDetail.status.textColor))
        }else{
            transaction_status.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400_96))
        }

        var transactionDetailAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        transaction_detail_title_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        transaction_detail_title_recycler_view.adapter = transactionDetailAdapter
        for (transactionDetails in orderDetail.title) {
            transactionDetailAdapter.addData(TitleContent(transactionDetails.label, transactionDetails.value))
        }
        transactionDetailAdapter.notifyDataSetChanged()

        if (orderDetail.invoice.invoiceRefNum.isBlank()) {
            invoice_layout.visibility = View.GONE
        } else {
            invoice_layout.visibility = View.VISIBLE
            invoice_number.text = orderDetail.invoice.invoiceRefNum
            invoice_see_button.visibility = if (orderDetail.invoice.invoiceUrl.isNotBlank()) View.VISIBLE else View.GONE
            if (orderDetail.invoice.invoiceUrl.isNotBlank()) {
                invoice_see_button.setOnClickListener {
                    startActivity(
                            SeeInvoiceActivity.newInstance(
                                    requireContext(),
                                    orderDetail.invoice.invoiceUrl,
                                    orderDetail.invoice.invoiceRefNum
                            )
                    )
                }
            }
        }

        if (orderDetail.hotelTransportDetails.isShowEVoucher) {
            evoucher_layout.visibility = View.VISIBLE
            evoucher_layout.setOnClickListener {
                goToEvoucherPage()
            }
        } else evoucher_layout.visibility = View.GONE

    }

    private fun renderCrossSelling(crossSelling: TravelCrossSelling) {
        if (crossSelling.items.isNotEmpty()) {
            trackingCrossSellUtil.crossSellImpression(crossSelling.items)
            cross_sell_widget.show()
            cross_sell_widget.buildView(crossSelling)
            cross_sell_widget.setListener(object : TravelCrossSellAdapter.OnItemClickListener {
                override fun onItemClickListener(item: TravelCrossSelling.Item, position: Int) {
                    trackingCrossSellUtil.crossSellClick(item, position)
                    RouteManager.route(context, item.uri)
                }

            })
        } else cross_sell_widget.hide()
    }

    private fun goToEvoucherPage() {
        context?.run { startActivity(HotelEVoucherActivity.getCallingIntent(this, orderId)) }
    }

    private fun renderHotelDetail(propertyDetail: HotelTransportDetail.PropertyDetail) {

        if (propertyDetail.bookingKey.content.isNotEmpty()) {
            hideBookingCode(false)
            booking_code.text = propertyDetail.bookingKey.content
        } else hideBookingCode(true)

        hotel_name.text = propertyDetail.propertyInfo.name
        hotel_address.text = propertyDetail.propertyInfo.address

        if (propertyDetail.room.isNotEmpty()) {
            room_name.text = propertyDetail.room.first().title
            room_info.text = propertyDetail.room.first().content

            for (amenity in propertyDetail.room.first().amenities) {
                if (context != null) {
                    val amenityTextView = Typography(requireContext())
                    amenityTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    amenityTextView.text = amenity.content
                    room_amenities.addView(amenityTextView)
                }
            }
        }

        if (propertyDetail.specialRequest.content.isNotEmpty()) {
            special_request_recycler_view.visibility = View.VISIBLE
            var specialRequestAdapter = TitleTextAdapter(TitleTextAdapter.VERTICAL_LAYOUT)
            special_request_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            special_request_recycler_view.adapter = specialRequestAdapter
            specialRequestAdapter.addData(mutableListOf(propertyDetail.specialRequest))
        } else special_request_recycler_view.visibility = View.GONE

        if (propertyDetail.extraInfo.content.isNotBlank()) {
            if (propertyDetail.extraInfo.isClickable) special_notes.setText(createHyperlinkText(propertyDetail.extraInfo.content,
                    propertyDetail.extraInfo.longDesc, R.string.hotel_order_detail_additional_info), TextView.BufferType.SPANNABLE)
            else special_notes.text = propertyDetail.extraInfo.content
            special_notes.visibility = View.VISIBLE
            special_notes.movementMethod = LinkMovementMethod.getInstance()
        } else special_notes.visibility = View.GONE

        if (propertyDetail.checkInOut.size >= 2) {
            checkin_checkout_date.setRoomDatesFormatted(
                    propertyDetail.checkInOut[0].checkInOut.date,
                    propertyDetail.checkInOut[1].checkInOut.date,
                    propertyDetail.stayLength.content)

            checkin_checkout_date.setRoomCheckTimes(
                    getString(R.string.hotel_order_detail_day_and_time,
                            propertyDetail.checkInOut[0].checkInOut.day, propertyDetail.checkInOut[0].checkInOut.time),
                    getString(R.string.hotel_order_detail_day_and_time,
                            propertyDetail.checkInOut[1].checkInOut.day, propertyDetail.checkInOut[1].checkInOut.time))
        }

        see_hotel_detail_button.setOnClickListener { RouteManager.route(context, propertyDetail.applink) }
    }

    private fun showCallButtonSheet(contactList: List<HotelTransportDetail.ContactInfo>) {
        val bottomSheet = HotelContactPhoneBottomSheet()
        bottomSheet.contactList = contactList
        bottomSheet.listener = this
        activity?.let { bottomSheet.show(it.supportFragmentManager, TAG_CONTACT_INFO) }
    }

    private fun showRefundInfo(cancellationPolicies: List<HotelTransportDetail.Cancellation.CancellationPolicy>) {
        val bottomSheet = HotelRefundBottomSheet()
        bottomSheet.cancellationPolicies = cancellationPolicies
        activity?.let { bottomSheet.show(it.supportFragmentManager, TAG_CANCELLATION_INFO) }
    }

    private fun renderGuestDetail(guestDetail: TitleContent) {
        guest_detail_name_hint.text = guestDetail.title
        guest_detail_name.text = guestDetail.content
    }

    private fun renderPaymentDetail(payMethod: List<HotelOrderDetail.LabelValue>,
                                    pricing: List<HotelOrderDetail.PaymentData>,
                                    paymentData: List<HotelOrderDetail.PaymentData>) {

        if (payMethod.isEmpty()) {
            payment_info_recycler_view.visibility = View.GONE
            payment_seperator_1.visibility = View.GONE
        } else {
            var paymentAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LEFT_LAYOUT)
            payment_info_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            payment_info_recycler_view.adapter = paymentAdapter
            for (item in payMethod) {
                paymentAdapter.addData(TitleContent(item.label, item.value))
            }
        }

        var faresAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        payment_fares_recycler_View.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_fares_recycler_View.adapter = faresAdapter
        for (item in pricing) {
            faresAdapter.addData(TitleContent(item.label, item.value))
        }

        var summaryAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT_ORANGE)
        payment_summary_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_summary_recycler_view.adapter = summaryAdapter
        for (item in paymentData) {
            summaryAdapter.addData(TitleContent(item.label, item.value))
        }
    }

    fun renderFooter(orderDetail: HotelOrderDetail) {

        order_detail_footer_layout.removeAllViews()
        if (orderDetail.contactUs.helpText.isNotBlank() && context != null) {
            val helpLabel = Typography(requireContext())
            helpLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            helpLabel.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96))

            val spannableString = createHyperlinkText(orderDetail.contactUs.helpText,
                    orderDetail.contactUs.helpUrl)

            helpLabel.highlightColor = Color.TRANSPARENT
            helpLabel.movementMethod = LinkMovementMethod.getInstance()
            helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE)
            helpLabel.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            helpLabel.layoutParams = params

            order_detail_footer_layout.addView(helpLabel)
        }


        for (button in orderDetail.actionButtons) {
            context?.let {
                val buttonCompat = UnifyButton(it)
                buttonCompat.text = button.label
                buttonCompat.isAllCaps = false
                buttonCompat.buttonSize = UnifyButton.Size.MEDIUM

                if (button.weight == 1) {
                    buttonCompat.background = ContextCompat.getDrawable(it, R.drawable.bg_hotel_rect_rounded_stroke_gray)
                    buttonCompat.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N200))
                } else if (button.weight == 2) {
                    buttonCompat.buttonType = UnifyButton.Type.TRANSACTION
                }
                buttonCompat.setOnClickListener {
                    if (button.uri.isNotBlank()) RouteManager.route(context, button.uri)
                    else if (button.uriWeb.isNotBlank()) RouteManager.route(context, button.uriWeb)
                }
                order_detail_footer_layout.addView(buttonCompat)
            }
        }
    }

    private fun createHyperlinkText(htmlText: String = "", content: String = "", @StringRes resId: Int = 0): SpannableString {

        val text = if (resId == 0) TextHtmlUtils.getTextFromHtml(htmlText) else TextHtmlUtils.getTextFromHtml(getString(resId, htmlText))
        val spannableString = SpannableString(text)
        val hyperlinkIndex = htmlText.toLowerCase().indexOf("<hyperlink>")
        val endIndexOfLink = htmlText.toLowerCase().indexOf("</hyperlink>")
        if (hyperlinkIndex >= 0) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    try {
                        if (resId == 0) RouteManager.route(context, "tokopedia://webview?url=$content")
                        else onImportantNotesClicked(content)
                    } catch (e: Exception) {
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    context?.let {
                        ds.color = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G400) // specific color for this link
                    }
                }
            }, hyperlinkIndex, endIndexOfLink - "<hyperlink>".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (resId != 0) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    onImportantNotesClicked(content)
                }
            }, text.indexOf("<font"), text.length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    private fun onImportantNotesClicked(notes: String) {
        val importantNotesBottomSheets = HotelBookingBottomSheets()
        activity?.let {
            context?.let { ctx ->
                val textView = Typography(ctx)
                textView.text = notes
                importantNotesBottomSheets.setTitle(getString(R.string.hotel_important_info_title))
                importantNotesBottomSheets.addContentView(textView)
                importantNotesBottomSheets.show(it.supportFragmentManager, HotelBookingFragment.TAG_HOTEL_IMPORTANT_NOTES)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_order_detail, container, false)


    private fun hideBookingCode(enableHide: Boolean) {
        booking_code_hint.visibility = if (enableHide) View.GONE else View.VISIBLE
        booking_code.visibility = if (enableHide) View.GONE else View.VISIBLE
        order_hotel_detail.seperator_1.visibility = if (enableHide) View.GONE else View.VISIBLE
    }

    override fun onClickCall(contactNumber: String) {
        val callIntent = Intent(ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$contactNumber")
        startActivity(callIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_KEY_ORDER_ID, orderId)
        outState.putString(SAVED_KEY_ORDER_CATEGORY, orderCategory)
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            if (isOrderDetailLoaded && isCrossSellingLoaded) {
                performanceMonitoring?.stopTrace()
                isTraceStop = true
            }
        }
    }

    companion object {
        fun getInstance(orderId: String, orderCategory: String): HotelOrderDetailFragment =
                HotelOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(KEY_ORDER_ID, orderId)
                        putString(KEY_ORDER_CATEGORY, orderCategory)
                    }
                }

        const val TAG_CONTACT_INFO = "guestContactInfo"
        const val TAG_CANCELLATION_INFO = "cancellationPolicyInfo"

        const val SAVED_KEY_ORDER_ID = "keyOrderId"
        const val SAVED_KEY_ORDER_CATEGORY = "keyOrderCategory"
    }
}