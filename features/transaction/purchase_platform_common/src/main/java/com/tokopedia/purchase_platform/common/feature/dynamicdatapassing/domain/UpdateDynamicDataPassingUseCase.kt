package com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.model.UpdateDynamicDataPassingUiModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.response.UpdateDynamicDataResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateDynamicDataPassingUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<UpdateDynamicDataPassingUiModel>() {

    private var params: Map<String, Any?>? = null
    private var isFireAndForget: Boolean = false
    private var response: UpdateDynamicDataResponse? = null

    @GqlQuery(UPDATE_DYNAMIC_DATA_FIRE_AND_FORGET_MUTATION, queryFireAndForget)
    fun setParams(params: DynamicDataPassingParamRequest, isFireAndForget: Boolean) {
        this.params = mapOf("request" to params)
        this.isFireAndForget = isFireAndForget
    }

    @GqlQuery(UPDATE_DYNAMIC_DATA_PASSING_MUTATION, query)
    override suspend fun executeOnBackground(): UpdateDynamicDataPassingUiModel {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val requestFireAndForget = GraphqlRequest(
            UpdateDynamicDataFireAndForgetMutation(),
            UpdateDynamicDataResponse::class.java,
            params
        )

        val request = GraphqlRequest(
            UpdateDynamicDataMutation(),
            UpdateDynamicDataResponse::class.java,
            params
        )

        response = if (isFireAndForget) {
            graphqlRepository.response(listOf(requestFireAndForget))
                .getSuccessData<UpdateDynamicDataResponse>()
        } else {
            graphqlRepository.response(listOf(request))
                .getSuccessData<UpdateDynamicDataResponse>()
        }

        if (response?.updateDynamicData?.status == "OK" && response?.updateDynamicData?.data?.dynamicData?.isNotEmpty() == true) {
            return UpdateDynamicDataPassingUiModel(response?.updateDynamicData?.data?.dynamicData ?: "")
        } else {
            if (response?.updateDynamicData?.errorMessages?.isNotEmpty() == true) {
                throw CartResponseErrorException(response?.updateDynamicData?.errorMessages?.joinToString())
            } else {
                throw CartResponseErrorException(CartConstant.CART_ERROR_GLOBAL)
            }
        }
    }

    companion object {
        private const val UPDATE_DYNAMIC_DATA_FIRE_AND_FORGET_MUTATION = "UpdateDynamicDataFireAndForgetMutation"
        private const val UPDATE_DYNAMIC_DATA_PASSING_MUTATION = "UpdateDynamicDataMutation"

        /*note : please update both of 2 mutations below if change is needed
        2 mutations below are same, but need to differentiate the mutation name
        between fire & forget, and the regular one*/

        const val queryFireAndForget = """
            mutation UpdateDynamicDataFireAndForgetMutation(${'$'}request: UpdateDynamicDataRequest) {
                  update_dynamic_data(request: ${'$'}request) {
                    status
                    error_message
                    data {
                      dynamic_data
                    }
                  }
                }"""

        const val query = """
            mutation UpdateDynamicDataMutation(${'$'}request: UpdateDynamicDataRequest) {
                  update_dynamic_data(request: ${'$'}request) {
                    status
                    error_message
                    data {
                      dynamic_data
                    }
                  }
                }"""
    }
}
