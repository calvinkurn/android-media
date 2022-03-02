package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.CATEGORY
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.EDUCATIONAL_INFORMATION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MIX_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.LEGO_3_IMAGE
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MAIN_QUEST
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.REPURCHASE_PRODUCT
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.SHARING_EDUCATION
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokopedianow.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeRepurchaseMapper.mapRepurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.ProductRecomMapper.mapProductRecomDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getItemIndex
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEmptyStateUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.mapper.EducationalInformationMapper.mapEducationalInformationUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeRepurchaseMapper.mapToRepurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.MixLeftCarouselMapper.mapToMixLeftCarousel
import com.tokopedia.tokopedianow.home.domain.mapper.QuestMapper.mapQuestUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.SharingEducationMapper.mapSharingEducationUiModel
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.domain.mapper.SwitcherMapper.createSwitcherUiModel
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.unifycomponents.ticker.TickerData

object HomeLayoutMapper {

    const val DEFAULT_QUANTITY = 0
    private const val DEFAULT_PARENT_ID = "0"

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        CATEGORY,
        LEGO_3_IMAGE,
        LEGO_6_IMAGE,
        BANNER_CAROUSEL,
        PRODUCT_RECOM,
        REPURCHASE_PRODUCT,
        EDUCATIONAL_INFORMATION,
        SHARING_EDUCATION,
        MAIN_QUEST,
        MIX_LEFT_CAROUSEL
    )

    fun MutableList<HomeLayoutItemUiModel>.addLoadingIntoList() {
        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
        add(HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.addEmptyStateIntoList(
        @HomeStaticLayoutId id: String,
        serviceType: String
    ) {
        val chooseAddressUiModel = TokoNowChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
        when (id) {
            EMPTY_STATE_OUT_OF_COVERAGE -> {
                val layout = TokoNowEmptyStateOocUiModel(id, SOURCE, serviceType)
                add(HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED))
            }
            EMPTY_STATE_FAILED_TO_FETCH_DATA -> {
                add(HomeLayoutItemUiModel(TokoNowServerErrorUiModel, HomeLayoutItemState.LOADED))
            }
            else -> {
                add(HomeLayoutItemUiModel(HomeEmptyStateUiModel(id), HomeLayoutItemState.LOADED))
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.addProductRecomOoc(recommendationWidget: RecommendationWidget) {
        val productRecomUiModel = HomeProductRecomUiModel(id = PRODUCT_RECOM_OOC, recommendationWidget)
        add(HomeLayoutItemUiModel(productRecomUiModel, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeLayoutList(
        response: List<HomeLayoutResponse>,
        hasTickerBeenRemoved: Boolean,
        removeAbleWidgets: List<HomeRemoveAbleWidget>,
        miniCartData: MiniCartSimplifiedData?,
        localCacheModel: LocalCacheModel,
        isLoggedIn: Boolean
    ) {
        val chooseAddressUiModel = TokoNowChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))

        if (!hasTickerBeenRemoved) {
            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = emptyList())
            add(HomeLayoutItemUiModel(ticker, HomeLayoutItemState.NOT_LOADED))
        }

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach { layoutResponse ->
            if (removeAbleWidgets.none { layoutResponse.layout == it.type && it.isRemoved }) {
                val state = HomeLayoutItemState.NOT_LOADED
                val serviceType = localCacheModel.service_type

                mapToHomeUiModel(layoutResponse, state, miniCartData, serviceType)?.let { item ->
                    add(item)
                }

                addSwitcherUiModel(layoutResponse, localCacheModel, isLoggedIn)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.addSwitcherUiModel(
        response: HomeLayoutResponse,
        localCacheModel: LocalCacheModel,
        isLoggedIn: Boolean
    ) {
        if (response.layout == EDUCATIONAL_INFORMATION && isLoggedIn) {
            val switcherUiModel = createSwitcherUiModel(localCacheModel)
            switcherUiModel?.let { uiModel -> add(uiModel) }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.addMoreHomeLayout(
        response: List<HomeLayoutResponse>,
        removeAbleWidgets: List<HomeRemoveAbleWidget>,
        miniCartData: MiniCartSimplifiedData?,
        serviceType: String
    ) {
        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach { layoutResponse ->
            if (removeAbleWidgets.none { layoutResponse.layout == it.type && it.isRemoved }) {
                val state = when (layoutResponse.layout) {
                    CATEGORY, REPURCHASE_PRODUCT, SHARING_EDUCATION, MAIN_QUEST  -> HomeLayoutItemState.NOT_LOADED
                    else ->  HomeLayoutItemState.LOADED
                }

                mapToHomeUiModel(layoutResponse, state, miniCartData, serviceType)?.let { item ->
                    add(item)
                }
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.addProgressBar() {
        add(HomeLayoutItemUiModel(HomeProgressBarUiModel, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.removeProgressBar() {
        removeFirst { it.layout is HomeProgressBarUiModel }
    }

    fun MutableList<HomeLayoutItemUiModel>.setStateToLoading(item: HomeLayoutItemUiModel) {
        item.layout?.let { layout ->
            updateItemById(layout.getVisitableId()) {
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADING)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeCategoryGridData(
        item: TokoNowCategoryGridUiModel,
        response: List<CategoryResponse>?,
        warehouseId: String = ""
    ) {
        updateItemById(item.id) {
            if (!response.isNullOrEmpty()) {
                val categoryList = mapToCategoryList(response, warehouseId)
                val layout = item.copy(categoryList = categoryList, state = TokoNowLayoutState.SHOW)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            } else {
                val layout = item.copy(categoryList = null, state = TokoNowLayoutState.HIDE)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            }
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

    fun MutableList<HomeLayoutItemUiModel>.mapQuestData(
        item: HomeQuestSequenceWidgetUiModel,
        questList: List<HomeQuestWidgetUiModel>,
        state: HomeLayoutItemState
    ) {
        updateItemById(item.visitableId) {
            val quest = HomeQuestSequenceWidgetUiModel(
                id = MAIN_QUEST,
                state = state,
                questList = questList
            )
            HomeLayoutItemUiModel(quest, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapProductPurchaseData(
        item: TokoNowRepurchaseUiModel,
        response: RepurchaseData,
        miniCartData: MiniCartSimplifiedData? = null
    ) {
        updateItemById(item.id) {
            val uiModel = mapToRepurchaseUiModel(item, response, miniCartData)
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

    fun MutableList<HomeLayoutItemUiModel>.updateRepurchaseProductQuantity(
        miniCartData: MiniCartSimplifiedData,
    ) {
        updateAllProductQuantity(miniCartData, REPURCHASE_PRODUCT)
        updateDeletedProductQuantity(miniCartData, REPURCHASE_PRODUCT)
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
            REPURCHASE_PRODUCT -> updateRepurchaseProductQuantity(productId, quantity)
            PRODUCT_RECOM -> updateProductRecomQuantity(productId, quantity)
        }
    }

    // Update quantity to 0 for deleted product in cart
    private fun MutableList<HomeLayoutItemUiModel>.updateDeletedProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            REPURCHASE_PRODUCT -> {
                firstOrNull { it.layout is TokoNowRepurchaseUiModel }?.run {
                    val layout = layout as TokoNowRepurchaseUiModel
                    val cartProductIds = miniCartData.miniCartItems.map { it.productId }
                    val deletedProducts = layout.productList.filter { it.productId !in cartProductIds }
                    val variantGroup = miniCartData.miniCartItems.groupBy { it.productParentId }

                    deletedProducts.forEach { model ->
                        if (model.parentId != DEFAULT_PARENT_ID) {
                            val miniCartItemsWithSameParentId = variantGroup[model.parentId]
                            val totalQuantity = miniCartItemsWithSameParentId?.sumOf { it.quantity }.orZero()
                            if (totalQuantity == DEFAULT_QUANTITY) {
                                updateRepurchaseProductQuantity(model.productId, DEFAULT_QUANTITY)
                            } else {
                                updateRepurchaseProductQuantity(model.productId, totalQuantity)
                            }
                        } else {
                            updateRepurchaseProductQuantity(model.productId, DEFAULT_QUANTITY)
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

    private fun MutableList<HomeLayoutItemUiModel>.updateRepurchaseProductQuantity(
        productId: String,
        quantity: Int
    ) {
        firstOrNull { it.layout is TokoNowRepurchaseUiModel }?.run {
            val layoutUiModel = layout as TokoNowRepurchaseUiModel
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
            val layout = homeLayoutItemUiModel.layout
            val uiModel = layout as HomeProductRecomUiModel
            if (!uiModel.recomWidget.recommendationItemList.isNullOrEmpty()) {
                val recom = uiModel.recomWidget
                val recommendationItemList = recom.recommendationItemList.toMutableList()
                val recommendationItem = recommendationItemList.firstOrNull {
                    it.productId.toString() == productId
                }

                if (recommendationItem?.quantity == quantity) return

                val index = recommendationItemList.indexOf(recommendationItem)

                recommendationItemList.getOrNull(index)?.copy(quantity = quantity)?.let {
                    recommendationItemList[index] = it
                }

                updateItemById(layout.getVisitableId()) {
                    val updatedRecom = recom.copy(recommendationItemList = recommendationItemList)
                    val updatedUiModel = uiModel.copy(recomWidget = updatedRecom)
                    homeLayoutItemUiModel.copy(layout = updatedUiModel)
                }
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }

    fun MutableList<HomeLayoutItemUiModel>.updateProductRecom(
        productId: String,
        quantity: Int
    ): HomeProductRecomUiModel? {
        return filter { it.layout is HomeProductRecomUiModel }.firstOrNull { uiModel ->
            val productRecom = uiModel.layout as HomeProductRecomUiModel
            val recomWidget = productRecom.recomWidget
            val recommendationItemList = recomWidget.recommendationItemList
            recommendationItemList.firstOrNull { it.productId.toString() == productId } != null
        }?.let { uiModel ->
            val productRecom = uiModel.layout as HomeProductRecomUiModel
            val recomWidget = productRecom.recomWidget
            val recomItemList = recomWidget.recommendationItemList.toMutableList()

            val product = recomItemList.first { it.productId.toString() == productId }
            val position = recomItemList.indexOf(product)
            recomItemList[position] = product.copy(quantity = quantity)

            val updatedRecomWidget = recomWidget.copy(recommendationItemList = recomItemList)
            return productRecom.copy(recomWidget = updatedRecomWidget)
        }
    }

    inline fun<reified T: Visitable<*>> List<HomeLayoutItemUiModel>.getItem(itemClass: Class<T>): T? {
        return mapNotNull { it.layout }.find {
            it.javaClass == itemClass
        } as? T
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is HomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            is TokoNowRepurchaseUiModel -> id
            is TokoNowCategoryGridUiModel -> id
            else -> null
        }
    }

    private fun mapToHomeUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED,
        miniCartData: MiniCartSimplifiedData? = null,
        serviceType: String
    ): HomeLayoutItemUiModel? {
        val loadedState = HomeLayoutItemState.LOADED

        return when (response.layout) {
            CATEGORY -> mapToCategoryLayout(response, state)
            LEGO_3_IMAGE, LEGO_6_IMAGE -> mapLegoBannerDataModel(response, loadedState)
            BANNER_CAROUSEL -> mapSliderBannerModel(response, loadedState)
            PRODUCT_RECOM -> mapProductRecomDataModel(response, loadedState, miniCartData)
            REPURCHASE_PRODUCT -> mapRepurchaseUiModel(response, state)
            EDUCATIONAL_INFORMATION -> mapEducationalInformationUiModel(response, loadedState, serviceType)
            SHARING_EDUCATION -> mapSharingEducationUiModel(response, state, serviceType)
            MAIN_QUEST -> mapQuestUiModel(response, state)
            MIX_LEFT_CAROUSEL -> mapToMixLeftCarousel(response, loadedState)
            else -> null
        }
    }
}
