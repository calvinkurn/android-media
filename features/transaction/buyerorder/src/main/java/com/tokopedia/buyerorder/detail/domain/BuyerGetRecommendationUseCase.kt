package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RecommendationResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by fwidjaja on 06/12/20.
 */
class BuyerGetRecommendationUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<RecommendationResponse>() {
    private var query: String = ""
    private var params: MutableMap<String, Any> = mutableMapOf()

    fun setup(query: String, params: MutableMap<String, Any>) {
        this.query = query
        this.params = params
    }

    override fun createObservable(requestParams: RequestParams): Observable<RecommendationResponse> {
        val graphqlRequest = GraphqlRequest(query, RecommendationResponse::class.java, params, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val actionButtonList = it.getData<RecommendationResponse>(RecommendationResponse::class.java)
            actionButtonList
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}