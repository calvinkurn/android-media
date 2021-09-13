package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RevieweeData {
    @SerializedName("reviewee_name")
    @Expose
    var revieweeName: String? = null

    @SerializedName("reviewee_uri")
    @Expose
    var revieweeUri: String? = null

    @SerializedName("reviewee_role")
    @Expose
    var revieweeRole: String? = null

    @SerializedName("reviewee_role_id")
    @Expose
    val revieweeRoleId = 0

    @SerializedName("reviewee_picture")
    @Expose
    var revieweePicture: String? = null

    @SerializedName("reviewee_buyer_badge")
    @Expose
    var revieweeBuyerBadge: RevieweeBuyerBadge? = null

    @SerializedName("reviewee_shop_badge")
    @Expose
    var revieweeShopBadge: RevieweeShopBadge? = null
}