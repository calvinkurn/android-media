package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import com.tokopedia.flight.dashboard.view.validator.FlightSelectPassengerValidator
import com.tokopedia.flight.search.domain.FlightDeleteAllFlightSearchDataUseCase
import com.tokopedia.flight.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 08/05/2020
 */
class FlightHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = TravelTestDispatcherProvider()

    private val flightAnalytics = mockk<FlightAnalytics>()
    private val travelTickerUseCase = mockk<TravelTickerCoroutineUseCase>()
    private val travelCollectiveBannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
    private val dashboardCache = mockk<FlightDashboardCache>()
    private val deleteAllFlightSearch = mockk<FlightDeleteAllFlightSearchDataUseCase>()
    private val passengerValidator = FlightSelectPassengerValidator()
    private val userSessionInterface = mockk<UserSession>()

    private lateinit var flightHomepageViewModel: FlightHomepageViewModel

    @Before
    fun setup() {
        flightHomepageViewModel = FlightHomepageViewModel(flightAnalytics,
                travelTickerUseCase, travelCollectiveBannerUseCase, dashboardCache,
                deleteAllFlightSearch, passengerValidator, userSessionInterface,
                testDispatcherProvider)
    }

    @Test
    fun fetchBannerData_returnEmptyData_bannerSizeShouldBeEmpty() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Success(TravelCollectiveBannerModel())

        // when
        flightHomepageViewModel.fetchBannerData("", true)

        // then
        assert(flightHomepageViewModel.bannerList.value is Success<TravelCollectiveBannerModel>)
        val bannerData = (flightHomepageViewModel.bannerList.value as Success<TravelCollectiveBannerModel>).data

        bannerData.banners.size shouldBe 0
    }

    @Test
    fun fetchBannerData_returnListBanner_bannerSizeShouldBeSameAsData() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Success(BANNER_DATA)

        // when
        flightHomepageViewModel.fetchBannerData("", true)

        // then
        assert(flightHomepageViewModel.bannerList.value is Success<TravelCollectiveBannerModel>)
        val bannerData = (flightHomepageViewModel.bannerList.value as Success<TravelCollectiveBannerModel>).data

        bannerData.banners.size shouldBe BANNER_DATA.banners.size
        for ((index, banner) in bannerData.banners.withIndex()) {
            banner.id shouldBe BANNER_DATA.banners[index].id
            banner.product shouldBe BANNER_DATA.banners[index].product
            banner.attribute.appUrl shouldBe BANNER_DATA.banners[index].attribute.appUrl
            banner.attribute.imageUrl shouldBe BANNER_DATA.banners[index].attribute.imageUrl
            banner.attribute.description shouldBe BANNER_DATA.banners[index].attribute.description
            banner.attribute.promoCode shouldBe BANNER_DATA.banners[index].attribute.promoCode
            banner.attribute.webUrl shouldBe BANNER_DATA.banners[index].attribute.webUrl
        }
    }

    @Test
    fun fetchBannerData_returnFail_bannerValueShouldBeFailed() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        // when
        flightHomepageViewModel.fetchBannerData("", true)

        // then
        assert(flightHomepageViewModel.bannerList.value is Fail)
    }

}