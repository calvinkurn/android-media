package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.feature.inbox.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_ovo_incentive_ticker.view.*

class ReviewPendingOvoIncentiveViewHolder(view: View, private val reviewPendingItemListener: ReviewPendingItemListener) : AbstractViewHolder<ReviewPendingOvoIncentiveUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_ovo_incentive_ticker
    }

    override fun bind(element: ReviewPendingOvoIncentiveUiModel) {
        with(element.productRevIncentiveOvoDomain) {
            productrevIncentiveOvo?.ticker?.let {
                itemView.ovoPointsTicker.apply {
                    setHtmlDescription(it.subtitle)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            reviewPendingItemListener.onClickOvoIncentiveTickerDescription(this@with)
                        }

                        override fun onDismiss() {
                            reviewPendingItemListener.onDismissOvoIncentiveTicker(it.subtitle)
                        }
                    })
                    show()
                }
                itemView.addOnImpressionListener(ImpressHolder()) {
                    ReviewTracking.onSuccessGetIncentiveOvoTracker(it.subtitle, ReviewInboxTrackingConstants.PENDING_TAB)
                }
            }
        }
    }
}