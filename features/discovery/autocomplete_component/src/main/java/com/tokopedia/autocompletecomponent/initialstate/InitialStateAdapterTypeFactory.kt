package com.tokopedia.autocompletecomponent.initialstate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipListener
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetDataView
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetViewHolder
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignViewHolder
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignListener
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateListener
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateViewHolder
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsDataView
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsInitialStateListener
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsViewHolder
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchViewHolder
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchListener
import com.tokopedia.autocompletecomponent.initialstate.productline.ProductLineListener
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.*
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewListener
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationListener
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationViewHolder

class InitialStateAdapterTypeFactory(
    private val recentViewListener: RecentViewListener,
    private val recentSearchListener: RecentSearchListener,
    private val productLineListener: ProductLineListener,
    private val popularSearchListener: PopularSearchListener,
    private val dynamicInitialStateListener: DynamicInitialStateListener,
    private val curatedCampaignListener: CuratedCampaignListener,
    private val chipListener: InitialStateChipListener,
    private val searchBarEducationListener: SearchBarEducationListener,
    private val mpsChipListener: MpsInitialStateListener,
    private var isReimagine: Boolean
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

    override fun type(initialStateChipWidgetDataView: InitialStateChipWidgetDataView): Int {
        return InitialStateChipWidgetViewHolder.LAYOUT
    }

    override fun type(initialStateChipWidgetTitleDataView: InitialStateChipWidgetTitleDataView): Int {
        return InitialStateChipWidgetTitleViewHolder.LAYOUT
    }

    override fun type(searchBarEducationDataView: SearchBarEducationDataView): Int {
        return SearchBarEducationViewHolder.LAYOUT
    }

    override fun type(mpsDataView: MpsDataView): Int {
        return MpsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            PopularSearchViewHolder.LAYOUT ->
                PopularSearchViewHolder(parent, popularSearchListener, isReimagine)
            RecentSearchViewHolder.LAYOUT ->
                RecentSearchViewHolder(parent, recentSearchListener, isReimagine)
            RecentViewViewHolder.LAYOUT ->
                RecentViewViewHolder(parent, recentViewListener)
            PopularSearchTitleViewHolder.LAYOUT ->
                PopularSearchTitleViewHolder(parent, popularSearchListener)
            RecentSearchTitleViewHolder.LAYOUT ->
                RecentSearchTitleViewHolder(parent, recentSearchListener)
            RecentViewTitleViewHolder.LAYOUT ->
                RecentViewTitleViewHolder(parent)
            RecentSearchSeeMoreViewHolder.LAYOUT ->
                RecentSearchSeeMoreViewHolder(parent, recentSearchListener)
            DynamicInitialStateTitleViewHolder.LAYOUT ->
                DynamicInitialStateTitleViewHolder(parent, dynamicInitialStateListener)
            DynamicInitialStateViewHolder.LAYOUT ->
                DynamicInitialStateViewHolder(parent, dynamicInitialStateListener, isReimagine)
            CuratedCampaignViewHolder.LAYOUT ->
                CuratedCampaignViewHolder(parent, curatedCampaignListener)
            InitialStateProductListViewHolder.LAYOUT ->
                InitialStateProductListViewHolder(parent, productLineListener)
            InitialStateProductListTitleViewHolder.LAYOUT ->
                InitialStateProductListTitleViewHolder(parent)
            InitialStateChipWidgetViewHolder.LAYOUT ->
                InitialStateChipWidgetViewHolder(parent, chipListener)
            InitialStateChipWidgetTitleViewHolder.LAYOUT ->
                InitialStateChipWidgetTitleViewHolder(parent)
            SearchBarEducationViewHolder.LAYOUT ->
                SearchBarEducationViewHolder(parent, searchBarEducationListener, isReimagine)
            MpsViewHolder.LAYOUT -> MpsViewHolder(parent, mpsChipListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
