package com.tokopedia.contactus.inboxdetail.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.contactus.test.R
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.usecase.RequestParams
import rx.Observable
import timber.log.Timber

class FakeGraphqlUseCaseInboxDetail(private val context: Context) : GraphqlUseCaseInterface {

    private var gqlRequest: GraphqlRequest? = null
    private var e: Exception? = null

    override fun clearRequest() {
        gqlRequest = null
    }

    override fun addRequest(requestObject: GraphqlRequest?) {
        gqlRequest = requestObject
    }

    override fun getExecuteObservable(requestParam: RequestParams?): Observable<GraphqlResponse> {
        Timber.d("executing fake usecase")
        e?.let {
            return Observable.error(e)
        }
        if (gqlRequest == null) throw Exception("gql request is null")

        return Observable.error(Throwable("unrecognized query"))
    }

    override fun unsubscribe() {
        // no op
    }

    fun returnException(exception: Exception) {
        e = exception
    }
}
