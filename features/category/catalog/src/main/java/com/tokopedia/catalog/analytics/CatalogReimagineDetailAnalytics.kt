package com.tokopedia.catalog.analytics

import android.os.Bundle
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.BUSINESS_UNITS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.CURRENT_SITE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_BUSINESS_UNIT
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_CREATIVE_NAME
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_CREATIVE_SLOT
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_CURRENT_SITE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_DIMENSION61
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_ECOMMERCE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_EVENT
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_EVENT_ACTION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_EVENT_CATEGORY
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_EVENT_LABEL
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_INDEX
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_IS_LOGIN
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_ITEM_CATEGORY
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_ITEM_ID
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_ITEM_LIST
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_ITEM_NAME
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_PRICE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_PROMOTIONS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_SCREEN_NAME
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_TRACKER_ID
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.KEY_USER_ID
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.catalog.ui.model.CatalogProductListUiModel
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics
import com.tokopedia.oldcatalog.model.util.CatalogUtil
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

object CatalogReimagineDetailAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendEventImpressionList(
        event: String,
        category: String,
        action: String,
        labels: String,
        trackerId: String = "",
        userId: String,
        promotion: List<HashMap<String, String>>
    ) {
        sendEventImpressionListGeneral(
            event,
            category,
            action,
            getCatalogEventLabel(labels),
            trackerId,
            userId,
            promotion
        )
    }

    fun sendEventImpressionListGeneral(
        event: String,
        category: String,
        action: String,
        labels: String,
        trackerId: String = "",
        userId: String,
        promotion: List<HashMap<String, String>>
    ) {
        val bundle = Bundle()
        val promotionBundle = arrayListOf<Bundle>()
        for (item in promotion) {
            val itemBundle = Bundle().apply {
                putString(
                    KEY_ITEM_ID,
                    item[KEY_ITEM_ID]
                )
                putString(
                    KEY_CREATIVE_NAME,
                    item[KEY_CREATIVE_NAME]
                )
                putString(
                    KEY_CREATIVE_SLOT,
                    item[KEY_CREATIVE_SLOT]
                )
                putString(
                    KEY_ITEM_NAME,
                    item[KEY_ITEM_NAME]
                )
            }
            promotionBundle.add(itemBundle)
        }

        bundle.putString(KEY_EVENT, event)
        bundle.putString(KEY_EVENT_CATEGORY, category)
        bundle.putString(KEY_EVENT_ACTION, action)
        bundle.putString(KEY_EVENT_LABEL, labels)
        bundle.putString(KEY_TRACKER_ID, trackerId)
        bundle.putString(
            KEY_BUSINESS_UNIT,
            BUSINESS_UNITS
        )
        bundle.putString(
            KEY_CURRENT_SITE,
            CURRENT_SITE
        )
        bundle.putString(KEY_USER_ID, userId)
        bundle.putParcelableArrayList(
            KEY_PROMOTIONS,
            promotionBundle
        )
        getTracker().sendEnhanceEcommerceEvent(event, bundle)
    }

    fun sendEvent(
        event: String,
        category: String,
        action: String,
        labels: String,
        trackerId: String = ""
    ) {
        HashMap<String, Any>().apply {
            put(KEY_EVENT, event)
            put(KEY_EVENT_CATEGORY, category)
            put(KEY_EVENT_ACTION, action)
            put(KEY_EVENT_LABEL, getCatalogEventLabel(labels))
            put(KEY_TRACKER_ID, trackerId)
            put(KEY_BUSINESS_UNIT, BUSINESS_UNITS)
            put(KEY_CURRENT_SITE, CURRENT_SITE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun sendEventPG(
        event: String = CatalogTrackerConstant.EVENT_VIEW_CLICK_PG,
        category: String,
        action: String,
        labels: String,
        trackerId: String = ""
    ) {
        HashMap<String, Any>().apply {
            put(KEY_EVENT, event)
            put(KEY_EVENT_CATEGORY, category)
            put(KEY_EVENT_ACTION, action)
            put(KEY_EVENT_LABEL, labels)
            put(KEY_TRACKER_ID, trackerId)
            put(KEY_BUSINESS_UNIT, BUSINESS_UNITS)
            put(KEY_CURRENT_SITE, CURRENT_SITE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun sendEventOpenScreen(
        screenName: String,
        trackerId: String = "",
        userId: String = ""
    ) {
        HashMap<String, Any>().apply {
            put(KEY_EVENT, CatalogTrackerConstant.EVENT_OPEN_SCREEN)
            put(KEY_TRACKER_ID, trackerId)
            put(KEY_BUSINESS_UNIT, BUSINESS_UNITS)
            put(KEY_CURRENT_SITE, CURRENT_SITE)
            put(KEY_IS_LOGIN, userId.isNotEmpty().toString())
            put(KEY_SCREEN_NAME, screenName)
            put(KEY_USER_ID, userId)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun sendEventAtc(
        event: String,
        eventAction: String,
        eventCategory: String,
        catalogId: String,
        trackerId: String = "",
        item: CatalogProductItem,
        searchFilterMap: HashMap<String, String>?,
        position: Int,
        userId: String,
        catalogUrl: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEY_ITEM_CATEGORY] = item.categoryId.toString()
        productMap[KEY_ITEM_ID] = item.id
        productMap[CatalogDetailAnalytics.KEYS.LIST] =
            CatalogDetailAnalytics.getCatalogTrackingUrl(catalogUrl)
        productMap[KEY_ITEM_NAME] = item.name
        productMap[KEY_DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEY_INDEX] = position
        productMap[KEY_PRICE] = CurrencyFormatHelper.convertRupiahToInt(
            CurrencyFormatHelper.convertRupiahToInt(item.priceString).toString()
        ).toString()
        list.add(productMap)

        val eCommerce = mapOf(
            CatalogDetailAnalytics.KEYS.CLICK to mapOf(
                CatalogDetailAnalytics.KEYS.ACTION_FIELD to mapOf(
                    CatalogDetailAnalytics.KEYS.LIST to CatalogDetailAnalytics.getCatalogTrackingUrl(
                        getCatalogTrackingUrl(catalogUrl)
                    )
                ),
                CatalogDetailAnalytics.KEYS.PRODUCTS to list
            )
        )

        val map = HashMap<String, Any>()
        map[KEY_EVENT] = event
        map[KEY_EVENT_CATEGORY] = eventCategory
        map[KEY_EVENT_ACTION] = eventAction
        map[KEY_EVENT_LABEL] = getCatalogEventLabel(catalogId)
        map[KEY_TRACKER_ID] = trackerId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNITS
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        map[KEY_ITEM_LIST] = getCatalogTrackingUrl(catalogUrl)
        map[KEY_ECOMMERCE] = eCommerce
        map[KEY_USER_ID] = userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendEventAtc(
        event: String,
        eventAction: String,
        eventCategory: String,
        catalogId: String,
        trackerId: String = "",
        item: CatalogProductListUiModel.CatalogProductUiModel,
        searchFilterMap: HashMap<String, String>?,
        position: Int,
        userId: String,
        catalogUrl: String,
        productName: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
//        productMap[KEY_ITEM_CATEGORY] = item.categoryId.toString()
        productMap[KEY_ITEM_ID] = item.productID
        productMap[CatalogDetailAnalytics.KEYS.LIST] =
            CatalogDetailAnalytics.getCatalogTrackingUrl(catalogUrl)
        productMap[KEY_ITEM_NAME] = productName
        productMap[KEY_DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEY_INDEX] = position
        productMap[KEY_PRICE] = CurrencyFormatHelper.convertRupiahToInt(
            CurrencyFormatHelper.convertRupiahToInt(item.price.original).toString()
        ).toString()
        list.add(productMap)

        val eCommerce = mapOf(
            CatalogDetailAnalytics.KEYS.CLICK to mapOf(
                CatalogDetailAnalytics.KEYS.ACTION_FIELD to mapOf(
                    CatalogDetailAnalytics.KEYS.LIST to CatalogDetailAnalytics.getCatalogTrackingUrl(
                        getCatalogTrackingUrl(catalogUrl)
                    )
                ),
                CatalogDetailAnalytics.KEYS.PRODUCTS to list
            )
        )

        val map = HashMap<String, Any>()
        map[KEY_EVENT] = event
        map[KEY_EVENT_CATEGORY] = eventCategory
        map[KEY_EVENT_ACTION] = eventAction
        map[KEY_EVENT_LABEL] = getCatalogEventLabel(catalogId)
        map[KEY_TRACKER_ID] = trackerId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNITS
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        map[KEY_ITEM_LIST] = getCatalogTrackingUrl(catalogUrl)
        map[KEY_ECOMMERCE] = eCommerce
        map[KEY_USER_ID] = userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendEvent(
        event: String,
        eventAction: String,
        eventCategory: String,
        catalogId: String,
        trackerId: String = "",
        item: CatalogProductItem,
        searchFilterMap: HashMap<String, String>?,
        position: Int,
        userId: String,
        catalogUrl: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEY_ITEM_CATEGORY] = item.categoryId.toString()
        productMap[KEY_ITEM_ID] = item.id
        productMap[CatalogDetailAnalytics.KEYS.LIST] =
            CatalogDetailAnalytics.getCatalogTrackingUrl(catalogUrl)
        productMap[KEY_ITEM_NAME] = item.name
        productMap[KEY_DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEY_INDEX] = position
        productMap[KEY_PRICE] = CurrencyFormatHelper.convertRupiahToInt(
            CurrencyFormatHelper.convertRupiahToInt(item.priceString).toString()
        ).toString()
        list.add(productMap)

        val eCommerce = mapOf(
            CatalogDetailAnalytics.KEYS.CLICK to mapOf(
                CatalogDetailAnalytics.KEYS.ACTION_FIELD to mapOf(
                    CatalogDetailAnalytics.KEYS.LIST to CatalogDetailAnalytics.getCatalogTrackingUrl(
                        getCatalogTrackingUrl(catalogUrl)
                    )
                ),
                CatalogDetailAnalytics.KEYS.PRODUCTS to list
            )
        )

        val map = HashMap<String, Any>()
        map[KEY_EVENT] = event
        map[KEY_EVENT_CATEGORY] = eventCategory
        map[KEY_EVENT_ACTION] = eventAction
        map[KEY_EVENT_LABEL] = getCatalogEventLabel(catalogId)
        map[KEY_TRACKER_ID] = trackerId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNITS
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        map[KEY_ITEM_LIST] = getCatalogTrackingUrl(catalogUrl)
        map[KEY_ECOMMERCE] = eCommerce
        map[KEY_USER_ID] = userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendEvent(
        event: String,
        eventAction: String,
        eventCategory: String,
        catalogId: String,
        trackerId: String = "",
        item: CatalogProductListUiModel.CatalogProductUiModel,
        searchFilterMap: HashMap<String, String>?,
        position: Int,
        userId: String,
        catalogUrl: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
//        productMap[KEY_ITEM_CATEGORY] = item.categoryId.toString()
        productMap[KEY_ITEM_ID] = item.productID
        productMap[CatalogDetailAnalytics.KEYS.LIST] =
            CatalogDetailAnalytics.getCatalogTrackingUrl(catalogUrl)
//        productMap[KEY_ITEM_NAME] = item.p
        productMap[KEY_DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEY_INDEX] = position
        productMap[KEY_PRICE] = CurrencyFormatHelper.convertRupiahToInt(
            CurrencyFormatHelper.convertRupiahToInt(item.price.original).toString()
        ).toString()
        list.add(productMap)

        val eCommerce = mapOf(
            CatalogDetailAnalytics.KEYS.CLICK to mapOf(
                CatalogDetailAnalytics.KEYS.ACTION_FIELD to mapOf(
                    CatalogDetailAnalytics.KEYS.LIST to CatalogDetailAnalytics.getCatalogTrackingUrl(
                        getCatalogTrackingUrl(catalogUrl)
                    )
                ),
                CatalogDetailAnalytics.KEYS.PRODUCTS to list
            )
        )

        val map = HashMap<String, Any>()
        map[KEY_EVENT] = event
        map[KEY_EVENT_CATEGORY] = eventCategory
        map[KEY_EVENT_ACTION] = eventAction
        map[KEY_EVENT_LABEL] = getCatalogEventLabel(catalogId)
        map[KEY_TRACKER_ID] = trackerId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNITS
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        map[KEY_ITEM_LIST] = getCatalogTrackingUrl(catalogUrl)
        map[KEY_ECOMMERCE] = eCommerce
        map[KEY_USER_ID] = userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendEventImpression(
        event: String,
        eventAction: String,
        eventCategory: String,
        catalogId: String,
        trackerId: String = "",
        item: CatalogProductItem,
        searchFilterMap: HashMap<String, String>?,
        position: Int,
        userId: String,
        catalogUrl: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEY_ITEM_CATEGORY] = item.categoryId.toString()
        productMap[KEY_DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEY_ITEM_ID] = item.id
        productMap[CatalogDetailAnalytics.KEYS.LIST] =
            CatalogDetailAnalytics.getCatalogTrackingUrl(catalogUrl)
        productMap[KEY_ITEM_NAME] = item.name
        productMap[KEY_INDEX] = position
        productMap[KEY_PRICE] = CurrencyFormatHelper.convertRupiahToInt(
            CurrencyFormatHelper.convertRupiahToInt(item.priceString).toString()
        ).toString()
        list.add(productMap)

        val eCommerce = mapOf(
            CatalogDetailAnalytics.KEYS.CURRENCY_CODE to CatalogDetailAnalytics.KEYS.IDR,
            CatalogDetailAnalytics.KEYS.IMPRESSION to list
        )

        val map = HashMap<String, Any>()
        map[KEY_EVENT] = event
        map[KEY_EVENT_CATEGORY] = eventCategory
        map[KEY_EVENT_ACTION] = eventAction
        map[KEY_EVENT_LABEL] = getCatalogEventLabel(catalogId)
        map[KEY_TRACKER_ID] = trackerId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNITS
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        map[KEY_ITEM_LIST] = getCatalogTrackingUrl(catalogUrl)
        map[KEY_ECOMMERCE] = eCommerce
        map[KEY_USER_ID] = userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getCatalogEventLabel(label: String): String {
        return "catalog id: $label"
    }

    fun getCatalogTrackingUrl(catalogUrl: String?): String {
        if (!catalogUrl.isNullOrEmpty()) {
            catalogUrl.split(CatalogDetailAnalytics.KEYS.CATALOG_URL_KEY).last().let {
                return "${CatalogDetailAnalytics.KEYS.CATALOG_URL_KEY}$it"
            }
        }
        return ""
    }
}
