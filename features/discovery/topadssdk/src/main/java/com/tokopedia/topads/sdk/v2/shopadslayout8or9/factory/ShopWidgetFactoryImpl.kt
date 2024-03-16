package com.tokopedia.topads.sdk.v2.shopadslayout8or9.factory

import android.view.View
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.EmptyShopCardModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ProductItemModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShopWidgetShimmerUiModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShowMoreItemModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.EmptyShopCardViewHolder
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.ProductItemViewHolder
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.ShopWidgetShimmerViewHolder
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.ShowMoreItemVieHolder

class ShopWidgetFactoryImpl(
    private val shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?
) :
    ShopWidgetFactory {

    override fun type(productItemModel: ProductItemModel): Int {
        return ProductItemViewHolder.LAYOUT
    }

    override fun type(showMoreItemModel: ShowMoreItemModel): Int {
        return ShowMoreItemVieHolder.LAYOUT
    }


    override fun type(emptyShopCardModel: EmptyShopCardModel): Int {
        return EmptyShopCardViewHolder.LAYOUT
    }

    override fun type(shopWidgetShimmerUiModel: ShopWidgetShimmerUiModel): Int {
        return ShopWidgetShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(
                view,
                topAdsBannerClickListener,
                impressionListener,
                shopWidgetAddToCartClickListener
            )
            ShowMoreItemVieHolder.LAYOUT -> ShowMoreItemVieHolder(view, topAdsBannerClickListener)
            EmptyShopCardViewHolder.LAYOUT -> EmptyShopCardViewHolder(view, topAdsBannerClickListener)
            ShopWidgetShimmerViewHolder.LAYOUT -> ShopWidgetShimmerViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}
