package com.tokopedia.sellerorder.detail.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomDetailIncomeDetailResponse(
    @SerializedName("get_som_income_detail")
    @Expose
    val getSomIncomeDetail: GetSomIncomeDetail? = null
) {
    data class GetSomIncomeDetail(
        @SerializedName("sections")
        @Expose
        val sections: List<Section?>? = null,
        @SerializedName("summary")
        @Expose
        val summary: Summary? = null,
        @SerializedName("title")
        @Expose
        val title: String? = null
    ) {
        data class Section(
            @SerializedName("attributes")
            @Expose
            val attributes: List<Attribute?>? = null,
            @SerializedName("components")
            @Expose
            val components: List<Component?>? = null,
            @SerializedName("key")
            @Expose
            val key: String? = null,
            @SerializedName("label")
            @Expose
            val label: String? = null,
            @SerializedName("sub_label")
            @Expose
            val subLabel: String? = null,
            @SerializedName("value")
            @Expose
            val value: String? = null,
            @SerializedName("value_raw")
            @Expose
            val valueRaw: Int? = null
        ) {
            data class Component(
                @SerializedName("attributes")
                @Expose
                val attributes: List<Attribute?>? = null,
                @SerializedName("key")
                @Expose
                val key: String? = null,
                @SerializedName("label")
                @Expose
                val label: String? = null,
                @SerializedName("sub_label")
                @Expose
                val subLabel: Any? = null,
                @SerializedName("type")
                @Expose
                val type: String? = null,
                @SerializedName("value")
                @Expose
                val value: String? = null,
                @SerializedName("value_raw")
                @Expose
                val valueRaw: Int? = null
            )
        }

        data class Summary(
            @SerializedName("key")
            @Expose
            val key: String? = null,
            @SerializedName("label")
            @Expose
            val label: String? = null,
            @SerializedName("sub_label")
            @Expose
            val subLabel: String? = null,
            @SerializedName("value")
            @Expose
            val value: String? = null,
            @SerializedName("value_raw")
            @Expose
            val valueRaw: Int? = null,
            @SerializedName("attributes")
            @Expose
            val attributes: List<Attribute?>? = null,
            @SerializedName("state")
            @Expose
            val state: String? = null,
            @SerializedName("note")
            @Expose
            val note: String? = null
        )

        data class Attribute(
            @SerializedName("data")
            @Expose
            val data: Data? = null,
            @SerializedName("tooltip")
            @Expose
            val tooltip: Tooltip? = null
        ) {
            data class Data(
                @SerializedName("type")
                @Expose
                val type: String? = null,
                @SerializedName("icon_url")
                @Expose
                val iconUrl: String? = null,
                @SerializedName("icon_url_dark")
                @Expose
                val iconUrlDark: String? = null,
                @SerializedName("level")
                @Expose
                val level: String? = null,
                @SerializedName("label")
                @Expose
                val label: String? = null
            )

            data class Tooltip(
                @SerializedName("title")
                @Expose
                val title: String? = null,
                @SerializedName("value")
                @Expose
                val value: String? = null
            )
        }
    }
}
