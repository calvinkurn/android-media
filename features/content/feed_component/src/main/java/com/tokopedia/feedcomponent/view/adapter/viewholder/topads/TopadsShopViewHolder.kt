package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import kotlinx.android.synthetic.main.item_topads_shop.view.*

/**
 * @author by milhamj on 08/01/19.
 */
class TopadsShopViewHolder(v: View,
                           private val topadsShopListener: TopadsShopViewHolder.TopadsShopListener,
                           private val cardTitleListener: CardTitleView.CardTitleListener)
    : AbstractViewHolder<TopadsShopViewModel>(v), TopAdsItemClickListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_shop
    }

    override fun bind(element: TopadsShopViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        itemView.topadsShop.bind(element.dataList)
        itemView.topadsShop.setItemClickListener(this)

        itemView.cardTitle.bind(element.title, element.template.cardrecom.title)
        itemView.cardTitle.listener = cardTitleListener

    }

    override fun bind(element: TopadsShopViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        val position = payloads[0] as Int
        itemView.topadsShop.notifyItemChanged(position)
    }

    override fun onProductItemClicked(position: Int, product: Product) {
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {
        topadsShopListener.onShopItemClicked(adapterPosition, position, shop)
    }

    override fun onAddFavorite(position: Int, data: Data) {
        topadsShopListener.onAddFavorite(adapterPosition, position, data)
    }

    fun onViewRecycled() {
        itemView.topadsShop.onViewRecycled()
    }

    interface TopadsShopListener {
        fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: Shop)

        fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: Data)
    }
}