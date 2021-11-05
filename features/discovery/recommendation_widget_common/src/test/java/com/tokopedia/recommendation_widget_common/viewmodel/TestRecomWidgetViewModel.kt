package com.tokopedia.recommendation_widget_common.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecomErrorModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any

/**
 * Created by yfsx on 19/10/21.
 */
class TestRecomWidgetViewModel {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val miniCartListSimplifiedUseCase =
        mockk<GetMiniCartListSimplifiedUseCase>(relaxed = true)
    private val updateCartUseCase = mockk<UpdateCartUseCase>(relaxed = true)
    private val deleteCartUseCase = mockk<DeleteCartUseCase>(relaxed = true)
    private val getRecommendationFilterChips = mockk<GetRecommendationFilterChips>(relaxed = true)

    private val viewModel = RecomWidgetViewModel(
        userSession = userSessionInterface,
        getRecommendationUseCase = { getRecommendationUseCase },
        addToCartUseCase = { addToCartUseCase },
        miniCartListSimplifiedUseCase = { miniCartListSimplifiedUseCase },
        updateCartUseCase = { updateCartUseCase },
        deleteCartUseCase = { deleteCartUseCase },
        dispatcher = CoroutineTestDispatchersProvider,
        getRecommendationFilterChips = {getRecommendationFilterChips}
    )
    private val recomItem = RecommendationItem(productId = 1234, shopId = 123)
    private val pageName = "testPageName"
    val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
    val miniCartSimplifiedDataMock = MiniCartSimplifiedData(miniCartItems = listOf(miniCart))

    private val recomItemForAtc = RecommendationItem(
        productId = 1234,
        shopId = 123,
        quantity = 0,
        currentQuantity = 0,
        pageName = pageName
    )
    private val recomItemForUpdateDelete = RecommendationItem(
        productId = 1234,
        shopId = 123,
        quantity = 8,
        currentQuantity = 8,
        pageName = pageName
    )
    val updatedQuantityForAtc = 1
    val updatedQuantityForUpdate = 11
    val updatedQuantityForDelete = 0


    @Test
    fun `test given recom pagename and not tokonow page then check success get recommendation by pagename`() =
        runBlocking {
            coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
                RecommendationWidget(
                    recommendationItemList = listOf(recomItem),
                    isTokonow = false
                )
            )
            viewModel.loadRecommendationCarousel(pageName = pageName, isTokonow = false)
            val recomWidget = viewModel.getRecommendationLiveData.value
            val dataList = recomWidget?.recommendationItemList
            Assert.assertTrue(recomWidget?.isTokonow == false)
            Assert.assertTrue(dataList?.isNotEmpty() == true)
        }

    @Test
    fun `test given recom pagename and tokonow page then check success get recommendation by pagename`() =
        runBlocking {
            coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
                RecommendationWidget(
                    recommendationItemList = listOf(recomItem),
                    isTokonow = true
                )
            )
            viewModel.loadRecommendationCarousel(pageName = pageName, isTokonow = true)
            val recomWidget = viewModel.getRecommendationLiveData.value
            val dataList = recomWidget?.recommendationItemList
            Assert.assertTrue(recomWidget?.isTokonow == true)
            Assert.assertTrue(dataList?.isNotEmpty() == true)
        }

    @Test
    fun `test given recom pagename when usecase throw error then check error get recommendation`() =
        runBlocking {
            coEvery { getRecommendationUseCase.getData(any()) } throws Throwable()
            viewModel.loadRecommendationCarousel(pageName = pageName, isTokonow = true)
            Assert.assertTrue(viewModel.getRecommendationLiveData.value == null)
            Assert.assertTrue(viewModel.errorGetRecommendation.value != null)
            Assert.assertTrue(viewModel.errorGetRecommendation.value?.pageName == pageName)
        }


    @Test
    fun `test add to cart non variant then return success cart data`() = runBlockingTest {
        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(
                success = 1,
                cartId = "12345",
                message = arrayListOf("sukses broh")
            ), status = "OK"
        )

        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns atcResponseSuccess

        doCommonActionForATCRecom()

        Assert.assertTrue(!atcResponseSuccess.isStatusError())
        Assert.assertTrue(viewModel.atcRecomTokonowSendTracker.value is Success)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForAtc)
        Assert.assertTrue(viewModel.refreshMiniCartDataTriggerByPageName.value == recomItemForAtc.pageName)
    }

    @Test
    fun `test add to cart non variant then return failed with message`() = runBlockingTest {
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal maning euy")
        )

        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns atcResponseError

        doCommonActionForATCRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error != null)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForAtc)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItemForAtc)
    }

    @Test
    fun `test add to cart non variant then return failed with throwable`() = runBlockingTest {
        coEvery {
            addToCartUseCase.executeOnBackground()
        } throws Throwable()

        doCommonActionForATCRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error != null)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForAtc)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItemForAtc)
    }

    @Test
    fun `test update cart non variant then return success cart data`() = runBlockingTest {
        val response = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        doCommonActionForUpdateCartRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error == null)
    }

    @Test
    fun `test update cart non variant then return failed with message`() = runBlockingTest {
        val response = UpdateCartV2Data(error = listOf("error nih gan"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        doCommonActionForUpdateCartRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error != null)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItemForUpdateDelete)
    }

    @Test
    fun `test update cart non variant then return error throwable`() = runBlockingTest {
        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        doCommonActionForUpdateCartRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error != null)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForUpdateDelete)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItemForUpdateDelete)
    }

    @Test
    fun `test delete cart non variant then return success cart data`() = runBlockingTest {
        val response = RemoveFromCartData(
            status = "OK",
            data = com.tokopedia.cartcommon.data.response.deletecart.Data(
                message = listOf("sukses delete cart"),
                success = 1
            )
        )
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        doCommonActionForDeleteCartRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error == null)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForUpdateDelete)
    }

    @Test
    fun `test delete cart non variant then return failed with message`() = runBlockingTest {
        val response = RemoveFromCartData(
            status = "ERROR",
            data = com.tokopedia.cartcommon.data.response.deletecart.Data(success = 0)
        )
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        doCommonActionForDeleteCartRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error != null)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForUpdateDelete)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItemForUpdateDelete)
    }

    @Test
    fun `test delete cart non variant then return error throwable`() = runBlockingTest {
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable()

        doCommonActionForDeleteCartRecom()

        Assert.assertTrue(viewModel.atcRecomTokonow.value?.error != null)
        Assert.assertTrue(viewModel.atcRecomTokonow.value?.recomItem == recomItemForUpdateDelete)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItemForUpdateDelete)
    }

    private fun doCommonActionForATCRecom() {
        coEvery {
            userSessionInterface.isLoggedIn
        } returns true

        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns miniCartSimplifiedDataMock

        viewModel.getMiniCart("", "")
        viewModel.onAtcRecomNonVariantQuantityChanged(recomItemForAtc, updatedQuantityForAtc)

        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }
        coVerify {
            addToCartUseCase.executeOnBackground()
        }
    }


    private fun doCommonActionForUpdateCartRecom() {
        coEvery {
            userSessionInterface.isLoggedIn
        } returns true

        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns miniCartSimplifiedDataMock

        viewModel.getMiniCart("", "")
        viewModel.onAtcRecomNonVariantQuantityChanged(
            recomItemForUpdateDelete,
            updatedQuantityForUpdate
        )

        coVerify {
            updateCartUseCase.executeOnBackground()
        }
        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }
    }

    private fun doCommonActionForDeleteCartRecom() {
        coEvery {
            userSessionInterface.isLoggedIn
        } returns true

        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns miniCartSimplifiedDataMock

        viewModel.getMiniCart("", "")
        viewModel.onAtcRecomNonVariantQuantityChanged(
            recomItemForUpdateDelete,
            updatedQuantityForDelete
        )
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }
        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }
    }

}