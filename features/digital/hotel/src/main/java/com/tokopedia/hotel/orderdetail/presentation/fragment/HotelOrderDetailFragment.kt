package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.data.model.TitleContent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.adapter.TitleTextAdapter
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_order_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_hotel_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_payment_detail.*
import kotlinx.android.synthetic.main.layout_order_detail_transaction_detail.*
import javax.inject.Inject

/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var orderDetailViewModel: HotelOrderDetailViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(HotelOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            orderDetailViewModel = viewModelProvider.get(HotelOrderDetailViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderDetailViewModel.orderDetailData.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderTransactionDetail(it.data)
                    if (it.data.hotelTransportDetails.isNotEmpty()) {
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

        orderDetailViewModel.getOrderDetail("")
    }

    fun renderTransactionDetail(orderDetail: HotelOrderDetail) {

        transaction_status.text = orderDetail.status.statusText

        var transactionDetailAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        transaction_detail_title_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        transaction_detail_title_recycler_view.adapter = transactionDetailAdapter
        for (transactionDetails in orderDetail.title) {
            transactionDetailAdapter.addData(TitleContent(transactionDetails.label, transactionDetails.value))
        }
        transactionDetailAdapter.notifyDataSetChanged()

        invoice_number.text = orderDetail.invoice.invoiceRefNum

    }

    fun renderHotelDetail(propertyDetail: HotelTransportDetail.PropertyDetail) {
        booking_code.text = propertyDetail.bookingKey.content
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

        var summaryAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        payment_summary_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        payment_summary_recycler_view.adapter = summaryAdapter
        summaryAdapter.addData(payment.summary.toMutableList())
    }

    fun renderFooter(orderDetail: HotelOrderDetail) {
        val helpText = TextViewCompat(context)
        helpText.setFontSize(TextViewCompat.FontSize.MICRO)
        helpText.setTextColor(resources.getColor(R.color.light_primary))
        helpText.text = Html.fromHtml(orderDetail.contactUs.helpText)
        helpText.gravity = Gravity.CENTER
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
            order_detail_footer_layout.addView(buttonCompat)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_order_detail, container, false)


    companion object {
        fun getInstance(): HotelOrderDetailFragment = HotelOrderDetailFragment()
    }
}