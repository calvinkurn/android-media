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
import com.tokopedia.topads.edit.data.response.ResponseGroupValidateName
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class ValidGroupUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ResponseGroupValidateName>(graphqlRepository) {


    fun setParams(groupName: String) {
        val params = mutableMapOf(
                ParamObject.SHOP_ID to userSession.shopId.toInt(),
                ParamObject.GROUP_NAME to groupName
        )
        setRequestParams(params)
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_ads_validate_group_name)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ResponseGroupValidateName::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it.topAdsGroupValidateName)

        }, onError)
    }
}