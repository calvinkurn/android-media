package com.tokopedia.autocomplete.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel

interface InitialStateTypeFactory {

    fun type(viewModel: PopularSearchViewModel): Int

    fun type(viewModel: RecentSearchViewModel): Int

    fun type(viewModel: RecentViewViewModel): Int

    fun type(viewModel: PopularSearchTitleViewModel): Int

    fun type(viewModel: RecentSearchTitleViewModel): Int

    fun type(viewModel: ReecentViewTitleViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}