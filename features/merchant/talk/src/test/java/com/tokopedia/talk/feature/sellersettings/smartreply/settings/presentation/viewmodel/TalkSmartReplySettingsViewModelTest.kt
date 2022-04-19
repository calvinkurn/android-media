package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel

import com.tokopedia.talk.feature.sellersettings.smartreply.settings.data.DiscussionGetSmartReplyResponseWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
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