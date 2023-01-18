package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticseller.data.response.GetReschedulePickupResponse
import com.tokopedia.logisticseller.data.response.SaveReschedulePickupResponse
import com.tokopedia.logisticseller.domain.usecase.GetReschedulePickupUseCase
import com.tokopedia.logisticseller.domain.usecase.SaveReschedulePickupUseCase
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupTestDataProvider.REASON_BELOW_MIN
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupTestDataProvider.REASON_MAX
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupTestDataProvider.REASON_MIN
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupTestDataProvider.REASON_MORE_MAX
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReschedulePickupComposeViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val getReschedulePickupUseCase: GetReschedulePickupUseCase = mockk(relaxed = true)
    private val saveReschedulePickupUseCase: SaveReschedulePickupUseCase = mockk(relaxed = true)
    private lateinit var reschedulePickupViewModel: ReschedulePickupComposeViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        reschedulePickupViewModel = ReschedulePickupComposeViewModel(
            coroutineTestRule.dispatchers,
            getReschedulePickupUseCase,
            saveReschedulePickupUseCase
        )
    }

    @Test
    fun `when get Reschedule Pickup Detail then returns success`() =
        coroutineTestRule.runBlockingTest {
            // given
            val response = ReschedulePickupTestDataProvider.getRescheduleInfo()
            coEvery { getReschedulePickupUseCase(any()) } returns ReschedulePickupTestDataProvider.getRescheduleInfo()
            // when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            val result = reschedulePickupViewModel.uiState.value
            val orderData = response.mpLogisticGetReschedulePickup.data.first().orderData
            val shipperName = response.mpLogisticGetReschedulePickup.data.first().shipperName

            assert(result.info.courier == "${orderData.first().shipperProductName} - $shipperName")
            assert(result.info.invoice == orderData.first().invoice)
            assert(result.info.guide == response.mpLogisticGetReschedulePickup.orderDetailTicker)
            assert(result.info.applink == response.mpLogisticGetReschedulePickup.appLink)
        }

    @Test
    fun `when get Reschedule Pickup Detail Response data is empty then throws error`() =
        coroutineTestRule.runBlockingTest {
            // given
            val response = GetReschedulePickupResponse.Data()
            coEvery { getReschedulePickupUseCase(any()) } returns response

            // when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.uiState.value.error == "Data Reschedule Pickup tidak ditemukan")
        }

    @Test
    fun `when get Reschedule Pickup Detail then throws error`() =
        coroutineTestRule.runBlockingTest {
            // given
            coEvery { getReschedulePickupUseCase(any()) } throws defaultThrowable
            // when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.uiState.value.error == defaultThrowable.message)
        }

    @Test
    fun `when save reschedule pickup then returns success`() =
        coroutineTestRule.runBlockingTest {
            // given
            coEvery { saveReschedulePickupUseCase(any()) } returns SaveReschedulePickupResponse.Data()
            // when
            reschedulePickupViewModel.saveReschedule(
                "12345"
            )
            // then
            assert(reschedulePickupViewModel.uiState.value.saveRescheduleModel != null)
        }

    @Test
    fun `when save reschedule pickup then throws error`() =
        coroutineTestRule.runBlockingTest {
            // given
            coEvery { saveReschedulePickupUseCase(any()) } throws defaultThrowable
            // when
            reschedulePickupViewModel.saveReschedule(
                "12345"
            )
            // then
            assert(reschedulePickupViewModel.uiState.value.error == defaultThrowable.message)
        }

    @Test
    fun `when setDay then reset time and reschedule summary`() =
        coroutineTestRule.runBlockingTest {
            // given
            val day = ReschedulePickupTestDataProvider.getChosenDay()
            // when
            reschedulePickupViewModel.setDay(day)
            // then
            assert(reschedulePickupViewModel.input.day == day.day)
            assert(reschedulePickupViewModel.input.time.isEmpty())
            assert(reschedulePickupViewModel.uiState.value.info.summary.isEmpty())
            assert(reschedulePickupViewModel.uiState.value.options.timeOptions == day.timeOptions)
            assert(!reschedulePickupViewModel.uiState.value.valid)
        }

    @Test
    fun `when setTime then set time and reschedule summary`() =
        coroutineTestRule.runBlockingTest {
            // given
            val day = ReschedulePickupTestDataProvider.getChosenDay()
            val time = ReschedulePickupTestDataProvider.getChosenTime(day)
            // when
            reschedulePickupViewModel.setTime(time)
            // then
            assert(reschedulePickupViewModel.input.time == time.time)
            assert(reschedulePickupViewModel.uiState.value.info.summary == time.etaPickup)
        }

    @Test
    fun `when setReason then set reason`() =
        coroutineTestRule.runBlockingTest {
            // given
            val reason = ReschedulePickupTestDataProvider.getChosenReason()
            // when
            reschedulePickupViewModel.setReason(reason)
            // then
            assert(reschedulePickupViewModel.input.reason == reason.reason)
            assert(reschedulePickupViewModel.uiState.value.reason == reason.reason)
        }

    @Test
    fun `when setReason with custom reason option then set custom reason flag to true`() =
        coroutineTestRule.runBlockingTest {
            // given
            val reason = ReschedulePickupTestDataProvider.getCustomReason()
            // when
            reschedulePickupViewModel.setReason(reason)
            // then
            assert(reschedulePickupViewModel.input.reason != reason.reason)
            assert(reschedulePickupViewModel.uiState.value.reason == reason.reason)
            assert(reschedulePickupViewModel.uiState.value.isCustomReason)
        }

    @Test
    fun `when setCustomReason with reason below 15 char then show custom reason error`() =
        coroutineTestRule.runBlockingTest {
            // given
            val reason = REASON_BELOW_MIN
            // when
            reschedulePickupViewModel.setCustomReason(reason)
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == "Min. 15 karakter")
        }

    @Test
    fun `when setCustomReason with reason length 15 char then dont show error`() =
        coroutineTestRule.runBlockingTest {
            // given
            val reason = REASON_MIN
            // when
            reschedulePickupViewModel.setCustomReason(reason)
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == null)
        }

    @Test
    fun `when setCustomReason with reason length 159 char then dont show error`() =
        coroutineTestRule.runBlockingTest {
            // given
            val reason = REASON_MAX
            // when
            reschedulePickupViewModel.setCustomReason(reason)
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == null)
        }

    @Test
    fun `when setCustomReason with reason length more than 160 char then dont show error`() =
        coroutineTestRule.runBlockingTest {
            // given
            val reason = REASON_MORE_MAX
            // when
            reschedulePickupViewModel.setCustomReason(reason)
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == "Sudah mencapai maks. char")
        }

    @Test
    fun `when closeBottomSheet then bottomsheet state should be none`() =
        coroutineTestRule.runBlockingTest {
            // when
            reschedulePickupViewModel.closeBottomSheetState()
            // then
            assert(reschedulePickupViewModel.uiState.value.bottomSheet == RescheduleBottomSheetState.NONE)
        }

    @Test
    fun `when closeBottomSheet after opening bottomsheet then bottomsheet state should be none`() =
        coroutineTestRule.runBlockingTest {
            // given
            val openedBottomSheet = RescheduleBottomSheetState.REASON
            reschedulePickupViewModel.openBottomSheetState(openedBottomSheet)

            // when
            reschedulePickupViewModel.closeBottomSheetState()

            // then
            assert(reschedulePickupViewModel.uiState.value.bottomSheet != openedBottomSheet)
            assert(reschedulePickupViewModel.uiState.value.bottomSheet == RescheduleBottomSheetState.NONE)
        }

    @Test
    fun `when openBottomSheet time before choosing day then bottomsheet state should be none`() =
        coroutineTestRule.runBlockingTest {
            // given
            val openedBottomSheet = RescheduleBottomSheetState.TIME

            // when
            reschedulePickupViewModel.openBottomSheetState(openedBottomSheet)

            // then
            assert(reschedulePickupViewModel.uiState.value.bottomSheet != openedBottomSheet)
            assert(reschedulePickupViewModel.uiState.value.bottomSheet == RescheduleBottomSheetState.NONE)
        }

    @Test
    fun `when openBottomSheet time after choosing day then bottomsheet state should be time`() =
        coroutineTestRule.runBlockingTest {
            // given
            val day = ReschedulePickupTestDataProvider.getChosenDay()
            reschedulePickupViewModel.setDay(day)
            val openedBottomSheet = RescheduleBottomSheetState.TIME

            // when
            reschedulePickupViewModel.openBottomSheetState(openedBottomSheet)

            // then
            assert(reschedulePickupViewModel.uiState.value.bottomSheet == openedBottomSheet)
            assert(reschedulePickupViewModel.uiState.value.bottomSheet != RescheduleBottomSheetState.NONE)
        }

    @Test
    fun `when setDialogState then should update save model`() =
        coroutineTestRule.runBlockingTest {
            // given
            coEvery { saveReschedulePickupUseCase(any()) } returns SaveReschedulePickupResponse.Data()
            reschedulePickupViewModel.saveReschedule("12345")

            // when
            reschedulePickupViewModel.setDialogState(false)

            // then
            assert(reschedulePickupViewModel.uiState.value.saveRescheduleModel?.openDialog == false)
        }

    @Test
    fun `when validateInput with template reason then should be valid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getChosenReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.setDay(day)
        reschedulePickupViewModel.setTime(time)

        // when
        reschedulePickupViewModel.setReason(reason)

        // then
        assert(reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason but has not specify the custom reason then should be invalid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.setDay(day)
        reschedulePickupViewModel.setTime(time)

        // when
        reschedulePickupViewModel.setReason(reason)

        // then
        assert(!reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason below min char then should be invalid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.setDay(day)
        reschedulePickupViewModel.setTime(time)
        reschedulePickupViewModel.setReason(reason)

        // when
        reschedulePickupViewModel.setCustomReason(REASON_BELOW_MIN)

        // then
        assert(!reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason more than max char then should be invalid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.setDay(day)
        reschedulePickupViewModel.setTime(time)
        reschedulePickupViewModel.setReason(reason)

        // when
        reschedulePickupViewModel.setCustomReason(REASON_MORE_MAX)

        // then
        assert(!reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason minimum length char then should be valid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.setDay(day)
        reschedulePickupViewModel.setTime(time)
        reschedulePickupViewModel.setReason(reason)

        // when
        reschedulePickupViewModel.setCustomReason(REASON_MIN)

        // then
        assert(reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason maximum length char then should be valid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.setDay(day)
        reschedulePickupViewModel.setTime(time)
        reschedulePickupViewModel.setReason(reason)

        // when
        reschedulePickupViewModel.setCustomReason(REASON_MAX)

        // then
        assert(reschedulePickupViewModel.uiState.value.valid)
    }
}
