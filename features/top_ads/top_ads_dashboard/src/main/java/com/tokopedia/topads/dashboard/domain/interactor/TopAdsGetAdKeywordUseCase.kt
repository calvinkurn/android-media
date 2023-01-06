package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 8/6/20.
 */

class TopAdsGetAdKeywordUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<KeywordsResponse>(graphqlRepository) {

    fun setParams(isPositive:Int,group:Int, search:String, sort:String?, status:Int?,page:Int) {
        val map = HashMap<String, Any?>()
        map[ParamObject.SHOP_id] = userSession.shopId
        map[ParamObject.GROUP] = group.toString()
        map[ParamObject.KEYWORD_TAG] = search
        map[ParamObject.IS_POSTIVE] = isPositive
        map[ParamObject.SORT] = sort
        map[ParamObject.STATUS] = status
        map[ParamObject.PAGE] = page
        setRequestParams(mapOf(ParamObject.QUERY_INPUT to map))
    }
    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (KeywordsResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(KeywordsResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}
