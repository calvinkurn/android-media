package com.tokopedia.scp_rewards.cabinet.domain.model

import com.google.gson.annotations.SerializedName

data class ScpRewardsGetUserMedalisResponse(
    @SerializedName("scpRewardsGetUserMedalisByType")
    val scpRewardsGetUserMedalisByType: ScpRewardsGetUserMedalisByType? = null
) {
    data class ScpRewardsGetUserMedalisByType(
        @SerializedName("medaliBanner")
        val medaliBanner: MedaliBanner? = null,
        @SerializedName("medaliList")
        val medaliList: List<Medal>? = null,
        @SerializedName("paging")
        val paging: Paging? = null,
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null
    ) {
        data class MedaliBanner(
            @SerializedName("imageList")
            val imageList: List<Image>? = null
        ) {
            data class Image(
                @SerializedName("imageURL")
                val imageURL: String = "",
                @SerializedName("redirectAppLink")
                val redirectAppLink: String = "",
                @SerializedName("redirectURL")
                val redirectURL: String = "",
                @SerializedName("creativeName")
                val creativeName: String = ""
            )
        }

        data class Medal(
            @SerializedName("celebrationImageURL")
            val celebrationImageURL: String = "",
            @SerializedName("cta")
            val cta: Cta? = null,
            @SerializedName("extraInfo")
            val extraInfo: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("isDisabled")
            val isDisabled: Boolean = false,
            @SerializedName("isNewMedali")
            val isNewMedali: Boolean = false,
            @SerializedName("logoImageURL")
            val logoImageURL: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("progressionCompletement")
            val progressionCompletement: Int = 0,
            @SerializedName("provider")
            val provider: String = ""
        ) {
            data class Cta(
                @SerializedName("appLink")
                val appLink: String = "",
                @SerializedName("url")
                val url: String = ""
            )
        }

        data class Paging(
            @SerializedName("cta")
            val cta: Cta? = null,
            @SerializedName("hasNext")
            val hasNext: Boolean = false
        ) {
            data class Cta(
                @SerializedName("appLink")
                val appLink: String = "",
                @SerializedName("isShown")
                val isShown: Boolean = false,
                @SerializedName("text")
                val text: String = "",
                @SerializedName("url")
                val url: String = ""
            )
        }

        data class ResultStatus(
            @SerializedName("code")
            val code: String = "",
            @SerializedName("message")
            val message: List<String>? = null,
            @SerializedName("status")
            val status: String = ""
        )
    }
}

fun ScpRewardsGetUserMedalisResponse.getData(successCode: String): ScpRewardsGetUserMedalisResponse? {
    return if (scpRewardsGetUserMedalisByType?.resultStatus?.code != successCode) null else this
}
