package com.tokopedia.usercomponents.userconsent.domain.collection

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ConsentCollectionParam(
    @SerializedName("collectionID")
    var collectionId: String,
    @SerializedName("version")
    var version: String,
): GqlParam
