package com.tokopedia.discovery.categoryrevamp.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.topAds.TopAdsResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class TopAdsProductsUseCase @Inject constructor(private val context: Context) : UseCase<TopAdsResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<TopAdsResponse> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_nav_top_ads), TopAdsResponse::class.java,requestParams?.parameters,false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(TopAdsResponse::class.java) as TopAdsResponse)
        }

    }
}