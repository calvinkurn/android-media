package com.tokopedia.topads.edit.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.R
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class GetProductUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ResponseProductList.Result>(graphqlRepository) {


    fun setParams(keyword: String, etalaseId: String, sortBy: String, promoted: String, rows: Int, start: Int) {
        val queryMap = mutableMapOf<String, Any>(
                ParamObject.KEYWORD to keyword,
                ParamObject.ETALASE to etalaseId,
                ParamObject.SORT_BY to sortBy,
                ParamObject.ROWS to rows,
                ParamObject.START to start,
                ParamObject.STATUS to promoted,
                ParamObject.SHOP_ID to Integer.parseInt(userSession.shopId)
        )
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_ads_create_productlist)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (ResponseProductList.Result) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ResponseProductList.Result::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}