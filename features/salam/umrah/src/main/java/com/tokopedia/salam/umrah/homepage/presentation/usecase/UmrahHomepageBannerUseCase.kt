package com.tokopedia.salam.umrah.homepage.presentation.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class UmrahHomepageBannerUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
        GraphqlUseCase<UmrahHomepageBannerEntity>(graphqlRepository) {
    suspend fun executeBanner(rawQuery: String, fromCloud: Boolean = false): Result<UmrahHomepageBannerEntity> {

        try {
            this.setGraphqlQuery(rawQuery)
            this.setTypeClass(UmrahHomepageBannerEntity::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahHomepageBanner = this.executeOnBackground()
            return Success(umrahHomepageBanner)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }
}