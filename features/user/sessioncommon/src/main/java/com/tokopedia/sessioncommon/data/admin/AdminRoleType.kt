package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminRoleType(
    @Expose
    @SerializedName("is_shop_admin")
    val isShopAdmin: Boolean = false,
    @Expose
    @SerializedName("is_location_admin")
    val isLocationAdmin: Boolean = false,
    @Expose
    @SerializedName("is_shop_owner")
    val isShopOwner: Boolean = false
)