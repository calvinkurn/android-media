package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSaldoUseCase @Inject constructor(
        val graphqlUseCase: GraphqlRepository)
    : UseCase<Result<SaldoPojo>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<SaldoPojo> {
        return try {
            val gqlRequest = GraphqlRequest(query, SaldoPojo::class.java, params.parameters)
            val gqlResponse = graphqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD).build())

            val error = gqlResponse.getError(SaldoPojo::class.java)
            if (error == null || error.isEmpty()) {
                Success(gqlResponse.getData(SaldoPojo::class.java))
            } else {
                Fail(MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", ")))
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {
        private val query = getQuery()
        private fun getQuery(): String {

            return """query {
                    balance {
                        buyer_hold
                        buyer_hold_fmt
                        buyer_usable
                        buyer_usable_fmt
                        seller_hold
                        seller_hold_fmt
                        seller_usable
                        seller_usable_fmt
                        have_error
                    }
            } """.trimIndent()
        }
    }
}