package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewModel
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewModel
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewModel
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewModel

interface SuggestionTypeFactory {

    fun type(viewModel: SuggestionTitleViewModel): Int

    fun type(viewModel: SuggestionSingleLineViewModel): Int

    fun type(viewModel: SuggestionDoubleLineViewModel): Int

    fun type(viewModel: SuggestionTopShopWidgetViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}