package com.tokopedia.sellerhome.util

import com.tokopedia.graphql.data.model.GraphqlResponse

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

inline fun <reified T> GraphqlResponse.getData(): T {
    return this.getData<T>(T::class.java)
}