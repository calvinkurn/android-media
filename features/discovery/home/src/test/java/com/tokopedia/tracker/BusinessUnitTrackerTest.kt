package com.tokopedia.tracker

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BusinessUnitTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.areEqualKeyValues
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class BusinessUnitTrackerTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    val positionOnWidgetHome = 5
    val tabIndex = 5
    val tabName = "Keuangan"
    val businessUnitItemDataModel = BusinessUnitItemDataModel(
            content = HomeWidget.ContentItemTab(
                    contentId = 101,
                    contentName = "LISTRIK PLN",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/pln_2.png",
                    url = "https://pulsa.tokopedia.com/?action=init_data\\u0026operator_id=18\\u0026product_id=291\\u0026client_number=884949494946",
                    applink = "tokopedia://digital/form?category_id=3\\u0026client_number=884949494946\\u0026product_id=291\\u0026operator_id=18\\u0026is_from_widget=true",
                    title1st = "Rp 92.500",
                    desc1st = "884949494946",
                    title2nd = "",
                    desc2nd = "",
                    tagName = "",
                    tagType = 0,
                    price = "",
                    originalPrice = "",
                    pricePrefix = "",
                    templateId = 1

            ),
            itemPosition = 3
    )

    val testTracker = mockk<TestTracker>(relaxed = true)

    Feature("Click tab tracker"){
        Scenario("Click on tab bu widget"){
            Given("the real data"){
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                    "event", "clickHomepage",
                        "eventCategory", "homepage",
                        "eventAction", "click on bu widget tab",
                        "eventLabel", tabName
                )
            }
            Then("must true") {
                val result =
                        areEqualKeyValues(testTracker.getTracker(), BusinessUnitTracking.getPageSelected(tabName))
                Assert.assertEquals(result, true)
            }
        }
    }

    Feature("Impression tracker") {
        Scenario("Test impression tracker") {
            Given("the real tracker data") {
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "homepage",
                        "eventAction", "impression on bu widget",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                    DataLayer.mapOf(
                                        "id", "101",
                                        "creative", "LISTRIK PLN",
                                        "name", "/ - p${positionOnWidgetHome} - bu widget - tab ${tabIndex} - $tabName",
                                        "creative_url", "https://ecs7.tokopedia.net/img/recharge/operator/pln_2.png",
                                        "position", "3",
                                        "promo_code", "",
                                        "promo_id", ""
                                )
                                )
                            )

                        )
                )
            }
            Then("must true") {
                val result = areEqualKeyValues(testTracker.getTracker(), BusinessUnitTracking.getBusinessUnitView(BusinessUnitTracking.mapToPromotionTracker(businessUnitItemDataModel, tabName, tabIndex, positionOnWidgetHome)))
                Assert.assertEquals(result, true)
            }
        }
    }

    Feature("Click tracker") {
        Scenario("Test Click tracker") {
            Given("the real tracker data") {
                every { testTracker.getTracker() } returns DataLayer.mapOf(
                        "event", "promoClick",
                        "eventCategory", "homepage",
                        "eventAction", "click on bu widget",
                        "eventLabel", "LISTRIK PLN",
                        "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                    DataLayer.mapOf(
                                        "id", "101",
                                        "creative", "LISTRIK PLN",
                                        "name", "/ - p${positionOnWidgetHome} - bu widget - tab $tabIndex - $tabName",
                                        "creative_url", "https://ecs7.tokopedia.net/img/recharge/operator/pln_2.png",
                                        "position", "3",
                                        "promo_id", "",
                                        "promo_code", ""
                                )
                                )
                            )

                        )
                )
            }
            Then("must true") {
                val result = areEqualKeyValues(testTracker.getTracker(), BusinessUnitTracking.getBusinessUnitClick(BusinessUnitTracking.mapToPromotionTracker(businessUnitItemDataModel, tabName, tabIndex, positionOnWidgetHome)))
                Assert.assertEquals(result, true)
            }
        }
    }
})