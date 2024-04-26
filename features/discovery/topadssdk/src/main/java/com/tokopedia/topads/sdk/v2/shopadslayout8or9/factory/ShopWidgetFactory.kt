package com.tokopedia.topads.sdk.v2.shopadslayout8or9.factory

import android.view.View
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.EmptyShopCardModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ProductItemModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShopWidgetShimmerUiModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShowMoreItemModel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.AbstractViewHolder

interface ShopWidgetFactory {

    fun type(productItemModel: ProductItemModel): Int
    fun type(showMoreItemModel: ShowMoreItemModel): Int
    fun type(emptyShopCardModel: EmptyShopCardModel): Int
    fun type(shopWidgetShimmerUiModel: ShopWidgetShimmerUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
