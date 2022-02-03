package com.tokopedia.tokopedianow.repurchase.domain.mapper

import TokoNowDateFilterBottomSheet.Companion.ALL_DATE_TRANSACTION_POSITION
import TokoNowDateFilterBottomSheet.Companion.LAST_ONE_MONTH_POSITION
import TokoNowDateFilterBottomSheet.Companion.LAST_THREE_MONTHS_POSITION
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.SORT_FILTER
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseProductMapper.mapToProductListUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.factory.RepurchaseSortFilterFactory
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.RepurchaseSortFilterType.*
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT
import com.tokopedia.unifycomponents.ChipsUnify

object RepurchaseLayoutMapper {

    const val PRODUCT_REPURCHASE = "product_repurchase"
    const val PRODUCT_RECOMMENDATION = "product_recom"

    private const val DEFAULT_QUANTITY = 0
    private const val DEFAULT_PARENT_ID = "0"

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

    fun MutableList<Visitable<*>>.addEmptyStateNoHistory(@StringRes title: Int, @StringRes description: Int) {
        add(RepurchaseEmptyStateNoHistoryUiModel(title, description))
    }

    fun MutableList<Visitable<*>>.removeEmptyStateNoHistory() {
        removeFirstLayout(RepurchaseEmptyStateNoHistoryUiModel::class.java)
    }

    fun MutableList<Visitable<*>>.removeAllProduct()  {
        removeAll { it is RepurchaseProductUiModel }
    }

    fun MutableList<Visitable<*>>.addCategoryGrid(response: List<CategoryResponse>?, warehouseId: String) {
        val categoryList = RepurchaseCategoryMapper.mapToCategoryList(response, warehouseId)
        add(TokoNowCategoryGridUiModel(
                id = "",
                title = "",
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
        add(TokoNowEmptyStateOocUiModel(hostSource = SOURCE))
    }

    fun MutableList<Visitable<*>>.addRecomWidget(pageName: String) {
        add(
            TokoNowRecommendationCarouselUiModel(
                pageName = pageName,
                isFirstLoad = true,
                isBindWithPageName = true
            )
        )
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoResult() {
        add(
            TokoNowEmptyStateNoResultUiModel(
                defaultTitleResId = R.string.tokopedianow_repurchase_no_result_title,
                defaultDescriptionResId = R.string.tokopedianow_repurchase_no_result_description,
                globalSearchBtnTextResId = R.string.tokopedianow_back_to_tokopedia
            )
        )
    }

    fun MutableList<Visitable<*>>.addServerErrorState() {
        add(TokoNowServerErrorUiModel)
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

            val title : Int
            val chipType: String

            if (sort == FREQUENTLY_BOUGHT) {
                title = R.string.tokopedianow_sort_filter_item_most_frequently_bought_bottomsheet
                chipType = ChipsUnify.TYPE_NORMAL
            } else {
                title =R.string.tokopedianow_sort_filter_item_last_bought_bottomsheet
                chipType = ChipsUnify.TYPE_SELECTED
            }

            val updatedFilter = filter?.copy(
                sort = sort,
                title = title,
                chipType = chipType
            )

            updatedFilter?.let {
                sortFilterList[filterIndex] = it
                set(sortFilterIndex, sortFilter.copy(
                    sortFilterList = sortFilterList
                ))
            }
        }
    }

    fun MutableList<Visitable<*>>.setDateFilter(selectedFilter: SelectedDateFilter?) {
        firstOrNull { it is RepurchaseSortFilterUiModel }?.let { item ->
            val sortFilterIndex = indexOf(item)
            val sortFilter = (item as RepurchaseSortFilterUiModel)
            val sortFilterList = sortFilter.sortFilterList.toMutableList()

            val filter = sortFilterList.firstOrNull { it.type == DATE_FILTER }
            val filterIndex = sortFilterList.indexOf(filter)

            var titleFormat: Int? = null
            val chipType: String
            val title: Int = when(selectedFilter?.position) {
                ALL_DATE_TRANSACTION_POSITION -> {
                    chipType = ChipsUnify.TYPE_NORMAL
                    R.string.tokopedianow_date_filter_all_date_transactions_chip_and_item_bottomsheet_title
                }
                LAST_ONE_MONTH_POSITION -> {
                    chipType = ChipsUnify.TYPE_SELECTED
                    R.string.tokopedianow_date_filter_last_one_month_chip_and_item_bottomsheet_title
                }
                LAST_THREE_MONTHS_POSITION -> {
                    chipType = ChipsUnify.TYPE_SELECTED
                    R.string.tokopedianow_date_filter_last_three_months_chip_and_item_bottomsheet_title
                }
                else -> {
                    titleFormat = R.string.tokopedianow_date_filter_custom_date_chip_title
                    chipType = ChipsUnify.TYPE_SELECTED
                    R.string.tokopedianow_date_filter_all_date_transactions_chip_and_item_bottomsheet_title
                }
            }

            val updatedFilter = filter?.copy(
                title = title,
                selectedDateFilter = selectedFilter,
                chipType = chipType,
                titleFormat = titleFormat
            )

            updatedFilter?.let {
                sortFilterList[filterIndex] = it
                set(sortFilterIndex, sortFilter.copy(
                    sortFilterList = sortFilterList
                ))
            }
        }
    }

    fun MutableList<Visitable<*>>.updateProductATCQuantity(miniCart: MiniCartSimplifiedData) {
        val variantGroup = miniCart.miniCartItems.groupBy { it.productParentId }

        miniCart.miniCartItems.map { miniCartItem ->
            val productId = miniCartItem.productId
            val parentId = miniCartItem.productParentId
            val quantity = if (parentId != DEFAULT_PARENT_ID) {
                val miniCartItemsWithSameParentId = variantGroup[miniCartItem.productParentId]
                miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
            } else {
                miniCartItem.quantity
            }
            updateProductQuantity(productId, quantity)
        }
    }

    fun MutableList<Visitable<*>>.updateDeletedATCQuantity(miniCart: MiniCartSimplifiedData, type: String) {
        when (type) {
            PRODUCT_REPURCHASE -> {
                val productList = filterIsInstance<RepurchaseProductUiModel>()
                val cartProductIds = miniCart.miniCartItems.map { it.productId }
                val deletedProducts = productList.filter { it.id !in cartProductIds }
                val variantGroup = miniCart.miniCartItems.groupBy { it.productParentId }

                deletedProducts.forEach { model ->
                    val productId = model.id
                    val parentId = model.parentId

                    if (parentId != DEFAULT_PARENT_ID) {
                        val miniCartItemsWithSameParentId = variantGroup[parentId]
                        val totalQuantity = miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
                        if (totalQuantity == DEFAULT_QUANTITY) {
                            updateProductQuantity(productId, DEFAULT_QUANTITY)
                        } else {
                            updateProductQuantity(productId, totalQuantity)
                        }
                    } else {
                        updateProductQuantity(productId, DEFAULT_QUANTITY)
                    }
                }
            }
            PRODUCT_RECOMMENDATION -> {
                firstOrNull { it is TokoNowRecommendationCarouselUiModel }?.let { uiModel ->
                    val layoutUiModel = uiModel as TokoNowRecommendationCarouselUiModel
                    val cartProductIds = miniCart.miniCartItems.map { it.productId }
                    val deletedProducts = layoutUiModel.carouselData.recommendationData.recommendationItemList.filter { it.productId.toString() !in cartProductIds }
                    val variantGroup = miniCart.miniCartItems.groupBy { it.productParentId }

                    deletedProducts.forEach { model ->
                        val productId = model.productId.toString()
                        val parentId = model.parentID.toString()

                        if (parentId != DEFAULT_PARENT_ID) {
                            val miniCartItemsWithSameParentId = variantGroup[parentId]
                            val totalQuantity = miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
                            if (totalQuantity == DEFAULT_QUANTITY) {
                                updateProductRecomQuantity(productId, DEFAULT_QUANTITY)
                            } else {
                                updateProductRecomQuantity(productId, totalQuantity)
                            }
                        } else {
                            updateProductRecomQuantity(productId, DEFAULT_QUANTITY)
                        }
                    }
                }
            }
        }
    }

    private fun MutableList<Visitable<*>>.updateProductQuantity(productId: String, quantity: Int) {
        val productList = filterIsInstance<RepurchaseProductUiModel>()

        productList.firstOrNull { it.id == productId }?.let {
            if(!it.isStockEmpty) {
                val index = indexOf(it)
                val productCard = it.productCard.run {
                    if (hasVariant()) {
                        copy(variant = variant?.copy(quantity = quantity))
                    } else {
                        copy(
                            hasAddToCartButton = quantity == DEFAULT_QUANTITY,
                            nonVariant = nonVariant?.copy(quantity = quantity)
                        )
                    }
                }
                set(index, it.copy(productCard = productCard))
            }
        }
    }

    private fun MutableList<Visitable<*>>.updateProductRecomQuantity(productId: String, quantity: Int) {
        firstOrNull { it is TokoNowRecommendationCarouselUiModel }?.let { uiModel ->
            val index = indexOf(uiModel)
            val layoutUiModel = uiModel as TokoNowRecommendationCarouselUiModel
            val recommendationData = layoutUiModel.carouselData.recommendationData.copy()
            recommendationData.recommendationItemList.firstOrNull { it.productId.toString() == productId }?.let {
                it.quantity = quantity
            }
            set(
                index = index,
                element = TokoNowRecommendationCarouselUiModel(
                    carouselData = layoutUiModel.carouselData.copy(
                        recommendationData = recommendationData
                    ),
                    id = layoutUiModel.id,
                    pageName = layoutUiModel.pageName
                )
            )
        }
    }

    private fun <T> MutableList<Visitable<*>>.removeFirstLayout(model: Class<T>) {
        firstOrNull { it.javaClass == model }?.let {
            val index = indexOf(it)
            removeAt(index)
        }
    }
}
