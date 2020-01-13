package com.tokopedia.salam.umrah.homepage.presentation.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class UmrahHomepageBannerUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
        GraphqlUseCase<UmrahHomepageBannerEntity>(graphqlRepository) {
    suspend fun executeBanner(rawQuery: String, fromCloud: Boolean = false, response: String): Result<UmrahHomepageBannerEntity> {

        try {
//            val umrahHomepageBannerParam = UmrahHomepageBannerParam()
//
//            val params = mapOf(PARAM_UMRAH_CATEGORY to umrahHomepageBannerParam)
//
//            this.setGraphqlQuery(rawQuery)
//            this.setRequestParams(params)
//            this.setTypeClass(UmrahHomepageBannerEntity::class.java)
//            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
//                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
//            val umrahHomepageBanner = this.executeOnBackground()
            delay(500)
            val gson = Gson()
            return Success(gson.fromJson(response,
                    UmrahHomepageBannerEntity::class.java))
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        private const val PARAM_UMRAH_CATEGORY = "params"
    }
        }