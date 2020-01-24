package com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Info {

    @SerializedName("__typename")
    @Expose
    var typename: String? = null
    @SerializedName("date_shop_created")
    @Expose
    var dateShopCreated: String? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: String? = null
    @SerializedName("shop_location")
    @Expose
    var shopLocation: String? = null
    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null
    @SerializedName("shop_score")
    @Expose
    var shopScore: Int? = null
    @SerializedName("total_active_product")
    @Expose
    var totalActiveProduct: Int? = null
    @SerializedName("shop_avatar")
    @Expose
    var shopAvatar: String? = null
    @SerializedName("shop_cover")
    @Expose
    var shopCover: String? = null
    @SerializedName("shop_domain")
    @Expose
    var shopDomain: String? = null

}
