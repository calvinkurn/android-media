package com.tokopedia.localizationchooseaddress.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.DEFAULT_LCA_VERSION
import kotlinx.parcelize.Parcelize

data class LocalCacheModel (
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
        val version: String = DEFAULT_LCA_VERSION
) {

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
): Parcelable