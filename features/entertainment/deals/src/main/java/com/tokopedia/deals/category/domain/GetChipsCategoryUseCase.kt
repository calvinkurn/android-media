package com.tokopedia.deals.category.domain

import com.tokopedia.deals.common.domain.DealsGqlQueries
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

class GetChipsCategoryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<CuratedData>
) {
    fun execute(
            onSuccess: (CuratedData) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        gqlUseCase.apply {
            setTypeClass(CuratedData::class.java)
            setGraphqlQuery(DealsGqlQueries.getChildCategory())
            setCacheStrategy(GraphqlCacheStrategy
                    .Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`()).build())
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    fun cancelJobs() {
        gqlUseCase.cancelJobs()
    }
}