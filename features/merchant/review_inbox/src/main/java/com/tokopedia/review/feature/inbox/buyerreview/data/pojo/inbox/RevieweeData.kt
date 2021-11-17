package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RevieweeData(
    @SerializedName("reviewee_name")
    @Expose
    var revieweeName: String = "",

    @SerializedName("reviewee_uri")
    @Expose
    var revieweeUri: String = "",

    @SerializedName("reviewee_role")
    @Expose
    var revieweeRole: String = "",

    @SerializedName("reviewee_role_id")
    @Expose
    val revieweeRoleId: Int = 0,

    @SerializedName("reviewee_picture")
    @Expose
    var revieweePicture: String = "",

    @SerializedName("reviewee_buyer_badge")
    @Expose
    var revieweeBuyerBadge: RevieweeBuyerBadge = RevieweeBuyerBadge(),

    @SerializedName("reviewee_shop_badge")
    @Expose
    var revieweeShopBadge: RevieweeShopBadge = RevieweeShopBadge()
)