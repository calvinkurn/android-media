package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.diffutil.InitialSearchStateDiffUtil
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.BaseInitialStateTypeFactory
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel

class TokoFoodInitStateAdapter(private val initialSearchAdapterFactoryImpl: InitialStateTypeFactoryImpl) :
    BaseAdapter<InitialStateTypeFactoryImpl>(initialSearchAdapterFactoryImpl) {

    fun setInitialStateList(newList: List<BaseInitialStateTypeFactory>) {
        val oldList = visitables.filterIsInstance<BaseInitialStateTypeFactory>()
        val callBack = InitialSearchStateDiffUtil(oldList, newList, initialSearchAdapterFactoryImpl)
        val diffResult = DiffUtil.calculateDiff(callBack)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
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
    }

    fun removeAllInitialState() {
        val initialStateList = visitables.filterIsInstance<BaseInitialStateTypeFactory>()
        if (initialStateList.size.isMoreThanZero()) {
            visitables.removeAll { it is BaseInitialStateTypeFactory }
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