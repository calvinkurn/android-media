package com.tokopedia.autocomplete.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.*
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewHolder
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewHolder
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel

class InitialStateAdapterTypeFactory(
        private val clickListener: InitialStateItemClickListener
) : BaseAdapterTypeFactory(), InitialStateTypeFactory {
    override fun type(viewModel: PopularSearchTitleViewModel): Int {
        return PopularSearchTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: RecentSearchTitleViewModel): Int {
        return RecentSearchTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: ReecentViewTitleViewModel): Int {
        return RecentViewTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: PopularSearchViewModel): Int {
        return PopularSearchViewHolder.LAYOUT
    }

    override fun type(viewModel: RecentSearchViewModel): Int {
        return RecentSearchViewHolder.LAYOUT
    }

    override fun type(viewModel: RecentViewViewModel): Int {
        return RecentViewViewHolder.LAYOUT
    }

    override fun type(viewModelInitialState: RecentSearchSeeMoreViewModel): Int {
        return RecentSearchSeeMoreViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            PopularSearchViewHolder.LAYOUT -> PopularSearchViewHolder(parent, clickListener)
            RecentSearchViewHolder.LAYOUT -> RecentSearchViewHolder(parent, clickListener)
            RecentViewViewHolder.LAYOUT -> RecentViewViewHolder(parent, clickListener)
            PopularSearchTitleViewHolder.LAYOUT -> PopularSearchTitleViewHolder(parent, clickListener)
            RecentSearchTitleViewHolder.LAYOUT -> RecentSearchTitleViewHolder(parent, clickListener)
            RecentViewTitleViewHolder.LAYOUT -> RecentViewTitleViewHolder(parent)
            RecentSearchSeeMoreViewHolder.LAYOUT -> RecentSearchSeeMoreViewHolder(parent, clickListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}