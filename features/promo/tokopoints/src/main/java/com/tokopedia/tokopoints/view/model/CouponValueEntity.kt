package com.tokopedia.tokopoints.view.model

import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.tokopoints.view.model.section.CouponUpperLeftSection

data class CouponValueEntity(
    @Expose @SerializedName(value = "catalog_id", alternate = ["id"])
    var catalogId: Int = 0,
    @Expose
@SerializedName("code")
var code: String = "",
    @Expose
@SerializedName("cta")
var cta: String = "",
    @Expose
@SerializedName("description")
var description: String = "",
    @Expose
@SerializedName("icon")
var icon: String = "",
    @Expose
@SerializedName(value = "imageUrlMobile", alternate = ["image_url_mobile", "imageURLMobile"])
var imageUrlMobile: String = "",
    @Expose
@SerializedName(value = "thumbnailUrlMobile", alternate = ["thumbnail_url_mobile"])
var thumbnailUrlMobile: String = "",
    @Expose
@SerializedName("title")
var title: String = "",
    @Expose
@SerializedName("usage")
var usage: CouponUsesEntity = CouponUsesEntity(),
    @SerializedName(value = "howToUse", alternate = ["how_to_use"])
var howToUse: String = "",
    @SerializedName(value = "minimum_usage", alternate = ["minimumUsage"])
var minimumUsage: String = "",
    @SerializedName(value = "minimum_usage_label", alternate = ["minimumUsageLabel"])
var minimumUsageLabel: String = "",
    @SerializedName("real_code")
var realCode: String = "",
    @SerializedName("tnc")
var tnc: String = "",
    @SerializedName("is_show_button")
var isIs_show_button:Boolean = false,
    @SerializedName("swipe")
var swipe: CouponSwipeDetail = CouponSwipeDetail(),
    @SerializedName("isStacked")
var isStacked:Boolean = false,
    @SerializedName("isNewCoupon")
var isNewCoupon:Boolean = false,
    @SerializedName("stackID")
var stackId: String = "",
    @SerializedName("redirectAppLink")
    var redirectAppLink: String = "",
    @SerializedName("upperLeftSection")
var upperLeftSection: CouponUpperLeftSection = CouponUpperLeftSection(),
) : BaseItem(){
    val isEmpty:Boolean
    get(){
        return (TextUtils.isEmpty(title)
            || TextUtils.isEmpty(imageUrlMobile))
    }
}
