package com.tokopedia.hotel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelMetaModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.domain.TravelRecentSearchUseCase
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.usecase.GetPropertyPopularUseCase
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelDeleteRecentSearchEntity
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPropertyDefaultHome
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

/**
 * @author by jessica on 2020-01-09
 */

@RunWith(JUnit4::class)
class HotelHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getTravelCollectiveBannerUseCase: GetTravelCollectiveBannerUseCase

    @RelaxedMockK
    lateinit var travelRecentSearchUseCase: TravelRecentSearchUseCase

    @RelaxedMockK
    lateinit var getPropertyPopularUseCase: GetPropertyPopularUseCase

    private val travelTickerCoroutineUseCase = mockk<TravelTickerCoroutineUseCase>()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelHomepageViewModel: HotelHomepageViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelHomepageViewModel = HotelHomepageViewModel(this.graphqlRepository, getTravelCollectiveBannerUseCase,
                travelRecentSearchUseCase, getPropertyPopularUseCase, travelTickerCoroutineUseCase, dispatcher)
    }

    @Test
    fun getHotelPromo_shouldReturnNoBanner() {
        //given
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(TravelCollectiveBannerModel())

        //when
        hotelHomepageViewModel.getHotelPromo()
        hotelHomepageViewModel.fetchVideoBannerData()

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Success)
        assert(hotelHomepageViewModel.videoBannerLiveData.value is Success)
    }

    @Test
    fun getHotelPromo_shouldReturn1Data() {
        //given
        val listOfBanner = mutableListOf<TravelCollectiveBannerModel.Banner>()
        listOfBanner.add(TravelCollectiveBannerModel.Banner(id = "1"))
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(TravelCollectiveBannerModel(banners = listOfBanner))

        //when
        hotelHomepageViewModel.getHotelPromo()
        hotelHomepageViewModel.fetchVideoBannerData()

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Success)
        assert((actual as Success).data.banners.size == 1)
        assert(actual.data.banners[0].id.equals("1"))

        val videoData = hotelHomepageViewModel.videoBannerLiveData.value
        assert(videoData is Success)
        assert((videoData as Success).data.banners.size == 1)
        assert(videoData.data.banners[0].id.equals("1"))
    }

    @Test
    fun getHotelPromo_shouldReturn5Data() {

        //given
        val listOfBanner = mutableListOf<TravelCollectiveBannerModel.Banner>()
        for (i in 1..5) listOfBanner.add(TravelCollectiveBannerModel.Banner(i.toString()))
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(TravelCollectiveBannerModel(banners = listOfBanner))

        //when
        hotelHomepageViewModel.getHotelPromo()
        hotelHomepageViewModel.fetchVideoBannerData()

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Success)
        assert((actual as Success).data.banners.size == 5)
        assert(actual.data.banners[2].id.equals("3"))

        val videoData = hotelHomepageViewModel.videoBannerLiveData.value
        assert(videoData is Success)
        assert((videoData as Success).data.banners.size == 5)
        assert(videoData.data.banners[2].id.equals("3"))
    }

    @Test
    fun getHotelPromo_shouldReturnFail() {

        //given
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any())
        } returns Fail(Throwable("Failing"))

        //when
        hotelHomepageViewModel.getHotelPromo()
        hotelHomepageViewModel.fetchVideoBannerData()

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Fail)
        assert((actual as Fail).throwable.message.equals("Failing"))

        val videoData = hotelHomepageViewModel.videoBannerLiveData.value
        assert(videoData is Fail)
        assert((videoData as Fail).throwable.message.equals("Failing"))
    }

    @Test
    fun getPropertyPopular_shouldReturnPropertyPopular() {
        //given
        val popularSearches = mutableListOf<PopularSearch>()
        for (i in 0..3) {
            popularSearches.add(PopularSearch(i.toLong()))
        }
        coEvery { getPropertyPopularUseCase.executeOnBackground() } returns popularSearches

        //when
        hotelHomepageViewModel.getPopularCitiesData()

        //then
        assert(hotelHomepageViewModel.popularCitiesLiveData.value is Success)
        assert((hotelHomepageViewModel.popularCitiesLiveData.value as Success).data.size == 4)
        assert((hotelHomepageViewModel.popularCitiesLiveData.value as Success).data[1].destinationId == 1L)
    }

    @Test
    fun getPropertyPopular_shouldReturnFailed() {
        //given
        coEvery { getPropertyPopularUseCase.executeOnBackground() } throws Throwable()

        //when
        hotelHomepageViewModel.getPopularCitiesData()

        //then
        assert(hotelHomepageViewModel.popularCitiesLiveData.value is Fail)
    }

    @Test
    fun getDefaultHomeParameter_shouldReturnData() {
        //given
        val defaultHomeData = HotelPropertyDefaultHome(label = "Jakarta", searchId = "538")
        val data = HotelPropertyDefaultHome.Response(
                HotelPropertyDefaultHome.PropertyDefaultHomeMetaAndData(
                        data = defaultHomeData
                ))
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns
                GraphqlResponse(mapOf<Type, Any>(HotelPropertyDefaultHome.Response::class.java to data),
                        mapOf<Type, List<GraphqlError>>(), false)

        //when
        hotelHomepageViewModel.getDefaultHomepageParameter("")

        //then
        assert(hotelHomepageViewModel.homepageDefaultParam.value != null)
        assert((hotelHomepageViewModel.homepageDefaultParam.value as HotelPropertyDefaultHome).label == "Jakarta")
        assert((hotelHomepageViewModel.homepageDefaultParam.value as HotelPropertyDefaultHome).searchId == "538")
    }

    @Test
    fun getDefaultHomeParameter_shouldReturnEmptyAndNull() {
        //given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns
                GraphqlResponse(mapOf<Type, Any>(),
                        mapOf<Type, List<GraphqlError>>(), false)

        //when
        hotelHomepageViewModel.getDefaultHomepageParameter("")

        //then
        assert(hotelHomepageViewModel.homepageDefaultParam.value == null)
    }

    @Test
    fun getRecentSearch_shouldReturnRecentSearches() {
        //given
        val title = "title1"
        val list = listOf(TravelRecentSearchModel.Item(title = "0"), TravelRecentSearchModel.Item(title = "1"))
        val recentSearchesDummy = TravelRecentSearchModel(items = list, travelMeta = TravelMetaModel(title = title))
        coEvery { travelRecentSearchUseCase.execute(any(), true) } returns recentSearchesDummy

        //when
        hotelHomepageViewModel.getRecentSearch("")

        //then
        assert(hotelHomepageViewModel.recentSearch.value != null)
        assert(hotelHomepageViewModel.recentSearch.value is Success)
        (hotelHomepageViewModel.recentSearch.value as Success).let {
            assert(it.data.items.size == 2)
            assert(it.data.title == title)
            for ((index, item) in it.data.items.withIndex()) {
                assert(item.title == index.toString())
            }
        }
    }

    @Test
    fun getRecentSearch_returnThrowableShouldBeFail() {
        //given
        coEvery { travelRecentSearchUseCase.execute(any(), true) } coAnswers { throw Throwable() }

        //when
        hotelHomepageViewModel.getRecentSearch("")

        //then
        assert(hotelHomepageViewModel.recentSearch.value != null)
        assert(hotelHomepageViewModel.recentSearch.value is Fail)
    }

    @Test
    fun deleteRecentSearch_shouldReturnSuccess() {
        //given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns
                GraphqlResponse(mapOf<Type, Any>(HotelDeleteRecentSearchEntity.Response::class.java to HotelDeleteRecentSearchEntity.Response(HotelDeleteRecentSearchEntity(true))),
                        mapOf<Type, List<GraphqlError>>(), false)

        //when
        hotelHomepageViewModel.deleteRecentSearch("")

        //then
        assert(hotelHomepageViewModel.deleteRecentSearch.value != null)
        assert(hotelHomepageViewModel.deleteRecentSearch.value is Success)
        assert((hotelHomepageViewModel.deleteRecentSearch.value as Success).data)
    }

    @Test
    fun deleteRecentSearch_shouldReturnFail() {
        //given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns
                GraphqlResponse(mapOf<Type, Any>(),
                        mapOf<Type, List<GraphqlError>>(GraphqlError::class.java to listOf(GraphqlError())), false)

        //when
        hotelHomepageViewModel.deleteRecentSearch("")

        //then
        assert(hotelHomepageViewModel.deleteRecentSearch.value != null)
        assert(hotelHomepageViewModel.deleteRecentSearch.value is Fail)
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
        hotelHomepageViewModel.fetchTickerData()

        //then
        val actual = hotelHomepageViewModel.tickerData.value
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
        hotelHomepageViewModel.fetchTickerData()

        //then
        val actual = hotelHomepageViewModel.tickerData.value
        assert(actual is Fail)
    }
}