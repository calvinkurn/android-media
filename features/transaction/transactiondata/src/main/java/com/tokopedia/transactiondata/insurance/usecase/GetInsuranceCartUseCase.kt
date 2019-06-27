package com.tokopedia.transactiondata.insurance.usecase

import android.content.Context
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.R
import com.tokopedia.transactiondata.insurance.entity.request.InsuranceCartRequest
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetInsuranceCartUseCase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val insuranceCartRequest = InsuranceCartRequest()
        insuranceCartRequest.clientVersion = Build.VERSION.SDK_INT.toString()

        val requestParams = RequestParams.create()
        requestParams.putObject("data", insuranceCartRequest)

        val graphqlRequestForUsable = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_insurance_cart),
                InsuranceCartGqlResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

}