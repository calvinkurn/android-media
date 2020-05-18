package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class ComponentsItem (

        @SerializedName("render_by_default")
        val renderByDefault: Boolean = false,

        @SerializedName("data")
        var data: List<DataItem>? = null,

        @SerializedName("ab_modulo")
        val abModulo: String? = "",

        @SerializedName("name")
        var name: String? = "",

        @SerializedName("ab_default")
        val abDefault: Boolean = false,

        @SerializedName("id")
        val id: String = "",

        @SerializedName("is_ab")
        val isAb: Boolean = false,

        @SerializedName("title")
        val title: String? = "",

        @SerializedName("skiprender")
        val skiprender: Boolean = false,

        @SerializedName("properties")
        var properties: Properties? = null
)