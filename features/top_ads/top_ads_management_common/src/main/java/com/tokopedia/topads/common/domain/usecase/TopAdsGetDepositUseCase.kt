package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.domain.query.GetTopadsDashboardDepositsV2
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetDepositUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                  private val userSession: UserSessionInterface)
    : GraphqlUseCase<Deposit>(graphqlRepository) {

    init {
        setTypeClass(Deposit::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetTopadsDashboardDepositsV2)
        setParams()
    }

    private fun setParams() {
        val params = mutableMapOf(
                ParamObject.SHOP_id to userSession.shopId
        )
        setRequestParams(params)
    }
}
