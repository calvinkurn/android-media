package com.tokopedia.usercomponents.userconsent.domain.collection

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.usercomponents.userconsent.domain.submission.DataElements

data class ConsentCollectionParam(
    @SerializedName("collectionID")
    var collectionId: String,
    @SerializedName("version")
    var version: String = "",
    @SerializedName("dataElements")
    val dataElements: MutableList<DataElements> = mutableListOf(),
    @SerializedName("identifier")
    var identifier: String = ""
): GqlParam
