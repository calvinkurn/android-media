package com.tokopedia.hotel.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

/**
 * @author by jessica on 26/03/20
 */

@RunWith(JUnit4::class)
class HotelSearchResultViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TravelTestDispatcherProvider()
    private lateinit var hotelSearchResultViewModel: HotelSearchResultViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelSearchResultViewModel = HotelSearchResultViewModel(graphqlRepository, dispatcher)
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
        val latitude = 0.0f
        val longitude = 0.0f
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "lalala"

        //when
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
        , type, totalRoom, totalAdult, latitude, longitude, "", ""))

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
        val latitude = 0.0f
        val longitude = 0.0f
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "lala"

        //when
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, "", ""))

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
        val latitude = 0.0f
        val longitude = 0.0f
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "name"

        //when
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , type, totalRoom, totalAdult, latitude, longitude, "", ""))

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
        val latitude = 3.0f
        val longitude = 4.0f
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4
        val cityName = "name"
        val searchType = HotelTypeEnum.COORDINATE.value

        //when
        hotelSearchResultViewModel.initSearchParam(HotelSearchModel(checkIn, checkOut, destinationId, cityName
                , "", totalRoom, totalAdult, latitude, longitude, searchType, ""))

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
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(PropertySearch.Response::class.java to PropertySearch.Response(PropertySearch(properties))),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelSearchResultViewModel.searchProperty(0, "")

        //then
        assert(hotelSearchResultViewModel.liveSearchResult.value is Success)
        assert((hotelSearchResultViewModel.liveSearchResult.value as Success).data.properties.isNotEmpty())
    }

    @Test
    fun searchProperty_shouldReturnFail() {
        //given
        val properties = listOf(Property(1), Property(2), Property(3))
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(PropertySearch.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

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
    fun addFilter_shouldUpdateFilter() {
        //given
        val filter = ParamFilter(maxPrice = 50000, minPrice = 30000)

        //when
        hotelSearchResultViewModel.addFilter(filter)

        //then
        assert(hotelSearchResultViewModel.searchParam.filter.maxPrice == 50000)
        assert(hotelSearchResultViewModel.searchParam.filter.minPrice == 30000)
        assert(hotelSearchResultViewModel.selectedFilter.maxPrice == 50000)
        assert(hotelSearchResultViewModel.isFilter)
    }
}