package com.tokopedia.product.addedit.description.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetYoutubeVideoSnippetResponse(
    @SerializedName("GetYoutubeVideoSnippet")
    @Expose
    val getYoutubeVideoSnippet: GetYoutubeVideoSnippet = GetYoutubeVideoSnippet()
) {

    data class GetYoutubeVideoSnippet(
        @SerializedName("Items")
        @Expose
        val items: List<Items> = listOf(),
        @SerializedName("Error")
        @Expose
        val error: Error = Error()
    ) {

        data class Items(
            @SerializedName("ID")
            @Expose
            val id: String = "",
            @SerializedName("Snippet")
            @Expose
            val snippet: Snippet = Snippet()
        )

        data class Snippet(
            @SerializedName("Title")
            @Expose
            val title: String = "",
            @SerializedName("Description")
            @Expose
            val description: String = "",
            @SerializedName("Thumbnails")
            @Expose
            val thumbnails: Thumbnails = Thumbnails()
        )

        data class Thumbnails(
            @SerializedName("Default")
            @Expose
            val default: Default = Default()
        )

        data class Default(
            @SerializedName("URL")
            @Expose
            val url: String = "",
            @SerializedName("Width")
            @Expose
            val hidth: Int = 0,
            @SerializedName("Height")
            @Expose
            val height: Int = 0
        )

        data class Error(
            @SerializedName("messages")
            @Expose
            val messages: String = "",
            @SerializedName("reason")
            @Expose
            val reason: List<String> = listOf(),
            @SerializedName("errorCode")
            @Expose
            val errorCode: String = ""
        )
    }
}
