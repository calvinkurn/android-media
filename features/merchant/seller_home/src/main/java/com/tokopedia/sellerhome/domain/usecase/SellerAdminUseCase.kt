package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SellerAdminUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): BaseGqlUseCase<AdminDataResponse>() {

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

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): AdminDataResponse {
        GraphqlRequest(QUERY, AdminTypeResponse::class.java, requestParams.parameters).let { request ->
            gqlRepository.getReseponse(listOf(request)).let { response ->
                response.getError(AdminTypeResponse::class.java).let { errors ->
                    if (errors.isNullOrEmpty()) {
                        response.getData<AdminTypeResponse>(AdminTypeResponse::class.java).let { data ->
                            return data.response
                        }
                    } else {
                        throw MessageErrorException(errors.joinToString { it.message })
                    }
                }
            }
        }
    }

}