package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.BlastSellerMetaDataResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatBlastSellerMetaDataUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<Unit, BlastSellerMetaDataResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        query {
          chatBlastSellerMetadata {
            status
            url
            urlBroadcast
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Flow<BlastSellerMetaDataResponse> {
        return repository.requestAsFlow(graphqlQuery(), params)
    }
}
