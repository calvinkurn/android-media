package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.ChatBlastSellerMetadataMapper
import com.tokopedia.centralizedpromo.domain.model.ChatBlastSellerMetadataResponse
import com.tokopedia.centralizedpromo.view.model.ChatBlastSellerMetadataUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.BaseGqlUseCase
import javax.inject.Inject

class GetChatBlastSellerMetadataUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: ChatBlastSellerMetadataMapper
) : BaseGqlUseCase<ChatBlastSellerMetadataUiModel>() {
    override suspend fun executeOnBackground(): ChatBlastSellerMetadataUiModel {
        val gqlRequest = GraphqlRequest(QUERY, ChatBlastSellerMetadataResponse::class.java)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(ChatBlastSellerMetadataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<ChatBlastSellerMetadataResponse>()
            return mapper.mapDomainDataModelToUiDataModel(data)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val QUERY = "query {\n" +
                "  chatBlastSellerMetadata {\n" +
                "    promo\n" +
                "    promoType\n" +
                "    url\n" +
                "  }\n" +
                "}"
    }
}