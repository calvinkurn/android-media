package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer

object BusinessUnitTracking : BaseTracking(){
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    private object CustomAction{
        const val BU_VIEW = "impression on bu widget"
        const val BU_CLICK = "CLICK on bu widget"
    }

    fun getPageSelected(header: String) = DataLayer.mapOf(
            Event.KEY, CustomEvent.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, "click on bu widget tab",
            Label.KEY, header
    )

    fun getBusinessUnitView(promotion: Promotion, tabIndex: Int, tabName: String, positionOnWidgetHome: Int) = getBasicPromotionView(
            Event.PROMO_VIEW,
            Category.HOMEPAGE,
            CustomAction.BU_VIEW,
            Label.NONE,
            listOf(promotion),
            tabIndex,
            tabName,
            positionOnWidgetHome
    )

    fun getBusinessUnitClick(promotion: Promotion, tabIndex: Int, tabName: String, positionOnWidgetHome: Int) = getBasicPromotionClick(
            Event.PROMO_CLICK,
            Category.HOMEPAGE,
            CustomAction.BU_CLICK,
            promotion.name.toLowerCase(),
            "",
            "",
            "",
            "",
            "",
            listOf(promotion),
            tabIndex,
            tabName,
            positionOnWidgetHome
    )

    private fun getBasicPromotionView(event: String, eventCategory: String, eventAction: String, eventLabel: String, promotions: List<Promotion>, tabIndex: Int, tabName: String, positionOnWidgetHome: Int): Map<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, getEcommercePromoView(promotions, tabIndex, tabName, positionOnWidgetHome)
        )
    }

    private fun getBasicPromotionClick(event: String, eventCategory: String, eventAction: String, eventLabel: String, channelId: String, affinity: String, attribution: String, categoryId: String, shopId: String, promotions: List<Promotion>, tabIndex: Int, tabName: String, positionOnWidgetHome: Int): Map<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Label.CHANNEL_LABEL, channelId,
                Label.AFFINITY_LABEL, affinity,
                Label.ATTRIBUTION_LABEL, attribution,
                Label.CATEGORY_LABEL, categoryId,
                Label.SHOP_LABEL, shopId,
                Ecommerce.KEY, getEcommercePromoClick(promotions, tabIndex, tabName, positionOnWidgetHome)
        )
    }

    private fun getEcommercePromoView(promotions: List<Promotion>, tabIndex: Int, tabName: String, positionOnWidgetHome: Int): Map<String, Any> {
        return DataLayer.mapOf(
                Ecommerce.PROMO_VIEW, DataLayer.listOf(
                Ecommerce.PROMOTIONS, getPromotions(promotions, tabIndex, tabName, positionOnWidgetHome)
        )
        )
    }

    private fun getEcommercePromoClick(promotions: List<Promotion>, tabIndex: Int, tabName: String, positionOnWidgetHome: Int): Map<String, Any> {
        return DataLayer.mapOf(
                Ecommerce.CLICK, DataLayer.listOf(
                Ecommerce.PROMOTIONS, getPromotions(promotions, tabIndex, tabName, positionOnWidgetHome)
        )
        )
    }

    private fun getPromotions(promotions: List<Promotion>, tabIndex: Int, tabName: String, positionOnWidgetHome: Int): List<Any>{
        val list = ArrayList<Map<String,Any>>()
        promotions.forEach { list.add(createPromotionMap(it, tabIndex, tabName, positionOnWidgetHome)) }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    private fun createPromotionMap(promotion: Promotion, tabIndex: Int, tabName: String, positionOnWidgetHome: Int) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map[Ecommerce.KEY_ID] = promotion.id
        map[Ecommerce.KEY_CREATIVE] = promotion.name
        map[Ecommerce.KEY_NAME] = Ecommerce.PROMOTION_NAME.format(positionOnWidgetHome, "bu widget - tab $tabIndex", tabName)
        map[Ecommerce.KEY_CREATIVE_URL] = promotion.creativeUrl
        map[Ecommerce.KEY_POSITION] = promotion.position
        return map
    }
}