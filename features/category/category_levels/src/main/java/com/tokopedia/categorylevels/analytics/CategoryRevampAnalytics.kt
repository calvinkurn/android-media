package com.tokopedia.categorylevels.analytics

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ORIGIN_FILTER
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.analytics.*
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
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

    private var categoryPageIdentifier : String = pageIdentifier
    private var categoryUrl : String? = null
    private var oldCategoryPageIdentifier : String = pageIdentifier

    private fun changePageIdentifier(pageIdentifier: String){
        categoryPageIdentifier = pageIdentifier
    }

    override fun setOldTabPageIdentifier(pageIdentifier: String) {
        oldCategoryPageIdentifier = categoryPageIdentifier
        changePageIdentifier(pageIdentifier)
    }

    private var viewedProductsSet: ArrayList<String> = arrayListOf()
    private var viewedBestSellerProductsSet: ArrayList<String> = arrayListOf()
    private var dimension40 = ""
    private fun createGeneralEvent(eventName: String = EVENT_CLICK_CATEGORY,
                                   eventCategory: String = VALUE_CATEGORY_PAGE,
                                   eventAction: String,
                                   eventLabel: String = categoryPageIdentifier): MutableMap<String, Any> {
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
        getTracker().sendGeneralEvent(createGeneralEvent(eventCategory = "$VALUE_TOP_NAV - $VALUE_CATEGORY_PAGE", eventAction = EVENT_CLICK_SEARCH))
    }

    override fun trackGlobalNavBarClick(buttonName: String, userID: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventCategory = "$VALUE_TOP_NAV - $VALUE_CATEGORY_PAGE", eventAction = "click $buttonName nav"))
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
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_NAVIGATION_CHIPS,
            eventLabel = oldCategoryPageIdentifier)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackTabsClick(
        id: String,
        parentPosition: Int,
        dataItem: DataItem,
        tabPosition1: Int,
        eventAction: String
    ) {
        dataItem.title = dataItem.name
        trackClickNavigationChips(dataItem, tabPosition1)
    }

    override fun trackClickNavigationDropDown() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_NAVIGATION_DROPDOWN))
    }

    override fun trackCategoryTreeDropDownClick(userLoggedIn: Boolean) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_NAVIGATION_DROPDOWN))
    }

    override fun trackClickCloseNavigation() {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_CLOSE_NAVIGATION))
    }

    override fun trackCategoryTreeCloseClick(userLoggedIn: Boolean) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_CLOSE_NAVIGATION))
    }

    override fun trackClickExpandNavigationAccordion(categoryId: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_EXPAND_NAVIGATION_ACCORDION, eventLabel = "$categoryPageIdentifier - $categoryId"
                ?: ""))
    }

    override fun trackClickCollapseNavigationAccordion(categoryId: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_COLLAPSE_NAVIGATION_ACCORDION, eventLabel = "$categoryPageIdentifier - $categoryId"
                ?: ""))
    }

    override fun trackClickCategoryOption(categoryId: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_CATEGORY_OPTION, eventLabel = "$categoryPageIdentifier - $categoryId"
                ?: ""))
    }

    override fun trackClickQuickFilter(filterName: String, componentName: String?, value: String, isFilterSelected: Boolean) {
        val selected = if (isFilterSelected) "true" else "false"
        val map = createGeneralEvent(eventAction = CLICK_FILTER_CHIPS, eventLabel = "$categoryPageIdentifier - $filterName - $value - $selected")
        getTracker().sendGeneralEvent(map)
    }

    override fun viewProductsList(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            componentsItems.data?.firstOrNull()?.let {
                if(getProductName(it.typeProductCard) ==  PRODUCT_CARD_CAROUSEL) {
                    it.productId?.let { productId ->
                        if (!viewedBestSellerProductsSet.contains(productId)) {
                            viewedBestSellerProductsSet.add(productId)
                            trackEventImpressionProductCard(componentsItems)
                        }
                    }
                } else {
                    it.productId?.let { productId ->
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
            if(getProductName(it.typeProductCard) ==  PRODUCT_CARD_CAROUSEL) {
                productMap[LIST] = if (it.isTopads == false) "$dimension40 - carousel-best-seller" else "$dimension40 - topads - carousel-best-seller"
            } else {
                productMap[LIST] = if (it.isTopads == false) "$dimension40 - product-card-infinite" else "$dimension40 - topads - product-card-infinite"
            }
            productMap[KEY_NAME] = it.name.toString()
            var label = ""
            getComponent(componentsItems.parentComponentId, categoryPageIdentifier)?.selectedFilters?.forEach { map ->
                label = "$label&${map.key}=${map.value}"
            }
            getComponent(componentsItems.parentComponentId, categoryPageIdentifier)?.selectedSort?.forEach { map ->
                label = "$label&${map.key}=${map.value}"
            }
            productMap[FIELD_DIMENSION_61] = label.removePrefix("&")
            productMap[KEY_POSITION] = componentsItems.position + 1
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[DIMENSION83] = getProductDime83(it)
        }
        list.add(productMap)

        val eCommerce = mapOf(
                CURRENCY_CODE to IDR,
                KEY_IMPRESSIONS to list)
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_VIEW,
                eventAction = CATEGORY_PRODUCT_LIST_IMPRESSION)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    private fun getProductName(productType: String?): String {
        return when (productType) {
            PRODUCT_CARD_REVAMP_ITEM, MASTER_PRODUCT_CARD_ITEM_LIST -> PRODUCT_CARD_REVAMP
            PRODUCT_CARD_CAROUSEL_ITEM -> PRODUCT_CARD_CAROUSEL
            PRODUCT_SPRINT_SALE_ITEM -> PRODUCT_SPRINT_SALE
            PRODUCT_SPRINT_SALE_CAROUSEL_ITEM -> PRODUCT_SPRINT_SALE_CAROUSEL
            else -> EMPTY_STRING
        }
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
                productCardItemList = if(getProductName(it.typeProductCard) ==  PRODUCT_CARD_CAROUSEL) {
                    if (it.isTopads == false) "$dimension40 - carousel-best-seller" else "$dimension40 - topads - carousel-best-seller"
                } else {
                    if (it.isTopads == false) "$dimension40 - product-card-infinite" else "$dimension40 - topads - product-card-infinite"
                }
                productMap[KEY_ATTRIBUTION] = NONE_OTHER
                productMap[KEY_BRAND] = NONE_OTHER
                productMap[KEY_CATEGORY] = it.departmentID
                productMap[KEY_ID] = it.productId.toString()
                productMap[LIST] = productCardItemList
                var label = ""
                getComponent(componentsItems.parentComponentId, categoryPageIdentifier)?.selectedFilters?.forEach { map ->
                    label = "$label&${map.key}=${map.value}"
                }
                getComponent(componentsItems.parentComponentId, categoryPageIdentifier)?.selectedSort?.forEach { map ->
                    label = "$label&${map.key}=${map.value}"
                }
                productMap[FIELD_DIMENSION_61] = label.removePrefix("&")
                productMap[KEY_NAME] = it.name.toString()
                productMap[KEY_POSITION] = componentsItems.position + 1
                productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                productMap[KEY_VARIANT] = NONE_OTHER
                productMap[DIMENSION83] = getProductDime83(it)
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
            val map = createGeneralEvent(eventName = EVENT_PRODUCT_CLICK, eventAction = CATEGORY_CLICK_PRODUCT_LIST)
            map[KEY_CAMPAIGN_CODE] = campaignCode
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
        }
    }

    private fun getProductDime83(dataItem: DataItem): String {
        if (dataItem.freeOngkir?.isActive == true){
            for(labelGroup in dataItem.labelsGroupList ?: arrayListOf()){
                if(labelGroup.position == Constant.LABEL_FULFILLMENT){
                    return BEBAS_ONGKIR_EXTRA
                }
            }
            return BEBAS_ONGKIR
        }else {
            return NONE_OTHER
        }
    }

    override fun clearProductViewIds(isRefresh : Boolean) {
        if(isRefresh) {
            viewedProductsSet.clear()
        }
    }

    override fun trackOpenScreen(screenName: String, additionalInfo: AdditionalInfo?, userLoggedIn: Boolean,campaignId: String,variantId: String, shopID: String) {
        additionalInfo?.categoryData?.let {
            categoryUrl = it[KEY_URL] ?: ""
            if(it[KEY_REDIRECTION_URL].isNullOrEmpty())
                TrackApp.getInstance().gtm.sendScreenAuthenticated(SCREEN_NAME, createOpenScreenEventMap(rootId = it[KEY_ROOT_ID], parent = it[KEY_PARENT], id = it[KEY_CATEGORY_ID_MAP], url = it[KEY_URL]))
            if(!it[KEY_CATEGORY_ID_MAP].isNullOrEmpty()){
                changePageIdentifier(it[KEY_CATEGORY_ID_MAP]!!)
            }
        }
    }

    private fun createOpenScreenEventMap(id: String?,
                                 parent: String?,
                                 rootId: String?,
                                 url: String?): Map<String, String> {
        val map = HashMap<String, String>()
        val substring = url?.split("/p/")
        if(substring?.isNullOrEmpty() == false) {
            val levels = substring[1].split("/")
            map[KEY_CATEGORY] = levels[0]
            map[KEY_CATEGORY_ID] = rootId ?: ""
            map[KEY_SUBCATEGORY] = ""
            map[KEY_SUBCATEGORY_ID] = ""
            map[KEY_PRODUCT_GROUP_NAME] = ""
            map[KEY_PRODUCT_GROUP_ID] = ""
            when (levels.size) {
                LVL2 -> {
                    map[KEY_SUBCATEGORY] = levels[1]
                    map[KEY_SUBCATEGORY_ID] = id ?: ""
                }
                LVL3 -> {
                    map[KEY_SUBCATEGORY] = levels[1]
                    map[KEY_SUBCATEGORY_ID] = parent ?: ""
                    map[KEY_PRODUCT_GROUP_NAME] = levels[2]
                    map[KEY_PRODUCT_GROUP_ID] = id ?: ""
                }
            }
        }
        return map
    }

    override fun trackClickDetailedFilter(componentName: String?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_FILTER_MENU))
    }

    override fun trackClickApplyFilter(mapParameters: Map<String, String>) {
        var label = ""
        for (map in mapParameters) {
            if(map.key!= ORIGIN_FILTER)
            label = "$label&${map.key}=${map.value}"
        }
        getTracker().sendGeneralEvent(createGeneralEvent(eventName = EVENT_CLICK_FILTER, eventAction = APPLY_FILTER, eventLabel = "$categoryPageIdentifier - ${label.removePrefix("&")}"))
    }


    override fun getHostSource(): String {
        return CATEGORY_HOST_SOURCE
    }

    override fun getHostTrackingSource(): String {
        return CATEGORY_HOST_TRACKING_SOURCE
    }

    override fun getEventLabel(): String {
        return categoryPageIdentifier
    }

    override fun trackLihatSemuaClick(dataItem: DataItem?) {
        getTracker().sendGeneralEvent(createGeneralEvent(eventAction = CLICK_LIHAT_SEMUA, eventLabel = "$categoryPageIdentifier - $CAROUSEL_BEST_SELLER"))
    }

    override fun onTopadsHeadlineImpression(cpmModel: CpmModel, adapterPosition: Int) {
        val list = ArrayList<Map<String, Any>>()
        cpmModel.data.firstOrNull()?.let {
            val productItem = HashMap<String, Any>()
            productItem[KEY_NAME] = CATEGORY_TOPADS_CARD
            productItem[KEY_ID] = it.id
            productItem[KEY_POSITION] = 1.toString()
            productItem[KEY_CREATIVE] = it.applinks
            list.add(productItem)
        }
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                eventAction = IMPRESSION_TOPADS_HEADLINE)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                com.tokopedia.discovery2.analytics.EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun onTopAdsHeadlineAdsClick(position: Int, applink: String?, cpmData: CpmData, components: ComponentsItem, userLoggedIn: Boolean) {
        if(applink?.contains("shop") == true && position == 0) {
            sendShopHeadlineClickEvent(position, cpmData, true)
        } else if(applink?.contains("shop") == true && position == 1){
            sendShopHeadlineClickEvent(position, cpmData, false)
        } else {
            val productId = applink?.substring(applink.lastIndexOf("/") + 1)
            val clickedProducts : MutableList<Product> = mutableListOf()
            for((index, productItem) in cpmData.cpm.cpmShop.products.withIndex()) {
                if(productId.equals(productItem.id)) {
                    clickedProducts.clear()
                    clickedProducts.add(productItem)
                    sendTopAdsHeadlineProductClick(clickedProducts, index+1)
                }
            }
        }
    }

    private fun sendTopAdsHeadlineProductClick(clickedProducts: MutableList<Product>, position: Int) {
        val list = ArrayList<Map<String, Any>>()
        for (item in clickedProducts) {
            val productItem = HashMap<String, Any>()
            productItem[KEY_NAME] = item.name
            productItem[KEY_ID] = item.id
            productItem[KEY_POSITION] = position.toString()
            productItem[LIST] = "$categoryUrl - topads headline"
            productItem[PRICE] = CurrencyFormatHelper.convertRupiahToInt(item.priceFormat ?: "")
            productItem[KEY_VARIANT] = ""
            productItem[KEY_BRAND] = ""
            productItem[CATEGORY] = ""
            list.add(productItem)
        }
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_CLICK,
                eventAction = CLICK_TOPADS_HEADLINE_PRODUCT, eventLabel = "$categoryPageIdentifier - $CLICK_PRODUCT")
        val eCommerce = mapOf(
                CLICK to mapOf(
                        ACTION_FIELD to mapOf(
                                LIST to "$categoryUrl - topads headline"
                        ),
                        PRODUCTS to list
                )
        )
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    private fun sendShopHeadlineClickEvent(position: Int, it: CpmData, isCekSekarang : Boolean) {
        val list = ArrayList<Map<String, Any>>()
        val productItem = HashMap<String, Any>()
        productItem[KEY_NAME] = CATEGORY_TOPADS_CARD
        productItem[KEY_ID] = it.id
        productItem[KEY_POSITION] = position.toString()
        productItem[KEY_CREATIVE] = it.applinks
        list.add(productItem)
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
                eventAction = CLICK_TOPADS_HEADLINE, eventLabel = if(isCekSekarang) "$categoryPageIdentifier -$CEK_SEKARANG" else "$categoryPageIdentifier - $HEADLINE_SHOP_NAME")
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                com.tokopedia.discovery2.analytics.EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }
}