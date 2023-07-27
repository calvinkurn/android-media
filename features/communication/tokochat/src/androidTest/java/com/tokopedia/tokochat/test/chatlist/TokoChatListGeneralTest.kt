package com.tokopedia.tokochat.test.chatlist

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.test.base.BaseTokoChatListTest
import org.junit.Test

@UiTest
class TokoChatListGeneralTest : BaseTokoChatListTest() {

    @Test
    fun should_show_chat_list_component() {
        // Given

        // When
        launchChatListActivity()

        // Then
        Thread.sleep(10000)
        // chat driver title
        // image profile
        // badge
        // first name
        // type order
        // thumbnail message
        // business name
        // timestamp
        // counter
    }

    @Test
    fun should_go_to_chat_room() {
        // Given

        // When

        // Then
    }

    @Test
    fun should_show_default_thumbnail_message() {
        // Given

        // When

        // Then
    }

    @Test
    fun should_show_99_plus_counter() {
        // Given

        // When

        // Then
    }

    @Test
    fun should_not_show_channel_when_expired() {
        // Given

        // When

        // Then
    }
}
