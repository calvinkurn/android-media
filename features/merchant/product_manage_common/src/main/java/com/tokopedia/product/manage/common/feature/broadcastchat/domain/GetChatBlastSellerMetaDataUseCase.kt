package com.tokopedia.product.manage.common.feature.broadcastchat.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.common.feature.broadcastchat.data.model.ChatBlastSellerMetaDataResponse
import com.tokopedia.product.manage.common.feature.broadcastchat.presentation.model.ChatBlastSellerEntryPointUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class GetChatBlastSellerMetaDataUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val mapper: ChatBlastSellerMetaDataMapper
) : GraphqlUseCase<ChatBlastSellerMetaDataResponse.ChatBlastSellerMetadata>(repository) {

    companion object {
        private val QUERY = """ query {
            chatBlastSellerMetadata{
                    url
                  }
            } """.trimIndent()
    }

    suspend fun execute(): Result<ChatBlastSellerEntryPointUiModel> {
        val chatBlastSellerMetadataRequest = GraphqlRequest(QUERY, ChatBlastSellerMetaDataResponse.ChatBlastSellerMetadata::class.java)
        return try {
            val gqlResponse = repository.getReseponse(listOf(chatBlastSellerMetadataRequest))
            val chatBlastSellerMetadataResponse = mapper.mapToChatBlastSellerMetaData(
                    requireNotNull(gqlResponse.getData<ChatBlastSellerMetaDataResponse>(
                            ChatBlastSellerMetaDataResponse.ChatBlastSellerMetadata::class.java
                    ).chatBlastSellerMetadata))
            Success(chatBlastSellerMetadataResponse)
        } catch (e: Throwable) {
            Fail(e)
        }
    }
}