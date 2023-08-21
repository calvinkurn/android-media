package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.domain.query.GetEtalaseList
import javax.inject.Inject

private const val SHOP_ID = "shopId"
class GetEtalaseListUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ResponseEtalase.Data>(graphqlRepository) {

    init {
        setTypeClass(ResponseEtalase.Data::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetEtalaseList)
    }

    fun setParams(shopId: String) {
        val params = mutableMapOf(
                SHOP_ID to shopId
        )
        setRequestParams(params)
    }

}
