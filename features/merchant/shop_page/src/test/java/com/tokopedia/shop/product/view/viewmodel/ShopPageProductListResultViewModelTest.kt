package com.tokopedia.shop.product.view.viewmodel

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.shop.common.data.model.RestrictionEngineRequestParams
import com.tokopedia.shop.common.data.response.Actions
import com.tokopedia.shop.common.data.response.RestrictValidateRestriction
import com.tokopedia.shop.common.data.response.RestrictionEngineDataResponse
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopStickySortFilter
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import rx.Observable
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopPageProductListResultViewModelTest : ShopPageProductListViewModelTestFixture() {

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
            shopPageProductListResultViewModel.getShop(
                    shopId = "123",
                    shopDomain = "domain",
                    isRefresh = true
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(shopPageProductListResultViewModel.shopInfoResp.value)
        }
    }

    @Test
    fun `check whether response get shop info success without force refresh and empty domain`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            shopPageProductListResultViewModel.getShop(
                    shopId = "123",
                    shopDomain = ""
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(shopPageProductListResultViewModel.shopInfoResp.value)
        }
    }

    @Test
    fun `check whether response get shop info success with shopId 0`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            shopPageProductListResultViewModel.getShop(
                    shopId = "0",
                    shopDomain = "domain"
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(shopPageProductListResultViewModel.shopInfoResp.value)
        }
    }

    @Test
    fun `check whether response get shop info success with only shopId`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            shopPageProductListResultViewModel.getShop(
                    shopId = "123"
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(shopPageProductListResultViewModel.shopInfoResp.value)
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
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Fail)
        }
    }

    @Test
    fun `check getShopRestrictionInfo is Success`() {
        runBlocking {

            coEvery {
                restrictionEngineNplUseCase.executeOnBackground()
            } returns RestrictValidateRestriction(
                    dataResponse = listOf(RestrictionEngineDataResponse(
                            actions = listOf(Actions())
                    ))
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
    fun `check toggleFavorite is Success`() {
        val onSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopUseCase.get().execute(any(), any()) } answers {
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
    fun `check whether response get shop product success`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            shopPageProductListResultViewModel.getShopProduct(
                    shopId = "123",
                    etalaseType = 2,
                    shopProductFilterParameter = ShopProductFilterParameter(),
                    widgetUserAddressLocalData = addressWidgetData
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
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
                    widgetUserAddressLocalData = addressWidgetData
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
                    widgetUserAddressLocalData = addressWidgetData
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
                    widgetUserAddressLocalData = addressWidgetData
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
                    widgetUserAddressLocalData = addressWidgetData
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
                    widgetUserAddressLocalData = addressWidgetData
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
                    widgetUserAddressLocalData = addressWidgetData
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Fail)
        }
    }

    @Test
    fun `check getShopProductEmptyState is Success with default value`() {
        runBlocking {
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct()

            shopPageProductListResultViewModel.getShopProductEmptyState(
                    shopId = "123",
                    widgetUserAddressLocalData = addressWidgetData
            )

            verifyGetShopProductUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.productDataEmpty.value)
            assertTrue(shopPageProductListResultViewModel.productDataEmpty.value is Success<List<ShopProductUiModel>>)
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
    fun `check getBottomSheetFilterData is Success`() {
        runBlocking {

            coEvery {
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            } returns DynamicFilterModel()

            shopPageProductListResultViewModel.getBottomSheetFilterData()

            verifyGetShopFilterBottomSheetDataUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.bottomSheetFilterLiveData.value)
            assertTrue(shopPageProductListResultViewModel.bottomSheetFilterLiveData.value is Success<DynamicFilterModel>)

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
    fun `check getSortNameById is return empty because no sort id match`() {
        runBlocking {

            val sortList = mutableListOf<ShopProductSort>().apply {
                add(ShopProductSort().apply {
                    name = "sortExample"
                    value = "321"
                    inputType = "inputType"
                    key = "key"
                })
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

            val actualResult : String = shopPageProductListResultViewModel.getSortNameById("123")
            assertNotNull(actualResult)
            assertTrue(actualResult == "")
        }
    }

    @Test
    fun `check getSortNameById is return same value with mock value`() {
        runBlocking {

            val sortList = mutableListOf<ShopProductSort>().apply {
                add(ShopProductSort().apply {
                    name = "sortExample"
                    value = "123"
                    inputType = "inputType"
                    key = "key"
                })
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

            val actualResult : String = shopPageProductListResultViewModel.getSortNameById("123")
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

            val actualResult : String = shopPageProductListResultViewModel.getSortNameById("123")
            assertNotNull(actualResult)
            assertTrue(actualResult == "")
        }
    }

    private fun convertToUiModel(list: List<ShopProductSort>): List<ShopProductSortModel> {
        return mutableListOf<ShopProductSortModel>().apply {
            list.forEach {
                add(ShopProductSortModel().apply {
                    key = it.key
                    value = it.value
                    inputType = it.inputType
                    name = it.name
                })
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
}