package com.tokopedia.product.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationItemDataModel
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 16/11/20
 */
class AddToCartDoneViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    private val viewModel by lazy {
        AddToCartDoneViewModel(userSessionInterface, addWishListUseCase,
                removeWishListUseCase, getRecommendationUseCase,
                addToCartUseCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `is usser logged in`() {
        every {
            userSessionInterface.isLoggedIn
        } returns true

        val isLoggedIn = viewModel.isLoggedIn()

        Assert.assertTrue(isLoggedIn)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `on error get recommendation`() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } throws Throwable()

        viewModel.getRecommendationProduct("123")

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertTrue(viewModel.recommendationProduct.value is Fail)
    }

    @Test
    fun `on success get recommendation`() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.getRecommendationProduct("123")

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertTrue((viewModel.recommendationProduct.value as Success).data.isNotEmpty())
    }

    @Test
    fun `on success get recommendation return empty list`() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns null

        viewModel.getRecommendationProduct("123")

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertTrue((viewModel.recommendationProduct.value as Success).data.isEmpty())
    }

    @Test
    fun `success add product to wishlist`() {
        val productId = "123"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        viewModel.addWishList(productId) { it, throwable ->
            Assert.assertEquals(it, true)
            Assert.assertEquals(throwable, null)
        }
    }

    @Test
    fun `error add product to wishlist`() {
        val productId = ""
        val errorMessage = "hehe"

        every {
            (addWishListUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList(errorMessage, productId)
        }

        viewModel.addWishList(productId) { it, throwable ->
            Assert.assertEquals(it, false)
            Assert.assertTrue(throwable?.message == errorMessage)
        }
    }

    @Test
    fun `success remove product to wishlist`() {
        val productId = "123"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        viewModel.removeWishList(productId) { it, throwable ->
            Assert.assertEquals(it, true)
            Assert.assertEquals(throwable, null)
        }
    }

    @Test
    fun `error remove product to wishlist`() {
        val productId = "123"
        val errorMessage = "hehe"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorRemoveWishlist(errorMessage, productId)
        }

        viewModel.removeWishList(productId) { it, throwable ->
            Assert.assertEquals(it, false)
            Assert.assertTrue(throwable?.message == errorMessage)
        }
    }

    @Test
    fun `add to cart success`() = runBlocking {
        val data = AddToCartDoneRecommendationItemDataModel(RecommendationItem(), true)
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        every {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(data)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }
}