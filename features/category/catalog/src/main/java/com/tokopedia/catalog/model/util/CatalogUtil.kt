package com.tokopedia.catalog.model.util

import android.content.Context
import android.content.Intent
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData

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
        return (key == CatalogSearchApiConst.PMIN && value.toString() == "0")
                || (key == CatalogSearchApiConst.PMAX && value.toString() == "0")
    }

    internal val nonFilterParameterKeyList = setOf(
            CatalogSearchApiConst.Q,
            CatalogSearchApiConst.RF,
            CatalogSearchApiConst.ACTIVE_TAB,
            CatalogSearchApiConst.SOURCE,
            CatalogSearchApiConst.LANDING_PAGE,
            CatalogSearchApiConst.PREVIOUS_KEYWORD,
            CatalogSearchApiConst.ORIGIN_FILTER,
            CatalogSearchApiConst.SKIP_REWRITE,
            CatalogSearchApiConst.NAVSOURCE,
            CatalogSearchApiConst.SKIP_BROADMATCH,
            CatalogSearchApiConst.HINT,
            CatalogSearchApiConst.FIRST_INSTALL,
            CatalogSearchApiConst.SEARCH_REF
    )

    private fun Map<String, Any>.hasMinAndMaxPriceFilter(): Boolean {
        var hasMinPriceFilter = false
        var hasMaxPriceFilter = false

        for(entry in this) {
            if (entry.key == CatalogSearchApiConst.PMIN) hasMinPriceFilter = true
            if (entry.key == CatalogSearchApiConst.PMAX) hasMaxPriceFilter = true

            // Immediately return so it doesn't continue the loop
            if (hasMinPriceFilter && hasMaxPriceFilter) return true
        }

        return false
    }

    fun Map<String, Any>.isSortHasDefaultValue(): Boolean {
        val sortValue = this[CatalogSearchApiConst.OB]

        return sortValue == CatalogSearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
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
}