package com.tokopedia.privacycenter.main.di

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil

inline fun <reified DataModel : Any> createResponseFromJson(json: String): GraphqlResponse {
    val obj = Gson().fromJson(json, DataModel::class.java)
    return GqlMockUtil.createSuccessResponse(obj)
}
