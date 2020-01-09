package com.tokopedia.hotel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.hotel.HotelDispatchersProviderTest
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 2020-01-09
 */

@RunWith(JUnit4::class)
class HotelHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getTravelCollectiveBannerUseCase: GetTravelCollectiveBannerUseCase

    private val dispatcher = HotelDispatchersProviderTest()
    private lateinit var hotelHomepageViewModel: HotelHomepageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelHomepageViewModel = HotelHomepageViewModel(getTravelCollectiveBannerUseCase, dispatcher)
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
}