package com.tokopedia.topads.edit.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class GetAdKeywordUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<GetKeywordResponse>(graphqlRepository) {


    fun setParams(groupId: Int) {
        val map = HashMap<String, Any?>()
        map[ParamObject.SHOP_id] = userSession.shopId
        map[ParamObject.GROUP_ID] = groupId.toString()
        val queryMap = mapOf(ParamObject.SOURCE to ParamObject.KEYWORD_SOURCE, ParamObject.FILTER to map)
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_ads_get_ad_keyword)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GetKeywordResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GetKeywordResponse::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}