package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetSocketCredentialResponse
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 30/06/20.
 */
class GetSocketCredentialUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetSocketCredentialResponse.SocketCredential>() {

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
        """

    override suspend fun executeOnBackground(): GetSocketCredentialResponse.SocketCredential {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = GetSocketCredentialResponse::class.java,
                params = emptyMap(),
                gqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<GetSocketCredentialResponse>(GetSocketCredentialResponse::class.java)
        return response.socketCredential
    }
}