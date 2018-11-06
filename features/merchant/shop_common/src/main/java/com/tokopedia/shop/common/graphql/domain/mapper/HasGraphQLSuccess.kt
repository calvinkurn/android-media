package com.tokopedia.shop.common.graphql.domain.mapper

import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage

/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLSuccess {
    val graphQLSuccessMessage: GraphQLSuccessMessage?
}
