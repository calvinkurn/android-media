package com.tokopedia.merchantvoucher.common.gql.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 08/08/18.
 */

data class GraphQLResult<T>(@SerializedName("result")
                            @Expose
                            val result: T? = null,
                            @SerializedName("error")
                            @Expose
                            val graphQLDataError: GraphQLDataError? = null) {

}
