package com.tokopedia.tracker

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.areEqualKeyValues
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
import com.tokopedia.home.beranda.domain.gql.feed.FreeOngkirInformation
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomeRecommendationTrackerTest : Spek({
    InstantTaskExecutorRuleSpek(this)
    val tabName = "for you"
    val homeFeedViewModel = HomeRecommendationItemDataModel(
            Product(
                    id = "679625601",
                    name = "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                    categoryBreadcrumbs = "Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                    priceInt = 40000,
                    price = "Rp 40.000",
                    freeOngkirInformation = FreeOngkirInformation(isActive = true),
                    recommendationType = "manual_injection_category_cross_sell",
                    isTopads = false,
                    clusterId = 123
            ),
            position = 10
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
                                "price","40000",
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir",
                                "dimension11", "123",
                                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
                        )
                )

                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewLogin(tabName, homeFeedViewModel))
                assertEquals(result, false)
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
                                "price","40000",
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir",
                                "dimension11", "123",
                                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
                        )
                )

                )
                )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewLoginTopAds(tabName, homeFeedViewModel))
                assertEquals(result, false)
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
                                 "price","40000",
                                 "brand", "none / other",
                                 "variant","none / other",
                                 "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                 "position","10",
                                 "dimension83","bebas ongkir",
                                 "dimension11", "123",
                                 "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
                         )
                 )

                 )
                 )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewNonLogin(tabName, homeFeedViewModel))
                assertEquals(result, false)
            }
        }

        Scenario("Test impression tracker non login user topads"){
            Given("the real tracker data"){
                 every { testTracker.getTracker() } returns DataLayer.mapOf(
                         "event", "productView",
                         "eventCategory", "homepage",
                         "eventAction", "product recommendation impression - non login - topads",
                         "eventLabel", "for you",
                         "ecommerce", DataLayer.mapOf(
                         "currencyCode","IDR",
                         "impressions", DataLayer.listOf(
                         DataLayer.mapOf(
                                 "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                 "id","679625601",
                                 "price","40000",
                                 "brand", "none / other",
                                 "variant","none / other",
                                 "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                 "position","10",
                                 "dimension83","bebas ongkir",
                                 "dimension11", "123",
                                 "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
                         )
                 )

                 )
                 )
            }
            Then("must true"){
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationProductViewNonLoginTopAds(tabName, homeFeedViewModel))
                assertEquals(result, false)
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
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationAddWishlistLogin(homeFeedViewModel.product.id, tabName))
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
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationRemoveWishlistLogin(homeFeedViewModel.product.id, tabName))
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
                val result = areEqualKeyValues(testTracker.getTracker(), HomeRecommendationTracking.getRecommendationAddWishlistNonLogin(homeFeedViewModel.product.id, tabName))
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
                            "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"),
                            "products", DataLayer.listOf(
                                DataLayer.mapOf(
                                        "name", "kabel antena tv kualitas Premium jack model L, Male to Female 3 M",
                                        "id","679625601",
                                        "price","40000",
                                        "brand", "none / other",
                                        "variant","none / other",
                                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                        "position","10",
                                        "dimension83","bebas ongkir",
                                        "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell",
                                        "dimension40", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell",
                                        "dimension11", "123"
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
                                        "price","40000",
                                        "brand", "none / other",
                                        "variant","none / other",
                                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                        "position","10",
                                        "dimension83","bebas ongkir",
                                        "dimension11", "123",
                                        "dimension40", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell",
                                        "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell"
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
                                "price","40000",
                                "brand", "none / other",
                                "variant","none / other",
                                "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                "position","10",
                                "dimension83","bebas ongkir",
                                "dimension11", "123",
                                "list", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads",
                                "dimension40", "/ - p2 - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
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
                                        "price","40000",
                                        "brand", "none / other",
                                        "variant","none / other",
                                        "category","Elektronik/TV \\u0026 Aksesoris/Antena TV \\u0026 Parabola",
                                        "position","10",
                                        "dimension83","bebas ongkir",
                                        "dimension11", "123",
                                        "list", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads",
                                        "dimension40", "/ - p2 - non login - for you - rekomendasi untuk anda - manual_injection_category_cross_sell - product topads"
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