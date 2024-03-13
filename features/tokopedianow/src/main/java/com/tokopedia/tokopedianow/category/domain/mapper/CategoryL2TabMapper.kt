package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryProductMapper.mapResponseToProductItem
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryProductMapper.updateProductCardItems
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryRecommendationMapper.mapToCategoryRecommendation
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse.CategoryDetail
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.CATEGORY_JUMPER
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.FEATURED_PRODUCT
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.PRODUCT_LIST_FILTER
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.PRODUCT_LIST_INFINITE_SCROLL
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.STATIC_TEXT
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryDividerUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.addProductAdsCarousel
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.updateProductAdsQuantity
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.category.constant.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel

object CategoryL2TabMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        FEATURED_PRODUCT,
        PRODUCT_LIST_FILTER,
        STATIC_TEXT,
        PRODUCT_LIST_INFINITE_SCROLL,
        CATEGORY_JUMPER
    )

    fun MutableList<Visitable<*>>.mapCategoryTabLayout(
        categoryIdL2: String,
        tickerData: GetTickerData?,
        categoryDetail: CategoryDetail,
        filterMap: Map<String, String>,
        components: List<Component>
    ) {
        val quickFilter = findItem<CategoryQuickFilterUiModel>()
        val categoryIdL3 = filterMap[SearchApiConst.SC]

        clear()
        addTicker(categoryIdL2, categoryIdL3, tickerData)

        components.filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }.forEach {
            when (it.type) {
                PRODUCT_LIST_FILTER -> addQuickFilter(it, quickFilter)
                PRODUCT_LIST_INFINITE_SCROLL -> addProductList(it)
                FEATURED_PRODUCT -> addProductAdsCarousel(it.id)
                CATEGORY_JUMPER -> addCategoryRecommendation(categoryDetail, components)
            }
        }
    }

    fun MutableList<Visitable<*>>.mapToQuickFilter(
        quickFilterUiModel: CategoryQuickFilterUiModel
    ) {
        updateItemById(quickFilterUiModel.id) {
            quickFilterUiModel
        }
    }

    fun MutableList<Visitable<*>>.mapProductAdsCarousel(
        item: TokoNowAdsCarouselUiModel,
        response: ProductAdsResponse,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ) {
        updateItemById(item.id) {
            ProductAdsMapper.mapProductAdsCarousel(
                id = item.id,
                response = response,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        }
    }

    inline fun<reified T> MutableList<Visitable<*>>.findItem(): T? {
        return find { it is T } as? T
    }

    fun MutableList<Visitable<*>>.addProductCardItems(
        response: AceSearchProductModel,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ) {
        response.searchProduct.data.productList.forEachIndexed { index, product ->
            add(mapResponseToProductItem(index, product, miniCartData, hasBlockedAddToCart))
        }
    }

    fun MutableList<Visitable<*>>.getProductIndex(productId: String): Int {
        return filterIsInstance<ProductItemDataView>().indexOfFirst {
            it.productCardModel.productId == productId
        }
    }

    fun MutableList<Visitable<*>>.findProductCardItem(productId: String): ProductItemDataView {
        return filterIsInstance<ProductItemDataView>().first {
            it.productCardModel.productId == productId
        }
    }

    fun MutableList<Visitable<*>>.updateAllProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        hasBlockedAddToCart: Boolean
    ) {
        val cartProductIds = miniCartData.miniCartItems.values.mapNotNull {
            if (it is MiniCartItem.MiniCartItemProduct) it.productId else null
        }
        updateProductCardItems(cartProductIds, miniCartData, hasBlockedAddToCart)
        updateProductAdsQuantity(cartProductIds, miniCartData, hasBlockedAddToCart)
    }

    fun MutableList<Visitable<*>>.addLoadMoreLoading() {
        add(TokoNowProgressBarUiModel(CategoryStaticLayoutId.LOAD_MORE_PROGRESS_BAR))
    }

    fun MutableList<Visitable<*>>.removeLoadMoreLoading() {
        removeItem(CategoryStaticLayoutId.LOAD_MORE_PROGRESS_BAR)
    }

    fun MutableList<Visitable<*>>.filterNotLoadedLayout(): MutableList<Visitable<*>> {
        return filter { it.getLayoutState() == TokoNowLayoutState.LOADING }.toMutableList()
    }

    fun List<Component>.filterTabComponents(): List<Component> {
        return filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }
    }

    fun MutableList<Visitable<*>>.addEmptyState(
        violation: AceSearchProductModel.Violation
    ) {
        val emptyStateUiModel = TokoNowEmptyStateNoResultUiModel(
            activeFilterList = null,
            excludeFilter = null,
            defaultTitle = violation.headerText,
            defaultDescription = violation.descriptionText,
            defaultDescriptionResId = R.string.tokopedianow_empty_product_filter_l2_description,
            defaultImage = violation.imageUrl,
            enablePrimaryButton = false,
            enableSecondaryButton = false
        )
        add(emptyStateUiModel)
    }

    fun MutableList<Visitable<*>>.addCategoryMenu() {
        val categoryMenuUiModel = TokoNowCategoryMenuUiModel(
            id = CategoryStaticLayoutId.CATEGORY_MENU_EMPTY_STATE,
            title = "",
            categoryListUiModel = null,
            state = TokoNowLayoutState.LOADING,
            source = TOKONOW_CATEGORY_L2
        )
        add(categoryMenuUiModel)
    }

    fun MutableList<Visitable<*>>.addCategoryRecommendation(
        components: List<Component>,
        categoryDetail: CategoryDetail,
        isAllProductShown: Boolean
    ) {
        val isLastItem = components.lastOrNull()?.type == CATEGORY_JUMPER

        if (isLastItem && isAllProductShown) {
            val categoryMenuUiModel = categoryDetail.mapToCategoryRecommendation(
                source = TOKONOW_CATEGORY_L2
            )
            add(CategoryDividerUiModel(topMargin = 16))
            add(categoryMenuUiModel)
        }
    }

    fun MutableList<Visitable<*>>.addFeedbackWidget() {
        add(TokoNowFeedbackWidgetUiModel(isDivider = true))
    }

    fun MutableList<Visitable<*>>.addDivider() {
        add(CategoryDividerUiModel())
    }

    fun MutableList<Visitable<*>>.addProductRecommendation() {
        val recommendationUiModel = TokoNowProductRecommendationUiModel(
            requestParam = GetRecommendationRequestParam(
                pageName = RecomPageConstant.TOKONOW_NO_RESULT,
                categoryIds = emptyList(),
                xSource = RecomPageConstant.RECOM_WIDGET,
                isTokonow = true,
                pageNumber = RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET,
                keywords = emptyList(),
                xDevice = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
            ),
            tickerPageSource = GetTargetedTickerUseCase.CATEGORY_PAGE
        )
        add(recommendationUiModel)
    }

    fun createMapParameter(queryParams: MutableMap<String?, Any?>): Map<String, String> {
        val mapParameter = mutableMapOf<String, String>()

        queryParams.forEach {
            it.key?.let { key ->
                mapParameter[key] = it.value.toString()
            }
        }

        return mapParameter
    }

    private fun MutableList<Visitable<*>>.addTicker(
        categoryIdL2: String,
        categoryIdL3: String?,
        tickerData: GetTickerData?
    ) {
        if (tickerData != null && !tickerData.isTickerEmpty()) {
            val tickerList = tickerData.tickerList
            val oosTickerList = tickerData.oosTickerList
            val oosCategoryIds = tickerData.oosCategoryIds
            val isCategoryOutOfStock = oosCategoryIds.contains(categoryIdL2) ||
                oosCategoryIds.contains(categoryIdL3)

            if (tickerList.isEmpty() && isCategoryOutOfStock) {
                add(
                    TokoNowTickerUiModel(
                        id = CategoryStaticLayoutId.TICKER_WIDGET_ID,
                        tickers = oosTickerList,
                        hasOutOfStockTicker = true
                    )
                )
            }
        } else {
            removeFirst { it is TokoNowTickerUiModel }
        }
    }

    private fun MutableList<Visitable<*>>.addQuickFilter(
        response: Component,
        quickFilter: CategoryQuickFilterUiModel?
    ) {
        if (quickFilter == null) {
            add(CategoryQuickFilterUiModel(id = response.id))
        } else {
            add(quickFilter.copy(state = TokoNowLayoutState.LOADING))
        }
    }

    private fun MutableList<Visitable<*>>.addProductList(response: Component) {
        add(CategoryProductListUiModel(id = response.id))
    }

    private fun MutableList<Visitable<*>>.addCategoryRecommendation(
        categoryDetail: CategoryDetail,
        components: List<Component>
    ) {
        val isLastItem = components.lastOrNull()?.type == CATEGORY_JUMPER

        if (!isLastItem) {
            val categoryMenuUiModel = categoryDetail.mapToCategoryRecommendation(
                source = TOKONOW_CATEGORY_L2
            )
            add(categoryMenuUiModel)
        }
    }

    private fun MutableList<Visitable<*>>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    private fun MutableList<Visitable<*>>.updateItemById(id: String?, block: () -> Visitable<*>?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is TokoNowAdsCarouselUiModel -> id
            is CategoryQuickFilterUiModel -> id
            is TokoNowProgressBarUiModel -> id
            else -> null
        }
    }

    private fun Visitable<*>.getLayoutState(): Int? {
        return when (this) {
            is TokoNowAdsCarouselUiModel -> state
            is CategoryQuickFilterUiModel -> state
            is CategoryProductListUiModel -> state
            else -> null
        }
    }

    inline fun <reified T : Visitable<*>>MutableList<Visitable<*>>.removeItem() {
        removeFirst { it is T }
    }

    fun MutableList<Visitable<*>>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }
}
