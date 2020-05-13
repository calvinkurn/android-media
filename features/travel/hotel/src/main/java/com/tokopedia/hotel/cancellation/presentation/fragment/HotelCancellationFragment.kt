package com.tokopedia.hotel.cancellation.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationActivity
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.cancellation.presentation.widget.HotelCancellationRefundDetailWidget
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.HotelTextHyperlinkUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
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

class HotelCancellationFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cancellationViewModel: HotelCancellationViewModel

    private val cancelInfoBottomSheet = BottomSheetUnify()

    private var invoiceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            invoiceId = it.getString(EXTRA_INVOICE_ID) ?: ""
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_hotel_cancellation, container, false)
    }


    override fun onErrorRetryClicked() {
        showLoadingState()
        getCancellationData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (cancellationViewModel.cancellationData.value == null) showLoadingState()
        getCancellationData()
    }

    private fun getCancellationData() {
        cancellationViewModel.getCancellationData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_hotel_cancellation_data), invoiceId)
    }

    override fun onResume() {
        super.onResume()
        (activity as HotelCancellationActivity).updateSubtitle(getString(R.string.hotel_cancellation_page_1_subtitle))
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationData.observe(this, androidx.lifecycle.Observer {
            hideLoadingState()
            when (it) {
                is Success -> {
                    initView(it.data)
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        })
    }

    private fun initView(hotelCancellationModel: HotelCancellationModel) {
        hotelCancellationModel.property.let {
            hotel_cancellation_property_name.text = it.name
            hotel_cancellation_room_name.text = it.room.firstOrNull()?.roomName ?: ""
            hotel_cancellation_room_guest_info.text = it.room.firstOrNull()?.roomContent ?: ""

            val checkIn = it.checkInOut.firstOrNull()
                    ?: HotelCancellationModel.PropertyData.CheckInOut()
            val checkOut = if (it.checkInOut.size > 1) it.checkInOut[1] else HotelCancellationModel.PropertyData.CheckInOut()
            hotel_cancellation_room_duration_view.setViewLabel(checkIn.title, checkOut.title)
            hotel_cancellation_room_duration_view.setRoomDatesFormatted(checkIn.checkInOut.date, checkOut.checkInOut.date, it.stayLength)
            hotel_cancellation_room_duration_view.setRoomCheckTimes("${checkIn.checkInOut.day}, ${checkIn.checkInOut.time}",
                    "${checkIn.checkInOut.day}, ${checkOut.checkInOut.time}")
        }

        hotelCancellationModel.cancelPolicy.let {
            hotel_cancellation_policy_title.text = it.title
            hotel_cancellation_policy_widget.initView(getString(R.string.hotel_cancellation_page_title), it.policy)
        }

        hotelCancellationModel.cancelInfo.let {
            if (it.desc.isEmpty()) {
                hotel_cancellation_ticker_refund_info.hide()
            } else {
                hotel_cancellation_ticker_refund_info.setHtmlDescription(it.desc)
                hotel_cancellation_ticker_refund_info.isClickable = it.isClickable
                hotel_cancellation_ticker_refund_info.tickerShape = Ticker.SHAPE_LOOSE
                hotel_cancellation_ticker_refund_info.tickerType = Ticker.TYPE_ANNOUNCEMENT

                if (it.isClickable) {
                    cancelInfoBottomSheet.setTitle(it.longDesc.title)
                    val typography = AppCompatTextView(requireContext())
                    typography.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.body_3))
                    typography.text = TextHtmlUtils.getTextFromHtml(it.longDesc.desc)
                    typography.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
                    typography.setMargin(0, 0, 0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2))
                    typography.setLineSpacing(6f, 1f)
                    typography.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
                    cancelInfoBottomSheet.setChild(typography)

                    hotel_cancellation_ticker_refund_info.setOnClickListener {
                        fragmentManager?.let { fm -> cancelInfoBottomSheet.show(fm, "") }
                    }
                }
            }
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

            if (it.footer.desc.isNotEmpty()) {
                val spannable = HotelTextHyperlinkUtil.getSpannedFromHtmlString(requireContext(),
                        it.footer.desc, it.footer.links)
                hotel_cancellation_refund_additional_text.highlightColor = Color.TRANSPARENT
                hotel_cancellation_refund_additional_text.movementMethod = LinkMovementMethod.getInstance()
                hotel_cancellation_refund_additional_text.setText(spannable, TextView.BufferType.SPANNABLE)
            }  else hotel_cancellation_refund_additional_text.hide()

        }

        if (hotelCancellationModel.footer.desc.isNotEmpty()) {
            hotel_cancellation_page_footer.highlightColor = Color.TRANSPARENT
            hotel_cancellation_page_footer.movementMethod = LinkMovementMethod.getInstance()
            hotel_cancellation_page_footer.setText(HotelTextHyperlinkUtil.getSpannedFromHtmlString(requireContext(),
                    hotelCancellationModel.footer.desc, hotelCancellationModel.footer.links), TextView.BufferType.SPANNABLE)
        } else hotel_cancellation_page_footer.hide()

        hotel_cancellation_button_next.setOnClickListener {
            (activity as HotelCancellationActivity).showCancellationReasonFragment()
        }
    }

    companion object {
        private const val EXTRA_INVOICE_ID = "extra_invoice_id"
        fun getInstance(invoiceId: String): HotelCancellationFragment =
                HotelCancellationFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }
}