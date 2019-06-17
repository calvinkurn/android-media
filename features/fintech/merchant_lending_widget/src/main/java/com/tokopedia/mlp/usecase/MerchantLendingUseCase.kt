package com.tokopedia.mlp.usecase

import android.content.Context
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mlp.contractModel.LeWidget
import com.tokopedia.mlp.contractModel.LeWidgetData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class MerchantLendingUseCase @Inject constructor(private val context: Context,
                                                 private val graphqlUseCase: GraphqlUseCase/*,
                                                 private val rawQueries: Map<String, String>*/)
    : UseCase<LeWidgetData>() {


/*

    fun createRequestParams(merchantID: Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt("merchantID", merchantID)
        return requestParams
    }

*/


    override fun createObservable(requestParams: RequestParams?): Observable<LeWidgetData> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.merchant_lending_query_contract)/*rawQueries[BrowseRawQueryKeyConstant.CATEGORY_LIST]*/, LeWidgetData::class.java, false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map { it.getData(LeWidgetData::class.java) as LeWidgetData }

    }

}



