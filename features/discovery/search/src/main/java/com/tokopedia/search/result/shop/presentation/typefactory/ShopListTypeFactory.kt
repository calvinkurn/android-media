package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory
import com.tokopedia.search.result.shop.presentation.model.EmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel

interface ShopListTypeFactory : SearchSectionTypeFactory {
    fun type(shopHeader: ShopHeaderViewModel): Int

    fun type(shopItem: ShopViewModel.ShopItem): Int

    fun type(emptySearchViewModel: EmptySearchViewModel): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
