package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel

interface ShopListTypeFactory : SearchSectionTypeFactory {
    fun type(shopHeader: ShopHeaderViewModel): Int

    fun type(shopItem: ShopViewModel.ShopItem): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
