package com.tokopedia.catalog.model.util

import android.content.Context
import android.content.Intent
import com.tokopedia.catalog.analytics.CatalogDetailAnalytics
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

object CatalogUtil {

    fun getSortFilterCount(mapParameter: Map<String, Any>): Int {
        var sortFilterCount = 0

        val mutableMapParameter = mapParameter.toMutableMap()
        val sortFilterParameter = mutableMapParameter.createAndCountSortFilterParameter {
            sortFilterCount += it
        }

        if (sortFilterParameter.hasMinAndMaxPriceFilter()) sortFilterCount -= 1
        if (sortFilterParameter.isSortHasDefaultValue()) sortFilterCount -= 1

        return sortFilterCount
    }

    private fun MutableMap<String, Any>.createAndCountSortFilterParameter(count: (Int) -> Unit): Map<String, Any> {
        val iterator = iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.isNotSortAndFilterEntry()) {
                iterator.remove()
                continue
            }

            count(entry.value.toString().split(OptionHelper.OPTION_SEPARATOR).size)
        }

        return this
    }

    private fun Map.Entry<String, Any>.isNotSortAndFilterEntry(): Boolean {
        return isNotFilterAndSortKey() || isPriceFilterWithZeroValue()
    }

    private  val NON_FILTER_PREFIX = "srp_"

    private fun Map.Entry<String, Any>.isNotFilterAndSortKey(): Boolean {
        return nonFilterParameterKeyList.contains(key) || key.startsWith(NON_FILTER_PREFIX)
    }

    private fun Map.Entry<String, Any>.isPriceFilterWithZeroValue(): Boolean {
        return (key == SearchApiConst.PMIN && value.toString() == "0")
                || (key == SearchApiConst.PMAX && value.toString() == "0")
    }

    internal val nonFilterParameterKeyList = setOf(
            SearchApiConst.Q,
            SearchApiConst.RF,
            SearchApiConst.ACTIVE_TAB,
            SearchApiConst.SOURCE,
            SearchApiConst.LANDING_PAGE,
            SearchApiConst.PREVIOUS_KEYWORD,
            SearchApiConst.ORIGIN_FILTER,
            SearchApiConst.SKIP_REWRITE,
            SearchApiConst.NAVSOURCE,
            SearchApiConst.SKIP_BROADMATCH,
            SearchApiConst.HINT,
            SearchApiConst.FIRST_INSTALL,
            SearchApiConst.SEARCH_REF
    )

    private fun Map<String, Any>.hasMinAndMaxPriceFilter(): Boolean {
        var hasMinPriceFilter = false
        var hasMaxPriceFilter = false

        for(entry in this) {
            if (entry.key == SearchApiConst.PMIN) hasMinPriceFilter = true
            if (entry.key == SearchApiConst.PMAX) hasMaxPriceFilter = true

            // Immediately return so it doesn't continue the loop
            if (hasMinPriceFilter && hasMaxPriceFilter) return true
        }

        return false
    }

    fun Map<String, Any>.isSortHasDefaultValue(): Boolean {
        val sortValue = this[SearchApiConst.OB]

        return sortValue == SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    fun linkerDataMapper(shareData: LinkerData): LinkerShareData {
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = shareData
        return linkerShareData
    }

    fun shareData(context: Context?, shareTxt: String?, productUri: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + productUri)
        context?.startActivity(Intent.createChooser(share, shareTxt))
    }

    fun getSortFilterAnalytics(searchFilterMap : HashMap<String,String>?) : String{
        var label = ""
        searchFilterMap?.forEach { map ->
            label = "$label&${map.key}=${map.value}"
        }
        return label.removePrefix("&")
    }

    /*

    fun trackEventImpressionProductCard(catalogId : String, userId : String ,
                                        item : CatalogProductItem,searchFilterMap : HashMap<String,String>?) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[CatalogDetailAnalytics.KEYS.BRAND] = CatalogDetailAnalytics.KEYS.NONE_OTHER
        productMap[CatalogDetailAnalytics.KEYS.CATEGORY] = item.categoryId
        productMap[CatalogDetailAnalytics.KEYS.ID] = item.id
        productMap[CatalogDetailAnalytics.KEYS.LIST] = if (it.isTopads == false) dimension40 else "$dimension40 - topads"
        productMap[CatalogDetailAnalytics.KEYS.NAME] = item.name
        productMap[CatalogDetailAnalytics.KEYS.DIMENSION61] = getSortFilterAnalytics(searchFilterMap)
        productMap[CatalogDetailAnalytics.KEYS.POSITION] = componentsItems.position + 1
        productMap[CatalogDetailAnalytics.KEYS.PRICE] = item.priceString
        productMap[CatalogDetailAnalytics.KEYS.VARIANT] = CatalogDetailAnalytics.KEYS.NONE_OTHER
        list.add(productMap)

        val eCommerce = mapOf(
                CURRENCY_CODE to IDR,
                CatalogDetailAnalytics.KEYS.IMPRESSION to list)
        val map = HashMap<String,Any>()
        map[CatalogDetailAnalytics.EventKeys.KEY_EVENT] = CatalogDetailAnalytics.EventKeys.EVENT_NAME_PRODUCT_VIEW
        map[CatalogDetailAnalytics.EventKeys.KEY_EVENT_CATEGORY] = CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY
        map[CatalogDetailAnalytics.EventKeys.KEY_EVENT_ACTION] = CatalogDetailAnalytics.ActionKeys.IMPRESSION_PRODUCT
        map[CatalogDetailAnalytics.EventKeys.KEY_EVENT_LABEL] = catalogId
        map[CatalogDetailAnalytics.EventKeys.KEY_ECOMMERCE] = eCommerce
        map[CatalogDetailAnalytics.EventKeys.KEY_PRODUCT_ID] = item.id
        map[CatalogDetailAnalytics.EventKeys.KEY_USER_ID] = userId
        map[CatalogDetailAnalytics.EventKeys.KEY_BUSINESS_UNIT] = CatalogDetailAnalytics.EventKeys.BUSINESS_UNIT_VALUE
        map[CatalogDetailAnalytics.EventKeys.KEY_CURRENT_SITE] = CatalogDetailAnalytics.EventKeys.CURRENT_SITE_VALUE
        trackingQueue.putEETracking(map)
    }

    fun trackProductCardClick(componentsItems: ComponentsItem) {
        if (!componentsItems.data.isNullOrEmpty()) {
            var productCardItemList = ""

            val list = ArrayList<Map<String, Any>>()
            val productMap = HashMap<String, Any>()
            productMap[CatalogDetailAnalytics.KEYS.BRAND] = CatalogDetailAnalytics.KEYS.NONE_OTHER
            productMap[CatalogDetailAnalytics.KEYS.CATEGORY] = item.categoryId
            productMap[CatalogDetailAnalytics.KEYS.ID] = item.id
            productMap[CatalogDetailAnalytics.KEYS.LIST] = if (it.isTopads == false) dimension40 else "$dimension40 - topads"
            productMap[CatalogDetailAnalytics.KEYS.NAME] = item.name
            productMap[CatalogDetailAnalytics.KEYS.DIMENSION61] = getSortFilterAnalytics(searchFilterMap)
            productMap[CatalogDetailAnalytics.KEYS.POSITION] = componentsItems.position + 1
            productMap[CatalogDetailAnalytics.KEYS.PRICE] = item.priceString
            productMap[CatalogDetailAnalytics.KEYS.VARIANT] = CatalogDetailAnalytics.KEYS.NONE_OTHER
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

     */
}