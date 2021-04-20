package com.tokopedia.shop.search.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.domain.interactor.GetSearchShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
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

    private val coroutineDispatcherProvider =CoroutineTestDispatchersProvider

    private val viewModel by lazy {
        ShopSearchProductViewModel(userSessionInterface, getSearchShopProductUseCase, coroutineDispatcherProvider)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when user search product should return success`() {
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
    fun `when user search product should return fail`(){
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

    @Test
    fun `when user is logged in and it is user's shop should return true`() {
        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            userSessionInterface.shopId
        } returns "10023"

        assertTrue(viewModel.isLoggedIn())
        assertTrue(viewModel.isMyShop("10023"))
    }
}