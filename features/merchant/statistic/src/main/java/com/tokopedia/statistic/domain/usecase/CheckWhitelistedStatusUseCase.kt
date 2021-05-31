package com.tokopedia.statistic.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.usecase.BaseGqlUseCase
import com.tokopedia.statistic.di.StatisticScope
import com.tokopedia.statistic.domain.model.CheckWhitelistedStatusResponse
import com.tokopedia.usecase.RequestParams
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/02/21
 */

//docs : https://tokopedia.atlassian.net/wiki/spaces/~354932339/pages/896109749/Centralized+Whitelist+System

@StatisticScope
class CheckWhitelistedStatusUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<Boolean>() {

    override suspend fun executeOnBackground(): Boolean {
        val tenMinutes = TimeUnit.MINUTES.toMillis(10)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(tenMinutes)
                .build()
        val gqlRequest = GraphqlRequest(QUERY, CheckWhitelistedStatusResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)
        val errors: List<GraphqlError>? = gqlResponse.getError(CheckWhitelistedStatusResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<CheckWhitelistedStatusResponse>()
            return response.whitelistedStatus.isWhitelisted
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val KEY_WHITE_LIST_NAME = "whitelistName"

        private val QUERY = """
            query checkWhitelistedStatus(${'$'}whitelistName: String!) {
              checkWhitelistedStatus(whitelistName: ${'$'}whitelistName) {
                Whitelisted
              }
            }
        """.trimIndent()

        fun createParam(whitelistName: String): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_WHITE_LIST_NAME, whitelistName)
            }
        }
    }
}