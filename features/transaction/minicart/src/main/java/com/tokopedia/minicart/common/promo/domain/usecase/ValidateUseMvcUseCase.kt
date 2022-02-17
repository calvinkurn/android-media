package com.tokopedia.minicart.common.promo.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.promo.data.request.ValidateUseMvcParam
import com.tokopedia.minicart.common.promo.data.response.ValidateUseMvcGqlResponse
import com.tokopedia.minicart.common.promo.domain.data.ValidateUseMvcData
import com.tokopedia.minicart.common.promo.domain.mapper.ValidateUseMvcMapper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ValidateUseMvcUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                private val validateUseMvcMapper: ValidateUseMvcMapper,
                                                private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<ValidateUseMvcData>() {

    private var param: ValidateUseMvcParam? = null

    fun setParam(param: ValidateUseMvcParam): ValidateUseMvcUseCase {
        this.param = param
        return this
    }

    private fun getParams(validateUseMvcParam: ValidateUseMvcParam): Map<String, Any?> {
        return mapOf(
            PARAM_PARAMS to mapOf(
                PARAM_PROMO to validateUseMvcParam
            ),
            ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )
    }

    @GqlQuery(QUERY_NAME, QUERY)
    override suspend fun executeOnBackground(): ValidateUseMvcData {
        val requestParam = this.param?.copy() ?: throw RuntimeException("param must be initialized")
        val request = GraphqlRequest(ValidateUseMvcQuery(), ValidateUseMvcGqlResponse::class.java, getParams(requestParam))
        val response = validateUseMvcMapper.mapValidateUseMvcResponse(graphqlRepository.response(listOf(request)).getSuccessData<ValidateUseMvcGqlResponse>().response)
        if (response.isError) {
            throw MessageErrorException(response.errorMessage.firstOrNull() ?: "")
        }
        return response
    }

    companion object {
        private const val PARAM_PARAMS = "params"
        private const val PARAM_PROMO = "promo"
        private const val QUERY_NAME = "ValidateUseMvcQuery"

        private const val QUERY = """
            mutation validateUseMvc(${"$"}params: PromoStackRequest, ${"$"}chosen_address: ChosenAddressParam) {
                validate_use_mvc(params: ${"$"}params, chosen_address: ${"$"}chosen_address) {
                    status
                    message
                    error_code
                    data {
                        curr_purchase
                        min_purchase
                        progress_percentage
                        message
                    }
                }
            }
        """
    }
}