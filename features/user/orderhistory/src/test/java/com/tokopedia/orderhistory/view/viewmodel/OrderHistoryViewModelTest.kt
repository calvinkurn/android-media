package com.tokopedia.orderhistory.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.orderhistory.FileUtil
import com.tokopedia.orderhistory.TestCoroutineContextDispatcher
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher: TestCoroutineContextDispatcher = TestCoroutineContextDispatcher()
    private val productHistoryUseCase: GetProductOrderHistoryUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)

    private lateinit var viewModel: OrderHistoryViewModel

    object Dummy {
        const val shopId = "123123"
        const val productId = "123432"
        const val userId = "45675"
        val successGetProductResponse: ChatHistoryProductResponse = FileUtil.parse(
                "/success_get_chat_history_product_response.json", ChatHistoryProductResponse::class.java
        )
        val errorGetProduct = Throwable()
    }

    @Before
    fun setup() {
        viewModel = OrderHistoryViewModel(testDispatcher, productHistoryUseCase, addWishListUseCase)
    }

    @Test
    fun `loadProductHistory when shop id is null`() {
        viewModel.loadProductHistory(null)
        verify { productHistoryUseCase wasNot Called }
    }

    @Test
    fun `loadProductHistory when shop id is not null`() {
        viewModel.loadProductHistory(Dummy.shopId)
        verify(exactly = 1) { productHistoryUseCase.loadProductHistory(Dummy.shopId, any(), any()) }
    }

    @Test
    fun `onSuccess loadProductHistory`() {
        every { productHistoryUseCase.loadProductHistory(Dummy.shopId, captureLambda(), any()) } answers {
            val onSuccess = lambda<(ChatHistoryProductResponse) -> Unit>()
            onSuccess.invoke(Dummy.successGetProductResponse)
        }

        val observer = mockk<Observer<Result<ChatHistoryProductResponse>>>(relaxed = true)
        viewModel.product.observeForever(observer)

        viewModel.loadProductHistory(Dummy.shopId)
        verify(exactly = 1) { observer.onChanged(Success(Dummy.successGetProductResponse)) }
    }

    @Test
    fun `onError loadProductHistory`() {
        every { productHistoryUseCase.loadProductHistory(Dummy.shopId, any(), captureLambda()) } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(Dummy.errorGetProduct)
        }

        val observer = mockk<Observer<Result<ChatHistoryProductResponse>>>(relaxed = true)
        viewModel.product.observeForever(observer)

        viewModel.loadProductHistory(Dummy.shopId)
        verify(exactly = 1) { observer.onChanged(Fail(Dummy.errorGetProduct)) }
    }

    @Test
    fun addToWishList() {
        val mockListener: WishListActionListener = mockk(relaxed = true)
        every { addWishListUseCase.createObservable(Dummy.productId, Dummy.userId, any()) } just Runs

        viewModel.addToWishList(Dummy.productId, Dummy.userId, mockListener)

        verify { addWishListUseCase.createObservable(Dummy.productId, Dummy.userId, mockListener) }
    }

}