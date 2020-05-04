package com.tokopedia.hotel.cancellation.presentation.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.cancellation.presentation.widget.HotelCancellationRefundDetailWidget
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_cancellation.*
import kotlinx.android.synthetic.main.layout_hotel_cancellation_refund_detail.*
import kotlinx.android.synthetic.main.layout_hotel_cancellation_summary.*
import kotlinx.android.synthetic.main.widget_hotel_cancellation_policy.*
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var cancellationViewModel: HotelCancellationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_cancellation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancellationViewModel.getCancellationData(GraphqlHelper.loadRawString(resources, R.raw.dummycancellation))
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).injectl(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    initView(it.data)
                }
                is Fail -> {
                }
            }
        })
    }

    private fun initView(hotelCancellationModel: HotelCancellationModel) {
        hotelCancellationModel.property.let {
            hotel_cancellation_property_name.text = it.name
            hotel_cancellation_room_name.text = it.room.firstOrNull()?.roomName ?: ""
            hotel_cancellation_room_guest_info.text = it.room.firstOrNull()?.roomContent ?: ""

            hotel_cancellation_room_duration_view.setViewLabel(it.checkInOut[0].title, it.checkInOut[1].title)
            hotel_cancellation_room_duration_view.setRoomDatesFormatted(it.checkInOut[0].checkInOut.date, it.checkInOut[1].checkInOut.date, it.stayLength)
            hotel_cancellation_room_duration_view.setRoomCheckTimes("${it.checkInOut[0].checkInOut.day}, ${it.checkInOut[0].checkInOut.time}", "${it.checkInOut[1].checkInOut.day}, ${it.checkInOut[1].checkInOut.time}")
        }

        hotelCancellationModel.cancelPolicy.let {
            hotel_cancellation_policy_title.text = it.title
            hotel_cancellation_policy_widget.initView("Ketentuan Pembatalan", it.policy)
        }

        hotelCancellationModel.cancelInfo.let {
            hotel_cancellation_ticker_refund_info.setHtmlDescription(it.desc)
            hotel_cancellation_ticker_refund_info.isClickable = it.isClickable
            hotel_cancellation_ticker_refund_info.tickerShape = Ticker.SHAPE_LOOSE
            hotel_cancellation_ticker_refund_info.tickerType = Ticker.TYPE_INFORMATION
        }

        hotelCancellationModel.payment.let {
            hotel_cancellation_payment_title.text = it.title

            hotel_cancellation_refund_price_detail.removeAllViews()
            for (paymentDetail in it.detail) {
                val widgetDetail = context?.let { HotelCancellationRefundDetailWidget(it) }
                widgetDetail?.let { widget ->
                    widget.initView(paymentDetail.title, paymentDetail.amount)
                    hotel_cancellation_refund_price_detail.addView(widget)
                }
            }

            hotel_cancellation_total_price_refund.removeAllViews()
            for (paymentSummary in it.summary) {
                val widgetDetail = context?.let { HotelCancellationRefundDetailWidget(it) }
                widgetDetail?.let { widget ->
                    widget.initView(paymentSummary.title, paymentSummary.amount, true)
                    hotel_cancellation_total_price_refund.addView(widget)
                }
            }

            hotel_cancellation_refund_additional_text.text = it.footer.desc
        }

        hotel_cancellation_page_footer.text = hotelCancellationModel.footer.desc
    }

}