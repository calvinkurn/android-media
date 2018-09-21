package com.tokopedia.merchantvoucher.common.domain.mapper

import com.tokopedia.merchantvoucher.common.data.GraphQLResult

/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLResult<T> {
    val result: GraphQLResult<T>?
}
