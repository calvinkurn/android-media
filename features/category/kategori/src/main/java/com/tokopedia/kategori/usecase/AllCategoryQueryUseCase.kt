package com.tokopedia.kategori.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kategori.model.CategoryAllList
import com.tokopedia.kategori.model.Data
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kategori.R
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

private const val KEY_DEPTH = "depth"
private const val KEY_IS_TRENDING = "isTrending"


class AllCategoryQueryUseCase @Inject constructor(private val context: Context,
                                                  private val graphqlUseCase: GraphqlUseCase
) : UseCase<CategoryAllList>() {

    fun createRequestParams(depth: Int, isTrending: Boolean): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(KEY_DEPTH, depth)
        requestParams.putBoolean(KEY_IS_TRENDING, isTrending)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<CategoryAllList> {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.categorylist), Data::class.java, requestParams?.parameters)
        graphqlUseCase.clearRequest()
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`() * 2).setSessionIncluded(true).build()

        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {

            ((it.getData(Data::class.java)) as Data).categoryAllList
        }

    }
}