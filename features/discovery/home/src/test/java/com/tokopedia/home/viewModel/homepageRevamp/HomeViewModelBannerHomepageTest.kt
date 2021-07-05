package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import io.mockk.*
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelBannerHomepageTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `User doesn't have cache, and must get data from network, and should available on view`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        // Banner data
        val bannerDataModel = HomepageBannerDataModel()
        bannerDataModel.slides = listOf(
                BannerSlidesModel()
        )
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bannerDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase
        )
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Get Initial Data and try click topads, user doesn't have cache, and must get data from network, and should available on view`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val bannerDataModel = HomepageBannerDataModel()
        val slidesModel = BannerSlidesModel()
        bannerDataModel.slides = listOf(
                slidesModel
        )

        // Banner data
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bannerDataModel)
                )
        )

        // home view model
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase
        )

        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Get update data and User doesn't have cache, and must get data from network, and should available on view`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        // Banner data
        val bannerDataModel = HomepageBannerDataModel()
        bannerDataModel.slides = listOf(
                BannerSlidesModel()
        )
        val newBannerDataModel = HomepageBannerDataModel()
        newBannerDataModel.slides = listOf(
                BannerSlidesModel(),
                BannerSlidesModel()
        )
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bannerDataModel)
                ),
                HomeDataModel(
                        list = listOf(newBannerDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase
        )
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Impression topads on banner`(){
        var url = ""
        var productId = ""
        var productName = ""
        var imageUrl = ""
        val slotUrl = slot<String>()
        val slotProductId = slot<String>()
        val slotProductName = slot<String>()
        val slotImageUrl = slot<String>()

        val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        // Banner data
        val bannerDataModel = HomepageBannerDataModel()
        bannerDataModel.slides = listOf(
                BannerSlidesModel(
                        topadsViewUrl = "coba topads"
                )
        )
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bannerDataModel)
                )
        )


        // set return impression
        every { topAdsUrlHitter.hitImpressionUrl(any<String>(), capture(slotUrl), capture(slotProductId),
                capture(slotProductName), capture(slotImageUrl)) } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }


        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase
        )
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect channel updated"
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
            })
        }
        confirmVerified(observerHome)

        val testImpressionUrl = "impression url"
        val testBannerId = "banner id"
        val testBannerName = "banner name"
        val testImageUrl = "image url"

        // Impression topads called
        topAdsUrlHitter.hitImpressionUrl(homeViewModel::class.java.simpleName, testImpressionUrl,
                testBannerId, testBannerName, testImageUrl)

        // verify impression
        assert(url == testImpressionUrl)
        assert(productId == testBannerId)
        assert(productName == testBannerName)
        assert(imageUrl == testImageUrl)

    }
}