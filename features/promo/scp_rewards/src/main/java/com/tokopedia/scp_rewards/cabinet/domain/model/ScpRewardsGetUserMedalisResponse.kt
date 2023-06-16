package com.tokopedia.scp_rewards.cabinet.domain.model


import com.google.gson.annotations.SerializedName

data class ScpRewardsGetUserMedalisResponse(
    @SerializedName("scpRewardsGetUserMedalisByType")
    val scpRewardsGetUserMedalisByType: ScpRewardsGetUserMedalisByType
){
    data class ScpRewardsGetUserMedalisByType(
        @SerializedName("medaliBanner")
        val medaliBanner: MedaliBanner,
        @SerializedName("medaliList")
        val medaliList: List<Medal>,
        @SerializedName("paging")
        val paging: Paging,
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus
    ){
        data class MedaliBanner(
            @SerializedName("imageList")
            val imageList: List<Image>
        ){
            data class Image(
                @SerializedName("imageURL")
                val imageURL: String,
                @SerializedName("redirectAppLink")
                val redirectAppLink: String,
                @SerializedName("redirectURL")
                val redirectURL: String
            )
        }

        data class Medal(
            @SerializedName("celebrationImageURL")
            val celebrationImageURL: String,
            @SerializedName("cta")
            val cta: Cta,
            @SerializedName("extraInfo")
            val extraInfo: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("isDisabled")
            val isDisabled: Boolean,
            @SerializedName("isNewMedali")
            val isNewMedali: Boolean,
            @SerializedName("logoImageURL")
            val logoImageURL: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("progressionCompletement")
            val progressionCompletement: Int,
            @SerializedName("provider")
            val provider: String
        ){
            data class Cta(
                @SerializedName("appLink")
                val appLink: String,
                @SerializedName("url")
                val url: String
            )
        }


        data class Paging(
            @SerializedName("cta")
            val cta: Cta,
            @SerializedName("hasNext")
            val hasNext: Boolean
        ){
            data class Cta(
                @SerializedName("appLink")
                val appLink: String,
                @SerializedName("isShown")
                val isShown: Boolean,
                @SerializedName("text")
                val text: String,
                @SerializedName("url")
                val url: String
            )
        }

        data class ResultStatus(
            @SerializedName("code")
            val code: String,
            @SerializedName("message")
            val message: List<String>,
            @SerializedName("status")
            val status: String
        )

    }
}

fun ScpRewardsGetUserMedalisResponse.getData(successCode:String) : ScpRewardsGetUserMedalisResponse? {
    return if(scpRewardsGetUserMedalisByType.resultStatus.code!=successCode) null else this
}
