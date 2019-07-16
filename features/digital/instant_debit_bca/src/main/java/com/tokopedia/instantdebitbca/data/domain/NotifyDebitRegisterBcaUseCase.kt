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
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

/**
 * Created by nabillasabbaha on 25/03/19.
 */
open class NotifyDebitRegisterBcaUseCase @Inject
constructor(@param:ApplicationContext var context: Context, var graphqlUseCase: GraphqlUseCase) : UseCase<NotifyDebitRegisterBca>() {

    override fun createObservable(requestParams: RequestParams): Observable<NotifyDebitRegisterBca> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> { requestParams ->
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.idb_notify_debit_register_mutation)
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
                    notifyDebitRegisterBca.callbackUrl = it.notifyDebitRegister!!.debitRegister!!.callbackUrl
                    notifyDebitRegisterBca
                }
    }

    open fun createRequestParam(debitDataString: String, deviceId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(MERCHANT_CODE, MERCHANT_DATA)
        requestParams.putString(DEVICE_ID, deviceId)
        requestParams.putString(BANK_CODE, BANK_DATA)
        requestParams.putString(CALLBACK_URL, CALLBACK_DATA)
        requestParams.putString(SIGNATURE, "")
        requestParams.putObject(DEBIT_DATA, debitDataString)
        return requestParams
    }

    companion object {

        val XCOID = "xcoID"
        val CREDENTIAL_TYPE = "credentialType"
        val CREDENTIAL_NO = "credentialNo"
        val MAX_LIMIT = "maxLimit"
        val USER_AGENT = "user_agent"
        val IP_ADDRESS = "ip_address"
        val ACTION = "action"
        val UPDATE = "update"

        val MERCHANT_CODE = "merchantCode"
        val DEVICE_ID = "deviceID"
        val BANK_CODE = "bankCode"
        val CALLBACK_URL = "callbackUrl"
        val SIGNATURE = "signature"
        val DEBIT_DATA = "debitData"
        val MERCHANT_DATA = AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_CODE
        val BANK_DATA = AuthUtil.KEY.INSTANT_DEBIT_BCA_BANK_CODE
        val CALLBACK_DATA = "https://tokopedia.com"
    }
}
