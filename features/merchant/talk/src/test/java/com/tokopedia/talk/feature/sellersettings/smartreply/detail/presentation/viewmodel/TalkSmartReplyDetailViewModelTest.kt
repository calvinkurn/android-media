package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.DiscussionSmartReplyMutationResult
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.DiscussionSetSmartReplySettingResponseWrapper
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.DiscussionSetSmartReplyTemplateResponseWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class TalkSmartReplyDetailViewModelTest : TalkSmartReplyDetailViewModelTestFixture() {

    @Test
    fun `when setSmartReply success should execute expected usecase and update livedata with expected value`() {
        val expectedResponse = DiscussionSetSmartReplySettingResponseWrapper(discussionSetSmartReplySetting = DiscussionSmartReplyMutationResult(isSuccess = true))

        onSetSmartReply_thenReturn(expectedResponse)

        viewModel.setSmartReply()

        verifyDiscussionSetSmartReplySettingsUseCaseCalled()
        viewModel.setSmartReplyResult.verifySuccessEquals(Success(expectedResponse.discussionSetSmartReplySetting.reason))
    }

    @Test
    fun `when setSmartReply fail due to BE reason should execute expected usecase and update livedata with expected value`() {
        val expectedResponse = DiscussionSetSmartReplySettingResponseWrapper(discussionSetSmartReplySetting = DiscussionSmartReplyMutationResult(isSuccess = false, reason = "Upstream Down"))

        onSetSmartReply_thenReturn(expectedResponse)

        viewModel.setSmartReply()

        val expectedData = Throwable(expectedResponse.discussionSetSmartReplySetting.reason)

        verifyDiscussionSetSmartReplySettingsUseCaseCalled()
        viewModel.setSmartReplyResult.verifyErrorEquals(Fail(expectedData))
    }

    @Test
    fun `when setSmartReply fail should execute expected usecase and update livedata with expected value`() {
        val expectedResponse = Throwable()

        onSetSmartReplyError_thenReturn(expectedResponse)

        viewModel.setSmartReply()

        verifyDiscussionSetSmartReplySettingsUseCaseCalled()
        viewModel.setSmartReplyResult.verifyErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when setSmartReplyTemplate success should execute expected usecase and update livedata with expected value`() {
        val expectedResponse = DiscussionSetSmartReplyTemplateResponseWrapper(discussionSetSmartReplyTemplate =  DiscussionSmartReplyMutationResult(isSuccess = true))

        onSetSmartReplyTemplate_thenReturn(expectedResponse)

        viewModel.setSmartReplyTemplate()

        verifyDiscussionSetSmartReplyTemplateUseCaseCalled()
        viewModel.setSmartReplyResult.verifySuccessEquals(Success(expectedResponse.discussionSetSmartReplyTemplate.reason))
    }

    @Test
    fun `when setSmartReplyTemplate fail due to BE reason should execute expected usecase and update livedata with expected value`() {
        val expectedResponse = DiscussionSetSmartReplyTemplateResponseWrapper(discussionSetSmartReplyTemplate =  DiscussionSmartReplyMutationResult(isSuccess = false, reason = "Upstream down"))

        onSetSmartReplyTemplate_thenReturn(expectedResponse)

        viewModel.setSmartReplyTemplate()

        val expectedData = Throwable(expectedResponse.discussionSetSmartReplyTemplate.reason)

        verifyDiscussionSetSmartReplyTemplateUseCaseCalled()
        viewModel.setSmartReplyResult.verifyErrorEquals(Fail(expectedData))
    }

    @Test
    fun `when setSmartReplyTemplate fail should execute expected usecase and update livedata with expected value`() {
        val expectedResponse = Throwable()

        onSetSmartReplyTemplateError_thenReturn(expectedResponse)

        viewModel.setSmartReplyTemplate()

        verifyDiscussionSetSmartReplyTemplateUseCaseCalled()
        viewModel.setSmartReplyResult.verifyErrorEquals(Fail(expectedResponse))
    }

    private fun onSetSmartReply_thenReturn(expectedResponse: DiscussionSetSmartReplySettingResponseWrapper) {
        coEvery { discussionSetSmartReplySettingsUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onSetSmartReplyError_thenReturn(throwable: Throwable) {
        coEvery { discussionSetSmartReplySettingsUseCase.executeOnBackground() } throws throwable
    }

    private fun onSetSmartReplyTemplate_thenReturn(expectedResponse: DiscussionSetSmartReplyTemplateResponseWrapper) {
        coEvery { discussionSetSmartReplyTemplateUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onSetSmartReplyTemplateError_thenReturn(throwable: Throwable) {
        coEvery { discussionSetSmartReplyTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyDiscussionSetSmartReplySettingsUseCaseCalled() {
        coVerify { discussionSetSmartReplySettingsUseCase.executeOnBackground() }
    }

    private fun verifyDiscussionSetSmartReplyTemplateUseCaseCalled() {
        coVerify { discussionSetSmartReplyTemplateUseCase.executeOnBackground() }
    }
}