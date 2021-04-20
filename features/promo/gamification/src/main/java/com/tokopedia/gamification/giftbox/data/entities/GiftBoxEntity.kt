package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName
import com.tokopedia.gamification.taptap.data.entiity.TokenAsset

data class GiftBoxEntity(@SerializedName("gamiLuckyHome") val gamiLuckyHome: GamiLuckyHome)

data class ResultStatus(

    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: List<String>,
    @SerializedName("reason") val reason: String
)

data class GamiLuckyHome(

        @SerializedName("resultStatus") val resultStatus: ResultStatus,
        @SerializedName("tokensUser") val tokensUser: TokensUser,
        @SerializedName("tokenAsset") val tokenAsset: TokenAsset,
        @SerializedName("actionButton") val actionButton: List<ActionButton>?,
        @SerializedName("prizeList") val prizeList: List<PrizeListItem>?,
        @SerializedName("reminder") val reminder: Reminder?,
        @SerializedName("infoURL") val infoUrl: String?,
        @SerializedName("prizeDetailList") val prizeDetailList: List<PrizeDetailListItem?>?,
        @SerializedName("prizeDetailListButton") val prizeDetailListButton: PrizeDetailListButton?,
        @SerializedName("bottomSheetButtonText") val bottomSheetButtonText: String?,
)

data class PrizeDetailListItem(
        @SerializedName("isSpecial") val isSpecial: Boolean?,
        @SerializedName("imageURL") val imageURL: String?,
        @SerializedName("text") val text: String?,
)
data class PrizeDetailListButton(
        @SerializedName("url") val url: String?,
        @SerializedName("applink") val applink: String?,
        @SerializedName("text") val text: String?,
)

data class TokensUser(

    @SerializedName("state") val state: String,
    @SerializedName("title") val title: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("campaignSlug") val campaignSlug: String?
)

data class ActionButton(

    @SerializedName("text") val text: String,
    @SerializedName("type") val type: String?,
    @SerializedName("url") val url: String,
    @SerializedName("applink") val applink: String?,
    @SerializedName("backgroundColor") val backgroundColor: String
)

data class PrizeListItem(

    @SerializedName("isSpecial") val isSpecial: Boolean,
    @SerializedName("imageURL") val imageURL: String,
    @SerializedName("text") val text: List<String>
)

data class Reminder(

    @SerializedName("text") val text: String,
    @SerializedName("enableText") val enableText: String,
    @SerializedName("disableText") val disableText: String,
    @SerializedName("buttonSet") val buttonSet: String?,
    @SerializedName("buttonUnset") val buttonUnset: String?,
    @SerializedName("textSet") val textSet: String?,
    @SerializedName("textUnset") val textUnset: String?,
    @SerializedName("isShow") val isShow: Boolean?,
)