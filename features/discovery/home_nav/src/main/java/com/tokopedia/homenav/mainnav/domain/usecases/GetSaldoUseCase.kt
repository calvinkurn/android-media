package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetHomeNavSaldoQuery
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
            val gqlRequest = GraphqlRequest(GetHomeNavSaldoQuery(), SaldoPojo::class.java, params.parameters)
            val gqlResponse = graphqlUseCase.response(listOf(gqlRequest), GraphqlCacheStrategy
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
}
