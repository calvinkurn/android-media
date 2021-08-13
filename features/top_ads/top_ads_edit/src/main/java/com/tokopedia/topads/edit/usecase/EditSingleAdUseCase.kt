package com.tokopedia.topads.edit.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.DATA
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.response.EditSingleAdResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class EditSingleAdUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<EditSingleAdResponse>(graphqlRepository) {


    fun setParams(adId: String, priceBid: Float, priceDaily: Float) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ParamObject.AD_ID] = adId
        queryMap[ParamObject.AD_TYPE] = "1" /*product*/
        queryMap[ParamObject.GROUPID] = "0"
        queryMap[ParamObject.PRICE_BID] = priceBid
        queryMap[ParamObject.PRICE_DAILY] = priceDaily
        queryMap[ParamObject.TOGGLE] = "on"
        setRequestParams(mapOf("input" to mapOf(DATA to queryMap)))
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_edit_sigle_ad)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (EditSingleAdResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(EditSingleAdResponse::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}