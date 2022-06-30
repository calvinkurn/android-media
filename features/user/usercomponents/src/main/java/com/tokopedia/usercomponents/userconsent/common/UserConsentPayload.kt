package com.tokopedia.usercomponents.userconsent.common

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class UserConsentPayload (
    @SerializedName("collection_id")
    var collectionId: String = "",
    @SerializedName("verion")
    var version: String = "",
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