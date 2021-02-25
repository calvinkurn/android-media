package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetAdminTypeUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase){

    companion object {
        private const val DEFAULT_SOURCE = "android"
        private const val PARAM_SOURCE = "source"
        private const val SOURCE = "\$source"

        val QUERY = """
            query getAdminType($SOURCE: String!) {
              getAdminType(source: $SOURCE) {
                shopID
                isMultiLocation
                admin_data {
                  detail_information {
                    admin_role_type {
                      is_shop_admin
                      is_location_admin
                      is_shop_owner            
                    }
                  }
                  status
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createRequestParams(source: String = DEFAULT_SOURCE): RequestParams =
                RequestParams.create().apply {
                    putString(PARAM_SOURCE, source)
                }
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>, source: String = DEFAULT_SOURCE) {
        val requestParams = RequestParams().apply { putString(PARAM_SOURCE, source) }.parameters
        GraphqlRequest(QUERY, AdminTypeResponse::class.java, requestParams).let { request ->
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(request)
            graphqlUseCase.execute(subscriber)
        }
    }
}