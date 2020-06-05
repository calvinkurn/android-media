package com.tokopedia.shop_showcase.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.DeleteShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ReorderShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller.ShopShowcaseListSellerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.*
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcaseListViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ShopShowcaseListViewModel(
                getBuyerShowcaseList,
                getSellerShowcaseList,
                deleteShowcase,
                reorderShowcase,
                getShopShowcaseTotalProductUseCase,
                dispatchers
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
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

}