package com.tokopedia.instantdebitbca.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.instantdebitbca.R
import com.tokopedia.instantdebitbca.data.ResponseAccessTokenBca
import com.tokopedia.instantdebitbca.view.TokenInstantDebitBca
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 21/03/19.
 */
class GetAccessTokenUseCase @Inject constructor(@ApplicationContext val context: Context,
                                                val graphqlUseCase: GraphqlUseCase)
    : UseCase<@JvmSuppressWildcards TokenInstantDebitBca>() {

    override fun createObservable(requestParams: RequestParams): Observable<TokenInstantDebitBca> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> {
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_access_token_instant_debit_bca)
                    if (!TextUtils.isEmpty(query)) {
                        graphqlUseCase.clearRequest()
                        graphqlUseCase.addRequest(GraphqlRequest(query, ResponseAccessTokenBca::class.java))
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    return@Func1 Observable.error(Exception("Query variable are empty"))
                })
                .map(Func1<GraphqlResponse, ResponseAccessTokenBca> { it.getData(ResponseAccessTokenBca::class.java) })
                .map { TokenInstantDebitBca(it.tokenBca.accessToken, it.tokenBca.tokenType) }
    }
}
