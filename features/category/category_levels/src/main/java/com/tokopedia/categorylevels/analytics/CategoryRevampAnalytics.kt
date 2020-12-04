package com.tokopedia.categorylevels.analytics

import com.tokopedia.discovery2.analytics.*
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

class CategoryRevampAnalytics(pageType: String = EMPTY_STRING,
                              pagePath: String = EMPTY_STRING,
                              pageIdentifier: String = EMPTY_STRING,
                              campaignCode: String = EMPTY_STRING,
                              sourceIdentifier: String = EMPTY_STRING,
                              trackingQueue: TrackingQueue,
                              private val userSession: UserSessionInterface)
    : BaseDiscoveryAnalytics(pageType, pagePath, pageIdentifier, campaignCode, sourceIdentifier, trackingQueue) {

    private var viewedProductsSet: ArrayList<String> = arrayListOf()
    private var viewedTopadsProductsSet: ArrayList<String> = arrayListOf()
    private var dimension40 = ""
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

    override fun trackClickCloseNavigation() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_CLOSE_NAVIGATION))
    }

    override fun trackClickExpandNavigationAccordion(categoryId: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_EXPAND_NAVIGATION_ACCORDION, eventLabel = categoryId
                ?: ""))
    }

    override fun trackClickCollapseNavigationAccordion(categoryId: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_COLLAPSE_NAVIGATION_ACCORDION, eventLabel = categoryId
                ?: ""))
    }

    override fun trackClickCategoryOption(categoryId: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_CATEGORY_OPTION, eventLabel = categoryId
                ?: ""))
    }

    override fun trackClickQuickFilter(filterName: String, componentName: String?, value: String, isFilterSelected: Boolean) {
        val selected = if (isFilterSelected) "true" else "false"
        val map = createGeneralEvent(eventAction = CLICK_FILTER_CHIPS, eventLabel = "$filterName - $value - $selected")
        getTracker().sendGeneralEvent(map)
    }

    override fun viewProductsList(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            componentsItems.data?.firstOrNull()?.let {
                it.productId?.let { productId ->
                    if(it.isTopads == true){
                        if (!viewedTopadsProductsSet.contains(productId)) {
                            viewedTopadsProductsSet.add(productId)
                            trackEventImpressionProductCard(componentsItems)
                        }
                    } else {
                        if (!viewedProductsSet.contains(productId)) {
                            viewedProductsSet.add(productId)
                            trackEventImpressionProductCard(componentsItems)
                        }
                    }
                }
            }
        }
    }

    private fun trackEventImpressionProductCard(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        val pagePath = getComponent(componentsItems.parentComponentId, componentsItems.pageEndPoint)?.pagePath
        if(!pagePath.isNullOrEmpty())
            dimension40 = pagePath
        componentsItems.data?.firstOrNull()?.let {
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_CATEGORY] = it.departmentID
            productMap[KEY_ID] = it.productId.toString()
            productMap[LIST] = if (it.isTopads == false) dimension40 else "$dimension40 - topads"
            productMap[KEY_NAME] = it.name.toString()
            productMap[KEY_POSITION] = if (it.isTopads == false) viewedProductsSet.indexOf(it.productId) + 1 else viewedTopadsProductsSet.indexOf(it.productId) + 1
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
            productMap[KEY_VARIANT] = NONE_OTHER
        }
        list.add(productMap)

        val eCommerce = mapOf(
                CURRENCY_CODE to IDR,
                KEY_IMPRESSIONS to list)
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_VIEW,
                eventAction = if (componentsItems.data?.firstOrNull()?.isTopads == false) CATEGORY_PRODUCT_LIST_IMPRESSION else CATEGORY_PRODUCT_LIST_IMPRESSION_TOPADS)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackProductCardClick(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            var productCardItemList = ""
            val list = ArrayList<Map<String, Any>>()
            val productMap = HashMap<String, Any>()
            val pagePath = getComponent(componentsItems.parentComponentId, componentsItems.pageEndPoint)?.pagePath
            if(!pagePath.isNullOrEmpty())
                dimension40 = pagePath
            componentsItems.data?.firstOrNull()?.let {
                productCardItemList = if (it.isTopads == false) dimension40 else "$dimension40 - topads"
                productMap[KEY_ATTRIBUTION] = NONE_OTHER
                productMap[KEY_BRAND] = NONE_OTHER
                productMap[KEY_CATEGORY] = it.departmentID
                productMap[KEY_ID] = it.productId.toString()
                productMap[LIST] = productCardItemList
                productMap[KEY_NAME] = it.name.toString()
                productMap[KEY_POSITION] = if (it.isTopads == false) viewedProductsSet.indexOf(it.productId) + 1 else viewedTopadsProductsSet.indexOf(it.productId) + 1
                productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                productMap[KEY_VARIANT] = NONE_OTHER
            }
            list.add(productMap)

            val eCommerce = mapOf(
                    CLICK to mapOf(
                            ACTION_FIELD to mapOf(
                                    LIST to productCardItemList
                            ),
                            PRODUCTS to list
                    )
            )
            val map = createGeneralEvent(eventName = EVENT_PRODUCT_CLICK, eventAction = if (componentsItems.data?.firstOrNull()?.isTopads == false) CATEGORY_CLICK_PRODUCT_LIST else CATEGORY_CLICK_PRODUCT_LIST_TOPADS)
            map[KEY_CAMPAIGN_CODE] = campaignCode
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
        }
    }

    override fun clearProductViewIds(isRefresh : Boolean) {
        if(isRefresh) {
            viewedProductsSet.clear()
            viewedTopadsProductsSet.clear()
        }
    }

}