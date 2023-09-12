package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryProductMapper.mapResponseToProductItem
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryProductMapper.updateProductCardItems
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.FEATURED_PRODUCT
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.PRODUCT_LIST_FILTER
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.PRODUCT_LIST_INFINITE_SCROLL
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.STATIC_TEXT
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateDivider
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
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel

object CategoryL2TabMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        FEATURED_PRODUCT,
        PRODUCT_LIST_FILTER,
        STATIC_TEXT,
        PRODUCT_LIST_INFINITE_SCROLL
    )

    fun MutableList<Visitable<*>>.mapToCategoryTabLayout(components: List<Component>) {
        components.filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }.forEach {
            when (it.type) {
                PRODUCT_LIST_FILTER -> addQuickFilter(it)
                PRODUCT_LIST_INFINITE_SCROLL -> addProductList(it)
                FEATURED_PRODUCT -> addProductAdsCarousel(it.id)
            }
        }
    }

    fun MutableList<Visitable<*>>.addTicker(
        categoryIdL2: String,
        tickerData: GetTickerData?
    ) {
        if(tickerData == null) return
        val tickerList = tickerData.tickerList
        val oosTickerList = tickerData.oosTickerList
        val oosCategoryIds = tickerData.oosCategoryIds

        if(oosTickerList.isNotEmpty() && oosCategoryIds.contains(categoryIdL2)) {
            add(TokoNowTickerUiModel(id = CategoryStaticLayoutId.TICKER_WIDGET_ID, tickers = oosTickerList))
        } else {
            add(TokoNowTickerUiModel(id = CategoryStaticLayoutId.TICKER_WIDGET_ID, tickers = tickerList))
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

    fun MutableList<Visitable<*>>.addProductCardItems(
        response: AceSearchProductModel,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ) {
        response.searchProduct.data.productList.forEachIndexed { index, product ->
            add(mapResponseToProductItem(index, product, miniCartData, hasBlockedAddToCart))
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

    fun MutableList<Visitable<*>>.updateAllProductQuantity(
        productId: String,
        quantity: Int,
        hasBlockedAddToCart: Boolean
    ) {
        updateProductCardItems(productId, quantity, hasBlockedAddToCart)
        updateProductAdsQuantity(productId, quantity, hasBlockedAddToCart)
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
        violation: AceSearchProductModel.Violation,
        excludedFilter: Option?
    ) {
        val emptyStateUiModel = TokoNowEmptyStateNoResultUiModel(
            activeFilterList = emptyList(),
            excludeFilter = excludedFilter,
            defaultTitle = violation.headerText,
            defaultDescription = violation.descriptionText,
            defaultImage = violation.imageUrl,
            defaultTextPrimaryButton = violation.buttonText,
            defaultUrlPrimaryButton = violation.ctaUrl
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

    fun MutableList<Visitable<*>>.addFeedbackWidget() {
        add(TokoNowFeedbackWidgetUiModel(isDivider = true))
    }

    fun MutableList<Visitable<*>>.addEmptyStateDivider() {
        add(CategoryEmptyStateDivider())
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

    private fun MutableList<Visitable<*>>.addQuickFilter(response: Component) {
        add(CategoryQuickFilterUiModel(id = response.id))
    }

    private fun MutableList<Visitable<*>>.addProductList(response: Component) {
        add(CategoryProductListUiModel(id = response.id))
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
