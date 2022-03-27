package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_recom.model.datamodel.RecomErrorResponse
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.viewmodel.InfiniteRecomViewModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by yfsx on 21/09/21.
 */
class TestInfiniteRecomViewModel {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val miniCartListSimplifiedUseCase = mockk<GetMiniCartListSimplifiedUseCase>(relaxed = true)
    private val updateCartUseCase = mockk<UpdateCartUseCase>(relaxed = true)
    private val deleteCartUseCase = mockk<DeleteCartUseCase>(relaxed = true)
    private val dispatcher = RecommendationDispatcherTest()

    private val viewModel: InfiniteRecomViewModel = spyk(InfiniteRecomViewModel(
            userSessionInterface = userSessionInterface,
            getRecommendationUseCase = Lazy { getRecommendationUseCase },
            addToCartUseCase = Lazy { addToCartUseCase },
            miniCartListSimplifiedUseCase = Lazy { miniCartListSimplifiedUseCase },
            updateCartUseCase = Lazy { updateCartUseCase },
            deleteCartUseCase = Lazy { deleteCartUseCase },
            dispatcher = dispatcher
    ))

    private val recommendation = RecommendationItem(productId = 1234)
    private val recomParam = GetRecommendationRequestParam()

    @Test
    fun `test success get recommendation first page should return success`() = runBlocking {
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
                RecommendationWidget(recommendationItemList = listOf(recommendation)))
        viewModel.getRecommendationFirstPage("", "", "", false)
        assert(viewModel.recommendationFirstLiveData.value != null)
        assert(viewModel.recommendationFirstLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `test get recommendation first page should return failed caused by empty recommendation`() = runBlocking {
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
                RecommendationWidget(recommendationItemList = listOf()))
        viewModel.getRecommendationFirstPage("", "", "", false)
        assert(viewModel.recommendationFirstLiveData.value == null)
    }

    @Test
    fun `test success get recommendation next page should return success`() = runBlocking {
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
                RecommendationWidget(recommendationItemList = listOf(recommendation)))
        viewModel.getRecommendationNextPage("", "", 2, "")
        assert(viewModel.recommendationNextLiveData.value != null)
        assert(viewModel.recommendationNextLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `test get recommendation next page should return success but empty recommendation`() = runBlocking {
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
                RecommendationWidget(recommendationItemList = listOf()))
        viewModel.getRecommendationNextPage("", "", 2, "")
        assert(viewModel.recommendationNextLiveData.value?.isEmpty() == true)
    }

    @Test
    fun `test add to cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, cartId = "12345"), status = "OK")
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }
        Assert.assertTrue(!atcResponseSuccess.isStatusError())
    }

    @Test
    fun `test add to cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal maning euy"))
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test add to cart non variant then return failed with throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } throws Throwable()

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test update cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        val response = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.atcRecomTokonow.value is Success)
    }

    @Test
    fun `test update cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        val response = UpdateCartV2Data(error = listOf("error nih gan"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test update cart non variant then return error throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test delete cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 0
        val response = RemoveFromCartData(status = "OK", data = com.tokopedia.cartcommon.data.response.deletecart.Data(message = listOf("sukses delete cart"), success = 1))
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.atcRecomTokonow.value is Success)
    }

    @Test
    fun `test delete cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 0
        val response = RemoveFromCartData(status = "ERROR", data = com.tokopedia.cartcommon.data.response.deletecart.Data(success = 0))
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test delete cart non variant then return error throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 0
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test success get recommendation first page tokonow should return success`() = runBlocking {
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
            RecommendationWidget(recommendationItemList = listOf(recommendation), isTokonow = true)
        )

        viewModel.getRecommendationFirstPage("", "", "", false)

        assert(viewModel.recommendationFirstLiveData.value != null)
        assert(viewModel.recommendationFirstLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `when atc recom non variant quantity changed with user not logged in should update atcRecomTokonowNonLogin value`() {
        every { userSessionInterface.isLoggedIn } returns false
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        assert(viewModel.atcRecomTokonowNonLogin.value == recomItem)
    }

    @Test
    fun `when atc recom non variant quantity changed with user logged in and new quantity is zero and recom not equals with item quantity should execute deleteRecomItemFromCart`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 1)
        val quantity = 0

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify(inverse = true) { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify(inverse = true) { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `when atc recom non variant quantity changed with user logged in and new quantity is not zero and not equals with recom item quantity should execute atcRecomNonVariant`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 0)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify(inverse = true) { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify(inverse = true) { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `when atc recom non variant quantity changed with user logged in and new quantity is not equals with recom item quantity and both are not zero should execute updateRecomCartNonVariant`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 3)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify(inverse = true) { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify(inverse = true) { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `when atc recom non variant quantity changed with user logged in and new quantity is equals with recom item quantity should do nothing`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 1)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify(inverse = true) { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify(inverse = true) { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify(inverse = true) { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `when get mini cart data then return response should update mini cart data and widget updater`() = runBlocking {
        val shopId = "123"
        val miniCartItems = listOf<MiniCartItem>(MiniCartItem(productId = "1"), MiniCartItem(productId = "2"))
        val miniCartData = MiniCartSimplifiedData(miniCartItems = miniCartItems)
        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns miniCartData

        viewModel.getMiniCart(shopId)

        verify { miniCartListSimplifiedUseCase.setParams(listOf(shopId)) }
        coVerify { miniCartListSimplifiedUseCase.executeOnBackground() }
        assert(viewModel.miniCartData.value == miniCartData.miniCartItems.associateBy { it.productId })
        assert(viewModel.minicartWidgetUpdater.value == miniCartData)
    }

    @Test
    fun `when get mini cart data then throw error should update mini cart error`() = runBlocking {
        val shopId = "123"
        val error = Throwable()
        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } throws error

        viewModel.getMiniCart(shopId)

        verify { miniCartListSimplifiedUseCase.setParams(listOf(shopId)) }
        coVerify { miniCartListSimplifiedUseCase.executeOnBackground() }
        assert(viewModel.minicartError.value == error)
    }

    @Test
    fun `when get recommendation first page then return empty result should update error recom data with empty first page` () = runBlocking {
        val pageName = "test"
        val productId = "123"
        val queryParam = "test"
        val forceRefresh = true

        coEvery { getRecommendationUseCase.getData(any()) } returns emptyList()

        viewModel.getRecommendationFirstPage(pageName, productId, queryParam, forceRefresh)

        assert(viewModel.errorGetRecomData.value != null)
        assert(viewModel.errorGetRecomData.value is RecomErrorResponse)
        assert(viewModel.errorGetRecomData.value?.isEmptyFirstPage == true)
        assert(viewModel.errorGetRecomData.value?.isForceRefreshAndError == forceRefresh)
    }

    @Test
    fun `when get recommendation first page then throw error should update error recom data with error first page` () = runBlocking {
        val pageName = "test"
        val productId = "123"
        val queryParam = "test"
        val forceRefresh = true
        val error = Throwable()

        coEvery { getRecommendationUseCase.getData(any()) } throws error

        viewModel.getRecommendationFirstPage(pageName, productId, queryParam, forceRefresh)

        assert(viewModel.errorGetRecomData.value != null)
        assert(viewModel.errorGetRecomData.value is RecomErrorResponse)
        assert(viewModel.errorGetRecomData.value?.isErrorFirstPage == true)
        assert(viewModel.errorGetRecomData.value?.isForceRefreshAndError == forceRefresh)
        assert(viewModel.errorGetRecomData.value?.errorThrowable == error)
    }

    @Test
    fun `when get recommendation next page then return empty result should update error recom data with empty next page` () = runBlocking {
        val pageName = "test"
        val productId = "123"
        val pageNumber = 2
        val queryParam = "test"

        coEvery { getRecommendationUseCase.getData(any()) } returns emptyList()

        viewModel.getRecommendationNextPage(pageName, productId, pageNumber, queryParam)

        assert(viewModel.errorGetRecomData.value != null)
        assert(viewModel.errorGetRecomData.value is RecomErrorResponse)
        assert(viewModel.errorGetRecomData.value?.isEmptyNextPage == true)
    }

    @Test
    fun `when get recommendation next page then throw error should update error recom data with page number` () = runBlocking {
        val pageName = "test"
        val productId = "123"
        val pageNumber = 2
        val queryParam = "test"
        val error = Throwable()

        coEvery { getRecommendationUseCase.getData(any()) } throws error

        viewModel.getRecommendationNextPage(pageName, productId, pageNumber, queryParam)

        assert(viewModel.errorGetRecomData.value != null)
        assert(viewModel.errorGetRecomData.value is RecomErrorResponse)
        assert(viewModel.errorGetRecomData.value?.errorThrowable == error)
        assert(viewModel.errorGetRecomData.value?.pageNumber == pageNumber)
    }
}