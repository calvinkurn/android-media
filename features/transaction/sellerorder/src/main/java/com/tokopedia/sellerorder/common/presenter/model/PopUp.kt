package com.tokopedia.sellerorder.common.presenter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerorder.common.util.SomConsts

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

    fun getPrimaryButton(): ActionButton? {
        return actionButtons.firstOrNull { it.type == SomConsts.KEY_PRIMARY_DIALOG_BUTTON }
    }

    fun getSecondaryButton(): ActionButton? {
        return actionButtons.firstOrNull { it.type == SomConsts.KEY_SECONDARY_DIALOG_BUTTON }
    }

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
        data class Params(
            @SerializedName("lg_fmd_txt_learn_more")
            val learnMoreText: TypeValue? = null,

            @SerializedName("lg_fmd_txt_open_dropoff_maps")
            val dropoffText: TypeValue? = null,

            @SerializedName("lg_fmd_url_learn_more")
            val learnMoreUrl: TypeValue? = null,

            @SerializedName("lg_fmd_url_open_dropoff_maps")
            val dropoffUrl: TypeValue? = null
        ) {
            data class TypeValue(
                @SerializedName("type")
                val type: String? = null,

                @SerializedName("data")
                val data: String? = null
            )
        }
    }
}
