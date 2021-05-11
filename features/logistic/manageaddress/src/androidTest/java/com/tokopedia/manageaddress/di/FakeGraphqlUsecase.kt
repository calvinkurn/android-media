package com.tokopedia.manageaddress.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.logisticCommon.domain.response.GetPeopleAddressResponse
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.manageaddress.test.R
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import rx.Observable
import timber.log.Timber

class FakeGraphqlUsecase(private val context: Context) : GraphqlUseCaseInterface {
    override fun clearRequest() {
        // no op
    }

    override fun addRequest(requestObject: GraphqlRequest?) {
        // no op
    }

    override fun getExecuteObservable(requestParam: RequestParams?): Observable<GraphqlResponse> {
        Timber.d("executing fake usecase")
        return Observable.just(GraphqlResponse(
                mapOf(GetPeopleAddressResponse::class.java to
                        Gson().fromJson(getRawString(context, R.raw.address), GetPeopleAddressResponse::class.java)
                ), mapOf(), false))
    }

    override fun unsubscribe() {
        // no op
    }
}