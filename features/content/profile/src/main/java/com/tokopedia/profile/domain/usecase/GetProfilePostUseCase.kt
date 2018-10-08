package com.tokopedia.profile.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfilePostUseCase @Inject constructor(@ApplicationContext val context: Context)
    : GraphqlUseCase() {
    override fun createObservable(requestParams: RequestParams?): Observable<GraphqlResponse> {
        this.clearRequest()
        return super.createObservable(requestParams)
    }
}