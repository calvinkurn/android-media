package com.tokopedia.salam.umrah.homepage.presentation.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntityParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by firman on 08/11/19
 */

class UmrahHomepageMyUmrahUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<UmrahHomepageMyUmrahEntity>(graphqlRepository) {

    val umrahHomepageMyUmrahParam = UmrahHomepageMyUmrahEntityParam()

    suspend fun executeMyUmrah(rawQuery: String, fromCloud: Boolean = false): Result<UmrahHomepageMyUmrahEntity> {

        try {
            val params = mapOf(PARAM_UMRAH_CATEGORY to umrahHomepageMyUmrahParam)

            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(params)
            this.setTypeClass(UmrahHomepageMyUmrahEntity::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahHomepageMyUmrah = this.executeOnBackground()
            return Success(umrahHomepageMyUmrah)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        private const val PARAM_UMRAH_CATEGORY = "params"
    }
}