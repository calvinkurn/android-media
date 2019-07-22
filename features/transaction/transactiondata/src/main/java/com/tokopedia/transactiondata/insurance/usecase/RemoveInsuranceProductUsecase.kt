package com.tokopedia.transactiondata.insurance.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.R
import com.tokopedia.transactiondata.insurance.entity.request.RemoveInsuranceData
import com.tokopedia.transactiondata.insurance.entity.request.RemoveInsuranceProductRequest
import com.tokopedia.transactiondata.insurance.entity.response.RemoveInsuranceProductGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class RemoveInsuranceProductUsecase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    var requestParams = RequestParams.create()

    fun setRequestParams(removeInsuranceDataList: ArrayList<RemoveInsuranceData>, pageType: String,
                         clientVersion: String, cartIdsList: ArrayList<String>) {
        val removeInsuranceProductRequest = RemoveInsuranceProductRequest(pageType,
                clientVersion,
                "android", removeInsuranceDataList)

        requestParams.putObject("data", removeInsuranceProductRequest)
        requestParams.putObject("cartIds", cartIdsList)
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