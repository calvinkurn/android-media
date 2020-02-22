package com.tokopedia.tracker

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomeFeedTrackerTest : Spek({
    InstantTaskExecutorRuleSpek(this)
    val tabName = "for you"
    val homeFeedViewModel = HomeFeedViewModel(
            productId = "679625601",
            productName = "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
            categoryBreadcrumbs = "Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
            priceNumber = 40000,
            price = "Rp 40.000",
            isFreeOngkirActive = true,
            position = 10,
            imageUrl = "", clickUrl = "", freeOngkirImageUrl = "",
            badges = listOf(), countReview = 1, discountPercentage = "", isTopAds = false,
            isWishList = false, labelGroups = listOf(), labels = listOf(), location = "",
            rating = 1, recommendationType = "manual_injection_category_cross_sell",
            slashedPrice = "", trackerImageUrl = "", wishlistUrl = ""
    )

    val nonLoginImpression = DataLayer.mapOf(
            "event", "productView",
            "eventCategory", "homepage",
            "eventAction", "product recommendation impression - non login",
            "eventLabel", "for you",
            "ecommerce", DataLayer.mapOf(
                    "currencyCode","IDR",
                    "impressions", DataLayer.listOf(
                        DataLayer.mapOf(
                                "name","kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                "id","679625601",
                                "price",40000,
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir",
                                "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
                        )
                    )

            )
    )

    Feature("Impression tracker"){
        Scenario("Test impression tracker login user"){

        }
        Scenario("Test impression tracker non login user"){
            val testTracker = HomeRecommendationTracking.getRecommendationProductViewNonLogin(tabName, homeFeedViewModel)
            assert(nonLoginImpression.equals(testTracker))
        }
    }

    Feature("Click tracker"){
        Scenario("Test click tracker login user"){

        }
        Scenario("Test click tracker non login user"){

        }
    }
})