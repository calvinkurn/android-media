package com.tokopedia.autocompletecomponent.suggestion.topshop

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

interface SuggestionTopShopTypeFactory {
    fun type(type: String): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}