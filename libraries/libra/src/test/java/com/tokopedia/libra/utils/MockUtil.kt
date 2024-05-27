package com.tokopedia.libra.utils

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type

object MockUtil {

    inline fun <reified T : Any> createSuccessResponse(obj: T): GraphqlResponse {
        val success = hashMapOf<Type, Any>(T::class.java to obj)
        return GraphqlResponse(success, HashMap<Type, List<GraphqlError>>(), false)
    }

}
