package com.tokopedia.talk.feature.report

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.data.TalkMutationData
import com.tokopedia.talk.common.data.TalkMutationResponse
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class ReportTalkViewModelTest : ReportTalkViewModelTestFixture() {

    @Test
    fun `when reportComment success should execute expected usecase and get expected data`() {
        val expectedResponse = TalkReportCommentResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 1)))

        onTalkReportComment_thenReturn(expectedResponse)

        viewModel.reportComment(anyString(), anyString(), anyInt())

        verifyTalkReportCommentUseCaseExecuted()
        verifyReportCommentDataEquals(Success(expectedResponse))
    }

    @Test
    fun `when reportComment fail due to backend should execute expected usecase and fail with expected error`() {
        val expectedResponse = TalkReportCommentResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 0)))

        onTalkReportComment_thenReturn(expectedResponse)

        viewModel.reportComment(anyString(), anyString(), anyInt())

        verifyTalkReportCommentUseCaseExecuted()
        verifyReportCommentErrorEquals(Fail(MessageErrorException(expectedResponse.talkReportComment.messageError.firstOrNull())))
    }

    @Test
    fun `when reportComment fail due to network should execute expected usecase and fail with expected error`() {
        val expectedResponse = Throwable()

        onTalkReportCommentFail_thenReturn(expectedResponse)

        viewModel.reportComment(anyString(), anyString(), anyInt())

        verifyTalkReportCommentUseCaseExecuted()
        verifyReportCommentErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when reportTalk success should execute expected usecase and get expected data`() {
        val expectedResponse = TalkReportTalkResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 1)))

        onTalkReportTalk_thenReturn(expectedResponse)

        viewModel.reportTalk(anyString(), anyString(), anyInt())

        verifyTalkReportTalkUseCaseExecuted()
        verifyReportTalkDataEquals(Success(expectedResponse))
    }

    @Test
    fun `when reportTalk fail due to backend should execute expected usecase and fail with expected error`() {
        val expectedResponse = TalkReportTalkResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 0)))

        onTalkReportTalk_thenReturn(expectedResponse)

        viewModel.reportTalk(anyString(), anyString(), anyInt())

        verifyTalkReportTalkUseCaseExecuted()
        verifyReportTalkErrorEquals(Fail(MessageErrorException(expectedResponse.talkReportTalk.messageError.firstOrNull())))
    }

    @Test
    fun `when reportTalk fail due to network should execute expected usecase and fail with expected error`() {
        val expectedResponse = Throwable()

        onTalkReportTalkFail_thenReturn(expectedResponse)

        viewModel.reportTalk(anyString(), anyString(), anyInt())

        verifyTalkReportTalkUseCaseExecuted()
        verifyReportTalkErrorEquals(Fail(expectedResponse))
    }

    private fun onTalkReportComment_thenReturn(talkReportCommentResponseWrapper: TalkReportCommentResponseWrapper) {
        coEvery { talkReportCommentUseCase.executeOnBackground() } returns talkReportCommentResponseWrapper
    }

    private fun onTalkReportCommentFail_thenReturn(throwable: Throwable) {
        coEvery { talkReportCommentUseCase.executeOnBackground() } throws throwable
    }

    private fun onTalkReportTalk_thenReturn(talkReportTalkResponseWrapper: TalkReportTalkResponseWrapper) {
        coEvery { talkReportTalkUseCase.executeOnBackground() } returns talkReportTalkResponseWrapper
    }

    private fun onTalkReportTalkFail_thenReturn(throwable: Throwable) {
        coEvery { talkReportTalkUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyTalkReportCommentUseCaseExecuted() {
        coVerify { talkReportCommentUseCase.executeOnBackground() }
    }

    private fun verifyTalkReportTalkUseCaseExecuted() {
        coVerify { talkReportTalkUseCase.executeOnBackground() }
    }

    private fun verifyReportCommentDataEquals(expectedResponse: Success<TalkReportCommentResponseWrapper>) {
        viewModel.reportCommentResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyReportCommentErrorEquals(expectedResponse: Fail) {
        viewModel.reportCommentResult.verifyErrorEquals(expectedResponse)
    }

    private fun verifyReportTalkDataEquals(expectedResponse: Success<TalkReportTalkResponseWrapper>) {
        viewModel.reportTalkResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyReportTalkErrorEquals(expectedResponse: Fail) {
        viewModel.reportTalkResult.verifyErrorEquals(expectedResponse)
    }

}