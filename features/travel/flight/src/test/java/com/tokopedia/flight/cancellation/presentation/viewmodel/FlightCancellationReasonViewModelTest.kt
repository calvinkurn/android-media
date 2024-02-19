package com.tokopedia.flight.cancellation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.flight.cancellation.data.CancellationAttachmentUploadEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.domain.FlightCancellationAttachmentUploadUseCase
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationReasonAndAttachmentModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_WRAPPER
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_WRAPPER_ATTACHMENT
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.flight.shouldBe
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

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

    @Test
    fun disableNextButtonNotifyState_NextStepFirstTrue() {
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
        viewModel.canNavigateToNextStep.value?.first shouldBe true
        viewModel.canNavigateToNextStep.value?.second shouldBe true
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reason shouldBe ""
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reasonId shouldBe "1"
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.attachmentList.size shouldBe 3

        //when
        viewModel.disableNextButtonNotifyState()

        //then
        assertEquals(viewModel.canNavigateToNextStep.value?.first, true)
        assertEquals(viewModel.canNavigateToNextStep.value?.second, false)
    }

    @Test
    fun disableNextButtonNotifyState_NextStepFirstFalse() {
        //when
        viewModel.disableNextButtonNotifyState()

        //then
        assertEquals(viewModel.canNavigateToNextStep.value?.first, false)
        assertEquals(viewModel.canNavigateToNextStep.value?.second, false)
    }

    @Test
    fun trackOnNext_Test() {
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

        //given
        coEvery {
            userSession.userId
        } returns "1111"

        //when
        viewModel.trackOnNext()
    }

    @Test
    fun onSuccess_validateAttachment_shouldFailedValidation_FileNameNotEmpty() {
        // given
        val fileName = "path/images.jpeg"
        // when
        viewModel.onSuccessChangeAttachment(fileName)

        // then
        viewModel.viewAttachmentModelList.value shouldBe null
    }

    @Test
    fun onNextButtonClicked_AttachmentMandatory_True() {
        viewModel.cancellationWrapperModel = DUMMY_CANCELLATION_WRAPPER_ATTACHMENT
        viewModel.buildAttachmentList()
        viewModel.selectedReason = FlightCancellationPassengerEntity.Reason(
            "1",
            "",
            arrayListOf(),
            arrayListOf(
                FlightCancellationPassengerEntity.RequiredDoc(
                    "1",
                    "TitleA"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "2",
                    "TitleB"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "3",
                    "TitleC"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "4",
                    "TitleD"
                )
            )
        )

        // when
        viewModel.onNextButtonClicked()
        //then
        viewModel.canNavigateToNextStep.value?.first shouldBe true
        viewModel.canNavigateToNextStep.value?.second shouldBe true
    }

    @Test
    fun onNextButtonClicked_AttachmentMandatory_True_TotalPassanger_0() {
        viewModel.cancellationWrapperModel = FlightCancellationWrapperModel()
        viewModel.buildAttachmentList()
        viewModel.selectedReason = FlightCancellationPassengerEntity.Reason(
            "1",
            "",
            arrayListOf(),
            arrayListOf(
                FlightCancellationPassengerEntity.RequiredDoc(
                    "1",
                    "TitleA"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "2",
                    "TitleB"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "3",
                    "TitleC"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "4",
                    "TitleD"
                )
            )
        )

        // when
        viewModel.onNextButtonClicked()
        //then
        viewModel.canNavigateToNextStep.value?.first shouldBe true
        viewModel.canNavigateToNextStep.value?.second shouldBe true
    }

    @Test
    fun onNextButtonClicked_AttachmentMandatory_True_TotalPassanger_More_Than_0() {
        viewModel.cancellationWrapperModel = FlightCancellationWrapperModel(
            FlightCancellationReasonAndAttachmentModel(),
            arrayListOf(
                FlightCancellationModel(
                    passengerModelList = arrayListOf(
                        FlightCancellationPassengerModel(
                            "A",
                            firstName = "Firmanda",
                            lastName = "Nugroho",
                            status = FlightBookingPassenger.ADULT.value
                        )
                    )
                )
            )
        )
        viewModel.buildAttachmentList()
        viewModel.selectedReason = FlightCancellationPassengerEntity.Reason(
            "1",
            "",
            arrayListOf(),
            arrayListOf(
                FlightCancellationPassengerEntity.RequiredDoc(
                    "1",
                    "TitleA"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "2",
                    "TitleB"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "3",
                    "TitleC"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "4",
                    "TitleD"
                )
            )
        )

        // when
        viewModel.onNextButtonClicked()
        //then
        viewModel.canNavigateToNextStep.value?.first shouldBe true
        viewModel.canNavigateToNextStep.value?.second shouldBe true
    }

    @Test
    fun uploadAttachmentAndBuildModel_Error() {
        val errorMessage = "MESSAGE"
        viewModel.cancellationWrapperModel = FlightCancellationWrapperModel(
            FlightCancellationReasonAndAttachmentModel(),
            arrayListOf(
                FlightCancellationModel(
                    passengerModelList = arrayListOf(
                        FlightCancellationPassengerModel(
                            "A",
                            firstName = "Firmanda",
                            lastName = "Nugroho",
                            status = FlightBookingPassenger.ADULT.value,
                            relationId = "1",
                        )
                    )
                )
            )
        )
        viewModel.selectedReason = FlightCancellationPassengerEntity.Reason(
            "1",
            "",
            arrayListOf(),
            arrayListOf(
                FlightCancellationPassengerEntity.RequiredDoc(
                    "1",
                    "TitleA"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "2",
                    "TitleB"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "3",
                    "TitleC"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "4",
                    "TitleD"
                )
            )
        )

        viewModel.buildAttachmentList()
        viewModel.buildViewAttachmentList(5)

        coEvery {
            attachmentUseCase.createRequestParams(any(), any(), any(), any(), any())
        } throws MessageErrorException(errorMessage)

        // when
        viewModel.onNextButtonClicked()
        //then
       Assert.assertEquals((viewModel.attachmentErrorString.value as Fail).throwable.message, errorMessage)
    }

    @Test
    fun uploadAttachmentAndBuildModel_Success() {
        viewModel.cancellationWrapperModel = FlightCancellationWrapperModel(
            FlightCancellationReasonAndAttachmentModel(),
            arrayListOf(
                FlightCancellationModel(
                    passengerModelList = arrayListOf(
                        FlightCancellationPassengerModel(
                            "A",
                            firstName = "Firmanda",
                            lastName = "Nugroho",
                            status = FlightBookingPassenger.ADULT.value,
                            relationId = "1",
                        )
                    )
                )
            )
        )
        viewModel.selectedReason = FlightCancellationPassengerEntity.Reason(
            "1",
            "",
            arrayListOf(),
            arrayListOf(
                FlightCancellationPassengerEntity.RequiredDoc(
                    "1",
                    "TitleA"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "2",
                    "TitleB"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "3",
                    "TitleC"
                ),
                FlightCancellationPassengerEntity.RequiredDoc(
                    "4",
                    "TitleD"
                )
            )
        )

        viewModel.buildAttachmentList()
        viewModel.buildViewAttachmentList(5)

        val cancellationUploadEntity = CancellationAttachmentUploadEntity(
            attributes = CancellationAttachmentUploadEntity.Attribute(
                isUploaded = true
            )
        )

        coEvery {
            attachmentUseCase.createRequestParams(any(), any(), any(), any(), any())
        } returns RequestParams()

        coEvery {
            attachmentUseCase.executeCoroutine(any())
        } returns cancellationUploadEntity

        // when
        viewModel.onNextButtonClicked()
        //then
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reason shouldBe ""
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.reasonId shouldBe "1"
        viewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.attachmentList.size shouldBe 1
    }
}
