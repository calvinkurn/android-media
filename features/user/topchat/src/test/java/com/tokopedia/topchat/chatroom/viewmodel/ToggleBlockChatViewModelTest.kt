package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.*
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class ToggleBlockChatViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_get_chat_setting_response_if_success_block_chat() {
        test_success_toggle_block(BlockActionType.BlockChat,
            isBlocked = true, isPromoBlocked = false)
    }

    @Test
    fun should_get_chat_setting_response_if_success_unblock_chat() {
        test_success_toggle_block(BlockActionType.UnblockChat,
            isBlocked = false, isPromoBlocked = false)
    }

    @Test
    fun should_get_chat_setting_response_if_success_block_promo() {
        test_success_toggle_block(
            BlockActionType.BlockPromo,
            isBlocked = false, isPromoBlocked = true,
            BroadcastSpamHandlerUiModel()
        )
    }

    @Test
    fun should_get_chat_setting_response_if_success_unblock_promo() {
        test_success_toggle_block(BlockActionType.UnblockPromo, isBlocked = false, isPromoBlocked = false)
    }

    @Test
    fun should_get_chat_setting_response_even_when_action_type_different() {
        test_success_toggle_block(TestBlockActionType.TestBlock,
            isBlocked = true, isPromoBlocked = false)
    }

    private fun test_success_toggle_block(
        blockActionType: ActionType,
        isBlocked: Boolean,
        isPromoBlocked: Boolean,
        element: BroadcastSpamHandlerUiModel? = null
    ) {
        //Given
        val expectedResponse = ChatSettingsResponse(
            ChatBlockResponse(true, ChatBlockStatus(
                isBlocked, isPromoBlocked, "test"
            ))
        )
        val expectedResult = WrapperChatSetting(
            blockActionType = blockActionType,
            response = Success(expectedResponse)
        ).apply {
            if(element != null) {
                this.element = element
            }
        }
        coEvery {
            chatToggleBlockChatUseCase(any())
        } returns expectedResponse

        //When
        viewModel.toggleBlockChatPromo(testMessageId, blockActionType)

        //Then
        Assert.assertEquals((expectedResult.response as Success).data,
            (viewModel.toggleBlock.value?.response as Success).data
        )
    }

    @Test
    fun should_throw_error_if_fail_block_chat() {
        test_error_toggle_block(BlockActionType.BlockChat)
    }

    @Test
    fun should_throw_error_if_fail_unblock_chat() {
        test_error_toggle_block(BlockActionType.UnblockChat)
    }

    @Test
    fun should_throw_error_if_fail_block_promo() {
        test_error_toggle_block(BlockActionType.BlockPromo)
    }

    @Test
    fun should_throw_error_if_fail_unblock_promo() {
        test_error_toggle_block(BlockActionType.UnblockPromo)
    }

    private fun test_error_toggle_block(blockActionType: BlockActionType) {
        //Given
        val expectedResult = WrapperChatSetting(
            blockActionType = blockActionType,
            response = Fail(expectedThrowable)
        )
        coEvery {
            chatToggleBlockChatUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.toggleBlockChatPromo(testMessageId, blockActionType)

        //Then
        Assert.assertEquals((expectedResult.response as Fail).throwable.message,
            (viewModel.toggleBlock.value?.response as Fail).throwable.message
        )
    }

    internal enum class TestBlockActionType: ActionType {
        TestBlock()
    }
}