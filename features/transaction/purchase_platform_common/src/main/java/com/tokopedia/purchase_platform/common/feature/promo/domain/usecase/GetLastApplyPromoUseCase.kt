package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import javax.inject.Inject

class GetLastApplyPromoUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ValidateUsePromoRequest, ValidateUsePromoRevampUiModel>(dispatcher.io) {

    companion object {
        private const val PARAM_PROMO = "promo"
        private const val PARAM_PARAMS = "params"

        private const val QUERY_GET_LAST_APPLY = "GetLastApplyQuery"
    }

    private fun getParams(validateUsePromoRequest: ValidateUsePromoRequest): Map<String, Any?> {
        return mapOf(
            PARAM_PARAMS to mapOf(
                PARAM_PROMO to validateUsePromoRequest
            ),
            ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )
    }

    override fun graphqlQuery(): String = GET_LAST_APPLY_QUERY

    @GqlQuery(QUERY_GET_LAST_APPLY, GET_LAST_APPLY_QUERY)
    override suspend fun execute(params: ValidateUsePromoRequest): ValidateUsePromoRevampUiModel {
        val param = params.copy()

        val request = GraphqlRequest(GetLastApplyQuery(), ValidateUseResponse::class.java, getParams(param))
        val validateUseGqlResponse = graphqlRepository.response(listOf(request)).getSuccessData<ValidateUseResponse>()

        return ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseGqlResponse.validateUsePromoRevamp)
    }
}
