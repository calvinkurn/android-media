package com.tokopedia.stickylogin.domain.usecase

import android.content.res.Resources
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.query.StickyLoginQuery
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import javax.inject.Inject

class StickyLoginUseCase @Inject constructor(
    val resources: Resources,
    val repository: GraphqlRepository
): GraphqlUseCase<StickyLoginTickerPojo.TickerResponse>(repository) {

    init {
        val query = StickyLoginQuery.query
        setTypeClass(StickyLoginTickerPojo.TickerResponse::class.java)
        setGraphqlQuery(query)
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