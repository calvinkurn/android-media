package com.tokopedia.merchantvoucher.common.gql.domain.mapper

import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLSuccessMessage


/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLSuccess {
    val graphQLSuccessMessage: GraphQLSuccessMessage?
}
