package com.tokopedia.discovery2

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapperTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.textcomponent.TextComponentViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner.CircularSliderBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner.SliderBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent.TextComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale.TimerSprintSaleItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YouTubeViewViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
//        MultiBannerViewModelTest::class,
        TimerSprintSaleItemViewModelTest::class,
        UtilsTest::class,
        DiscoveryDataMapperTest::class,
        BannerCarouselItemViewModelTest::class,
        BannerCarouselViewModelTest::class,
        BrandRecommendationViewModelTest::class,
        BrandRecommendationItemViewModelTest::class,
        CarouselBannerViewModelTest::class,
        CarouselBannerItemViewModelTest::class,
        CategoryNavigationItemViewModelTest::class,
        CategoryNavigationViewModelTest::class,
        ChipsFilterItemViewModelTest::class,
        CircularSliderBannerViewModelTest::class,
        SliderBannerViewModelTest::class,
        NavigationChipsViewModelTest::class,
        DynamicCategoryItemViewModelTest::class,
        DynamicCategoryViewModelTest::class,
        LihatSemuaViewModelTest::class,
        YouTubeViewViewModelTest::class,
        ShimmerViewModelTest::class,
        TextComponentViewModelTest::class,
        ComingSoonViewModelTest::class

)



class DiscoveryTestSuite {
}