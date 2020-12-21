package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import rx.Subscriber
import javax.inject.Inject

class GetAdminTypeUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase){

    companion object {
        val QUERY = """
            query getAdminType {
              getAdminType(source: "akw-testing") {
                shopID
                isMultiLocation
                admin_data {
                  admin_type_string
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

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        GraphqlRequest(QUERY, AdminTypeResponse::class.java).let { request ->
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(request)
            graphqlUseCase.execute(subscriber)
        }
    }
}