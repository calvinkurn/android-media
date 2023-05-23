package com.tokopedia.mvc.presentation.creation.step2

import android.content.SharedPreferences
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.mvc.data.mapper.VoucherValidationPartialMapper
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse.*
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse.VoucherValidationPartial.*
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherCreationStepTwoFieldValidation
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.DateStartEndData
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import com.tokopedia.mvc.presentation.creation.util.CreationUtil.roundTimePerHalfHour
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.removeTime
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class VoucherInformationViewModelTest {

    private lateinit var viewModel: VoucherInformationViewModel

    @RelaxedMockK
    lateinit var voucherValidationPartialUseCase: VoucherValidationPartialUseCase

    @RelaxedMockK
    lateinit var sharedPreference: SharedPreferences

    @RelaxedMockK
    lateinit var mapper: VoucherValidationPartialMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = VoucherInformationViewModel(
            CoroutineTestDispatchers,
            voucherValidationPartialUseCase,
            sharedPreference
        )
    }

    @Test
    fun `when initVoucherConfiguration is called, should set voucherConfiguration data accordingly`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val startPeriod = Date().roundTimePerHalfHour().removeTime()
            val voucherConfiguration = VoucherConfiguration(startPeriod = startPeriod)
            val expectedVoucherConfiguration = VoucherConfiguration(
                startPeriod = startPeriod,
                isFinishFilledStepOne = true
            )

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedVoucherConfiguration, actual.voucherConfiguration)

            job.cancel()
        }
    }

    @Test
    fun `when getCurrentVoucherConfiguration() is called, should return with the most updated voucher configuration data`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val startPeriod = Date().roundTimePerHalfHour().removeTime()
            val voucherConfiguration = VoucherConfiguration(startPeriod = startPeriod)
            val expectedVoucherConfiguration = VoucherConfiguration(
                startPeriod = startPeriod,
                isFinishFilledStepOne = true
            )

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration
                )
            )

            // Given
            val actual = viewModel.getCurrentVoucherConfiguration()
            assertEquals(expectedVoucherConfiguration, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handleBackToPreviousStep() is called, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            mockVoucherConfigurationInitiation()

            val expectedEmittedAction =
                VoucherCreationStepTwoAction.BackToPreviousStep(viewModel.getCurrentVoucherConfiguration())

            val emittedAction = arrayListOf<VoucherCreationStepTwoAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(VoucherCreationStepTwoEvent.TapBackButton)

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher target selection, should set voucher configuration data accordingly`() {
        runBlockingTest {
            // Given
            val isPublic = true
            val isChangingTargetBuyer = true
            val targetBuyer = VoucherTargetBuyer.ALL_BUYER

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration()
                    .copy(isVoucherPublic = isPublic, targetBuyer = targetBuyer)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.ChooseVoucherTarget(
                    isPublic,
                    isChangingTargetBuyer
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedVoucherConfiguration, actual.voucherConfiguration)

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher target selection, but the target buyer is not changing, should keep target buyer data as it is`() {
        runBlockingTest {
            // Given
            val isPublic = true
            val isChangingTargetBuyer = false
            val expectedTargetBuyer = VoucherTargetBuyer.ALL_BUYER

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration()
                    .copy(isVoucherPublic = isPublic, targetBuyer = expectedTargetBuyer)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.ChooseVoucherTarget(
                    isPublic,
                    isChangingTargetBuyer
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedVoucherConfiguration, actual.voucherConfiguration)

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher name changes, should update the voucher name data accordingly`() {
        runBlockingTest {
            // Given
            val voucherName = "RAMADHAN CERIA"

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration().copy(voucherName = voucherName)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherNameChanged(voucherName))

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.voucherName,
                actual.voucherConfiguration.voucherName
            )

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher code changes, should update the voucher code data accordingly`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val startPeriod = Date().roundTimePerHalfHour().removeTime()
            val isPublic = true
            val voucherCode = "CLDPLY"

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val voucherConfiguration =
                VoucherConfiguration(startPeriod = startPeriod, isFinishFilledStepOne = true)
            val expectedVoucherConfiguration =
                voucherConfiguration.copy(isVoucherPublic = isPublic, voucherCode = voucherCode)

            viewModel.processEvent(
                VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                    pageMode,
                    voucherConfiguration
                )
            )

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherCodeChanged(voucherCode))

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.voucherCode,
                actual.voucherConfiguration.voucherCode
            )

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher recurring toggle changes, should update the recurring toggle data accordingly`() {
        runBlockingTest {
            // Given
            val isRecurringActive = true

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration().copy(isPeriod = isRecurringActive)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.OnVoucherRecurringToggled(
                    isRecurringActive
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.isPeriod,
                actual.voucherConfiguration.isPeriod
            )

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher start period changes, should update the start period data accordingly`() {
        runBlockingTest {
            // Given
            val startPeriod = Date().addTimeToSpesificDate(Calendar.DATE, 7).removeTime()

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration().copy(startPeriod = startPeriod)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.OnVoucherStartDateChanged(
                    startPeriod.toCalendar()
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.startPeriod,
                actual.voucherConfiguration.startPeriod
            )

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher end period changes, should update the end period data accordingly`() {
        runBlockingTest {
            // Given
            val endPeriod = Date().addTimeToSpesificDate(Calendar.MONTH, 1).removeTime()

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration().copy(endPeriod = endPeriod)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.OnVoucherEndDateChanged(
                    endPeriod.toCalendar()
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.endPeriod,
                actual.voucherConfiguration.endPeriod
            )

            job.cancel()
        }
    }

    @Test
    fun `when handling total recurring period changes, should update the total period data accordingly`() {
        runBlockingTest {
            // Given
            val totalPeriod = 3

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedVoucherConfiguration =
                viewModel.getCurrentVoucherConfiguration().copy(totalPeriod = totalPeriod)

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.OnVoucherRecurringPeriodSelected(totalPeriod)
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.endPeriod,
                actual.voucherConfiguration.endPeriod
            )

            job.cancel()
        }
    }

    @Test
    fun `when handleCoachmark() is called, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            val expectedEmittedAction = VoucherCreationStepTwoAction.ShowCoachmark

            viewModel.setSharedPrefCoachMarkAlreadyShown()

            val emittedAction = arrayListOf<VoucherCreationStepTwoAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(VoucherCreationStepTwoEvent.HandleCoachMark)

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handling navigation to next step, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val voucherConfiguration = viewModel.getCurrentVoucherConfiguration()

            val expectedEmittedAction =
                VoucherCreationStepTwoAction.NavigateToNextStep(pageMode, voucherConfiguration)

            val emittedAction = arrayListOf<VoucherCreationStepTwoAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepTwoEvent.NavigateToNextStep(
                    voucherConfiguration
                )
            )

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when getFieldValidated() is called and the page mode is EDIT will return validated field as ALL`() {
        runBlockingTest {
            // Given
            mockEditVoucherConfigurationInitiation()
            val expectedFieldValidate = VoucherCreationStepTwoFieldValidation.ALL

            val emittedValue = arrayListOf<VoucherCreationStepTwoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherCodeChanged(""))

            // Then
            val actual = emittedValue.last().fieldValidated
            assertEquals(expectedFieldValidate, actual)

            job.cancel()
        }
    }

    @Test
    fun `when mapVoucherRecurringPeriodData() is called, should return list of DateStartEndData correctly`() {
        runBlockingTest {
            // Given
            val expected = listOf(
                DateStartEndData(
                    "dateStart",
                    "dateEnd",
                    "hourStart",
                    "hourEnd"
                )
            )

            val validationDate = listOf(
                Data.ValidationDate(
                    "dateEnd",
                    "dateStart",
                    "hourEnd",
                    "hourStart"
                )
            ).map {
                VoucherValidationResult.ValidationDate(
                    it.endDate,
                    it.startDate,
                    it.endHour,
                    it.startHour,
                    it.totalLiveTime,
                    it.available,
                    it.notAvailableReason,
                    it.type
                )
            }

            // When
            val actual = viewModel.mapVoucherRecurringPeriodData(validationDate)

            // Then
            assertEquals(expected.first().dateStart, actual.first().dateStart)
            assertEquals(expected.first().dateEnd, actual.first().dateEnd)
            assertEquals(expected.first().hourStart, actual.first().hourStart)
            assertEquals(expected.first().hourEnd, actual.first().hourEnd)
        }
    }

    private fun mockVoucherConfigurationInitiation() {
        val pageMode = PageMode.CREATE
        val startPeriod = Date().roundTimePerHalfHour().removeTime()

        val voucherConfiguration =
            VoucherConfiguration(startPeriod = startPeriod, isFinishFilledStepOne = true)

        viewModel.processEvent(
            VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                pageMode,
                voucherConfiguration
            )
        )
    }

    private fun mockEditVoucherConfigurationInitiation() {
        val pageMode = PageMode.EDIT
        val startPeriod = Date().roundTimePerHalfHour().removeTime()
        val voucherConfiguration =
            VoucherConfiguration(startPeriod = startPeriod, isFinishFilledStepOne = true)

        viewModel.processEvent(
            VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                pageMode,
                voucherConfiguration
            )
        )
    }

    private fun mockVoucherValidationPartialGQLCall() {
        val validationResult = mapper.map(
            VoucherValidationPartialResponse(
                voucherValidationPartial = VoucherValidationPartial(
                    data = Data(
                        validationDate = listOf(
                            Data.ValidationDate(
                                "endDate",
                                "startDate",
                                "endHour",
                                "startHour"
                            )
                        )
                    )
                )
            )
        )
        coEvery { voucherValidationPartialUseCase.execute(any()) } returns validationResult
    }
}
