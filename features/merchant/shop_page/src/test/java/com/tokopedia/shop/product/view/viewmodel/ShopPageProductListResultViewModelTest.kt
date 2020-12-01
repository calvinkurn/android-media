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
import com.tokopedia.shop.product.view.datamodel.GetShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
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
import org.mockito.ArgumentMatchers.*
import rx.Observable
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopPageProductListResultViewModelTest : ShopPageProductListViewModelTestFixture() {

    private var shopProductTotalData = -1

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
                    shopId = "123"
            )
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(shopPageProductListResultViewModel.shopInfoResp.value)
        }
    }

    @Test
    fun `check whether response get shop info success without force refresh and empty domain copy`() {
        runBlocking {
            shopPageProductListResultViewModel.getShop(null)
        }
    }

    @Test
    fun `check whether response get shop info success without force refresh and empty domain copy2`() {
        runBlocking {
            shopPageProductListResultViewModel.getShop("0")
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
            } throws Exception()

            shopPageProductListResultViewModel.getShopRestrictionInfo(
                    input = RestrictionEngineRequestParams()
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
                    shopProductFilterParameter = ShopProductFilterParameter()
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Success)
            assertNotNull(shopPageProductListResultViewModel.productData.value)
            shopProductTotalData = (
                    shopPageProductListResultViewModel.productData.value as Success<GetShopProductUiModel>
                    ).data.listShopProductUiModel.size
        }
    }

    @Test
    fun `check whether response get shop product error`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            shopPageProductListResultViewModel.getShopProduct(
                    shopId = "123",
                    etalaseType = 2,
                    shopProductFilterParameter = ShopProductFilterParameter()
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
                    shopId = "123"
            )

            verifyGetShopProductUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.productDataEmpty.value)
            assertTrue(shopPageProductListResultViewModel.productDataEmpty.value is Success<List<ShopProductViewModel>>)
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
                    tempShopProductFilterParameter = ShopProductFilterParameter()
            )

            verifyGetShopFilterProductCountUseCaseCalled()

            assertNotNull(shopPageProductListResultViewModel.shopProductFilterCountLiveData.value)
            assertTrue(shopPageProductListResultViewModel.shopProductFilterCountLiveData.value is Success<Int>)

        }
    }

    @Test
    fun `check getSortNameById is return empty because no sort id match`() {
        runBlocking {

            val sortDataExample = ShopProductSort()
            sortDataExample.name = "sortExample"
            sortDataExample.value = "321"
            sortDataExample.inputType = "inputType"
            sortDataExample.key = "key"
            val sortList = mutableListOf<ShopProductSort>()
            sortList.add(sortDataExample)

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

            val sortDataExample = ShopProductSort()
            sortDataExample.name = "sortExample"
            sortDataExample.value = "123"
            sortDataExample.inputType = "inputType"
            sortDataExample.key = "key"
            val sortList = mutableListOf<ShopProductSort>()
            sortList.add(sortDataExample)

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