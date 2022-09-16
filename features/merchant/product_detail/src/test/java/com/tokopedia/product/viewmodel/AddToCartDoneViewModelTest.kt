package com.tokopedia.product.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationItemDataModel
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
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
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    private val viewModel by lazy {
        AddToCartDoneViewModel(userSessionInterface, addToWishlistV2UseCase, deleteWishlistV2UseCase,
                getRecommendationUseCase, addToCartUseCase, CoroutineTestDispatchersProvider)
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
            getRecommendationUseCase.getData(any())
        } throws Throwable()

        viewModel.getRecommendationProduct("123")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        Assert.assertTrue(viewModel.recommendationProduct.value is Fail)
    }

    @Test
    fun `on success get recommendation`() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOfRecom

        viewModel.getRecommendationProduct("123")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        Assert.assertTrue((viewModel.recommendationProduct.value as Success).data.isNotEmpty())
    }

    @Test
    fun `on success get recommendation return empty list`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf()

        viewModel.getRecommendationProduct("123")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        Assert.assertTrue((viewModel.recommendationProduct.value as Success).data.isEmpty())
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

    @Test
    fun `verify add to wishlistv2 returns success` () {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail` () {
        val productId = "123"
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns success`(){
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`(){
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }
}