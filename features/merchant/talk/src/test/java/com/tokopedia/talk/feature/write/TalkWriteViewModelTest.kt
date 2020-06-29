package com.tokopedia.talk.feature.write

import android.accounts.NetworkErrorException
import com.tokopedia.talk.common.data.TalkMutationData
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.data.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.talk.feature.write.data.TalkCreateNewTalk
import com.tokopedia.talk.feature.write.data.TalkCreateNewTalkResponseWrapper
import com.tokopedia.talk.util.verifyErrorEquals
import com.tokopedia.talk.util.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import java.lang.Exception

class TalkWriteViewModelTest : TalkWriteViewModelTestFixture() {

    @Test
    fun `when getWriteFormData success should execute expected use case and get expected data`() {
        val productId = 0
        val expectedResponse = DiscussionGetWritingFormResponseWrapper()

        onGetDiscussionWritingForm_shouldReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyDiscussionGetWritingFormSuccessEquals(Success(expectedResponse.discussionGetWritingForm))
    }

    @Test
    fun `when getWriteFormData fail should execute expected use case and fail with expected error`() {
        val productId = 0
        val expectedError = NetworkErrorException()

        onGetDiscussionWritingFormError_shouldReturn(expectedError)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyDiscussionGetWritingErrorEquals(Fail(expectedError))
    }

    @Test
    fun `when submitWriteForm should execute expected use case and get expected data`() {
        val response = TalkCreateNewTalkResponseWrapper(TalkCreateNewTalk(talkMutationData = TalkMutationData(isSuccess = 1)))
        val text = "Ready stock?"

        onTalkCreateNewTalk_thenReturn(response)

        viewModel.submitWriteForm(text)

        val expectedResponse = Success(response.talkCreateNewTalk)

        verifyTalkCreateNewTalkUseCaseCalled()
        verifyTalkCreateNewTalkSuccessEquals(expectedResponse)
    }

    @Test
    fun `when submitWriteForm fails due to backend should execute expected use case and fail with expected error`() {
        val response = TalkCreateNewTalkResponseWrapper(TalkCreateNewTalk(messageError = listOf("Some Error"), talkMutationData = TalkMutationData(isSuccess = 0)))
        val text = "Ready stock?"

        onTalkCreateNewTalkFail_thenReturn(response)

        viewModel.submitWriteForm(text)

        val expectedResponse = Fail(Throwable(response.talkCreateNewTalk.messageError.first()))

        verifyTalkCreateNewTalkUseCaseCalled()
        verifyTalkDeleteTalkErrorEquals(expectedResponse)
    }

    @Test
    fun `when submitWriteForm fails due to network issue should execute expected use case and fail with expected error`() {
        val exception = NetworkErrorException()
        val text = "Ready stock?"

        onTalkCreateNewTalkNetworkFail_thenReturn(exception)

        viewModel.submitWriteForm(text)

        verifyTalkCreateNewTalkUseCaseCalled()
        verifyTalkDeleteTalkErrorEquals(Fail(exception))
    }

    private fun verifyDiscussionGetWritingFormUseCaseCalled() {
        coVerify { discussionGetWritingFormUseCase.executeOnBackground() }
    }

    private fun verifyTalkCreateNewTalkUseCaseCalled() {
        coVerify { talkCreateNewTalkUseCase.executeOnBackground() }
    }

    private fun onGetDiscussionWritingForm_shouldReturn(response: DiscussionGetWritingFormResponseWrapper) {
        coEvery { discussionGetWritingFormUseCase.executeOnBackground() } returns response
    }

    private fun onGetDiscussionWritingFormError_shouldReturn(exception: Exception) {
        coEvery { discussionGetWritingFormUseCase.executeOnBackground() } throws exception
    }

    private fun onTalkCreateNewTalk_thenReturn(talkCreateNewTalkResponseWrapper: TalkCreateNewTalkResponseWrapper) {
        coEvery { talkCreateNewTalkUseCase.executeOnBackground() } returns talkCreateNewTalkResponseWrapper
    }

    private fun onTalkCreateNewTalkFail_thenReturn(talkCreateNewTalkResponseWrapper: TalkCreateNewTalkResponseWrapper) {
        coEvery { talkCreateNewTalkUseCase.executeOnBackground() } returns talkCreateNewTalkResponseWrapper
    }

    private fun onTalkCreateNewTalkNetworkFail_thenReturn(exception: Exception) {
        coEvery { talkCreateNewTalkUseCase.executeOnBackground() } throws exception
    }

    private fun verifyTalkCreateNewTalkSuccessEquals(expectedResponse: Success<TalkCreateNewTalk>) {
        viewModel.talkCreateNewTalkResponse.verifySuccessEquals(expectedResponse)
    }

    private fun verifyTalkDeleteTalkErrorEquals(expectedError: Fail) {
        viewModel.talkCreateNewTalkResponse.verifyErrorEquals(expectedError)
    }

    private fun verifyDiscussionGetWritingFormSuccessEquals(expectedResponse: Success<DiscussionGetWritingForm>) {
        viewModel.writeFormData.verifySuccessEquals(expectedResponse)
    }

    private fun verifyDiscussionGetWritingErrorEquals(expectedError: Fail) {
        viewModel.writeFormData.verifyErrorEquals(expectedError)
    }
}