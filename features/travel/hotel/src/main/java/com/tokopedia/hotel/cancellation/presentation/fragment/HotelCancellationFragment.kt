package com.tokopedia.hotel.cancellation.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationActivity
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.cancellation.presentation.widget.HotelCancellationRefundDetailWidget
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_cancellation.*
import kotlinx.android.synthetic.main.layout_hotel_cancellation_refund_detail.*
import kotlinx.android.synthetic.main.layout_hotel_cancellation_summary.*
import kotlinx.android.synthetic.main.widget_hotel_cancellation_policy.*
import java.util.regex.Matcher
import java.util.regex.Pattern
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

        /*
         Data is still dummy data
         */
        cancellationViewModel.getCancellationData(GraphqlHelper.loadRawString(resources, R.raw.dummycancellation))
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
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

            val checkIn = it.checkInOut.firstOrNull()?: HotelCancellationModel.PropertyData.CheckInOut()
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
            hotel_cancellation_ticker_refund_info.setHtmlDescription(it.desc)
            hotel_cancellation_ticker_refund_info.isClickable = it.isClickable
            hotel_cancellation_ticker_refund_info.tickerShape = Ticker.SHAPE_LOOSE
            hotel_cancellation_ticker_refund_info.tickerType = Ticker.TYPE_ANNOUNCEMENT
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
            val spannable = createHyperlinkText(it.footer.desc, it.footer.links)
            hotel_cancellation_refund_additional_text.highlightColor = Color.TRANSPARENT
            hotel_cancellation_refund_additional_text.movementMethod = LinkMovementMethod.getInstance()
            hotel_cancellation_refund_additional_text.setText(spannable, TextView.BufferType.SPANNABLE)
        }

        hotel_cancellation_page_footer.highlightColor = Color.TRANSPARENT
        hotel_cancellation_page_footer.movementMethod = LinkMovementMethod.getInstance()
        hotel_cancellation_page_footer.setText(createHyperlinkText(hotelCancellationModel.footer.desc,
                hotelCancellationModel.footer.links), TextView.BufferType.SPANNABLE)

        hotel_cancellation_button_next.setOnClickListener {
            (activity as HotelCancellationActivity).showCancellationReasonFragment()
        }
    }

    /*
     * PLEASE DON'T REVIEW FOR THIS FUNCTION YET
     * func: createHyperlinkText()
     */
    private fun createHyperlinkText(htmlText: String = "", urls: List<String> = listOf()): SpannableString {

        var htmlTextCopy = htmlText
        val text = TextHtmlUtils.getTextFromHtml(htmlTextCopy)
        val spannableString = SpannableString(text)

        val matcherHyperlinkOpenTag: Matcher = Pattern.compile("<hyperlink>").matcher(htmlTextCopy)
        htmlTextCopy = htmlTextCopy.replace("</hyperlink>", "<hhyperlink>")
        val matcherHyperlinkCloseTag: Matcher = Pattern.compile("<hhyperlink>").matcher(htmlTextCopy)
        val posOpenTags: MutableList<Int> = mutableListOf()
        val posCloseTags: MutableList<Int> = mutableListOf()
        while (matcherHyperlinkOpenTag.find()) {
            posOpenTags.add(matcherHyperlinkOpenTag.start())
        }
        while (matcherHyperlinkCloseTag.find()) {
            posCloseTags.add(matcherHyperlinkCloseTag.start())
        }

        for ((index, tag) in posOpenTags.withIndex()) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    RouteManager.route(context, urls[index])
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Green_G500) // specific color for this link
                }
            }, tag - (index * ("<hyperlink></hyperlink>".length)),
                    posCloseTags[index] - ((index * ("<hyperlink></hyperlink>".length)) + "<hyperlink>".length),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

}