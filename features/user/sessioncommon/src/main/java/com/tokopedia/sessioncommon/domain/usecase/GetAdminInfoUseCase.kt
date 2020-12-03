package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.admin.AdminInfoResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetAdminInfoUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase){

    companion object {
        private const val shopID = "shop_id"

        private val QUERY = """
            query getAdminInfo(${'$'}$shopID: Int!){
              getAdminInfo(source: "akw-testing", shop_id: ${'$'}$shopID) {
                admin_data {
                  location_list {
                    location_id
                  }
                  detail_information {
                    admin_role_type {
                      is_shop_admin
                      is_location_admin
                      is_shop_owner            
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