package com.tokopedia.affiliate.feature.explore.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreProductViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterListViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel

/**
 * @author by yfsx on 24/09/18.
 */
class ExploreAdapter(adapterTypeFactory: ExploreTypeFactory, visitables: List<Visitable<*>>) : BaseAdapter<ExploreTypeFactory>(adapterTypeFactory, visitables) {

    val data: List<Visitable<*>>
        get() = visitables

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun clearProductElements() {
        visitables.removeAll {
            it is ExploreProductViewModel
        }
        notifyDataSetChanged()
    }

    fun removeEmptySearch() {
        visitables.removeAll {
            it is ExploreEmptySearchViewModel
        }
        notifyDataSetChanged()
    }

    fun getFilterList(): List<FilterViewModel> {
        return (visitables.firstOrNull { it is FilterListViewModel } as? FilterListViewModel)?.filters
                ?: arrayListOf()
    }

    fun setFilterList(filterList: List<FilterViewModel>) {
        (visitables.firstOrNull { it is FilterListViewModel } as? FilterListViewModel)?.filters =
                filterList.toMutableList()
        notifyDataSetChanged()
    }
}
