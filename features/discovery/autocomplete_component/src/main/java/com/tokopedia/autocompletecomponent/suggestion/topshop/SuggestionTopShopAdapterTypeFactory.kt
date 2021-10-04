package com.tokopedia.autocompletecomponent.suggestion.topshop

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.suggestion.SUGGESTION_TOP_SHOP
import com.tokopedia.autocompletecomponent.suggestion.SUGGESTION_TOP_SHOP_SEE_MORE

class SuggestionTopShopAdapterTypeFactory(
        private val suggestionTopShopListener: SuggestionTopShopListener
) : BaseAdapterTypeFactory(), SuggestionTopShopTypeFactory {

    override fun type(type: String): Int {
        return when(type) {
            SUGGESTION_TOP_SHOP -> SuggestionTopShopCardViewHolder.LAYOUT
            SUGGESTION_TOP_SHOP_SEE_MORE -> SuggestionTopShopSeeMoreViewHolder.LAYOUT
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SuggestionTopShopCardViewHolder.LAYOUT -> SuggestionTopShopCardViewHolder(parent, suggestionTopShopListener)
            SuggestionTopShopSeeMoreViewHolder.LAYOUT -> SuggestionTopShopSeeMoreViewHolder(parent, suggestionTopShopListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}