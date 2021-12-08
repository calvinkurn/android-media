package com.tokopedia.test.application.graphql

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type

object GqlMockUtil {

    /**
     * Convenient utils method to create success response when mocking GraphqlResponse (typically
     * when creating Test Double for GraphqlRepository)
     *
     * Example single request:
     * val example = ExampleResponse(1, "Lorem")
     *
     * val mocked = GqlMockUtil.createSuccessResponse(example)
     *
     * Example multiple request:
     * val example = ExampleResponse(1, "Lorem")
     * val example2 = Example2Response(1, "Lorem")
     * val maps = hashMapOf(example.toGqlPair(), example2.toGqlPair())
     *
     * val mocked = maps.toSuccessGqlResponse()
     * */
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