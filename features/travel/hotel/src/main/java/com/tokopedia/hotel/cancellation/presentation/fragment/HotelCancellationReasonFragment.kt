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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.adapter.HotelCancellationReasonAdapter
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_cancellation_reason.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationReasonFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cancellationViewModel: HotelCancellationViewModel

    lateinit var reasonAdapter: HotelCancellationReasonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancellationViewModel.getCancellationData(GraphqlHelper.loadRawString(resources, R.raw.dummycancellation))
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

    fun initView(hotelCancellationModel: HotelCancellationModel) {
        hotel_cancellation_page_footer.highlightColor = Color.TRANSPARENT
        hotel_cancellation_page_footer.movementMethod = LinkMovementMethod.getInstance()
        hotel_cancellation_page_footer.setText(createHyperlinkText(hotelCancellationModel.footer.desc, hotelCancellationModel.footer.links), TextView.BufferType.SPANNABLE)

        hotel_cancellation_reason_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        hotel_cancellation_reason_rv.setHasFixedSize(true)
        hotel_cancellation_reason_rv.isNestedScrollingEnabled = false
        reasonAdapter = HotelCancellationReasonAdapter()
        reasonAdapter.updateItems(hotelCancellationModel.reasons)
        hotel_cancellation_reason_rv.adapter = reasonAdapter
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_cancellation_reason, container, false)

    /*
     * PLEASE DON'T REVIEW FOR THIS FUNCTION YET
     * func: createHyperlinkText
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