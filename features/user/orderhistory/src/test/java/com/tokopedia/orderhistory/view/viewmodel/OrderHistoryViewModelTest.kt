package com.tokopedia.orderhistory.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.orderhistory.FileUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

class OrderHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val productHistoryUseCase: GetProductOrderHistoryUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)

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
        viewModel = OrderHistoryViewModel(
                CoroutineTestDispatchersProvider,
                productHistoryUseCase,
                addWishListUseCase,
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

    @Test
    fun `when error addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorAtc = getErrorAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(errorAtc)

        // When
        viewModel.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke("Gagal menambahkan produk")
        }
    }

    @Test
    fun `when error throwable addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorMsg = "Gagal menambahkan produk"
        every {
            addToCartUseCase.createObservable(any())
        } throws IllegalStateException(errorMsg)

        // When
        viewModel.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(errorMsg)
        }
    }

    @Test
    fun `when success addProductToCart`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(successAtc)

        // When
        viewModel.addProductToCart(RequestParams(), onSuccess, {})

        // Then
        verify(exactly = 1) {
            onSuccess.invoke(successAtc.data)
        }
    }

    private fun getErrorAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 0
            data.message.add("Gagal menambahkan produk")
        }
    }

    private fun getSuccessAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 1
        }
    }
}