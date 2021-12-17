package com.tokopedia.localizationchooseaddress.domain.model

import com.google.gson.annotations.SerializedName

data class LocalCacheModel (
        @SerializedName("address_id")
        val address_id: String = "",
        @SerializedName("city_id")
        val city_id: String = "",
        @SerializedName("district_id")
        val district_id: String = "",
        @SerializedName("lat")
        val lat: String = "",
        @SerializedName("long")
        val long: String = "",
        @SerializedName("postal_code")
        val postal_code: String = "",
        @SerializedName("label")
        val label: String = "",
        @SerializedName("shop_id")
        val shop_id: String = "",
        @SerializedName("warehouse_id")
        val warehouse_id: String = ""
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