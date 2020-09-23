package com.tokopedia.shop_showcase.viewmodel.shopshowcasemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop_showcase.coroutines.TestCoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseListBuyerUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseTotalProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcasePickerViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopShowcasePickerViewModelTest {

    @RelaxedMockK
    lateinit var getBuyerShowcaseList: GetShopShowcaseListBuyerUseCase

    @RelaxedMockK
    lateinit var getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShopShowcasePickerViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopShowcasePickerViewModel(
                getBuyerShowcaseList,
                getShopShowcaseTotalProductUseCase,
                TestCoroutineDispatchers()
        )
    }

    @Test
    fun `given showcase list as a buyer when shopid and isowner is provided`() {
        runBlocking {
            val shopId = "123456"
            val isOwner = false

            coEvery {
                getBuyerShowcaseList.executeOnBackground()
            } returns ShopShowcaseListBuyerResponse()

            viewModel.getShopShowcaseListAsBuyer(shopId = shopId, isOwner = isOwner)

            coVerify {
                getBuyerShowcaseList.executeOnBackground()
            }

            val expectedResponse = Success(ShopShowcaseListBuyerResponse())
            val actualResponse = viewModel.getListBuyerShopShowcaseResponse.value as Success<ShopShowcaseListBuyerResponse>
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `when get total product should return success`() {
        runBlocking {
            val shopId = "123456"
            val page = 1
            val perPage = 15
            val fkeyword = ""
            val sort = 0
            val fmenu = "etalase"

            coEvery {
                getShopShowcaseTotalProductUseCase.executeOnBackground()
            } returns GetShopProductsResponse()

            viewModel.getTotalProduct(
                    shopId = shopId,
                    page = page,
                    perPage = perPage,
                    sortId = sort,
                    etalase = fmenu,
                    search = fkeyword
            )

            coVerify {
                getShopShowcaseTotalProductUseCase.executeOnBackground()
            }

            val expectedResponse = Success(GetShopProductsResponse())
            val actualResponse = viewModel.getShopProductResponse.value as Success<GetShopProductsResponse>
            assertEquals(expectedResponse, actualResponse)
        }
    }

}