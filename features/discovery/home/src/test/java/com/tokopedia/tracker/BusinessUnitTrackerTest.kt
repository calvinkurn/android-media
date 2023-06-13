//package com.tokopedia.tracker
//
//import com.tokopedia.analyticconstant.DataLayer
//import com.tokopedia.home.analytics.v2.BusinessUnitTracking
//import com.tokopedia.home.beranda.data.model.HomeWidget
//import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
//import com.tokopedia.home.viewModel.homepage.areEqualKeyValues
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.Assert
//import org.junit.Test
//
//class BusinessUnitTrackerTest{
//
//    private val positionOnWidgetHome = 5
//    private val tabIndex = 5
//    private val tabName = "Keuangan"
//    private val businessUnitItemDataModel = BusinessUnitItemDataModel(
//            content = HomeWidget.ContentItemTab(
//                    contentId = 101,
//                    contentName = "LISTRIK PLN",
//                    imageUrl = "https://images.tokopedia.net/img/recharge/operator/pln_2.png",
//                    url = "https://pulsa.tokopedia.com/?action=init_data\\u0026operator_id=18\\u0026product_id=291\\u0026client_number=884949494946",
//                    applink = "tokopedia://digital/form?category_id=3\\u0026client_number=884949494946\\u0026product_id=291\\u0026operator_id=18\\u0026is_from_widget=true",
//                    title1st = "Rp 92.500",
//                    desc1st = "884949494946",
//                    title2nd = "",
//                    desc2nd = "",
//                    tagName = "",
//                    tagType = 0,
//                    price = "",
//                    originalPrice = "",
//                    pricePrefix = "",
//                    templateId = 1
//
//            ),
//            itemPosition = 3
//    )
//
//    private val testTracker = mockk<TestTracker>(relaxed = true)
//
//    @Test
//    fun `Click tab tracker`(){
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "clickHomepage",
//                "eventCategory", "homepage",
//                "eventAction", "click on bu widget tab",
//                "eventLabel", tabName
//        )
//        val result =
//                areEqualKeyValues(testTracker.getTracker(), BusinessUnitTracking.getPageSelected(tabName))
//        Assert.assertEquals(result, true)
//    }
//
//    @Test
//    fun `Impression tracker`() {
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "promoView",
//                "eventCategory", "homepage",
//                "eventAction", "impression on bu widget",
//                "eventLabel", "",
//                "ecommerce", DataLayer.mapOf(
//                "promoView", DataLayer.mapOf(
//                "promotions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "id", "101",
//                        "creative", "LISTRIK PLN",
//                        "name", "/ - p${positionOnWidgetHome} - bu widget - tab ${tabIndex} - $tabName",
//                        "creative_url", "",
//                        "position", "4",
//                        "promo_code", "",
//                        "promo_id", ""
//                )
//        )
//        )
//
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), BusinessUnitTracking.getBusinessUnitView(BusinessUnitTracking.mapToPromotionTracker(businessUnitItemDataModel, tabName, tabIndex, positionOnWidgetHome)))
//        Assert.assertEquals(result, true)
//    }
//
//    @Test
//    fun `Click tracker`() {
//        every { testTracker.getTracker() } returns DataLayer.mapOf(
//                "event", "promoClick",
//                "eventCategory", "homepage",
//                "eventAction", "click on bu widget",
//                "eventLabel", "LISTRIK PLN",
//                "ecommerce", DataLayer.mapOf(
//                "promoClick", DataLayer.mapOf(
//                "promotions", DataLayer.listOf(
//                DataLayer.mapOf(
//                        "id", "101",
//                        "creative", "LISTRIK PLN",
//                        "name", "/ - p${positionOnWidgetHome} - bu widget - tab $tabIndex - $tabName",
//                        "creative_url", "",
//                        "position", "4",
//                        "promo_id", "",
//                        "promo_code", ""
//                )
//        )
//        )
//        )
//        )
//        val result = areEqualKeyValues(testTracker.getTracker(), BusinessUnitTracking.getBusinessUnitClick(BusinessUnitTracking.mapToPromotionTracker(businessUnitItemDataModel, tabName, tabIndex, positionOnWidgetHome)))
//        Assert.assertEquals(result, true)
//    }
//}
