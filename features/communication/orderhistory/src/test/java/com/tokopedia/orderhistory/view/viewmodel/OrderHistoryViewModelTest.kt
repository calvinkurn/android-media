package com.tokopedia.orderhistory.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.orderhistory.FileUtil
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testRule = UnconfinedTestRule()

    private val productHistoryUseCase: GetProductOrderHistoryUseCase = mockk(relaxed = true)
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)

    private lateinit var viewModel: OrderHistoryViewModel

    object Dummy {
        const val shopId = "123123"
        val successGetProductResponse: ChatHistoryProductResponse = FileUtil.parse(
            "/success_get_chat_history_product_response.json",
            ChatHistoryProductResponse::class.java
        )
        val errorGetProduct = Throwable()
    }

    @Before
    fun setup() {
        viewModel = OrderHistoryViewModel(
            CoroutineTestDispatchersProvider,
            productHistoryUseCase,
            addToWishlistV2UseCase,
            addToCartUseCase
        )
    }

    @Test
    fun `loadProductHistory when shop id is null`() {
        viewModel.loadProductHistory(null)
        verify { productHistoryUseCase wasNot Called }
    }

    @Test
    fun `loadProductHistory when shop id is not null`() {
        viewModel.loadProductHistory(Dummy.shopId)
        coVerify(exactly = 1) { productHistoryUseCase(any()) }
    }

    @Test
    fun `onSuccess loadProductHistory`() {
        coEvery { productHistoryUseCase(any()) } returns Dummy.successGetProductResponse

        val observer = mockk<Observer<Result<ChatHistoryProductResponse>>>(relaxed = true)
        viewModel.product.observeForever(observer)

        viewModel.loadProductHistory(Dummy.shopId)
        verify(exactly = 1) { observer.onChanged(Success(Dummy.successGetProductResponse)) }
    }

    @Test
    fun `onError loadProductHistory`() {
        coEvery { productHistoryUseCase(any()) } throws Dummy.errorGetProduct

        val observer = mockk<Observer<Result<ChatHistoryProductResponse>>>(relaxed = true)
        viewModel.product.observeForever(observer)

        viewModel.loadProductHistory(Dummy.shopId)
        verify(exactly = 1) { observer.onChanged(Fail(Dummy.errorGetProduct)) }
    }

    @Test
    fun `addToWishListV2 returns Success`() {
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)
        val productId = "1"
        val userId = "1234"

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)

        viewModel.addToWishListV2(productId, userId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail`() {
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishListV2(productId, "", mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, "") }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        verify { mockListener.onErrorAddWishList(mockThrowable, productId) }
    }

    @Test
    fun `when error addProductToCart`() {
        // Given
        val errorMsg = "Gagal menambahkan produk"
        val expectedThrowable = Throwable(errorMsg)
        coEvery {
            addToCartUseCase.executeOnBackground()
        } throws expectedThrowable

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), Product())

        // Then
        assertEquals(
            errorMsg,
            (viewModel.addToCartResult.value as Fail).throwable.message
        )
    }

    @Test
    fun `when error addProductToCart but no error message`() {
        // Given
        val expectedThrowable = Throwable()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } throws expectedThrowable

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), Product())

        // Then
        assertEquals(
            null,
            viewModel.addToCartResult.value
        )
    }

    @Test
    fun `when error throwable addProductToCart`() {
        // Given
        val errorMsg = "Gagal menambahkan produk"
        val expectedThrowable = Throwable(errorMsg)
        coEvery {
            addToCartUseCase.executeOnBackground()
        } throws expectedThrowable

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), Product())

        // Then
        assertEquals(
            errorMsg,
            (viewModel.addToCartResult.value as Fail).throwable.message
        )
    }

    @Test
    fun `when success addProductToCart`() {
        // Given
        val successAtc = getSuccessAtcModel()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), Product())

        // Then
        assertEquals(
            successAtc.data,
            (viewModel.addToCartResult.value as Success).data.first
        )
    }

    @Test
    fun `when success addProductToCart but success value not 1`() {
        // Given
        val errorMsg = "Error message"
        val successAtc = getSuccessAtcModel(successValue = 0, message = arrayListOf(errorMsg))
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), Product())

        // Then
        assertEquals(
            errorMsg,
            (viewModel.addToCartResult.value as Fail).throwable.message
        )
    }

    @Test
    fun `when success addProductToCart but success value not 1 and no error message`() {
        // Given
        val successAtc = getSuccessAtcModel(successValue = 0)
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), Product())

        // Then
        assertEquals(
            "",
            (viewModel.addToCartResult.value as Fail).throwable.message
        )
    }

    private fun getSuccessAtcModel(
        successValue: Int = 1,
        message: ArrayList<String> = arrayListOf()
    ): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = successValue
            data.message = message
        }
    }
}
