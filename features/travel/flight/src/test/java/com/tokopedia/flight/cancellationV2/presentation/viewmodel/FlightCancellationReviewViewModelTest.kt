package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationEstimateRefundUseCase
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationRequestCancelUseCase
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dummy.*
import com.tokopedia.flight.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 22/07/2020
 */
class FlightCancellationReviewViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    private val estimateUseCase: FlightCancellationEstimateRefundUseCase = mockk()
    private val requestUseCase: FlightCancellationRequestCancelUseCase = mockk()
    private val userSession: UserSessionInterface = mockk()

    private lateinit var viewmodel: FlightCancellationReviewViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewmodel = FlightCancellationReviewViewModel(estimateUseCase, requestUseCase, flightAnalytics, userSession, testDispatcherProvider)
    }

    @Test
    fun defaultValueAfterInit() {
        // given

        // when

        // then
        assert(viewmodel.requestCancel.value is Success)
        (viewmodel.requestCancel.value as Success).data shouldBe false
    }

    @Test
    fun onInit_failToFetchRefund_shouldRemoveJourneyWithoutPassengersToCancelAndRefundIsFail() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER
        coEvery { estimateUseCase.createRequestParams(any(), any(), any(), any()) } returns mapOf()
        coEvery { estimateUseCase.execute(any()) } coAnswers { throw Throwable("Testing Error") }
        coEvery { userSession.userId } returns "12345"

        // when
        viewmodel.onInit()

        // then
        viewmodel.cancellationWrapperModel.cancellationList.size shouldBe 1
        assert(viewmodel.cancellationWrapperModel.cancellationList[0].passengerModelList.size > 0)

        assert(viewmodel.estimateRefundFinish.value is Fail)
        (viewmodel.estimateRefundFinish.value as Fail).throwable.message shouldBe "Testing Error"
    }

    @Test
    fun onInit_successToFetchRefund_RefundIsSuccessAndContainsDummyData() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER
        viewmodel.invoiceId = "1234567890"
        coEvery { estimateUseCase.createRequestParams(any(), any(), any(), any()) } returns mapOf()
        coEvery { userSession.userId } returns "12345"
        coEvery { estimateUseCase.execute(any()) } returns DUMMY_ESTIMATE_REFUND

        // when
        viewmodel.onInit()

        // then
        assert(viewmodel.estimateRefundFinish.value is Success)
        (viewmodel.estimateRefundFinish.value as Success).data.totalValue shouldBe DUMMY_ESTIMATE_REFUND.totalValue
        (viewmodel.estimateRefundFinish.value as Success).data.totalValueNumeric shouldBe DUMMY_ESTIMATE_REFUND.totalValueNumeric
        (viewmodel.estimateRefundFinish.value as Success).data.estimationExistsPolicy.size shouldBe DUMMY_ESTIMATE_REFUND.estimationExistsPolicy.size
        (viewmodel.estimateRefundFinish.value as Success).data.estimationNotExistPolicy.size shouldBe DUMMY_ESTIMATE_REFUND.estimationNotExistPolicy.size
        (viewmodel.estimateRefundFinish.value as Success).data.nonRefundableText shouldBe DUMMY_ESTIMATE_REFUND.nonRefundableText
        (viewmodel.estimateRefundFinish.value as Success).data.showEstimate shouldBe DUMMY_ESTIMATE_REFUND.showEstimate
        (viewmodel.estimateRefundFinish.value as Success).data.details.size shouldBe DUMMY_ESTIMATE_REFUND.details.size

        viewmodel.invoiceId shouldBe "1234567890"

        viewmodel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.estimateRefund shouldBe DUMMY_ESTIMATE_REFUND.totalValueNumeric
        viewmodel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.estimateFmt shouldBe DUMMY_ESTIMATE_REFUND.totalValue
        viewmodel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.showEstimateRefund shouldBe true
    }

    @Test
    fun isRefundable_defaultRefundableWrapper_shouldBeTrue() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER

        // when
        val isRefundable = viewmodel.isRefundable()

        // then
        isRefundable shouldBe true
    }

    @Test
    fun isRefundable_defaultNotRefundableWrapper_shouldBeFalse() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_NOT_REFUNDABLE_CANCELLATION_WRAPPER

        // when
        val isRefundable = viewmodel.isRefundable()

        // then
        isRefundable shouldBe false
    }

    @Test
    fun showAttachment_defaultNotRefundableWrapper_shouldBeFalse() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_NOT_REFUNDABLE_CANCELLATION_WRAPPER

        // when
        val showAttachment = viewmodel.shouldShowAttachments()

        // then
        showAttachment shouldBe false
    }

    @Test
    fun showAttachment_defaultEmptyFilePathWrapper_shouldBeFalse() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_EMPTY_FILEPATH_CANCELLATION_WRAPPER

        // when
        val showAttachment = viewmodel.shouldShowAttachments()

        // then
        showAttachment shouldBe false
    }

//    @Test
//    fun showAttachment_defaultRefundableWrapper_shouldBeTrue() {
//        // given
//        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER
//
//        // when
//        val showAttachment = viewmodel.shouldShowAttachments()
//
//        // then
//        showAttachment shouldBe true
//    }

    @Test
    fun requestCancellation_failRequest_shouldBeFail() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER
        coEvery { requestUseCase.createRequestParams(any(), any(), any(), any()) } returns mapOf()
        coEvery { requestUseCase.execute(any()) } coAnswers { throw Throwable("Test Error") }

        // when
        viewmodel.requestCancellation()

        // then
        assert(viewmodel.requestCancel.value is Fail)
        (viewmodel.requestCancel.value as Fail).throwable.message shouldBe "Test Error"
    }

    @Test
    fun requestCancellation_successRequest_shouldBeSuccess() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER
        coEvery { requestUseCase.createRequestParams(any(), any(), any(), any()) } returns mapOf()
        coEvery { requestUseCase.execute(any()) } returns DUMMY_CANCEL_REQUEST

        // when
        viewmodel.requestCancellation()

        // then
        assert(viewmodel.requestCancel.value is Success)
        (viewmodel.requestCancel.value as Success).data shouldBe true
    }

    @Test
    fun trackOnSubmit() {
        // given
        viewmodel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER
        coEvery { userSession.userId } returns "0987654321"

        // when
        viewmodel.trackOnSubmit()

        // then
        coVerify {
            flightAnalytics.eventClickNextOnCancellationSubmit(
                    "0 - 1234567890",
                    "0987654321"
            )
        }
    }

}