package com.tokopedia.merchantvoucher.common.domain.mapper

import com.tokopedia.merchantvoucher.common.data.GraphQLSuccessMessage


/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLSuccess {
    val graphQLSuccessMessage: GraphQLSuccessMessage?
}
