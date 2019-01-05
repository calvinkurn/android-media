package com.tokopedia.topads.dashboard.data.utils.extention

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse

inline fun <reified T> GraphqlResponse.getRealData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()){
        return getData(T::class.java)
    } else {
        throw MessageErrorException(error[0].message)
    }
}