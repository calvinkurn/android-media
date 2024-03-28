package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartMultiUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.helper.ResourceProvider
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.util.AddressMapper.mapToWarehousesData
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.resetIndices
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addEmptyShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addExpandCollapse
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLocalLoad
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProductCartItem
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProductCarts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addTopCheckAllShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.filteredBy
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.getExpandCollapse
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductShoppingListMapper
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.GetShoppingListUseCase
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.SaveShoppingListStateUseCase
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.RecommendationModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.StateFlow
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

abstract class TokoNowShoppingListViewModelFixture {

    protected lateinit var viewModel: TokoNowShoppingListViewModel

    protected val mutableLayout = mutableListOf<Visitable<*>>()

    protected val filteredAvailableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    protected val filteredUnavailableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    protected val filteredRecommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    protected val availableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    protected val unavailableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()

    private val recommendedProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val cartProducts = mutableListOf<ShoppingListCartProductItemUiModel>()

    private var mMiniCartData: MiniCartSimplifiedData? = null
    private var isFirstPageProductRecommendationError = false
    private var recommendationModel: RecommendationModel = RecommendationModel()

    @RelaxedMockK
    lateinit var addressData: TokoNowLocalAddress

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var addToWishlistUseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var productRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var getShoppingListUseCase: GetShoppingListUseCase

    @RelaxedMockK
    lateinit var saveShoppingListStateUseCase: SaveShoppingListStateUseCase

    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase

    @RelaxedMockK
    lateinit var deleteFromWishlistUseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var addToCartMultiUseCase: AddToCartMultiUseCase

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowShoppingListViewModel(
            addressData,
            userSession,
            productRecommendationUseCase,
            getShoppingListUseCase,
            saveShoppingListStateUseCase,
            getMiniCartUseCase,
            addToWishlistUseCase,
            deleteFromWishlistUseCase,
            addToCartMultiUseCase,
            resourceProvider,
            CoroutineTestDispatchersProvider
        )
    }

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

    protected fun StateFlow<UiState<*>>.verifySuccess(expectedResult: Any) {
        Assert.assertEquals(expectedResult, (value as UiState.Success).data)
    }

    protected fun StateFlow<UiState<*>>.verifyError(throwable: Throwable, expectedResult: Any) {
        Assert.assertEquals(throwable, (value as UiState.Error).throwable)
        Assert.assertEquals(expectedResult, (value as UiState.Error).data)
    }

    protected fun StateFlow<UiState<*>>.verifyIsError() {
        Assert.assertTrue(value is UiState.Error)
    }

    protected fun StateFlow<Any?>.verifyValue(expectedResult: Any?) {
        Assert.assertEquals(expectedResult, value)
    }

    protected fun verifyIsTrue(expectedResult: Boolean) {
        Assert.assertTrue(expectedResult)
    }

    protected fun stubOutOfCoverage(
        isOoc: Boolean
    ) {
        every {
            addressData.isOutOfCoverage()
        } returns isOoc
    }

    protected fun stubShopId(
        shopId: Long
    ) {
        every {
            addressData.getShopId()
        } returns shopId
    }

    protected fun stubLoggedIn(
        isLoggedIn: Boolean
    ) {
        every {
            userSession.isLoggedIn
        } returns isLoggedIn
    }

    protected fun stubGetMiniCart(
        response: MiniCartSimplifiedData
    ) {
        coEvery {
            getMiniCartUseCase.setParams(
                shopIds = listOf(addressData.getShopId().toString()),
                source = MiniCartSource.TokonowShoppingList
            )
            getMiniCartUseCase.executeOnBackground()
        } returns response
    }

    protected fun stubGetMiniCart(
        throwable: Throwable
    ) {
        coEvery {
            getMiniCartUseCase.setParams(
                shopIds = listOf(addressData.getShopId().toString()),
                source = MiniCartSource.TokonowShoppingList
            )
            getMiniCartUseCase.executeOnBackground()
        } throws throwable
    }

    protected fun stubGetShoppingList(
        response: GetShoppingListDataResponse.Data
    ) {
        coEvery {
            val warehouses = mapToWarehousesData(addressData.getAddressData())
            getShoppingListUseCase.execute(warehouses)
        } returns response
    }

    protected fun stubGetShoppingList(
        throwable: Throwable
    ) {
        coEvery {
            val warehouses = mapToWarehousesData(addressData.getAddressData())
            getShoppingListUseCase.execute(warehouses)
        } throws throwable
    }

    protected fun stubGetProductRecommendation(
        param: GetRecommendationRequestParam,
        response: RecommendationWidget
    ) {
        coEvery {
            productRecommendationUseCase.getData(param)
        } returns response
    }

    protected fun stubGetProductRecommendation(
        param: GetRecommendationRequestParam,
        throwable: Throwable
    ) {
        coEvery {
            productRecommendationUseCase.getData(param)
        } throws throwable
    }

    protected fun stubSaveShoppingListState(
        actionList: List<SaveShoppingListStateActionParam>
    ) {
        coEvery {
            saveShoppingListStateUseCase.execute(actionList)
        } returns Unit
    }

    protected fun stubSaveShoppingListState(
        actionList: List<SaveShoppingListStateActionParam>,
        throwable: Throwable
    ) {
        coEvery {
            saveShoppingListStateUseCase.execute(actionList)
        } throws throwable
    }

    protected fun verifyProductRecommendationUseCase(
        param: GetRecommendationRequestParam
    ) {
        coVerify {
            productRecommendationUseCase.getData(param)
        }
    }

    protected fun verifyAddToWishlistUseCase(
        productId: String,
        userId: String
    ) {
        coVerify {
            addToWishlistUseCase.setParams(productId, userId)
            addToWishlistUseCase.executeOnBackground()
        }
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
            state = TokoNowLayoutState.SHOW
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

    protected fun updateLayout() {
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

    protected fun setDataToGlobalVariables(
        miniCartData: MiniCartSimplifiedData?,
        shoppingList: GetShoppingListDataResponse.Data?,
        recommendationWidget: RecommendationWidget,
        shopId: Long,
        isProductRecommendationError: Boolean = false
    ) {
        mMiniCartData = if (shopId != TokoNowShoppingListViewModel.INVALID_SHOP_ID) miniCartData else null

        if (shoppingList != null) {
            availableProducts.addAll(ProductShoppingListMapper.mapAvailableShoppingList(shoppingList.listAvailableItem))
            unavailableProducts.addAll(
                ProductShoppingListMapper.mapUnavailableShoppingList(
                    shoppingList.listUnavailableItem
                )
            )
        }

        recommendationModel = RecommendationModel(
            pageCounter = if (recommendationWidget.hasNext) Int.ONE else Int.ZERO,
            hasNext = recommendationWidget.hasNext,
            title = recommendationWidget.title
        )
        recommendedProducts.addAll(
            ProductRecommendationMapper.mapRecommendedProducts(
                recommendationWidget
            )
        )

        isFirstPageProductRecommendationError = isProductRecommendationError
    }

    protected fun loadLayoutWithExpandCollapseWidget() {
        // create fake variables for stub
        val shopId = 222121L
        val miniCartData = ShoppingListDataFactory.Main.createMiniCart()
        val shoppingList = ShoppingListDataFactory.Main.createShoppingList()
        val newShoppingList = shoppingList.copy(
            listAvailableItem = shoppingList.listAvailableItem + shoppingList.listAvailableItem,
            listUnavailableItem = shoppingList.listUnavailableItem + shoppingList.listUnavailableItem
        )
        val recommendationWidget = ShoppingListDataFactory.Main.createRecommendationWidget(
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
            response = newShoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = newShoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

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

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                true
            )

        viewModel
            .isNavToolbarScrollingBehaviourEnabled
            .verifyValue(
                true
            )
    }
}
