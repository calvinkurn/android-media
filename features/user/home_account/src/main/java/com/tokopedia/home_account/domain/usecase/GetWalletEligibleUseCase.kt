package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.WalletEligibleDataModel
import com.tokopedia.home_account.domain.query.GetWalletEligibleQuery
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetWalletEligibleUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Map<String, Any>, WalletEligibleDataModel>(dispatcher) {

    fun getParams(
        partnerCode: String,
        walletCode: String
    ): Map<String, Any> = mapOf(
        PARAM_PARTNER_CODE to partnerCode,
        PARAM_WALLET_CODE to walletCode
    )

    override fun graphqlQuery(): String {
        return GetWalletEligibleQuery.query
    }

    override suspend fun execute(params: Map<String, Any>): WalletEligibleDataModel {
        return repository.request(graphqlQuery(), params)
    }

    companion object {
        private const val PARAM_PARTNER_CODE = "partnerCode"
        private const val PARAM_WALLET_CODE = "walletCode"
    }
}