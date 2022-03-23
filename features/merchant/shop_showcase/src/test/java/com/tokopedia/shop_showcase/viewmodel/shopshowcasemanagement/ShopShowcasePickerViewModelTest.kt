package com.tokopedia.shop_showcase.viewmodel.shopshowcasemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcasePickerViewModel
import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.Product
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

@ExperimentalCoroutinesApi
class ShopShowcasePickerViewModelTest {

    @RelaxedMockK
    lateinit var getShopEtalaseUseCase: GetShopEtalaseUseCase

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @RelaxedMockK
    lateinit var createShopShowcaseUseCase: CreateShopShowcaseUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShopShowcasePickerViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopShowcasePickerViewModel(
                getShopEtalaseUseCase,
                getProductListUseCase,
                createShopShowcaseUseCase,
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


    @Test
    fun `Get Showcase list as Seller Fail Scenario`() {
        runBlocking {
            coEvery {
                getShopEtalaseUseCase.executeOnBackground()
            } throws Exception()

            viewModel.getShopShowcaseListAsSeller()

            coVerify {
                getShopEtalaseUseCase.executeOnBackground()
            }

            Assert.assertTrue(viewModel.getListSellerShopShowcaseResponse.value is Fail)
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
            viewModel.getTotalProducts(
                    shopId = "123",
                    filter = productListFilter
            )

            coVerify { getProductListUseCase.executeOnBackground() }

            val expectedResponse = Success(ArgumentMatchers.anyInt())
            val actualResponse = viewModel.shopTotalProduct.value as Success<Int>
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `Get total product Fail Scenario`() {
        runBlocking {
            coEvery { getProductListUseCase.executeOnBackground() } throws Exception()

            val productListFilter = GetProductListFilter(perPage = 1)
            viewModel.getTotalProducts(
                    shopId = "123",
                    filter = productListFilter
            )

            coVerify { getProductListUseCase.executeOnBackground() }
            assertTrue(viewModel.shopTotalProduct.value is Fail)
        }
    }

    @Test
    fun `when create shop showcase should return success`() {
        runBlocking {
            mockkObject(CreateShopShowcaseUseCase)
            coEvery { createShopShowcaseUseCase.executeOnBackground() } returns AddShopShowcaseResponse()

            viewModel.addShopShowcase(AddShopShowcaseParam())
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verify { CreateShopShowcaseUseCase.createRequestParams(AddShopShowcaseParam()) }
            coVerify { createShopShowcaseUseCase.executeOnBackground() }


            val expectedResponse = Success(AddShopShowcaseResponse())
            val actualResponse = viewModel.createShopShowcase.value as Success<AddShopShowcaseResponse>
            TestCase.assertTrue(viewModel.createShopShowcase.value is Success)
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `Create new showcase Fail Scenario`() {
        runBlocking {
            mockkObject(CreateShopShowcaseUseCase)
            coEvery { createShopShowcaseUseCase.executeOnBackground() } throws Exception()

            viewModel.addShopShowcase(AddShopShowcaseParam())
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verify { CreateShopShowcaseUseCase.createRequestParams(AddShopShowcaseParam()) }
            coVerify { createShopShowcaseUseCase.executeOnBackground() }

            assertTrue(viewModel.createShopShowcase.value is Fail)
        }
    }

}