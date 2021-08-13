package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopLocationResponse(
    @Expose
    @SerializedName("location_id")
    val locationId: String,
    @Expose
    @SerializedName("location_type")
    val locationType: Int
) {
    companion object {
        private const val MAIN_LOCATION = 1
    }

    fun isMainLocation(): Boolean {
        return locationType == MAIN_LOCATION
    }
}