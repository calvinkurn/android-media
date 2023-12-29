package com.tokopedia.scp_rewards.cabinet.domain.model


import com.google.gson.annotations.SerializedName

data class ScpRewardsGetMedaliSectionResponse(
    @SerializedName("scpRewardsGetMedaliSectionLayout")
    val scpRewardsGetMedaliSectionLayout: ScpRewardsGetMedaliSectionLayout? = null
){
    data class ScpRewardsGetMedaliSectionLayout(
        @SerializedName("medaliSectionLayoutList")
        val medaliSectionLayoutList: List<MedaliSectionLayout>? = null,
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null
    ){
        data class ResultStatus(
            @SerializedName("code")
            val code: String = "",
            @SerializedName("message")
            val message: List<String>? = null,
            @SerializedName("status")
            val status: String = ""
        )

        data class MedaliSectionLayout(
            @SerializedName("backgroundColor")
            val backgroundColor: String = "",
            @SerializedName("backgroundImageURL")
            val backgroundImageURL: String = "",
            @SerializedName("display")
            val display: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("jsonParameter")
            val jsonParameter: String = "",
            @SerializedName("layout")
            val layout: String = "",
            @SerializedName("medaliSectionTitle")
            val medaliSectionTitle: MedaliSectionTitle? = null
        ){
            data class MedaliSectionTitle(
                @SerializedName("color")
                val color: String = "",
                @SerializedName("content")
                val content: String = "",
                @SerializedName("description")
                val description: String = ""
            )
        }

    }
}
