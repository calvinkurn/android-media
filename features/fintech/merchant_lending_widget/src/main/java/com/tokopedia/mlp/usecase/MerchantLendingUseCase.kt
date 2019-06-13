package com.tokopedia.mlp.usecase

import android.content.Context
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mlp.model.LeWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class MerchandLendingUsecase @Inject constructor(private val context: Context,
                                                 private val graphqlUseCase: GraphqlUseCase/*,
                                                 private val rawQueries: Map<String, String>*/)
    : UseCase<LeWidget>() {


    fun createRequestParams(merchantid: Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt("merchantid", merchantid)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<LeWidget> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.merchant_lending_query)/*rawQueries[BrowseRawQueryKeyConstant.CATEGORY_LIST]*/, LeWidget::class.java, requestParams!!.parameters, false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map { it.getData(LeWidget::class.java) as LeWidget }

    }

}



