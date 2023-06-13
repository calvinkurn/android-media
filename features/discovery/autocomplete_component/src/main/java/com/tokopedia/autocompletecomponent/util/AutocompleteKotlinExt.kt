package com.tokopedia.autocompletecomponent.util

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.AUTO_COMPLETE_MANUAL_ENTER
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.INITIAL_STATE_MANUAL_ENTER
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SRP_COMPONENT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ADDRESS_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_CITY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_DISTRICT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LAT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LONG
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_POST_CODE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.WAREHOUSES
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import kotlin.math.cos
import kotlin.math.roundToInt

private const val WAREHOUSES_SEPARATOR = ","
private const val WAREHOUSE_ID_SERVICE_TYPE_SEPARATOR = "#"

@Suppress("MagicNumber")
internal fun CardView.getVerticalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}

@Suppress("MagicNumber")
internal fun CardView.getHorizontalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}

internal fun Map<String, Any>?.getValueString(key: String): String {
    this ?: return ""

    return get(key)?.toString() ?: ""
}

internal fun RecyclerView.addItemDecorationIfNotExists(itemDecoration: RecyclerView.ItemDecoration) {
    val hasNoItemDecoration = itemDecorationCount == 0
    if (hasNoItemDecoration) addItemDecoration(itemDecoration)
}

internal fun Map<String, String>.getWithDefault(key: String, defaultValue: String): String {
    val currentValue = this[key] ?: ""

    return if (currentValue.isEmpty()) defaultValue else currentValue
}

internal fun MutableMap<String, String>.removeKeys(vararg keys: String) {
    keys.forEach { this.remove(it) }
}

internal fun MutableMap<String, String>.addComponentId() {
    val query = get(SearchApiConst.Q) ?: ""

    if (query.isEmpty())
        put(SRP_COMPONENT_ID, INITIAL_STATE_MANUAL_ENTER)
    else
        put(SRP_COMPONENT_ID, AUTO_COMPLETE_MANUAL_ENTER)
}

internal fun MutableMap<String, String>.addQueryIfEmpty() {
    if (get(SearchApiConst.ACTIVE_TAB) == SearchApiConst.ACTIVE_TAB_MPS) return
    val query = get(SearchApiConst.Q) ?: ""

    if (query.isEmpty()) {
        val hint = get(SearchApiConst.HINT) ?: ""
        put(SearchApiConst.Q, hint)
    }
}

internal fun RequestParams.putChooseAddressParams(localCacheModel: LocalCacheModel) {
    if (localCacheModel.lat.isNotEmpty())
        putString(USER_LAT, localCacheModel.lat)

    if (localCacheModel.long.isNotEmpty())
        putString(USER_LONG, localCacheModel.long)

    if (localCacheModel.address_id.isNotEmpty())
        putString(USER_ADDRESS_ID, localCacheModel.address_id)

    if (localCacheModel.city_id.isNotEmpty())
        putString(USER_CITY_ID, localCacheModel.city_id)

    if (localCacheModel.district_id.isNotEmpty())
        putString(USER_DISTRICT_ID, localCacheModel.district_id)

    if (localCacheModel.postal_code.isNotEmpty())
        putString(USER_POST_CODE, localCacheModel.postal_code)

    if (localCacheModel.warehouse_id.isNotEmpty())
        putString(USER_WAREHOUSE_ID, localCacheModel.warehouse_id)

    if (localCacheModel.warehouses.isNotEmpty())
        putString(WAREHOUSES, localCacheModel.warehousesParams())
}

private fun LocalCacheModel.warehousesParams() =
    warehouses.joinToString(separator = WAREHOUSES_SEPARATOR) {
        it.warehouse_id.toString() +
            WAREHOUSE_ID_SERVICE_TYPE_SEPARATOR +
            it.service_type
    }
