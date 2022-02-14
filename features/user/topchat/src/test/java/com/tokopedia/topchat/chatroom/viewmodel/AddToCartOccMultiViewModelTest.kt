package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class AddToCartOccMultiViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_get_correct_product_id_when_successfully_occ() {
        //Given
        val expectedProductId = "123"
        val expectedResult = AddToCartOccMultiDataModel(
            status = "OK",
            data = AddToCartOccMultiData(
                success = 1
            )
        )
        val product = ProductAttachmentUiModel.Builder().withProductId(expectedProductId).build()
        coEvery {
            addToCartOccMultiUseCase.executeOnBackground()
        } returns expectedResult

        //When
        viewModel.occProduct(testUserId, product)

        //Then
        Assert.assertEquals(
            expectedProductId,
            (viewModel.occProduct.value as Success).data.productId)
    }

    @Test
    fun should_get_error_message_when_failed_to_occ() {
        //Given
        val expectedProductId = "123"
        val errorMessage = "Oops!"
        val expectedResult = AddToCartOccMultiDataModel(
            status = "ERROR",
            errorMessage = listOf(errorMessage)
        )
        val product = ProductAttachmentUiModel.Builder().withProductId(expectedProductId).build()
        coEvery {
            addToCartOccMultiUseCase.executeOnBackground()
        } returns expectedResult

        //When
        viewModel.occProduct(testUserId, product)

        //Then
        Assert.assertEquals(
            errorMessage,
            (viewModel.occProduct.value as Fail).throwable.message)
    }

    @Test
    fun should_get_throwable_when_failed_to_occ() {
        //Given
        val expectedProductId = "123"
        val errorMessage = "Oops!"
        val expectedResult = Throwable(errorMessage)
        val product = ProductAttachmentUiModel.Builder().withProductId(expectedProductId).build()
        coEvery {
            addToCartOccMultiUseCase.executeOnBackground()
        } throws expectedResult

        //When
        viewModel.occProduct(testUserId, product)

        //Then
        Assert.assertEquals(
            errorMessage,
            (viewModel.occProduct.value as Fail).throwable.message)
    }
}