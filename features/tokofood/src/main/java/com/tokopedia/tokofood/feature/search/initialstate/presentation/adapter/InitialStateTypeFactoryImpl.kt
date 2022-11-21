package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.feature.search.common.presentation.adapter.TokofoodSearchTypeFactory
import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel
import com.tokopedia.tokofood.feature.search.common.presentation.viewholder.TokofoodSearchErrorStateViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.listener.InitialStateListener
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsListUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderItemInitialStateUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreCuisineUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.ChipsListViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.CuisineItemViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.HeaderItemInitStateViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.HeaderRecentSearchViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.RecentSearchItemViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.SeeMoreCuisineViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.SeeMoreRecentSearchViewHolder

class InitialStateTypeFactoryImpl(
    private val initialStateListener: InitialStateListener,
    private val tokoFoodSearchStateListener: TokofoodSearchErrorStateViewHolder.Listener,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
): BaseAdapterTypeFactory(), InitialStateTypeFactory, TokofoodSearchTypeFactory {

    override fun type(type: HeaderItemInitialStateUiModel): Int {
        return HeaderItemInitStateViewHolder.LAYOUT
    }

    override fun type(type: HeaderRecentSearchUiModel): Int {
        return HeaderRecentSearchViewHolder.LAYOUT
    }

    override fun type(type: RecentSearchItemUiModel): Int {
        return RecentSearchItemViewHolder.LAYOUT
    }

    override fun type(type: SeeMoreCuisineUiModel): Int {
        return SeeMoreCuisineViewHolder.LAYOUT
    }

    override fun type(type: SeeMoreRecentSearchUiModel): Int {
        return SeeMoreRecentSearchViewHolder.LAYOUT
    }

    override fun type(type: ChipsListUiModel): Int {
        return ChipsListViewHolder.LAYOUT
    }

    override fun type(type: CuisineItemUiModel): Int {
        return CuisineItemViewHolder.LAYOUT
    }

    override fun type(uiModel: TokofoodSearchErrorStateUiModel): Int {
        return TokofoodSearchErrorStateViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HeaderItemInitStateViewHolder.LAYOUT -> HeaderItemInitStateViewHolder(parent)
            HeaderRecentSearchViewHolder.LAYOUT -> HeaderRecentSearchViewHolder(parent, initialStateListener)
            RecentSearchItemViewHolder.LAYOUT -> RecentSearchItemViewHolder(parent, initialStateListener, tokofoodScrollChangedListener)
            SeeMoreCuisineViewHolder.LAYOUT -> SeeMoreCuisineViewHolder(parent, initialStateListener)
            SeeMoreRecentSearchViewHolder.LAYOUT -> SeeMoreRecentSearchViewHolder(parent, initialStateListener)
            ChipsListViewHolder.LAYOUT -> ChipsListViewHolder(parent, initialStateListener, tokofoodScrollChangedListener)
            CuisineItemViewHolder.LAYOUT -> CuisineItemViewHolder(parent, initialStateListener, tokofoodScrollChangedListener)
            TokofoodSearchErrorStateViewHolder.LAYOUT -> TokofoodSearchErrorStateViewHolder(parent, tokoFoodSearchStateListener)
            else ->  super.createViewHolder(parent, type)
        }
    }
}
