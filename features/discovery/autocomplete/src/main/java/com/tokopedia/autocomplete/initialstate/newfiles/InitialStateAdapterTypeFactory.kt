package com.tokopedia.autocomplete.initialstate.newfiles

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.adapter.ItemClickListener
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewHolder
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewHolder
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewHolder
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewHolder

class InitialStateAdapterTypeFactory(private val clickListener: ItemClickListener) : BaseAdapterTypeFactory(), InitialStateTypeFactory {
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

    override fun type(viewModel: RecentViewSearchViewModel): Int {
        return RecentViewViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        if (type == PopularSearchViewHolder.LAYOUT) {
            viewHolder = PopularSearchViewHolder(parent, clickListener)
        } else if (type == RecentSearchViewHolder.LAYOUT) {
            viewHolder = RecentSearchViewHolder(parent, clickListener)
        } else if (type == RecentViewViewHolder.LAYOUT) {
            viewHolder = RecentViewViewHolder(parent, clickListener)
        }else if(type == PopularSearchTitleViewHolder.LAYOUT) {
            viewHolder = PopularSearchTitleViewHolder(parent, clickListener)
        } else if(type == RecentSearchTitleViewHolder.LAYOUT) {
            viewHolder = RecentSearchTitleViewHolder(parent, clickListener)
        } else if(type == RecentViewTitleViewHolder.LAYOUT) {
            viewHolder = RecentViewTitleViewHolder(parent)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}