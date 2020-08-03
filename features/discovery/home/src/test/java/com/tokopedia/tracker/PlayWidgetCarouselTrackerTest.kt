//package com.tokopedia.tracker
//
//import com.tokopedia.analyticconstant.DataLayer
//import com.tokopedia.home.analytics.v2.PlayWidgetCarouselTracking
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.Assert
//import org.junit.Test
//
//class PlayWidgetCarouselTrackerTest {
//
//    private val testTracker = mockk<TestTracker>(relaxed = true)
//    private val userId = "0"
//    private val creativeName = "creative_name"
//
//    @Test
//    fun test_click_channel(){
//        val bannerId = "1"
//        val widgetPosition = "1"
//        val position = "1"
//        val creativeName = "asd"
//        val channelId = "1"
//        val promoCode = "promoCode"
//        val shopId =  "1"
//        val autoPlay = "success"
//        val channelName = "channelName"
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//             "event" , "promoClick",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "click",
//            "eventLabel" , "click channel - $shopId - $channelId - $position - $widgetPosition - $autoPlay",
//            "userId", userId,
//            "channelId", channelId,
//            "ecommerce", DataLayer.mapOf(
//                "promoClick", DataLayer.mapOf(
//                    "promotions", DataLayer.listOf(
//                        DataLayer.mapOf(
//                                "id", bannerId,
//                                "name", "/ - p$widgetPosition - play sgc channel - $channelName",
//                                "creative", creativeName,
//                                "position", position
//                        )
//                    )
//                )
//            )
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getClickBanner(
//                channelId = channelId,
//                position = position,
//                userId = userId,
//                bannerId = bannerId,
//                creativeName = creativeName,
//                shopId = shopId,
//                widgetPosition = widgetPosition,
//                autoPlay = autoPlay,
//                channelName = channelName
//        )))
//    }
//
//    @Test
//    fun test_view_channel(){
//        val bannerId = "1"
//        val widgetPosition = "1"
//        val position = "1"
//        val creativeName = "asd"
//        val channelId = "1"
//        val promoCode = "promoCode"
//        val shopId =  "1"
//        val autoPlay = "success"
//        val channelName = "channelName"
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//             "event" , "promoView",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "impression on play sgc channel",
//            "eventLabel" , "$shopId - $channelId - $position - $widgetPosition - $autoPlay",
//            "userId", userId,
//            "channelId", channelId,
//            "ecommerce", DataLayer.mapOf(
//                "promoView", DataLayer.mapOf(
//                    "promotions", DataLayer.listOf(
//                        DataLayer.mapOf(
//                                "id", bannerId,
//                                "name", "/ - p$widgetPosition - play sgc channel - $channelName",
//                                "creative", creativeName,
//                                "position", position
//                        )
//                    )
//                )
//            )
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getImpressionBanner(
//                channelId = channelId,
//                position = position,
//                userId = userId,
//                bannerId = bannerId,
//                creativeName = creativeName,
//                shopId = shopId,
//                widgetPosition = widgetPosition,
//                autoPlay = autoPlay,
//                channelName = channelName
//        )))
//    }
//
//    @Test
//    fun test_click_banner_left(){
//        val bannerId = "1"
//        val widgetPosition = "1"
//        val position = "1"
//        val shopName = "shopName"
//        val creativeName = "asd"
//        val channelId = "1"
//        val promoCode = "promoCode"
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//             "event" , "promoClick",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "click",
//            "eventLabel" , "click on banner play - $creativeName - $position",
//            "channelId" , channelId,
//            "userId", userId,
//            "ecommerce", DataLayer.mapOf(
//                "promoClick", DataLayer.mapOf(
//                    "promotions", DataLayer.listOf(
//                        DataLayer.mapOf(
//                                "id", bannerId,
//                                "name", "/ - p$widgetPosition - play sgc banner - $shopName",
//                                "creative", creativeName,
//                                "position", position,
//                                "promo_code", promoCode
//                        )
//                    )
//                )
//            )
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getClickLeftBanner(
//                channelId = channelId,
//                position = position,
//                userId = userId,
//                bannerId = bannerId,
//                creativeName = creativeName,
//                promoCode = promoCode,
//                shopName = shopName,
//                widgetPosition = widgetPosition
//        )))
//    }
//
//    @Test
//    fun test_view_banner_left(){
//        val bannerId = "1"
//        val widgetPosition = "1"
//        val position = "1"
//        val shopName = "shopName"
//        val creativeName = "asd"
//        val channelId = "1"
//        val promoCode = "promoCode"
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//             "event" , "promoView",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "impression on play sgc banner",
//            "eventLabel" , "$creativeName - $position",
//            "channelId" , channelId,
//            "userId", userId,
//            "ecommerce", DataLayer.mapOf(
//                "promoView", DataLayer.mapOf(
//                    "promotions", DataLayer.listOf(
//                        DataLayer.mapOf(
//                                "id", bannerId,
//                                "name", "/ - p$widgetPosition - play sgc banner - $shopName",
//                                "creative", creativeName,
//                                "position", position,
//                                "promo_code", promoCode
//                        )
//                    )
//                )
//            )
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getImpressionLeftBanner(
//                channelId = channelId,
//                position = position,
//                userId = userId,
//                bannerId = bannerId,
//                creativeName = creativeName,
//                promoCode = promoCode,
//                shopName = shopName,
//                widgetPosition = widgetPosition
//        )))
//    }
//
//    @Test
//    fun test_click_other_content(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//            "event" , "clickHomepage",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "click other content",
//            "eventLabel" , creativeName,
//            "userId", userId
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getClickSeeAll(
//                creativeName, userId
//        )))
//    }
//
//    @Test
//    fun test_click_remind_me(){
//        val channelId = "channelID"
//        val notifierId = "notifierId"
//        val userId = "123"
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//            "event" , "clickHomepage",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "click remind me",
//            "eventLabel" , "$channelId - $notifierId",
//            "userId", userId
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getClickAddRemind(
//                channelId, notifierId, userId
//        )))
//    }
//
//    @Test
//    fun test_click_remove_remind_me(){
//        val channelId = "channelID"
//        val notifierId = "notifierId"
//        val userId = "123"
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//            "event" , "clickHomepage",
//            "eventCategory" , "homepage-cmp",
//            "eventAction" , "click on remove remind me",
//            "eventLabel" , "$channelId - $notifierId",
//            "userId", userId
//        )
//
//        Assert.assertTrue(areEqualKeyValues(testTracker.getTracker(), PlayWidgetCarouselTracking.getClickAddRemind(
//                channelId, notifierId, userId
//        )))
//    }
//
//    private fun areEqualKeyValues(first: Map<String, Any>, second: Map<String,Any>): Boolean{
//        first.forEach{
//            if(it.value != second[it.key]) return false
//        }
//        return true
//    }
//}