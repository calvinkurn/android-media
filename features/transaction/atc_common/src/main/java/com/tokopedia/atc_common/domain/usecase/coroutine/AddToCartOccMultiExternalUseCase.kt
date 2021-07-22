package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartOccMultiExternalGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.analytics.AddToCartOccExternalAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartExternalDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.query.QUERY_ADD_TO_CART_OCC_EXTERNAL_MULTI
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddToCartOccMultiExternalUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                           private val addToCartExternalDataMapper: AddToCartExternalDataMapper,
                                                           private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) : UseCase<AddToCartOccMultiDataModel>() {

    private var productIds: List<String>? = null
    private var userId: String? = null

    fun setParams(productIds: List<String>, userId: String): AddToCartOccMultiExternalUseCase {
        this.productIds = productIds
        this.userId = userId
        return this
    }

    /**
     * Execute ATC OCC with previously set params
     *
     * @return AddToCartDataModel when BE responds, however client need to check whether ATC is success
     * @see AddToCartOccMultiDataModel.isStatusError
     *
     * @throws RuntimeException("Parameters has not been initialized!") when params is null
     * @see AddToCartOccMultiExternalUseCase.setParams
     */
    override suspend fun executeOnBackground(): AddToCartOccMultiDataModel {
        val sentParams = productIds ?: throw RuntimeException(AtcConstant.ERROR_PARAMETER_NOT_INITIALIZED)
        val userId = userId ?: throw RuntimeException(AtcConstant.ERROR_PARAMETER_NOT_INITIALIZED)
        if (sentParams.isEmpty()) {
            throw RuntimeException(AtcConstant.ERROR_PARAMETER_NOT_INITIALIZED)
        }

        val graphqlRequest = GraphqlRequest(QUERY_ADD_TO_CART_OCC_EXTERNAL_MULTI, AddToCartOccMultiExternalGqlResponse::class.java, getParams(sentParams))
        val addToCartOccExternalGqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest)).getSuccessData<AddToCartOccMultiExternalGqlResponse>()

        val result = addToCartExternalDataMapper.map(addToCartOccExternalGqlResponse)
        if (!result.isStatusError()) {
            addToCartOccExternalGqlResponse.response.data.data.forEach {
                AddToCartOccExternalAnalytics.sendEETracking(it)
                AddToCartBaseAnalytics.sendAppsFlyerTracking(it.productId, it.productName, it.price.toString(),
                        it.quantity.toString(), it.category)
                AddToCartBaseAnalytics.sendBranchIoTracking(it.productId, it.productName, it.price.toString(),
                        it.quantity.toString(), it.category, "",
                        "", "", "",
                        "", "", userId)
            }
        }
        return result
    }

    private fun getParams(productIds: List<String>): Map<String, Any> {
        return mapOf(
                PARAM to mapOf(
                        PARAM_PRODUCT_IDS to productIds,
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
                )
        )
    }

    companion object {
        private const val PARAM = "param"
        private const val PARAM_PRODUCT_IDS = "product_ids"
    }
}