package com.tokopedia.atc_common.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_OCC_EXTERNAL
import com.tokopedia.atc_common.data.model.response.AddToCartOccExternalGqlResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.tracking.AddToCartOccExternalTracking
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartOccExternalUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                      @Named(MUTATION_ATC_OCC_EXTERNAL) private val query: String,
                                                      private val graphqlUseCase: GraphqlUseCase,
                                                      private val addToCartDataMapper: AddToCartDataMapper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_PRODUCT_ID = "REQUEST_PARAM_KEY_PRODUCT_ID"

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
                AddToCartOccExternalTracking.sendEETracking(addToCartOccGqlResponse.addToCartOccResponse.data.detail)
                AddToCartOccExternalTracking.sendAppsFlyerTracking(addToCartOccGqlResponse.addToCartOccResponse.data.detail)
                AddToCartOccExternalTracking.sendBranchIoTracking(context, addToCartOccGqlResponse.addToCartOccResponse.data.detail)
            }
            result
        }

    }

    private fun getParams(productId: String): Map<String, Any> {
        return mapOf(PARAM to mapOf(PARAM_PRODUCT_ID to productId))
    }
}