package com.tokopedia.notifications.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@SuppressLint("Invalid Data Type")
data class AnimationCrackCouponResponse(
    @SerializedName("gamiScratchCardCrack")
    @Expose
    val gamiScratchCardCrack: GamiScratchCardCrack = GamiScratchCardCrack()
)

data class GamiScratchCardCrack(
    @SerializedName("resultStatus")
    @Expose
    val resultStatus: ResultStatus? = ResultStatus(),
    @SerializedName("scratchCard")
    @Expose
    val scratchCard: ScratchCard? = ScratchCard(),
    @SerializedName("benefits")
    @Expose
    val benefits: List<Benefits>? = emptyList(),
    @SerializedName("cta")
    @Expose
    val cta: Cta? = Cta(),
    @SerializedName("assets")
    @Expose
    val assets: Assets? = Assets()
)

data class Cta(
    @SerializedName("text")
    @Expose
    val text: String? = "",
    @SerializedName("url")
    @Expose
    val url: String? = "",
    @SerializedName("appLink")
    @Expose
    val appLink: String? = "",
    @SerializedName("type")
    @Expose
    val type: String? = "",
    @SerializedName("backgroundColor")
    @Expose
    val backgroundColor: String? = "",
    @SerializedName("color")
    @Expose
    val color: String? = "",
    @SerializedName("iconURL")
    @Expose
    val iconURL: String? = "",
    @SerializedName("imageURL")
    @Expose
    val imageURL: String? = ""
)

data class Benefits(
    @SerializedName("catalogID")
    @Expose
    val catalogID: Long? = 0,
    @SerializedName("promoID")
    @Expose
    val promoID: Long? = 0,
    @SerializedName("title")
    @Expose
    val title: String? = "",
    @SerializedName("baseCode")
    @Expose
    val baseCode: String? = "",
    @SerializedName("slug")
    @Expose
    val slug: String? = "",
    @SerializedName("assets")
    @Expose
    val assets: List<Assets>? = emptyList()
)


