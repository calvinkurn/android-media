package com.tokopedia.stickylogin.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.stickylogin.R
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.common.StickyLoginConstant
import javax.inject.Inject

@Deprecated("delete soon")
class StickyLoginUseCase @Inject constructor(
    val resources: Resources,
    val repository: GraphqlRepository
): GraphqlUseCase<StickyLoginTickerDataModel.TickerResponse>(repository) {

    init {
        setTypeClass(StickyLoginTickerDataModel.TickerResponse::class.java)
        setGraphqlQuery(GraphqlHelper.loadRawString(resources, R.raw.gql_sticky_login_query))
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).apply {
            setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
            setSessionIncluded(true)
        }.build())
    }

    fun setParams(page: StickyLoginConstant.Page) {
        val map = mutableMapOf<String, Any>(
                StickyLoginConstant.PARAMS_PAGE to page.toString()
        )
        setRequestParams(map)
    }
}