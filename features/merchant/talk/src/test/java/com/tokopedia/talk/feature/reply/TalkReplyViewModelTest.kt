package com.tokopedia.talk.feature.reply

import android.accounts.NetworkErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.data.TalkMutationData
import com.tokopedia.talk.common.data.TalkMutationResponse
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewComment
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteComment
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalk
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalk
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResultData
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkCommentNotFraudResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkCommentNotFraudSuccess
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkNotFraudResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.data.ChatTemplatesAll
import com.tokopedia.talk.feature.sellersettings.template.data.GetAllTemplateResponseWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString

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

        val expectedResponse = Fail(MessageErrorException(response.talkFollowUnfollowTalkResponse.messageError.first()))

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

        val expectedResponse = Fail(MessageErrorException(response.talkDeleteTalk.messageError.first()))

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

        val expectedResponse = Fail(MessageErrorException(response.talkDeleteComment.messageError.first()))

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

        val expectedResponse = Fail(MessageErrorException(response.talkCreateNewComment.messageError.first()))

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
    fun `when markCommentNotFraud success should execute expected usecase and get expected data`() {
        val expectedResponse = TalkMarkCommentNotFraudResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 1)))
        val commentId = anyString()

        onTalkMarkCommentNotFraud_thenReturn(expectedResponse)

        viewModel.markCommentNotFraud(commentId, anyString())

        verifyTalkMarkCommentNotFraudUseCaseExecuted()
        verifyMarkCommentNotFraudDataEquals(Success(TalkMarkCommentNotFraudSuccess(commentId)))
    }

    @Test
    fun `when markCommentNotFraud fail due to backend should execute expected usecase and fail with expected error`() {
        val expectedResponse = TalkMarkCommentNotFraudResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 0)))

        onTalkMarkCommentNotFraud_thenReturn(expectedResponse)

        viewModel.markCommentNotFraud(anyString(), anyString())

        verifyTalkMarkCommentNotFraudUseCaseExecuted()
        verifyMarkCommentNotFraudErrorEquals(Fail(MessageErrorException(expectedResponse.talkMarkCommentNotFraud.messageError.firstOrNull())))
    }

    @Test
    fun `when markCommentNotFraud fail due to network should execute expected usecase and fail with expected error`() {
        val expectedResponse = Throwable()

        onTalkMarkCommentNotFraudFail_thenReturn(expectedResponse)

        viewModel.markCommentNotFraud(anyString(), anyString())

        verifyTalkMarkCommentNotFraudUseCaseExecuted()
        verifyMarkCommentNotFraudErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when markNotFraud success should execute expected usecase and get expected data`() {
        val expectedResponse = TalkMarkNotFraudResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 1)))

        onTalkMarkNotFraud_thenReturn(expectedResponse)

        viewModel.markQuestionNotFraud(anyString())

        verifyTalkMarkNotFraudUseCaseExecuted()
        verifyMarkNotFraudDataEquals(Success(expectedResponse))
    }

    @Test
    fun `when markNotFraud fail due to backend should execute expected usecase and fail with expected error`() {
        val expectedResponse = TalkMarkNotFraudResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 0)))

        onTalkMarkNotFraud_thenReturn(expectedResponse)

        viewModel.markQuestionNotFraud(anyString())

        verifyTalkMarkNotFraudUseCaseExecuted()
        verifyMarkNotFraudErrorEquals(Fail(MessageErrorException(expectedResponse.talkMarkNotFraud.messageError.firstOrNull())))
    }

    @Test
    fun `when markNotFraud fail due to network should execute expected usecase and fail with expected error`() {
        val expectedResponse = Throwable()

        onTalkMarkNotFraudFail_thenReturn(expectedResponse)

        viewModel.markQuestionNotFraud(anyString())

        verifyTalkMarkNotFraudUseCaseExecuted()
        verifyMarkNotFraudErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when reportComment success should execute expected usecase and get expected data`() {
        val expectedResponse = TalkReportCommentResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 1)))

        onTalkReportComment_thenReturn(expectedResponse)

        viewModel.reportComment(anyString())

        verifyTalkReportCommentUseCaseExecuted()
        verifyReportCommentDataEquals(Success(expectedResponse))
    }

    @Test
    fun `when reportComment fail due to backend should execute expected usecase and fail with expected error`() {
        val expectedResponse = TalkReportCommentResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 0)))

        onTalkReportComment_thenReturn(expectedResponse)

        viewModel.reportComment(anyString())

        verifyTalkReportCommentUseCaseExecuted()
        verifyReportCommentErrorEquals(Fail(MessageErrorException(expectedResponse.talkReportComment.messageError.firstOrNull())))
    }

    @Test
    fun `when reportComment fail due to network should execute expected usecase and fail with expected error`() {
        val expectedResponse = Throwable()

        onTalkReportCommentFail_thenReturn(expectedResponse)

        viewModel.reportComment(anyString())

        verifyTalkReportCommentUseCaseExecuted()
        verifyReportCommentErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when reportTalk success should execute expected usecase and get expected data`() {
        val expectedResponse = TalkReportTalkResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 1)))

        onTalkReportTalk_thenReturn(expectedResponse)

        viewModel.reportTalk(anyString())

        verifyTalkReportTalkUseCaseExecuted()
        verifyReportTalkDataEquals(Success(expectedResponse))
    }

    @Test
    fun `when reportTalk fail due to backend should execute expected usecase and fail with expected error`() {
        val expectedResponse = TalkReportTalkResponseWrapper(TalkMutationResponse(data = TalkMutationData(isSuccess = 0)))

        onTalkReportTalk_thenReturn(expectedResponse)

        viewModel.reportTalk(anyString())

        verifyTalkReportTalkUseCaseExecuted()
        verifyReportTalkErrorEquals(Fail(MessageErrorException(expectedResponse.talkReportTalk.messageError.firstOrNull())))
    }

    @Test
    fun `when reportTalk fail due to network should execute expected usecase and fail with expected error`() {
        val expectedResponse = Throwable()

        onTalkReportTalkFail_thenReturn(expectedResponse)

        viewModel.reportTalk(anyString())

        verifyTalkReportTalkUseCaseExecuted()
        verifyReportTalkErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when getTemplateList success should execute expected use case`() {
        val expectedResponse = GetAllTemplateResponseWrapper()

        onSuccessGetTemplateList_thenReturn(expectedResponse)

        viewModel.getAllTemplates(ArgumentMatchers.anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
        verifyGetTemplateListSuccess(expectedResponse.chatTemplatesAll)
    }

    @Test
    fun `when getTemplateList fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailGetTemplateList_thenReturn(expectedResponse)

        viewModel.getAllTemplates(ArgumentMatchers.anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
        verifyGetTemplateListFail(expectedResponse)
    }

    @Test
    fun `when getUserId should get expected userId`() {
        val expectedUserId = "102131"

        onGetUserId_thenReturn(expectedUserId)

        Assert.assertEquals(expectedUserId, viewModel.userId)
    }

    @Test
    fun `when getProfilePicture should get expected profilePicture`() {
        val expectedProfilePicture = "profilePicture"

        onGetProfilePicture_thenReturn(expectedProfilePicture)

        Assert.assertEquals(expectedProfilePicture, viewModel.profilePicture)
    }

    @Test
    fun `when getShopAvatar should get expected shopAvatar`() {
        val expectedShopAvatar = "shopAvatar"

        onGetShopAvatar_thenReturn(expectedShopAvatar)

        Assert.assertEquals(expectedShopAvatar, viewModel.shopAvatar)
    }

    @Test
    fun `when getShopName should get expected shopName`() {
        val expectedShopName = "shopName"

        onGetShopName_thenReturn(expectedShopName)

        Assert.assertEquals(expectedShopName, viewModel.getShopName())
    }

    @Test
    fun `when set isMyShop should get expected value`() {
        val expectedIsMyShop = true

        viewModel.isMyShop = expectedIsMyShop

        Assert.assertEquals(expectedIsMyShop, viewModel.isMyShop)
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

    private fun onTalkMarkCommentNotFraud_thenReturn(talkMarkCommentNotFraudResponseWrapper: TalkMarkCommentNotFraudResponseWrapper) {
        coEvery { talkMarkCommentNotFraudUseCase.executeOnBackground() } returns talkMarkCommentNotFraudResponseWrapper
    }

    private fun onTalkMarkCommentNotFraudFail_thenReturn(throwable: Throwable) {
        coEvery { talkMarkCommentNotFraudUseCase.executeOnBackground() } throws throwable
    }

    private fun onTalkMarkNotFraud_thenReturn(talkMarkNotFraudResponseWrapper: TalkMarkNotFraudResponseWrapper) {
        coEvery { talkMarkNotFraudUseCase.executeOnBackground() } returns talkMarkNotFraudResponseWrapper
    }

    private fun onTalkMarkNotFraudFail_thenReturn(throwable: Throwable) {
        coEvery { talkMarkNotFraudUseCase.executeOnBackground() } throws throwable
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

    private fun onSuccessGetTemplateList_thenReturn(getAllTemplateResponseWrapper: GetAllTemplateResponseWrapper) {
        coEvery { getAllTemplatesUseCase.executeOnBackground() } returns getAllTemplateResponseWrapper
    }

    private fun onFailGetTemplateList_thenReturn(throwable: Throwable) {
        coEvery { getAllTemplatesUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetUserId_thenReturn(userId: String) {
        every { userSession.userId } returns userId
    }

    private fun onGetShopAvatar_thenReturn(shopAvatar: String) {
        every { userSession.shopAvatar } returns shopAvatar
    }

    private fun onGetProfilePicture_thenReturn(profilePicture: String) {
        every { userSession.profilePicture } returns profilePicture
    }

    private fun onGetShopName_thenReturn(shopName: String) {
        every { userSession.shopName } returns shopName
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

    private fun verifyTalkMarkNotFraudUseCaseExecuted() {
        coVerify { talkMarkNotFraudUseCase.executeOnBackground() }
    }

    private fun verifyTalkMarkCommentNotFraudUseCaseExecuted() {
        coVerify { talkMarkCommentNotFraudUseCase.executeOnBackground() }
    }

    private fun verifyTalkReportCommentUseCaseExecuted() {
        coVerify { talkReportCommentUseCase.executeOnBackground() }
    }

    private fun verifyTalkReportTalkUseCaseExecuted() {
        coVerify { talkReportTalkUseCase.executeOnBackground() }
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

    private fun verifyMarkCommentNotFraudDataEquals(expectedResponse: Success<TalkMarkCommentNotFraudSuccess>) {
        viewModel.markCommentNotFraudResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyMarkCommentNotFraudErrorEquals(expectedResponse: Fail) {
        viewModel.markCommentNotFraudResult.verifyErrorEquals(expectedResponse)
    }

    private fun verifyMarkNotFraudDataEquals(expectedResponse: Success<TalkMarkNotFraudResponseWrapper>) {
        viewModel.markNotFraudResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyMarkNotFraudErrorEquals(expectedResponse: Fail) {
        viewModel.markNotFraudResult.verifyErrorEquals(expectedResponse)
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

    private fun verifyIsFollowingEquals(isFollowing: Boolean) {
        assertEquals(viewModel.getIsFollowing(), isFollowing)
    }

    private fun verifyAttachedProductsEqual(attachedProducts: MutableList<AttachedProduct>) {
        viewModel.attachedProducts.verifyValueEquals(attachedProducts)
    }

    private fun verifyGetAllTemplatesUseCaseCalled() {
        coVerify { getAllTemplatesUseCase.executeOnBackground() }
    }

    private fun verifyGetTemplateListSuccess(chatTemplatesAll: ChatTemplatesAll) {
        viewModel.templateList.verifySuccessEquals(Success(chatTemplatesAll))
    }

    private fun verifyGetTemplateListFail(throwable: Throwable) {
        viewModel.templateList.verifyErrorEquals(Fail(throwable))
    }

}