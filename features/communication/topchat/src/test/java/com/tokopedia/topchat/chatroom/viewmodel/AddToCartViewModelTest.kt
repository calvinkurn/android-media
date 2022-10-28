package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class AddToCartViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun `should_get_data_model_when_success_add_product_to_cart`() {
        //Given
        val expectedResult = AddToCartDataModel().apply {
            this.data = DataModel(success = 1)
        }
        val dummyAddToCartParam = AddToCartParam()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns expectedResult

        //When
        viewModel.addProductToCart(dummyAddToCartParam)

        //Then
        Assert.assertEquals(
            expectedResult.data,
            (viewModel.addToCart.value as Success).data.dataModel
        )
    }

    @Test
    fun `should_get_error_message_when_fail_to_add_product_to_cart`() {
        //Given
        val expectedErrorMessage = "Oops!"
        val expectedResult = AddToCartDataModel().apply {
            this.data = DataModel(success = 0)
            this.errorMessage = arrayListOf(expectedErrorMessage)
        }
        val dummyAddToCartParam = AddToCartParam()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } returns expectedResult

        //When
        viewModel.addProductToCart(dummyAddToCartParam)

        //Then
        Assert.assertEquals(
            expectedErrorMessage,
            (viewModel.addToCart.value as Fail).throwable.message
        )
    }

    @Test
    fun `should_get_exception_when_error_add_product_to_cart`() {
        //Given
        val expectedResult = Throwable("Oops!")
        val dummyAddToCartParam = AddToCartParam()
        coEvery {
            addToCartUseCase.executeOnBackground()
        } throws expectedResult

        //When
        viewModel.addProductToCart(dummyAddToCartParam)

        //Then
        Assert.assertEquals(
            expectedResult.message,
            (viewModel.addToCart.value as Fail).throwable.message
        )
    }
}