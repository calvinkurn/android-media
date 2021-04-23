package com.tokopedia.autocomplete.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignViewHolder
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewHolder
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewHolder
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductListTitleViewHolder
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductListViewHolder
import com.tokopedia.autocomplete.initialstate.recentsearch.*
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewHolder
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewHolder
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleDataView

class InitialStateAdapterTypeFactory(
        private val clickListener: InitialStateItemClickListener
) : BaseAdapterTypeFactory(), InitialStateTypeFactory {
    override fun type(popularSearchTitleDataView: PopularSearchTitleDataView): Int {
        return PopularSearchTitleViewHolder.LAYOUT
    }

    override fun type(recentSearchTitleDataView: RecentSearchTitleDataView): Int {
        return RecentSearchTitleViewHolder.LAYOUT
    }

    override fun type(recentViewTitleDataView: RecentViewTitleDataView): Int {
        return RecentViewTitleViewHolder.LAYOUT
    }

    override fun type(popularSearchDataView: PopularSearchDataView): Int {
        return PopularSearchViewHolder.LAYOUT
    }

    override fun type(recentSearchDataView: RecentSearchDataView): Int {
        return RecentSearchViewHolder.LAYOUT
    }

    override fun type(recentViewDataView: RecentViewDataView): Int {
        return RecentViewViewHolder.LAYOUT
    }

    override fun type(recentSearchSeeMoreDataView: RecentSearchSeeMoreDataView): Int {
        return RecentSearchSeeMoreViewHolder.LAYOUT
    }

    override fun type(dynamicInitialStateSearchDataView: DynamicInitialStateSearchDataView): Int {
        return DynamicInitialStateViewHolder.LAYOUT
    }

    override fun type(dynamicInitialStateTitleDataView: DynamicInitialStateTitleDataView): Int {
        return DynamicInitialStateTitleViewHolder.LAYOUT
    }

    override fun type(curatedCampaignDataView: CuratedCampaignDataView): Int {
        return CuratedCampaignViewHolder.LAYOUT
    }

    override fun type(initialStateProductListDataView: InitialStateProductListDataView): Int {
        return InitialStateProductListViewHolder.LAYOUT
    }

    override fun type(initialStateProductLineTitleDataView: InitialStateProductLineTitleDataView): Int {
        return InitialStateProductListTitleViewHolder.LAYOUT
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
            DynamicInitialStateTitleViewHolder.LAYOUT -> DynamicInitialStateTitleViewHolder(parent, clickListener)
            DynamicInitialStateViewHolder.LAYOUT -> DynamicInitialStateViewHolder(parent, clickListener)
            CuratedCampaignViewHolder.LAYOUT -> CuratedCampaignViewHolder(parent, clickListener)
            InitialStateProductListViewHolder.LAYOUT -> InitialStateProductListViewHolder(parent, clickListener)
            InitialStateProductListTitleViewHolder.LAYOUT -> InitialStateProductListTitleViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}