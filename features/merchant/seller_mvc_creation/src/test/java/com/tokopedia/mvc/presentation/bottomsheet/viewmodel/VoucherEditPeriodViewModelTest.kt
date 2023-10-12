package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvc.data.mapper.UpdateVoucherMapper
import com.tokopedia.mvc.domain.usecase.UpdateCouponFacadeUseCase
import com.tokopedia.mvc.util.DateTimeUtils
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class VoucherEditPeriodViewModelTest {

    private lateinit var viewModel: VoucherEditPeriodViewModel

    @RelaxedMockK
    lateinit var updateVoucherPeriodUseCase: UpdateCouponFacadeUseCase

    @RelaxedMockK
    lateinit var hourStartObserver: Observer<in String>

    @RelaxedMockK
    lateinit var hourEndObserver: Observer<in String>

    @RelaxedMockK
    lateinit var toShowDateToasterObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var startCalendarObserver: Observer<in Calendar>

    @RelaxedMockK
    lateinit var endCalendarObserver: Observer<in Calendar>

    @RelaxedMockK
    lateinit var updateVoucherPeriodStateObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var mapper: UpdateVoucherMapper

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = VoucherEditPeriodViewModel(
            CoroutineTestDispatchersProvider,
            updateVoucherPeriodUseCase,
            mapper
        )
        with(viewModel) {
            hourStartLiveData.observeForever(hourStartObserver)
            hourEndLiveData.observeForever(hourEndObserver)
            toShowDateToaster.observeForever(toShowDateToasterObserver)
            startDateCalendarLiveData.observeForever(startCalendarObserver)
            endDateCalendarLiveData.observeForever(endCalendarObserver)
            updateVoucherPeriodStateLiveData.observeForever(updateVoucherPeriodStateObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            hourStartLiveData.removeObserver(hourStartObserver)
            hourEndLiveData.removeObserver(hourEndObserver)
            toShowDateToaster.removeObserver(toShowDateToasterObserver)
            startDateCalendarLiveData.removeObserver(startCalendarObserver)
            endDateCalendarLiveData.removeObserver(endCalendarObserver)
            updateVoucherPeriodStateLiveData.removeObserver(updateVoucherPeriodStateObserver)
        }
    }

    @Test
    fun `when setStartDateTime() is called, but the end date is less than the start date, should set the data accordingly`() {
        // Given
        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.set(Calendar.YEAR, 2020)
        startDateCalendar.set(Calendar.MONTH, 10)
        startDateCalendar.set(Calendar.DATE, 10)

        val endDateCalendar = Calendar.getInstance()
        startDateCalendar.set(Calendar.YEAR, 2020)
        startDateCalendar.set(Calendar.MONTH, 1)
        startDateCalendar.set(Calendar.DATE, 10)

        val expectedStartDate = startDateCalendar
        val expectedEndDate = DateTimeUtils.getMaxDate(startDateCalendar as? GregorianCalendar)

        // When
        viewModel.setEndDateTime(endDateCalendar)
        viewModel.setStartDateTime(startDateCalendar)

        // Then
        val actualStartDate = viewModel.startDateCalendarLiveData.value
        val actualEndDate = viewModel.endDateCalendarLiveData.value
        assertEquals(expectedStartDate, actualStartDate)
        assertEquals(expectedEndDate, actualEndDate)
    }
}
