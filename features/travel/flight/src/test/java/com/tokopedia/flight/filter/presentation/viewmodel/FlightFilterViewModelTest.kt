package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
import com.tokopedia.flight.searchV4.domain.FlightSearchCountUseCase
import com.tokopedia.flight.searchV4.domain.FlightSearchStatisticsUseCase
import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel
import com.tokopedia.flight.searchV4.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.searchV4.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.searchV4.presentation.model.filter.TransitEnum
import com.tokopedia.flight.searchV4.presentation.model.statistics.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 2020-02-26
 */

class FlightFilterViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var flightSearchCountUseCase: FlightSearchCountUseCase

    @RelaxedMockK
    lateinit var flightSearchStatisticsUseCase: FlightSearchStatisticsUseCase

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var flightFilterViewModel: FlightFilterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        flightFilterViewModel = FlightFilterViewModel(flightSearchCountUseCase, flightSearchStatisticsUseCase, dispatcher)
        flightFilterViewModel.filterViewData.observeForever { }
    }

    @Test
    fun init_shouldInitFilterAndSortValue() {
        //given
        val filterViewModel = FlightFilterModel()
        filterViewModel.priceMin = 100
        val selectedSort = TravelSortOption.EARLIEST_ARRIVAL

        //when
        flightFilterViewModel.init(selectedSort, filterViewModel)

        //then
        val actualFilterModel = flightFilterViewModel.filterModel.value
        assert(actualFilterModel != null && actualFilterModel == filterViewModel)
        assert(actualFilterModel?.priceMin == filterViewModel.priceMin)

        val actualSelectedSort = flightFilterViewModel.selectedSort.value
        assert(actualSelectedSort != null && actualSelectedSort == selectedSort)
    }

    @Test
    fun setStatistics_shouldSetStatisticLiveData() {
        //given
        val statistics = FlightSearchStatisticModel(30, 100000, 60, 90,
                listOf(), listOf(), listOf(), listOf(), listOf(), false, true, true,
                true, false)

        //when
        flightFilterViewModel.setStatistics(statistics)

        //then
        val actualStatistics = flightFilterViewModel.statisticModel.value
        assert(actualStatistics != null)
        actualStatistics?.let {
            assert(it.minPrice == 30)
            assert(it.maxPrice == 100000)
        }

    }

    @Test
    fun getStatistics_shouldReturnStatisticModel() {
        //given
        val transitStats = mutableListOf<TransitStat>()
        transitStats.add(TransitStat(TransitEnum.ONE, 30, "30"))

        val airlineStats = mutableListOf<AirlineStat>()
        for (i in 0 until 3) {
            airlineStats.add(AirlineStat(FlightAirlineModel(i.toString(), "", "", ""), 0, ""))
        }

        val departureStats = mutableListOf<DepartureStat>()
        departureStats.add(DepartureStat(DepartureTimeEnum._00, 0, ""))
        departureStats.add(DepartureStat(DepartureTimeEnum._06, 30, "30"))

        val arrivalStats = mutableListOf<DepartureStat>()
        arrivalStats.add(DepartureStat(DepartureTimeEnum._00, 0, ""))

        val refundableStats = mutableListOf<RefundableStat>()
        refundableStats.add(RefundableStat(RefundableEnum.REFUNDABLE, 0, ""))

        val statistics = FlightSearchStatisticModel(30, 60, 60, 90,
                transitStats, airlineStats, departureStats, arrivalStats, refundableStats, false,
                true, true, true, true)

        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns statistics

        //when
        flightFilterViewModel.init(0, FlightFilterModel())

        //then
        val actualStatisticModel = flightFilterViewModel.statisticModel.value
        assert(actualStatisticModel != null)
        assert(actualStatisticModel?.transitTypeStatList?.size == 1)
        assert(actualStatisticModel?.airlineStatList?.size == 3)
        assert(actualStatisticModel?.departureTimeStatList?.size == 2)
        assert(actualStatisticModel?.arrivalTimeStatList?.size == 1)
        assert(actualStatisticModel?.refundableTypeStatList?.size == 1)
        assert(actualStatisticModel?.minPrice == 30)
        assert(actualStatisticModel?.maxPrice == 60)
        assert(actualStatisticModel?.minDuration == 60)
        assert(actualStatisticModel?.maxDuration == 90)
        assert(actualStatisticModel?.isHaveSpecialPrice == false)
        assert(actualStatisticModel?.isHaveBaggage == true)
        assert(actualStatisticModel?.isHaveInFlightMeal == true)
    }

    @Test
    fun setSelectedSort_shouldSetSelectedSortData() {
        //given
        val selectedSort = TravelSortOption.EARLIEST_ARRIVAL

        //when
        flightFilterViewModel.setSelectedSort(selectedSort)

        //then
        assert(flightFilterViewModel.selectedSort.value == selectedSort)
    }

    @Test
    fun filterTransit_shouldUpdateFilterModel() {
        //given
        flightFilterViewModel.init(0, FlightFilterModel())
        val selectedTransit = listOf(TransitEnum.DIRECT, TransitEnum.ONE)

        //when
        flightFilterViewModel.filterTransit(selectedTransit)

        //then
        val actualSelectedTransit = flightFilterViewModel.filterModel.value?.transitTypeList
                ?: mutableListOf()
        assert(actualSelectedTransit.size == selectedTransit.size)
        assert(actualSelectedTransit[0] == selectedTransit[0])
    }

    @Test
    fun filterDepartureTime_shouldUpdateFilterModel() {
        //given
        flightFilterViewModel.init(0, FlightFilterModel())
        val selectedDepartureTime = listOf(DepartureTimeEnum._18, DepartureTimeEnum._00)

        //when
        flightFilterViewModel.filterDepartureTime(selectedDepartureTime)

        //then
        val actualDepartureTime = flightFilterViewModel.filterModel.value?.departureTimeList
                ?: mutableListOf()
        assert(actualDepartureTime.size == selectedDepartureTime.size)
        assert(actualDepartureTime[0] == selectedDepartureTime[0])
    }

    @Test
    fun filterArrivalTime_shouldUpdateFilterModel() {
        //given
        flightFilterViewModel.init(0, FlightFilterModel())
        val selectedArrivalTime = listOf(DepartureTimeEnum._18, DepartureTimeEnum._00)

        //when
        flightFilterViewModel.filterArrivalTime(selectedArrivalTime)

        //then
        val actualArrivalTime = flightFilterViewModel.filterModel.value?.arrivalTimeList
                ?: mutableListOf()
        assert(actualArrivalTime.size == selectedArrivalTime.size)
        assert(actualArrivalTime[0] == selectedArrivalTime[0])
    }

    @Test
    fun filterAirlines_shouldUpdateFilterModel() {
        //given
        flightFilterViewModel.init(0, FlightFilterModel())
        val selectedAirlines = listOf("Garuda", "Air Asia")

        //when
        flightFilterViewModel.filterAirlines(selectedAirlines)

        //then
        val actualSelectedAirlines = flightFilterViewModel.filterModel.value?.airlineList
                ?: mutableListOf()
        assert(actualSelectedAirlines.size == selectedAirlines.size)
        assert(actualSelectedAirlines[0] == selectedAirlines[0])
    }

    @Test
    fun filterFacilities_shouldUpdateFilterModel() {
        //given
        flightFilterViewModel.init(0, FlightFilterModel())
        val selectedFacilities = listOf(FlightFilterFacilityEnum.BAGGAGE)

        //when
        flightFilterViewModel.filterFacilities(selectedFacilities)

        //then
        val actualSelectedFacilities = flightFilterViewModel.filterModel.value?.facilityList
                ?: mutableListOf()
        assert(actualSelectedFacilities.size == selectedFacilities.size)
        assert(actualSelectedFacilities[0] == selectedFacilities[0])
    }

    @Test
    fun filterPrices_shouldUpdateFilterModel() {
        //given
        flightFilterViewModel.init(0, FlightFilterModel())
        val priceMin = 1000
        val priceMax = 3000

        //when
        flightFilterViewModel.filterPrices(priceMin, priceMax)

        //then
        assert(flightFilterViewModel.filterModel.value?.priceMin == priceMin)
        assert(flightFilterViewModel.filterModel.value?.priceMax == priceMax)
    }

    @Test
    fun getFlightCount_shouldReturnFlightCount() {
        //given
        coEvery {
            flightSearchCountUseCase.execute(any())
        } returns 33

        //when
        flightFilterViewModel.init(0, FlightFilterModel())

        //then
        assert(flightFilterViewModel.flightCount.value == 33)
    }

    @Test
    fun resetFilter_shouldResetFilterModel() {
        //given
        val filterModel = FlightFilterModel()
        filterModel.priceMin = 90000
        filterModel.airlineList = mutableListOf("Garuda")

        //when
        flightFilterViewModel.resetFilter()

        //then
        val actualFilterModel = flightFilterViewModel.filterModel.value ?: FlightFilterModel()
        assert(!actualFilterModel.isHasFilter)
        assert(actualFilterModel.priceMin == 0)
        assert(actualFilterModel.airlineList.isEmpty())
        assert(flightFilterViewModel.selectedSort.value == FlightFilterViewModel.SORT_DEFAULT_VALUE)
    }

    @Test
    fun resetFilter_withNullFilter_shouldResetFilterModel() {
        //given
        val flightFilterViewModel = FlightFilterViewModel(flightSearchCountUseCase, flightSearchStatisticsUseCase, CoroutineTestDispatchersProvider)

        //when
        flightFilterViewModel.resetFilter()

        //then
        val actualFilterModel = flightFilterViewModel.filterModel.value ?: FlightFilterModel()
        assert(!actualFilterModel.isHasFilter)
        assert(actualFilterModel.priceMin == 0)
        assert(actualFilterModel.airlineList.isEmpty())
        assert(flightFilterViewModel.selectedSort.value == FlightFilterViewModel.SORT_DEFAULT_VALUE)
    }

    @Test
    fun resetFilter_shouldReturnDefaultFilterModel() {
        //when
        flightFilterViewModel.resetFilter()

        //then
        val actualFilterModel = flightFilterViewModel.filterModel.value
        assert(actualFilterModel?.isHasFilter == false)
    }

    @Test
    fun getAirlineList_shouldReturnEmptyListWhenDataNull() {
        //given
        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns FlightSearchStatisticModel(0, 1000, 60, 90,
                listOf(), null, listOf(), listOf(), listOf(), false,
                true, true, false, true)
        flightFilterViewModel.init(0, FlightFilterModel())

        //when
        val airlineList = flightFilterViewModel.getAirlineList()

        //then
        assert(airlineList.isEmpty())
    }

    @Test
    fun getAirlineList_shouldReturnEmptyDataWhenStatisticNull() {
        //when
        val airlineList = flightFilterViewModel.getAirlineList()

        //then
        assert(airlineList.isEmpty())
    }

    @Test
    fun getAirlineList_shouldReturnEmptyData() {
        //given
        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns FlightSearchStatisticModel(0, 1000, 60, 90,
                listOf(), listOf(), listOf(), listOf(), listOf(), false,
                true, true, true, false)
        flightFilterViewModel.init(0, FlightFilterModel())

        //when
        val airlineList = flightFilterViewModel.getAirlineList()

        //then
        assert(airlineList.isEmpty())
    }

    @Test
    fun getAirlineList_shouldReturnThreeAirline() {
        //given
        val airlinesMockData = mutableListOf<AirlineStat>()
        for (i in 0 until 3) {
            airlinesMockData.add(AirlineStat(FlightAirlineModel(i.toString(), "", "", ""), 0, ""))
        }
        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns FlightSearchStatisticModel(0, 1000, 60, 90,
                listOf(), airlinesMockData, listOf(), listOf(), listOf(), false, true, true,
                false, false)
        flightFilterViewModel.init(0, FlightFilterModel())

        //when
        val airlineList = flightFilterViewModel.getAirlineList()

        //then
        assert(airlineList.isNotEmpty())
        assert(airlineList.size == 3)
        assert(airlineList[0].airlineDB.id == "0")
    }

    @Test
    fun mapStatisticToModel_shouldReturnEmptyList() {
        //given
        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns null

        //when
        flightFilterViewModel.init(0, FlightFilterModel())

        //then
        assert(flightFilterViewModel.filterViewData.value?.isEmpty() ?: false)

    }

    @Test
    fun mapStatisticToModel_shouldReturnFilterViewData() {
        //given
        val statistics = FlightSearchStatisticModel(0, 100000, 60, 90,
                listOf(), listOf(), listOf(), listOf(), listOf(), false, true, true,
                true, true)
        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns statistics

        val filterModel = FlightFilterModel()
        filterModel.priceMin = 20000
        filterModel.priceMax = 80000

        //when
        flightFilterViewModel.init(0, filterModel)

        //then
        assert(flightFilterViewModel.filterViewData.value?.isNotEmpty() ?: false)
        val priceRange = (flightFilterViewModel.filterViewData?.value?.get(FlightFilterViewModel.PRICE_ORDER) as PriceRangeModel)
        assert(priceRange.initialStartValue == 0)
        assert(priceRange.initialEndValue == 100000)
        assert(priceRange.selectedStartValue == filterModel.priceMin)
        assert(priceRange.selectedEndValue == filterModel.priceMax)
    }

    @Test
    fun mapStatisticToModel_shouldReturnDefaultMinPriceAndMaxPrice() {
        //given
        val statistics = FlightSearchStatisticModel(30, 100000, 60, 90,
                listOf(), listOf(), listOf(), listOf(), listOf(), false, true, true,
        false, true)
        coEvery {
            flightSearchStatisticsUseCase.execute(any())
        } returns statistics

        //when
        flightFilterViewModel.setStatistics(statistics)

        //then
        assert(flightFilterViewModel.filterViewData.value?.isNotEmpty() ?: false)
        val priceRange = (flightFilterViewModel.filterViewData?.value?.get(FlightFilterViewModel.PRICE_ORDER) as PriceRangeModel)
        assert(priceRange.selectedStartValue == statistics.minPrice)
        assert(priceRange.selectedEndValue == statistics.maxPrice)
    }

}