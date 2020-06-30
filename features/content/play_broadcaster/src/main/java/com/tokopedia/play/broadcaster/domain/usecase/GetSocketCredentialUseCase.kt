package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.broadcaster.domain.model.GetSocketCredentialResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import javax.inject.Inject


/**
 * Created by mzennis on 30/06/20.
 */
class GetSocketCredentialUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<GetSocketCredentialResponse.Credential>() {

    private val query = """
            query {
              playGetSocketCredential(){
                header{
                  status
                  message
                }
                data{
                  gc_token
                  setting {
                    ping_interval
                    max_char
                    max_retry
                    min_reconnect_delay
                  }
                }
              }
            }
        """

    override suspend fun executeOnBackground(): GetSocketCredentialResponse.Credential {
        val gqlRequest = GraphqlRequest(query, GetSocketCredentialResponse::class.java, emptyMap())
        val gqlResponse = configureGqlResponse(graphqlRepository, gqlRequest, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetSocketCredentialResponse>(GetSocketCredentialResponse::class.java)
        if (response?.socketCredential != null) {
            if (response.socketCredential.header.message.isNotEmpty())
                throw DefaultErrorThrowable(response.socketCredential.header.message)
            else
                return response.socketCredential.data
        }
        throw DefaultErrorThrowable()
    }
}