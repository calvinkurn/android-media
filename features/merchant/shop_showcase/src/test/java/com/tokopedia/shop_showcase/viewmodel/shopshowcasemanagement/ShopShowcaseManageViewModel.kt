package com.tokopedia.shop_showcase.viewmodel.shopshowcasemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop_showcase.coroutines.TestCoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.DeleteShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ReorderShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller.ShopShowcaseListSellerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.*
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcaseListViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ShopShowcaseManageViewModel {

    @RelaxedMockK
    lateinit var getSellerShowcaseList: GetShopShowcaseListSellerUseCase

    @RelaxedMockK
    lateinit var getBuyerShowcaseList: GetShopShowcaseListBuyerUseCase

    @RelaxedMockK
    lateinit var deleteShowcase: DeleteShopShowcaseUseCase

    @RelaxedMockK
    lateinit var reorderShowcase: ReorderShopShowcaseListUseCase

    @RelaxedMockK
    lateinit var getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private lateinit var viewModel: ShopShowcaseListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopShowcaseListViewModel(
                getBuyerShowcaseList,
                getSellerShowcaseList,
                deleteShowcase,
                reorderShowcase,
                getShopShowcaseTotalProductUseCase,
                TestCoroutineDispatchers()
        )
    }

    @Test
    fun `given showcase list as a seller`() {
        runBlocking {
            coEvery {
                getSellerShowcaseList.executeOnBackground()
            } returns ShopShowcaseListSellerResponse()

            viewModel.getShopShowcaseListAsSeller()

            coVerify {
                getSellerShowcaseList.executeOnBackground()
            }

            val expectedResponse = Success(ShopShowcaseListSellerResponse())
            val actualResponse = viewModel.getListSellerShopShowcaseResponse.value as Success<ShopShowcaseListSellerResponse>
            assertEquals(expectedResponse, actualResponse)
        }
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
    fun `remove showcase when showcaseid is provided`() {
        runBlocking {
            val showcaseId = "123456"

            coEvery {
                deleteShowcase.executeOnBackground()
            } returns DeleteShopShowcaseResponse()

            viewModel.removeSingleShopShowcase(showcaseId)

            coVerify {
                deleteShowcase.executeOnBackground()
            }

            val expectedResponse = Success(DeleteShopShowcaseResponse())
            val actualResponse = viewModel.deleteShopShowcaseResponse.value as Success<DeleteShopShowcaseResponse>
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `given success response when list of id is provided`() {
        runBlocking {
            coEvery {
                reorderShowcase.executeOnBackground()
            } returns ReorderShopShowcaseResponse()

            val showcaseList: List<String> = listOf("123456", "987654")

            viewModel.reorderShopShowcaseList(showcaseList)

            coVerify {
                reorderShowcase.executeOnBackground()
            }

            val expectedResponse = Success(ReorderShopShowcaseResponse())
            val actualResponse = viewModel.reoderShopShowcaseResponse.value as Success<ReorderShopShowcaseResponse>
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