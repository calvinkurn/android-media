package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.domain.query.GetBalanceAndPointQuery
import javax.inject.Inject

open class GetBalanceAndPointUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, BalanceAndPointDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return GetBalanceAndPointQuery.query
    }

    override suspend fun execute(params: String): BalanceAndPointDataModel {
        val mapParams = getParams(params)
        return request(repository, mapParams)
    }

    private fun getParams(
        partnerCode: String
    ): Map<String, Any> = mapOf(
        PARAM_PARTNER_CODE to partnerCode
    )

    companion object {
        private const val PARAM_PARTNER_CODE = "partnerCode"
    }
}