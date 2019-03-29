package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.product.data.FlashSaleTncGQL
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject
import javax.inject.Named

class GetFlashSaleTncUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_TNC) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository) : GraphqlUseCase<FlashSaleTncGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleTncGQL::class.java)
        setGraphqlQuery(gqlRawString)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).apply {
            setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
            setSessionIncluded(true)
        }.build())
    }

    fun setParams(campaignSlug: String, shopId: Int) {
        val map = mutableMapOf<String, Any?>(
                FlashSaleConstant.PARAM_SLUG to campaignSlug,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        setRequestParams(map)
    }

}
