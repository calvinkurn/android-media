package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartBundleGqlResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartBundleDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.usecase.query.MUTATION_ADD_TO_CART_BUNDLE
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddToCartBundleUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                 private val addToCartBundleDataMapper: AddToCartBundleDataMapper,
                                                 private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<AddToCartBundleDataModel>() {

    private var params: Map<String, Any?> = emptyMap()

    fun setParams(addToCartBundleRequestParam: AddToCartBundleRequestParams, source: String = "") {
        params = mapOf(
                KEY_PARAMS to addToCartBundleRequestParam,
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )
    }

    override suspend fun executeOnBackground(): AddToCartBundleDataModel {
        if (params.isEmpty()) {
            throw RuntimeException("Parameters has not been initialized!")
        }

        val request = GraphqlRequest(MUTATION_ADD_TO_CART_BUNDLE, AddToCartBundleGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<AddToCartBundleGqlResponse>()

        val result = addToCartBundleDataMapper.mapAddToCartBundleResponse(response.addToCartBundle)
        if (result.success) {
            return result.addToCartBundleDataModel
        } else {
            throw ResponseErrorException(result.errorMessage)
        }
    }

    companion object {
        const val KEY_PARAMS = "params"
    }
}
