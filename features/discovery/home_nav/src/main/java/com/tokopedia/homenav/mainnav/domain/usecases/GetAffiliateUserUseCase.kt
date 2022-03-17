package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.usecases.query.AffiliateUserDetailQuery
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.pojo.eligibility.WalletAppEligibility
import com.tokopedia.navigation_common.usecase.query.QueryHomeWallet
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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