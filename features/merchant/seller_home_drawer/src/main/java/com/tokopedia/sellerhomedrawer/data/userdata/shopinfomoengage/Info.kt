package com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Info (

    @SerializedName("date_shop_created")
    @Expose
    var dateShopCreated: String? = "",
    @SerializedName("shop_id")
    @Expose
    var shopId: String? = "",
    @SerializedName("shop_location")
    @Expose
    var shopLocation: String? = "",
    @SerializedName("shop_name")
    @Expose
    var shopName: String? = "",
    @SerializedName("shop_score")
    @Expose
    var shopScore: Int? = 0,
    @SerializedName("total_active_product")
    @Expose
    var totalActiveProduct: Int? = 0,
    @SerializedName("shop_avatar")
    @Expose
    var shopAvatar: String? = "",
    @SerializedName("shop_cover")
    @Expose
    var shopCover: String? = "",
    @SerializedName("shop_domain")
    @Expose
    var shopDomain: String? = ""

)
