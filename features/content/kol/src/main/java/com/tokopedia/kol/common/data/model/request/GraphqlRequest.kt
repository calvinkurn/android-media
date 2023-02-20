package com.tokopedia.kol.common.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * @author by milhamj on 26/02/18.
 */
data class GraphqlRequest(
    @SerializedName("query") val query: String,
    @SerializedName("variables") val variables: HashMap<String, Any>
)
