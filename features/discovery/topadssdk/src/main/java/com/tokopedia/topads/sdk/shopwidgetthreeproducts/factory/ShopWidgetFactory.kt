package com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory

import android.view.View
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.*
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.viewholder.AbstractViewHolder

interface ShopWidgetFactory {

    fun type(productItemModel: ProductItemModel): Int
    fun type(showMoreItemModel: ShowMoreItemModel): Int
    fun type(emptyShopCardModel: EmptyShopCardModel): Int
    fun type(shopWidgetShimmerUiModel: ShopWidgetShimmerUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}
