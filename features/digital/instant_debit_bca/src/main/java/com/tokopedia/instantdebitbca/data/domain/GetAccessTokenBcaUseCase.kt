package com.tokopedia.instantdebitbca.data.domain

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.instantdebitbca.R
import com.tokopedia.instantdebitbca.data.data.ResponseAccessTokenBca
import com.tokopedia.instantdebitbca.data.view.model.TokenInstantDebitBca
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

/**
 * Created by nabillasabbaha on 25/03/19.
 */
class GetAccessTokenBcaUseCase @Inject
constructor(@param:ApplicationContext private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<TokenInstantDebitBca>() {

    override fun createObservable(requestParams: RequestParams): Observable<TokenInstantDebitBca> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> { requestParams ->
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.idb_access_token_mutation)
                    val variable = requestParams.parameters
                    if (!TextUtils.isEmpty(query)) {
                        graphqlUseCase.clearRequest()
                        graphqlUseCase.addRequest(GraphqlRequest(query, ResponseAccessTokenBca::class.java, variable))
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    Observable.error(Exception("Query variable are empty"))
                })
                .map { graphqlResponse -> graphqlResponse.getData<ResponseAccessTokenBca>(ResponseAccessTokenBca::class.java) }
                .map { it ->
                    val token = TokenInstantDebitBca()
                    it.merchantAuth?.dataToken?.tokenBca?.let {
                        token.accessToken = it.accessToken
                        token.tokenType = it.tokenType
                    }
                    token
                }
    }

    fun createRequestParam(): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(MERCHANT_CODE, AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_CODE)
        requestParams.putString(PROFILE_CODE, AuthUtil.KEY.INSTANT_DEBIT_BCA_PROFILE_CODE)
        return requestParams
    }

    companion object {

        val MERCHANT_CODE = "merchantCode"
        val PROFILE_CODE = "profileCode"
    }
}
