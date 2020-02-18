package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by yoasfs on 2020-02-18
 */

class GetPopularKeywordUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase)
    : UseCase<GraphqlResponse>() {

    companion object {

        private const val PARAM_COUNT = "count"
        private const val DEFAULT_COUNT = 10

        fun getParam(count: Int): RequestParams {
            return RequestParams.create().apply {
                putInt(PARAM_COUNT, count)
            }
        }
    }

    //region query
    private val query by lazy {
        val count = "\$count"

        """
            query PopularKeywords($count: Int!) {
                popular_keywords(count: $count) {
                    keywords {
                      url
                      image_url
                      keyword
                      mobile_url
                      product_count
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        graphqlUseCase.clearRequest()
        val params = GraphqlRequest(query, HomeWidget.PopularKeywordList::class.java, getParam(DEFAULT_COUNT).parameters)
        graphqlUseCase.addRequest(params)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)

    }
}