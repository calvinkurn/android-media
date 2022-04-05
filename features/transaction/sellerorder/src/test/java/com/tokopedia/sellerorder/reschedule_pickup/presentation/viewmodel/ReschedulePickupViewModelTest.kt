package com.tokopedia.sellerorder.reschedule_pickup.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.domain.GetReschedulePickupUseCase
import com.tokopedia.sellerorder.reschedule_pickup.domain.SaveReschedulePickupUseCase
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
            coEvery { getReschedulePickupUseCase.execute(any()) } returns GetReschedulePickupResponse.Data()
            //when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.reschedulePickupDetail.value is Success)
        }

    @Test
    fun `when get Reschedule Pickup Detail then throws error`() =
        coroutineTestRule.runBlockingTest {
            //given
            coEvery { getReschedulePickupUseCase.execute(any()) } throws defaultThrowable
            //when
            reschedulePickupViewModel.getReschedulePickupDetail("12345")
            // then
            assert(reschedulePickupViewModel.reschedulePickupDetail.value is Fail)
        }

    @Test
    fun `when save reschedule pickup then returns success`() =
        coroutineTestRule.runBlockingTest {
            //given
            coEvery { saveReschedulePickupUseCase.execute(any()) } returns SaveReschedulePickupResponse.Data()
            //when
            reschedulePickupViewModel.saveReschedule("12345", "2022-02-02", "18:00", "reason")
            // then
            assert(reschedulePickupViewModel.saveRescheduleDetail.value is Success)
        }

    @Test
    fun `when save reschedule pickup then throws error`() =
        coroutineTestRule.runBlockingTest {
            //given
            coEvery { saveReschedulePickupUseCase.execute(any()) } throws defaultThrowable
            //when
            reschedulePickupViewModel.saveReschedule("12345", "2022-02-02", "18:00", "reason")
            // then
            assert(reschedulePickupViewModel.saveRescheduleDetail.value is Fail)
        }
}