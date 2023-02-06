package com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request

import com.google.gson.annotations.SerializedName

data class DynamicDataPassingParamRequest(
    @SerializedName("source")
    var source: String = "",

    @SerializedName("data")
    var data: List<DynamicDataParam> = arrayListOf()
) {

    data class DynamicDataParam(
        @SerializedName("level")
        var level: String = "",

        @SerializedName("parent_unique_id")
        var parentUniqueId: String = "",

        @SerializedName("unique_id")
        var uniqueId: String = "",

        @SerializedName("attribute")
        var attribute: String = "",

        @SerializedName("donation")
        var donation: Boolean = false,

        @SerializedName("add_on")
        var addOn: AddOn = AddOn()
    )

    data class AddOn(
        @SerializedName("source")
        var source: String = "",

        @SerializedName("add_on_data")
        var addOnData: List<AddOnDataParam> = emptyList()
    ) {

        data class AddOnDataParam(
            @SerializedName("add_on_id")
            var addOnId: Long = 0L,

            @SerializedName("add_on_qty")
            var addOnQty: Int = 0,

            @SerializedName("add_on_metadata")
            var addOnMetadata: AddOnMetadataParam = AddOnMetadataParam()
        ) {
            data class AddOnMetadataParam(
                @SerializedName("add_on_note")
                var addOnNote: AddOnNoteParam = AddOnNoteParam()
            ) {
                data class AddOnNoteParam(
                    @SerializedName("from")
                    var from: String = "",

                    @SerializedName("is_custom_note")
                    var isCustomNote: Boolean = false,

                    @SerializedName("notes")
                    var notes: String = "",

                    @SerializedName("to")
                    var to: String = ""
                )
            }
        }
    }
}
