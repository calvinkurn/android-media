package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel

import com.tokopedia.talk.feature.inbox.data.SmartReplyTalkDecommissionConfig
import com.tokopedia.talk.feature.inbox.data.TickerConfig
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.data.DiscussionGetSmartReplyResponseWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class TalkSmartReplySettingsViewModelTest : TalkSmartReplySettingsViewModelTestFixture() {

    @Test
    fun `when getSmartReplyData success should execute expected usecase and update livedata value accordingly`() {
        val expectedResponse = DiscussionGetSmartReplyResponseWrapper()

        onGetSmartReplyDataSuccess_thenReturn(expectedResponse)

        viewModel.getSmartReplyData()

        verifyDiscussionGetSmartReplyUseCaseCalled()
        viewModel.smartReplyData.verifySuccessEquals(Success(expectedResponse.discussionGetSmartReply))
    }

    @Test
    fun `when getSmartReplyData fail should execute expected usecase and update livedata value accordingly`() {
        val expectedResponse = Throwable()

        onGetSmartReplyDataFail_thenReturn(expectedResponse)

        viewModel.getSmartReplyData()

        verifyDiscussionGetSmartReplyUseCaseCalled()
        viewModel.smartReplyData.verifyErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `getSmartReplyDecommissionConfig should success map config from remote config`() {
        val expected = SmartReplyTalkDecommissionConfig.SmartReplyPage(
            isSmartReviewDisabled = true,
            tickerConfig = TickerConfig(title = "Dummy Title", text = "Dummy text")
        )
        val partialRemoteConfigJson = """
            {
                "smart_reply_page": {
                    "is_smart_review_disabled": true,
                    "ticker_config": {
                        "title": "Dummy Title",
                        "text": "Dummy text"
                    }
                }
            }
        """.trimIndent()

        every {
            firebaseRemoteConfig.getString("android_seller_app_smart_reply_talk_decommission_config")
        } returns partialRemoteConfigJson

        Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
    }

    @Test
    fun `getSmartReplyDecommissionConfig should set smartReplyDecommissionConfig default config when JSON mapping is error`() {
        val expected = SmartReplyTalkDecommissionConfig.SmartReplyPage(
            isSmartReviewDisabled = false,
            tickerConfig = TickerConfig(title = "", text = "")
        )
        val partialRemoteConfigJson = "this is invalid json"

        every {
            firebaseRemoteConfig.getString("android_seller_app_smart_reply_talk_decommission_config")
        } returns partialRemoteConfigJson

        Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
    }

    private fun verifyDiscussionGetSmartReplyUseCaseCalled() {
        coVerify { discussionGetSmartReplyUseCase.executeOnBackground() }
    }

    private fun onGetSmartReplyDataSuccess_thenReturn(expectedResponse: DiscussionGetSmartReplyResponseWrapper) {
        coEvery { discussionGetSmartReplyUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetSmartReplyDataFail_thenReturn(throwable: Throwable) {
        coEvery { discussionGetSmartReplyUseCase.executeOnBackground() } throws throwable
    }
}
