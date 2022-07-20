package com.tokopedia.chat_common.util

import android.util.Log
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type

/**
 * @author by nisie on 10/12/18.
 * Please implement onError.
 */

fun handleError(graphqlResponse: GraphqlResponse,
                type: Type, routingOnNext: (GraphqlResponse) -> Unit,
                onError: (Throwable) -> Unit = { Log.d("ERR", it.toString()) }) {
    val graphqlErrorList = graphqlResponse.getError(type)
    if (graphqlErrorList == null || graphqlErrorList.isEmpty()) {
        routingOnNext(graphqlResponse)
    } else if (!graphqlErrorList.isEmpty()
            && graphqlErrorList[0] != null
            && graphqlErrorList[0].message != null) {
        onError(MessageErrorException(graphqlErrorList[0].message))
    }
}