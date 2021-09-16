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
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.RepurchaseSortFilterType.*
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT

object RepurchaseLayoutMapper {

    fun MutableList<Visitable<*>>.addLayoutList() {
        val sortFilter = RepurchaseSortFilterUiModel(SORT_FILTER, emptyList())

        add(sortFilter)
        addChooseAddress()
    }

    fun MutableList<Visitable<*>>.addSortFilter() {
        firstOrNull { it is RepurchaseSortFilterUiModel }?.let {
            val uiModel = RepurchaseSortFilterFactory.createSortFilter()
            val index = indexOf(it)
            set(index, uiModel)
        }
    }

    fun MutableList<Visitable<*>>.addProduct(response: List<RepurchaseProduct>) {
        addAll(response.mapToProductListUiModel())
    }

    fun MutableList<Visitable<*>>.addLoading() {
        add(RepurchaseLoadingUiModel)
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoHistory(@StringRes description: Int) {
        add(RepurchaseEmptyStateNoHistoryUiModel(description))
    }

    fun MutableList<Visitable<*>>.removeEmptyStateNoHistory() {
        removeFirstLayout(RepurchaseEmptyStateNoHistoryUiModel::class.java)
    }

    fun MutableList<Visitable<*>>.removeAllProduct()  {
        removeAll { it is RepurchaseProductUiModel }
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

    fun MutableList<Visitable<*>>.setCategoryFilter(selectedFilter: SelectedSortFilter?) {
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

    fun MutableList<Visitable<*>>.setSortFilter(sort: Int) {
        firstOrNull { it is RepurchaseSortFilterUiModel }?.let { item ->
            val sortFilterIndex = indexOf(item)
            val sortFilter = (item as RepurchaseSortFilterUiModel)
            val sortFilterList = sortFilter.sortFilterList.toMutableList()

            val filter = sortFilterList.firstOrNull { it.type == SORT }
            val filterIndex = sortFilterList.indexOf(filter)
            val title = if (sort == FREQUENTLY_BOUGHT) {
                R.string.tokopedianow_sort_filter_item_most_frequently_bought_bottomsheet
            } else {
                R.string.tokopedianow_sort_filter_item_last_bought_bottomsheet
            }
            val updatedFilter = filter?.copy(
                sort = sort,
                title = title
            )
            updatedFilter?.let {
                sortFilterList[filterIndex] = it
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
