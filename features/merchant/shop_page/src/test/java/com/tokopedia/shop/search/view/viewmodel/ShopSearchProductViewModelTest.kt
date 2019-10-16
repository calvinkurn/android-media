package com.tokopedia.shop.search.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.domain.interactor.GetSearchShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyString

@ExperimentalCoroutinesApi
class ShopSearchProductViewModelTest {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getSearchShopProductUseCase: GetSearchShopProductUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ShopSearchProductViewModel(userSessionInterface, getSearchShopProductUseCase, dispatcher)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testSearchShopProductSuccess() {
        mockkObject(GetSearchShopProductUseCase)
        coEvery {
            getSearchShopProductUseCase.executeOnBackground()
        }returns UniverseSearchResponse()
        viewModel.getSearchShopProduct(anyString(), anyString())
        verify {
            GetSearchShopProductUseCase.createRequestParam(anyInt(),anyString())
        }
        assertTrue(getSearchShopProductUseCase.requestParams.isNotEmpty())
        coVerify {
            getSearchShopProductUseCase.executeOnBackground()
        }
        assertTrue(viewModel.shopSearchProductResult.value is Success)
    }

    @Test
    fun testSearchShopProductError() {
        mockkObject(GetSearchShopProductUseCase)
        coEvery {
            getSearchShopProductUseCase.executeOnBackground()
        }throws Throwable()
        viewModel.getSearchShopProduct(anyString(), anyString())
        verify {
            GetSearchShopProductUseCase.createRequestParam(anyInt(),anyString())
        }
        assertTrue(getSearchShopProductUseCase.requestParams.isNotEmpty())
        coVerify {
            getSearchShopProductUseCase.executeOnBackground()
        }
        assertTrue(viewModel.shopSearchProductResult.value is Fail)
    }
}