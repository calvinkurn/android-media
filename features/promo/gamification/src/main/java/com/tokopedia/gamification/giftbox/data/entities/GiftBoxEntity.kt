package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName

data class GiftBoxEntity(@SerializedName("gamiLuckyHome") val gamiLuckyHome: GamiLuckyHome)

data class ResultStatus(

    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: List<String>,
    @SerializedName("reason") val reason: String
)

data class GamiLuckyHome(

    @SerializedName("resultStatus") val resultStatus: ResultStatus,
    @SerializedName("tokensUser") val tokensUser: TokensUser,
    @SerializedName("tokenAsset") val tokenAsset: TokenAsset,
    @SerializedName("actionButton") val actionButton: List<ActionButton>?,
    @SerializedName("prizeList") val prizeList: List<PrizeList>?,
    @SerializedName("reminder") val reminder: Reminder
)

data class TokensUser(

    @SerializedName("state") val state: String,
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("campaignSlug") val campaignSlug: String?
)

data class TokenAsset(

    @SerializedName("backgroundImgURL") val backgroundImgURL: String,
    @SerializedName("imageV2URLs") val imageV2URLs: List<String>
)

data class ActionButton(

    @SerializedName("text") val text: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("applink") val applink: String?,
    @SerializedName("backgroundColor") val backgroundColor: String
)

data class PrizeList(

    @SerializedName("isSpecial") val isSpecial: Boolean,
    @SerializedName("imageURL") val imageURL: String,
    @SerializedName("text") val text: List<String>
)

data class Reminder(

    @SerializedName("text") val text: String,
    @SerializedName("enableText") val enableText: String,
    @SerializedName("disableText") val disableText: String
)