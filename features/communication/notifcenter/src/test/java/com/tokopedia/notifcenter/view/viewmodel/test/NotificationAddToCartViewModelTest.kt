package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class NotificationAddToCartViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `when success addProductToCart`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), onSuccess, {})

        // Then
        verify(exactly = 1) {
            onSuccess.invoke(successAtc.data)
        }
    }

    @Test
    fun `when success addProductToCart but status success is 0 and with error message`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val onError: (msg: String?) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel().apply {
            this.data.success = 0
            this.errorMessage = arrayListOf("Error message")
        }
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), onSuccess, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(any())
        }
    }

    @Test
    fun `when success addProductToCart but status success is 0 and with data message`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val onError: (msg: String?) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel().apply {
            this.data.success = 0
            this.data.message = arrayListOf("Data Message")
        }
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), onSuccess, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(any())
        }
    }

    @Test
    fun `when success addProductToCart but status success is 0 and no data & error message`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val onError: (msg: String?) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel().apply {
            this.data.success = 0
        }
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns successAtc

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), onSuccess, onError)

        // Then
        verify(exactly = 0) {
            onError.invoke(any())
        }
    }

    @Test
    fun `when error addProductToCart`() {
        // Given
        val onError: (msg: String?) -> Unit = mockk(relaxed = true)
        val errorAtc = getErrorAtcModel()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns errorAtc
        // When
        viewModel.addProductToCart(AddToCartRequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke("Gagal menambahkan produk")
        }
    }

    @Test
    fun `when error throwable addProductToCart`() {
        // Given
        val onError: (msg: String?) -> Unit = mockk(relaxed = true)
        val errorMsg = "Gagal menambahkan produk"
        coEvery {
            addToCartUseCase.executeOnBackground()
        } throws Throwable(errorMsg)

        // When
        viewModel.addProductToCart(AddToCartRequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(errorMsg)
        }
    }

    private fun getSuccessAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 1
        }
    }

    private fun getErrorAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 0
            data.message.add("Gagal menambahkan produk")
        }
    }
}
