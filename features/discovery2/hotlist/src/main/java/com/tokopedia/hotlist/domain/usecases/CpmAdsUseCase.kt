package com.tokopedia.hotlist.domain.usecases

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotlist.data.cpmAds.CpmItem
import com.tokopedia.hotlist.data.cpmAds.CpmTopAdsResponse
import com.tokopedia.hotlist.domain.mapper.CpmItemMapper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.hotlist.R
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CpmAdsUseCase @Inject constructor(private val context: Context) : UseCase<List<CpmItem>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<CpmItem>> {
        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_cpm_top_ads), CpmTopAdsResponse::class.java, requestParams?.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            CpmItemMapper().transform((it.getData(CpmTopAdsResponse::class.java) as CpmTopAdsResponse))
        }
    }
}