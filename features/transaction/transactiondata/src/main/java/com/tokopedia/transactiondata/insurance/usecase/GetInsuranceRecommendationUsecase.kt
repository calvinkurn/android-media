package com.tokopedia.transactiondata.insurance.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.R
import com.tokopedia.transactiondata.insurance.entity.request.InsuranceRecommendationRequest
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceRecommendationGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetInsuranceRecommendationUsecase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    var requestParams = RequestParams.create()


    fun setRequestParams(insuranceRecommendationRequest: InsuranceRecommendationRequest) {
        requestParams.putObject("digitalProducts", insuranceRecommendationRequest)
    }


    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val graphqlRequestForUsable = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_insurance_recommendation),
                InsuranceRecommendationGqlResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}