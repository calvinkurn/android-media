package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.CATEGORY
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.EDUCATIONAL_INFORMATION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.LEGO_3_IMAGE
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.RECENT_PURCHASE
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.SHARING_EDUCATION
import com.tokopedia.tokopedianow.common.model.TokoNowLayoutUiModel
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokopedianow.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.RecentPurchaseMapper.mapRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.ProductRecomMapper.mapProductRecomDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getItemIndex
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.RecentPurchaseData
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.EducationalInformationMapper.mapEducationalInformationUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.RecentPurchaseMapper.mapToRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.SharingEducationMapper.mapSharingEducationUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.unifycomponents.ticker.TickerData

object HomeLayoutMapper {

    const val DEFAULT_QUANTITY = 0
    private const val DEFAULT_PARENT_ID = "0"

    /**
     * List of layout IDs that doesn't need to call GQL query from Toko Now Home
     * to fetch the data. For example: Choose Address Widget. The GQL call for
     * Choose Address Widget data is done internally, so Toko Now Home doesn't
     * need to call query to fetch data for it.
     */
    private val STATIC_LAYOUT_ID = listOf(
        CHOOSE_ADDRESS_WIDGET_ID,
        EMPTY_STATE_NO_ADDRESS,
        EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE,
        EMPTY_STATE_FAILED_TO_FETCH_DATA,
        PRODUCT_RECOM_OOC
    )

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        CATEGORY,
        LEGO_3_IMAGE,
        LEGO_6_IMAGE,
        BANNER_CAROUSEL,
        PRODUCT_RECOM,
        RECENT_PURCHASE,
        EDUCATIONAL_INFORMATION,
        SHARING_EDUCATION
    )

    fun MutableList<HomeLayoutItemUiModel>.addLoadingIntoList() {
        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
        add(HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.addEmptyStateIntoList(id: String) {
        val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        val homeEmptyStateUiModel = HomeEmptyStateUiModel(id = id)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
        add(HomeLayoutItemUiModel(homeEmptyStateUiModel, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.addProductRecomOoc(recommendationWidget: RecommendationWidget) {
        val productRecomUiModel = HomeProductRecomUiModel(id = PRODUCT_RECOM_OOC, recommendationWidget)
        add(HomeLayoutItemUiModel(productRecomUiModel, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeLayoutList(
        response: List<HomeLayoutResponse>,
        hasTickerBeenRemoved: Boolean,
        hasSharingEducationBeenRemoved: Boolean
    ) {
        val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))

        if (!hasTickerBeenRemoved) {
            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = emptyList())
            add(HomeLayoutItemUiModel(ticker, HomeLayoutItemState.NOT_LOADED))
        }

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach {
            if (!(hasSharingEducationBeenRemoved && it.layout == SHARING_EDUCATION)) {
                mapToHomeUiModel(it)?.let { item ->
                    add(item)
                }
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapGlobalHomeLayoutData(
        item: HomeComponentVisitable,
        response: HomeLayoutResponse
    ) {
        mapToHomeUiModel(response, HomeLayoutItemState.LOADED)?.let {
            updateItemById(item.visitableId()) { it }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapGlobalHomeLayoutData(
        item: HomeLayoutUiModel,
        response: HomeLayoutResponse,
        miniCartData: MiniCartSimplifiedData? = null
    ) {
        mapToHomeUiModel(response, HomeLayoutItemState.LOADED, miniCartData)?.let {
            updateItemById(item.visitableId) { it }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.updateStateToLoading(item: HomeLayoutItemUiModel) {
        item.layout.let { layout ->
            updateItemById(layout.getVisitableId()) {
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADING)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeCategoryGridData(
        item: TokoNowCategoryGridUiModel,
        response: List<CategoryResponse>?
    ) {
        updateItemById(item.visitableId) {
            if (!response.isNullOrEmpty()) {
                val categoryList = mapToCategoryList(response)
                val layout = item.copy(categoryList = categoryList, state = TokoNowLayoutState.SHOW)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            } else {
                val layout = item.copy(categoryList = null, state = TokoNowLayoutState.HIDE)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapEducationalInformationData(
        item: HomeEducationalInformationWidgetUiModel
    ) {
        updateItemById(item.visitableId) {
            HomeLayoutItemUiModel(item.copy(state = HomeLayoutItemState.LOADED), HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapSharingEducationData(
        item: HomeSharingEducationWidgetUiModel
    ) {
        updateItemById(item.visitableId) {
            HomeLayoutItemUiModel(item.copy(state = HomeLayoutItemState.LOADED), HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapTickerData(
        item: HomeTickerUiModel,
        tickerData: List<TickerData>
    ) {
        updateItemById(item.visitableId) {
            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = tickerData)
            HomeLayoutItemUiModel(ticker, HomeLayoutItemState.LOADED)
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapProductRecomData(
        item: HomeProductRecomUiModel,
        recommendationWidget: RecommendationWidget
    ) {
        updateItemById(item.visitableId) {
            val productRecom = HomeProductRecomUiModel(id = item.visitableId, recomWidget = recommendationWidget)
            HomeLayoutItemUiModel(productRecom, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapProductPurchaseData(
        item: TokoNowRecentPurchaseUiModel,
        response: RecentPurchaseData,
        miniCartData: MiniCartSimplifiedData? = null
    ) {
        updateItemById(item.visitableId) {
            val uiModel = mapToRecentPurchaseUiModel(item, response, miniCartData)
            HomeLayoutItemUiModel(uiModel, HomeLayoutItemState.LOADED)
        }
    }

    fun getAddToCartQuantity(productId: String, miniCartData: MiniCartSimplifiedData?): Int {
        return miniCartData?.run {
            val miniCartItem = miniCartData.miniCartItems.firstOrNull {
                it.productId == productId
            }
            val productParentId = miniCartItem?.productParentId

            return if(productParentId != DEFAULT_PARENT_ID) {
                val variantGroup = miniCartItems.groupBy { it.productParentId }
                val miniCartItemsWithSameParentId = variantGroup[productParentId]
                miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
            } else {
                miniCartItem.quantity.orZero()
            }
        } ?: DEFAULT_QUANTITY
    }

    fun MutableList<HomeLayoutItemUiModel>.updateRecentPurchaseQuantity(
        miniCartData: MiniCartSimplifiedData,
    ) {
        updateAllProductQuantity(miniCartData, RECENT_PURCHASE)
        updateDeletedProductQuantity(miniCartData, RECENT_PURCHASE)
    }

    fun MutableList<HomeLayoutItemUiModel>.updateProductRecomQuantity(
        miniCartData: MiniCartSimplifiedData,
    ) {
        updateAllProductQuantity(miniCartData, PRODUCT_RECOM)
        updateDeletedProductQuantity(miniCartData, PRODUCT_RECOM)
    }

    // Update all product with quantity from cart
    private fun MutableList<HomeLayoutItemUiModel>.updateAllProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        @TokoNowLayoutType type: String
    ) {
        miniCartData.miniCartItems.map { miniCartItem ->
            val productId = miniCartItem.productId
            val quantity = getAddToCartQuantity(productId, miniCartData)
            updateProductQuantity(productId, quantity, type)
        }
    }

    // Update single product with quantity from cart
    fun MutableList<HomeLayoutItemUiModel>.updateProductQuantity(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            RECENT_PURCHASE -> updateRecentPurchaseQuantity(productId, quantity)
            PRODUCT_RECOM -> updateProductRecomQuantity(productId, quantity)
        }
    }

    // Update quantity to 0 for deleted product in cart
    private fun MutableList<HomeLayoutItemUiModel>.updateDeletedProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            RECENT_PURCHASE -> {
                firstOrNull { it.layout is TokoNowRecentPurchaseUiModel }?.run {
                    val layout = layout as TokoNowRecentPurchaseUiModel
                    val cartProductIds = miniCartData.miniCartItems.map { it.productId }
                    val deletedProducts = layout.productList.filter { it.productId !in cartProductIds }
                    val variantGroup = miniCartData.miniCartItems.groupBy { it.productParentId }

                    deletedProducts.forEach { model ->
                        if (model.parentId != DEFAULT_PARENT_ID) {
                            val miniCartItemsWithSameParentId = variantGroup[model.parentId]
                            val totalQuantity = miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
                            if (totalQuantity == DEFAULT_QUANTITY) {
                                updateRecentPurchaseQuantity(model.productId, DEFAULT_QUANTITY)
                            } else {
                                updateRecentPurchaseQuantity(model.productId, totalQuantity)
                            }
                        } else {
                            updateRecentPurchaseQuantity(model.productId, DEFAULT_QUANTITY)
                        }
                    }
                }
            }
            PRODUCT_RECOM -> {
                filter { it.layout is HomeProductRecomUiModel }.forEach { homeLayoutItemUiModel->
                    val layout = homeLayoutItemUiModel.layout as HomeProductRecomUiModel
                    val cartProductIds = miniCartData.miniCartItems.map { it.productId }
                    val deletedProducts = layout.recomWidget.recommendationItemList.filter { it.productId.toString() !in cartProductIds }
                    val variantGroup = miniCartData.miniCartItems.groupBy { it.productParentId }

                    deletedProducts.forEach { item ->
                        if (item.parentID.toString() != DEFAULT_PARENT_ID) {
                            val miniCartItemsWithSameParentId = variantGroup[item.parentID.toString()]
                            val totalQuantity = miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
                            if (totalQuantity == DEFAULT_QUANTITY) {
                                updateProductRecomQuantity(item.productId.toString(), DEFAULT_QUANTITY)
                            } else {
                                updateProductRecomQuantity(item.productId.toString(), totalQuantity)
                            }
                        } else {
                            updateProductRecomQuantity(item.productId.toString(), DEFAULT_QUANTITY)
                        }
                    }
                }
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.updateRecentPurchaseQuantity(
        productId: String,
        quantity: Int
    ) {
        firstOrNull { it.layout is TokoNowRecentPurchaseUiModel }?.run {
            val layoutUiModel = layout as TokoNowRecentPurchaseUiModel
            val productList = layoutUiModel.productList.toMutableList()
            val productUiModel = productList.firstOrNull {
                it.productId == productId
            }
            val index = layoutUiModel.productList.indexOf(productUiModel)

            productUiModel?.product?.run {
                if (hasVariant()) {
                    copy(variant = variant?.copy(quantity = quantity))
                } else {
                    copy(
                        hasAddToCartButton = quantity == DEFAULT_QUANTITY,
                        nonVariant = nonVariant?.copy(quantity = quantity)
                    )
                }
            }?.let {
                updateItemById(layout.getVisitableId()) {
                    productList[index] = productUiModel.copy(product = it)
                    copy(layout = layoutUiModel.copy(productList = productList))
                }
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.updateProductRecomQuantity(
        productId: String,
        quantity: Int
    ) {
        filter { it.layout is HomeProductRecomUiModel }.forEach { homeLayoutItemUiModel ->
            val uiModel = homeLayoutItemUiModel.layout as? HomeProductRecomUiModel
            if (!uiModel?.recomWidget?.recommendationItemList.isNullOrEmpty()) {
                val recom = uiModel?.recomWidget?.copy()
                val recommendationItem = recom?.recommendationItemList?.firstOrNull { it.productId.toString() == productId }
                recommendationItem?.let {
                    it.quantity = quantity
                    mapProductRecomData(uiModel, recom)
                }
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.setQuantityToZero(
        productId: String,
        @TokoNowLayoutType type: String
    ) = updateProductQuantity(productId, DEFAULT_QUANTITY, type)

    fun MutableList<HomeLayoutItemUiModel>.removeItem(id: String) {
        getItemIndex(id)?.let { removeAt(it) }
    }

    fun MutableList<HomeLayoutItemUiModel>.findNextIndex(): Int? {
        return firstOrNull { it.state == HomeLayoutItemState.NOT_LOADED }?.let { indexOf(it) }
    }

    fun Visitable<*>.isNotStaticLayout(): Boolean {
        return this.getVisitableId() !in STATIC_LAYOUT_ID
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is HomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            is TokoNowLayoutUiModel -> visitableId
            else -> null
        }
    }

    private fun mapToHomeUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED,
        miniCartData: MiniCartSimplifiedData? = null
    ): HomeLayoutItemUiModel? {
        return when (response.layout) {
            CATEGORY -> mapToCategoryLayout(response, state)
            LEGO_3_IMAGE, LEGO_6_IMAGE -> mapLegoBannerDataModel(response, state)
            BANNER_CAROUSEL -> mapSliderBannerModel(response, state)
            PRODUCT_RECOM -> mapProductRecomDataModel(response, state, miniCartData)
            RECENT_PURCHASE -> mapRecentPurchaseUiModel(response, state)
            EDUCATIONAL_INFORMATION -> mapEducationalInformationUiModel(response, state)
            SHARING_EDUCATION -> mapSharingEducationUiModel(response, state)
            else -> null
        }
    }
}
