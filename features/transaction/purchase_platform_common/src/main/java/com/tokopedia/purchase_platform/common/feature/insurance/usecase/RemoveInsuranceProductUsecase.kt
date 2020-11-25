package com.tokopedia.purchase_platform.common.feature.insurance.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.feature.insurance.request.RemoveInsuranceData
import com.tokopedia.purchase_platform.common.feature.insurance.request.RemoveInsuranceProductRequest
import com.tokopedia.purchase_platform.common.feature.insurance.response.RemoveInsuranceProductGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class RemoveInsuranceProductUsecase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    var requestParams = RequestParams.create()

    val DATA_REQUEST_PARAM = "data"
    val CART_ID_REQUEST_PARAM = "cartIds"
    val CLIENT_REQUEST_PARAM = "android"

    fun setRequestParams(removeInsuranceDataList: ArrayList<RemoveInsuranceData>, pageType: String,
                         clientVersion: String, cartIdsList: ArrayList<String>) {
        val removeInsuranceProductRequest = RemoveInsuranceProductRequest(pageType,
                clientVersion,
                CLIENT_REQUEST_PARAM, removeInsuranceDataList)

        requestParams.putObject(DATA_REQUEST_PARAM, removeInsuranceProductRequest)
        requestParams.putObject(CART_ID_REQUEST_PARAM, cartIdsList)
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val request = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.mutation_remove_insurance_product),
                RemoveInsuranceProductGqlResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.addRequest(request)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}