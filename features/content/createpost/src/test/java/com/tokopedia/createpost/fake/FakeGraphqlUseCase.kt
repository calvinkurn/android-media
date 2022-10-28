package com.tokopedia.createpost.fake

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import rx.Subscriber

/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
class FakeGraphqlUseCase : GraphqlUseCase() {

    init {
    }

    private var mResponse: GraphqlResponse? = null
    private var mError: Throwable? = null

    fun setSuccessResponse(response: GraphqlResponse): FakeGraphqlUseCase {
        mResponse = response
        mError = null

        return this
    }

    fun setErrorResponse(e: Throwable): FakeGraphqlUseCase {
        mResponse = null
        mError = e

        return this
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        if(mResponse != null) {
            Observable.just(mResponse).subscribe(subscriber)
        }
        else {
            Observable.error<GraphqlResponse>(mError).subscribe(subscriber)
        }
    }
}