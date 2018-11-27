package com.tokopedia.shop.common.graphql.domain.mapper

import com.tokopedia.shop.common.graphql.data.GraphQLResult

/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLResult<T> {
    val result: GraphQLResult<T>?
}
