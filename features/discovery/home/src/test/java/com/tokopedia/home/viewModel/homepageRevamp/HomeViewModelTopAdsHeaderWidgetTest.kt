package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDisplayHeadlineAds
import com.tokopedia.home.beranda.domain.model.DisplayHeadlineAdsEntity
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 06/11/20.
 */
class HomeViewModelTopAdsHeaderWidgetTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private val getDisplayHeadlineAds = mockk<GetDisplayHeadlineAds> (relaxed = true)


    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `success get featured shop data`() = runBlocking{
        val featuredShopDataModel = FeaturedShopDataModel(ChannelModel(id="1",groupId = "1"))
        val badge = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Headline.Badges(
                imageUrl = "imageUrl"
        )
        val imageProduct = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Product.ImageProduct(
                imageUrl = "imageUrl"
        )
        val product = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Product(
                id = "1",
                review = "100",
                rating = 100,
                imageProduct = imageProduct
        )
        val shop = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Shop(
                products = listOf(
                    product
                )
        )
        val headlineAds = DisplayHeadlineAdsEntity.DisplayHeadlineAds(applink = "headline", headline = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Headline(
                badges = listOf(
                        badge
                ),
                shop = shop
            )
        )

        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns listOf(headlineAds)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(featuredShopDataModel),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getDisplayHeadlineAds = getDisplayHeadlineAds)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } != null )
        val grid = (homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } as? FeaturedShopDataModel)?.channelModel?.channelGrids?.firstOrNull()
        assert( (homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } as? FeaturedShopDataModel)?.channelModel?.channelGrids?.isNotEmpty() == true)
        assert(grid?.applink == headlineAds.applink)
        assert(grid?.shop?.shopBadgeUrl == badge.imageUrl)
        assert(grid?.countReviewFormat == product.review)
        assert(grid?.rating == product.rating)
        assert(grid?.imageUrl == imageProduct.imageUrl)
    }

    @Test
    fun `test mapper should null badge, count review, rating, and image url`() = runBlocking{
        val featuredShopDataModel = FeaturedShopDataModel(ChannelModel(id="1",groupId = "1"))
        val badge = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Headline.Badges(
                imageUrl = "imageUrl"
        )
        val imageProduct = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Product.ImageProduct(
                imageUrl = "imageUrl"
        )
        val shop = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Shop(
                products = listOf()
        )
        val headlineAds = DisplayHeadlineAdsEntity.DisplayHeadlineAds(applink = "headline", headline = DisplayHeadlineAdsEntity.DisplayHeadlineAds.Headline(
                badges = listOf(),
                shop = shop
            )
        )

        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns listOf(headlineAds)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(featuredShopDataModel),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getDisplayHeadlineAds = getDisplayHeadlineAds)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } != null )
        val grid = (homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } as? FeaturedShopDataModel)?.channelModel?.channelGrids?.firstOrNull()
        assert( (homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } as? FeaturedShopDataModel)?.channelModel?.channelGrids?.isNotEmpty() == true)
        assert(grid?.applink == headlineAds.applink)
        assert(grid?.shop?.shopBadgeUrl == "")
        assert(grid?.countReviewFormat == "")
        assert(grid?.rating == 0)
        assert(grid?.imageUrl == "")
    }

    @Test
    fun `success but empty get featured shop data`() = runBlocking{
        val featuredShopDataModel = FeaturedShopDataModel(ChannelModel(id="1",groupId = "1"))

        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns listOf()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(featuredShopDataModel),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getDisplayHeadlineAds = getDisplayHeadlineAds)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } == null )
    }


    @Test
    fun `error get featured shop data`() = runBlocking{
        val featuredShopDataModel = FeaturedShopDataModel(ChannelModel(id="1",groupId = "1"))

        coEvery { getDisplayHeadlineAds.executeOnBackground() } throws TimeoutException()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(featuredShopDataModel),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getDisplayHeadlineAds = getDisplayHeadlineAds)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } == null )
    }

    @Test
    fun `no slot data featured shop`() = runBlocking{
        coEvery { getDisplayHeadlineAds.executeOnBackground() } throws TimeoutException()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getDisplayHeadlineAds = getDisplayHeadlineAds)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is FeaturedShopDataModel } == null )
    }

}