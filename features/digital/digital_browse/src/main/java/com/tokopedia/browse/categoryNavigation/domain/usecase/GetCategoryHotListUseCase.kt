package com.tokopedia.browse.categoryNavigation.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.CategoryHotlist
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.Data
import com.tokopedia.browse.categoryNavigation.domain.mapper.CategoryHotlistMapper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetCategoryHotListUseCase @Inject constructor(private val context: Context,
                                                    private val graphqlUseCase: GraphqlUseCase
) : UseCase<CategoryHotlist>() {

    private val KEY_CATEGORY_ID = "categoryId"
    private val KEY_CATEGORY_NAME = "categoryName"


    fun createRequestParams(categoryId: Int, categoryName: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(KEY_CATEGORY_ID, categoryId)
        requestParams.putString(KEY_CATEGORY_NAME, categoryName)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<CategoryHotlist> {

        val categoryName = requestParams!!.getString(KEY_CATEGORY_NAME, "")
        requestParams.clearValue(KEY_CATEGORY_NAME)

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.category_hotlist), Data::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`() * 2).setSessionIncluded(true).build()

        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {

            CategoryHotlistMapper().transform(((it.getData(Data::class.java) as Data).categoryHotlist), categoryName)
        }

    }
}
