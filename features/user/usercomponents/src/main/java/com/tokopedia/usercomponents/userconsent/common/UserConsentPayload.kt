package com.tokopedia.usercomponents.userconsent.common

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class UserConsentPayload (
    @SerializedName("identifier")
    var identifier: String = "",
    @SerializedName("collection_id")
    var collectionId: String = "",
    @SerializedName("dataElements")
    var dataElements: Map<String, String>? = mapOf(),
    @SerializedName("default")
    var default: Boolean = false,
    @SerializedName("purpose")
    var purposes: MutableList<PurposeDataModel> = mutableListOf()
) {
    data class PurposeDataModel(
        @SerializedName("purpose_id")
        var purposeId: String = "",
        @SerializedName("version")
        var version: String = "",
        @SerializedName("transaction_type")
        var transactionType: String = ""
    )

    override fun toString(): String {
        return Gson().toJson(this, UserConsentPayload::class.java)
    }
}
