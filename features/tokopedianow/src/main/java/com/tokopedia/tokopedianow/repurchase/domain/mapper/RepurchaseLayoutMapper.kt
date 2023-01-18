package com.tokopedia.tokopedianow.repurchase.domain.mapper

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.RECOM_WIDGET
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.REPURCHASE_EMPTY_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
import com.tokopedia.tokopedianow.datefilter.presentation.fragment.TokoNowDateFilterFragment.Companion.ALL_DATE_TRANSACTION_POSITION
import com.tokopedia.tokopedianow.datefilter.presentation.fragment.TokoNowDateFilterFragment.Companion.LAST_ONE_MONTH_POSITION
import com.tokopedia.tokopedianow.datefilter.presentation.fragment.TokoNowDateFilterFragment.Companion.LAST_THREE_MONTHS_POSITION
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryMenuMapper
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryMenuMapper.APPLINK_PARAM_WAREHOUSE_ID
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.SORT_FILTER
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseProductMapper.mapToProductListUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.factory.RepurchaseSortFilterFactory
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.RepurchaseSortFilterType.CATEGORY_FILTER
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.RepurchaseSortFilterType.DATE_FILTER
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.RepurchaseSortFilterType.SORT
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedDateFilter
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedSortFilter
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT
import com.tokopedia.unifycomponents.ChipsUnify

object RepurchaseLayoutMapper {

    const val PRODUCT_REPURCHASE = "product_repurchase"

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

    fun MutableList<Visitable<*>>.updateProductWishlist(productId: String, hasBeenWishlist: Boolean) {
        filterIsInstance<RepurchaseProductUiModel>().find { it.productCardModel.productId == productId }?.apply {
            val index = indexOf(this)
            set(
                index,
                copy(
                    productCardModel = productCardModel.copy(
                        hasBeenWishlist = hasBeenWishlist
                    )
                )
            )
        }
    }

    fun MutableList<Visitable<*>>.removeEmptyStateNoHistory() {
        removeFirstLayout(RepurchaseEmptyStateNoHistoryUiModel::class.java)
    }

    fun MutableList<Visitable<*>>.removeAllProduct()  {
        removeAll { it is RepurchaseProductUiModel }
    }

    fun MutableList<Visitable<*>>.addCategoryMenu(
        @TokoNowLayoutState state: Int
    ) {
        add(TokoNowCategoryMenuUiModel(state = state))
    }

    fun MutableList<Visitable<*>>.mapCategoryMenuData(
        response: List<GetCategoryListResponse.CategoryListResponse.CategoryResponse>?,
        warehouseId: String = ""
    ) {
        getCategoryMenuIndex()?.let { index ->
            val item = this[index]
            if (item is TokoNowCategoryMenuUiModel) {
                val newItem = if (!response.isNullOrEmpty()) {
                    val seeAllAppLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY + APPLINK_PARAM_WAREHOUSE_ID + warehouseId
                    val categoryList = CategoryMenuMapper.mapToCategoryList(
                        response = response,
                        headerName = item.title,
                        seeAllAppLink = seeAllAppLink
                    )
                    item.copy(
                        categoryListUiModel = categoryList,
                        state = TokoNowLayoutState.SHOW,
                        seeAllAppLink = seeAllAppLink
                    )
                } else {
                    item.copy(
                        categoryListUiModel = null,
                        state = TokoNowLayoutState.HIDE,
                        seeAllAppLink = ""
                    )
                }
                removeAt(index)
                add(index, newItem)
            }
        }
    }

    fun MutableList<Visitable<*>>.getCategoryMenuIndex(): Int? {
        return firstOrNull { it is TokoNowCategoryMenuUiModel }?.let { indexOf(it) }
    }

    fun MutableList<Visitable<*>>.addChooseAddress() {
        add(TokoNowChooseAddressWidgetUiModel())
    }

    fun MutableList<Visitable<*>>.addEmptyStateOoc(serviceType: String) {
        add(TokoNowEmptyStateOocUiModel(hostSource = SOURCE, serviceType = serviceType))
    }

    fun MutableList<Visitable<*>>.addProductRecommendation(pageName: String) {
        add(
            TokoNowProductRecommendationUiModel(
                requestParam = GetRecommendationRequestParam(
                    pageName = pageName,
                    xSource = RECOM_WIDGET,
                    isTokonow = true,
                    pageNumber = PAGE_NUMBER_RECOM_WIDGET,
                    xDevice = DEFAULT_VALUE_OF_PARAMETER_DEVICE,
                )
            )
        )
    }

    fun MutableList<Visitable<*>>.addProductRecommendationOoc(pageName: String) {
        add(
            TokoNowProductRecommendationOocUiModel(
                pageName = pageName,
                isFirstLoad = true,
                isBindWithPageName = true,
                miniCartSource = MiniCartSource.TokonowRepurchasePage
            )
        )
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoResult(serviceType: String) {
        add(
            TokoNowEmptyStateNoResultUiModel(
                defaultTitleResId = R.string.tokopedianow_repurchase_no_result_title,
                defaultDescriptionResId = getServiceTypeRes(
                    key = REPURCHASE_EMPTY_RESOURCE_ID,
                    serviceType = serviceType
                ),
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

    fun MutableList<Visitable<*>>.removeProductRecommendation() {
        removeFirstLayout(TokoNowProductRecommendationUiModel::class.java)
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
        miniCart.miniCartItems.values.map { miniCartItem ->
            if (miniCartItem is MiniCartItem.MiniCartItemProduct) {
                val productId = miniCartItem.productId
                val parentId = miniCartItem.productParentId
                val quantity = if (parentId != DEFAULT_PARENT_ID) {
                    val miniCartItemsWithSameParentId = miniCart.miniCartItems.getMiniCartItemParentProduct(miniCartItem.productParentId)
                    miniCartItemsWithSameParentId?.totalQuantity.orZero()
                } else {
                    miniCartItem.quantity
                }
                updateProductQuantity(productId, quantity)
            }
        }
    }

    fun MutableList<Visitable<*>>.updateDeletedATCQuantity(miniCart: MiniCartSimplifiedData, type: String) {
        when (type) {
            PRODUCT_REPURCHASE -> {
                val productList = filterIsInstance<RepurchaseProductUiModel>()
                val cartProductIds = miniCart.miniCartItems.values.mapNotNull {
                    if (it is MiniCartItem.MiniCartItemProduct) it.productId else null
                }
                val deletedProducts = productList.filter { it.productCardModel.productId !in cartProductIds }
                deletedProducts.forEach { model ->
                    val productId = model.productCardModel.productId
                    val parentId = model.parentId

                    if (parentId != DEFAULT_PARENT_ID) {
                        val totalQuantity = miniCart.miniCartItems.getMiniCartItemParentProduct(parentId)?.totalQuantity.orZero()
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
        }
    }

    fun MutableList<Visitable<*>>.updateProductQuantity(productId: String, quantity: Int) {
        val productList = filterIsInstance<RepurchaseProductUiModel>()

        productList.firstOrNull { it.productCardModel.productId == productId }?.let {
            val index = indexOf(it)
            val productCard = it.productCardModel.copy(orderQuantity = quantity)
            set(index, it.copy(productCardModel = productCard))
        }
    }

    private fun <T> MutableList<Visitable<*>>.removeFirstLayout(model: Class<T>) {
        firstOrNull { it.javaClass == model }?.let {
            val index = indexOf(it)
            removeAt(index)
        }
    }
}
