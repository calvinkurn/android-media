package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RestrictionEngineRequestParams(
    @Expose
    @SerializedName("source")
    var source: String = "",
    @Expose
    @SerializedName("userID")
    var userId: Long = 0,
    @Expose
    @SerializedName("dataRequest")
    var dataRequest: MutableList<RestrictionEngineDataRequest> = mutableListOf()
)

data class RestrictionEngineDataRequest(
    @Expose
    @SerializedName("product")
    var product: RestrictionEngineDataRequestProduct,
    @Expose
    @SerializedName("shop")
    var shop: RestrictionEngineDataRequestShop,
    @Expose
    @SerializedName("campaign")
    var campaign: RestrictionEngineDataRequestCampaign
)

data class RestrictionEngineDataRequestProduct(
    @Expose
    @SerializedName("productID")
    var productID: String = ""
)

data class RestrictionEngineDataRequestShop(
    @Expose
    @SerializedName("shopID")
    var shopID: Long = 0
)

data class RestrictionEngineDataRequestCampaign(
    @Expose
    @SerializedName("campaignID")
    var campaignID: Long = 0
)
