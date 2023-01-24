package com.tokopedia.checkout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.checkout.data.model.response.dynamicdata.UpdateDynamicDataPassingResponse
import com.tokopedia.checkout.data.model.response.dynamicdata.UpdateDynamicDataPassingUiModel
import com.tokopedia.checkout.domain.model.cartshipmentform.DynamicDataPassingParamRequest
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateDynamicDataPassingUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<UpdateDynamicDataPassingUiModel>() {

    private var params: Map<String, Any?>? = null

    fun setParams(params: DynamicDataPassingParamRequest) {
        this.params = mapOf("params" to params)
    }

    @GqlQuery(UPDATE_DYNAMIC_DATA_PASSING_QUERY, query)
    override suspend fun executeOnBackground(): UpdateDynamicDataPassingUiModel {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(
            UpdateDynamicDataPassingQuery(),
            UpdateDynamicDataPassingResponse::class.java,
            params
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<UpdateDynamicDataPassingResponse>()

        if (response.status == "OK") {
            return UpdateDynamicDataPassingUiModel(response.dynamicData)
        } else {
            if (response.errorMessages.isNotEmpty()) {
                throw CartResponseErrorException(response.errorMessages.joinToString())
            } else {
                throw CartResponseErrorException(CartConstant.CART_ERROR_GLOBAL)
            }
        }
    }

    companion object {
        private const val UPDATE_DYNAMIC_DATA_PASSING_QUERY = "UpdateDynamicDataPassingQuery"

        const val query = """
            mutation UpdateDynamicDataPassingQuery(${'$'}param: String) {
                  update_dynamic_data_passing(param: ${'$'}param) {
                    status
                    error_message
                    data {
                      dynamic_data
                    }
                  }
                }"""
    }
}
