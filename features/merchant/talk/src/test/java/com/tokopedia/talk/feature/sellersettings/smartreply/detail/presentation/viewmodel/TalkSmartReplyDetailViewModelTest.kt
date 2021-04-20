package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.DiscussionSmartReplyMutationResult
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.DiscussionSetSmartReplySettingResponseWrapper
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.DiscussionSetSmartReplyTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.TalkSmartReplyDetailButtonState
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
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

        val expectedData = MessageErrorException(expectedResponse.discussionSetSmartReplySetting.reason)

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

        val expectedData = MessageErrorException(expectedResponse.discussionSetSmartReplyTemplate.reason)

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

    @Test
    fun `when updateIsSwitchActive should set buttonState accordingly`() {
        val isSwitchActive = true
        val expectedButtonState = TalkSmartReplyDetailButtonState(isSwitchActive = isSwitchActive)

        viewModel.updateIsSwitchActive(isSwitchActive)

        verifyButtonState(expectedButtonState)
    }

    @Test
    fun `when updateMessageChanged should set buttonState accordingly`() {
        val originalMessage = "Ready gan"
        val isReady = true
        val newMessage = "Ready gan, order aja"
        val expectedButtonState = TalkSmartReplyDetailButtonState(isReadyTextChanged = true)

        viewModel.messageReady = originalMessage
        viewModel.initMessages()
        viewModel.updateMessageChanged(newMessage, isReady)

        verifyButtonState(expectedButtonState)
    }

    @Test
    fun `when updateMessageChanged for not ready message should set buttonState accordingly`() {
        val originalMessageNotReady = "Lagi kosong gan"
        val isNotReady = false
        val newMessageNotReady = "Lagi kosong gan, entar kita update ya"
        val expectedButtonState = TalkSmartReplyDetailButtonState(isNotReadyTextChanged = true)

        viewModel.messageNotReady = originalMessageNotReady
        viewModel.initMessages()
        viewModel.updateMessageChanged(newMessageNotReady, isNotReady)

        verifyButtonState(expectedButtonState)
    }

    @Test
    fun `when getShopAvatar should get expected shop avatar`() {
        val expectedShopAvatar = "shopAvatar"

        onGetShopAvatar_thenReturn(expectedShopAvatar)

        Assert.assertEquals(expectedShopAvatar, viewModel.shopAvatar)
    }

    @Test
    fun `when getShopName should get expected shop name`() {
        val expectedShopName = "shopName"

        onGetShopName_thenReturn(expectedShopName)

        Assert.assertEquals(expectedShopName, viewModel.shopName)
    }

    @Test
    fun `when set isSmartReplyOn should get expected isSmartReplyOn`() {
        val expectedIsSmartReplyOn = true

        viewModel.isSmartReplyOn = expectedIsSmartReplyOn

        Assert.assertEquals(expectedIsSmartReplyOn, viewModel.isSmartReplyOn)
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

    private fun onGetShopName_thenReturn(shopName: String) {
        every { viewModel.shopName } returns shopName
    }

    private fun onGetShopAvatar_thenReturn(shopAvatar: String) {
        every { viewModel.shopAvatar } returns shopAvatar
    }

    private fun verifyButtonState(buttonState: TalkSmartReplyDetailButtonState) {
        viewModel.buttonState.verifyValueEquals(buttonState)
    }
}