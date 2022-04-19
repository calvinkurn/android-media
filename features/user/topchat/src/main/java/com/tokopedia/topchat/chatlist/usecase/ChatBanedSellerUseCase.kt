package com.tokopedia.topchat.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.pojo.chatbannedstatus.ChatBannedSellerResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChatBanedSellerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatBannedSellerResponse>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getStatus(
            onSuccess: (Boolean) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatBannedSellerResponse::class.java)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val isBanned = response.chatBannedSeller.isBanned()
                    withContext(dispatchers.main) {
                        onSuccess(isBanned)
                    }
                },
                { exception ->
                    withContext(dispatchers.main) {
                        onError(exception)
                    }
                }
        )
    }

    private val query = """
        query chatBannedSeller {
          chatBannedSeller {
                status
          }
        }
    """.trimIndent()

}