package com.tokopedia.browse.categoryNavigation.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoryAllList
import com.tokopedia.browse.categoryNavigation.data.model.category.Data
import com.tokopedia.browse.categoryNavigation.domain.mapper.CategoryListOneModelMapper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetCategoryLevelOneUseCase
@Inject constructor(private val context: Context,
                    private val graphqlUseCase: GraphqlUseCase/*,
                                                 private val rawQueries: Map<String, String>*/)
    : UseCase<CategoryAllList>() {

    private val cacheDuration = TimeUnit.HOURS.toSeconds(3)


    fun createRequestParams(safeSearch: Boolean): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putBoolean("safeSearch", safeSearch)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<CategoryAllList> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.category_list), Data::class.java, requestParams!!.parameters, false)
        graphqlUseCase.clearRequest()

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(cacheDuration).setSessionIncluded(true).build()

        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {

            CategoryListOneModelMapper().transform(it.getData(Data::class.java) as Data)


        }

    }

}