package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.admin.AdminInfoResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetLocationAdminUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase){

    companion object {
        private const val shopID = "shop_id"

        private val QUERY = """
            query getAdminInfo(${'$'}$shopID: Int!){
              getAdminInfo(source: "akw-testing", shop_id: ${'$'}$shopID) {
                admin_data {                        
                  detail_information {
                    admin_role_type {
                      is_location_admin             
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }

    fun execute(shopId: Int, subscriber: Subscriber<GraphqlResponse>) {
        val requestParams = RequestParams().apply {
            putInt(shopID, shopId)
        }.parameters

        GraphqlRequest(QUERY, AdminInfoResponse::class.java, requestParams).let { request ->
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(request)
            graphqlUseCase.execute(subscriber)
        }
    }
}