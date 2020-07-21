//package com.tokopedia.tracker
//
//import com.tokopedia.analyticconstant.DataLayer
//import com.tokopedia.areEqualKeyValues
//import com.tokopedia.home.analytics.HomePageTrackingV2
//import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
//import com.tokopedia.home.beranda.domain.model.FreeOngkir
//import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.Assert
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
//class MixLeftTrackerTest : Spek({
//    InstantTaskExecutorRuleSpek(this)
//    val userId = "1234"
//    val testTracker = mockk<TestTracker>(relaxed = true)
//    val positionOnWidgetHome = 5
//    val channel = DynamicHomeChannel.Channels(
//            id = "21370", galaxyAttribution = "PG", persona = "", brandId = "", categoryPersona = "", name = "Testing Left",
//            layout = "left_carousel", type = "inspiration", showPromoBadge = true,
//            header = DynamicHomeChannel.Header(name = "Testing Left"),
//            grids = arrayOf(
//                DynamicHomeChannel.Grid(
//                    id = "580739285",
//                    name = "Realme 5 3/64 Ram 3Gb Rom 64Gb Garansi resmi",
//                    url = "https://www.tokopedia.com/enterphone2/realme-5-3-64-ram-3gb-rom-64gb-garansi-resmi-biru",
//                    price = "Rp 1.799.000",
//                    imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/10/3/1321037/1321037_4d027c9e-dde8-4305-a63f-e66787598c7c_700_700.jpg",
//                    freeOngkir = FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
//                    applink = "tokopedia://product/580739285",
//                    isTopads = true
//                ),
//                DynamicHomeChannel.Grid(
//                    id = "183822484",
//                    name = "Monster Mass Iron Labs 90 Capsules IronLabs MonsterMass 90Caps 90 Caps",
//                    url = "https://www.tokopedia.com/big-man/monster-mass-iron-labs-90-capsules-ironlabs-monstermass-90caps-90-caps?src=topads",
//                    price = "Rp 200.000",
//                    imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/11/20/19270355/19270355_0a7b3fec-2ad0-4c9d-a662-7298e901f375_1280_1280.jpg",
//                    freeOngkir = FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
//                    applink = "tokopedia://product/580739285"
//                ),
//                DynamicHomeChannel.Grid(
//                    id = "558833181",
//                    name = "Samsung Galaxy A30S [4GB/64GB] - Garansi Resmi Indonesia",
//                    url = "https://www.tokopedia.com/supergadgettt/samsung-galaxy-a30s-4gb-64gb-garansi-resmi-indonesia-prismcrushblack",
//                    price = "Rp 2.715.000",
//                    imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/13/3115896/3115896_73bad062-57f3-4255-90d8-a482241bb587_1000_1000",
//                    freeOngkir = FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
//                    applink = "tokopedia://product/580739285"
//                )
//            ),
//            banner = DynamicHomeChannel.Banner(
//                    id = "1801",
//                    title = "",
//                    description = "",
//                    url = "",
//                    backColor = "#ffffff",
//                    applink = "",
//                    textColor = "#ffffff",
//                    imageUrl = "https://ecs7.tokopedia.net/img/",
//                    attribution = "",
//                    cta = DynamicHomeChannel.CtaData(
//                            type = "ghost",
//                            mode = "inverted",
//                            text = "Cek Sekarang",
//                            couponCode = ""
//                    )
//            )
//
//    )
//
//    Feature("Click see more tracker"){
//        Scenario("Click on see more"){
//            Given("the real data"){
//                every { testTracker.getTracker() } returns DataLayer.mapOf(
//                        "event", "clickHomepage",
//                        "eventCategory", "homepage",
//                        "eventAction", "click view all on dynamic channel left carousel",
//                        "eventLabel", channel.id + " - " + channel.header.name
//                )
//            }
//            Then("must true") {
//                val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.MixLeft.getMixLeftClickLoadMore(channel))
//                Assert.assertEquals(result, true)
//            }
//        }
//    }
//
//    Feature("Click see more card tracker"){
//        Scenario("Click on see more"){
//            Given("the real data"){
//                every { testTracker.getTracker() } returns DataLayer.mapOf(
//                        "event", "clickHomepage",
//                        "eventCategory", "homepage",
//                        "eventAction", "click view all card on dynamic channel left carousel",
//                        "eventLabel", channel.id + " - " + channel.header.name,
//                        "currentSite", "tokopediamarketplace",
//                        "screenName", "/",
//                        "userId", userId,
//                        "businessUnit", "home & browse"
//                )
//            }
//            Then("must true") {
//                val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.MixLeft.getMixLeftClickLoadMoreCard(channel, userId))
//                Assert.assertEquals(result, true)
//            }
//        }
//    }
//
//    Feature("Click product tracker"){
//        Scenario("Click on product"){
//            Given("the real tracker data"){
//                every { testTracker.getTracker() } returns DataLayer.mapOf(
//                        "event", "productClick",
//                        "eventCategory", "homepage",
//                        "eventAction", "click on product dynamic channel left carousel",
//                        "eventLabel", "Testing Left",
//                        "channelId", "21370",
//                        "ecommerce", DataLayer.mapOf(
//                            "currencyCode","IDR",
//                            "click", DataLayer.mapOf(
//                                "actionField", DataLayer.mapOf(
//                                "list", "/ - p1 - dynamic channel left carousel - Testing Left - topads"
//                            ),
//                        "products", DataLayer.listOf(
//                            DataLayer.mapOf(
//                                    "name", "Realme 5 3/64 Ram 3Gb Rom 64Gb Garansi resmi",
//                                    "id","580739285",
//                                    "price", "1799000",
//                                    "brand", "none / other",
//                                    "variant","none / other",
//                                    "category", "none / other",
//                                    "position", "1",
//                                    "dimension83", "bebas ongkir",
//                                    "dimension84", "21370",
//                                    "list", "/ - p1 - dynamic channel left carousel - Testing Left - topads",
//                                    "dimension40", "/ - p1 - dynamic channel left carousel - Testing Left - topads"
//                            )
//                        )
//                )
//                )
//                )
//            }
//            Then("must true") {
//                val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.MixLeft.getMixLeftProductClick(channel, channel.grids.first(), 0))
//                Assert.assertEquals(result, true)
//            }
//        }
//    }
//
//    Feature("View product tracker"){
//        val pos = 0
//        Scenario("View on product"){
//            Given("the real tracker data"){
//                every { testTracker.getTracker() } returns DataLayer.mapOf(
//                        "event", "productView",
//                        "eventCategory", "homepage",
//                        "eventAction", "impression on product dynamic channel left carousel",
//                        "eventLabel", channel.header.name,
//                        "channelId", channel.id,
//                        "ecommerce", DataLayer.mapOf(
//                            "currencyCode","IDR",
//                            "impressions", DataLayer.listOf(
//                                DataLayer.mapOf(
//                                        "name", "Realme 5 3/64 Ram 3Gb Rom 64Gb Garansi resmi",
//                                        "id","580739285",
//                                        "price", "1799000",
//                                        "brand", "none / other",
//                                        "variant","none / other",
//                                        "category", "none / other",
//                                        "position", "1",
//                                        "dimension83", "bebas ongkir",
//                                        "dimension84", "21370",
//                                        "list", "/ - p1 - dynamic channel left carousel - Testing Left - topads",
//                                        "dimension40", "/ - p1 - dynamic channel left carousel - Testing Left - topads"
//                                )
//                            )
//                    )
//                )
//            }
//            Then("must true") {
//                val result = areEqualKeyValues(testTracker.getTracker(), HomePageTrackingV2.MixLeft.getMixLeftProductView(channel, channel.grids[pos], pos))
//                Assert.assertEquals(result, true)
//            }
//        }
//    }
//
//})