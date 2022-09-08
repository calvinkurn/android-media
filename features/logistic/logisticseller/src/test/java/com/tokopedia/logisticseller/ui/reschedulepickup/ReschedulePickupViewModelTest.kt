package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.data.response.GetReschedulePickupResponse
import com.tokopedia.logisticseller.data.response.SaveReschedulePickupResponse
import com.tokopedia.logisticseller.domain.usecase.GetReschedulePickupUseCase
import com.tokopedia.logisticseller.domain.usecase.SaveReschedulePickupUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        coroutineTestRule.runBlockingTest {
            //given
            val response = GetReschedulePickupResponse.Data(
                mpLogisticGetReschedulePickup = GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup(
                    data = listOf(
                        GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem()
                    )
                ))
            coEvery { getReschedulePickupUseCase(any()) } returns response
            //when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.reschedulePickupDetail.value is Success)
        }

    @Test
    fun `when get Reschedule Pickup Detail Response data is empty then throws error`() =
        coroutineTestRule.runBlockingTest {
            //given
            val response = GetReschedulePickupResponse.Data()
            coEvery { getReschedulePickupUseCase(any()) } returns response

            //when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.reschedulePickupDetail.value is Fail)
        }



    @Test
    fun `when get Reschedule Pickup Detail then throws error`() =
        coroutineTestRule.runBlockingTest {
            //given
            coEvery { getReschedulePickupUseCase(any()) } throws defaultThrowable
            //when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.reschedulePickupDetail.value is Fail)
        }

    @Test
    fun `when save reschedule pickup then returns success`() =
        coroutineTestRule.runBlockingTest {
            //given
            coEvery { saveReschedulePickupUseCase(any()) } returns SaveReschedulePickupResponse.Data()
            //when
            reschedulePickupViewModel.saveReschedule("12345", "2022-02-02", RescheduleTimeOptionModel(), "reason")
            // then
            assert(reschedulePickupViewModel.saveRescheduleDetail.value is Success)
        }

    @Test
    fun `when save reschedule pickup then throws error`() =
        coroutineTestRule.runBlockingTest {
            //given
            coEvery { saveReschedulePickupUseCase(any()) } throws defaultThrowable
            //when
            reschedulePickupViewModel.saveReschedule("12345", "2022-02-02", RescheduleTimeOptionModel(), "reason")
            // then
            assert(reschedulePickupViewModel.saveRescheduleDetail.value is Fail)
        }
}