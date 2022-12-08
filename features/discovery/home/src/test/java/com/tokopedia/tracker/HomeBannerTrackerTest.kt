//package com.tokopedia.tracker
//
//import com.tokopedia.analyticconstant.DataLayer
//import com.tokopedia.home.analytics.HomePageTrackingV2
//import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
//import com.tokopedia.home.viewModel.homepage.areEqualKeyValues
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.Assert
//import org.junit.Test
//
//
//class HomeBannerTrackerTest{
//
//    private val testTracker = mockk<TestTracker>(relaxed = true)
//
//    private val homeBanner = BannerSlidesModel(
//            id = 6794,
//            galaxyAttribution = "OTHERS",
//            persona = "",
//            brandId = "",
//            categoryPersona = "",
//            title = "X_Others_HPB2_Donasi COVID19_New User_22 Mar 20",
//            imageUrl = "https://images.tokopedia.net/img/banner/2020/3/22/85531617/85531617_f8dd3096-8eb6-4f82-b193-d7d0cee392e4.jpg",
//            redirectUrl = "https://www.tokopedia.com/discovery/salam-donasicovid19",
//            applink = "tokopedia://discovery/salam-donasicovid19",
//            topadsViewUrl = "",
//            promoCode = "",
//            creativeName = "X_Others_HPB2_Donasi COVID19_New User_22 Mar 20",
//            type = "new_user_mp",
//            campaignCode = "this_is_campaign_code",
//            position = 1
//    )
//
//    @Test
//    fun `Impression Banner Promotion`(){
//        homeBanner.position = -1
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "promoView",
//                "eventCategory", "homepage",
//                "eventAction", "slider banner impression",
//                "eventLabel", "",
//                "ecommerce", DataLayer.mapOf(
//                "promoView", DataLayer.mapOf(
//                "promotions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "/ - p1 - promo",
//                        "id","6794",
//                        "promo_code","",
//                        "position","-1",
//                        "promo_id", "",
//                        "creative","X_Others_HPB2_Donasi COVID19_New User_22 Mar 20",
//                        "creative_url",""
//                )
//        )
//        )
//        )
//
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.HomeBanner.getBannerImpression(homeBanner))
//        Assert.assertEquals(result, true)
//    }
//
//    @Test
//    fun `Impression Banner Overlay`(){
//        homeBanner.position = -1
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "promoView",
//                "eventCategory", "homepage",
//                "eventAction", "overlay slider banner impression",
//                "eventLabel", "",
//                "ecommerce", DataLayer.mapOf(
//                "promoView", DataLayer.mapOf(
//                "promotions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "/ - p1 - promo overlay",
//                        "id","6794",
//                        "promo_code","",
//                        "position","-1",
//                        "promo_id", "",
//                        "creative","X_Others_HPB2_Donasi COVID19_New User_22 Mar 20",
//                        "creative_url",""
//                )
//        )
//        )
//        )
//        )
//
//
//        val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.HomeBanner.getOverlayBannerImpression(homeBanner))
//        Assert.assertEquals(result, true)
//
//
//    }
//
//    @Test
//    fun `Click Banner Promotion`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                        "event", "promoClick",
//                        "eventCategory", "homepage",
//                        "eventAction", "slider banner click",
//                        "eventLabel", "",
//                        "affinityLabel", "",
//                        "attribution", "OTHERS",
//                        "shopId", "",
//                        "shopId", "",
//                        "channelId", "",
//                        "categoryId", "",
//                        "ecommerce", DataLayer.mapOf(
//                        "currencyCode","IDR",
//                        "ecommerce", DataLayer.listOf(
//                        DataLayer.mapOf(
//                                "promoClick", DataLayer.mapOf(
//                                "promotions", DataLayer.listOf(
//                                DataLayer.mapOf(
//                                        "name", "/ - p1 - promo",
//                                        "id","6794",
//                                        "promo_code","",
//                                        "position","-1",
//                                        "promo_id", "",
//                                        "creative","X_Others_HPB2_Donasi COVID19_New User_22 Mar 20",
//                                        "creative_url","https://images.tokopedia.net/img/banner/2020/3/22/85531617/85531617_f8dd3096-8eb6-4f82-b193-d7d0cee392e4.jpg"
//                                )
//                        )
//                        )
//                        )
//                )
//                )
//                )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.HomeBanner.getBannerClick(homeBanner))
//        Assert.assertEquals(result, false)
//    }
//
//    @Test
//    fun `Click Banner Overlay`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "promoClick",
//                "eventCategory", "homepage",
//                "eventAction", "overlay slider banner click",
//                "eventLabel", "",
//                "affinityLabel", "",
//                "attribution", "OTHERS",
//                "shopId", "",
//                "shopId", "",
//                "channelId", "",
//                "categoryId", "",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "ecommerce", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "promoClick", DataLayer.mapOf(
//                        "promotions", DataLayer.listOf(
//                        DataLayer.mapOf(
//                                "name", "/ - p1 - promo overlay",
//                                "id","6794",
//                                "promo_code","",
//                                "position","-1",
//                                "promo_id", "",
//                                "creative","X_Others_HPB2_Donasi COVID19_New User_22 Mar 20",
//                                "creative_url","https://images.tokopedia.net/img/banner/2020/3/22/85531617/85531617_f8dd3096-8eb6-4f82-b193-d7d0cee392e4.jpg"
//                        )
//                )
//                )
//                )
//        )
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.HomeBanner.getBannerClick(homeBanner))
//        Assert.assertEquals(result, false)
//    }
//}
