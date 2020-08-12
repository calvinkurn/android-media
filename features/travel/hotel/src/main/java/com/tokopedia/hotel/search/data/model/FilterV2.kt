package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 12/08/20
 */

data class FilterV2(
        @SerializedName("Type")
        @Expose
        val type: String = "",

        @SerializedName("Name")
        @Expose
        val name: String = "",

        @SerializedName("DisplayName")
        @Expose
        val displayName: String = "",

        @SerializedName("Options")
        @Expose
        val options: List<String> = listOf(),

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