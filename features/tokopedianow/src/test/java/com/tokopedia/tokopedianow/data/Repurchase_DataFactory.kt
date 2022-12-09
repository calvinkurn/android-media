package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addCategoryGrid
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addChooseAddress
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoHistory
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoResult
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateOoc
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addLoading
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addProductRecommendation
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addServerErrorState
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addSortFilter
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.setDateFilter
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.setSortFilter
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel

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

fun createCategoryGridLayout(warehouseId: String): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    layoutList.addCategoryGrid(listOf(
            CategoryResponse(
                id = "3",
                name = "Category 3",
                url = "tokopedia://",
                appLinks = "tokoepdia://",
                imageUrl = "tokopedia://",
                parentId = "5",
                childList = listOf(),
                isAdult = 0
            )
        ),
        warehouseId = warehouseId
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

fun createEmptyStateLayout(id: String, serviceType: String = "", pageName: String = ""): List<Visitable<*>> {
    val layoutList: MutableList<Visitable<*>> = mutableListOf()
    when(id) {
        RepurchaseStaticLayoutId.EMPTY_STATE_NO_HISTORY_SEARCH -> {
            val title = R.string.tokopedianow_repurchase_empty_state_no_history_title_search
            val description = R.string.tokopedianow_repurchase_empty_state_no_history_desc_search
            layoutList.addEmptyStateNoHistory(title, description)
        }
        RepurchaseStaticLayoutId.EMPTY_STATE_NO_HISTORY_FILTER -> {
            val title = R.string.tokopedianow_repurchase_empty_state_no_history_title_filter
            val description = R.string.tokopedianow_repurchase_empty_state_no_history_desc_filter
            layoutList.addEmptyStateNoHistory(title, description)
        }
        RepurchaseStaticLayoutId.EMPTY_STATE_OOC -> {
            layoutList.addEmptyStateOoc(serviceType)
        }
        RepurchaseStaticLayoutId.ERROR_STATE_FAILED_TO_FETCH_DATA -> {
            layoutList.addServerErrorState()
        }
        else -> {
            layoutList.addEmptyStateNoResult(serviceType)
            layoutList.addProductRecommendation(PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM)
        }
    }
    return layoutList
}
