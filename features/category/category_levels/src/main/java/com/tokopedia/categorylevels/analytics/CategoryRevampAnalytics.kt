package com.tokopedia.categorylevels.analytics

import com.tokopedia.discovery2.analytics.*
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class CategoryRevampAnalytics(pageType: String = EMPTY_STRING,
                              pagePath: String = EMPTY_STRING,
                              pageIdentifier: String = EMPTY_STRING,
                              campaignCode: String = EMPTY_STRING,
                              sourceIdentifier: String = EMPTY_STRING,
                              trackingQueue: TrackingQueue,
                              private val userSession: UserSessionInterface)
    : BaseDiscoveryAnalytics(pageType, pagePath, pageIdentifier, campaignCode, sourceIdentifier, trackingQueue) {

    private fun createGeneralEvent(eventName: String = EVENT_CLICK_CATEGORY,
                                   eventCategory: String = "$VALUE_CATEGORY_PAGE - $pageIdentifier",
                                   eventAction: String,
                                   eventLabel: String = EMPTY_STRING): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT_VALUE,
                KEY_CURRENT_SITE to CURRENT_SITE_VALUE,
                KEY_USER_ID to userSession.userId
        )
    }

    override fun trackBackClick() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = EVENT_CLICK_BACK))
    }

    override fun trackSearchClick() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventCategory = "$VALUE_TOP_NAV - $VALUE_CATEGORY_PAGE - $pageIdentifier", eventAction = EVENT_CLICK_SEARCH))
    }

    override fun trackShareClick() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = EVENT_CLICK_SHARE))
    }

    override fun trackImpressionNavigationChips(componentsItems: ArrayList<ComponentsItem>?) {
        if (!componentsItems.isNullOrEmpty()) {
            val list = ArrayList<Map<String, Any>>()
            componentsItems.forEachIndexed { index, navigationChip ->
                val data: ArrayList<DataItem> = ArrayList()
                navigationChip.data?.let {
                    data.addAll(it)
                }
                val map = HashMap<String, Any>()
                data.firstOrNull()?.let {
                    map[KEY_ID] = it.id.toString()
                    map[KEY_CREATIVE] = "${it.id} - ${it.title}"
                    map[KEY_NAME] = it.title ?: EMPTY_STRING
                    map[KEY_POSITION] = index + 1
                }
                list.add(map)
            }
            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_NAVIGATION_CHIPS)
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    override fun trackClickNavigationChips(categoryItem: DataItem?, position: Int) {
        val list = ArrayList<Map<String, Any>>()
        categoryItem?.let {
            val map = HashMap<String, Any>()
            map[KEY_ID] = it.id.toString()
            map[KEY_CREATIVE] = "${it.id} - ${it.title}"
            map[KEY_NAME] = it.title ?: EMPTY_STRING
            map[KEY_POSITION] = position + 1
            list.add(map)
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_NAVIGATION_CHIPS)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackClickNavigationDropDown() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_NAVIGATION_DROPDOWN))
    }

    override fun trackClickQuickFilter(filterName: String, componentName: String?, value: String) {
        val map = createGeneralEvent(eventAction = CLICK_FILTER_CHIPS, eventLabel = "$value - $filterName")
        getTracker().sendGeneralEvent(map)
    }
}