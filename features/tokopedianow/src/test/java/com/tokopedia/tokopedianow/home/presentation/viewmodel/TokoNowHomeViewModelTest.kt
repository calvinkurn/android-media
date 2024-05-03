package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowMediumWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowSmallWidget
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemSeeAllUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.data.BuyerCommunicationDataFactory.createBuyerCommunicationResponse
import com.tokopedia.tokopedianow.data.ClaimCouponDataFactory.createCatalogCouponList
import com.tokopedia.tokopedianow.data.ClaimCouponDataFactory.createChannelLayout
import com.tokopedia.tokopedianow.data.ClaimCouponDataFactory.createLayoutListUiModel
import com.tokopedia.tokopedianow.data.ClaimCouponDataFactory.createRedeemCoupon
import com.tokopedia.tokopedianow.data.createCategoryGridDataModel
import com.tokopedia.tokopedianow.data.createCategoryGridListFirstFetch
import com.tokopedia.tokopedianow.data.createCategoryGridListSecondFetch
import com.tokopedia.tokopedianow.data.createCategoryGridWithAdultDataFetch
import com.tokopedia.tokopedianow.data.createCategoryList
import com.tokopedia.tokopedianow.data.createChooseAddress
import com.tokopedia.tokopedianow.data.createDynamicChannelLayoutList
import com.tokopedia.tokopedianow.data.createDynamicLegoBannerDataModel
import com.tokopedia.tokopedianow.data.createEmptyState
import com.tokopedia.tokopedianow.data.createHomeLayoutList
import com.tokopedia.tokopedianow.data.createHomeLayoutListForQuestOnly
import com.tokopedia.tokopedianow.data.createHomeProductCardUiModel
import com.tokopedia.tokopedianow.data.createKeywordSearch
import com.tokopedia.tokopedianow.data.createLeftCarouselAtcDataModel
import com.tokopedia.tokopedianow.data.createLoadingState
import com.tokopedia.tokopedianow.data.createMiniCartSimplifier
import com.tokopedia.tokopedianow.data.createPlayWidgetChannel
import com.tokopedia.tokopedianow.data.createPlayWidgetUiModel
import com.tokopedia.tokopedianow.data.createTicker
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOMEPAGE_TOKONOW
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.mapToClaimCouponDataModel
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.domain.model.ReferralEvaluateJoinResponse
import com.tokopedia.tokopedianow.home.mapper.HomeHeaderMapper
import com.tokopedia.tokopedianow.home.mapper.HomeHeaderMapper.createHomeHeaderUiModel
import com.tokopedia.tokopedianow.home.mapper.PlayWidgetMapper.createPlayWidgetState
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.model.HomeClaimCouponDataModel
import com.tokopedia.tokopedianow.home.presentation.model.HomeReferralDataModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.Companion.COUPON_STATUS_CLAIM
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.Companion.COUPON_STATUS_CLAIMED
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.Companion.COUPON_STATUS_LOGIN
import com.tokopedia.tokopedianow.util.AddressMapperTestUtils.mapToWarehousesData
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTest : TokoNowHomeViewModelTestFixture() {

    @Test
    fun `when tracking with setting screenName should give the same result`() {
        viewModel.trackOpeningScreen(HOMEPAGE_TOKONOW)
        verifyTrackOpeningScreen()
    }

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        val warehouses = listOf(
            WarehouseData("12556", "hub")
        )
        val homeLayoutListResponse = createHomeLayoutList()
        val categoryListResponse = createCategoryList(emptyList())
        val buyerCommunicationResponse = createBuyerCommunicationResponse()

        onGetHomeLayoutData_thenReturn(homeLayoutListResponse)
        onGetCategoryList_thenReturn(categoryListResponse)
        onGetBuyerCommunication_thenReturn(buyerCommunicationResponse)
        onGetWarehouseData_thenReturn(warehouses)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = LocalCacheModel()
        )

        val homeHeaderUiModel = HomeHeaderMapper
            .mapToHomeHeaderUiModel(warehouses, buyerCommunicationResponse)

        val expectedHomeLayoutList = HomeLayoutListUiModel(
            items = listOf(
                homeHeaderUiModel,
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetHomeLayoutListSuccess(expectedHomeLayoutList)
    }

    @Test
    fun `given homeLayoutList null when getLayoutComponentData should update live data FAIL`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryList(emptyList()))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )

        onGetHomeLayoutItemList_returnNull()

        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        viewModel.homeLayoutList
            .verifyErrorEquals(Fail(NullPointerException()))
    }

    @Test
    fun `when getHomeLayout two times should call use case twice`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `when getting loadingState should run and give the success result`() {
        viewModel.getLoadingState()

        val expectedLayoutList = createLoadingState()

        verifyGetHomeLayoutListSuccess(expectedLayoutList)
        assertEquals(viewModel.isEmptyState, false)
    }

    @Test
    fun `when getting emptyState should run and give the success result`() {
        val serviceType = "2h"
        val idEmptyState = EMPTY_STATE_OUT_OF_COVERAGE

        viewModel.getEmptyState(idEmptyState, serviceType)

        val expectedLayoutList = createEmptyState(idEmptyState, serviceType)

        verifyGetHomeLayoutListSuccess(expectedLayoutList)
        assertEquals(viewModel.isEmptyState, true)
    }

    @Test
    fun `when getEmptyState error should set live data value FAIL`() {
        val serviceType = "2h"
        val idEmptyState = EMPTY_STATE_OUT_OF_COVERAGE

        onGetHomeLayoutItemList_returnNull()

        viewModel.getEmptyState(idEmptyState, serviceType)

        viewModel.homeLayoutList
            .verifyErrorEquals(Fail(NullPointerException()))
    }

    @Test
    fun `when getting keywordSearch should run and give the success result`() {
        onGetKeywordSearch_thenReturn(createKeywordSearch())

        viewModel.getKeywordSearch(anyBoolean(), anyString(), anyString())

        verifyGetKeywordSearchUseCaseCalled()

        val expectedResponse = createKeywordSearch()
        verifyKeywordSearchResponseSuccess(expectedResponse.searchData)
    }

    @Test
    fun `given get keyword error when getKeywordSearch should NOT set keywordSearch value`() {
        onGetKeywordSearch_thenReturn(NullPointerException())

        viewModel.getKeywordSearch(anyBoolean(), anyString(), anyString())

        verifyGetKeywordSearchUseCaseCalled()

        viewModel.keywordSearch
            .verifyValueEquals(null)
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayoutData_thenReturn(Exception())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Exception())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddressFail()
    }

    @Test
    fun `when getting data for mini cart should run and give success result`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        verifyMiniCartResponseSuccess(createMiniCartSimplifier())
    }

    @Test
    fun `when getting data for mini cart should throw mini cart exception`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(Exception())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        verifyMiniCartFail()
    }

    @Test
    fun `when getMiniCart throw exception should set miniCart value fail`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_throwException(Exception())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        viewModel.miniCart
            .verifyErrorEquals(Fail(Exception()))
    }

    @Test
    fun `when getMiniCart twice should run and give success result`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")
        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        verifyMiniCartResponseSuccess(createMiniCartSimplifier())
    }

    @Test
    fun `when shopId is empty should get null for category livedata`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf(), warehouseId = "233")

        verifyMiniCartNullResponse()
    }

    @Test
    fun `when warehouseId is zero should get null for category livedata`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "0")

        verifyMiniCartNullResponse()
    }

    @Test
    fun `given user is not logged in when getMiniCart should not call use case`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = false)

        viewModel.getMiniCart(listOf("123"), "1")

        verifyGetMiniCartUseCaseNotCalled()
        verifyMiniCartNullResponse()
    }

    @Test
    fun `when getting data category grid should run and give the success result`() {
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val warehouses = mapToWarehousesData(localCacheModel)

        // set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), localCacheModel)
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        // set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(createCategoryGridListSecondFetch())

        viewModel.getCategoryMenu(localCacheModel)

        // prepare model for expectedResult
        val expectedCategoryItem = TokoNowCategoryMenuUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryListUiModel = listOf(
                TokoNowCategoryMenuItemUiModel(
                    id = "1",
                    title = "Category 1",
                    imageUrl = "tokopedia://",
                    appLink = "tokoepdia://",
                    headerName = "Category Tokonow",
                    color = "#FFFFFF"
                ),
                TokoNowCategoryMenuItemSeeAllUiModel(
                    appLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY
                )
            ),
            seeAllAppLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY,
            state = TokoNowLayoutState.SHOW
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCategoryListUseCaseCalled(warehouses = warehouses, count = 2)
        verifyCategoryMenuItem(expectedCategoryItem)
    }

    @Test
    fun `when get category grid should not add adult category to category list`() {
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val warehouses = mapToWarehousesData(localCacheModel)
        val appLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY

        // set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), localCacheModel)
        onGetCategoryList_thenReturn(createCategoryGridWithAdultDataFetch())

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        viewModel.getCategoryMenu(localCacheModel)

        // prepare model for expectedResult
        val expectedCategoryItem = TokoNowCategoryMenuUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryListUiModel = listOf(
                TokoNowCategoryMenuItemUiModel(
                    id = "1",
                    title = "Category 1",
                    imageUrl = "tokopedia://",
                    appLink = "tokoepdia://",
                    headerName = "Category Tokonow",
                    color = "#FFFFFF"
                ),
                TokoNowCategoryMenuItemUiModel(
                    id = "3",
                    title = "Category 3",
                    imageUrl = "tokopedia://",
                    appLink = "tokoepdia://",
                    headerName = "Category Tokonow",
                    color = "#FFFFFF"
                ),
                TokoNowCategoryMenuItemSeeAllUiModel(
                    appLink = appLink
                )
            ),
            seeAllAppLink = appLink,
            state = TokoNowLayoutState.SHOW
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCategoryListUseCaseCalled(warehouses = warehouses, count = 2)
        verifyCategoryMenuItem(expectedCategoryItem)
    }

    @Test
    fun `when getting data category grid should run, throw exception and give the success result`() {
        val localCacheModel = LocalCacheModel()
        val warehouses = mapToWarehousesData(localCacheModel)

        // set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )

        // fetch widget one by one
        viewModel.onScroll(1, localCacheModel, listOf())

        // set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(Exception())

        viewModel.getCategoryMenu(localCacheModel)

        // prepare model for expectedResult
        val expectedCategoryItem = TokoNowCategoryMenuUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryListUiModel = null,
            state = TokoNowLayoutState.HIDE
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetCategoryListUseCaseCalled(warehouses = warehouses)
        verifyCategoryMenuItem(expectedCategoryItem)
    }

    @Test
    fun `when get category grid data error should map data with null category list`() {
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val warehouses = mapToWarehousesData(localCacheModel)

        onGetHomeLayoutData_thenReturn(
            layoutResponse = createHomeLayoutList(),
            localCacheModel = localCacheModel
        )
        onGetCategoryList_thenReturn(Exception())

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val data = HomeLayoutListUiModel(
            items = listOf(
                createHomeHeaderUiModel(),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                TokoNowCategoryMenuUiModel(
                    id = "11111",
                    title = "Category Tokonow",
                    categoryListUiModel = null,
                    state = TokoNowLayoutState.HIDE
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )
        val expectedResult = Success(data)

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCategoryListUseCaseCalled(warehouses = warehouses)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getHomeLayout should add all dynamic channel to home layout list`() {
        val channelId = "2322"
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Product Recommendation",
            warehouseId = warehouseId
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = createDynamicChannelLayoutList(),
            localCacheModel = localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )

        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        val data = HomeLayoutListUiModel(
            items = listOf(
                createHomeHeaderUiModel(),
                createDynamicLegoBannerDataModel(
                    id = "34923",
                    groupId = "",
                    headerName = "Lego Banner",
                    layout = "lego_3_image"
                ),
                createDynamicLegoBannerDataModel(
                    id = "11111",
                    groupId = "",
                    headerName = "Lego 6",
                    layout = "6_image"
                ),
                HomeProductRecomUiModel(
                    id = "2322",
                    title = "Product Recommendation",
                    productList = listOf(),
                    realTimeRecom = realTimeRecom,
                    seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
                        id = channelId,
                        headerName = "Product Recommendation"
                    ),
                    headerModel = TokoNowDynamicHeaderUiModel(
                        title = "Product Recommendation"
                    )
                ),
                createLeftCarouselAtcDataModel(
                    id = "2122",
                    headerName = "Mix Left Atc Carousel",
                    warehouseId = "1"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )
        val expectedResult = Success(data)

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when removeLeftCarouselAtc should remove left carousel atc widget from layout list`() {
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val dynamicChannelResponse = createDynamicChannelLayoutList(
            listOf(
                HomeLayoutResponse(
                    id = "2122",
                    layout = "left_carousel_atc",
                    header = Header(
                        name = "Mix Left Atc Carousel",
                        serverTimeUnix = 0
                    )
                )
            )
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = dynamicChannelResponse,
            localCacheModel = localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )

        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        viewModel.removeLeftCarouselAtc("2122")

        val expectedResultWithoutLeftCarouselAtcWidget = Success(
            HomeLayoutListUiModel(
                items = listOf(
                    createHomeHeaderUiModel()
                ),
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        viewModel.homeLayoutList.verifySuccessEquals(expectedResultWithoutLeftCarouselAtcWidget)
    }

    @Test
    fun `when removeLeftCarouselAtc throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.removeLeftCarouselAtc("1")

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `given getRepurchaseProduct error when getHomeLayout should NOT add repurchase widget`() {
        val error = NullPointerException()

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(error)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given getRepurchaseProduct error when getHomeLayout should NOT add old repurchase widget`() {
        val error = NullPointerException()

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(error)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given getRepurchaseProduct empty when getHomeLayout should NOT add repurchase widget`() {
        val repurchaseProductResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf()
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseProductResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given getRepurchaseProduct empty when getHomeLayout should NOT add old repurchase widget`() {
        val repurchaseProductResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf()
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseProductResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given miniCartSimplifiedData is NULL when getMiniCartItem should return MiniCartItemProduct`() {
        val productId = "1"

        val expectedResult = MiniCartItem.MiniCartItemProduct()
        val actualResult = viewModel.getMiniCartItem(productId)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getMiniCartItem should return mini cart item by ID`() {
        val productId = "1"

        val miniCartItem = MiniCartItem.MiniCartItemProduct(productId = productId)
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = mapOf(MiniCartItemKey(productId) to miniCartItem))

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "2")

        val expectedResult = MiniCartItem.MiniCartItemProduct(productId = productId)
        val actualResult = viewModel.getMiniCartItem(productId)

        verifyGetMiniCartUseCaseCalled()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given productId not match when getMiniCartItem should return MiniCartItemProduct`() {
        val productId = "3"

        val miniCartItem = MiniCartItem.MiniCartItemProduct(productId = "1")
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = mapOf(MiniCartItemKey("1") to miniCartItem))

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "2")

        val expectedResult = MiniCartItem.MiniCartItemProduct()
        val actualResult = viewModel.getMiniCartItem(productId)

        verifyGetMiniCartUseCaseCalled()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given homeLayoutItemList contains repurchase when getRepurchaseWidgetProducts should product list`() {
        val channelId = "1001"
        val productId = "1"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(repurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())

        val expected = listOf(
            createHomeProductCardUiModel(
                channelId = channelId,
                productId = productId,
                quantity = 0,
                stock = 5,
                minOrder = 3,
                maxOrder = 4,
                position = 1,
                originalPosition = 1,
                headerName = "Kamu pernah beli"
            )
        )
        val actual = viewModel.getRepurchaseProducts()

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        assertEquals(expected, actual)
    }

    @Test
    fun `given homeLayoutItemList does NOT contain repurchase when getRepurchaseWidgetProducts should return empty list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val expected = emptyList<TokoNowRepurchaseProductUiModel>()
        val actual = viewModel.getRepurchaseProducts()

        verifyGetHomeLayoutDataUseCaseCalled()
        assertEquals(expected, actual)
    }

    @Test
    fun `given null homeLayoutItemList when getRepurchaseWidgetProducts should return empty list`() {
        addHomeLayoutItem(null)

        val expected = emptyList<TokoNowRepurchaseProductUiModel>()
        val actual = viewModel.getRepurchaseProducts()

        assertEquals(expected, actual)
    }

    @Test
    fun `given homeLayoutItemList is empty when getRepurchaseWidgetProducts should return empty list`() {
        val expected = emptyList<TokoNowRepurchaseProductUiModel>()
        val actual = viewModel.getRepurchaseProducts()

        assertEquals(expected, actual)
    }

    @Test
    fun `when get home recom success should add recom widget to home layout list`() {
        val channelId = "1001"
        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "2"))
        )
        val homeLayoutResponse = listOf(homeRecomResponse)
        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )

        viewModel.getLayoutComponentData(
            localCacheModel = LocalCacheModel()
        )

        val productList = listOf(
            ProductCardCompactCarouselItemUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon",
                productCardModel = ProductCardCompactUiModel(
                    productId = "2",
                    usePreDraw = true,
                    price = "0",
                    needToShowQuantityEditor = true
                ),
                shopType = "pm"
            )
        )

        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Lagi Diskon"
        )

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            title = "Lagi Diskon",
            productList = productList,
            realTimeRecom = realTimeRecom,
            seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
                id = channelId,
                headerName = "Lagi Diskon"
            ),
            headerModel = TokoNowDynamicHeaderUiModel(
                title = "Lagi Diskon"
            )
        )

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.homeLayoutList.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get repurchase success should add repurchase to home layout list`() {
        val channelId = "1001"
        val productId = "1"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(repurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())

        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    channelId = channelId,
                    productId = productId,
                    quantity = 0,
                    stock = 5,
                    minOrder = 3,
                    maxOrder = 4,
                    position = 1,
                    originalPosition = 1,
                    headerName = "Kamu pernah beli"
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(),
            repurchaseUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when removeSharingEducationWidget should remove education widget from home layout list`() {
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_usp",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.removeWidget(channelId)

        val items = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given user has repurchase product when getHomeLayout should add sharing education widget to home layout list`() {
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = "1", stock = 5, maxOrder = 4, minOrder = 3))
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val items = listOf(
            createHomeHeaderUiModel(),
            HomeSharingEducationWidgetUiModel(
                id = channelId,
                HomeLayoutItemState.LOADED,
                R.string.tokopedianow_home_sharing_education_button,
                ""
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given hasSharingEducationBeenRemoved true when getHomeLayout should NOT add sharing education`() {
        val hasSharingEducationBeenRemoved = true
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = "1", stock = 5, maxOrder = 4, minOrder = 3))
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = mutableListOf(
                HomeRemoveAbleWidget(TokoNowLayoutType.SHARING_EDUCATION, hasSharingEducationBeenRemoved)
            )
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val items = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseNotCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given repurchase product empty when getHomeLayout should NOT add sharing education widget`() {
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = emptyList()
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val items = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given unknown layout when getHomeLayout should not call other use case`() {
        val layout = "unknown"

        val firstHomeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "34923",
                layout = layout,
                header = Header(
                    name = "Lego Banner",
                    serverTimeUnix = 0
                ),
                token = "==sfvf" // dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(firstHomeLayoutResponse)

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.NOT_LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled()

        // Other Use Case
        verifyGetCategoryListUseCaseNotCalled()
        verifyGetRepurchaseWidgetUseCaseNotCalled()
    }

    @Test
    fun `when setProductAddToCartQuantity throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.setProductAddToCartQuantity(MiniCartSimplifiedData())

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `when getHomeLayoutJob is active should not set homeLayoutList value`() {
        mockGetHomeLayoutJobActive()

        viewModel.setProductAddToCartQuantity(MiniCartSimplifiedData())

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `when removeSharingEducationWidget throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.removeWidget("1")

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `given unknown layout when getLayoutComponentData should not call any use case`() {
        val unknownLayout = HomeLayoutItemUiModel(
            UnknownLayout,
            HomeLayoutItemState.NOT_LOADED
        )
        val unknownHomeLayout = HomeLayoutItemUiModel(
            UnknownHomeLayout,
            HomeLayoutItemState.NOT_LOADED
        )
        val nullHomeLayout = HomeLayoutItemUiModel(
            null,
            HomeLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)
        addHomeLayoutItem(unknownHomeLayout)
        addHomeLayoutItem(nullHomeLayout)

        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetCategoryListUseCaseNotCalled()
        verifyGetRepurchaseWidgetUseCaseNotCalled()
        verifyGetMiniCartUseCaseNotCalled()

        val expected = Success(
            HomeLayoutListUiModel(
                items = emptyList(),
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifyValueEquals(expected)
    }

    @Test
    fun `given null layout when removeTickerWidget should filter null layout from list`() {
        val nullLayout = HomeLayoutItemUiModel(
            layout = null,
            state = HomeLayoutItemState.NOT_LOADED
        )

        onGetTicker_thenReturn(createTicker())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        addHomeLayoutItem(nullLayout)

        viewModel.removeWidget(id = "1")

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = listOf(
                    createHomeHeaderUiModel(),
                    createDynamicLegoBannerDataModel(
                        "34923",
                        "",
                        "Lego Banner"
                    ),
                    createCategoryGridDataModel(
                        "11111",
                        "Category Tokonow",
                        null,
                        TokoNowLayoutState.HIDE
                    )
                ),
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given eligible for NOW15 when getHomeLayout should add NOW15 switcher widget to layout list`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        onGetHomeLayoutData_thenReturn(
            listOf(
                HomeLayoutResponse(
                    id = "2",
                    layout = "tokonow_usp",
                    header = Header(
                        name = "Tokonow USP",
                        serverTimeUnix = 0
                    ),
                    token = ""
                )
            ),
            localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            createHomeHeaderUiModel(),
            HomeEducationalInformationWidgetUiModel(
                "2",
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given NOT eligible for NOW15 when getHomeLayout should NOT add NOW15 switcher widget to layout list`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 0, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetHomeLayoutData_thenReturn(
            listOf(
                HomeLayoutResponse(
                    id = "2",
                    layout = "tokonow_usp",
                    header = Header(
                        name = "Tokonow USP",
                        serverTimeUnix = 0
                    ),
                    token = ""
                )
            ),
            localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            createHomeHeaderUiModel(),
            HomeEducationalInformationWidgetUiModel(
                "2",
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given EMPTY warehouses when getHomeLayout should NOT add home switcher widget to layout list`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            warehouses = emptyList(),
            service_type = serviceType
        )

        onGetHomeLayoutData_thenReturn(
            listOf(
                HomeLayoutResponse(
                    id = "2",
                    layout = "tokonow_usp",
                    header = Header(
                        name = "Tokonow USP",
                        serverTimeUnix = 0
                    ),
                    token = ""
                )
            ),
            localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            createHomeHeaderUiModel(),
            HomeEducationalInformationWidgetUiModel(
                "2",
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given user NOT logged in when getHomeLayout should NOT add switcher widget to layout list`() {
        val serviceType = "2h"
        val userLoggedIn = false

        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetIsUserLoggedIn_thenReturn(userLoggedIn)

        onGetHomeLayoutData_thenReturn(
            listOf(
                HomeLayoutResponse(
                    id = "2",
                    layout = "tokonow_usp",
                    header = Header(
                        name = "Tokonow USP",
                        serverTimeUnix = 0
                    ),
                    token = ""
                )
            ),
            localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            createHomeHeaderUiModel(),
            HomeEducationalInformationWidgetUiModel(
                "2",
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get referral sender home success with code equals to 200 should return sharingUrlParam`() {
        val id = "155"
        val slug = "slug"
        val ogImage = "https://tkpd.com/image.png"
        val ogTitle = "Title"
        val ogDescription = "Desc"
        val textDescription = "Text desc"
        val sharingUrlParam = "123"
        val maxReward = "Rp 123.000"
        val userStatus = "100"
        val isSender = true
        val warehouseId = "15021"
        val serviceType = "2h"
        val titleSection = "Tokonow Referral"

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId,
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        val layoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "tokonow_referral",
                header = Header(
                    name = "Tokonow Referral",
                    serverTimeUnix = 0
                ),
                widgetParam = slug,
                token = ""
            )
        )

        val referral = HomeReferralDataModel(
            ogImage = ogImage,
            ogTitle = ogTitle,
            ogDescription = ogDescription,
            textDescription = textDescription,
            sharingUrlParam = sharingUrlParam,
            userStatus = userStatus,
            maxReward = maxReward,
            isSender = isSender,
            isEligible = true
        )

        val homeSharingWidgetUiModel = HomeSharingReferralWidgetUiModel(
            id = id,
            state = HomeLayoutItemState.LOADED,
            slug = slug,
            ogImage = ogImage,
            ogTitle = ogTitle,
            ogDescription = ogDescription,
            textDescription = textDescription,
            sharingUrlParam = sharingUrlParam,
            userStatus = userStatus,
            maxReward = maxReward,
            isSender = isSender,
            warehouseId = warehouseId,
            titleSection = titleSection
        )

        onGetHomeLayoutData_thenReturn(layoutResponse, localCacheModel)
        onGetReferralSenderHome_thenReturn(slug, referral)

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(
            createHomeHeaderUiModel(),
            homeSharingWidgetUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetReferralSenderHomeUseCaseCalled(slug)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.getReferralResult
            .verifySuccessEquals(Success(referral))
    }

    @Test
    fun `when get referral not eligible should not add referral widget to home layout list`() {
        val id = "155"
        val slug = "slug"
        val isEligible = false
        val localCacheModel = LocalCacheModel()

        val layoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "tokonow_referral",
                header = Header(
                    name = "Tokonow Referral",
                    serverTimeUnix = 0
                ),
                widgetParam = slug,
                token = ""
            )
        )

        val referral = HomeReferralDataModel(isEligible = isEligible)

        onGetHomeLayoutData_thenReturn(layoutResponse, localCacheModel)
        onGetReferralSenderHome_thenReturn(slug, referral)

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(createHomeHeaderUiModel())
        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetReferralSenderHomeUseCaseCalled(slug)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.getReferralResult
            .verifyValueEquals(null)
    }

    @Test
    fun `when get referral sender home error should not add referral widget to home layout list`() {
        val id = "155"
        val slug = "slug"
        val messageError = "this is an error"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "tokonow_referral",
                header = Header(
                    name = "Tokonow Referral",
                    serverTimeUnix = 0
                ),
                widgetParam = slug,
                token = ""
            )
        )
        val localCacheModel = LocalCacheModel()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse, localCacheModel)
        onGetReferralSenderHome_thenReturn(slug, MessageErrorException(messageError))

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(createHomeHeaderUiModel())
        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetReferralSenderHomeUseCaseCalled(slug)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.getReferralResult
            .verifyErrorEquals(Fail(MessageErrorException(messageError)))
    }

    @Test
    fun `when get medium play widget success should add medium play widget to home layout list`() {
        val id = "1001"
        val title = "Medium Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )
        val playWidgetState = createPlayWidgetState()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val widgetModel = playWidgetState.model.copy(title = title, actionAppLink = appLink)
        val widgetState = playWidgetState.copy(model = widgetModel)

        val playWidgetUiModel = HomePlayWidgetUiModel(
            id = id,
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = widgetState,
            isAutoRefresh = false
        )

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(),
            playWidgetUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.invalidatePlayImpression
            .verifyValueEquals(true)
    }

    @Test
    fun `when get small play widget success should add small play widget to home layout list`() {
        val id = "1001"
        val title = "Small Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel_small",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )
        val playWidgetState = createPlayWidgetState()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val widgetModel = playWidgetState.model.copy(title = title, actionAppLink = appLink)
        val widgetState = playWidgetState.copy(model = widgetModel)

        val playWidgetUiModel = HomePlayWidgetUiModel(
            id = id,
            widgetType = TokoNowSmallWidget(channelTag),
            playWidgetState = widgetState,
            isAutoRefresh = false
        )

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(),
            playWidgetUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.invalidatePlayImpression
            .verifyValueEquals(true)
    }

    @Test
    fun `given small play widget items empty when get small play widget success should NOT add small play widget to home layout list`() {
        val id = "1001"
        val title = "Small Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"
        val playWidgetItems = emptyList<PlayWidgetChannelUiModel>()

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel_small",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )
        val playWidgetState = createPlayWidgetState(items = playWidgetItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel()
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.invalidatePlayImpression
            .verifyValueEquals(true)
    }

    @Test
    fun `when get play widget error should remove play widget from home layout list`() {
        val id = "1001"
        val title = "Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(Exception())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(createHomeHeaderUiModel())

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when auto refresh play widget should map play widget isAutoRefresh true`() {
        val id = "1200"
        val title = "Auto Refresh Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val impressHolder = object : ImpressionableModel {
            override val impressHolder: ImpressHolder = ImpressHolder()
        }

        val playWidgetState = PlayWidgetState(
            model = createPlayWidgetUiModel(
                title = title,
                appLink = appLink
            ),
            impressHolder = impressHolder
        )

        val playWidgetUiModel = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = false
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        val widgetState = PlayWidgetState(
            model = createPlayWidgetUiModel(),
            impressHolder = impressHolder
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(widgetState)

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.autoRefreshPlayWidget(playWidgetUiModel)

        val homePlayWidgetUiModel = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = true
        )

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(state = HomeLayoutItemState.LOADING),
            homePlayWidgetUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when update play widget should map total view to play widget`() {
        val id = "1200"
        val channelId = "1"
        val totalView = "30rb"
        val title = "Update Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"
        val playWidgetState = PlayWidgetState(
            model = createPlayWidgetUiModel(
                title = title,
                appLink = appLink,
                items = listOf(
                    PlayWidgetBannerUiModel(appLink = "", imageUrl = ""),
                    createPlayWidgetChannel(channelId = channelId, totalView = "10rb"),
                    createPlayWidgetChannel(channelId = "2", totalView = "20rb")
                )
            )
        )

        val widget = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = false
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.updatePlayWidget(channelId, totalView)

        val widgetModel = playWidgetState.model.copy(
            items = listOf(
                PlayWidgetBannerUiModel(appLink = "", imageUrl = ""),
                createPlayWidgetChannel(channelId = channelId, totalView = totalView),
                createPlayWidgetChannel(channelId = "2", totalView = "20rb")
            )
        )
        val widgetState = playWidgetState.copy(model = widgetModel)

        val playWidgetUiModel = widget.copy(
            playWidgetState = widgetState,
            isAutoRefresh = false
        )

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(),
            playWidgetUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when update play widget error should do nothing`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.updatePlayWidget("1", "20rb")

        viewModel.homeLayoutList.verifyValueEquals(null)
    }

    @Test
    fun `when auto refresh play widget error should remove widget from home layout list`() {
        val id = "1200"
        val title = "Auto Refresh Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"
        val playWidgetState = createPlayWidgetState()

        val widget = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowSmallWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = false
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(Exception())

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.autoRefreshPlayWidget(widget)

        val homeLayoutItems = listOf(
            createHomeHeaderUiModel(state = HomeLayoutItemState.LOADING)
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get evaluate join return success response`() {
        // Given
        val response = ReferralEvaluateJoinResponse(
            ReferralEvaluateJoinResponse.GamiReferralEvaluateJoinResponse()
        )
        val expectedResult = Success(response.gamiReferralEvaluteJoinResponse.toHomeReceiverDialogUiModel())

        // When
        onGetReferralEvalute_thenReturn(response)
        viewModel.getReceiverHomeDialog(anyString())

        // Then
        viewModel.referralEvaluate.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get evaluate join return error response`() {
        // Given
        val error = Exception()
        val expectedError = Fail(error)

        // When
        onGetReferralEvalute_thenReturn(error)
        viewModel.getReceiverHomeDialog(anyString())

        // Then
        viewModel.referralEvaluate.verifyErrorEquals(expectedError)
    }

    @Test
    fun `when getProductRecomOoc should add product recom to home layout list`() {
        /**
         * add procuct recom ooc to layout
         */
        viewModel.getProductRecomOoc()

        /**
         * verify the data test
         */
        val actualValue = (viewModel.homeLayoutList.value as Success).data.items.first()
        assertTrue(actualValue is TokoNowProductRecommendationOocUiModel)
    }

    @Test
    fun `when fetching list of catalog coupons (single coupon) then receiving success result with show status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC"
        val styleParam = "columns=single"
        val buttonStr = COUPON_STATUS_CLAIM
        val slugs = slugText.split(";")

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam,
            isEmpty = false
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when fetching list of catalog coupons (single coupon) but failed then receiving success result with hide status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC"
        val styleParam = "columns=single"
        val buttonStr = COUPON_STATUS_CLAIM

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            errorThrowable = Exception()
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = true,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam,
            isEmpty = false
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when refetching list of catalog coupons (single coupon) then receiving success result with show status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC"
        val styleParam = "columns=single"
        val slugs = slugText.split(";")
        var buttonStr = COUPON_STATUS_CLAIM

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        var catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // reset dummy data
        buttonStr = COUPON_STATUS_CLAIMED

        catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // refetch catalog list
        viewModel.getCatalogCouponList(widgetId, slugs)

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            isEmpty = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when refetching list of catalog coupons (single coupon) then receiving success result with hide status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC"
        val styleParam = "columns=single"
        val slugs = slugText.split(";")
        val buttonStr = COUPON_STATUS_CLAIM

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // reset dummy data
        onGetCatalogCouponList_thenReturn(
            errorThrowable = Exception()
        )

        // refetch catalog list
        viewModel.getCatalogCouponList(widgetId, slugs)

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = true,
            isEmpty = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when fetching list of catalog coupons (double coupon) then receiving success result with show status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC;DEF"
        val styleParam = "columns=double"
        val buttonStr = COUPON_STATUS_CLAIM
        val slugs = slugText.split(";")

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            isEmpty = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when fetching list of catalog coupons (double coupon) but failed then receiving success result with hide status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC;DEF"
        val styleParam = "columns=double"
        val buttonStr = COUPON_STATUS_CLAIM

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            errorThrowable = Exception()
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = true,
            isEmpty = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when refetching list of catalog coupons (double coupon) then receiving success result with show status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC;DEF"
        val styleParam = "columns=double"
        val slugs = slugText.split(";")
        var buttonStr = COUPON_STATUS_CLAIM

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        var catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // reset dummy data
        buttonStr = COUPON_STATUS_CLAIMED

        catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // refetch catalog list
        viewModel.getCatalogCouponList(widgetId, slugs)

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            isEmpty = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when refetching list of catalog coupons (double coupon) then receiving success result with hide status`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC;DEF"
        val styleParam = "columns=double"
        val slugs = slugText.split(";")
        val buttonStr = COUPON_STATUS_CLAIM

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // reset dummy data
        onGetCatalogCouponList_thenReturn(
            errorThrowable = Exception()
        )

        // refetch catalog list
        viewModel.getCatalogCouponList(widgetId, slugs)

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = true,
            isEmpty = false,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when fetching list of catalog coupons (double coupon) but slug size and style param not matching then receiving nothing`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC"
        val styleParam = "columns=double"
        val buttonStr = COUPON_STATUS_CLAIM
        val slugs = slugText.split(";")

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            isEmpty = true,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when fetching list of catalog coupons (single coupon) but slug size and style param not matching then receiving nothing`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC;DEF"
        val styleParam = "columns=single"
        val buttonStr = COUPON_STATUS_CLAIM
        val slugs = slugText.split(";")

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = false
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            isEmpty = true,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when fetching list of catalog coupons then receiving nothing`() {
        // create dummy data
        val warehouseId = "1"
        val widgetId = "2132"
        val widgetTitle = "Coupon Widget"
        val slugText = "ABC;DEF"
        val styleParam = "columns=double"
        val buttonStr = COUPON_STATUS_CLAIM
        val slugs = slugText.split(";")

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )
        val layoutResponse = createChannelLayout(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            styleParam = styleParam
        )
        val catalogCouponList = createCatalogCouponList(
            slugs = slugs,
            buttonStr = buttonStr,
            isEmpty = true
        )

        println(catalogCouponList)

        onGetHomeLayoutData_thenReturn(
            layoutResponse = layoutResponse,
            localCacheModel = localCacheModel
        )
        onGetCatalogCouponList_thenReturn(
            catalogCouponList = catalogCouponList
        )

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        // prepare model for expectedResult
        val data = createLayoutListUiModel(
            widgetId = widgetId,
            widgetTitle = widgetTitle,
            slugText = slugText,
            isError = false,
            isEmpty = true,
            warehouseId = warehouseId,
            buttonStr = buttonStr,
            styleParam = styleParam
        )
        val expectedResult = Success(data)

        // verify use case and the result
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCatalogCouponListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when redeeming coupon and user has logged in then receiving success result`() {
        // create dummy data
        val userLoggedIn = true
        val widgetId = "1212"
        val catalogId = "2222"
        val couponStatus = "Klaim"
        val position = 0
        val slugText = "ABC;CDE"
        val couponName = "3% tolong diklaim"
        val warehouseId = "12322"

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = userLoggedIn)

        val response = createRedeemCoupon()
        onRedeemCoupon_thenReturn(createRedeemCoupon())

        // redeem the coupon
        viewModel.claimCoupon(
            widgetId = widgetId,
            catalogId = catalogId,
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId
        )

        // verify use case and the result
        verifyRedeemCouponUseCaseCalled()
        viewModel.couponClaimed
            .verifySuccessEquals(
                Success(
                    response.mapToClaimCouponDataModel(
                        couponStatus = couponStatus,
                        position = position,
                        slugText = slugText,
                        couponName = couponName,
                        warehouseId = warehouseId
                    )
                )
            )
    }

    @Test
    fun `when redeeming coupon and user has not logged in then receiving login code`() {
        // create dummy data
        val userLoggedIn = false
        val widgetId = "1212"
        val catalogId = "2222"
        val couponStatus = "Klaim"
        val position = 0
        val slugText = "ABC;CDE"
        val couponName = "3% tolong diklaim"
        val warehouseId = "12322"

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = userLoggedIn)

        onRedeemCoupon_thenReturn(createRedeemCoupon())

        // redeem the coupon
        viewModel.claimCoupon(
            widgetId = widgetId,
            catalogId = catalogId,
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId
        )

        // verify the result
        viewModel.couponClaimed
            .verifySuccessEquals(Success(HomeClaimCouponDataModel(code = COUPON_STATUS_LOGIN)))
    }

    @Test
    fun `when redeeming coupon then receiving error result`() {
        // create dummy data
        val userLoggedIn = true
        val widgetId = "1212"
        val catalogId = "2222"
        val couponStatus = "Klaim"
        val position = 0
        val slugText = "ABC;CDE"
        val couponName = "3% tolong diklaim"
        val warehouseId = "12322"

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = userLoggedIn)

        onRedeemCoupon_thenReturn(Exception())

        // redeem the coupon
        viewModel.claimCoupon(
            widgetId = widgetId,
            catalogId = catalogId,
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId
        )

        // verify the result
        assertTrue(viewModel.couponClaimed.value is Fail)
    }
}
