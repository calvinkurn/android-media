package com.tokopedia.merchantvoucher.common.gql.data.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 08/08/18.
 */

data class GraphQLDataError(
        @SerializedName("message")
        @Expose
        val message: String? = null) {
}
