package com.tokopedia.home.account.revamp.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.account.AccountConstants.Query.NEW_QUERY_BUYER_ACCOUNT_HOME
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler
import com.tokopedia.home.account.revamp.Utils
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetBuyerAccountDataUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): UseCase<AccountDataModel>() {

    override suspend fun executeOnBackground(): AccountDataModel {
        val rawQuery = rawQueries[NEW_QUERY_BUYER_ACCOUNT_HOME]
        val gqlRequest = GraphqlRequest(rawQuery,
                AccountDataModel::class.java, mapOf<String, Any>())
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(AccountDataModel::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            var data: AccountDataModel? = gqlResponse.getData(AccountDataModel::class.java)
            if (data == null) {
                val mapResponse = Utils.convertResponseToJson(gqlResponse)
                data = AccountDataModel()
                AccountHomeErrorHandler.logDataNull("GetBuyerAccountDataUseCase",
                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
            }
            return data
        }
    }
}