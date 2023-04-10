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
    @SerializedName("default")
    var default: Boolean = false,
    @SerializedName("purposes")
    var purposes: MutableList<Purpose> = mutableListOf(),
    @SerializedName("dataElements")
    var dataElements: MutableList<DataElements> = mutableListOf()
): GqlParam

data class Purpose(
    @SerializedName("purposeID")
    var purposeID: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("version")
    /** purposes version */
    var version: String = "",
    @SerializedName("dataElementType")
    var dataElementType: String = ""
)

data class DataElements(
    @SerializedName("elementName")
    var elementName: String = "",
    @SerializedName("elementValue")
    var elementValue: String = ""
)
