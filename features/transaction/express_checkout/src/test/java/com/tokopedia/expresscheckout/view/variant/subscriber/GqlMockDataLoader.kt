package com.tokopedia.expresscheckout.view.variant.subscriber

import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import java.lang.reflect.Type

open class GqlMockDataLoader {

    fun getGraphqlResponse(rawJson: String, typeList: ArrayList<Type>): GraphqlResponse {
        val response = GraphqlResponseInternal(JsonParser().parse(rawJson).asJsonArray, false)

        val mResults = HashMap<Type, Any>()
        val mErrors = HashMap<Type, List<GraphqlError>>()
        val mIsCached = false

        val results = java.util.HashMap<Type, Any>()
        val errors = java.util.HashMap<Type, List<GraphqlError>>()
        for (i in 0 until response.originalResponse.size()) {
            try {
                val data = response.originalResponse.get(i).asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                if (data != null && !data.isJsonNull) {
                    //Lookup for data
                    results[typeList.get(i)] = CommonUtils.fromJson<Any>(data.toString(), typeList.get(i))
                }

                val error = response.originalResponse.get(i).asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                if (error != null && !error.isJsonNull) {
                    //Lookup for error
                    errors[typeList.get(i)] = CommonUtils.fromJson<List<GraphqlError>>(error.toString(), object : TypeToken<List<GraphqlError>>() {

                    }.type)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //Just to avoid any accidental data loss
            }

        }
        return GraphqlResponse(results, errors, response.isCached)
    }
}