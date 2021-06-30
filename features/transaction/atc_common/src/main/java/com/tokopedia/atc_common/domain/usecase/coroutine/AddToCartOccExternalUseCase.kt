package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.analytics.AddToCartOccExternalAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.query.QUERY_ADD_TO_CART_OCC
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddToCartOccExternalUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val addToCartDataMapper: AddToCartDataMapper,
                                                      private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) : UseCase<AddToCartDataModel>() {

    private var productId: String? = null
    private var userId: String? = null

    fun setParams(productId: String, userId: String): AddToCartOccExternalUseCase {
        this.productId = productId
        this.userId = userId
        return this
    }

    /**
     * Execute ATC OCC with previously set params
     *
     * @return AddToCartDataModel when BE responds, however client need to check whether ATC is success
     * @see AddToCartDataModel.isStatusError
     *
     * @throws RuntimeException("Parameters has not been initialized!") when params is null
     * @see AddToCartOccExternalUseCase.setParams
     */
    override suspend fun executeOnBackground(): AddToCartDataModel {
        val sentParams = productId ?: throw RuntimeException("Parameters has not been initialized!")
        val userId = userId ?: throw RuntimeException("Parameters has not been initialized!")

        val graphqlRequest = GraphqlRequest(QUERY_ADD_TO_CART_OCC, AddToCartOccGqlResponse::class.java, getParams(sentParams))
        val addToCartOccGqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest)).getSuccessData<AddToCartOccGqlResponse>()

        val result = addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
        if (!result.isStatusError()) {
            addToCartOccGqlResponse.addToCartOccResponse.data.detail.also {
                AddToCartOccExternalAnalytics.sendEETracking(it)
                AddToCartBaseAnalytics.sendAppsFlyerTracking(it.productId.toString(), it.productName, it.price.toString(),
                        it.quantity.toString(), it.category)
                AddToCartBaseAnalytics.sendBranchIoTracking(it.productId.toString(), it.productName, it.price.toString(),
                        it.quantity.toString(), it.category, "",
                        "", "", "",
                        "", "", userId)
            }
        }
        return result
    }

    private fun getParams(productId: String): Map<String, Any> {
        return mapOf(
                PARAM to mapOf(
                        PARAM_PRODUCT_ID to productId,
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
                )
        )
    }

    companion object {
        private const val PARAM = "param"
        private const val PARAM_PRODUCT_ID = "product_id"
    }
}