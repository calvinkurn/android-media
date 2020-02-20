package com.tokopedia.salam.umrah.homepage.presentation.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by firman on 28/10/19
 */

class UmrahHomepageCategoryFeaturedUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<UmrahHomepageCategoryFeaturedEntity>(graphqlRepository) {

    val umrahHomepageCategoryFeaturedParam = UmrahHomepageCategoryFeaturedParam()

    suspend fun executeCategoryFeatured(rawQuery: String, fromCloud: Boolean = false): Result<UmrahHomepageCategoryFeaturedEntity> {

        try {
            val params = mapOf(PARAM_UMRAH_CATEGORY to umrahHomepageCategoryFeaturedParam)

            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(params)
            this.setTypeClass(UmrahHomepageCategoryFeaturedEntity::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahHomepageCategoryFeatured = this.executeOnBackground()
            return Success(umrahHomepageCategoryFeatured)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        private const val PARAM_UMRAH_CATEGORY = "params"
    }
}