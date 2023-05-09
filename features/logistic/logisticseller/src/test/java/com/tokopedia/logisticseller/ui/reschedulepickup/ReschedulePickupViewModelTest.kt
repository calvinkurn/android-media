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
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupTestDataProvider.getRescheduleInfoWithErrorOrderData
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleErrorAction
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupAction
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupErrorState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReschedulePickupViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val getReschedulePickupUseCase: GetReschedulePickupUseCase = mockk(relaxed = true)
    private val saveReschedulePickupUseCase: SaveReschedulePickupUseCase = mockk(relaxed = true)
    private lateinit var reschedulePickupViewModel: ReschedulePickupViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        reschedulePickupViewModel = ReschedulePickupViewModel(
            coroutineTestRule.dispatchers,
            getReschedulePickupUseCase,
            saveReschedulePickupUseCase
        )
    }

    @Test
    fun `when get Reschedule Pickup Detail then returns success`() =
        coroutineTestRule.runTest {
            // given
            val response = ReschedulePickupTestDataProvider.getRescheduleInfo()
            coEvery { getReschedulePickupUseCase(any()) } returns ReschedulePickupTestDataProvider.getRescheduleInfo()
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.LoadRescheduleInfo("12345"))
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
        coroutineTestRule.runTest {
            // given
            val response = GetReschedulePickupResponse.Data()
            coEvery { getReschedulePickupUseCase(any()) } returns response

            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.LoadRescheduleInfo(("12345")))

            // then
            runCollectingUiEvent {
                assert(
                    it.last() == ReschedulePickupAction.ShowError(
                        ReschedulePickupErrorState(
                            "Data Reschedule Pickup tidak ditemukan",
                            RescheduleErrorAction.SHOW_EMPTY_STATE
                        )
                    )
                )
            }
        }

    @Test
    fun `when get Reschedule Pickup order data is error then show toaster`() =
        coroutineTestRule.runTest {
            // given
            val response = getRescheduleInfoWithErrorOrderData()
            coEvery { getReschedulePickupUseCase(any()) } returns response

            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.LoadRescheduleInfo(("12345")))
            // then
            runCollectingUiEvent {
                assert(
                    it.last() == ReschedulePickupAction.ShowError(
                        ReschedulePickupErrorState(
                            "error",
                            RescheduleErrorAction.SHOW_TOASTER_FAILED_GET_RESCHEDULE
                        )
                    )
                )
            }
        }

    @Test
    fun `when get Reschedule Pickup Detail then throws error`() =
        coroutineTestRule.runTest {
            // given
            coEvery { getReschedulePickupUseCase(any()) } throws defaultThrowable
            // when
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.LoadRescheduleInfo(("12345")))
            // then
            runCollectingUiEvent {
                assert(
                    it.last() == ReschedulePickupAction.ShowError(
                        ReschedulePickupErrorState(
                            defaultThrowable.message!!,
                            RescheduleErrorAction.SHOW_EMPTY_STATE
                        )
                    )
                )
            }
        }

    @Test
    fun `when save reschedule pickup then returns success`() =
        coroutineTestRule.runTest {
            // given
            coEvery { saveReschedulePickupUseCase(any()) } returns SaveReschedulePickupResponse.Data()
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SaveReschedule)
            // then
            assert(reschedulePickupViewModel.uiState.value.saveRescheduleModel != null)
        }

    @Test
    fun `when save reschedule pickup then throws error`() =
        coroutineTestRule.runTest {
            // given
            coEvery { saveReschedulePickupUseCase(any()) } throws defaultThrowable
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SaveReschedule)
            // then
            runCollectingUiEvent {
                assert(
                    it.last() == ReschedulePickupAction.ShowError(
                        ReschedulePickupErrorState(
                            defaultThrowable.message!!,
                            RescheduleErrorAction.SHOW_TOASTER_FAILED_SAVE_RESCHEDULE
                        )
                    )
                )
            }
        }

    @Test
    fun `when setDay then reset time and reschedule summary`() =
        coroutineTestRule.runTest {
            // given
            val day = ReschedulePickupTestDataProvider.getChosenDay()
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
            // then
            assert(reschedulePickupViewModel.input.day == day.day)
            assert(reschedulePickupViewModel.input.time.isEmpty())
            assert(reschedulePickupViewModel.uiState.value.info.summary.isEmpty())
            assert(reschedulePickupViewModel.uiState.value.options.timeOptions == day.timeOptions)
            assert(!reschedulePickupViewModel.uiState.value.valid)
        }

    @Test
    fun `when setTime then set time and reschedule summary`() =
        coroutineTestRule.runTest {
            // given
            val day = ReschedulePickupTestDataProvider.getChosenDay()
            val time = ReschedulePickupTestDataProvider.getChosenTime(day)
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))
            // then
            assert(reschedulePickupViewModel.input.time == time.time)
            assert(reschedulePickupViewModel.uiState.value.info.summary == time.etaPickup)
        }

    @Test
    fun `when setReason then set reason`() =
        coroutineTestRule.runTest {
            // given
            val reason = ReschedulePickupTestDataProvider.getChosenReason()
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))
            // then
            assert(reschedulePickupViewModel.input.reason == reason.reason)
            assert(reschedulePickupViewModel.uiState.value.reason == reason.reason)
        }

    @Test
    fun `when setReason with custom reason option then set custom reason flag to true`() =
        coroutineTestRule.runTest {
            // given
            val reason = ReschedulePickupTestDataProvider.getCustomReason()
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))
            // then
            assert(reschedulePickupViewModel.input.reason != reason.reason)
            assert(reschedulePickupViewModel.uiState.value.reason == reason.reason)
            assert(reschedulePickupViewModel.uiState.value.isCustomReason)
        }

    @Test
    fun `when setCustomReason with reason below 15 char then show custom reason error`() =
        coroutineTestRule.runTest {
            // given
            val reason = REASON_BELOW_MIN
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(reason))
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == "Min. 15 karakter")
        }

    @Test
    fun `when setCustomReason with reason length 15 char then dont show error`() =
        coroutineTestRule.runTest {
            // given
            val reason = REASON_MIN
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(reason))
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == null)
        }

    @Test
    fun `when setCustomReason with reason length 159 char then dont show error`() =
        coroutineTestRule.runTest {
            // given
            val reason = REASON_MAX
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(reason))
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == null)
        }

    @Test
    fun `when setCustomReason with reason length more than 160 char then dont show error`() =
        coroutineTestRule.runTest {
            // given
            val reason = REASON_MORE_MAX
            // when
            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(reason))
            // then
            assert(reschedulePickupViewModel.input.reason == reason)
            assert(reschedulePickupViewModel.uiState.value.customReasonError == "Sudah mencapai maks. char")
        }

    @Test
    fun `when setDialogState then should update save model`() =
        coroutineTestRule.runTest {
            // given
            coEvery { saveReschedulePickupUseCase(any()) } returns SaveReschedulePickupResponse.Data()
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SaveReschedule)

            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CloseDialog(false))

            // then
            assert(reschedulePickupViewModel.uiState.value.saveRescheduleModel?.openDialog == false)
        }

    @Test
    fun `when validateInput with template reason then should be valid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getChosenReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))

        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))

        // then
        assert(reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason but has not specify the custom reason then should be invalid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))

        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))

        // then
        assert(!reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason below min char then should be invalid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))

        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(REASON_BELOW_MIN))

        // then
        assert(!reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason more than max char then should be invalid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))

        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(REASON_MORE_MAX))

        // then
        assert(!reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason minimum length char then should be valid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))

        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(REASON_MIN))

        // then
        assert(reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when validateInput with template custom reason maximum length char then should be valid to save reschedule`() {
        // given
        val day = ReschedulePickupTestDataProvider.getChosenDay()
        val reason = ReschedulePickupTestDataProvider.getCustomReason()
        val time = ReschedulePickupTestDataProvider.getChosenTime(day)
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectDay(day))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectTime(time))
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SelectReason(reason))

        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CustomReason(REASON_MAX))

        // then
        assert(reschedulePickupViewModel.uiState.value.valid)
    }

    @Test
    fun `when clickSubtitle then should show TnC on webview`() {
        // given
        val url = "url"
        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.ClickSubtitle(url))

        // then
        runCollectingUiEvent {
            assert(it.last() == ReschedulePickupAction.OpenTnCWebView(url))
        }
    }

    @Test
    fun `when user click dialog after success save reschedule then should close page`() =
        coroutineTestRule.runTest {
            // given
            val success = true
            coEvery { saveReschedulePickupUseCase(any()) } returns SaveReschedulePickupResponse.Data()
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.SaveReschedule)

            // when
            reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.CloseDialog(success))

            // then
            assert(reschedulePickupViewModel.uiState.value.saveRescheduleModel?.openDialog == false)
            runCollectingUiEvent {
                assert(it.last() == ReschedulePickupAction.ClosePage(success))
            }
        }

    @Test
    fun `when user press back then should close page`() {
        // when
        reschedulePickupViewModel.onEvent(ReschedulePickupUiEvent.PressBack)

        // then
        runCollectingUiEvent {
            assert(it.last() == ReschedulePickupAction.ClosePage(false))
        }
    }

    private fun runCollectingUiEvent(block: (List<ReschedulePickupAction>) -> Unit) {
        val scope = CoroutineScope(coroutineTestRule.dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ReschedulePickupAction>()
        val uiEventCollectorJob = scope.launch {
            reschedulePickupViewModel.uiEffect.toList(uiEvent)
        }
        block.invoke(uiEvent)
        uiEventCollectorJob.cancel()
    }
}
