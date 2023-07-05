package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcConstant.ATC_ERROR_GLOBAL
import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartExternalDataMapper
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.atc_common.domain.usecase.query.ADD_TO_CART_EXTERNAL_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject
import kotlin.math.roundToLong

class AddToCartExternalUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val addToCartDataMapper: AddToCartExternalDataMapper,
    private val analytics: AddToCartExternalAnalytics,
    private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Pair<String, String>, AddToCartExternalModel>(dispatcher.io) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"

        const val QUERY_ADD_TO_CART_EXTERNAL = "AddToCartExternalQuery"
    }

    override fun graphqlQuery(): String = ADD_TO_CART_EXTERNAL_QUERY

    @GqlQuery(QUERY_ADD_TO_CART_EXTERNAL, ADD_TO_CART_EXTERNAL_QUERY)
    override suspend fun execute(params: Pair<String, String>): AddToCartExternalModel {
        val productId = params.first
        val param = mapOf(
            PARAM_PRODUCT_ID to productId,
            KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
        )
        val request = GraphqlRequest(
            AddToCartExternalQuery(),
            AddToCartExternalGqlResponse::class.java,
            param
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<AddToCartExternalGqlResponse>()
        if (response.response.status.equals("OK", true)) {
            val result = addToCartDataMapper.map(response)
            if (result.success == 1) {
                val data = result.data
                analytics.sendEnhancedEcommerceTracking(data)
                AddToCartBaseAnalytics.sendAppsFlyerTracking(
                    data.productId,
                    data.productName,
                    data.price.roundToLong().toString(),
                    data.quantity.toString(),
                    data.category
                )
                AddToCartBaseAnalytics.sendBranchIoTracking(
                    data.productId, data.productName, data.price.roundToLong().toString(),
                    data.quantity.toString(), data.category, "",
                    "", "", "",
                    "", "", params.second
                )
                return result
            } else {
                val message = response.response.data.message.firstOrNull() ?: ATC_ERROR_GLOBAL
                throw MessageErrorException(message)
            }
        } else {
            throw MessageErrorException(ATC_ERROR_GLOBAL)
        }
    }
}
