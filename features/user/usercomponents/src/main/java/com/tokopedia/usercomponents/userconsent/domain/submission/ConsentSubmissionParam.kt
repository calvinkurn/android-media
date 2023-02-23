package com.tokopedia.usercomponents.userconsent.domain.submission

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam


/**
 * Submission Consent Parameters
 */
data class ConsentSubmissionParam(
    @SerializedName("collectionId")
    var collectionId: String = "",
    /** collection version */
    @SerializedName("version")
    var version: String = "",
    @SerializedName("dataElements")
    var dataElements: Map<String, String>? = mapOf(),
    @SerializedName("default")
    var default: Boolean = false,
    @SerializedName("purposes")
    var purposes: MutableList<Purpose> = mutableListOf()
): GqlParam

data class Purpose(
    @SerializedName("purposeID")
    var purposeID: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("version")
    /** purposes version */
    var version: String = "",
)
