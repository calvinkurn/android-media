package com.tokopedia.transactiondata.insurance.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.R
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceData
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceDataCart
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductDetailRequest
import com.tokopedia.transactiondata.insurance.entity.response.UpdateInsuranceDataGqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class UpdateInsuranceProductDataUsecase @Inject constructor(@ApplicationContext val context: Context) {
    var graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    var requestParams = RequestParams.create()

    fun setRequestParams(updateInsuranceDataList: ArrayList<UpdateInsuranceData>, pageType: String,
                         clientVersion: String, cartIdsList: ArrayList<UpdateInsuranceDataCart>) {

        val updateInsuranceProductRequest = UpdateInsuranceProductDetailRequest(pageType, clientVersion, "android", updateInsuranceDataList)
        requestParams.putObject("data", updateInsuranceProductRequest)
        requestParams.putObject("items", cartIdsList)
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val graphqlRequestForUsable = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.mutation_update_insurance_product_data),
                UpdateInsuranceDataGqlResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}