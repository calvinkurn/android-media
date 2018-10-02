package com.tokopedia.merchantvoucher.common.gql.domain.mapper

import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLResult

/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLResult<T> {
    val result: GraphQLResult<T>?
}
