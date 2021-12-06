package com.tokopedia.test.application.graphql

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type

object GqlMockUtil {

    inline fun <reified T : Any> createSuccessResponse(obj: T): GraphqlResponse {
        val success = hashMapOf<Type, Any>(T::class.java to obj)
        return success.toSuccessGqlResponse()
    }

}

fun HashMap<Type, Any>.toSuccessGqlResponse(): GraphqlResponse {
    return GraphqlResponse(this, HashMap<Type, List<GraphqlError>>(), false)
}

inline fun <reified T : Any> T.toGqlPair(): Pair<Type, T> {
    return T::class.java to this
}