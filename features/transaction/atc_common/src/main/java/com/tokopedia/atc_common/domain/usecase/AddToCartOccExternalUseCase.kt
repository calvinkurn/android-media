package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_OCC_EXTERNAL
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper.Companion.PARAM_KEY_CHOSEN_ADDRESS
import com.tokopedia.atc_common.data.model.response.AddToCartOccExternalGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.analytics.AddToCartOccExternalAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartOccExternalUseCase @Inject constructor(@Named(MUTATION_ATC_OCC_EXTERNAL) private val query: String,
                                                      private val graphqlUseCase: GraphqlUseCase,
                                                      private val addToCartDataMapper: AddToCartDataMapper,
                                                      private val chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_PRODUCT_ID = "REQUEST_PARAM_KEY_PRODUCT_ID"
        const val REQUEST_PARAM_KEY_USER_ID = "REQUEST_PARAM_KEY_USER_ID"

        private const val PARAM = "param"
        private const val PARAM_PRODUCT_ID = "product_id"
    }

    override fun createObservable(requestParams: RequestParams): Observable<AddToCartDataModel> {
        val productId = requestParams.getString(REQUEST_PARAM_KEY_PRODUCT_ID, "")
        val graphqlRequest = GraphqlRequest(query, AddToCartOccExternalGqlResponse::class.java, getParams(productId))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOccGqlResponse = it.getData<AddToCartOccExternalGqlResponse>(AddToCartOccExternalGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
            if (addToCartOccGqlResponse.addToCartOccResponse.data.success == 1) {
                val detail = addToCartOccGqlResponse.addToCartOccResponse.data.detail
                AddToCartOccExternalAnalytics.sendEETracking(detail)
                AddToCartBaseAnalytics.sendAppsFlyerTracking(detail.productId.toString(), detail.productName, detail.price.toString(),
                        detail.quantity.toString(), detail.category)
                AddToCartBaseAnalytics.sendBranchIoTracking(detail.productId.toString(), detail.productName, detail.price.toString(),
                        detail.quantity.toString(), detail.category, "",
                        "", "", "",
                        "", "", requestParams.getString(REQUEST_PARAM_KEY_USER_ID, ""))
            }
            result
        }

    }

    private fun getParams(productId: String): Map<String, Any> {
        return mapOf(
                PARAM to mapOf(
                        PARAM_PRODUCT_ID to productId,
                        PARAM_KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
                )
        )
    }
}