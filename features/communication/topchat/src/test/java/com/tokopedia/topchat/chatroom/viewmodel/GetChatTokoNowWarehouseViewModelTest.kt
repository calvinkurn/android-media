package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test

class GetChatTokoNowWarehouseViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_get_response_when_success_get_warehouse_id() {
        //Given
        val expectedWarehouseId = "testWarehouse123"
        val expectedResponse = ChatTokoNowWarehouseResponse().also {
            it.chatTokoNowWarehouse.warehouseId = expectedWarehouseId
        }
        coEvery {
            getChatTokoNowWarehouseUseCase(any())
        } returns flow {
            emit(expectedResponse)
        }

        //When
        viewModel.adjustInterlocutorWarehouseId(testMessageId)

        //Then
        Assert.assertEquals(
            expectedWarehouseId,
            viewModel.attachProductWarehouseId
        )
    }

    @Test
    fun should_get_error_when_fail_to_get_warehouse_id() {
        //Given
        coEvery {
            getChatTokoNowWarehouseUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.adjustInterlocutorWarehouseId(testMessageId)

        //Then
        Assert.assertEquals(
            "0",
            viewModel.attachProductWarehouseId
        )
    }
}