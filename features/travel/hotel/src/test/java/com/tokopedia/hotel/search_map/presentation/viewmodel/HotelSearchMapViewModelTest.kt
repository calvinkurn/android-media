package com.tokopedia.hotel.search_map.presentation.viewmodel

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.VisibleRegion
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.usecase.SearchPropertyUseCase
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HotelSearchMapViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val io: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val default: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val immediate: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val computation: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
    private lateinit var hotelSearchMapViewModel: HotelSearchMapViewModel

    private val travelTickerCoroutineUseCase = mockk<TravelTickerCoroutineUseCase>()
    private val searchPropertyUseCase = mockk<SearchPropertyUseCase>()

    @Before
    fun setUp() {
        hotelSearchMapViewModel = HotelSearchMapViewModel(dispatcher, searchPropertyUseCase, travelTickerCoroutineUseCase)
    }

    @Test
    fun initialState() {
        //when

        //then
        assert(hotelSearchMapViewModel.selectedSort.name.isEmpty())
        assert(hotelSearchMapViewModel.filter.price.maxPrice == 0)
        assert(!hotelSearchMapViewModel.isFilter)
    }

    @Test
    fun valueAssigned() {
        //when
        hotelSearchMapViewModel.selectedSort = Sort(name = "AA")
        hotelSearchMapViewModel.filter = Filter(Filter.FilterPrice(maxPrice = 100))
        hotelSearchMapViewModel.isFilter = true

        //then
        assert(hotelSearchMapViewModel.selectedSort.name == "AA")
        assert(hotelSearchMapViewModel.filter.price.maxPrice == 100)
        assert(hotelSearchMapViewModel.isFilter)
    }

    @Test
    fun initSearchParam_typeCity_shouldInitSearchParam() {
        //given
        val destinationId = 100.toLong()
        val type = HotelTypeEnum.CITY.value
        val latitude = 0.0
        val longitude = 0.0
        val radius = 0.0
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "lalala"

        //when
        hotelSearchMapViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, radius, ""))

        //then
        assert(hotelSearchMapViewModel.hotelSearchModel.id == destinationId)
        assert(hotelSearchMapViewModel.hotelSearchModel.name == cityName)
        assert(hotelSearchMapViewModel.hotelSearchModel.lat == latitude)
        assert(hotelSearchMapViewModel.hotelSearchModel.long == longitude)
        assert(hotelSearchMapViewModel.hotelSearchModel.checkIn == checkIn)
        assert(hotelSearchMapViewModel.hotelSearchModel.checkOut == checkOut)
        assert(hotelSearchMapViewModel.hotelSearchModel.room == totalRoom)
        assert(hotelSearchMapViewModel.hotelSearchModel.adult == totalAdult)
        assert(hotelSearchMapViewModel.searchParam.location.cityID == destinationId)
        assert(hotelSearchMapViewModel.searchParam.location.districtID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchMapViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchMapViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchMapViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchMapViewModel.searchParam.room == totalRoom)
        assert(hotelSearchMapViewModel.searchParam.guest.adult == totalAdult)
    }

    @Test
    fun initSearchParam_typeDistrict_shouldInitSearchParam() {
        //given
        val destinationId = 100.toLong()
        val type = HotelTypeEnum.DISTRICT.value
        val latitude = 0.0
        val longitude = 0.0
        val radius = 0.0
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "lala"

        //when
        hotelSearchMapViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName,
                type, totalRoom, totalAdult, latitude, longitude, radius, ""))

        //then
        assert(hotelSearchMapViewModel.searchParam.location.cityID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.districtID == destinationId)
        assert(hotelSearchMapViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchMapViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchMapViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchMapViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchMapViewModel.searchParam.room == totalRoom)
        assert(hotelSearchMapViewModel.searchParam.guest.adult == totalAdult)

    }

    @Test
    fun initSearchParam_typeRegion_shouldInitSearchParam() {
        //given
        val destinationId = 100.toLong()
        val type = HotelTypeEnum.REGION.value
        val latitude = 0.0
        val longitude = 0.0
        val radius = 0.0
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "name"

        //when
        hotelSearchMapViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, radius, ""))

        //then
        assert(hotelSearchMapViewModel.searchParam.location.cityID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.districtID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.regionID == destinationId.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchMapViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchMapViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchMapViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchMapViewModel.searchParam.room == totalRoom)
        assert(hotelSearchMapViewModel.searchParam.guest.adult == totalAdult)

    }

    @Test
    fun initSearchParam_typeCoordinate_shouldInitSearchParam() {
        //given
        val destinationId = 100.toLong()
        val latitude = 3.0
        val longitude = 4.0
        val radius = 22.0
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "name"
        val searchType = HotelTypeEnum.COORDINATE.value

        //when
        hotelSearchMapViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName,
                "", totalRoom, totalAdult, latitude, longitude, radius, searchType, ""))

        //then
        assert(hotelSearchMapViewModel.searchParam.location.cityID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.districtID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.regionID == 0.toLong())
        assert(hotelSearchMapViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchMapViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchMapViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchMapViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchMapViewModel.searchParam.room == totalRoom)
        assert(hotelSearchMapViewModel.searchParam.guest.adult == totalAdult)
        assert(hotelSearchMapViewModel.searchParam.location.searchType == searchType)

    }

    @Test
    fun searchProperty_shouldBeSuccessWithData() {
        //given
        val properties = listOf(Property(1), Property(2), Property(3))
        coEvery {
            searchPropertyUseCase.execute(any() as String, any())
        } returns Success(PropertySearch(properties))

        //when
        hotelSearchMapViewModel.searchProperty(0, "")

        //then
        assert(hotelSearchMapViewModel.liveSearchResult.value is Success)
        assert((hotelSearchMapViewModel.liveSearchResult.value as Success).data.properties.isNotEmpty())
    }

    @Test
    fun searchProperty_shouldReturnFail() {
        //given
        coEvery {
            searchPropertyUseCase.execute(any() as String, any())
        } returns Fail(Throwable())

        //when
        hotelSearchMapViewModel.searchProperty(0, "")

        //then
        assert(hotelSearchMapViewModel.liveSearchResult.value is Fail)
    }

    @Test
    fun addSort_sortByPopularity_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "popularity")

        //when
        hotelSearchMapViewModel.addSort(sort)

        //then
        assert(hotelSearchMapViewModel.searchParam.sort.popularity)
        assert(!hotelSearchMapViewModel.searchParam.sort.price)
        assert(!hotelSearchMapViewModel.searchParam.sort.ranking)
        assert(!hotelSearchMapViewModel.searchParam.sort.star)
        assert(!hotelSearchMapViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchMapViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByPrice_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "price")

        //when
        hotelSearchMapViewModel.addSort(sort)

        //then
        assert(!hotelSearchMapViewModel.searchParam.sort.popularity)
        assert(hotelSearchMapViewModel.searchParam.sort.price)
        assert(!hotelSearchMapViewModel.searchParam.sort.ranking)
        assert(!hotelSearchMapViewModel.searchParam.sort.star)
        assert(!hotelSearchMapViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchMapViewModel.searchParam.sort.sortDir == "asc")
    }

    @Test
    fun addSort_sortByRanking_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "ranking")

        //when
        hotelSearchMapViewModel.addSort(sort)

        //then
        assert(!hotelSearchMapViewModel.searchParam.sort.popularity)
        assert(!hotelSearchMapViewModel.searchParam.sort.price)
        assert(hotelSearchMapViewModel.searchParam.sort.ranking)
        assert(!hotelSearchMapViewModel.searchParam.sort.star)
        assert(!hotelSearchMapViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchMapViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByStar_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "star")

        //when
        hotelSearchMapViewModel.addSort(sort)

        //then
        assert(!hotelSearchMapViewModel.searchParam.sort.popularity)
        assert(!hotelSearchMapViewModel.searchParam.sort.price)
        assert(!hotelSearchMapViewModel.searchParam.sort.ranking)
        assert(hotelSearchMapViewModel.searchParam.sort.star)
        assert(!hotelSearchMapViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchMapViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByReviewScore_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "reviewscore")

        //when
        hotelSearchMapViewModel.addSort(sort)

        //then
        assert(!hotelSearchMapViewModel.searchParam.sort.popularity)
        assert(!hotelSearchMapViewModel.searchParam.sort.price)
        assert(!hotelSearchMapViewModel.searchParam.sort.ranking)
        assert(!hotelSearchMapViewModel.searchParam.sort.star)
        assert(hotelSearchMapViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchMapViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByElse_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "review")

        //when
        hotelSearchMapViewModel.addSort(sort)

        //then
        assert(!hotelSearchMapViewModel.searchParam.sort.popularity)
        assert(!hotelSearchMapViewModel.searchParam.sort.price)
        assert(!hotelSearchMapViewModel.searchParam.sort.ranking)
        assert(!hotelSearchMapViewModel.searchParam.sort.star)
        assert(!hotelSearchMapViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchMapViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addFilter_shouldUpdateFilterV2() {
        //given
        val filter = listOf(ParamFilterV2(name = "Filter 1", values = mutableListOf("aa", "bb")),
                ParamFilterV2(name = "Filter 2"))

        //when
        hotelSearchMapViewModel.addFilter(filter)

        //then
        assert(hotelSearchMapViewModel.getSelectedFilter().size == 1)
    }

    @Test
    fun addFilterWithQuickFilter_shouldUpdateFilterV2() {
        //given
        val quickFilters = listOf(QuickFilter(name = "hygiene verified", values = listOf("hygiene verified")),
                QuickFilter(name = "clean", values = listOf("clean")))
        val sortFilterItems = arrayListOf(SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_SELECTED),
                SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL))

        //when
        hotelSearchMapViewModel.addFilter(quickFilters, sortFilterItems)

        assert(hotelSearchMapViewModel.getSelectedFilter().size == 1)
    }

    @Test
    fun addFilterWithQuickFilter_shouldUpdateFilterV2_2() {
        //given
        val selectedFilter = listOf(ParamFilterV2(name = "hygiene verified", values = mutableListOf("hygiene verified")))
        hotelSearchMapViewModel.addFilter(selectedFilter)

        val quickFilters = listOf(QuickFilter(name = "hygiene verified", values = listOf("hygiene verified")),
                QuickFilter(name = "clean", values = listOf("clean")))
        val sortFilterItems = arrayListOf(SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_SELECTED),
                SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL))

        //when
        hotelSearchMapViewModel.addFilter(quickFilters, sortFilterItems)

        assert(hotelSearchMapViewModel.getSelectedFilter().size == 1)
    }

    @Test
    fun addFilterWithQuickFilter_shouldUpdateFilterV2_3() {
        //given
        val selectedFilter = listOf(ParamFilterV2(name = "hygiene verified", values = mutableListOf("hygiene verified")))
        hotelSearchMapViewModel.addFilter(selectedFilter)

        val quickFilters = listOf(QuickFilter(name = "hygiene verified", values = listOf("hygiene verified")),
                QuickFilter(name = "clean", values = listOf("clean")))
        val sortFilterItems = arrayListOf(SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL),
                SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL))

        //when
        hotelSearchMapViewModel.addFilter(quickFilters, sortFilterItems)

        assert(hotelSearchMapViewModel.getSelectedFilter().isEmpty())
    }

    @Test
    fun getTickerData_shouldData() {
        //given
        val title = "Title ABC"
        val message = "this is a message"
        val response = TravelTickerModel(title = title, message = message, url = "", type = 0, status = 0,
                endTime = "", startTime = "", instances = 0, page = "", isPeriod = true)
        coEvery {
            travelTickerCoroutineUseCase.execute(any(), any())
        } returns Success(response)

        //when
        hotelSearchMapViewModel.fetchTickerData()

        //then
        val actual = hotelSearchMapViewModel.tickerData.value
        assert(actual is Success)
        assert((actual as Success).data.title == title)
        assert(actual.data.message == message)
    }

    @Test
    fun getTickerData_shouldReturnFail() {
        //given
        coEvery {
            travelTickerCoroutineUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        hotelSearchMapViewModel.fetchTickerData()

        //then
        val actual = hotelSearchMapViewModel.tickerData.value
        assert(actual is Fail)
    }

    @Test
    fun getMidPoint_shouldReturnSuccess() {
        //given
        val latitude = 3.0
        val longitude = 4.0
        val latLong = LatLng(latitude, longitude)

        //when
        hotelSearchMapViewModel.getMidPoint(latLong)

        //then
        val actual = hotelSearchMapViewModel.screenMidPoint.value
        assert(actual is Success)
        assert((actual as Success).data.longitude == latLong.longitude)
        assert(actual.data.latitude == latLong.latitude)
    }

    @Test
    fun getMidPoint_shouldReturnFail() {
        //given
        val latitude = 0.0
        val longitude = 0.0
        val latLong = LatLng(latitude, longitude)

        //when
        hotelSearchMapViewModel.getMidPoint(latLong)

        //then
        val actual = hotelSearchMapViewModel.screenMidPoint.value
        assert(actual is Fail)
    }

    @Test
    fun getVisibleRadius_successToGetVisibleRadius() {
        // given
        val visibleRegion = VisibleRegion(
                LatLng(0.0, 0.0),
                LatLng(1.1, 1.1),
                LatLng(2.2, 2.2),
                LatLng(3.3, 3.3),
                LatLngBounds(LatLng(0.0, 0.0), LatLng(4.4, 4.4))
        )
        val googleMap = mockk<GoogleMap>()
        val distances = FloatArray(1)
        Location.distanceBetween(
                2.2,
                2.2,
                1.1,
                1.1,
                distances
        )

        coEvery { googleMap.projection.visibleRegion } returns visibleRegion

        // when
        hotelSearchMapViewModel.getVisibleRadius(googleMap)

        // then
        assert(hotelSearchMapViewModel.radius.value is Success)
        assert((hotelSearchMapViewModel.radius.value as Success).data == (distances[0] / 2).toDouble())
    }

    @Test
    fun getVisibleRadius_failedToGetVisibleRadius() {
        // given
        val googleMap = mockk<GoogleMap>()
        coEvery { googleMap.projection.visibleRegion } coAnswers { throw Throwable("Failed to get Visible Radius") }

        // when
        hotelSearchMapViewModel.getVisibleRadius(googleMap)

        // then
        assert(hotelSearchMapViewModel.radius.value is Fail)
        assert((hotelSearchMapViewModel.radius.value as Fail).throwable.message == "Failed to get Visible Radius")
    }

    @Test
    fun getFilterCount_whenNoFilterSelected_shouldReturnZero() {
        // given


        // when
        val filterCount = hotelSearchMapViewModel.getFilterCount()

        // then
        assert(filterCount == 0)
    }

    @Test
    fun getFilterCount_whenHaveFilterSelected_shouldReturnFilterCount() {
        // given
        val filter = listOf(
                ParamFilterV2(name = "Filter 1", values = mutableListOf("aa", "bb"), ),
                ParamFilterV2(name = "Price", values = mutableListOf("1.000")))
        hotelSearchMapViewModel.addFilter(filter)

        // when
        val filterCount = hotelSearchMapViewModel.getFilterCount()

        // then
        assert(filterCount == 3)
    }

    @Test
    fun onGetLocation_failedToGetLocation_LatLongShouldBeFail() {
        // given
        val deviceLocation = DeviceLocation(0.0, 0.0, 0)

        // when
        val onGetLocationFun = hotelSearchMapViewModel.onGetLocation()
        onGetLocationFun(deviceLocation)

        // then
        assert(hotelSearchMapViewModel.latLong.value is Fail)
    }

    @Test
    fun onGetLocation_successToGetLocation_LatLongShouldBeSuccessAndContainsLatLong() {
        // given
        val deviceLocation = DeviceLocation(1.12345, 2.54321, 0)

        // when
        val onGetLocationFun = hotelSearchMapViewModel.onGetLocation()
        onGetLocationFun(deviceLocation)

        // then
        assert(hotelSearchMapViewModel.latLong.value is Success)
        assert((hotelSearchMapViewModel.latLong.value as Success).data.first == deviceLocation.longitude)
        assert((hotelSearchMapViewModel.latLong.value as Success).data.second == deviceLocation.latitude)
    }
}
