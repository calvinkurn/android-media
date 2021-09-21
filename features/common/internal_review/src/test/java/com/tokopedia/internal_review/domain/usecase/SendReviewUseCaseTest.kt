package com.tokopedia.internal_review.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.internal_review.data.remotemodel.ReviewResponseModel
import com.tokopedia.internal_review.utils.TestHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * Created By @ilhamsuaib on 29/01/21
 */

class SendReviewUseCaseTest {

    companion object {
        private const val RESPONSE_SUCCESS = "json/submit_review_response_success.json"
        private const val RESPONSE_ERROR = "json/submit_review_response_error.json"
    }

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @get:Rule
    val expectedException = ExpectedException.none()

    private lateinit var sendReviewUseCase: SendReviewUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sendReviewUseCase = SendReviewUseCase(gqlRepository)
    }

    @Test
    fun `should success submit review when return response with no error message`() = runBlocking {
        val successResponse = TestHelper.createSuccessResponse<ReviewResponseModel>(RESPONSE_SUCCESS)
        val params = getSendReviewParam()
        sendReviewUseCase.params = params

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = sendReviewUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        val errors = successResponse.getData<ReviewResponseModel>(ReviewResponseModel::class.java).chipSubmitReviewApp.messageError

        assert(errors.isNullOrEmpty())
        Assert.assertEquals(params, sendReviewUseCase.params)
        Assert.assertEquals(true, result)
    }

    @Test
    fun `should throw exception when submit review then returns response with error message inside`() = runBlocking {
        val successResponse = TestHelper.createSuccessResponse<ReviewResponseModel>(RESPONSE_ERROR)
        val params = getSendReviewParam()
        sendReviewUseCase.params = params

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        expectedException.expect(MessageErrorException::class.java)
        sendReviewUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        val errors = successResponse.getData<ReviewResponseModel>(ReviewResponseModel::class.java).chipSubmitReviewApp.messageError
        assert(!errors.isNullOrEmpty())
        Assert.assertEquals(params, sendReviewUseCase.params)
    }

    @Test
    fun `should throw exception when submit review then returns error response`() = runBlocking {
        val successResponse = TestHelper.createErrorResponse<ReviewResponseModel>()
        val params = getSendReviewParam()
        sendReviewUseCase.params = params

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        expectedException.expect(MessageErrorException::class.java)
        sendReviewUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        val errors = successResponse.getData<ReviewResponseModel>(ReviewResponseModel::class.java).chipSubmitReviewApp.messageError
        assert(!errors.isNullOrEmpty())
        Assert.assertEquals(params, sendReviewUseCase.params)
    }

    private fun getSendReviewParam(): RequestParams {
        return RequestParams.create().apply {
            putString("userID", "123456")
            putInt("rating", 5)
            putString("comment", "nice app")
            putString("appVersion", "SA 2.42")
            putString("deviceModel", "samsung")
            putString("osType", "android")
            putString("osVersion", "10")
        }
    }
}