package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.diffutil.InitialSearchStateDiffUtil
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.BaseInitialStateVisitable
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel

class TokoFoodInitStateAdapter(private val initialSearchAdapterFactoryImpl: InitialStateTypeFactoryImpl) :
    BaseAdapter<InitialStateTypeFactoryImpl>(initialSearchAdapterFactoryImpl) {

    fun setInitialStateList(newList: List<BaseInitialStateVisitable>) {
        val oldList = visitables.filterIsInstance<BaseInitialStateVisitable>()
        val callBack = InitialSearchStateDiffUtil(oldList, newList, initialSearchAdapterFactoryImpl)
        val diffResult = DiffUtil.calculateDiff(callBack)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun showErrorState(errorType: Int) {
        if (visitables.getOrNull(Int.ZERO) !is TokofoodSearchErrorStateUiModel) {
            visitables.add(TokofoodSearchErrorStateUiModel(errorType))
            notifyItemInserted(Int.ZERO)
        }
    }

    fun removeErrorState() {
        if (visitables.getOrNull(lastIndex) is TokofoodSearchErrorStateUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    inline fun <reified T : Visitable<*>> expandInitialStateItem(newInitialStateList: List<T>) {
        val initialStateFirstIndex = list.indexOfLast { it is T } + Int.ONE
        list.addAll(initialStateFirstIndex, newInitialStateList)
        notifyItemRangeInserted(initialStateFirstIndex, newInitialStateList.size)
    }

    inline fun <reified T : Visitable<*>> removeSeeMoreButtonSection(seeMoreSection: T) {
        val seeMoreSectionIndex = list.indexOf(seeMoreSection)
        if (seeMoreSectionIndex > RecyclerView.NO_POSITION) {
            list.removeAt(seeMoreSectionIndex)
            notifyItemRemoved(seeMoreSectionIndex)
        }
    }

    fun removeRecentSearchItem(position: Int) {
        if (position > RecyclerView.NO_POSITION) {
            visitables.removeAt(position)
            notifyItemRemoved(position)
        }

        val recentSearchIndex = visitables.indexOfFirst { it is RecentSearchItemUiModel }
        if (recentSearchIndex == RecyclerView.NO_POSITION) {
            val headerRecentSearchIndex = visitables.indexOfFirst { it is HeaderRecentSearchUiModel }
            if (headerRecentSearchIndex > RecyclerView.NO_POSITION) {
                visitables.removeAt(headerRecentSearchIndex)
                notifyItemRemoved(headerRecentSearchIndex)
            }
        }
    }

    fun removeAllInitialState() {
        val initialStateList = visitables.filterIsInstance<BaseInitialStateVisitable>()
        if (initialStateList.size.isMoreThanZero()) {
            visitables.removeAll { it is BaseInitialStateVisitable }
            notifyItemRangeRemoved(visitables.size, initialStateList.size)
        }
    }

    fun removeAllRecentSearchSection() {
        removeRecentSearchHeaderSection()
        removeAllRecentSearch()
        removeSeeMoreRecentSearch()
    }

    private fun removeRecentSearchHeaderSection() {
        val recentSearchHeaderIndex = visitables.indexOfFirst { it is HeaderRecentSearchUiModel }
        if (recentSearchHeaderIndex > RecyclerView.NO_POSITION) {
            visitables.removeAt(recentSearchHeaderIndex)
            notifyItemRemoved(recentSearchHeaderIndex)
        }
    }

    private fun removeAllRecentSearch() {
        val recentSearchList = visitables.filterIsInstance<RecentSearchItemUiModel>()
        if (recentSearchList.size.isMoreThanZero()) {
            visitables.removeAll { it is RecentSearchItemUiModel }
            notifyItemRangeRemoved(visitables.size, recentSearchList.size)
        }
    }

    private fun removeSeeMoreRecentSearch() {
        val seeMoreRecentSearchHeaderIndex = visitables.indexOfFirst { it is SeeMoreRecentSearchUiModel }
        if (seeMoreRecentSearchHeaderIndex > RecyclerView.NO_POSITION) {
            visitables.removeAt(seeMoreRecentSearchHeaderIndex)
            notifyItemRemoved(seeMoreRecentSearchHeaderIndex)
        }
    }
}