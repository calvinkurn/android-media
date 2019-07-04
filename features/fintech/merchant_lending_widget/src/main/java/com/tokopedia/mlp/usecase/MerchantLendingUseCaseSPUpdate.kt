package com.tokopedia.mlp.usecase

import android.content.Context
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mlp.modelToggle.Data
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class MerchantLendingUseCaseSPUpdate @Inject constructor(private val context: Context,
                                                 private val graphqlUseCase: GraphqlUseCase
) : UseCase<Data?>() {


    fun createRequestParams(enable: Boolean): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putBoolean("enable",enable)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<Data?>? {

        val graphqlRequestSPUpdate= GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.merchant_lending_query_spupdate), Data::class.java,requestParams?.parameters,false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequestSPUpdate)
        return graphqlUseCase.createObservable(requestParams).map { it.getData(Data::class.java) as Data }

    }

}




