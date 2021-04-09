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
        const val PARAM_PAGE = "page"
        const val DEFAULT_COUNT = 4
        const val DEFAULT_PAGE = 1
    }

    private var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): HomeWidget.PopularKeywordQuery {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(count: Int = DEFAULT_COUNT, page: Int = DEFAULT_PAGE) {
        params.parameters.clear()
        params.putInt(PARAM_COUNT, count)
        params.putInt(PARAM_PAGE, page)
    }
}