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

    override suspend fun executeOnBackground(): AdminDataResponse {
        GraphqlRequest(GetAdminTypeUseCase.QUERY, AdminTypeResponse::class.java, RequestParams.EMPTY.parameters).let { request ->
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