package com.tokopedia.gamification.pdp.data

import com.google.gson.annotations.SerializedName

data class GamingRecommendationParamResponse(
        @SerializedName("PageName") val pageName : String,
        @SerializedName("PageNumber") val pageNumber : Int,
        @SerializedName("CategoryIDs") val categoryIDs : String,
        @SerializedName("XSource") val xSource : String,
        @SerializedName("Mv") val mv : String,
        @SerializedName("Os") val os : Boolean,
        @SerializedName("PowerBadge") val powerBadge : Boolean,
        @SerializedName("PowerMerchant") val powerMerchant : Boolean,
        @SerializedName("HasDiscount") val hasDiscount : Boolean,
        @SerializedName("ShopId") val shopId : Int,
        @SerializedName("PriceMin") val priceMin : Int,
        @SerializedName("PriceMax") val priceMax : Int,
        @SerializedName("Ref") val ref : String,
        @SerializedName("QueryParam") val queryParam : String
)