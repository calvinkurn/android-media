package com.tokopedia.shop_showcase.viewmodel.shopshowcasemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.DeleteShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ReorderShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.*
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcaseListViewModel
import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.Product
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import rx.Observable

@ExperimentalCoroutinesApi
class ShopShowcaseManageViewModel {

    @RelaxedMockK
    lateinit var getShopEtalaseUseCase: GetShopEtalaseUseCase

    @RelaxedMockK
    lateinit var getBuyerShowcaseList: GetShopEtalaseByShopUseCase // GetShopShowcaseListBuyerUseCase

    @RelaxedMockK
    lateinit var deleteShowcase: DeleteShopShowcaseUseCase

    @RelaxedMockK
    lateinit var reorderShowcase: ReorderShopShowcaseListUseCase

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShopShowcaseListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopShowcaseListViewModel(
                getBuyerShowcaseList,
                getShopEtalaseUseCase,
                deleteShowcase,
                reorderShowcase,
                getProductListUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `given showcase list as a seller`() {
        runBlocking {
            coEvery {
                getShopEtalaseUseCase.executeOnBackground()
            } returns ShopShowcaseListSellerResponse()

            viewModel.getShopShowcaseListAsSeller()

            coVerify {
                getShopEtalaseUseCase.executeOnBackground()
            }

            val expectedResponse = Success(ShopShowcaseListSellerResponse())
            val actualResponse = viewModel.getListSellerShopShowcaseResponse.value as Success<ShopShowcaseListSellerResponse>
            assertEquals(expectedResponse, actualResponse)
        }
    }

    // Buyerview
    @Test
    fun `given showcase list as a buyer when shopid and isowner is provided`() {
        runBlocking {
            val shopId = "123456"
            val isOwner = false
            val index = 0
            val etalaseId = "2"
            val etalaseName = "Elektronik"
            val etalaseList = arrayListOf(ShopEtalaseModel(id = etalaseId, name = etalaseName))

            coEvery { getBuyerShowcaseList.createObservable(any()) } returns Observable.just(etalaseList)

            viewModel.getShopShowcaseListAsBuyer(
                    shopId = shopId,
                    isOwner = isOwner,
                    hideShowCaseGroup = false
            )

            val expectedResponse = Success(etalaseList)
            val actualResponse = viewModel.getListBuyerShopShowcaseResponse.value as Success<List<ShopEtalaseModel>>
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

            val productList = listOf<Product>()
            val productMapper = ProductMapper()
            val showCaseProductList = productMapper.mapToUIModel(productList)
            coEvery { getProductListUseCase.executeOnBackground() } returns showCaseProductList

            val productListFilter = GetProductListFilter(perPage = 1)
            viewModel.getTotalProduct(
                    shopId = "123",
                    filter = productListFilter
            )

            coVerify { getProductListUseCase.executeOnBackground() }

            val expectedResponse = Success(ArgumentMatchers.anyInt())
            val actualResponse = viewModel.shopTotalProduct.value as Success<Int>
            assertEquals(expectedResponse, actualResponse)
        }
    }

}