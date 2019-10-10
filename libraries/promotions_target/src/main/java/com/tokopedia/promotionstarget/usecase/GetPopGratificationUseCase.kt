package com.tokopedia.promotionstarget.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promotionstarget.data.GetPopGratificationlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

class GetPopGratificationUseCase(val queryString: String): UseCase<GetPopGratificationlResponse>() {


    override fun createObservable(requestParams: RequestParams?): Observable<GetPopGratificationlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, GetPopGratificationlResponse::class.java, getQueryParams("", ""))
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.addRequest(graphqlRequest)

        graphqlUseCase.executeSync()
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data = (it.getData(GetPopGratificationlResponse::class.java) as GetPopGratificationlResponse)
            data
        } //To change body of created functions use File | Settings | File Templates.
    }

    fun getData(){

    }

    private fun getQueryParams(campaignSlug: String, page: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        val requestParams = RequestParams.create()
        variables["CampaignSlug"] = campaignSlug
        variables["Page"] = page
        return variables
    }

}