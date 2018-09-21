package com.tokopedia.mvc.common.domain.mapper

import com.tokopedia.mvc.common.data.GraphQLResult

/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLResult<T> {
    val result: GraphQLResult<T>?
}
