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
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItem)
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
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItem)
    }

    @Test
    fun `test update cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
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
        Assert.assertTrue(viewModel.refreshMiniCartDataTrigger.value == true)
    }

    @Test
    fun `test update cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
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
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItem)
    }

    @Test
    fun `test update cart non variant then return error throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItem)
    }

    @Test
    fun `test delete cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
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
        Assert.assertTrue(viewModel.deleteCartRecomTokonowSendTracker.value is Success)
        Assert.assertTrue(viewModel.refreshMiniCartDataTrigger.value == true)
    }

    @Test
    fun `test delete cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
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
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItem)
    }

    @Test
    fun `test delete cart non variant then return error throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 0
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
        Assert.assertTrue(viewModel.atcRecomTokonowResetCard.value == recomItem)
    }

    @Test
    fun `given recommendation first page tokonow when get recommendation first page with empty pageName product id then recommendation in view model should not empty`() = runBlocking {
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
            RecommendationWidget(recommendationItemList = listOf(recommendation), isTokonow = true)
        )

        viewModel.getRecommendationFirstPage("", "", "", false)

        assert(viewModel.recommendationFirstLiveData.value != null)
        assert(viewModel.recommendationFirstLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
    }

    @Test
    fun `given atc recom non variant quantity changed when user not logged in then atcRecomTokonowNonLogin in viewmodel should be updated with new recomItem`() {
        every { userSessionInterface.isLoggedIn } returns false
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        assert(viewModel.atcRecomTokonowNonLogin.value == recomItem)
    }

    @Test
    fun `given atc recom non variant quantity changed when user logged in with new quantity is zero and not equal to recom item quantity then deleteRecomItemFromCart should be executed`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 1)
        val quantity = 0

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify(inverse = true) { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify(inverse = true) { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `given atc recom non variant quantity changed when user logged in with new quantity is not zero and not equal to recom item quantity then atcRecomNonVariant should be executed`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 0)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify(inverse = true) { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify(inverse = true) { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `given atc recom non variant quantity changed when user logged in with new quantity is not equal to recom item quantity and both are not zero then updateRecomCartNonVariant should be executed`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 3)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify(inverse = true) { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify(inverse = true) { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `given atc recom non variant quantity changed when user logged in with new quantity is equal to recom item quantity then do nothing`() {
        every { userSessionInterface.isLoggedIn } returns true
        val recomItem = RecommendationItem(productId = 12345, shopId = 123, quantity = 1)
        val quantity = 1

        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)

        verify(inverse = true) { viewModel.deleteRecomItemFromCart(recomItem, any()) }
        verify(inverse = true) { viewModel.atcRecomNonVariant(recomItem, quantity) }
        verify(inverse = true) { viewModel.updateRecomCartNonVariant(recomItem, quantity, any()) }
    }

    @Test
    fun `given mini cart data when get mini cart data then miniCartData and minicartWidgetUpdater in viewmodel should be updated`() = runBlocking {
        val shopId = "123"
        val miniCartItems = hashMapOf<MiniCartItemKey, MiniCartItem>(MiniCartItemKey("1") to MiniCartItem.MiniCartItemProduct(productId = "1"), MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2"))
        val miniCartData = MiniCartSimplifiedData(miniCartItems = miniCartItems)
        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns miniCartData

        viewModel.getMiniCart(shopId)

        verify { miniCartListSimplifiedUseCase.setParams(listOf(shopId), any()) }
        coVerify { miniCartListSimplifiedUseCase.executeOnBackground() }
        assert(viewModel.miniCartData.value == miniCartData.miniCartItems)
        assert(viewModel.minicartWidgetUpdater.value == miniCartData)
    }

    @Test
    fun `given thrown error when get mini cart data then minicartError should be updated`() = runBlocking {
        val shopId = "123"
        val error = Throwable()
        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } throws error

        viewModel.getMiniCart(shopId)

        verify { miniCartListSimplifiedUseCase.setParams(listOf(shopId), any()) }
        coVerify { miniCartListSimplifiedUseCase.executeOnBackground() }
        assert(viewModel.minicartError.value == error)
    }

    @Test
    fun `given empty result when get recommendation first page then errorGetRecomData should be updated with empty first page set to true` () = runBlocking {
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
    fun `given thrown error when get recommendation first page then errorGetRecomData should be updated with error first page set to true` () = runBlocking {
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
    fun `given empty result when get recommendation next page then errorGetRecomData should be updated with empty next page set to true` () = runBlocking {
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
    fun `given thrown error when get recommendation next page then errorGetRecomData should be updated with page number being set` () = runBlocking {
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

    @Test
    fun `given add to cart non variant then return success should update atcRecomTokonow and send tracker`() {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, cartId = "12345", message = arrayListOf("message")), status = "OK")

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity)

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Success)
        Assert.assertTrue(viewModel.atcRecomTokonowSendTracker.value is Success)
        Assert.assertTrue(viewModel.refreshMiniCartDataTrigger.value == true)
    }

    @Test
    fun `given tokonow recom widget when get recommendation first page should map mini cart qty to recommendation`() = runBlocking {
        val parentId = 111L
        val productId1 = 1234L
        val productId2 = 2345L
        val miniCartQty = 1
        val recomQty = 2

        //Recom widget mock data
        val recomWidget = RecommendationWidget(recommendationItemList = listOf(
            RecommendationItem(productId = productId1, quantity = recomQty, currentQuantity = recomQty),
            RecommendationItem(productId = productId2, parentID = parentId, quantity = recomQty, currentQuantity = recomQty)
        ), isTokonow = true)

        //Mini cart mock data
        val miniCartItemProduct1 = MiniCartItem.MiniCartItemProduct(productId = productId1.toString(), quantity = miniCartQty)
        val miniCartItemProduct2 = MiniCartItem.MiniCartItemProduct(productId = productId2.toString(), productParentId = parentId.toString(), quantity = miniCartQty)
        val miniCartItemParent = MiniCartItem.MiniCartItemParentProduct(
            parentId = parentId.toString(),
            products = mapOf(Pair(MiniCartItemKey(parentId.toString(), MiniCartItemType.PARENT), miniCartItemProduct2)),
            totalQuantity = miniCartQty
        )
        val miniCart = mutableMapOf<MiniCartItemKey, MiniCartItem>().apply {
            put(MiniCartItemKey(parentId.toString(), MiniCartItemType.PARENT), miniCartItemParent)
            put(MiniCartItemKey(productId1.toString()), miniCartItemProduct1)
        }

        coEvery { getRecommendationUseCase.getData(any()) } returns listOf(
            recomWidget
        )
        coEvery { viewModel.miniCartData.value } returns miniCart

        viewModel.getRecommendationFirstPage("", "", "", false)

        assert(viewModel.recommendationFirstLiveData.value != null)
        assert(viewModel.recommendationFirstLiveData.value?.filterIsInstance<RecommendationItemDataModel>()?.isNotEmpty() == true)
        assert(viewModel.recommendationWidgetData.value == recomWidget)
        assert(viewModel.recommendationFirstLiveData.value?.single { it.productItem.productId == productId1 }?.productItem?.quantity == miniCartQty)
        assert(viewModel.recommendationFirstLiveData.value?.single { it.productItem.productId == productId1 }?.productItem?.currentQuantity == miniCartQty)
        assert(viewModel.recommendationFirstLiveData.value?.single { it.productItem.productId == productId2 }?.productItem?.quantity == miniCartQty)
        assert(viewModel.recommendationFirstLiveData.value?.single { it.productItem.productId == productId2 }?.productItem?.currentQuantity == miniCartQty)
    }
}