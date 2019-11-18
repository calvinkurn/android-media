package com.tokopedia.v2.home.model.pojo.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DynamicHomeIcon (
        @Expose
        @SerializedName("useCaseIcon")
        val useCaseIcon: List<DynamicIcon> = listOf(),

        @Expose
        @SerializedName("dynamicIcon")
        val dynamicIcon: List<DynamicIcon> = listOf()
)
open class UseCaseIcon(
        @Expose
        @SerializedName("id")
        val id: String = "-1",

        @Expose
        @SerializedName("applinks")
        val applinks: String = "",

        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String = "",

        @Expose
        @SerializedName("name")
        val name: String = "",

        @Expose
        @SerializedName("url")
        val url: String = ""
)

data class DynamicIcon(
        @Expose
        @SerializedName("id")
        val id: String = "-1",

        @Expose
        @SerializedName("applinks")
        val applinks: String = "",

        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String = "",

        @Expose
        @SerializedName("name")
        val name: String = "",

        @Expose
        @SerializedName("url")
        val url: String = "",

        @Expose
        @SerializedName("bu_identifier")
        val bu_identifier: String = ""
)