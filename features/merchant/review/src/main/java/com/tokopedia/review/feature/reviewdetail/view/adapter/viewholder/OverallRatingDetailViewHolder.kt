package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.review.R
import com.tokopedia.review.common.util.toggle
import com.tokopedia.review.feature.reviewdetail.view.adapter.OverallRatingDetailListener
import com.tokopedia.review.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography


class OverallRatingDetailViewHolder(val view: View,
                                    private val listener: OverallRatingDetailListener): AbstractViewHolder<OverallRatingDetailUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = com.tokopedia.review.R.layout.item_overall_review_detail
    }

    private val ratingStar: Typography = view.findViewById(R.id.rating_star_overall)
    private val totalReview: Typography = view.findViewById(R.id.total_review)
    private val reviewPeriod: ChipsUnify = view.findViewById(R.id.review_period_filter_button_detail)
    private val ticker: Ticker = view.findViewById(R.id.rating_disclaimer_product_ticker)

    override fun bind(element: OverallRatingDetailUiModel?) {

        ratingStar.text = element?.ratingAvg.toString()

        val strReview = getString(R.string.review_text)
        val ratingCount = element?.reviewCount.toString()
        val strReviewSpan = SpannableString("$ratingCount $strReview")
        strReviewSpan.setSpan(StyleSpan(Typeface.BOLD), 0, ratingCount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        totalReview.text = strReviewSpan

        reviewPeriod.apply {
            chip_text.text = element?.chipFilter.orEmpty()
            setOnClickListener {
                toggle()
                listener.onFilterPeriodClicked(view, getString(R.string.title_bottom_sheet_filter))
            }
            setChevronClickListener {
                toggle()
                listener.onFilterPeriodClicked(view, getString(R.string.title_bottom_sheet_filter))
            }
        }

        ticker.apply {
            shouldShowWithAction(!element?.tickerText.isNullOrEmpty() && listener.shouldShowTickerForRatingDisclaimer()) {
                setHtmlDescription(element?.tickerText ?: "")
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(
                            context,
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl)
                        )
                    }

                    override fun onDismiss() {
                        listener.updateSharedPreference()
                    }
                })
            }
        }
    }

}