package com.tokopedia.home_account.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountErrorHandler
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.SaldoBalanceDataModel
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created by Ade Fulki on 21/02/21.
 */

class HomeAccountSaldoBalanceUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : GraphqlUseCase<SaldoBalanceDataModel>(graphqlRepository) {

    override suspend fun executeOnBackground(): SaldoBalanceDataModel {
        val query = rawQueries[AccountConstants.Query.QUERY_GET_BALANCE]
        val gqlRequest = GraphqlRequest(
                query,
                SaldoBalanceDataModel::class.java,
                mapOf<String, Any>()
        )
        val gqlStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), gqlStrategy)
        val errors = gqlResponse.getError(SaldoBalanceDataModel::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            var dataSaldo: SaldoBalanceDataModel? = gqlResponse.getData(SaldoBalanceDataModel::class.java)
            if (dataSaldo == null) {
                val mapResponse = Utils.convertResponseToJson(gqlResponse)
                dataSaldo = SaldoBalanceDataModel()
                AccountErrorHandler.logDataNull("Account_GetBalance",
                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
            }
            return dataSaldo
        }
    }
}