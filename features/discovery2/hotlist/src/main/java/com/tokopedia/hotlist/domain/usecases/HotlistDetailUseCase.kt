package com.tokopedia.hotlist.domain.usecases

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotlist.data.hotListDetail.HotListDetailResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.hotlist.R
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class HotlistDetailUseCase @Inject constructor(private val context: Context) : UseCase<HotListDetailResponse>() {
    override fun createObservable(requestParams: RequestParams?): Observable<HotListDetailResponse> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_hotlist_detail), HotListDetailResponse::class.java, requestParams?.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(HotListDetailResponse::class.java) as HotListDetailResponse)
        }
    }
}