package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

interface SuggestionTypeFactory {

    fun type(viewModel: SuggestionTitleViewModel): Int

    fun type(viewModel: SuggestionSingleLineViewModel): Int

    fun type(viewModel: SuggestionDoubleLineViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}