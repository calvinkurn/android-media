package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.adapter.OverallRatingDetailListener
import com.tokopedia.review.feature.reviewdetail.view.model.BadRatingReasonTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class BadRatingReasonTickerViewHolder(val view: View,
                                    private val listener: OverallRatingDetailListener
): AbstractViewHolder<BadRatingReasonTickerUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_bad_rating_reason_ticker
    }
    private val ticker: Ticker = view.findViewById(R.id.rating_disclaimer_product_ticker)

    override fun bind(element: BadRatingReasonTickerUiModel) {
        ticker.apply {
            shouldShowWithAction(element.tickerText.isNotBlank() && listener.shouldShowTickerForRatingDisclaimer()) {
                setHtmlDescription(element.tickerText)
                setDescriptionClickEvent (object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                        listener.updateSharedPreference()
                    }
                })
            }
        }
    }

}