package com.tokopedia.search.utils

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import timber.log.Timber

fun Map<String, Any>?.convertValuesToString(): Map<String, String> {
    if (this == null) return mapOf()

    val mapValuesInString = mutableMapOf<String, String>()

    this.forEach { originalMap ->
        mapValuesInString[originalMap.key] = originalMap.value.toString()
    }

    return mapValuesInString
}

internal fun RecyclerView.addItemDecorationIfNotExists(itemDecoration: RecyclerView.ItemDecoration) {
    val hasNoItemDecoration = itemDecorationCount == 0
    if (hasNoItemDecoration) addItemDecoration(itemDecoration)
}

internal fun String?.decodeQueryParameter(): String {
    this ?: return ""

    val queryParametersSplitIndex = this.indexOf("?")

    return if (queryParametersSplitIndex < 0) this
    else decodeQueryParameter(queryParametersSplitIndex)
}

private fun String.decodeQueryParameter(queryParamIndex: Int): String {
    val path = this.substring(0, queryParamIndex)
    val queryParameters = this.substring(queryParamIndex + 1, length)
    val queryParameterEncoded =
        UrlParamUtils.generateUrlParamString(UrlParamUtils.getParamMap(queryParameters))

    return "$path?$queryParameterEncoded"
}

internal fun safeCastRupiahToInt(price: String?): Int {
    return try {
        CurrencyFormatHelper.convertRupiahToInt(price ?: "")
    }
    catch(throwable: Throwable) {
        Timber.w(throwable)
        0
    }
}
internal fun Map<String, Any>?.getValueString(key: String): String {
    this ?: return ""

    return get(key)?.toString() ?: ""
}

internal fun LocalCacheModel.toSearchParams(): Map<String, String> {
    return mutableMapOf<String, String>().also { map ->
        if (lat.isNotEmpty()) map[SearchApiConst.USER_LAT] = lat
        if (long.isNotEmpty()) map[SearchApiConst.USER_LONG] = long
        if (address_id.isNotEmpty()) map[SearchApiConst.USER_ADDRESS_ID] = address_id
        if (city_id.isNotEmpty()) map[SearchApiConst.USER_CITY_ID] = city_id
        if (district_id.isNotEmpty()) map[SearchApiConst.USER_DISTRICT_ID] = district_id
        if (postal_code.isNotEmpty()) map[SearchApiConst.USER_POST_CODE] = postal_code
        if (warehouse_id.isNotEmpty()) map[SearchApiConst.USER_WAREHOUSE_ID] = warehouse_id
    }
}

fun List<LabelGroupDataView>?.getFormattedPositionName(): String =
    this?.joinToString(transform = LabelGroupDataView::getPositionTitle) ?: ""

internal fun String.orNone() = this.ifEmpty { SearchTrackingConstant.NONE }