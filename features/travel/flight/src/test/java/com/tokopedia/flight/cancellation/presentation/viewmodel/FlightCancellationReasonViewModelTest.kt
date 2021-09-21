package com.tokopedia.flight.cancellation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.domain.FlightCancellationAttachmentUploadUseCase
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_WRAPPER
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_WRAPPER_ATTACHMENT
import com.tokopedia.flight.shouldBe
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 27/07/2020
 */
class FlightCancellationReasonViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    private val userSession: UserSessionInterface = mockk()
    private val attachmentUseCase: FlightCancellationAttachmentUploadUseCase = mockk()

    private lateinit var viewModel: FlightCancellationReasonViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FlightCancellationReasonViewModel(attachmentUseCase, userSession, flightAnalytics, testDispatcherProvider)
    }

    @Test
    fun onInitViewModel() {
        // given

        // when

        // then
        viewModel.selectedReason shouldBe null
        viewModel.editedAttachmentPosition shouldBe -1
        viewModel.canNavigateToNextStep.value!!.first shouldBe false
        viewModel.canNavigateToNextStep.value!!.second shouldBe false
        viewModel.attachmentErrorStringRes.value shouldBe FlightCancellationReasonViewModel.DEFAULT_STRING_RES_ERROR
        viewModel.getAttachments().size shouldBe 0
    }

    @Test
    fun buildAttachmentList() {
        // given
        viewModel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER

        // when
        viewModel.buildAttachmentList()

        // then
        viewModel.getAttachments().size shouldBe 1
        viewModel.cancellationWrapperModel shouldBe DUMMY_CANCELLATION_WRAPPER
    }

    @Test
    fun buildViewAttachmentList() {
        // given
        viewModel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER_ATTACHMENT
        viewModel.buildAttachmentList()

        // when
        viewModel.buildViewAttachmentList(5)

        // then
        viewModel.viewAttachmentModelList.value!!.size shouldBe 2

        for (item in viewModel.viewAttachmentModelList.value!!) {
            item.docType shouldBe 5
        }
    }

    @Test
    fun onSuccess_validateAttachmentWithEmptyPath_shouldFailedValidation() {
        // given

        // when
        viewModel.onSuccessChangeAttachment("")

        // then
        viewModel.viewAttachmentModelList.value shouldBe null
    }

    @Test
    fun onNextButtonClicked_nullSelectedReason_shouldBuildAndAutoMNavigate() {
        // given
        viewModel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER

        // when
        viewModel.onNextButtonClicked()

        // then
        viewModel.canNavigateToNextStep.value!!.first shouldBe true
        viewModel.canNavigateToNextStep.value!!.second shouldBe true
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reason shouldBe ""
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reasonId shouldBe "0"
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.attachmentList.size shouldBe 0
    }

    @Test
    fun onNextButtonClicked_selectedReasonWithoutRequiredDocs_shouldBuildAndAutoMNavigate() {
        // given
        viewModel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER_ATTACHMENT
        viewModel.buildAttachmentList()
        viewModel.selectedReason = FlightCancellationPassengerEntity.Reason(
                "1",
                "",
                arrayListOf(),
                arrayListOf()
        )


        // when
        viewModel.onNextButtonClicked()

        // then
        viewModel.canNavigateToNextStep.value!!.first shouldBe true
        viewModel.canNavigateToNextStep.value!!.second shouldBe true
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reason shouldBe ""
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reasonId shouldBe "1"
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.attachmentList.size shouldBe 3
    }

}