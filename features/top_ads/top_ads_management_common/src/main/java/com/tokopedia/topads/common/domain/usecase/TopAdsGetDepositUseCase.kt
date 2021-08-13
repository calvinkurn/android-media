package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val DEPOSIT = """query topadsDashboardDeposits(${'$'}shop_id: Int!) {
  topadsDashboardDeposits(shop_id: ${'$'}shop_id) {
    data {
      amount
      amount_fmt
    }
  }
}
"""

@GqlQuery("DepositQuery", DEPOSIT)

class TopAdsGetDepositUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                  private val userSession: UserSessionInterface)
    : GraphqlUseCase<Deposit>(graphqlRepository) {

    init {
        setTypeClass(Deposit::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(DepositQuery.GQL_QUERY)
        setParams()
    }

    private fun setParams() {
        val params = mutableMapOf(
                ParamObject.SHOP_id to userSession.shopId.toIntOrZero(),
        )
        setRequestParams(params)
    }
}