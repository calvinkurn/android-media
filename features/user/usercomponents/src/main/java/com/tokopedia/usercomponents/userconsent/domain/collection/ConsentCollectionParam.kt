package com.tokopedia.usercomponents.userconsent.domain.collection

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ConsentCollectionParam(
    @SerializedName("collectionID")
    var collectionId: String,
    @SerializedName("version")
    var version: String = "",
    @SerializedName("dataElements")
    val dataElements: Map<String, String>? = null,
    @SerializedName("identifier")
    var identifier: String = ""
): GqlParam
