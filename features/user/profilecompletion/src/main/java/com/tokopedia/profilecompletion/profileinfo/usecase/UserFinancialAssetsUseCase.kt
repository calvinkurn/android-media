package com.tokopedia.profilecompletion.profileinfo.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.profileinfo.data.UserFinancialAssetsData
import javax.inject.Inject

class UserFinancialAssetsUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, UserFinancialAssetsData>(dispatcher.io) {
    override fun graphqlQuery(): String =
        """
            query {
              checkUserFinancialAssets {
                has_financial_assets
                detail {
                  has_egold
                  has_mutual_fund
                  has_deposit_balance
                  has_ongoing_trx
                  has_loan
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): UserFinancialAssetsData =
        graphqlRepository.request(graphqlQuery(), params)
}
