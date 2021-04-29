package com.tokopedia.autocomplete.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewDataView

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
}