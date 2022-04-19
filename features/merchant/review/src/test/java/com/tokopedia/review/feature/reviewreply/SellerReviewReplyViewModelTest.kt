package com.tokopedia.review.feature.reviewreply

import com.tokopedia.review.feature.reviewreply.update.domain.model.ReviewReplyUpdateResponse
import com.tokopedia.review.feature.reviewreply.update.presenter.model.ReviewReplyUpdateUiModel
import com.tokopedia.review.feature.reviewreply.insert.domain.model.ReviewReplyInsertResponse
import com.tokopedia.review.feature.reviewreply.insert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyInsertTemplateResponse
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment.Companion.DATE_REVIEW_FORMAT
import com.tokopedia.review.feature.reviewreply.view.model.InsertTemplateReplyUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import java.text.SimpleDateFormat
import java.util.*

class SellerReviewReplyViewModelTest : SellerReviewReplyViewModelTestFixture() {

    @Test
    fun `when get reply template list should return success`() {
        runBlocking {
            onReplyTemplateList_thenReturn()
            viewModel.getTemplateListReply(anyString())

            verifySuccessReplyTemplateListUseCaseCalled()
            assertTrue(viewModel.reviewTemplate.value is Success)
            assertNotNull(viewModel.reviewTemplate.value)
        }
    }

    @Test
    fun `when insert review reply should return success`() {
        runBlocking {
            onInsertReviewReply_thenReturn()
            viewModel.insertReviewReply(anyString(), anyString())

            verifySuccessInsertReviewReplyUseCalled()
            val expectedValue = Success(ReviewReplyInsertUiModel(anyBoolean()))
            assertTrue(viewModel.insertReviewReply.value is Success)
            viewModel.insertReviewReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when update review reply should return success`() {
        runBlocking {
            onUpdateReviewReply_thenReturn()
            viewModel.updateReviewReply(anyString(), anyString())

            verifyUpdateReviewReplyUseCaseCalled()
            val expectedValue = Success(ReviewReplyUpdateUiModel(anyBoolean()))
            assertTrue(viewModel.updateReviewReply.value is Success)
            viewModel.updateReviewReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when insert template reply should return success`() {
        runBlocking {
            onInsertTemplateReply_thenReturn()
            viewModel.insertTemplateReviewReply(anyString(), anyString(), anyString())

            verifySuccessInsertTemplateReplyUseCaseCalled()
            val expectedValue = Success(InsertTemplateReplyUiModel())
            assertTrue(viewModel.insertTemplateReply.value is Success)
            viewModel.insertTemplateReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when insert template reply should return fail`() {
        runBlocking {
            val error = NullPointerException()
            onInsertTemplateReply_thenError(error)

            viewModel.insertTemplateReviewReply(anyString(), anyString(), anyString())
            val expectedResult = Fail(error)
            viewModel.insertTemplateReply.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get reply template list should return fail`() {
        runBlocking {
            val error = NullPointerException()
            onReplyTemplateList_thenError(error)

            viewModel.getTemplateListReply(anyString())
            val expectedResult = Fail(error)
            viewModel.reviewTemplate.verifyErrorEquals(expectedResult)

        }
    }

    @Test
    fun `when insert review reply return fail`() {
        runBlocking {
            val error = NullPointerException()
            onInsertReviewReply_thenError(error)

            viewModel.insertReviewReply(anyString(), anyString())
            val expectedResult = Fail(error)
            viewModel.insertReviewReply.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when update review reply return fail`() {
        runBlocking {
            val error = NullPointerException()
            onUpdateReviewReply_thenError(error)

            viewModel.updateReviewReply(anyString(), anyString())
            val expectedResult = Fail(error)
            viewModel.updateReviewReply.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when reply time equals with expected result return success`() {
        val expectedResult = SimpleDateFormat(DATE_REVIEW_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)
        assertEquals(viewModel.replyTime, expectedResult)
    }

    private fun onReplyTemplateList_thenError(exception: NullPointerException) {
        coEvery { getReviewTemplateListUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onInsertReviewReply_thenError(exception: NullPointerException) {
        coEvery { reviewReplyInsertUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onUpdateReviewReply_thenError(exception: NullPointerException) {
        coEvery { reviewReplyUpdateUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onInsertTemplateReply_thenError(exception: NullPointerException) {
        coEvery { insertTemplateReviewReplyUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onReplyTemplateList_thenReturn() {
        coEvery { getReviewTemplateListUseCase.executeOnBackground() } returns ReviewReplyTemplateListResponse.ReviewResponseTemplateList()
    }

    private fun onInsertReviewReply_thenReturn() {
        coEvery { reviewReplyInsertUseCase.executeOnBackground() } returns ReviewReplyInsertResponse.ProductrevInsertSellerResponse()
    }

    private fun onUpdateReviewReply_thenReturn() {
        coEvery { reviewReplyUpdateUseCase.executeOnBackground() } returns ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse()
    }

    private fun onInsertTemplateReply_thenReturn() {
        coEvery { insertTemplateReviewReplyUseCase.executeOnBackground() } returns ReviewReplyInsertTemplateResponse.InsertResponseTemplate()
    }

    private fun verifySuccessInsertReviewReplyUseCalled() {
        coVerify { reviewReplyInsertUseCase.executeOnBackground() }
    }

    private fun verifyUpdateReviewReplyUseCaseCalled() {
        coVerify { reviewReplyUpdateUseCase.executeOnBackground() }
    }

    private fun verifySuccessReplyTemplateListUseCaseCalled() {
        coVerify { getReviewTemplateListUseCase.executeOnBackground() }
    }

    private fun verifySuccessInsertTemplateReplyUseCaseCalled() {
        coVerify { insertTemplateReviewReplyUseCase.executeOnBackground() }
    }
}
