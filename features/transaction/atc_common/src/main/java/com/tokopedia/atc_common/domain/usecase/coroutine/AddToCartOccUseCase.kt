package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.query.QUERY_ADD_TO_CART_OCC
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddToCartOccUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              private val addToCartDataMapper: AddToCartDataMapper,
                                              private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) : UseCase<AddToCartDataModel>() {

    private var requestParams: AddToCartOccRequestParams? = null

    fun setParams(addToCartOccRequestParams: AddToCartOccRequestParams): AddToCartOccUseCase {
        requestParams = addToCartOccRequestParams
        return this
    }

    /**
     * Execute ATC OCC with previously set params
     *
     * @return AddToCartDataModel when BE responds, however client need to check whether ATC is success
     * @see AddToCartDataModel.isStatusError
     *
     * @throws RuntimeException("Parameters has not been initialized!") when params is null
     * @see AddToCartOccUseCase.setParams
     */
    override suspend fun executeOnBackground(): AddToCartDataModel {
        val sentParams = requestParams?.copy()
                ?: throw RuntimeException("Parameters has not been initialized!")

        val graphqlRequest = GraphqlRequest(QUERY_ADD_TO_CART_OCC, AddToCartOccGqlResponse::class.java, getParams(sentParams))
        val addToCartOccGqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest)).getSuccessData<AddToCartOccGqlResponse>()

        val result = addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
        if (!result.isStatusError()) {
            sentParams.run {
                AddToCartBaseAnalytics.sendAppsFlyerTracking(productId, productName, price,
                        quantity, category)
                AddToCartBaseAnalytics.sendBranchIoTracking(productId, productName, price,
                        quantity, category, categoryLevel1Id,
                        categoryLevel1Name, categoryLevel2Id, categoryLevel2Name,
                        categoryLevel3Id, categoryLevel3Name, userId)
            }
        }
        return result
    }

    private fun getParams(addToCartRequest: AddToCartOccRequestParams): Map<String, Any> {
        addToCartRequest.chosenAddressAddToCart = chosenAddressAddToCartRequestHelper.getChosenAddress()
        return mapOf(PARAM to addToCartRequest)
    }

    companion object {
        private const val PARAM = "param"
    }
}