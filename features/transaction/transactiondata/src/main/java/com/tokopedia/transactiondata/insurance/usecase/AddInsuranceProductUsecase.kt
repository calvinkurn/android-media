package com.tokopedia.transactiondata.insurance.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.R
import com.tokopedia.transactiondata.insurance.entity.request.AddInsuranceProductToCartRequest
import com.tokopedia.transactiondata.insurance.entity.request.AddMarketPlaceToCartRequest
import com.tokopedia.transactiondata.insurance.entity.response.AddInsuranceProductToCartGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class AddInsuranceProductUsecase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    var requestParams = RequestParams.create()
    var PARAM_ADD_TO_INSURANCE_CART_TRANSACTIONAL: String = "transactional"
    var PARAM_ADD_TO_INSURANCE_CART_MARKETPLACE: String = "marketplace"


    fun setRequestParams(request: AddInsuranceProductToCartRequest, marketPlaceRequest: AddMarketPlaceToCartRequest) {
        requestParams.putObject(PARAM_ADD_TO_INSURANCE_CART_TRANSACTIONAL, request)
        requestParams.putObject(PARAM_ADD_TO_INSURANCE_CART_MARKETPLACE, marketPlaceRequest)

    }


    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val graphqlRequestForUsable = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_insurance_product),
                AddInsuranceProductToCartGqlResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}