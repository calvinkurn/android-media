package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.data.model.TitleContent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter
import com.tokopedia.hotel.orderdetail.presentation.adapter.TitleTextAdapter
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelContactPhoneBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_order_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_payment_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_transaction_detail.*
import javax.inject.Inject
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_CATEGORY
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_ID
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.view.*


/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailFragment : BaseDaggerFragment(), ContactAdapter.OnClickCallListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var orderDetailViewModel: HotelOrderDetailViewModel

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
                    if (it.data.hotelTransportDetails.isNotEmpty()) {
                        renderConditionalInfo(it.data.hotelTransportDetails.first())
                        renderHotelDetail(it.data.hotelTransportDetails.first().propertyDetail.first())
                        renderGuestDetail(it.data.hotelTransportDetails.first().guestDetail)
                        renderPaymentDetail(it.data.hotelTransportDetails.first().payment)
                    }
                    renderFooter(it.data)
                }
                is Fail -> {
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_KEY_ORDER_ID) &&
                savedInstanceState.containsKey(SAVED_KEY_ORDER_CATEGORY)) {
            orderId = savedInstanceState.getString(SAVED_KEY_ORDER_ID)!!
            orderCategory = savedInstanceState.getString(SAVED_KEY_ORDER_CATEGORY)
        }

        orderDetailViewModel.getOrderDetail(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_order_list_detail),
                orderId, orderCategory)
    }

    fun renderConditionalInfo(hotelTransportDetail: HotelTransportDetail) {

        top_conditional_text.visibility = if (hotelTransportDetail.conditionalInfoTop.title.isNotBlank()) View.VISIBLE else View.GONE
        top_conditional_text.text = hotelTransportDetail.conditionalInfoTop.title

        bottom_conditional_text.visibility = if (hotelTransportDetail.conditionalInfoBottom.title.isNotBlank()) View.VISIBLE else View.GONE
        bottom_conditional_text.text = hotelTransportDetail.conditionalInfoBottom.title

        call_hotel_layout.setOnClickListener { showCallButtonSheet(hotelTransportDetail.contactInfo) }
    }

    fun renderTransactionDetail(orderDetail: HotelOrderDetail) {

        transaction_status.text = orderDetail.status.statusText
        when (orderDetail.status.status) {
            ORDER_STATUS_FAIL -> transaction_status.setTextColor(resources.getColor(R.color.red_pink))
            ORDER_STATUS_SUCCESS -> transaction_status.setTextColor(resources.getColor(R.color.tkpd_main_green))
        }

        var transactionDetailAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        transaction_detail_title_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        transaction_detail_title_recycler_view.adapter = transactionDetailAdapter
        for (transactionDetails in orderDetail.title) {
            transactionDetailAdapter.addData(TitleContent(transactionDetails.label, transactionDetails.value))
        }
        transactionDetailAdapter.notifyDataSetChanged()

        invoice_number.text = orderDetail.invoice.invoiceRefNum
        invoice_see_button.visibility = if (orderDetail.invoice.invoiceUrl.isNotBlank()) View.VISIBLE else View.GONE
        if (orderDetail.invoice.invoiceUrl.isNotBlank()) {
            invoice_see_button.setOnClickListener {
                RouteManager.route(context, orderDetail.invoice.invoiceUrl)
            }
        }
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

        var specialRequestAdapter = TitleTextAdapter(TitleTextAdapter.VERTICAL_LAYOUT)
        special_request_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        special_request_recycler_view.adapter = specialRequestAdapter
        specialRequestAdapter.addData(propertyDetail.specialRequest.toMutableList())

        special_notes.text = propertyDetail.extraInfo

        checkin_checkout_date.setRoomDatesFormatted(
                propertyDetail.checkInOut[0].checkInOut.date,
                propertyDetail.checkInOut[1].checkInOut.date,
                propertyDetail.checkInOut[2].content)
    }

    fun showCallButtonSheet(contactList: List<HotelTransportDetail.ContactInfo>) {
        val bottomSheet = HotelContactPhoneBottomSheet()
        bottomSheet.contactList = contactList
        bottomSheet.listener = this
        bottomSheet.show(activity!!.supportFragmentManager, TAG_CONTACT_INFO)
    }

    fun renderGuestDetail(guestDetail: TitleContent) {
        guest_detail_name_hint.text = guestDetail.title
        guest_detail_name.text = guestDetail.content
    }

    fun renderPaymentDetail(payment: HotelTransportDetail.Payment) {
        payment_info_hint.text = payment.title

        var paymentAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        payment_info_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_info_recycler_view.adapter = paymentAdapter
        paymentAdapter.addData(payment.detail.toMutableList())

        var faresAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        payment_fares_recycler_View.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_fares_recycler_View.adapter = faresAdapter
        faresAdapter.addData(payment.fares.toMutableList())

        var summaryAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT_ORANGE)
        payment_summary_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_summary_recycler_view.adapter = summaryAdapter
        summaryAdapter.addData(payment.summary.toMutableList())
    }

    fun renderFooter(orderDetail: HotelOrderDetail) {

        order_detail_footer_layout.removeAllViews()

        val helpText = TextViewCompat(context)
        helpText.setFontSize(TextViewCompat.FontSize.MICRO)
        helpText.setTextColor(resources.getColor(R.color.light_primary))
        helpText.text = Html.fromHtml(orderDetail.contactUs.helpText)
        helpText.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_16)
        helpText.layoutParams = params
        order_detail_footer_layout.addView(helpText)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_order_detail, container, false)


    fun hideBookingCode(enableHide: Boolean) {
        booking_code_hint.visibility = if (enableHide) View.GONE else View.VISIBLE
        booking_code.visibility = if (enableHide) View.GONE else View.VISIBLE
        order_hotel_detail.seperator_1.visibility = if (enableHide) View.GONE else View.VISIBLE
    }

    override fun onClickCall(contactNumber: String) {
        Toast.makeText(context, contactNumber, Toast.LENGTH_SHORT).show()
        val callIntent = Intent(ACTION_DIAL)
        callIntent.setData(Uri.parse("tel:${contactNumber}"))
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
        const val ORDER_STATUS_SUCCESS = 700
        const val ORDER_STATUS_FAIL = 600

        const val SAVED_KEY_ORDER_ID = "keyOrderId"
        const val SAVED_KEY_ORDER_CATEGORY = "keyOrderCategory"
    }
}