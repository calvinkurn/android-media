package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test

class GetChatListGroupStickerViewModelTest: BaseTopChatViewModelTest() {
    @Test
    fun should_give_list_group_sticker_when_success() {
        //Given
        val isSeller = false
        val expectedResponse = ChatListGroupStickerResponse()
        val expectedListDiff = emptyList<StickerGroup>()
        val expectedResult = Pair(expectedResponse, expectedListDiff)

        coEvery {
            getChatListGroupStickerUseCase(any())
        } returns flow {
            emit(expectedResult)
        }

        //When
        viewModel.getStickerGroupList(isSeller)

        //Then
        Assert.assertEquals(
            expectedResult,
            (viewModel.chatListGroupSticker.value as Success).data
        )
    }

    @Test
    fun should_give_error_when_failed_to_get_list_group_sticker() {
        //Given
        val isSeller = false

        coEvery {
            getChatListGroupStickerUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.getStickerGroupList(isSeller)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatListGroupSticker.value as Fail).throwable.message
        )
    }
}