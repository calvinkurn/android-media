package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.InboxReputationOvoIncentiveViewModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_ovo_incentive_ticker.view.*

class InboxReputationOvoIncentiveViewHolder(view: View, private val viewListener: com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation.View) : AbstractViewHolder<InboxReputationOvoIncentiveViewModel>(view) {

    companion object {
        val LAYOUT: Int = com.tokopedia.review.R.layout.item_ovo_incentive_ticker
    }

    override fun bind(element: InboxReputationOvoIncentiveViewModel) {
        with(element.productRevIncentiveOvoDomain) {
            productrevIncentiveOvo?.let {
                val title = it.ticker.title
                itemView.ovoPointsTicker.apply {
                    tickerTitle = title
                    setHtmlDescription(it.ticker.subtitle)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(charSequence: CharSequence) {
                            viewListener.onClickOvoIncentiveTickerDescription(this@with)
                        }

                        override fun onDismiss() {
                            viewListener.onDismissOvoIncentiveTicker(title)
                        }
                    })
                    show()
                }
            }
        }
    }

}