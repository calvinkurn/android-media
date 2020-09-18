package com.tokopedia.talk.feature.reply

import android.accounts.NetworkErrorException
import com.tokopedia.talk.common.data.TalkMutationData
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewComment
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteComment
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalk
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResultData
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalk
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResultData
import com.tokopedia.talk.util.verifyErrorEquals
import com.tokopedia.talk.util.verifySuccessEquals
import com.tokopedia.talk.util.verifyValueEquals
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
        val response = TalkDeleteTalkResponseWrapper(TalkDeleteTalk(data = TalkMutationData(isSuccess = 1)))
        val questionId = "148148"

        onTalkDeleteTalk_thenReturn(response)

        viewModel.deleteTalk(questionId)

        val expectedResponse = Success(response)

        verifyTalkDeleteTalkUseCaseExecuted()
        verifyTalkDeleteTalkDataEquals(expectedResponse)
    }

    @Test
    fun `when deleteTalk fails due to backend should execute expected use case and fail with expected error`() {
        val response = TalkDeleteTalkResponseWrapper(TalkDeleteTalk(messageError = listOf("Some Error"), data = TalkMutationData(isSuccess = 0)))
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
        val response = TalkDeleteCommentResponseWrapper(TalkDeleteComment(data = TalkMutationData(isSuccess = 1)))
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
        val response = TalkDeleteCommentResponseWrapper(TalkDeleteComment(messageError = listOf("Some Error"), data = TalkMutationData(isSuccess = 0)))
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
    fun `when createNewComment should execute expected use case and get expected data`() {
        val response = TalkCreateNewCommentResponseWrapper(TalkCreateNewComment(data = TalkMutationData(isSuccess = 1)))
        val questionId = "148148"
        val comment = "Barangnya ready ga ya?"

        onTalkCreateNewComment_thenReturn(response)

        viewModel.createNewComment(comment, questionId)

        val expectedResponse = Success(response)

        verifyTalkCreateNewCommentUseCaseExecuted()
        verifyTalkCreateNewCommentDataEquals(expectedResponse)
    }

    @Test
    fun `when createNewComment fails due to backend should execute expected use case and fail with expected error`() {
        val response = TalkCreateNewCommentResponseWrapper(TalkCreateNewComment(messageError = listOf("Some Error"), data = TalkMutationData(isSuccess = 0)))
        val questionId = "148148"
        val comment = "Barangnya ready ga ya?"

        onTalkCreateNewComment_thenReturn(response)

        viewModel.createNewComment(comment, questionId)

        val expectedResponse = Fail(Throwable(response.talkCreateNewComment.messageError.first()))

        verifyTalkCreateNewCommentUseCaseExecuted()
        verifyTalkCreateNewCommentErrorEquals(expectedResponse)
    }

    @Test
    fun `when createNewComment fails due to network should execute expected use case and fail with expected error`() {
        val questionId = "148148"
        val comment = "Barangnya ready ga ya?"
        val exception = NetworkErrorException()

        onTalkCreateNewCommentFail_thenReturn(exception)

        viewModel.createNewComment(questionId, comment)

        val expectedResponse = Fail(exception)

        verifyTalkCreateNewCommentUseCaseExecuted()
        verifyTalkCreateNewCommentErrorEquals(expectedResponse)
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
    fun `when data is null removeAttachedProduct should do nothing`() {
        viewModel.removeAttachedProduct("1")
    }

    @Test
    fun `when set IsFollowing should set isFollowing to expected value`() {
        val expected = true

        viewModel.setIsFollowing(expected)

        verifyIsFollowingEquals(expected)
    }

    @Test
    fun `when set IsMyShop should set to expected value`() {
        val expected = false
        val shopId = "13516"

        viewModel.setIsMyShop(shopId)

        assertEquals(expected, viewModel.isMyShop)
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

    private fun onTalkCreateNewComment_thenReturn(talkCreateNewCommentResponseWrapper: TalkCreateNewCommentResponseWrapper) {
        coEvery { talkCreateNewCommentUseCase.executeOnBackground() } returns talkCreateNewCommentResponseWrapper
    }

    private fun onTalkCreateNewCommentFail_thenReturn(exception: Exception) {
        coEvery { talkCreateNewCommentUseCase.executeOnBackground() } throws exception
    }

    private fun verifyGetDiscussionDataByQuestionIdUseCaseExecuted() {
        coVerify { discussionDataByQuestionIDUseCase.executeOnBackground() }
    }

    private fun verifyTalkFollowUnfollowTalkUseCaseExecuted() {
        coVerify { talkFollowUnfollowTalkUseCase.executeOnBackground() }
    }

    private fun verifyTalkDeleteCommentUseCaseExecuted() {
        coVerify { talkDeleteCommentUseCase.executeOnBackground() }
    }

    private fun verifyTalkDeleteTalkUseCaseExecuted() {
        coVerify { talkDeleteTalkUseCase.executeOnBackground() }
    }

    private fun verifyTalkCreateNewCommentUseCaseExecuted() {
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

    private fun verifyTalkCreateNewCommentDataEquals(expectedResponse: Success<TalkCreateNewCommentResponseWrapper>) {
        viewModel.createNewCommentResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyTalkCreateNewCommentErrorEquals(expectedResponse: Fail) {
        viewModel.createNewCommentResult.verifyErrorEquals(expectedResponse)
    }

    private fun verifyIsFollowingEquals(isFollowing: Boolean) {
        assertEquals(viewModel.getIsFollowing(), isFollowing)
    }

    private fun verifyAttachedProductsEqual(attachedProducts: MutableList<AttachedProduct>) {
        viewModel.attachedProducts.verifyValueEquals(attachedProducts)
    }
}