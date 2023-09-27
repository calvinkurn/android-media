package com.tokopedia.sellerorder.common.presenter.model

import com.google.gson.annotations.SerializedName

data class PopUp(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("body")
    val body: String = "",

    @SerializedName("actionButton")
    val actionButtons: List<ActionButton> = emptyList(),

    @SerializedName("template")
    val template: Template? = null

) {
    data class ActionButton(
        @SerializedName("displayName")
        val displayName: String = "",

        @SerializedName("color")
        val color: String = "",

        @SerializedName("type")
        val type: String = ""

    )

    data class Template(
        @SerializedName("code")
        val code: String? = null,

        @SerializedName("params")
        val param: Params? = null

    ) {
        // todo ask ka tama how iOS handle this
        data class Params(
            @SerializedName("lg_fmd_txt_learn_more")
            val learnMoreText: TypeValue = TypeValue(),

            @SerializedName("lg_fmd_txt_open_dropoff_maps")
            val dropoffText: TypeValue = TypeValue(),

            @SerializedName("lg_fmd_url_learn_more")
            val learnMoreUrl: TypeValue = TypeValue(),

            @SerializedName("lg_fmd_url_open_dropoff_maps")
            val dropoffUrl: TypeValue = TypeValue()
        ) {
            data class TypeValue(
                @SerializedName("type")
                val type: String = "",

                @SerializedName("data")
                val data: String = ""
            )
        }
    }
}
