//package com.tokopedia.tracker
//
//import com.tokopedia.analyticconstant.DataLayer
//import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
//import com.tokopedia.home.beranda.domain.gql.feed.FreeOngkirInformation
//import com.tokopedia.home.beranda.domain.gql.feed.Product
//import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
//import com.tokopedia.home.viewModel.homepage.areEqualKeyValues
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//class HomeRecommendationTrackerTest{
//    private val tabName = "for you"
//    private val homeFeedViewModel = HomeRecommendationItemDataModel(
//            Product(
//                    id = "679625601",
//                    name = "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                    categoryBreadcrumbs = "Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                    priceInt = 40000,
//                    price = "Rp 40.000",
//                    freeOngkirInformation = FreeOngkirInformation(isActive = true),
//                    recommendationType = "manual_injection_category_cross_sell",
//                    isTopads = false,
//                    clusterId = 123
//            ),
//            position = 10
//    )
//
//    private val testTracker = mockk<TestTracker>(relaxed = true)
//
//    @Test
//    fun `Impression tracker`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productView",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation impression",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "impressions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id","679625601",
//                        "price","40000",
//                        "brand", "none / other",
//                        "variant","none / other",
//                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position","10",
//                        "dimension83","bebas ongkir",
//                        "dimension11", "123",
//                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
//                )
//        )
//
//        )
//        )
//
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewLogin(tabName, homeFeedViewModel))
//        assertEquals(result, false)
//    }
//
//    @Test
//    fun `Test impression tracker login user topads`() {
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productView",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation impression - topads",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode", "IDR",
//                "impressions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id", "679625601",
//                        "price", "40000",
//                        "brand", "none / other",
//                        "variant", "none / other",
//                        "category", "Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position", "10",
//                        "dimension83", "bebas ongkir",
//                        "dimension11", "123",
//                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
//                )
//        )
//
//        )
//        )
//
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewLoginTopAds(tabName, homeFeedViewModel))
//        assertEquals(result, false)
//    }
//
//    @Test
//    fun `Test impression tracker non login user`() {
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productView",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation impression - non login",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode", "IDR",
//                "impressions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id", "679625601",
//                        "price", "40000",
//                        "brand", "none / other",
//                        "variant", "none / other",
//                        "category", "Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position", "10",
//                        "dimension83", "bebas ongkir",
//                        "dimension11", "123",
//                        "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
//                )
//        )
//
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewNonLogin(tabName, homeFeedViewModel))
//        assertEquals(result, false)
//    }
//
//    @Test
//    fun `Test impression tracker non login user topads`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productView",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation impression - non login - topads",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "impressions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id","679625601",
//                        "price","40000",
//                        "brand", "none / other",
//                        "variant","none / other",
//                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position","10",
//                        "dimension83","bebas ongkir",
//                        "dimension11", "123",
//                        "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
//                )
//        )
//
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewNonLoginTopAds(tabName, homeFeedViewModel))
//        assertEquals(result, false)
//    }
//
//    @Test
//    fun `Wishlist click`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "clickHomepage",
//                "eventCategory", "homepage",
//                "eventAction", "add wishlist - product recommendation - login",
//                "eventLabel", "679625601 - for you"
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationAddWishlistLogin(homeFeedViewModel.product.id, tabName))
//        assertEquals(result, true)
//    }
//
//    @Test
//    fun `Remove wishlist`() {
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "clickHomepage",
//                "eventCategory", "homepage",
//                "eventAction", "remove wishlist - product recommendation - login",
//                "eventLabel", "679625601 - for you"
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationRemoveWishlistLogin(homeFeedViewModel.product.id, tabName))
//        assertEquals(result, true)
//    }
//
//    @Test
//    fun `Add wishlist non login user`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "clickHomepage",
//                "eventCategory", "homepage",
//                "eventAction", "add wishlist - product recommendation - non login",
//                "eventLabel", "679625601 - for you"
//        )
//
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationAddWishlistNonLogin(homeFeedViewModel.product.id, tabName))
//        assertEquals(result, true)
//    }
//
//    @Test
//    fun `Click tracker`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productClick",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation click",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "click", DataLayer.mapOf(
//                "actionField", DataLayer.mapOf(
//                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"),
//                "products", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id","679625601",
//                        "price","40000",
//                        "brand", "none / other",
//                        "variant","none / other",
//                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position","10",
//                        "dimension83","bebas ongkir",
//                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell",
//                        "dimension40", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell",
//                        "dimension11", "123"
//                )
//        )
//        )
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickLogin(tabName, homeFeedViewModel))
//        assertEquals(result, true)
//
//    }
//
//    @Test
//    fun `Test click tracker non login user non topads`(){
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productClick",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation click - non login",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "click", DataLayer.mapOf(
//                "actionField", DataLayer.mapOf(
//                "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
//        ),
//                "products", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id","679625601",
//                        "price","40000",
//                        "brand", "none / other",
//                        "variant","none / other",
//                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position","10",
//                        "dimension83","bebas ongkir",
//                        "dimension11", "123",
//                        "dimension40", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell",
//                        "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
//                )
//        )
//        )
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickNonLogin(tabName, homeFeedViewModel))
//        assertEquals(result, true)
//
//    }
//
//    @Test
//    fun `Test click tracker login user topads`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productClick",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation click - topads",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "click", DataLayer.mapOf(
//                "actionField", DataLayer.mapOf(
//                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
//        ),
//                "products", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id","679625601",
//                        "price","40000",
//                        "brand", "none / other",
//                        "variant","none / other",
//                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position","10",
//                        "dimension83","bebas ongkir",
//                        "dimension11", "123",
//                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads",
//                        "dimension40", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
//                )
//        )
//        )
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickLoginTopAds(tabName, homeFeedViewModel))
//        assertEquals(result, true)
//    }
//
//    @Test
//    fun `Test click tracker non login user topads`(){
//
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "productClick",
//                "eventCategory", "homepage",
//                "eventAction", "product recommendation click - non login - topads",
//                "eventLabel", "for you",
//                "ecommerce", DataLayer.mapOf(
//                "currencyCode","IDR",
//                "click", DataLayer.mapOf(
//                "actionField", DataLayer.mapOf(
//                "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
//        ),
//                "products", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
//                        "id","679625601",
//                        "price","40000",
//                        "brand", "none / other",
//                        "variant","none / other",
//                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
//                        "position","10",
//                        "dimension83","bebas ongkir",
//                        "dimension11", "123",
//                        "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads",
//                        "dimension40", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
//                )
//        )
//        )
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickNonLoginTopAds(tabName, homeFeedViewModel))
//        assertEquals(result, true)
//    }
//}