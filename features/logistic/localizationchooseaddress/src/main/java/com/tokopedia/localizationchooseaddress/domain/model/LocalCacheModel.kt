package com.tokopedia.localizationchooseaddress.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.DEFAULT_LCA_VERSION
import kotlinx.parcelize.Parcelize

data class LocalCacheModel(
    @Expose
    @SerializedName("address_id")
    val address_id: String = "",

    @Expose
    @SerializedName("city_id")
    val city_id: String = "",

    @Expose
    @SerializedName("district_id")
    val district_id: String = "",

    @Expose
    @SerializedName("lat")
    val lat: String = "",

    @Expose
    @SerializedName("long")
    val long: String = "",

    @Expose
    @SerializedName("postal_code")
    val postal_code: String = "",

    @Expose
    @SerializedName("label")
    val label: String = "",

    @Expose
    @SerializedName("shop_id")
    val shop_id: String = "",

    @Expose
    @SerializedName("warehouse_id")
    val warehouse_id: String = "",

    @Expose
    @SerializedName("warehouses")
    val warehouses: List<LocalWarehouseModel> = listOf(),

    @Expose
    @SerializedName("service_type")
    val service_type: String = "",

    @Expose
    @SerializedName("version")
    val version: String = DEFAULT_LCA_VERSION,

    @Expose
    @SerializedName("tokonow_last_update")
    val tokonow_last_update: String = ""
) {
    companion object {
        const val OOC_WAREHOUSE_ID = "0"
        const val SERVICE_TYPE_15M = "15m"
        const val SERVICE_TYPE_20M = "20m"
    }

    /**
     * @return String of formatted "{latitude},{longitude}" if both value is not empty,
     * return empty String otherwise.
     */
    val latLong: String
        get() {
            return if (lat.isNotBlank() && long.isNotBlank()) {
                "$lat,$long"
            } else {
                ""
            }
        }

    fun isNow15Available(): Boolean {
        val now15Warehouse = warehouses.find { it.service_type == SERVICE_TYPE_15M }
        val now15WarehouseId = now15Warehouse?.warehouse_id.orZero()
        return now15WarehouseId != 0L
    }

    fun isOutOfCoverage(): Boolean {
        return warehouse_id.isBlank() || warehouse_id == OOC_WAREHOUSE_ID
    }

    fun getServiceType(): String {
        return if (service_type == SERVICE_TYPE_15M) SERVICE_TYPE_20M else service_type
    }

    val warehouse_ids: List<String>
        get() = warehouses.filter { warehouse -> warehouse.warehouse_id != 0L }
            .map { it.warehouse_id.toString() }
}

@Parcelize
data class LocalWarehouseModel(
    @Expose
    @SuppressLint("Invalid Data Type")
    @SerializedName("warehouse_id")
    val warehouse_id: Long = 0,

    @Expose
    @SerializedName("service_type")
    val service_type: String = ""
) : Parcelable
