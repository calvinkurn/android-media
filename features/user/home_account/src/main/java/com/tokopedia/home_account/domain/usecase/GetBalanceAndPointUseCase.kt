package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.domain.query.GetBalanceAndPointQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class GetBalanceAndPointUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, BalanceAndPointDataModel>(dispatcher.io) {

    private var query: String = ""

    fun setQuery(walletId: String) {
        when (walletId) {
            AccountConstants.WALLET.GOPAY,
            AccountConstants.WALLET.GOPAYLATER,
            AccountConstants.WALLET.OVO -> {
                query = GetBalanceAndPointQuery.gopayOvoQuery
            }
            AccountConstants.WALLET.TOKOPOINT -> {
                query = GetBalanceAndPointQuery.tokopointsQuery
            }
        }
    }

    fun getParams(
        partnerCode: String
    ): Map<String, Any> = mapOf(
        PARAM_PARTNER_CODE to partnerCode
    )

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: Map<String, Any>): BalanceAndPointDataModel {
        return request(repository, params)
    }

    companion object {
        private const val PARAM_PARTNER_CODE = "partnerCode"
    }
}