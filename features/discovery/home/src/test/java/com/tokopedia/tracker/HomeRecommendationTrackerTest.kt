package com.tokopedia.tracker

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
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

    val testTracker = mockk<TestTracker>(relaxed = true)

    Feature("Impression tracker"){
        Scenario("Test impression tracker login user"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "productView",
                        "eventCategory", "homepage",
                        "eventAction", "product recommendation impression",
                        "eventLabel", "for you",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode","IDR",
                        "impressions", DataLayer.listOf(
                        DataLayer.mapOf(
                                "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                "id","679625601",
                                "price",40000,
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir",
                                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
                        )
                )

                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewLogin(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

        Scenario("Test impression tracker login user topads"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "productView",
                        "eventCategory", "homepage",
                        "eventAction", "product recommendation impression - topads",
                        "eventLabel", "for you",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode","IDR",
                        "impressions", DataLayer.listOf(
                        DataLayer.mapOf(
                                "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                "id","679625601",
                                "price",40000,
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir",
                                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
                        )
                )

                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewLoginTopAds(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

        Scenario("Test impression tracker non login user"){
            Given("the real tracker data"){
                 every { testTracker.getTracker() } returns DataLayer.mapOf(
                         "event", "productView",
                         "eventCategory", "homepage",
                         "eventAction", "product recommendation impression - non login",
                         "eventLabel", "for you",
                         "ecommerce", DataLayer.mapOf(
                         "currencyCode","IDR",
                         "impressions", DataLayer.listOf(
                         DataLayer.mapOf(
                                 "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
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
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewNonLogin(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

        Scenario("Test impression tracker non login user topads"){
            Given("the real tracker data"){
                 every { testTracker.getTracker() } returns DataLayer.mapOf(
                         "event", "productView",
                         "eventCategory", "homepage",
                         "eventAction", "product recommendation impression - non login - top ads",
                         "eventLabel", "for you",
                         "ecommerce", DataLayer.mapOf(
                         "currencyCode","IDR",
                         "impressions", DataLayer.listOf(
                         DataLayer.mapOf(
                                 "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                 "id","679625601",
                                 "price",40000,
                                 "brand", "none / other",
                                 "variant","none / other",
                                 "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                 "position","10",
                                 "dimension83","bebas ongkir",
                                 "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
                         )
                 )

                 )
                 )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewNonLoginTopAds(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

    }
    Feature("Wishlist click"){
        Scenario("Add wishlist"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                    "event", "clickHomepage",
                    "eventCategory", "homepage",
                    "eventAction", "add wishlist - product recommendation - login",
                    "eventLabel", "679625601 - for you"
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationAddWishlistLogin(homeFeedViewModel.productId, tabName))
                assertEquals(result, true)
            }
        }
        Scenario("Remove wishlist"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                    "event", "clickHomepage",
                    "eventCategory", "homepage",
                    "eventAction", "remove wishlist - product recommendation - login",
                    "eventLabel", "679625601 - for you"
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationRemoveWishlistLogin(homeFeedViewModel.productId, tabName))
                assertEquals(result, true)
            }
        }
        Scenario("Add wishlist non login user"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                    "event", "clickHomepage",
                    "eventCategory", "homepage",
                    "eventAction", "add wishlist - product recommendation - non login",
                    "eventLabel", "679625601 - for you"
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationAddWishlistNonLogin(homeFeedViewModel.productId, tabName))
                assertEquals(result, true)
            }
        }
    }
    Feature("Click tracker"){
        Scenario("Test click tracker login user non topads"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "productClick",
                        "eventCategory", "homepage",
                        "eventAction", "product recommendation click",
                        "eventLabel", "for you",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode","IDR",
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
                ),
                        "products", DataLayer.listOf(
                        DataLayer.mapOf(
                                "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                "id","679625601",
                                "price",40000,
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir"
                        )
                )
                )
                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickLogin(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

        Scenario("Test click tracker non login user non topads"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "productClick",
                        "eventCategory", "homepage",
                        "eventAction", "product recommendation click - non login",
                        "eventLabel", "for you",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode","IDR",
                        "click", DataLayer.mapOf(
                            "actionField", DataLayer.mapOf(
                                "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
                            ),
                            "products", DataLayer.listOf(
                                DataLayer.mapOf(
                                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                        "id","679625601",
                                        "price",40000,
                                        "brand", "none / other",
                                        "variant","none / other",
                                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                        "position","10",
                                        "dimension83","bebas ongkir"
                                )
                            )
                )
                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickNonLogin(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

        Scenario("Test click tracker login user topads"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "productClick",
                        "eventCategory", "homepage",
                        "eventAction", "product recommendation click - topads",
                        "eventLabel", "for you",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode","IDR",
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
                ),
                        "products", DataLayer.listOf(
                        DataLayer.mapOf(
                                "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                "id","679625601",
                                "price",40000,
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir"
                        )
                )
                )
                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickLoginTopAds(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }

        Scenario("Test click tracker non login user topads"){
            Given("the real tracker data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "productClick",
                        "eventCategory", "homepage",
                        "eventAction", "product recommendation click - non login - topads",
                        "eventLabel", "for you",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode","IDR",
                        "click", DataLayer.mapOf(
                            "actionField", DataLayer.mapOf(
                                "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
                            ),
                            "products", DataLayer.listOf(
                                DataLayer.mapOf(
                                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                        "id","679625601",
                                        "price",40000,
                                        "brand", "none / other",
                                        "variant","none / other",
                                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                        "position","10",
                                        "dimension83","bebas ongkir"
                                )
                            )
                )
                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductClickNonLoginTopAds(tabName, homeFeedViewModel))
                assertEquals(result, true)
            }
        }
    }
})

fun areEqualKeyValues(first: Map<String, Any>, second: Map<String,Any>): Boolean{
    first.forEach{
        if(it.value != second[it.key]) return false
    }
    return true
}

interface TestTracker{
    fun getTracker(): Map<String, Any>
}