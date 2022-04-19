package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem
import java.io.Serializable

data class CatalogsValueEntity(
        @Expose
        @SerializedName("baseCode")
        var baseCode: String? = null,

        @Expose
        @SerializedName("id")
        var id: Int = 0,

        @Expose
        @SerializedName(value = "imageUrlMobile", alternate = ["image_url_mobile"])
        var imageUrlMobile: String? = null,

        @Expose
        @SerializedName(value = "isGift", alternate = ["is_gift"])
        var isGift: Int = 0,

        @Expose
        @SerializedName("points")
        var points: String? = null,

        @Expose
        @SerializedName(value = "pointsStr", alternate = ["points_str"])
        var pointsStr: String? = null,

        @Expose
        @SerializedName("quota")
        var quota: Int = 0,

        @Expose
        @SerializedName("slug")
        var slug: String? = null,

        @Expose
        @SerializedName(value = "thumbnailURLMobile", alternate = ["thumbnail_url_mobile"])
        var thumbnailUrlMobile: String? = null,

        @Expose
        @SerializedName("title")
        var title: String? = null,

        @Expose
        @SerializedName(value = "isDisabled", alternate = ["is_disabled"])
        var isDisabled: Boolean = false,

        @Expose
        @SerializedName(value = "isDisabledButton", alternate = ["is_disabled_button"])
        var isDisabledButton: Boolean = false,

        @Expose
        @SerializedName(value = "upperTextDesc", alternate = ["upper_text_desc"])
        var upperTextDesc: List<String>? = null,

        @Expose
        @SerializedName(value = "expiredLabel", alternate = ["expired_label"])
        var expiredLabel: String? = null,

        @Expose
        @SerializedName(value = "expired")
        var expired: String? = null,

        @Expose
        @SerializedName(value = "disableErrorMessage", alternate = ["disable_error_message"])
        var disableErrorMessage: String? = null,

        @Expose
        @SerializedName(value = "expiredStr", alternate = ["expired_str"])
        var expiredStr: String? = null,

        @Expose
        @SerializedName(value = "catalogType", alternate = ["catalog_type"])
        var catalogType: Int = 0,

        @Expose
        @SerializedName(value = "pointsSlash", alternate = ["points_slash"])
        var pointsSlash: Int = 0,

        @Expose
        @SerializedName(value = "pointsSlashStr", alternate = ["points_slash_str"])
        var pointsSlashStr: String? = null,

        @Expose
        @SerializedName(value = "discountPercentage", alternate = ["discount_percentage"])
        var discountPercentage: Int = 0,

        @Expose
        @SerializedName(value = "discountPercentageStr", alternate = ["discount_percentage_str"])
        var discountPercentageStr: String? = null,

        @SerializedName(value = "buttonStr", alternate = ["button_str"])
        var buttonStr: String? = null,

        @Expose
        @SerializedName(value = "howToUse", alternate = ["how_to_use"])
        var howToUse: String? = null,

        @Expose
        @SerializedName("tnc")
        var tnc: String? = null,

        @Expose
        @SerializedName("cta")
        var cta: String? = null,

        @Expose
        @SerializedName("isShowTukarButton")
        var isShowTukarButton: Boolean = false,

        @SerializedName("quotaPercentage")
        val quotaPercentage: Int? = null,

        @SerializedName("activePeriod")
        var activePeriod: String? = null,

        @SerializedName("minimumUsageLabel")
        var minimumUsageLabel: String? = null,

        @SerializedName("minimumUsage")
        var minimumUsage: String? = null,

        @SerializedName("activePeriodDate")
        var activePeriodDate: String? = null,

)