package com.tokopedia.shop.settings.basicinfo.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.basicinfo.view.model.ShopDomainSuggestion
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopDomainSuggestionViewHolder

class ShopDomainSuggestionAdapterFactoryImpl(
    private val onClickDomainSuggestion: (String) -> Unit
): BaseAdapterTypeFactory(), ShopDomainSuggestionAdapterFactory {

    override fun type(viewModel: ShopDomainSuggestion): Int = ShopDomainSuggestionViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ShopDomainSuggestionViewHolder.LAYOUT -> ShopDomainSuggestionViewHolder(parent, onClickDomainSuggestion)
            else -> super.createViewHolder(parent, type)
        }
    }
}