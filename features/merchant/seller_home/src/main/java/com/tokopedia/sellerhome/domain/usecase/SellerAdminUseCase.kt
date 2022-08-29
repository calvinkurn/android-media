package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("SellerAdminGqlQuery", SellerAdminUseCase.QUERY)
class SellerAdminUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    BaseGqlUseCase<AdminDataResponse>() {

    var requestParams: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): AdminDataResponse {
        GraphqlRequest(
            SellerAdminGqlQuery(), AdminTypeResponse::class.java, requestParams.parameters
        ).let { request ->
            gqlRepository.response(listOf(request)).let { response ->
                response.getError(AdminTypeResponse::class.java).let { errors ->
                    if (errors.isNullOrEmpty()) {
                        response.getData<AdminTypeResponse>(AdminTypeResponse::class.java)
                            .let { data ->
                                return data.response
                            }
                    } else {
                        throw MessageErrorException(errors.joinToString { it.message })
                    }
                }
            }
        }
    }

    companion object {
        const val QUERY = """
            query getAdminType(${'$'}source: String!) {
              getAdminType(source: ${'$'}source) {
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
                }
              }
            }
        """
        private const val DEFAULT_SOURCE = "android"
        private const val PARAM_SOURCE = "source"

        @JvmStatic
        fun createRequestParams(source: String = DEFAULT_SOURCE): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_SOURCE, source)
            }
        }
    }
}