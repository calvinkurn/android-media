package com.tokopedia.hotel.search_map.presentation.viewmodel

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.usecase.SearchPropertyUseCase
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HotelSearchMapViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TravelTestDispatcherProvider()
    private lateinit var hotelSearchResultViewModel: HotelSearchMapViewModel

    private val travelTickerCoroutineUseCase = mockk<TravelTickerCoroutineUseCase>()
    private val searchPropertyUseCase = mockk<SearchPropertyUseCase>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelSearchResultViewModel = HotelSearchMapViewModel(dispatcher, searchPropertyUseCase, travelTickerCoroutineUseCase)
    }

    @Test
    fun initialState() {
        //when

        //then
        assert(hotelSearchResultViewModel.selectedSort.name.isEmpty())
        assert(hotelSearchResultViewModel.filter.price.maxPrice == 0)
        assert(!hotelSearchResultViewModel.isFilter)
    }

    @Test
    fun valueAssigned() {
        //when
        hotelSearchResultViewModel.selectedSort = Sort(name = "AA")
        hotelSearchResultViewModel.filter = Filter(Filter.FilterPrice(maxPrice = 100))
        hotelSearchResultViewModel.isFilter = true

        //then
        assert(hotelSearchResultViewModel.selectedSort.name == "AA")
        assert(hotelSearchResultViewModel.filter.price.maxPrice == 100)
        assert(hotelSearchResultViewModel.isFilter)
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
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, radius, ""))

        //then
        assert(hotelSearchResultViewModel.searchParam.location.cityID == destinationId)
        assert(hotelSearchResultViewModel.searchParam.location.districtID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchResultViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchResultViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchResultViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchResultViewModel.searchParam.room == totalRoom)
        assert(hotelSearchResultViewModel.searchParam.guest.adult == totalAdult)
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
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, radius, ""))

        //then
        assert(hotelSearchResultViewModel.searchParam.location.cityID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.districtID == destinationId.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchResultViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchResultViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchResultViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchResultViewModel.searchParam.room == totalRoom)
        assert(hotelSearchResultViewModel.searchParam.guest.adult == totalAdult)

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
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, radius, ""))

        //then
        assert(hotelSearchResultViewModel.searchParam.location.cityID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.districtID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.regionID == destinationId.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchResultViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchResultViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchResultViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchResultViewModel.searchParam.room == totalRoom)
        assert(hotelSearchResultViewModel.searchParam.guest.adult == totalAdult)

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
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , "", totalRoom, totalAdult, latitude, longitude, radius,searchType, ""))

        //then
        assert(hotelSearchResultViewModel.searchParam.location.cityID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.districtID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.regionID == 0.toLong())
        assert(hotelSearchResultViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchResultViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchResultViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchResultViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchResultViewModel.searchParam.room == totalRoom)
        assert(hotelSearchResultViewModel.searchParam.guest.adult == totalAdult)
        assert(hotelSearchResultViewModel.searchParam.location.searchType == searchType)

    }

    @Test
    fun searchProperty_shouldBeSuccessWithData() {
        //given
        val properties = listOf(Property(1), Property(2), Property(3))
        coEvery {
            searchPropertyUseCase.execute(any() as String, any())
        } returns Success(PropertySearch(properties))

        //when
        hotelSearchResultViewModel.searchProperty(0, "")

        //then
        assert(hotelSearchResultViewModel.liveSearchResult.value is Success)
        assert((hotelSearchResultViewModel.liveSearchResult.value as Success).data.properties.isNotEmpty())
    }

    @Test
    fun searchProperty_shouldReturnFail() {
        //given
        coEvery {
            searchPropertyUseCase.execute(any() as String, any())
        } returns Fail(Throwable())

        //when
        hotelSearchResultViewModel.searchProperty(0, "")

        //then
        assert(hotelSearchResultViewModel.liveSearchResult.value is Fail)
    }

    @Test
    fun addSort_sortByPopularity_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "popularity")

        //when
        hotelSearchResultViewModel.addSort(sort)

        //then
        assert(hotelSearchResultViewModel.searchParam.sort.popularity)
        assert(!hotelSearchResultViewModel.searchParam.sort.price)
        assert(!hotelSearchResultViewModel.searchParam.sort.ranking)
        assert(!hotelSearchResultViewModel.searchParam.sort.star)
        assert(!hotelSearchResultViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchResultViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByPrice_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "price")

        //when
        hotelSearchResultViewModel.addSort(sort)

        //then
        assert(!hotelSearchResultViewModel.searchParam.sort.popularity)
        assert(hotelSearchResultViewModel.searchParam.sort.price)
        assert(!hotelSearchResultViewModel.searchParam.sort.ranking)
        assert(!hotelSearchResultViewModel.searchParam.sort.star)
        assert(!hotelSearchResultViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchResultViewModel.searchParam.sort.sortDir == "asc")
    }

    @Test
    fun addSort_sortByRanking_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "ranking")

        //when
        hotelSearchResultViewModel.addSort(sort)

        //then
        assert(!hotelSearchResultViewModel.searchParam.sort.popularity)
        assert(!hotelSearchResultViewModel.searchParam.sort.price)
        assert(hotelSearchResultViewModel.searchParam.sort.ranking)
        assert(!hotelSearchResultViewModel.searchParam.sort.star)
        assert(!hotelSearchResultViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchResultViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByStar_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "star")

        //when
        hotelSearchResultViewModel.addSort(sort)

        //then
        assert(!hotelSearchResultViewModel.searchParam.sort.popularity)
        assert(!hotelSearchResultViewModel.searchParam.sort.price)
        assert(!hotelSearchResultViewModel.searchParam.sort.ranking)
        assert(hotelSearchResultViewModel.searchParam.sort.star)
        assert(!hotelSearchResultViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchResultViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByReviewScore_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "reviewscore")

        //when
        hotelSearchResultViewModel.addSort(sort)

        //then
        assert(!hotelSearchResultViewModel.searchParam.sort.popularity)
        assert(!hotelSearchResultViewModel.searchParam.sort.price)
        assert(!hotelSearchResultViewModel.searchParam.sort.ranking)
        assert(!hotelSearchResultViewModel.searchParam.sort.star)
        assert(hotelSearchResultViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchResultViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addSort_sortByElse_shouldAssignedToSelectedSort() {
        //given
        val sort = Sort(name = "review")

        //when
        hotelSearchResultViewModel.addSort(sort)

        //then
        assert(!hotelSearchResultViewModel.searchParam.sort.popularity)
        assert(!hotelSearchResultViewModel.searchParam.sort.price)
        assert(!hotelSearchResultViewModel.searchParam.sort.ranking)
        assert(!hotelSearchResultViewModel.searchParam.sort.star)
        assert(!hotelSearchResultViewModel.searchParam.sort.reviewScore)
        assert(hotelSearchResultViewModel.searchParam.sort.sortDir == "desc")
    }

    @Test
    fun addFilter_shouldUpdateFilterV2() {
        //given
        val filter = listOf(ParamFilterV2(name = "Filter 1", values = mutableListOf("aa", "bb")),
                ParamFilterV2(name = "Filter 2"))

        //when
        hotelSearchResultViewModel.addFilter(filter)

        //then
        assert(hotelSearchResultViewModel.getSelectedFilter().size == 1)
    }

    @Test
    fun addFilterWithQuickFilter_shouldUpdateFilterV2() {
        //given
        val quickFilters= listOf(QuickFilter(name = "hygiene verified", values = listOf("hygiene verified")),
                QuickFilter(name = "clean", values = listOf("clean")))
        val sortFilterItems = arrayListOf(SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_SELECTED),
                SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL))

        //when
        hotelSearchResultViewModel.addFilter(quickFilters, sortFilterItems)

        assert(hotelSearchResultViewModel.getSelectedFilter().size == 1)
    }

    @Test
    fun addFilterWithQuickFilter_shouldUpdateFilterV2_2() {
        //given
        val selectedFilter = listOf(ParamFilterV2(name = "hygiene verified", values = mutableListOf("hygiene verified")))
        hotelSearchResultViewModel.addFilter(selectedFilter)

        val quickFilters= listOf(QuickFilter(name = "hygiene verified", values = listOf("hygiene verified")),
                QuickFilter(name = "clean", values = listOf("clean")))
        val sortFilterItems = arrayListOf(SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_SELECTED),
                SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL))

        //when
        hotelSearchResultViewModel.addFilter(quickFilters, sortFilterItems)

        assert(hotelSearchResultViewModel.getSelectedFilter().size == 1)
    }

    @Test
    fun addFilterWithQuickFilter_shouldUpdateFilterV2_3() {
        //given
        val selectedFilter = listOf(ParamFilterV2(name = "hygiene verified", values = mutableListOf("hygiene verified")))
        hotelSearchResultViewModel.addFilter(selectedFilter)

        val quickFilters= listOf(QuickFilter(name = "hygiene verified", values = listOf("hygiene verified")),
                QuickFilter(name = "clean", values = listOf("clean")))
        val sortFilterItems = arrayListOf(SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL),
                SortFilterItem("hygiene  verified", type = ChipsUnify.TYPE_NORMAL))

        //when
        hotelSearchResultViewModel.addFilter(quickFilters, sortFilterItems)

        assert(hotelSearchResultViewModel.getSelectedFilter().isEmpty())
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
        hotelSearchResultViewModel.fetchTickerData()

        //then
        val actual = hotelSearchResultViewModel.tickerData.value
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
        hotelSearchResultViewModel.fetchTickerData()

        //then
        val actual = hotelSearchResultViewModel.tickerData.value
        assert(actual is Fail)
    }

    @Test
    fun setPermissionHelper_shouldReturnSuccess(){
        //given
        val permissionCheckerHelper: PermissionCheckerHelper = mockk()

        //when
        hotelSearchResultViewModel.setPermissionHelper(permissionCheckerHelper)

        //then
        val actual = hotelSearchResultViewModel.permissionCheckerHelper
        assert(actual == permissionCheckerHelper)
    }

    @Test
    fun getMidPoint_shouldReturnSuccess(){
        //given
        val latitude = 3.0
        val longitude = 4.0
        val latLong = LatLng(latitude,longitude)

        //when
        hotelSearchResultViewModel.getMidPoint(latLong)

        //then
        val actual = hotelSearchResultViewModel.screenMidPoint.value
        assert(actual is Success)
        assert((actual as Success).data.longitude == latLong.longitude)
        assert(actual.data.latitude == latLong.latitude)
    }

    @Test
    fun getMidPoint_shouldReturnFail(){
        //given
        val latitude = 0.0
        val longitude = 0.0
        val latLong = LatLng(latitude,longitude)

        //when
        hotelSearchResultViewModel.getMidPoint(latLong)

        //then
        val actual = hotelSearchResultViewModel.screenMidPoint.value
        assert(actual is Fail)
    }

    @Test
    fun getVisibleRadius_positiveFlow(){
        //given
        val googleMap = mockk<GoogleMap>()
        val throwable = mockk<Throwable>()
        val radius: Double = 30.9
        try {
            val visibleRegion: VisibleRegion = mockk()
            val diagonalDistance = FloatArray(1)

            val farLeft = visibleRegion.farLeft
            val nearRight = visibleRegion.nearRight

            Location.distanceBetween(
                    farLeft.latitude,
                    farLeft.longitude,
                    nearRight.latitude,
                    nearRight.longitude,
                    diagonalDistance
            )
            //when
            hotelSearchResultViewModel.getVisibleRadius(googleMap)
            //then
            val actual = hotelSearchResultViewModel.radius.value
            assert(actual is Success)
            assert((actual as Success).data == radius)
        } catch (error: Throwable) {
            //given
            every { hotelSearchResultViewModel.getVisibleRadius(googleMap) } throws throwable

            //when
            hotelSearchResultViewModel.getVisibleRadius(googleMap)

            //then
            val actual = hotelSearchResultViewModel.radius.value
            assert(actual is Fail)
        }
    }
}
