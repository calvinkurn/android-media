package com.tokopedia.reviewseller.reviewreply

import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyInsertResponse
import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyUpdateResponse
import com.tokopedia.reviewseller.feature.reviewreply.view.model.InsertReplyResponseUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.UpdateReplyResponseUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.*

class SellerReviewReplyViewModelTest: SellerReviewReplyViewModelTestFixture() {

    @Test
    fun `when get reply template list should return success`() {
        runBlocking {
            onReplyTemplateList_thenReturn()
            viewModel.getTemplateListReply(anyInt())

            verifySuccessReplyTemplateListUseCaseCalled()
            assertTrue(viewModel.reviewTemplate.value is Success)
            assertNotNull(viewModel.reviewTemplate.value)
        }
    }

    @Test
    fun `when insert review reply should return success`() {
        runBlocking {
            onInsertReviewReply_thenReturn()
            viewModel.insertReviewReply(anyInt(), anyInt(), anyInt(), anyString())

            verifySuccessInsertReviewReply()
            val expectedValue = Success(InsertReplyResponseUiModel(anyInt()))
            assertTrue(viewModel.insertReviewReply.value is Success)
            viewModel.insertReviewReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when update review reply should return success`() {
        runBlocking {
            onUpdateReviewReply_thenReturn()
            viewModel.updateReviewReply(anyInt(), anyString())

            verifyUpdateReviewReplyUseCaseCalled()
            val expectedValue = Success(UpdateReplyResponseUiModel(anyBoolean()))
            assertTrue(viewModel.updateReviewReply.value is Success)
            viewModel.updateReviewReply.verifyValueEquals(expectedValue)
        }
    }


    @Test
    fun `when get reply template list should return fail`() {
        runBlocking {
            val error = NullPointerException()
            onReplyTemplateList_thenError(error)

            viewModel.getTemplateListReply(anyInt())
            val expectedResult = Fail(error)
            viewModel.reviewTemplate.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when insert review reply return fail`() {
        runBlocking {
            val error = NullPointerException()
            onInsertReviewReply_thenError(error)

            viewModel.insertReviewReply(anyInt(), anyInt(), anyInt(), anyString())
            val expectedResult = Fail(error)
            viewModel.insertReviewReply.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when update review reply return fail`() {
        runBlocking {
            val error = NullPointerException()
            onUpdateReviewReply_thenError(error)

            viewModel.updateReviewReply(anyInt(), anyString())
            val expectedResult = Fail(error)
            viewModel.updateReviewReply.verifyErrorEquals(expectedResult)
        }
    }


    private fun onReplyTemplateList_thenError(exception: NullPointerException) {
        coEvery { getReviewTemplateListUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onInsertReviewReply_thenError(exception: NullPointerException) {
        coEvery { insertSellerResponseUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onUpdateReviewReply_thenError(exception: NullPointerException) {
        coEvery { updateSellerResponseUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onReplyTemplateList_thenReturn() {
        coEvery { getReviewTemplateListUseCase.executeOnBackground() } returns ReviewReplyTemplateListResponse.ReviewResponseTemplateList()
    }

    private fun onInsertReviewReply_thenReturn() {
        coEvery { insertSellerResponseUseCase.executeOnBackground() } returns ReviewReplyInsertResponse.InboxReviewInsertReviewResponse()
    }

    private fun onUpdateReviewReply_thenReturn() {
        coEvery { updateSellerResponseUseCase.executeOnBackground() } returns ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse()
    }

    private fun verifySuccessInsertReviewReply() {
        coVerify { insertSellerResponseUseCase.executeOnBackground() }
    }

    private fun verifyUpdateReviewReplyUseCaseCalled() {
        coVerify { updateSellerResponseUseCase.executeOnBackground() }
    }

    private fun verifySuccessReplyTemplateListUseCaseCalled() {
        coVerify { getReviewTemplateListUseCase.executeOnBackground() }
    }
}