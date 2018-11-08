package com.tokopedia.merchantvoucher.common.gql.data.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 08/08/18.
 */

data class GraphQLSuccessMessage(
        @SerializedName("success")
        @Expose
        val isSuccess: Boolean = false,
        @SerializedName("message")
        @Expose
        val message: String? = null) {
}
