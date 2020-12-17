package com.tokopedia.common_wallet.pendingcashback.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_wallet.R
import com.tokopedia.common_wallet.pendingcashback.data.PendingCashbackEntity
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 2/7/18.
 */

class GetPendingCasbackUseCase @Inject constructor(@param:ApplicationContext private val context: Context,
                                                   private val graphqlUseCase: GraphqlUseCase)
    : UseCase<PendingCashback>() {

    override fun createObservable(requestParams: RequestParams): Observable<PendingCashback> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> {
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.wallet_pending_cashback_query)
                    val variables = HashMap<String, Any>()

                    if (!TextUtils.isEmpty(query)) {
                        val request = GraphqlRequest(query, ResponsePendingCashback::class.java, variables, false)
                        graphqlUseCase!!.clearRequest()
                        graphqlUseCase.addRequest(request)
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    Observable.error<GraphqlResponse>(Exception("Query and/or variable are empty."))
                })
                .map(Func1<GraphqlResponse, ResponsePendingCashback> {
                    it.getData(ResponsePendingCashback::class.java)
                }).map(Func1<ResponsePendingCashback, PendingCashback> {
                    return@Func1 mapper(it.pendingCashbackEntity)
                })
    }

    private fun mapper(pendingCashbackEntity: PendingCashbackEntity?): PendingCashback {
        pendingCashbackEntity?.let {
            var amount = 0
            try {
                amount = Integer.parseInt(pendingCashbackEntity.balance)
            } catch (ignored: NumberFormatException) {
            }

            val pendingCashback = PendingCashback()
            pendingCashback.amount = amount
            pendingCashback.amountText = pendingCashbackEntity.balanceText
            return pendingCashback
        }
        throw RuntimeException("Get Pending Cashback Failed")
    }

    override fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}
