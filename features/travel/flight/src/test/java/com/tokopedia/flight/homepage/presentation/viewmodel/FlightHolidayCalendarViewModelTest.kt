package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.dummy.HOLIDAY_EMPTY_DATA
import com.tokopedia.flight.dummy.HOLIDAY_WITH_DATA
import com.tokopedia.flight.shouldBe
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 23/06/2020
 */
class FlightHolidayCalendarViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: TravelCalendarHolidayUseCase = mockk()

    private lateinit var viewModel: FlightHolidayCalendarViewModel

    @Before
    fun setUp() {
        viewModel = FlightHolidayCalendarViewModel(useCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun getCalendarHoliday_failedToFetch_holidayCalendarDataShouldBeEmpty() {
        // given
        coEvery { useCase.execute() } returns Fail(Throwable("Failed"))

        // when
        viewModel.getCalendarHoliday()

        // then
        viewModel.holidayCalendarData.value?.size shouldBe 0
    }

    @Test
    fun getCalendarHoliday_successToFetchButEmpty_holidayCalendarDataShouldBeEmpty() {
        // given
        coEvery { useCase.execute() } returns Success(HOLIDAY_EMPTY_DATA)

        // when
        viewModel.getCalendarHoliday()

        // then
        viewModel.holidayCalendarData.value?.size shouldBe 0
    }

    @Test
    fun getCalendarHoliday_successToFetchAndContainsData_holidayCalendarDataShouldBeDummyData() {
        // given
        coEvery { useCase.execute() } returns Success(HOLIDAY_WITH_DATA)

        // when
        viewModel.getCalendarHoliday()

        // then
        viewModel.holidayCalendarData.value?.size shouldBe HOLIDAY_WITH_DATA.data.size

        for ((index, item) in viewModel.holidayCalendarData.value!!.withIndex()) {
            item.getTitle() shouldBe HOLIDAY_WITH_DATA.data[index].attribute.label
            item.getDate().time shouldBe TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, HOLIDAY_WITH_DATA.data[index].attribute.date).time
        }
    }
}