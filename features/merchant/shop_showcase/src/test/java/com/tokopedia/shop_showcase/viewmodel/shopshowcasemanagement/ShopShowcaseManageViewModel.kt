package com.tokopedia.shop_showcase.viewmodel.shopshowcasemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop_showcase.coroutine.TestCoroutineDispatchers
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Matchers.*

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
                TestCoroutineDispatchers.main()
        )
    }

    @Test
    fun `given showcase list as a seller`() {
        mockkObject(GetShopShowcaseListBuyerUseCase)
        coEvery {
            getSellerShowcaseList.executeOnBackground()
        } returns ShopShowcaseListSellerResponse()
        viewModel.getShopShowcaseListAsSeller()
        coVerify {
            getSellerShowcaseList.executeOnBackground()
        }
        Assert.assertTrue(viewModel.getListSellerShopShowcaseResponse.value is Success)
    }

    @Test
    fun `given showcase list as a buyer when shopid and isowner is provided`() {
        mockkObject(GetShopShowcaseListBuyerUseCase)
        coEvery {
            getBuyerShowcaseList.executeOnBackground()
        } returns ShopShowcaseListBuyerResponse()
        viewModel.getShopShowcaseListAsBuyer(anyString(), anyBoolean())
        verify {
            GetShopShowcaseListBuyerUseCase.createRequestParam(anyString(), anyBoolean())
        }
        Assert.assertTrue(getBuyerShowcaseList.params.parameters.isNotEmpty())
        coVerify {
            getBuyerShowcaseList.executeOnBackground()
        }
        Assert.assertTrue(viewModel.getListBuyerShopShowcaseResponse.value is Success)
    }

    @Test
    fun `remove showcase when showcaseid is provided`() {
        mockkObject(DeleteShopShowcaseUseCase)
        coEvery {
            deleteShowcase.executeOnBackground()
        } returns DeleteShopShowcaseResponse()
        viewModel.removeSingleShopShowcase(anyString())
        verify {
            DeleteShopShowcaseUseCase.createRequestParam(anyString())
        }
        Assert.assertTrue(deleteShowcase.params.parameters.isNotEmpty())
        coVerify {
            deleteShowcase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.deleteShopShowcaseResponse.value is Success)
    }

    @Test
    fun `given success response when list of id is provided`() {
        mockkObject(ReorderShopShowcaseListUseCase)
        coEvery {
            reorderShowcase.executeOnBackground()
        } returns ReorderShopShowcaseResponse()
        val anyList: List<String> = anyList()
        viewModel.reorderShopShowcaseList(anyList)
        verify {
            ReorderShopShowcaseListUseCase.createRequestParam(anyList)
        }
        Assert.assertTrue(reorderShowcase.params.parameters.isNotEmpty())
        coEvery {
            reorderShowcase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.reoderShopShowcaseResponse.value is Success)
    }

    @Test
    fun `when get total product should return success`() {
        mockkObject(GetShopShowcaseTotalProductUseCase)
        coEvery {
            getShopShowcaseTotalProductUseCase.executeOnBackground()
        } returns GetShopProductsResponse()

        val page = 1
        val search = "baju"
        val perPage = 15
        val etalase = "pakaian"

        val paramInput = mapOf(
                "page" to page,
                "fkeyword" to search,
                "perPage" to perPage,
                "fmenu" to etalase,
                "sort" to ArgumentMatchers.anyInt()
        )

        viewModel.getTotalProduct(ArgumentMatchers.anyString(), page, perPage, ArgumentMatchers.anyInt(), etalase, search)

        verify {
            GetShopShowcaseTotalProductUseCase.createRequestParam(ArgumentMatchers.anyString(), paramInput)
        }

        Assert.assertTrue(reorderShowcase.params.parameters.isNotEmpty())

        coEvery {
            getShopShowcaseTotalProductUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.getShopProductResponse.value is Success)
    }

}