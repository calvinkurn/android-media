package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopQuestGeneralTrackerResponse(
        @SerializedName("shopQuestGeneralTracker")
        @Expose
        val shopQuestGeneralTracker: ShopQuestGeneralTracker? = ShopQuestGeneralTracker()
)

data class ShopQuestGeneralTracker(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("is_success")
        @Expose
        val success: Boolean = false
)