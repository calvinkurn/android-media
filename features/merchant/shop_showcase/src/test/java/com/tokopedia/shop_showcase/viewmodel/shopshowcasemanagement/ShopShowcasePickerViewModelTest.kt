package com.tokopedia.shop_showcase.viewmodel.shopshowcasemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseTotalProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcasePickerViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopShowcasePickerViewModelTest {

    @RelaxedMockK
    lateinit var getShopEtalaseUseCase: GetShopEtalaseUseCase

    @RelaxedMockK
    lateinit var getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase

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
                getShopShowcaseTotalProductUseCase,
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

}