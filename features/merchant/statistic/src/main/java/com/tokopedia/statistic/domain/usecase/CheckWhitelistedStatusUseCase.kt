package com.tokopedia.statistic.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.statistic.domain.model.CheckWhitelistedStatusResponse
import com.tokopedia.usecase.RequestParams
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/02/21
 */

//docs : https://tokopedia.atlassian.net/wiki/spaces/~354932339/pages/896109749/Centralized+Whitelist+System
@GqlQuery("CheckWhitelistedStatusGqlQuery", CheckWhitelistedStatusUseCase.QUERY)
class CheckWhitelistedStatusUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : BaseStatisticUseCase<Boolean>() {

    override suspend fun execute(params: RequestParams): Boolean {
        val expiryTime = TimeUnit.MINUTES.toMillis(EXPIRY_TIME)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
            .setExpiryTime(expiryTime)
            .build()
        val gqlRequest = GraphqlRequest(
            CheckWhitelistedStatusGqlQuery(),
            CheckWhitelistedStatusResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)
        val errors: List<GraphqlError>? = gqlResponse.getError(
            CheckWhitelistedStatusResponse::class.java
        )
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<CheckWhitelistedStatusResponse>(
                CheckWhitelistedStatusResponse::class.java
            )
            return response.whitelistedStatus.isWhitelisted
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    fun createParam(whitelistName: String): RequestParams {
        return RequestParams.create().apply {
            putString(KEY_WHITE_LIST_NAME, whitelistName)
        }
    }

    companion object {
        const val QUERY = """
            query checkWhitelistedStatus(${'$'}whitelistName: String!) {
              checkWhitelistedStatus(whitelistName: ${'$'}whitelistName) {
                Whitelisted
              }
            }
        """
        private const val KEY_WHITE_LIST_NAME = "whitelistName"
        private const val EXPIRY_TIME = 10L
    }
}