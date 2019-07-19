package com.tokopedia.instantdebitbca.data.domain

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.instantdebitbca.R
import com.tokopedia.instantdebitbca.data.data.ResponseDebitRegisterBca
import com.tokopedia.instantdebitbca.data.view.model.NotifyDebitRegisterBca
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class NotifyDebitRegisterEditLimit @Inject
constructor(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase) : NotifyDebitRegisterBcaUseCase(context, graphqlUseCase) {

    override fun createObservable(requestParams: RequestParams): Observable<NotifyDebitRegisterBca> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> { requestParams ->
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.idb_edit_limit_mutation)
                    val variable = requestParams.parameters
                    if (!TextUtils.isEmpty(query)) {
                        graphqlUseCase.clearRequest()
                        graphqlUseCase.addRequest(GraphqlRequest(query, ResponseDebitRegisterBca::class.java, variable))
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    Observable.error(Exception("Query variable are empty"))
                })
                .map { graphqlResponse -> graphqlResponse.getData<ResponseDebitRegisterBca>(ResponseDebitRegisterBca::class.java) }
                .map { it ->
                    val notifyDebitRegisterBca = NotifyDebitRegisterBca()
                    it.notifyDebitRegister?.debitRegister?.let {
                        notifyDebitRegisterBca.callbackUrl = it.callbackUrl
                    }
                    notifyDebitRegisterBca
                }
    }

    override fun createRequestParam(debitDataString: String, deviceId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(MERCHANT_CODE, MERCHANT_DATA)
        requestParams.putString(BANK_CODE, BANK_DATA)
        requestParams.putString(CALLBACK_URL, CALLBACK_DATA)
        requestParams.putString(SIGNATURE, "")
        requestParams.putString(ACTION, UPDATE)
        requestParams.putObject(DEBIT_DATA, debitDataString)
        return requestParams
    }
}
