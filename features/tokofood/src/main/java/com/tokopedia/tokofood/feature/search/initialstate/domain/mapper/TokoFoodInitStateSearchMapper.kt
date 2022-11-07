package com.tokopedia.tokofood.feature.search.initialstate.domain.mapper

import com.tokopedia.tokofood.feature.search.initialstate.domain.model.RemoveSearchHistoryResponse
import com.tokopedia.tokofood.feature.search.initialstate.domain.model.TokoFoodInitSearchStateResponse
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.BaseInitialStateVisitable
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsListUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsPopularSearch
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderItemInitialStateUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.InitialStateWrapperUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RemoveSearchHistoryUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreCuisineUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel
import javax.inject.Inject

class TokoFoodInitStateSearchMapper @Inject constructor() {

    fun mapToInitialStateWrapperUiModel(tokoFoodInitSearchState: TokoFoodInitSearchStateResponse.TokofoodInitSearchState): InitialStateWrapperUiModel {
        val baseInitialStateList = mutableListOf<BaseInitialStateVisitable>().apply {
            tokoFoodInitSearchState.sections.forEach {
                when (it.id) {
                    RECENT_SEARCH -> {
                        addRecentSearchSection(it)
                    }
                    POPULAR_SEARCH -> {
                        addPopularSearchSection(it)
                    }
                    CUISINE_LIST -> {
                        addCuisineListSection(it)
                    }
                    else -> {
                        return@forEach
                    }
                }
            }
        }
        return InitialStateWrapperUiModel(baseInitialStateList)
    }

    fun mapToRemoveSearchUiModel(removeSearchHistoryResponse: RemoveSearchHistoryResponse.TokofoodRemoveSearchHistory):
            RemoveSearchHistoryUiModel {
        return RemoveSearchHistoryUiModel(
            removeSearchHistoryResponse.message,
            removeSearchHistoryResponse.success
        )
    }

    private fun MutableList<BaseInitialStateVisitable>.addRecentSearchSection(section: TokoFoodInitSearchStateResponse.TokofoodInitSearchState.Section) {
        val recentSearchSize = section.items.size

        val recentSearchLimit = if (recentSearchSize > INIT_LIMIT_ITEM) {
            section.items.take(INIT_LIMIT_ITEM)
        } else {
            section.items
        }

        add(HeaderRecentSearchUiModel(section.header, section.labelText, section.labelAction))
        addAll(
            recentSearchLimit.map { item ->
                RecentSearchItemUiModel(
                    sectionId = section.id,
                    itemId = item.id,
                    imageUrl = item.imageUrl,
                    title = item.title,
                    template = item.template,
                    imageActionUrl = item.shortcutImage
                )
            }
        )
        if (recentSearchSize > INIT_LIMIT_ITEM) {
            val limitItem = if (recentSearchSize >= ALL_LIMIT_ITEM) ALL_LIMIT_ITEM else recentSearchSize

            val recentSearchSeeMore = section.items.subList(THREE_POSITION, limitItem)
            val recentSearchSeeMoreList = recentSearchSeeMore.map {
                RecentSearchItemUiModel(
                    sectionId = section.id,
                    itemId = it.id,
                    imageUrl = it.imageUrl,
                    title = it.title,
                    template = it.template,
                    imageActionUrl = it.shortcutImage
                )
            }
            add(SeeMoreRecentSearchUiModel(section.id, recentSearchSeeMoreList))
        }
    }

    private fun MutableList<BaseInitialStateVisitable>.addPopularSearchSection(section: TokoFoodInitSearchStateResponse.TokofoodInitSearchState.Section) {
        add(HeaderItemInitialStateUiModel(section.header, section.labelText, section.labelAction))
        add(ChipsListUiModel(
            section.items.map { item ->
                ChipsPopularSearch(
                    id = section.id,
                    title = item.title,
                    appLink = item.applink
                )
            }
        ))
    }

    private fun MutableList<BaseInitialStateVisitable>.addCuisineListSection(section: TokoFoodInitSearchStateResponse.TokofoodInitSearchState.Section) {
        val cuisineListSize = section.items.size
        val cuisineListLimit = if (section.items.size > INIT_LIMIT_ITEM) section.items.take(INIT_LIMIT_ITEM) else section.items

        add(HeaderItemInitialStateUiModel(section.header, section.labelText, section.labelAction))
        addAll(
            cuisineListLimit.map { item ->
                CuisineItemUiModel(
                    sectionId = section.id,
                    itemId = item.id,
                    imageUrl = item.imageUrl,
                    title = item.title,
                    template = item.template,
                    appLink = item.applink
                )
            }
        )
        if (section.items.size > INIT_LIMIT_ITEM) {
            val cuisineListSeeMore = section.items.subList(THREE_POSITION, cuisineListSize)
            val cuisineListSeeMoreList = cuisineListSeeMore.map {
                CuisineItemUiModel(
                    sectionId = section.id,
                    itemId = it.id,
                    imageUrl = it.imageUrl,
                    title = it.title,
                    template = it.template,
                    appLink = it.applink
                )
            }
            add(SeeMoreCuisineUiModel(section.id, cuisineListSeeMoreList))
        }
    }

    companion object {
        const val POPULAR_SEARCH = "popular_search"
        const val RECENT_SEARCH = "recent_search"
        const val CUISINE_LIST = "cuisine_list"
        const val INIT_LIMIT_ITEM = 3
        const val ALL_LIMIT_ITEM = 5
        const val THREE_POSITION = 3
    }
}
