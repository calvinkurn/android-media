package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.domain.usecase.validation.PeriodValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PeriodValidation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class SetVoucherPeriodViewModelTest {

    companion object {
        private val DUMMY_CALENDAR =
                Calendar.Builder()
                        .setDate(1,1,1)
                        .build()

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"
    }

    @RelaxedMockK
    lateinit var periodValidationUseCase: PeriodValidationUseCase

    lateinit var mViewModel: SetVoucherPeriodViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = SetVoucherPeriodViewModel(CoroutineTestDispatchersProvider, periodValidationUseCase)
    }

    @Test
    fun `setting start date calendar will change use case param value and the calendar live data`() {
        with(mViewModel) {
            setStartDateCalendar(DUMMY_CALENDAR)

            assert(startDateCalendarLiveData.value == DUMMY_CALENDAR)
            assert(dateStartLiveData.value == DUMMY_CALENDAR.time.toFormattedString(DATE_FORMAT))
            assert(hourStartLiveData.value == DUMMY_CALENDAR.time.toFormattedString(HOUR_FORMAT))
        }
    }

    @Test
    fun `setting end date calendar will change use case param value and the calendar live data`() {
        with(mViewModel) {
            setEndDateCalendar(DUMMY_CALENDAR)

            assert(endDateCalendarLiveData.value == DUMMY_CALENDAR)
            assert(dateEndLiveData.value == DUMMY_CALENDAR.time.toFormattedString(DATE_FORMAT))
            assert(hourEndLiveData.value == DUMMY_CALENDAR.time.toFormattedString(HOUR_FORMAT))
        }
    }

    @Test
    fun `success validating voucher period`() = runBlocking {
        with(mViewModel) {
            val dummySuccessPeriodValidation = PeriodValidation()

            coEvery {
                periodValidationUseCase.executeOnBackground()
            } returns dummySuccessPeriodValidation

            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            validateVoucherPeriod()

            coVerify {
                periodValidationUseCase.executeOnBackground()
            }

            assert(periodValidationLiveData.value == Success(dummySuccessPeriodValidation))
        }
    }

    @Test
    fun `fail validating voucher period`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                periodValidationUseCase.executeOnBackground()
            } throws dummyThrowable

            setStartDateCalendar(DUMMY_CALENDAR)
            setEndDateCalendar(DUMMY_CALENDAR)

            validateVoucherPeriod()

            coVerify {
                periodValidationUseCase.executeOnBackground()
            }

            assert(periodValidationLiveData.value is Fail)
        }
    }

}