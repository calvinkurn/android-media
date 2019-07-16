package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModelKt

interface ShopListTypeFactory : SearchSectionTypeFactory {
    fun type(shopItem: ShopViewModelKt.ShopItem): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
