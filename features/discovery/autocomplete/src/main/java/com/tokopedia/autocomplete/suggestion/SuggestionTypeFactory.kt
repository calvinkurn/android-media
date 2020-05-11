package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewModel
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewModel
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewModel

interface SuggestionTypeFactory {

    fun type(viewModel: SuggestionTitleViewModel): Int

    fun type(viewModel: SuggestionSingleLineViewModel): Int

    fun type(viewModel: SuggestionDoubleLineViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}