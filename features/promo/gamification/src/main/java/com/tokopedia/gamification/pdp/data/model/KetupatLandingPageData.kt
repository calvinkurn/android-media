package com.tokopedia.gamification.pdp.data.model

import com.google.gson.annotations.SerializedName

data class KetupatLandingPageData(
    @SerializedName("gamiGetScratchCardLandingPage")
    var gamiGetScratchCardLandingPage: GamiGetScratchCardLandingPage
) {
    data class GamiGetScratchCardLandingPage(
        @SerializedName("resultStatus")
        var resultStatus: ResultStatus?,
        @SerializedName("scratchCard")
        var scratchCard: ScratchCard?,
        @SerializedName("appBar")
        var appBar: AppBar?,
        @SerializedName("sections")
        var sections: List<Sections?>
    ) {
        data class ResultStatus(
            @SerializedName("code")
            val code: String?,
            @SerializedName("status")
            val status: String?,
            @SerializedName("message")
            val message: List<String>?
        )

        data class ScratchCard(
            @SerializedName("id")
            val id: Long?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("slug")
            val slug: String?,
            @SerializedName("startTime")
            val startTime: String?,
            @SerializedName("endTime")
            val endTime: String?
        )

        data class AppBar(
            @SerializedName("title")
            val title: String?,
            @SerializedName("isShownShareIcon")
            val isShownShareIcon: Boolean = false,
            @SerializedName("shared")
            val shared: Shared?
        ) {
            data class Shared(
                @SerializedName("ogTitle")
                val ogTitle: String?,
                @SerializedName("ogDescription")
                val ogDescription: String?,
                @SerializedName("ogImageURL")
                val ogImageURL: String?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("page")
                val page: String?,
                @SerializedName("identifier")
                val identifier: String?
            )
        }

        data class Sections(
            @SerializedName("id")
            val id: Long?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("assets")
            val assets: List<Assets?>,
            @SerializedName("text")
            val text: List<Text?>,
            @SerializedName("cta")
            val cta: List<Cta?>,
            @SerializedName("jsonParameter")
            var jsonParameter: String?
        ) {
            data class Assets(
                @SerializedName("key")
                val key: String?,
                @SerializedName("value")
                val value: String?
            )

            data class Text(
                @SerializedName("key")
                val key: String?,
                @SerializedName("value")
                val value: String?
            )

            data class Cta(
                @SerializedName("text")
                val text: String?,
                @SerializedName("url")
                val url: String?,
                @SerializedName("appLink")
                val appLink: String?,
                @SerializedName("type")
                val type: String?,
                @SerializedName("backgroundColor")
                val backgroundColor: String?,
                @SerializedName("color")
                val color: String?,
                @SerializedName("iconURL")
                val iconURL: String?,
                @SerializedName("imageURL")
                val imageURL: String?,
            )
        }
    }
}
