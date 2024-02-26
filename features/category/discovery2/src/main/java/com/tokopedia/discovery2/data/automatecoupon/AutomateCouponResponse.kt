package com.tokopedia.discovery2.data.automatecoupon

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.data.claim_coupon.ResultStatus

data class AutomateCouponResponse(
    @SerializedName("promoCatalogGetCouponListWidget")
    val promoCatalog: PromoCatalogCoupons? = null
)

data class PromoCatalogCoupons(
    @SerializedName("resultStatus")
    val resultStatus: ResultStatus? = null,
    @SerializedName("couponListWidget")
    val catalogWithCouponList: List<CouponListWidgets>? = null,
)

data class CouponListWidgets(
    @SerializedName("widgetInfo")
    val info: CouponInfo? = null
)

data class CouponInfo(
    @SerializedName("headerList")
    val header: List<Details>? = null,
    @SerializedName("titleList")
    val title: List<Details>? = null,
    @SerializedName("subtitleList")
    val subtitle: List<Details>? = null,
    @SerializedName("footerList")
    val footer: List<Details>? = null,
    @SerializedName("ctaList")
    val ctaList: List<CTA>? = null,
    @SerializedName("badgeList")
    val badges: List<Value>? = null,
    @SerializedName("iconURL")
    val iconURL: String? = null,
    @SerializedName("backgroundInfo")
    val background: BackgroundInfo? = null,
    @SerializedName("actionInfo")
    val action: ActionInfo? = null
)

data class Details(
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("parent")
    val parent: Parent? = null,
    @SerializedName("child")
    val children: List<Child>? = null,
)

data class CTA(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("isDisabled")
    val isDisabled: Boolean? = null,
    @SerializedName("jsonMetadata")
    val metadata: String? = null
)

data class Parent(
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("colorInfo")
    val colorList: ColorList? = null
)

data class Child(
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("valueList")
    val values: List<Value>? = null,
    @SerializedName("colorInfo")
    val colorList: ColorList? = null
)

data class ColorList(
    @SerializedName("colorList")
    val hexColors: List<String>? = null
)

data class Value(
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("value", alternate = ["text"])
    val value: String? = null,
)

data class BackgroundInfo(
    @SerializedName("imageURL")
    val imageURL: String? = null
)

data class ActionInfo(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("isDisabled")
    val isDisabled: Boolean? = null,
    @SerializedName("jsonMetadata")
    val metadata: String? = null,
    @SerializedName("app_link")
    val appLink: String? = null
)
