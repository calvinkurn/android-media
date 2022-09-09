package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.every
import io.mockk.slot
import org.junit.Assert
import org.junit.Test

class SeamlessLoginViewModelTest: BaseTopChatViewModelTest() {

    private val testLiteUrl = "tokopedia.lite"
    private val slot = slot<SeamlessLoginSubscriber>()

    @Test
    fun should_get_url_when_success_seamless_login() {
        //Given
        val expectedUrl = "tokopedia.url"
        every {
            seamlessLoginUsecase.generateSeamlessUrl(testLiteUrl, capture(slot))
        } answers {
            val subs = slot.captured
            subs.onUrlGenerated(expectedUrl)
        }

        // When
        viewModel.onClickBannedProduct(testLiteUrl)

        // Then
        Assert.assertEquals(expectedUrl, viewModel.seamlessLogin.value)
    }

    @Test
    fun should_get_lite_url_when_fail_seamless_login() {
        //Given
        every {
            seamlessLoginUsecase.generateSeamlessUrl(testLiteUrl, capture(slot))
        } answers {
            val subs = slot.captured
            subs.onError(testLiteUrl)
        }

        // When
        viewModel.onClickBannedProduct(testLiteUrl)

        // Then
        Assert.assertEquals(testLiteUrl, viewModel.seamlessLogin.value)
    }
}