package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addHeader
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowLocalLoadUiModel
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.Main
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.resetIndices
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addEmptyShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addExpandCollapse
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLocalLoad
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProductCartItem
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProductCarts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addTopCheckAllShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.filteredBy
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.getExpandCollapse
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductRecommendationMapper.mapRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductShoppingListMapper.mapAvailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductShoppingListMapper.mapUnavailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.RecommendationModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListLoadingMoreUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel.Companion.INVALID_SHOP_ID
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel.Companion.PRODUCT_RECOMMENDATION_PAGE_NAME
import com.tokopedia.tokopedianow.shoppinglist.util.Constant
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import org.junit.After
import org.junit.Test

class ShoppingListLoadLayout: TokoNowShoppingListViewModelFixture() {

    private val mutableLayout = mutableListOf<Visitable<*>>()

    private val availableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val unavailableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val recommendedProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val cartProducts = mutableListOf<ShoppingListCartProductItemUiModel>()

    private val filteredAvailableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    private val filteredUnavailableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    private val filteredRecommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    private var mMiniCartData: MiniCartSimplifiedData? = null
    private var isFirstPageProductRecommendationError = false
    private var recommendationModel: RecommendationModel = RecommendationModel()

    @After
    fun tearDown() {
        mutableLayout.clear()

        availableProducts.clear()
        unavailableProducts.clear()
        recommendedProducts.clear()
        cartProducts.clear()

        filteredAvailableProducts.clear()
        filteredUnavailableProducts.clear()
        filteredRecommendedProducts.clear()

        mMiniCartData = null
        isFirstPageProductRecommendationError = false
        recommendationModel = RecommendationModel()
    }

    private fun getExpandCollapseState(
        productLayoutType: ShoppingListProductLayoutType
    ): ShoppingListProductState = mutableLayout.getExpandCollapse(productLayoutType)?.productState ?: ShoppingListProductState.COLLAPSE

    private fun filterProductRecommendationWithAvailableProduct() {
        val newRecommendedProducts = recommendedProducts.filteredBy(
            productList = availableProducts
        )

        filteredRecommendedProducts.clear()
        filteredRecommendedProducts.addAll(newRecommendedProducts)
    }

    private fun filterShoppingListWithProductCart(
        miniCartData: MiniCartSimplifiedData?
    ) {
        filteredAvailableProducts.clear()
        filteredUnavailableProducts.clear()

        if (miniCartData == null) {
            filteredAvailableProducts.addAll(availableProducts)
            filteredUnavailableProducts.addAll(unavailableProducts)
        } else {
            val miniCartItems = miniCartData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()

            cartProducts.clear()

            val newAvailableProducts = cartProducts.addProductCartItem(
                productList = availableProducts.toMutableList(),
                miniCartItems = miniCartItems
            )

            val newUnavailableProducts = cartProducts.addProductCartItem(
                productList = unavailableProducts.toMutableList(),
                miniCartItems = miniCartItems
            )

            filteredAvailableProducts.addAll(newAvailableProducts)
            filteredUnavailableProducts.addAll(newUnavailableProducts)
        }
    }

    private fun addHeaderSection() {
        mutableLayout.addHeader(
            headerModel = HeaderModel(
                pageTitle = resourceProvider.getString(R.string.tokopedianow_shopping_list_page_title),
                pageTitleColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Static_White),
                ctaText = resourceProvider.getString(R.string.tokopedianow_shopping_list_repurchase),
                ctaTextColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Static_White),
                ctaChevronColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Static_White),
                backgroundGradientColor = TokoNowThematicHeaderUiModel.GradientColor(
                    startColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_GN500),
                    endColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_GN400)
                ),
                chooseAddressResIntColor = unifyprinciplesR.color.Unify_Static_White,
                isChooseAddressShown = true,
                isSuperGraphicImageShown = true
            ),
            state = SHOW
        )
    }

    private fun addShoppingListSection(
        isShoppingListAvailable: Boolean,
        availableExpandCollapseCurrentState: ShoppingListProductState,
        unavailableExpandCollapseCurrentState: ShoppingListProductState
    ) {
        mutableLayout.doIf(
            predicate = isShoppingListAvailable,
            then = {
                /**
                 * Add Available Products
                 */

                doIf(
                    predicate = filteredAvailableProducts.isNotEmpty(),
                    then = layout@ {
                        val displayedAvailableItems = filteredAvailableProducts.take(Constant.MAX_TOTAL_PRODUCT_DISPLAYED)
                        val areAllAvailableProductsSelected = filteredAvailableProducts.count { it.isSelected } == filteredAvailableProducts.size
                        val areAvailableProductsMoreThanDefaultDisplayed = filteredAvailableProducts.size > Constant.MAX_TOTAL_PRODUCT_DISPLAYED
                        val remainingTotalProduct = filteredAvailableProducts.size - displayedAvailableItems.size
                        val newState = if (availableExpandCollapseCurrentState == ShoppingListProductState.EXPAND && areAvailableProductsMoreThanDefaultDisplayed) ShoppingListProductState.EXPAND else ShoppingListProductState.COLLAPSE
                        val newDisplayedItems = if (availableExpandCollapseCurrentState == ShoppingListProductState.EXPAND && areAvailableProductsMoreThanDefaultDisplayed) filteredAvailableProducts else displayedAvailableItems

                        this@layout
                            .addTopCheckAllShoppingList(
                                productState = newState,
                                isSelected = areAllAvailableProductsSelected
                            )
                            .addProducts(newDisplayedItems.resetIndices())
                            .doIf(
                                predicate = areAvailableProductsMoreThanDefaultDisplayed,
                                then = {
                                    addExpandCollapse(
                                        productState = newState,
                                        remainingTotalProduct = remainingTotalProduct,
                                        productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
                                    )
                                }
                            )
                        doIf(
                            predicate = filteredUnavailableProducts.isNotEmpty(),
                            then = {
                                addDivider()
                            }
                        )
                    }
                )

                /**
                 * Add Unavailable Products
                 */

                doIf(
                    predicate = filteredUnavailableProducts.isNotEmpty(),
                    then = layout@ {
                        val displayedUnavailableItems = filteredUnavailableProducts.take(Constant.MAX_TOTAL_PRODUCT_DISPLAYED)
                        val areUnavailableProductsMoreThanDefaultDisplayed = filteredUnavailableProducts.size > Constant.MAX_TOTAL_PRODUCT_DISPLAYED
                        val remainingTotalProduct = filteredUnavailableProducts.size - displayedUnavailableItems.size
                        val newState = if (unavailableExpandCollapseCurrentState == ShoppingListProductState.EXPAND && areUnavailableProductsMoreThanDefaultDisplayed) ShoppingListProductState.EXPAND else ShoppingListProductState.COLLAPSE
                        val newDisplayedItems = if (unavailableExpandCollapseCurrentState == ShoppingListProductState.EXPAND && areUnavailableProductsMoreThanDefaultDisplayed) filteredUnavailableProducts else displayedUnavailableItems

                        this@layout
                            .addTitle("${resourceProvider.getString(R.string.tokopedianow_shopping_list_unavailable_widget_title)} (${filteredUnavailableProducts.size})")
                            .addProducts(newDisplayedItems.resetIndices())
                            .doIf(
                                predicate = areUnavailableProductsMoreThanDefaultDisplayed,
                                then = {
                                    addExpandCollapse(
                                        productState = newState,
                                        remainingTotalProduct = remainingTotalProduct,
                                        productLayoutType = ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
                                    )
                                }
                            )
                    }
                )
            },
            ifNot = {
                addEmptyShoppingList()
            }
        )
    }

    private fun addProductCartWidgetSection(
        isShoppingListAvailable: Boolean
    ) {
        mutableLayout
            .doIf(
                predicate = cartProducts.isNotEmpty(),
                then = layout@ {
                    this@layout
                        .doIf(
                            predicate = isShoppingListAvailable,
                            then = {
                                addDivider()
                            }
                        )
                        .addTitle("${cartProducts.size} ${resourceProvider.getString(R.string.tokopedianow_shopping_list_cart_widget_title)}")
                        .addProductCarts(cartProducts)
                }
            )
    }

    private fun addProductRecommendationSection(
        isShoppingListAvailable: Boolean
    ) {
        mutableLayout
            .doIf(
                predicate = filteredRecommendedProducts.isNotEmpty(),
                then = layout@ {
                    this@layout
                        .doIf(
                            predicate = isShoppingListAvailable,
                            then = {
                                addDivider()
                            }
                        )
                        .addTitle(recommendationModel.title)
                        .addProducts(filteredRecommendedProducts.resetIndices())
                        .doIf(
                            predicate = recommendationModel.hasNext,
                            then = {
                                addLoadMore()
                            }
                        )
                },
                ifNot = layout@{
                    this@layout
                        .doIf(
                            predicate = isFirstPageProductRecommendationError,
                            then = {
                                doIf(
                                    predicate = isShoppingListAvailable,
                                    then = {
                                        addDivider()
                                    }
                                )
                                addLocalLoad()
                            }
                        )
                }
            )
    }

    private fun updateLayout() {
        filterProductRecommendationWithAvailableProduct()
        filterShoppingListWithProductCart(mMiniCartData)

        val availableExpandCollapseCurrentState = getExpandCollapseState(
            productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
        )
        val unavailableExpandCollapseCurrentState = getExpandCollapseState(
            productLayoutType = ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
        )

        mutableLayout.clear()

        val isShoppingListAvailable = availableProducts.isNotEmpty() || unavailableProducts.isNotEmpty()
        val isFilteredShoppingListAvailable =  filteredAvailableProducts.isNotEmpty() || filteredUnavailableProducts.isNotEmpty()

        addHeaderSection()
        addShoppingListSection(
            isShoppingListAvailable = isShoppingListAvailable,
            availableExpandCollapseCurrentState = availableExpandCollapseCurrentState,
            unavailableExpandCollapseCurrentState = unavailableExpandCollapseCurrentState
        )
        addProductCartWidgetSection(
            isShoppingListAvailable = isFilteredShoppingListAvailable
        )
        addProductRecommendationSection(
            isShoppingListAvailable = isShoppingListAvailable
        )
    }

    private fun setDataToGlobalVariables(
        miniCartData: MiniCartSimplifiedData?,
        shoppingList: GetShoppingListDataResponse.Data,
        recommendationWidget: RecommendationWidget,
        shopId: Long,
        isProductRecommendationError: Boolean = false
    ) {
        mMiniCartData = if (shopId != INVALID_SHOP_ID) miniCartData else null

        availableProducts.addAll(mapAvailableShoppingList(shoppingList.listAvailableItem))
        unavailableProducts.addAll(mapUnavailableShoppingList(shoppingList.listUnavailableItem))

        recommendationModel = RecommendationModel(
            pageCounter = if (recommendationWidget.hasNext) Int.ONE else Int.ZERO,
            hasNext = recommendationWidget.hasNext,
            title = recommendationWidget.title
        )
        recommendedProducts.addAll(mapRecommendedProducts(recommendationWidget))

        isFirstPageProductRecommendationError = isProductRecommendationError
    }

    @Test
    fun `When processing to load layout, recommendation is empty here but not for mini cart data and shopping list (available and unavailable), intersection is done between mini cart data and available list to get data for cart widget, the result should be contains cart widget because the intersection result is not empty`() {
        // create fake variables for stub
        val shopId = 222121L
        val miniCartData = Main.createMiniCart()
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = false,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            response = miniCartData
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify cart product existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListCartProductUiModel>().isNotEmpty()
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )
    }

    @Test
    fun `When processing to load layout, recommendation is empty here, mini cart data is null because there is invalid shop id, just shopping list (available and unavailable) is not empty, intersection is done between minicart and available list to get data for cart widget, the result should not contain cart widget because the intersection result is empty`() {
        // create fake variables for stub
        val shopId = INVALID_SHOP_ID
        val miniCartData = Main.createMiniCart()
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = false,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            response = miniCartData
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify cart product existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListCartProductUiModel>().isEmpty()
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.count { it.isSelected },
                    price = filteredAvailableProducts.filter { it.isSelected }.sumOf { it.priceInt }
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )
    }

    @Test
    fun `When processing to load layout, recommendation is empty here, mini cart data is null because gql retrieval response is failed, just shopping list (available and unavailable) is not empty, intersection is done between minicart and available list to get data for cart widget, the result should not contain cart widget because the intersection result is empty`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = false,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify cart product existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListCartProductUiModel>().isEmpty()
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.count { it.isSelected },
                    price = filteredAvailableProducts.filter { it.isSelected }.sumOf { it.priceInt }
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )
    }

    @Test
    fun `When processing to load layout, product recommendation widget response doesn't have next page, the result for product recommendation should not have load more`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            hasNext = false,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify loading more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<LoadingMoreModel>().isEmpty()
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.count { it.isSelected },
                    price = filteredAvailableProducts.filter { it.isSelected }.sumOf { it.priceInt }
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )
    }

    @Test
    fun `When processing to load layout, product recommendation widget response has next page, the result for product recommendation should have load more`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            hasNext = true,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify loading more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isNotEmpty()
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.count { it.isSelected },
                    price = filteredAvailableProducts.filter { it.isSelected }.sumOf { it.priceInt }
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )
    }

    @Test
    fun `When processing to load layout, failed to get product recommendation widget, the result should not show product recommendation instead showing local load`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = true,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify loading more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<TokoNowLocalLoadUiModel>().isNotEmpty()
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.count { it.isSelected },
                    price = filteredAvailableProducts.filter { it.isSelected }.sumOf { it.priceInt }
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )
    }
}
