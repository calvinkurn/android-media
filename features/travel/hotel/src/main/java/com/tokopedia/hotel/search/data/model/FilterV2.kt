package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 12/08/20
 */

data class FilterV2(
        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("displayName")
        @Expose
        val displayName: String = "",

        @SerializedName("options")
        @Expose
        var options: List<String> = listOf(),

        var optionSelected: List<String> = listOf(),

        @SerializedName("image")
        @Expose
        val image: Images = Images()
) {
    data class Images(
            @SerializedName("light")
            @Expose
            val light: String = "",

            @SerializedName("dark")
            @Expose
            val dark: String = ""
    )
}