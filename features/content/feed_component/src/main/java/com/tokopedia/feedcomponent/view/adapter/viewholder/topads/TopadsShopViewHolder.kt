package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import kotlinx.android.synthetic.main.item_topads_shop.view.*

/**
 * @author by milhamj on 08/01/19.
 */
class TopadsShopViewHolder(v: View,
                           private val itemClickListener: TopAdsItemClickListener,
                           private val cardTitleListener: CardTitleView.CardTitleListener)
    : AbstractViewHolder<TopadsShopViewModel>(v) {

    private var cardAdapter: RecommendationCardAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_shop
    }

    override fun bind(element: TopadsShopViewModel?) {
        if (element == null) {
            itemView.gone()
            return
        }

        itemView.topadsShop.bind(element.dataList)
        itemView.topadsShop.setItemClickListener(itemClickListener)

        itemView.cardTitle.bind(element.title, element.template.cardrecom.title)
        itemView.cardTitle.listener = cardTitleListener
    }

    override fun bind(element: TopadsShopViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        val position = payloads[0] as Int
        if (cardAdapter?.list?.size ?: 0 > position) {
            cardAdapter?.notifyItemChanged(position)
        }
    }
}