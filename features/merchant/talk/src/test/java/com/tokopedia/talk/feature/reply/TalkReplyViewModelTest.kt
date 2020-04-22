package com.tokopedia.talk.feature.reply

import android.accounts.NetworkErrorException
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteComment
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResultData
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalk
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResultData
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalk
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResultData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
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

    @Test
    fun `when deleteTalk should execute expected use case and get expected data`() {
        val response = TalkDeleteTalkResponseWrapper(TalkDeleteTalk(data = TalkDeleteTalkResultData(isSuccess = 1)))
        val questionId = "148148"

        onTalkDeleteTalk_thenReturn(response)

        viewModel.deleteTalk(questionId)

        val expectedResponse = Success(response)

        verifyTalkDeleteTalkUseCaseExecuted()
        verifyTalkDeleteTalkDataEquals(expectedResponse)
    }

    @Test
    fun `when deleteTalk fails due to backend should execute expected use case and fail with expected error`() {
        val response = TalkDeleteTalkResponseWrapper(TalkDeleteTalk(messageError = listOf("Some Error"), data = TalkDeleteTalkResultData(isSuccess = 0)))
        val questionId = "148148"

        onTalkDeleteTalk_thenReturn(response)

        viewModel.deleteTalk(questionId)

        val expectedResponse = Fail(Throwable(response.talkDeleteTalk.messageError.first()))

        verifyTalkDeleteTalkUseCaseExecuted()
        verifyTalkDeleteTalkErrorEquals(expectedResponse)
    }

    @Test
    fun `when deleteTalk fails due to network should execute expected use case and fail with expected error`() {
        val questionId = "148148"
        val exception = NetworkErrorException()

        onTalkDeleteTalkFail_thenReturn(exception)

        viewModel.deleteTalk(questionId)

        val expectedResponse = Fail(exception)

        verifyTalkDeleteTalkUseCaseExecuted()
        verifyTalkDeleteTalkErrorEquals(expectedResponse)
    }

    @Test
    fun `when deleteComment should execute expected use case and get expected data`() {
        val response = TalkDeleteCommentResponseWrapper(TalkDeleteComment(data = TalkDeleteCommentResultData(isSuccess = 1)))
        val questionId = "148148"
        val commentId = "19219314"

        onTalkDeleteComment_thenReturn(response)

        viewModel.deleteComment(questionId, commentId)

        val expectedResponse = Success(response)

        verifyTalkDeleteCommentUseCaseExecuted()
        verifyTalkDeleteCommentDataEquals(expectedResponse)
    }

    @Test
    fun `when deleteComment fails due to backend should execute expected use case and fail with expected error`() {
        val response = TalkDeleteCommentResponseWrapper(TalkDeleteComment(messageError = listOf("Some Error"), data = TalkDeleteCommentResultData(isSuccess = 0)))
        val questionId = "148148"
        val commentId = "19219314"

        onTalkDeleteComment_thenReturn(response)

        viewModel.deleteComment(questionId, commentId)

        val expectedResponse = Fail(Throwable(response.talkDeleteComment.messageError.first()))

        verifyTalkDeleteCommentUseCaseExecuted()
        verifyTalkDeleteCommentErrorEquals(expectedResponse)
    }

    @Test
    fun `when deleteComment fails due to network should execute expected use case and fail with expected error`() {
        val questionId = "148148"
        val commentId = "19219314"
        val exception = NetworkErrorException()

        onTalkDeleteCommentFail_thenReturn(exception)

        viewModel.deleteComment(questionId, commentId)

        val expectedResponse = Fail(exception)

        verifyTalkDeleteCommentUseCaseExecuted()
        verifyTalkDeleteCommentErrorEquals(expectedResponse)
    }

    @Test
    fun `when setAttachedProducts should set attachedProducts to expected value`() {
        val attachedProducts = mutableListOf(AttachedProduct(productId = "1"), AttachedProduct(productId = "2"))

        viewModel.setAttachedProducts(attachedProducts)

        verifyAttachedProductsEqual(attachedProducts)
    }

    @Test
    fun `when removeAttachedProduct should remove specified attachedProduct from attachedProducts`() {
        val attachedProducts = mutableListOf(AttachedProduct(productId = "1"), AttachedProduct(productId = "2"))

        viewModel.setAttachedProducts(attachedProducts)
        viewModel.removeAttachedProduct("1")

        val expectedAttachedProduct = mutableListOf(AttachedProduct(productId = "2"))
        verifyAttachedProductsEqual(expectedAttachedProduct)
    }

    @Test
    fun `when set IsFollowing should set isFollowing to expected value`() {
        val expected = true

        viewModel.setIsFollowing(expected)

        verifyIsFollowingEquals(expected)
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

    private fun onTalkDeleteTalk_thenReturn(talkDeleteTalkResponseWrapper: TalkDeleteTalkResponseWrapper) {
        coEvery { talkDeleteTalkUseCase.executeOnBackground() } returns talkDeleteTalkResponseWrapper
    }

    private fun onTalkDeleteTalkFail_thenReturn(exception: Exception) {
        coEvery { talkDeleteTalkUseCase.executeOnBackground() } throws exception
    }

    private fun onTalkDeleteComment_thenReturn(talkDeleteCommentResponseWrapper: TalkDeleteCommentResponseWrapper) {
        coEvery { talkDeleteCommentUseCase.executeOnBackground() } returns talkDeleteCommentResponseWrapper
    }

    private fun onTalkDeleteCommentFail_thenReturn(exception: Exception) {
        coEvery { talkDeleteCommentUseCase.executeOnBackground() } throws exception
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

    private fun verifyTalkDeleteTalkDataEquals(expectedResponse: Success<TalkDeleteTalkResponseWrapper>) {
        viewModel.deleteTalkResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyTalkDeleteTalkErrorEquals(expectedResponse: Fail) {
        viewModel.deleteTalkResult.verifyErrorEquals(expectedResponse)
    }

    private fun verifyTalkDeleteCommentDataEquals(expectedResponse: Success<TalkDeleteCommentResponseWrapper>) {
        viewModel.deleteCommentResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyTalkDeleteCommentErrorEquals(expectedResponse: Fail) {
        viewModel.deleteCommentResult.verifyErrorEquals(expectedResponse)
    }

    private fun verifyIsFollowingEquals(isFollowing: Boolean) {
        assertEquals(viewModel.getIsFollowing(), isFollowing)
    }

    private fun verifyAttachedProductsEqual(attachedProducts: MutableList<AttachedProduct>) {
        viewModel.attachedProducts.verifyValueEquals(attachedProducts)
    }
}