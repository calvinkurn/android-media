package com.tokopedia.autocomplete.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel

interface InitialStateTypeFactory {

    fun type(viewModel: PopularSearchViewModel): Int

    fun type(viewModel: RecentSearchViewModel): Int

    fun type(viewModel: RecentViewViewModel): Int

    fun type(viewModel: PopularSearchTitleViewModel): Int

    fun type(viewModel: RecentSearchTitleViewModel): Int

    fun type(viewModel: RecentViewTitleViewModel): Int

    fun type(viewModel: DynamicInitialStateSearchViewModel): Int

    fun type(viewModelInitialState: DynamicInitialStateTitleViewModel): Int

    fun type(viewModelInitialState: RecentSearchSeeMoreViewModel): Int

    fun type(curatedCampaignViewModel: CuratedCampaignViewModel): Int

    fun type(initialStateProductListDataView: InitialStateProductListDataView): Int

    fun type(initialStateProductLineTitleDataView: InitialStateProductLineTitleDataView): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}