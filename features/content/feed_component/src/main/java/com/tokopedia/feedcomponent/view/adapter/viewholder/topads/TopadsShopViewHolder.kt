package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.old.listener.TopAdsItemClickListener
import com.tokopedia.topads.sdk.v2.dynamicfeedshop.adapter.DynamicFeedShopAdapter
import com.tokopedia.topads.sdk.v2.dynamicfeedshop.widget.TopAdsDynamicFeedShopView

/**
 * @author by milhamj on 08/01/19.
 */
class TopadsShopViewHolder(
    v: View, private val topadsShopListener: TopadsShopListener?,
    private val cardTitleListener: CardTitleView.CardTitleListener?
) : AbstractViewHolder<TopadsShopUiModel>(v), TopAdsItemClickListener,
    DynamicFeedShopAdapter.TopAdsShopImpressionListener {
    
    private val viewPaddingBottom: View = itemView.findViewById(R.id.viewPaddingBottom)
    private val topadsShop: TopAdsDynamicFeedShopView = itemView.findViewById(R.id.topadsShop)
    private val cardTitle: CardTitleView = itemView.findViewById(R.id.cardTitle)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_shop
    }

    override fun bind(element: TopadsShopUiModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        if (element.dataList.isNotEmpty()) {
            viewPaddingBottom.visible()
            topadsShop.bind(element.dataList)
            topadsShop.setItemClickListener(this)
            topadsShop.setImpressionListener(this)
        } else {
            viewPaddingBottom.gone()
        }

        if (element.title.text.isNotEmpty()) {
            cardTitle.visible()
            cardTitle.bind(
                element.title,
                element.template.cardrecom.title,
                adapterPosition
            )
            cardTitle.listener = cardTitleListener
        } else {
            cardTitle.gone()
        }

    }

    override fun bind(element: TopadsShopUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        val position = payloads[0] as Int
        topadsShop.notifyItemChanged(position)
    }

    override fun onProductItemClicked(position: Int, product: Product) {
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {
        topadsShopListener?.onShopItemClicked(adapterPosition, position, shop)
    }

    override fun onAddFavorite(position: Int, data: Data) {
        topadsShopListener?.onAddFavorite(adapterPosition, position, data)
    }

    override fun onViewRecycled() {
        topadsShop.onViewRecycled()
    }

    interface TopadsShopListener {
        fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: Shop)

        fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: Data)

        fun onTopAdsImpression(url: String, shopId: String, shopName: String, imageUrl: String)
    }

    override fun onImpressionShopAds(
        url: String,
        shopId: String,
        shopName: String,
        imageUrl: String
    ) {
        topadsShopListener?.onTopAdsImpression(url, shopId, shopName, imageUrl)
    }
}
