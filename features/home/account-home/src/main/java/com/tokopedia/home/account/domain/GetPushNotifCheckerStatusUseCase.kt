package com.tokopedia.home.account.domain

import android.content.Context
import androidx.annotation.MainThread
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.PushNotifCheckerResponse
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetPushNotifCheckerStatusUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase
): UseCase<PushNotifCheckerResponse>() {

    override fun createObservable(requestParams: RequestParams): Observable<PushNotifCheckerResponse?> {
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_push_notif_checker
        )

        val graphqlRequest = GraphqlRequest(query, PushNotifCheckerResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map { graphqlResponse ->
                    val data: PushNotifCheckerResponse? = graphqlResponse.getData((PushNotifCheckerResponse::class.java))
                    if (data == null) {
                        throw RuntimeException()
                    }
                    data
                }
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}