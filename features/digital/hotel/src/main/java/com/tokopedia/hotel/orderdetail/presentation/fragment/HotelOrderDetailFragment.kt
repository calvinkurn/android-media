package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.evoucher.presentation.activity.HotelEVoucherActivity
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.data.model.TitleContent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_CATEGORY
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_ID
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter
import com.tokopedia.hotel.orderdetail.presentation.adapter.TitleTextAdapter
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelContactPhoneBottomSheet
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelRefundBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_hotel_order_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.view.*
import kotlinx.android.synthetic.main.layout_order_detail_payment_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_transaction_detail.*
import java.io.UnsupportedEncodingException
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

    private var orderId: String = ""
    private var orderCategory: String = ""

    override fun getScreenName(): String = getString(R.string.hotel_order_detail_title)

    override fun initInjector() = getComponent(HotelOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            orderDetailViewModel = viewModelProvider.get(HotelOrderDetailViewModel::class.java)
        }

        arguments?.let {
            orderId = it.getString(KEY_ORDER_ID, "")
            orderCategory = it.getString(KEY_ORDER_CATEGORY, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderDetailViewModel.orderDetailData.observe(this, Observer {
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
                }
            }
        })
    }


    override fun onErrorRetryClicked() {
        getOrderDetailData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_KEY_ORDER_ID) &&
                savedInstanceState.containsKey(SAVED_KEY_ORDER_CATEGORY)) {
            orderId = savedInstanceState.getString(SAVED_KEY_ORDER_ID)!!
            orderCategory = savedInstanceState.getString(SAVED_KEY_ORDER_CATEGORY)
        }

        loadingState.visibility = View.VISIBLE

        getOrderDetailData()
    }

    fun getOrderDetailData() {
        if (userSessionInterface.isLoggedIn) {
            orderDetailViewModel.getOrderDetail(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_order_list_detail),
                    orderId, orderCategory)
        } else RouteManager.route(context, ApplinkConst.LOGIN)
    }

    fun renderConditionalInfo(hotelOrderDetail: HotelOrderDetail) {

        top_conditional_text.visibility = if (hotelOrderDetail.conditionalInfo.title.isNotBlank()) View.VISIBLE else View.GONE
        top_conditional_text.text = hotelOrderDetail.conditionalInfo.title

        bottom_conditional_text.visibility = if (hotelOrderDetail.conditionalInfoBottom.title.isNotBlank()) View.VISIBLE else View.GONE
        bottom_conditional_text.text = Html.fromHtml(hotelOrderDetail.conditionalInfoBottom.title)
    }

    fun renderCancellationInfo(hotelTransportDetail: HotelTransportDetail) {

//        if (hotelTransportDetail.cancellation.title.isEmpty()) {
//            refund_ticker.visibility = View.GONE
//        } else {
//            refund_ticker.visibility = View.VISIBLE
//            refund_ticker.tickerTitle = hotelTransportDetail.cancellation.title
//            refund_ticker.setHtmlDescription(hotelTransportDetail.cancellation.content)
//
//            refund_ticker.setOnClickListener {
//                if (hotelTransportDetail.cancellation.isClickable)
//                    showRefundInfo(hotelTransportDetail.cancellation.cancellationPolicies)
//            }
//        }

        if (hotelTransportDetail.contactInfo.isNotEmpty()) {
            call_hotel_layout.setOnClickListener { showCallButtonSheet(hotelTransportDetail.contactInfo) }
        } else call_hotel_layout.visibility = View.GONE

    }

    fun renderTransactionDetail(orderDetail: HotelOrderDetail) {

        transaction_status.text = orderDetail.status.statusText
        if (orderDetail.status.textColor.isNotEmpty())
            transaction_status.setTextColor(Color.parseColor(orderDetail.status.textColor))

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
                    RouteManager.route(context, orderDetail.invoice.invoiceUrl)
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

    fun goToEvoucherPage() {
        startActivity(HotelEVoucherActivity.getCallingIntent(context!!, orderId))
    }

    fun renderHotelDetail(propertyDetail: HotelTransportDetail.PropertyDetail) {

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
                val amenityTextView = TextViewCompat(context)
                amenityTextView.setFontSize(TextViewCompat.FontSize.MICRO)
                amenityTextView.text = amenity.content
                room_amenities.addView(amenityTextView)
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
            special_notes.setText(createHyperlinkText(propertyDetail.extraInfo.content,
                    propertyDetail.extraInfo.uri), TextView.BufferType.SPANNABLE)
            special_notes.visibility = View.VISIBLE
            special_notes.movementMethod = LinkMovementMethod.getInstance()
        } else special_notes.visibility = View.GONE

        checkin_checkout_date.setRoomDatesFormatted(
                propertyDetail.checkInOut[0].checkInOut.date,
                propertyDetail.checkInOut[1].checkInOut.date,
                propertyDetail.stayLength.content)

        see_hotel_detail_button.setOnClickListener { RouteManager.route(context, propertyDetail.applink) }
    }

    fun showCallButtonSheet(contactList: List<HotelTransportDetail.ContactInfo>) {
        val bottomSheet = HotelContactPhoneBottomSheet()
        bottomSheet.contactList = contactList
        bottomSheet.listener = this
        bottomSheet.show(activity!!.supportFragmentManager, TAG_CONTACT_INFO)
    }

    fun showRefundInfo(cancellationPolicies: List<HotelTransportDetail.Cancellation.CancellationPolicy>) {
        val bottomSheet = HotelRefundBottomSheet()
        bottomSheet.cancellationPolicies = cancellationPolicies
        bottomSheet.show(activity!!.supportFragmentManager, TAG_CANCELLATION_INFO)
    }

    fun renderGuestDetail(guestDetail: TitleContent) {
        guest_detail_name_hint.text = guestDetail.title
        guest_detail_name.text = guestDetail.content
    }

    fun renderPaymentDetail(payMethod: List<HotelOrderDetail.LabelValue>,
                            pricing: List<HotelOrderDetail.PaymentData>,
                            paymentData: List<HotelOrderDetail.PaymentData>) {

        var paymentAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        payment_info_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_info_recycler_view.adapter = paymentAdapter
        for (item in payMethod) {
            paymentAdapter.addData(TitleContent(item.label, item.value))
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
        if (orderDetail.contactUs.helpText.isNotBlank()){
            val helpLabel = TextViewCompat(context)
            helpLabel.setFontSize(TextViewCompat.FontSize.MICRO)
            helpLabel.setTextColor(resources.getColor(R.color.light_primary))

            val spannableString = createHyperlinkText(orderDetail.contactUs.helpText,
                    orderDetail.contactUs.helpUrl)

            helpLabel.highlightColor = Color.TRANSPARENT
            helpLabel.movementMethod = LinkMovementMethod.getInstance()
            helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE)
            helpLabel.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_16)
            helpLabel.layoutParams = params

            order_detail_footer_layout.addView(helpLabel)
        }


        for (button in orderDetail.actionButtons) {
            val buttonCompat = ButtonCompat(context)
            buttonCompat.text = button.label
            buttonCompat.isAllCaps = false

            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.topMargin = resources.getDimensionPixelSize(R.dimen.dp_8)
            buttonCompat.layoutParams = params

            if (button.weight == 1) {
                buttonCompat.background = resources.getDrawable(R.drawable.rect_white_rounded_stroke_gray)
                buttonCompat.setTextColor(resources.getColor(R.color.grey_500))
            } else if (button.weight == 2) {
                buttonCompat.buttonCompatType = ButtonCompat.TRANSACTION
            }
            buttonCompat.setOnClickListener {
                if (button.uri.isNotBlank()) RouteManager.route(context, button.uri)
                else if (button.uriWeb.isNotBlank()) RouteManager.route(context, button.uriWeb)
            }
            order_detail_footer_layout.addView(buttonCompat)
        }
    }

    fun createHyperlinkText(htmlText: String = "", url: String = ""): SpannableString {

        val text = Html.fromHtml(htmlText)
        val spannableString = SpannableString(text)
        val startIndexOfLink = htmlText.toLowerCase().indexOf("<hyperlink>") + "<hyperlink>".length
        val endIndexOfLink = htmlText.toLowerCase().indexOf("</hyperlink>")
        if (startIndexOfLink >= 0) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    try {
                        RouteManager.route(context, url)
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = resources.getColor(R.color.green_250) // specific color for this link
                }
            }, startIndexOfLink - "<hyperlink>".length, endIndexOfLink - "<hyperlink>".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_order_detail, container, false)


    private fun hideBookingCode(enableHide: Boolean) {
        booking_code_hint.visibility = if (enableHide) View.GONE else View.VISIBLE
        booking_code.visibility = if (enableHide) View.GONE else View.VISIBLE
        order_hotel_detail.seperator_1.visibility = if (enableHide) View.GONE else View.VISIBLE
    }

    override fun onClickCall(contactNumber: String) {
        Toast.makeText(context, contactNumber, Toast.LENGTH_SHORT).show()
        val callIntent = Intent(ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$contactNumber")
        startActivity(callIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_KEY_ORDER_ID, orderId)
        outState.putString(SAVED_KEY_ORDER_CATEGORY, orderCategory)
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