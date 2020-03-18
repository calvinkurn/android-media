package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.InstantTaskExecutorRuleSpek
import com.tokopedia.util.TestCoroutineDispatcherProviderImpl
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.internal.matchers.Null
import javax.inject.Provider

class ShopHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        TestCoroutineDispatcherProviderImpl
    }

    private val getShopProductUseCase: GqlGetShopProductUseCase = mockk(relaxed = true)
    private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    private val removeWishListUseCase: RemoveWishListUseCase = mockk(relaxed = true)
    private val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val gqlCheckWishlistUseCaseProvider: Provider<GQLCheckWishlistUseCase> = mockk(relaxed = true)
    private lateinit var gqlCheckWishlistUseCase: GQLCheckWishlistUseCase

    private lateinit var viewModel: ShopHomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        gqlCheckWishlistUseCase = mockk(relaxed = true)
        viewModel = ShopHomeViewModel(
                userSessionInterface,
                getShopPageHomeLayoutUseCase,
                getShopProductUseCase,
                testCoroutineDispatcherProvider,
                addToCartUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                gqlCheckWishlistUseCaseProvider
        )
    }

    @Test
    fun `check whether response get home layout success is not null`() {

        val mockShopId = "1234"

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getShopPageHomeData(mockShopId)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.shopHomeLayoutData.value is Success)
        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.shopHomeLayoutData.value)
        assertNotNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether response get home layout error is null`() {

        val mockShopId = "1234"

        coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } throws Exception()
        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getShopPageHomeData(mockShopId)

        coVerify {
            getShopPageHomeLayoutUseCase.executeOnBackground()
            getShopProductUseCase.executeOnBackground()
        }

        assert(viewModel.shopHomeLayoutData.value is Fail)
        assertNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether response get lazy load product success is not null`() {
        val mockShopId = "1234"

        coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()

        viewModel.getNextProductList(mockShopId, 2)

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Success)
        assertNotNull(viewModel.productListData.value)
    }

    @Test
    fun `check whether response get lazy load product failed is null`() {
        val mockShopId = "1234"

        coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()

        viewModel.getNextProductList(mockShopId, anyInt())

        coVerify {
            getShopProductUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productListData.value is Fail)
    }

    @Test
    fun `check whether response get wish list status success is not null`() {

        coEvery { gqlCheckWishlistUseCase.executeOnBackground() } returns CheckWishlistResult.Response().checkWishlist

        viewModel.getWishlistStatus(anyList())

        coVerify { gqlCheckWishlistUseCase.executeOnBackground() }

        assertTrue(viewModel.checkWishlistData.value is Success)
        assertNotNull(viewModel.checkWishlistData.value)

    }

}