package com.tokopedia.hotlist.domain.usecases


import com.tokopedia.hotlist.data.cpmAds.CpmItem
import com.tokopedia.hotlist.data.cpmAds.CpmTopAdsResponse
import com.tokopedia.hotlist.domain.mapper.CpmItemMapper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.hotlist.data.raw.GqlCpmTopAds
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CpmAdsUseCase @Inject constructor() : UseCase<List<CpmItem>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<CpmItem>> {
        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(
                GqlCpmTopAds.GQL_CPM_TOP_ADS, CpmTopAdsResponse::class.java, requestParams?.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            CpmItemMapper().transform((it.getData(CpmTopAdsResponse::class.java) as CpmTopAdsResponse))
        }
    }
}