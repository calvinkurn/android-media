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
        private val graphqlUseCase: GraphqlUseCase<HomeWidget.PopularKeywordList>)
    : UseCase<HomeWidget.PopularKeywordList>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeWidget.PopularKeywordList::class.java)
    }
    companion object {

        const val PARAM_COUNT = "count"
        const val DEFAULT_COUNT = 10
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
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): HomeWidget.PopularKeywordList {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(count: Int = DEFAULT_COUNT) {
        params = RequestParams.create().apply {
            putInt(PARAM_COUNT, count)
        }
    }
}