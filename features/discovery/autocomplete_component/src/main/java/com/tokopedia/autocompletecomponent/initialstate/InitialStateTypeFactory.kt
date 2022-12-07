package com.tokopedia.autocompletecomponent.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetDataView
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationDataView

interface InitialStateTypeFactory {

    fun type(popularSearchDataView: PopularSearchDataView): Int

    fun type(recentSearchDataView: RecentSearchDataView): Int

    fun type(recentViewDataView: RecentViewDataView): Int

    fun type(popularSearchTitleDataView: PopularSearchTitleDataView): Int

    fun type(recentSearchTitleDataView: RecentSearchTitleDataView): Int

    fun type(recentViewTitleDataView: RecentViewTitleDataView): Int

    fun type(dynamicInitialStateSearchDataView: DynamicInitialStateSearchDataView): Int

    fun type(dynamicInitialStateTitleDataView: DynamicInitialStateTitleDataView): Int

    fun type(recentSearchSeeMoreDataView: RecentSearchSeeMoreDataView): Int

    fun type(curatedCampaignDataView: CuratedCampaignDataView): Int

    fun type(initialStateProductListDataView: InitialStateProductListDataView): Int

    fun type(initialStateProductLineTitleDataView: InitialStateProductLineTitleDataView): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(initialStateChipWidgetDataView: InitialStateChipWidgetDataView): Int

    fun type(initialStateChipWidgetTitleDataView: InitialStateChipWidgetTitleDataView): Int

    fun type(searchBarEducationDataView: SearchBarEducationDataView): Int
}