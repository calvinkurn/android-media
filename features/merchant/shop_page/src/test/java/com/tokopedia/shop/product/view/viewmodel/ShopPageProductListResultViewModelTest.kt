package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.shop.common.data.model.RestrictionEngineRequestParams
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.data.response.Actions
import com.tokopedia.shop.common.data.response.RestrictValidateRestriction
import com.tokopedia.shop.common.data.response.RestrictionEngineDataResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowButton
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.Status
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopPageProductResultPageData
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseTitleUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopStickySortFilter
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import rx.Observable
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopPageProductListResultViewModelTest : ShopPageProductListViewModelTestFixture() {

    @After
    fun afterTest() {
        unmockkAll()
    }

    @Test
    fun `check userSession isLoggedIn return same value with mock value`() {
        every { userSessionInterface.isLoggedIn } returns true
        assertTrue(shopPageProductListResultViewModel.isLogin)
    }

    @Test
    fun `check userSession userId return same value with mock value`() {
        every { userSessionInterface.userId } returns "123"
        assertEquals("123", shopPageProductListResultViewModel.userId)
    }

    @Test
    fun `check whether response get shop info success with force refresh`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            coEvery { gqlShopPageGetDynamicTabUseCase.executeOnBackground() } returns ShopPageGetDynamicTabResponse()
            shopPageProductListResultViewModel.getShop(
                shopId = "123",
                shopDomain = "domain",
                isRefresh = true
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Success<ShopPageProductResultPageData>)
            assertNotNull(shopPageProductListResultViewModel.shopData.value)
        }
    }

    @Test
    fun `check whether response get shop info success without force refresh and empty domain`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            coEvery { gqlShopPageGetDynamicTabUseCase.executeOnBackground() } returns ShopPageGetDynamicTabResponse()
            shopPageProductListResultViewModel.getShop(
                shopId = "123",
                shopDomain = ""
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Success<ShopPageProductResultPageData>)
            assertNotNull(shopPageProductListResultViewModel.shopData.value)
        }
    }

    @Test
    fun `check whether response get shop info success with shopId 0`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            coEvery { gqlShopPageGetDynamicTabUseCase.executeOnBackground() } returns ShopPageGetDynamicTabResponse()
            shopPageProductListResultViewModel.getShop(
                shopId = "0",
                shopDomain = "domain"
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Success<ShopPageProductResultPageData>)
            assertNotNull(shopPageProductListResultViewModel.shopData.value)
        }
    }

    @Test
    fun `check whether response get shop info success with only shopId`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            coEvery { gqlShopPageGetDynamicTabUseCase.executeOnBackground() } returns ShopPageGetDynamicTabResponse()
            shopPageProductListResultViewModel.getShop(
                shopId = "123"
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Success<ShopPageProductResultPageData>)
            assertNotNull(shopPageProductListResultViewModel.shopData.value)
        }
    }

    @Test
    fun `check whether response get shop info success with shopId 0 and domain empty`() {
        runBlocking {
            shopPageProductListResultViewModel.getShop("0", "")
        }
    }

    @Test
    fun `check whether response get shop info error`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()

            shopPageProductListResultViewModel.getShop(
                shopId = "123",
                shopDomain = "domain",
                isRefresh = true
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Fail)
        }
    }

    @Test
    fun `check whether response get shop dynamic tab error`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            coEvery { gqlShopPageGetDynamicTabUseCase.executeOnBackground() } throws Exception()
            shopPageProductListResultViewModel.getShop(
                shopId = "123",
                shopDomain = "domain",
                isRefresh = true
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Fail)
        }
    }

    @Test
    fun `check whether shopData value is fail if exception thrown`() {
        runBlocking {
            val observer = mockk<Observer<Result<ShopPageProductResultPageData>>>()
            shopPageProductListResultViewModel.shopData.observeForever(observer)
            every { observer.onChanged(any()) } throws Exception()
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            coEvery { gqlShopPageGetDynamicTabUseCase.executeOnBackground() } returns ShopPageGetDynamicTabResponse()
            shopPageProductListResultViewModel.getShop(
                shopId = "123",
                shopDomain = "domain",
                isRefresh = true
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopData.value is Fail)
        }
    }

    @Test
    fun `check getShopRestrictionInfo is Success`() {
        runBlocking {
            val mockButtonLabel = "Button"
            coEvery {
                restrictionEngineNplUseCase.executeOnBackground()
            } returns RestrictValidateRestriction(
                dataResponse = listOf(
                    RestrictionEngineDataResponse(
                        actions = listOf(Actions())
                    )
                )
            )
            coEvery {
                getFollowStatusUseCase.executeOnBackground()
            } returns FollowStatusResponse(
                FollowStatus(
                    status = Status(userIsFollowing = false, userNeverFollow = false, userFirstFollow = false),
                    followButton = FollowButton(buttonLabel = mockButtonLabel, voucherIconURL = "", coachmarkText = ""),
                    error = null
                )
            )
            shopPageProductListResultViewModel.getShopRestrictionInfo(
                input = RestrictionEngineRequestParams(),
                shopId = "12131"
            )

            verifyGetShopRestrictionInfoUseCaseCalled()
            assertNotNull(shopPageProductListResultViewModel.restrictionEngineData.value)
            assertTrue(shopPageProductListResultViewModel.restrictionEngineData.value is Success)
            assertTrue((shopPageProductListResultViewModel.restrictionEngineData.value as Success).data.buttonLabel == mockButtonLabel)
        }
    }

    @Test
    fun `check getShopRestrictionInfo is Success if followButton is null`() {
        runBlocking {
            coEvery {
                restrictionEngineNplUseCase.executeOnBackground()
            } returns RestrictValidateRestriction(
                dataResponse = listOf(
                    RestrictionEngineDataResponse(
                        actions = listOf(Actions())
                    )
                )
            )
            coEvery {
                getFollowStatusUseCase.executeOnBackground()
            } returns FollowStatusResponse(
                FollowStatus(
                    status = Status(userIsFollowing = false, userNeverFollow = false, userFirstFollow = false),
                    followButton = null,
                    error = null
                )
            )
            shopPageProductListResultViewModel.getShopRestrictionInfo(
                input = RestrictionEngineRequestParams(),
                shopId = "12131"
            )

            verifyGetShopRestrictionInfoUseCaseCalled()
            assertNotNull(shopPageProductListResultViewModel.restrictionEngineData.value)
            assertTrue(shopPageProductListResultViewModel.restrictionEngineData.value is Success)
        }
    }

    @Test
    fun `check getShopRestrictionInfo is Success if followStatus is null`() {
        runBlocking {
            coEvery {
                restrictionEngineNplUseCase.executeOnBackground()
            } returns RestrictValidateRestriction(
                dataResponse = listOf(
                    RestrictionEngineDataResponse(
                        actions = listOf(Actions())
                    )
                )
            )
            shopPageProductListResultViewModel.getShopRestrictionInfo(
                input = RestrictionEngineRequestParams(),
                shopId = "12131"
            )

            verifyGetShopRestrictionInfoUseCaseCalled()
            assertNotNull(shopPageProductListResultViewModel.restrictionEngineData.value)
            assertTrue(shopPageProductListResultViewModel.restrictionEngineData.value is Success)
        }
    }

    @Test
    fun `check getShopRestrictionInfo is Fail`() {
        runBlocking {
            coEvery {
                restrictionEngineNplUseCase.executeOnBackground()
            } throws Exception()

            shopPageProductListResultViewModel.getShopRestrictionInfo(
                input = RestrictionEngineRequestParams(),
                shopId = "123121"
            )

            verifyGetShopRestrictionInfoUseCaseCalled()
            assertNotNull(shopPageProductListResultViewModel.restrictionEngineData.value)
            assertTrue(shopPageProductListResultViewModel.restrictionEngineData.value is Fail)
        }
    }

    @Test
    fun `check restrictionEngineData value is fail if mapper throw an exception`() {
        runBlocking {
            coEvery {
                restrictionEngineNplUseCase.executeOnBackground()
            } returns RestrictValidateRestriction(
                dataResponse = listOf(
                    RestrictionEngineDataResponse(
                        actions = listOf(Actions())
                    )
                )
            )
            mockkObject(ShopPageProductListMapper)
            every { ShopPageProductListMapper.mapRestrictionEngineResponseToModel(any()) } throws Exception()
            shopPageProductListResultViewModel.getShopRestrictionInfo(
                input = RestrictionEngineRequestParams(),
                shopId = "12131"
            )

            verifyGetShopRestrictionInfoUseCaseCalled()
            assertNotNull(shopPageProductListResultViewModel.restrictionEngineData.value)
            assertTrue(shopPageProductListResultViewModel.restrictionEngineData.value is Fail)
        }
    }

    @Test
    fun `check toggleFavorite is Success`() {
        val onSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopUseCase.get().execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onCompleted()
            (secondArg() as Subscriber<Boolean>).onNext(true)
        }
        shopPageProductListResultViewModel.toggleFavorite(shopId = "123", onSuccess = onSuccess, onError = {})
        verify { onSuccess.invoke(true) }
    }

    @Test
    fun `check toggleFavorite is Fail`() {
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val throwable = Throwable()
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopUseCase.get().execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onError(throwable)
        }
        shopPageProductListResultViewModel.toggleFavorite(shopId = "123", onSuccess = {}, onError = onError)
        verify { onError.invoke(throwable) }
    }

    @Test
    fun `check whether response get shop product success with direct purchase true`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
            )
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
            assert(
                (shopPageProductListResultViewModel.productData.value as? Success)?.data?.listShopProductUiModel?.all {
                    it.isEnableDirectPurchase
                } == mockIsDirectPurchaseTrue
            )
        }
    }

    @Test
    fun `check whether response get shop product success with direct purchase false`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
            )
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseFalse
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
            assert(
                (shopPageProductListResultViewModel.productData.value as? Success)?.data?.listShopProductUiModel?.all {
                    it.isEnableDirectPurchase
                } == mockIsDirectPurchaseFalse
            )
        }
    }

    @Test
    fun `check whether response get shop product success with force refresh`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
        }
    }

    @Test
    fun `check whether response get shop product success with next page available`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                totalData = 20
            )
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
        }
    }

    @Test
    fun `check whether response get shop product success with isMyShop is true`() {
        runBlocking {
            every { userSessionInterface.shopId } returns "123"
            shopPageProductListResultViewModel.isMyShop("123")

            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                totalData = 20
            )
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
        }
    }

    @Test
    fun `check whether response get shop product success with isMyShop is false`() {
        runBlocking {
            every { userSessionInterface.shopId } returns "321"
            shopPageProductListResultViewModel.isMyShop("123")

            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                totalData = 20
            )
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
        }
    }

    @Test
    fun `check whether response get shop product success with no default parameters`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                totalData = 20,
                data = listOf(ShopProduct())
            )
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                page = 1,
                perPage = 10,
                etalase = "123",
                search = "search",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
        }
    }

    @Test
    fun `check whether response get shop product error`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            shopPageProductListResultViewModel.getShopProduct(
                shopId = "123",
                etalaseType = 2,
                shopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Fail)
        }
    }

    @Test
    fun `check getShopProductEmptyState is Success with default value and direct purchase true`() {
        runBlocking {
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
            )

            shopPageProductListResultViewModel.getShopProductEmptyState(
                shopId = "123",
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )

            verifyGetShopProductUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.productDataEmpty.value)
            assertTrue(shopPageProductListResultViewModel.productDataEmpty.value is Success<List<ShopProductUiModel>>)
            assert(
                (shopPageProductListResultViewModel.productDataEmpty.value as? Success)?.data?.all {
                    it.isEnableDirectPurchase
                } == mockIsDirectPurchaseTrue
            )
        }
    }

    @Test
    fun `check getShopProductEmptyState is Success with default value and direct purchase false`() {
        runBlocking {
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
            )

            shopPageProductListResultViewModel.getShopProductEmptyState(
                shopId = "123",
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseFalse
            )

            verifyGetShopProductUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.productDataEmpty.value)
            assertTrue(shopPageProductListResultViewModel.productDataEmpty.value is Success<List<ShopProductUiModel>>)
            assert(
                (shopPageProductListResultViewModel.productDataEmpty.value as? Success)?.data?.all {
                    it.isEnableDirectPurchase
                } == mockIsDirectPurchaseFalse
            )
        }
    }

    @Test
    fun `check getShopProductEmptyState is Fail`() {
        runBlocking {
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } throws Exception()

            shopPageProductListResultViewModel.getShopProductEmptyState(
                shopId = "123",
                widgetUserAddressLocalData = addressWidgetData,
                isEnableDirectPurchase = mockIsDirectPurchaseTrue
            )

            verifyGetShopProductUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.productDataEmpty.value)
            assertTrue(shopPageProductListResultViewModel.productDataEmpty.value is Fail)
        }
    }

    @Test
    fun `check getShopFilterData is Success when isOwner value is true`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())

            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopSortFilterData.value)
            assertTrue(shopPageProductListResultViewModel.shopSortFilterData.value is Success<ShopStickySortFilter>)
        }
    }

    @Test
    fun `check getShopFilterData is Success when isOwner value is false`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())

            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = false
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopSortFilterData.value)
            assertTrue(shopPageProductListResultViewModel.shopSortFilterData.value is Success<ShopStickySortFilter>)
        }
    }

    @Test
    fun `check getShopFilterData is Success when isForceRefresh value is true`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())

            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = false,
                isForceRefresh = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopSortFilterData.value)
            assertTrue(shopPageProductListResultViewModel.shopSortFilterData.value is Success<ShopStickySortFilter>)
        }
    }

    @Test
    fun `check getShopFilterData is Fail when mapper throw exception`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())
            val observer = mockk<Observer<Result<ShopStickySortFilter>>>(relaxed = true)
            shopPageProductListResultViewModel.shopSortFilterData.observeForever(observer)
            every { observer.onChanged(any<Success<ShopStickySortFilter>>()) } throws Exception()
            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = false,
                isForceRefresh = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopSortFilterData.value)
            assertTrue(shopPageProductListResultViewModel.shopSortFilterData.value is Fail)
        }
    }

    @Test
    fun `check getShopFilterData shopSortFilterData value is fail if both shopEtalaseData response is error`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } throws Exception()

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf()
            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = false,
                isForceRefresh = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopSortFilterData.value)
            assertTrue(shopPageProductListResultViewModel.shopSortFilterData.value is Fail)
        }
    }

    @Test
    fun `check getShopFilterData shopSortFilterData value is fail if both shopSortData response is error`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } throws Exception()
            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = false,
                isForceRefresh = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopSortFilterData.value)
            assertTrue(shopPageProductListResultViewModel.shopSortFilterData.value is Fail)
        }
    }

    @Test
    fun `check getBottomSheetFilterData is Success`() {
        runBlocking {
            coEvery {
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            } returns DynamicFilterModel(
                data = DataValue(
                    listOf(Filter(title = "pengiriman"), Filter(title = "Rating"))
                )
            )

            shopPageProductListResultViewModel.getBottomSheetFilterData()

            verifyGetShopFilterBottomSheetDataUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.bottomSheetFilterLiveData.value)
            assertTrue(shopPageProductListResultViewModel.bottomSheetFilterLiveData.value is Success<DynamicFilterModel>)
        }
    }

    @Test
    fun `check getBottomSheetFilterData bottomSheetFilterLiveData value is null if response error`() {
        runBlocking {
            coEvery {
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            } throws Exception()

            shopPageProductListResultViewModel.getBottomSheetFilterData()

            verifyGetShopFilterBottomSheetDataUseCaseCalled()

            assertNull(shopPageProductListResultViewModel.bottomSheetFilterLiveData.value)
        }
    }

    @Test
    fun `check getFilterResultCount is Success`() {
        runBlocking {
            coEvery {
                getShopFilterProductCountUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct().totalData

            shopPageProductListResultViewModel.getFilterResultCount(
                shopId = anyString(),
                productPerPage = mockProductPerPage,
                searchKeyword = anyString(),
                etalaseId = anyString(),
                tempShopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData
            )

            verifyGetShopFilterProductCountUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopProductFilterCountLiveData.value)
            assertTrue(shopPageProductListResultViewModel.shopProductFilterCountLiveData.value is Success<Int>)
        }
    }

    @Test
    fun `check getFilterResultCount is Fail`() {
        runBlocking {
            coEvery {
                getShopFilterProductCountUseCase.executeOnBackground()
            } throws Exception()

            shopPageProductListResultViewModel.getFilterResultCount(
                shopId = anyString(),
                productPerPage = mockProductPerPage,
                searchKeyword = anyString(),
                etalaseId = anyString(),
                tempShopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData
            )

            verifyGetShopFilterProductCountUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopProductFilterCountLiveData.value)
            assertTrue(shopPageProductListResultViewModel.shopProductFilterCountLiveData.value is Fail)
        }
    }

    @Test
    fun `check getSortNameById is return empty because no sort id match`() {
        runBlocking {
            val sortList = mutableListOf<ShopProductSort>().apply {
                add(
                    ShopProductSort().apply {
                        name = "sortExample"
                        value = "321"
                        inputType = "inputType"
                        key = "key"
                    }
                )
            }

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns sortList

            coEvery {
                shopProductSortMapper.convertSort(any())
            } returns convertToUiModel(sortList)

            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            val actualResult: String = shopPageProductListResultViewModel.getSortNameById("123")
            assertNotNull(actualResult)
            assertTrue(actualResult == "")
        }
    }

    @Test
    fun `check getSortNameById is return same value with mock value`() {
        runBlocking {
            val sortList = mutableListOf<ShopProductSort>().apply {
                add(
                    ShopProductSort().apply {
                        name = "sortExample"
                        value = "123"
                        inputType = "inputType"
                        key = "key"
                    }
                )
            }

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns sortList

            coEvery {
                shopProductSortMapper.convertSort(any())
            } returns convertToUiModel(sortList)

            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            val actualResult: String = shopPageProductListResultViewModel.getSortNameById("123")
            assertNotNull(actualResult)
            assertTrue(actualResult == "sortExample")
        }
    }

    @Test
    fun `check getSortNameById is return empty because list is empty`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf()

            coEvery {
                shopProductSortMapper.convertSort(any())
            } returns convertToUiModel(listOf())

            shopPageProductListResultViewModel.getShopFilterData(
                shopInfo = ShopInfo(),
                isOwner = true
            )

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            val actualResult: String = shopPageProductListResultViewModel.getSortNameById("123")
            assertNotNull(actualResult)
            assertTrue(actualResult == "")
        }
    }

    private fun convertToUiModel(list: List<ShopProductSort>): List<ShopProductSortModel> {
        return mutableListOf<ShopProductSortModel>().apply {
            list.forEach {
                add(
                    ShopProductSortModel().apply {
                        key = it.key
                        value = it.value
                        inputType = it.inputType
                        name = it.name
                    }
                )
            }
        }
    }

    @Test
    fun `check clearCache for UseCase is called`() {
        shopPageProductListResultViewModel.clearCache()
        verify {
            getShopEtalaseByShopUseCase.clearCache()
            getShopProductUseCase.clearCache()
        }
    }

    @Test
    fun `check isMyShop is return valid logged in shop id`() {
        every { userSessionInterface.shopId } returns "123"
        val expectedResult = shopPageProductListResultViewModel.isMyShop("123")
        assertTrue(expectedResult)
        assertFalse(!expectedResult)
    }

    private fun verifyGetShopInfoUseCaseCalled() {
        coVerify { getShopInfoUseCase.executeOnBackground() }
    }

    private fun verifyGetShopProductUseCaseCalled() {
        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    private fun verifyGetShopEtalaseByShopUseCaseCalled() {
        coVerify { getShopEtalaseByShopUseCase.createObservable(any()) }
    }

    private fun verifyGetShopSortUseCaseCalled() {
        coVerify { gqlGetShopSortUseCase.executeOnBackground() }
    }

    private fun verifyGetShopFilterBottomSheetDataUseCaseCalled() {
        coVerify { getShopFilterBottomSheetDataUseCase.executeOnBackground() }
    }

    private fun verifyGetShopFilterProductCountUseCaseCalled() {
        coVerify { getShopFilterProductCountUseCase.executeOnBackground() }
    }

    private fun verifyGetShopRestrictionInfoUseCaseCalled() {
        coVerify { restrictionEngineNplUseCase.executeOnBackground() }
    }

    @Test
    fun `when call getShopProductDataWithUpdatedQuantity with mocked mini cart data, then product in cart should match with mini cart data`() {
        val mockMiniCartSimplifiedData = getMockMiniCartSimplifiedData()
        val mockShopProductListData = getMockShopProductData()
        shopPageProductListResultViewModel.updateMiniCartData(mockMiniCartSimplifiedData)
        shopPageProductListResultViewModel.getShopProductDataWithUpdatedQuantity(
            mockShopProductListData
        )
        shopPageProductListResultViewModel.updatedShopProductListQuantityData.value?.onEach { shopProductUiModel ->
            when (shopProductUiModel) {
                is ShopProductUiModel -> {
                    checkProductListDataShouldMatchWithMatchedMiniCartData(
                        shopProductUiModel,
                        mockMiniCartSimplifiedData
                    )
                }
            }
        }
    }

    private fun getMatchedMiniCartItem(
        shopProductUiModel: ShopProductUiModel,
        miniCartData: MiniCartSimplifiedData
    ): List<MiniCartItem.MiniCartItemProduct> {
        return miniCartData.let { miniCartSimplifiedData ->
            val isVariant = shopProductUiModel.isVariant
            val listMatchedMiniCartItemProduct = if (isVariant) {
                miniCartSimplifiedData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()
                    .filter { it.productParentId == shopProductUiModel.parentId }
            } else {
                val childProductId = shopProductUiModel.id
                miniCartSimplifiedData.miniCartItems.getMiniCartItemProduct(childProductId)?.let {
                    listOf(it)
                }.orEmpty()
            }
            listMatchedMiniCartItemProduct.filter { !it.isError }
        }
    }

    private fun checkProductListDataShouldMatchWithMatchedMiniCartData(
        shopProductUiModel: ShopProductUiModel,
        miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        val matchedMiniCartItem = getMatchedMiniCartItem(
            shopProductUiModel,
            miniCartSimplifiedData
        )
        if (matchedMiniCartItem.isNotEmpty()) {
            val quantityOnMiniCart = matchedMiniCartItem.sumOf { it.quantity }
            assert(quantityOnMiniCart == shopProductUiModel.productInCart)
        } else {
            assert(shopProductUiModel.productInCart.isZero())
        }
    }

    @Test
    fun `when call getShopProductDataWithUpdatedQuantity without mocked mini cart data, then product in cart should be zero`() {
        val mockShopProductListData = getMockShopProductData()
        shopPageProductListResultViewModel.getShopProductDataWithUpdatedQuantity(mockShopProductListData)
        shopPageProductListResultViewModel.updatedShopProductListQuantityData.value?.onEach { shopProductUiModel ->
            when (shopProductUiModel) {
                is ShopProductUiModel -> {
                    assert(shopProductUiModel.productInCart.isZero())
                }
            }
        }
    }

    private fun getMockShopProductData(): MutableList<Visitable<*>> {
        return mutableListOf(
            ShopProductUiModel().apply {
                id = "1"
                productInCart = 3
            },
            ShopProductUiModel().apply {
                id = "8"
            },
            ShopProductUiModel().apply {
                parentId = "12"
                isVariant = true
            },
            ShopProductUiModel().apply {
                parentId = "13"
                isVariant = true
            },
            ShopProductUiModel().apply {
                parentId = "14"
                isVariant = true
            },
            ShopProductUiModel(),
            ShopProductEtalaseTitleUiModel("", "")
        )
    }

    private fun getMockMiniCartSimplifiedData(): MiniCartSimplifiedData {
        return MiniCartSimplifiedData(
            miniCartItems = hashMapOf(
                MiniCartItemKey("1") to MiniCartItem.MiniCartItemProduct(
                    productId = "1",
                    quantity = 3
                ),
                MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(
                    productId = "2",
                    productParentId = "12",
                    quantity = 5
                ),
                MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(
                    productId = "3",
                    productParentId = "12",
                    quantity = 5
                ),
                MiniCartItemKey("4") to MiniCartItem.MiniCartItemProduct(
                    productId = "4",
                    productParentId = "13",
                    quantity = 5,
                    isError = true
                ),
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    productParentId = "13",
                    quantity = 5
                ),
                MiniCartItemKey("6") to MiniCartItem.MiniCartItemProduct(
                    productId = "6",
                    productParentId = "14",
                    quantity = 5,
                    isError = true
                ),
                MiniCartItemKey("7") to MiniCartItem.MiniCartItemProduct(
                    productId = "7",
                    productParentId = "14",
                    quantity = 5,
                    isError = true
                ),
                MiniCartItemKey("8") to MiniCartItem.MiniCartItemProduct(
                    productId = "8",
                    isError = true
                ),
                MiniCartItemKey("9") to MiniCartItem.MiniCartItemParentProduct()
            )
        )
    }

    @Test
    fun `when call handleAtcFlow on add item to cart state is success, then miniCartAdd value should be success`() {
        val mockQuantity = 5
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "33"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(
                AddToCartDataModel(
                    data = DataModel(
                        success = 1,
                        productId = "33"
                    )
                )
            )
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartAdd.value is Success)
        assert(shopPageProductListResultViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.ADD)
        assert(shopPageProductListResultViewModel.createAffiliateCookieAtcProduct.value != null)
    }

    @Test
    fun `when call handleAtcFlow on add item to cart state is error, then miniCartAdd value should be fail`() {
        val mockQuantity = 5
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "33"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartAdd.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow on remove item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 0
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(
                RemoveFromCartData(
                    data = Data(
                        success = 1
                    )
                )
            )
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartRemove.value is Success)
        assert(shopPageProductListResultViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.REMOVE)
        assert(shopPageProductListResultViewModel.createAffiliateCookieAtcProduct.value == null)
    }

    @Test
    fun `when call handleAtcFlow on remove item from cart state is error, then miniCartRemove value should be fail`() {
        val mockQuantity = 0
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartRemove.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow on update add item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 10
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(
                UpdateCartV2Data(
                    data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                        status = true
                    )
                )
            )
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartUpdate.value is Success)
        assert(shopPageProductListResultViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.UPDATE_ADD)
        assert(shopPageProductListResultViewModel.createAffiliateCookieAtcProduct.value != null)
    }

    @Test
    fun `when call handleAtcFlow on update remove item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(
                UpdateCartV2Data(
                    data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                        status = true
                    )
                )
            )
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartUpdate.value is Success)
        assert(shopPageProductListResultViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.UPDATE_REMOVE)
        assert(shopPageProductListResultViewModel.createAffiliateCookieAtcProduct.value == null)
    }

    @Test
    fun `when call handleAtcFlow on update item from cart state is success, then miniCartRemove value should be fail`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        shopPageProductListResultViewModel.updateMiniCartData(getMockMiniCartSimplifiedData())
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartUpdate.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow without set mini cart data, then miniCartAdd, miniCartRemove ,miniCartUpdate and shopPageAtcTracker value should be null`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        shopPageProductListResultViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(shopPageProductListResultViewModel.miniCartAdd.value == null)
        assert(shopPageProductListResultViewModel.miniCartUpdate.value == null)
        assert(shopPageProductListResultViewModel.miniCartUpdate.value == null)
        assert(shopPageProductListResultViewModel.shopPageAtcTracker.value == null)
    }

    @Test
    fun `check when call initAffiliateCookie is success`() {
        val mockShopId = "456"
        coEvery {
            affiliateCookieHelper.initCookie(any(), any(), any())
        } returns Unit
        shopPageProductListResultViewModel.initAffiliateCookie(
            affiliateCookieHelper,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(), any(), any()) }
    }

    @Test
    fun `when when call initAffiliateCookie is not success`() {
        val mockShopId = "456"
        coEvery {
            affiliateCookieHelper.initCookie(any(), any(), any())
        } throws Exception()
        shopPageProductListResultViewModel.initAffiliateCookie(
            affiliateCookieHelper,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(), any(), any()) }
    }

    @Test
    fun `when call createAffiliateCookieShopAtcDirectPurchase is success`() {
        val mockAffiliateChannel = "channel"
        val mockIsVariant = true
        val mockProductId = "678"
        val mockStockQty = 11
        val mockShopId = "125"
        coEvery {
            affiliateCookieHelper.initCookie(any(),any(),any())
        } returns Unit
        shopPageProductListResultViewModel.createAffiliateCookieShopAtcProduct(
            affiliateCookieHelper,
            mockAffiliateChannel,
            mockProductId,
            mockIsVariant,
            mockStockQty,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(),any(),any()) }
    }

    @Test
    fun `when call createAffiliateCookieShopAtcDirectPurchase is error`() {
        val mockAffiliateChannel = "channel"
        val mockIsVariant = true
        val mockProductId = "678"
        val mockStockQty = 11
        val mockShopId = "125"
        coEvery {
            affiliateCookieHelper.initCookie(any(),any(),any())
        } throws Exception()
        shopPageProductListResultViewModel.createAffiliateCookieShopAtcProduct(
            affiliateCookieHelper,
            mockAffiliateChannel,
            mockProductId,
            mockIsVariant,
            mockStockQty,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(),any(),any()) }
    }

    @Test
    fun `when call getShopAffiliateChannel success, then shopAffiliateChannel should return mocked value`() {
        val mockAffiliateChannel = "channel"
        coEvery {
            sharedPreferences.getString(any(), any())
        } returns mockAffiliateChannel
        shopPageProductListResultViewModel.getShopAffiliateChannel()
        assert(shopPageProductListResultViewModel.shopAffiliateChannel.value == mockAffiliateChannel)
    }

    @Test
    fun `when getShopAffiliateChannel error, then shopAffiliateChannel should be null`() {
        coEvery {
            sharedPreferences.getString(any(), any())
        } throws Exception()
        shopPageProductListResultViewModel.getShopAffiliateChannel()
        assert(shopPageProductListResultViewModel.shopAffiliateChannel.value == null)
    }
}
