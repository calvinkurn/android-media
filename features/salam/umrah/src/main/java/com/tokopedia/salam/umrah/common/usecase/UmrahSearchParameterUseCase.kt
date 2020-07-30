package com.tokopedia.salam.umrah.common.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by firman on 17/10/19
 */

class UmrahSearchParameterUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<UmrahSearchParameterEntity>(graphqlRepository) {

    suspend fun executeUseCase(rawQuery: String, fromCloud: Boolean = false): Result<UmrahSearchParameterEntity> {

        try {
            this.setGraphqlQuery(rawQuery)
            this.setTypeClass(UmrahSearchParameterEntity::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahSearchParam = this.executeOnBackground()
            return Success(umrahSearchParam)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }
}