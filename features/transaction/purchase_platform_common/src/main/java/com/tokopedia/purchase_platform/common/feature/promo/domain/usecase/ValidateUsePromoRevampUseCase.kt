package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ValidateUsePromoRevampUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<ValidateUsePromoRevampUiModel>() {

    private var paramValidateUse: ValidateUsePromoRequest? = null

    companion object {
        private const val PARAM_PROMO = "promo"
        private const val PARAM_PARAMS = "params"

        private const val QUERY_VALIDATE_USE = "ValidateUseQuery"
    }

    fun setParam(param: ValidateUsePromoRequest): ValidateUsePromoRevampUseCase {
        paramValidateUse = param
        return this
    }

    private fun getParams(validateUsePromoRequest: ValidateUsePromoRequest): Map<String, Any?> {
        return mapOf(
            PARAM_PARAMS to mapOf(
                PARAM_PROMO to validateUsePromoRequest
            ),
            KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )
    }

    @GqlQuery(QUERY_VALIDATE_USE, VALIDATE_USE_QUERY)
    override suspend fun executeOnBackground(): ValidateUsePromoRevampUiModel {
        val param = paramValidateUse?.copy() ?: throw RuntimeException("Param has not been initialized")
        val request = GraphqlRequest(ValidateUseQuery(), ValidateUseResponse::class.java, getParams(param))
        val validateUseGqlResponse = graphqlRepository.response(listOf(request)).getSuccessData<ValidateUseResponse>()
        return ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseGqlResponse.validateUsePromoRevamp)
    }
}
