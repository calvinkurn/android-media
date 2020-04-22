package com.tokopedia.talk.feature.reply

import android.accounts.NetworkErrorException
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalk
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResultData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class TalkReplyViewModelTest : TalkReplyViewModelTestFixture() {

    @Test
    fun `when getDiscussionData should execute expected use case and get expected data`() {
        val response = DiscussionDataByQuestionIDResponseWrapper()
        val questionId = "148148"
        val shopId = "480749"

        onGetDiscussionData_thenReturn(response)

        viewModel.getDiscussionDataByQuestionID(questionId, shopId)

        val expectedResponse = Success(response)

        verifyGetDiscussionDataByQuestionIdUseCaseExecuted()
        verifyDiscussionDataEquals(expectedResponse)
    }

    @Test
    fun `when getDiscussionData fails should execute expected use case and fail with expected error`() {
        val questionId = "148148"
        val shopId = "480749"
        val exception = NetworkErrorException()

        onGetDiscussionDataFail_thenReturn(exception)

        viewModel.getDiscussionDataByQuestionID(questionId, shopId)

        verifyGetDiscussionDataByQuestionIdUseCaseExecuted()
        verifyDiscussionDataErrorEquals(Fail(exception))
    }

    @Test
    fun `when followUnfollowTalk should execute expected use case and get expected data`() {
        val response = TalkFollowUnfollowTalkResponseWrapper(TalkFollowUnfollowTalk(data = TalkFollowUnfollowTalkResultData(isSuccess = 1)))
        val questionId = 148148

        onTalkFollowUnfollowTalk_thenReturn(response)

        viewModel.followUnfollowTalk(questionId)

        val expectedResponse = Success(response)

        verifyTalkFollowUnfollowTalkUseCaseExecuted()
        verifyTalkFollowUnfollowTalkDataEquals(expectedResponse)
    }

    @Test
    fun `when followUnfollowTalk fails due to backend should execute expected use case and fail with get expected error`() {
        val response = TalkFollowUnfollowTalkResponseWrapper(TalkFollowUnfollowTalk(messageError = listOf("Some Error"), data = TalkFollowUnfollowTalkResultData(isSuccess = 0)))
        val questionId = 148148

        onTalkFollowUnfollowTalk_thenReturn(response)

        viewModel.followUnfollowTalk(questionId)

        val expectedResponse = Fail(Throwable(message = response.talkFollowUnfollowTalkResponse.messageError.first()))
        verifyTalkFollowUnfollowTalkUseCaseExecuted()
        verifyTalkFollowUnfollowTalkErrorEquals(expectedResponse)
    }

    @Test
    fun `when followUnfollowTalk fails due to network should execute expected use case and fail with get expected error`() {
        val questionId = 148148
        val exception = NetworkErrorException()

        onTalkFollowUnfollowTalkFail_thenReturn(exception)

        viewModel.followUnfollowTalk(questionId)

        val expectedResponse = Fail(exception)
        verifyTalkFollowUnfollowTalkUseCaseExecuted()
        verifyTalkFollowUnfollowTalkErrorEquals(expectedResponse)
    }

    private fun onGetDiscussionData_thenReturn(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper) {
        coEvery { discussionDataByQuestionIDUseCase.executeOnBackground() } returns discussionDataByQuestionIDResponseWrapper
    }

    private fun onGetDiscussionDataFail_thenReturn(exception: Exception) {
        coEvery { discussionDataByQuestionIDUseCase.executeOnBackground() } throws exception
    }

    private fun onTalkFollowUnfollowTalk_thenReturn(talkFollowUnfollowTalkResponseWrapper: TalkFollowUnfollowTalkResponseWrapper) {
        coEvery { talkFollowUnfollowTalkUseCase.executeOnBackground() } returns talkFollowUnfollowTalkResponseWrapper
    }

    private fun onTalkFollowUnfollowTalkFail_thenReturn(exception: Exception) {
        coEvery { talkFollowUnfollowTalkUseCase.executeOnBackground() } throws exception
    }

    private fun verifyGetDiscussionDataByQuestionIdUseCaseExecuted() {
        coVerify { discussionDataByQuestionIDUseCase.executeOnBackground() }
    }

    private fun verifyTalkFollowUnfollowTalkUseCaseExecuted() {
        coVerify { talkFollowUnfollowTalkUseCase.executeOnBackground() }
    }

    private fun verifyTalkDeleteTalkUseCaseExecuted() {
        coVerify { talkDeleteCommentUseCase.executeOnBackground() }
    }

    private fun verifyTalkDeleteCommentUseCaseExecuted() {
        coVerify { talkDeleteTalkUseCase.executeOnBackground() }
    }

    private fun verifyTalCreateNewCommentUseCaseExecuted() {
        coVerify { talkCreateNewCommentUseCase.executeOnBackground() }
    }

    private fun verifyDiscussionDataEquals(expectedResponse: Success<DiscussionDataByQuestionIDResponseWrapper>) {
        viewModel.discussionData.verifySuccessEquals(expectedResponse)
    }

    private fun verifyDiscussionDataErrorEquals(expectedResponse: Fail) {
        viewModel.discussionData.verifyErrorEquals(expectedResponse)
    }

    private fun verifyTalkFollowUnfollowTalkDataEquals(expectedResponse: Success<TalkFollowUnfollowTalkResponseWrapper>) {
        viewModel.followUnfollowResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyTalkFollowUnfollowTalkErrorEquals(expectedResponse: Fail) {
        viewModel.followUnfollowResult.verifyErrorEquals(expectedResponse)
    }
}