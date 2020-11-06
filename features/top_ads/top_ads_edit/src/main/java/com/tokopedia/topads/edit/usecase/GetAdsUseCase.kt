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
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class GetAdsUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<GetAdProductResponse>(graphqlRepository) {


    fun setParams(page:Int,groupId: Int?) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = Integer.parseInt(userSession.shopId)
        queryMap[ParamObject.TYPE] = ParamObject.PRODUCT
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.PER_PAGE] = 20
        queryMap[ParamObject.GROUPID] = groupId
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_get_ads_product)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GetAdProductResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GetAdProductResponse::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)
        }, onError)
    }
}