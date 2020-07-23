package com.tokopedia.home.account.revamp.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.account.AccountConstants.Query.NEW_QUERY_BUYER_ACCOUNT_HOME
import com.tokopedia.home.account.revamp.domain.data.model.AccountModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetBuyerAccountDataUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): UseCase<AccountModel>() {

    override suspend fun executeOnBackground(): AccountModel {
        val rawQuery = rawQueries[NEW_QUERY_BUYER_ACCOUNT_HOME]
        val gqlRequest = GraphqlRequest(rawQuery,
                AccountModel::class.java, mapOf<String, Any>())
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(AccountModel::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(AccountModel::class.java)
        }
    }
}