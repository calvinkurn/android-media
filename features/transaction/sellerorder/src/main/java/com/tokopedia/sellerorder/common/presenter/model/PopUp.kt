package com.tokopedia.sellerorder.common.presenter.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopUp(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("body")
    @Expose
    val body: String = "",

    @SerializedName("actionButton")
    @Expose
    val actionButtons: List<ActionButton> = emptyList(),

    @SerializedName("template")
    val template: Template = Template()

) {
    data class ActionButton(
        @SerializedName("displayName")
        @Expose
        val displayName: String = "",

        @SerializedName("color")
        @Expose
        val color: String = "",

        @SerializedName("type")
        @Expose
        val type: String = ""

    )

    data class Template(
        @SerializedName("code")
        val code: String = "",

        @SerializedName("params")
        val param: Params = Params()

    ) {
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
