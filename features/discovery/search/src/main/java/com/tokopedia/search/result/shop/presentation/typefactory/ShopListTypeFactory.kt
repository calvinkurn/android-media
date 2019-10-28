package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.shop.presentation.model.*

internal interface ShopListTypeFactory {

    fun type(shopCpmViewModel: ShopCpmViewModel): Int

    fun type(shopTotalCountViewModel: ShopTotalCountViewModel): Int

    fun type(shopItem: ShopViewModel.ShopItem): Int

    fun type(shopEmptySearchViewModel: ShopEmptySearchViewModel): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>

}
