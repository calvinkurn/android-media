package com.tokopedia.mvc.common.domain.mapper

import com.tokopedia.mvc.common.data.GraphQLSuccessMessage


/**
 * Created by hendry on 08/08/18.
 */

interface HasGraphQLSuccess {
    val graphQLSuccessMessage: GraphQLSuccessMessage?
}
