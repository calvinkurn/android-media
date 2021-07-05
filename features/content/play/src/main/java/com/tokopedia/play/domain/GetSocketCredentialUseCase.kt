package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.SocketCredential
import javax.inject.Inject


class GetSocketCredentialUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SocketCredential>(graphqlRepository) {

    private val query = """
        query GetSocketCredential{
          playGetSocketCredential{
            gc_token,
            setting {
              ping_interval
              max_chars
              max_retries
              min_reconnect_delay
            }
          }
        }
        """.trimIndent()

    override suspend fun executeOnBackground(): SocketCredential {
        val gqlRequest = GraphqlRequest(query, SocketCredential.Response::class.java, emptyMap())
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(SocketCredential.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(SocketCredential.Response::class.java) as SocketCredential.Response).socketCredential
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}