package com.tokopedia.shop.product.view.viewmodel

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.view.isZero
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
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import rx.Observable

@ExperimentalCoroutinesApi
class ShopPageProductListResultViewModelTest : ShopPageProductListViewModelTestFixture() {

    private var shopProductTotalData = -1

    @Test
    fun `check whether response get shop info success is not null`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            shopPageProductListResultViewModel.getShop(anyString(), anyString(), anyBoolean())
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(shopPageProductListResultViewModel.shopInfoResp.value)
        }
    }

    @Test
    fun `check whether response get shop info error`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()

            shopPageProductListResultViewModel.getShop(anyString(), anyString(), anyBoolean())
            verifyGetShopInfoUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.shopInfoResp.value is Fail)
        }
    }

    @Test
    fun `check whether response get shop product success is not null`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            shopPageProductListResultViewModel.getShopProduct(
                    anyString(), anyInt(), anyInt(), anyString(), anyString(), anyBoolean(), anyInt(), ShopProductFilterParameter()
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
                    anyString(),
                    anyInt(),
                    anyInt(),
                    anyString(),
                    anyString(),
                    anyBoolean(),
                    anyInt(),
                    ShopProductFilterParameter()
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(shopPageProductListResultViewModel.productData.value is Fail)
        }
    }

    @Test
    fun `check getShopProductEmptyState is Success`() {
        runBlocking {
            if(shopProductTotalData.isZero()) {
                coEvery {
                    getShopProductUseCase.executeOnBackground()
                } returns ShopProduct.GetShopProduct()

                shopPageProductListResultViewModel.getShopProductEmptyState(
                        shopId = anyString(),
                        page = anyInt(),
                        perPage = anyInt(),
                        sortId = anyInt(),
                        etalase = anyString(),
                        search = anyString(),
                        isForceRefresh = anyBoolean()
                )

                verifyGetShopProductUseCaseCalled()

                assertNotNull(shopPageProductListResultViewModel.productDataEmpty.value)
                assertTrue(shopPageProductListResultViewModel.productDataEmpty.value is Success<List<ShopProductViewModel>>)
            }
        }
    }

    @Test
    fun `check getShopFilterData is Success`() {
        runBlocking {

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())

            shopPageProductListResultViewModel.getShopFilterData(
                    shopInfo = ShopInfo(),
                    isOwner = anyBoolean(),
                    isForceRefresh = anyBoolean()
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
}