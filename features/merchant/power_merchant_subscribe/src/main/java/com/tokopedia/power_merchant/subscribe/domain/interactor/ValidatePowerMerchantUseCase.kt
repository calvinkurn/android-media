package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.power_merchant.subscribe.domain.model.GoldValidateShopBeforePMResponse
import com.tokopedia.power_merchant.subscribe.domain.query.ValidatePowerMerchant
import javax.inject.Inject

class ValidatePowerMerchantUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GoldValidateShopBeforePMResponse>(graphqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(ValidatePowerMerchant.QUERY)
        setTypeClass(GoldValidateShopBeforePMResponse::class.java)
    }

    suspend fun execute(): GoldValidateShopBeforePMResponse {
        return executeOnBackground()
    }
}