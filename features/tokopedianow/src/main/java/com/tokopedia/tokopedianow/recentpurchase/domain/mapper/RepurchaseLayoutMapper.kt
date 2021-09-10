package com.tokopedia.tokopedianow.recentpurchase.domain.mapper

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.SORT_FILTER
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseProductMapper.mapToProductListUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.factory.RepurchaseSortFilterFactory
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.RepurchaseSortFilterType.*

object RepurchaseLayoutMapper {

    fun MutableList<Visitable<*>>.addLayoutList() {
        val sortFilter = RepurchaseSortFilterUiModel(SORT_FILTER, emptyList())
        val productGrid = RepurchaseProductGridUiModel(emptyList())

        add(sortFilter)
        addChooseAddress()
        add(productGrid)
    }

    fun MutableList<Visitable<*>>.addSortFilter() {
        val sortFilter = RepurchaseSortFilterFactory.createSortFilter()
        firstOrNull { it is RepurchaseSortFilterUiModel }?.let {
            val index = indexOf(it)
            set(index, sortFilter)
        }
    }

    fun MutableList<Visitable<*>>.addProductGrid(response: List<RepurchaseProduct>) {
        val productList = response.mapToProductListUiModel()
        add(RepurchaseProductGridUiModel(productList))
    }

    fun MutableList<Visitable<*>>.addLoading() {
        add(RepurchaseLoadingUiModel)
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoHistory(@StringRes description: Int) {
        removeFirstLayout(RepurchaseProductGridUiModel::class.java)
        add(RepurchaseEmptyStateNoHistoryUiModel(description))
    }

    fun MutableList<Visitable<*>>.addCategoryGrid(response: List<CategoryResponse>?) {
        val categoryList = RepurchaseCategoryMapper.mapToCategoryList(response)
        add(TokoNowCategoryGridUiModel(
                id = "",
                title = "",
                titleRes = R.string.tokopedianow_repurchase_category_grid_title,
                categoryList = categoryList,
                state = TokoNowLayoutState.SHOW
            )
        )
    }

    fun MutableList<Visitable<*>>.addProductRecom(pageName: String, recommendationWidget: RecommendationWidget) {
        add(TokoNowRecommendationCarouselUiModel(
                pageName = pageName,
                carouselData = RecommendationCarouselData(
                    recommendationData = recommendationWidget,
                    state = STATE_READY
                )
            )
        )
    }

    fun MutableList<Visitable<*>>.addChooseAddress() {
        add(TokoNowChooseAddressWidgetUiModel())
    }

    fun MutableList<Visitable<*>>.addEmptyStateOoc() {
        add(TokoNowEmptyStateOocUiModel())
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoResult() {
        add(TokoNowEmptyStateNoResultUiModel())
    }

    fun MutableList<Visitable<*>>.removeLoading() {
        removeFirstLayout(RepurchaseLoadingUiModel::class.java)
    }

    fun MutableList<Visitable<*>>.removeChooseAddress() {
        removeFirstLayout(TokoNowChooseAddressWidgetUiModel::class.java)
    }

    fun MutableList<Visitable<*>>.setSelectedCategoryFilter(selectedFilter: SelectedSortFilter?) {
        firstOrNull { it is RepurchaseSortFilterUiModel }?.let { item ->
            val sortFilterIndex = indexOf(item)
            val sortFilter = (item as RepurchaseSortFilterUiModel)
            val sortFilterList = sortFilter.sortFilterList.toMutableList()

            val categoryFilter = sortFilterList.firstOrNull { it.type == CATEGORY_FILTER }
            val categoryFilterIndex = sortFilterList.indexOf(categoryFilter)
            val updatedCategoryFilter = categoryFilter?.copy(selectedItem = selectedFilter)

            updatedCategoryFilter?.let {
                sortFilterList[categoryFilterIndex] = it
                set(sortFilterIndex, sortFilter.copy(
                    sortFilterList = sortFilterList
                ))
            }
        }
    }

    private fun <T> MutableList<Visitable<*>>.removeFirstLayout(model: Class<T>) {
        firstOrNull { it.javaClass == model }?.let {
            val index = indexOf(it)
            removeAt(index)
        }
    }
}
