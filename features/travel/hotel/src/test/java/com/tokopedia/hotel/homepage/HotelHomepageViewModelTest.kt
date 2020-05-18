package com.tokopedia.hotel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelMetaModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.domain.TravelRecentSearchUseCase
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelDeleteRecentSearchEntity
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

    private val dispatcher = TravelTestDispatcherProvider()
    private lateinit var hotelHomepageViewModel: HotelHomepageViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelHomepageViewModel = HotelHomepageViewModel(this.graphqlRepository, getTravelCollectiveBannerUseCase, travelRecentSearchUseCase, dispatcher)
    }

    @Test
    fun getHotelPromo_shouldReturnNoBanner() {
        //given
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Success(TravelCollectiveBannerModel())

        //when
        hotelHomepageViewModel.getHotelPromo("")

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Success)
    }

    @Test
    fun getHotelPromo_shouldReturn1Data() {
        //given
        val listOfBanner = mutableListOf<TravelCollectiveBannerModel.Banner>()
        listOfBanner.add(TravelCollectiveBannerModel.Banner(id = "1"))
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Success(TravelCollectiveBannerModel(banners = listOfBanner))

        //when
        hotelHomepageViewModel.getHotelPromo("")

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Success)
        assert((actual as Success).data.banners.size == 1)
        assert(actual.data.banners[0].id.equals("1"))
    }

    @Test
    fun getHotelPromo_shouldReturn5Data() {

        //given
        val listOfBanner = mutableListOf<TravelCollectiveBannerModel.Banner>()
        for (i in 1..5) listOfBanner.add(TravelCollectiveBannerModel.Banner(i.toString()))
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Success(TravelCollectiveBannerModel(banners = listOfBanner))

        //when
        hotelHomepageViewModel.getHotelPromo("")

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Success)
        assert((actual as Success).data.banners.size == 5)
        assert(actual.data.banners[2].id.equals("3"))
    }

    @Test
    fun getHotelPromo_shouldReturnFail() {

        //given
        coEvery {
            getTravelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Fail(Throwable("Failing"))

        //when
        hotelHomepageViewModel.getHotelPromo("")

        //then
        val actual = hotelHomepageViewModel.promoData.value
        assert(actual is Fail)
        assert((actual as Fail).throwable.message.equals("Failing"))
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
}