package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.usecases.query.AffiliateUserDetailQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class GetAffiliateUserUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : UseCase<Result<AffiliateUserDetailData>>() {

    private var graphqlUseCase: GraphqlUseCase<AffiliateUserDetailData> = GraphqlUseCase(graphqlRepository)

    init {
        graphqlUseCase.setGraphqlQuery(AffiliateUserDetailQuery())
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(AffiliateUserDetailData::class.java)
    }
    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<AffiliateUserDetailData> {
        return try{
            graphqlUseCase.setRequestParams(params.parameters)
            val data = graphqlUseCase.executeOnBackground()
            return Success(data)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setStrategyCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }

    fun setStrategyCloudThenCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }
}