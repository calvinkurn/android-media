package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by yoasfs on 2020-02-18
 */

class GetPopularKeywordUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeWidget.PopularKeywordQuery>)
    : UseCase<HomeWidget.PopularKeywordQuery>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeWidget.PopularKeywordQuery::class.java)
    }
    companion object {

        const val PARAM_COUNT = "count"
        const val DEFAULT_COUNT = 4
    }

    private var params: RequestParams = RequestParams.create()

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
                      product_count_formatted
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): HomeWidget.PopularKeywordQuery {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(count: Int = DEFAULT_COUNT) {
        params.parameters.clear()
        params.putInt(PARAM_COUNT, count)
    }
}