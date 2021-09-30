package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addCategoryGrid
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addChooseAddress
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoHistory
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoResult
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateOoc
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addLoading
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addServerErrorState
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addSortFilter
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.removeAllProduct
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.setDateFilter
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.setSortFilter
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel

fun createRepurchaseLoadingLayout(): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.addLoading()
    return layoutList
}

fun createChooseAddressLayout(): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.addChooseAddress()
    return layoutList
}

fun createCategoryGridLayout(): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.addCategoryGrid(listOf(
            CategoryResponse(
                id = "3",
                name = "Category 3",
                url = "tokopedia://",
                appLinks = "tokoepdia://",
                imageUrl = "tokopedia://",
                parentId = "5",
                childList = listOf()
            )
        )
    )
    return layoutList
}

fun createSortFilterLayout(): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.add(RepurchaseSortFilterUiModel(RepurchaseStaticLayoutId.SORT_FILTER, emptyList()))
    layoutList.addSortFilter()
    return layoutList
}

fun createSortFilterLayout(sort: Int): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.add(RepurchaseSortFilterUiModel(RepurchaseStaticLayoutId.SORT_FILTER, emptyList()))
    layoutList.addSortFilter()
    layoutList.setSortFilter(sort)
    return layoutList
}

fun createDateFilterLayout(selectedFilter: RepurchaseSortFilterUiModel.SelectedDateFilter): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.add(RepurchaseSortFilterUiModel(RepurchaseStaticLayoutId.SORT_FILTER, emptyList()))
    layoutList.addSortFilter()
    layoutList.setDateFilter(selectedFilter)
    return layoutList
}

fun createProductRecomLayout(pageName: String, carouselData: RecommendationCarouselData): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.add(
        TokoNowRecommendationCarouselUiModel(
            pageName = pageName,
            carouselData = carouselData
        )
    )
    return layoutList
}

fun createEmptyStateLayout(id: String): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    when(id) {
        RepurchaseStaticLayoutId.EMPTY_STATE_NO_HISTORY_SEARCH -> {
            val description = R.string.tokopedianow_repurchase_empty_state_no_history_desc_search
            layoutList.addEmptyStateNoHistory(description)
        }
        RepurchaseStaticLayoutId.EMPTY_STATE_NO_HISTORY_FILTER -> {
            val description = R.string.tokopedianow_repurchase_empty_state_no_history_desc_filter
            layoutList.addEmptyStateNoHistory(description)
        }
        RepurchaseStaticLayoutId.EMPTY_STATE_OOC -> {
            layoutList.addEmptyStateOoc()
        }
        RepurchaseStaticLayoutId.ERROR_STATE_FAILED_TO_FETCH_DATA -> {
            layoutList.addServerErrorState()
        }
        else -> {
            layoutList.addEmptyStateNoResult()
        }
    }
    return layoutList
}