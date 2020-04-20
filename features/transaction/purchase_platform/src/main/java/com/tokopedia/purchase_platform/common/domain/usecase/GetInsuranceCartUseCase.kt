package com.tokopedia.purchase_platform.common.domain.usecase

import android.content.Context
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.feature.insurance.request.InsuranceCartRequest
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetInsuranceCartUseCase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    val DATA_REQUEST_PARAM = "data"

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val insuranceCartRequest = InsuranceCartRequest()
        insuranceCartRequest.clientVersion = Build.VERSION.SDK_INT.toString()

        val requestParams = RequestParams.create()
        requestParams.putObject(DATA_REQUEST_PARAM, insuranceCartRequest)

        val graphqlRequestForUsable = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_insurance_cart),
                InsuranceCartGqlResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}