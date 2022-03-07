package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class GetChatRoomSettingViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_get_response_when_success_get_chat_room_setting() {
        //Given
        val expectedResult = RoomSettingResponse()
        coEvery {
            getChatRoomSettingUseCase(any())
        } returns expectedResult

        //When
        viewModel.loadChatRoomSettings(testMessageId)

        //Then
        Assert.assertEquals(expectedResult,
            (viewModel.chatRoomSetting.value as Success).data
        )
    }

    @Test
    fun should_invoke_error_when_error_get_chat_room_setting() {
        //Given
        coEvery {
            getChatRoomSettingUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.loadChatRoomSettings(testMessageId)

        //Then
        Assert.assertEquals(expectedThrowable.message,
            (viewModel.chatRoomSetting.value as Fail).throwable.message
        )
    }
}