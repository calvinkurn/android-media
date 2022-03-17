package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.usecases.query.AffiliateUserDetailQuery
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class GetAffiliateUserUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : UseCase<AffiliateUserDetailData>() {

    private var graphqlUseCase: GraphqlUseCase<AffiliateUserDetailData> = GraphqlUseCase(graphqlRepository)

    init {
        graphqlUseCase.setGraphqlQuery(AffiliateUserDetailQuery())
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(AffiliateUserDetailData::class.java)
    }
    override suspend fun executeOnBackground(): AffiliateUserDetailData {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }
}