package com.tokopedia.centralizedpromoold.domain.usecase

import com.tokopedia.centralizedpromoold.domain.usecase.GetChatBlastSellerMetadataGqlQuery
import com.tokopedia.centralizedpromoold.view.model.ChatBlastSellerMetadataUiModel
import com.tokopedia.centralizedpromoold.domain.mapper.ChatBlastSellerMetadataMapperOld
import com.tokopedia.centralizedpromoold.domain.model.ChatBlastSellerMetadataResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.BaseGqlUseCase
import javax.inject.Inject

@GqlQuery("GetChatBlastSellerMetadataGqlQuery", GetChatBlastSellerMetadataUseCase.QUERY)
class GetChatBlastSellerMetadataUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: ChatBlastSellerMetadataMapperOld
) : BaseGqlUseCase<ChatBlastSellerMetadataUiModel>() {
    override suspend fun executeOnBackground(): ChatBlastSellerMetadataUiModel {
        val gqlRequest = GraphqlRequest(
            GetChatBlastSellerMetadataGqlQuery(),
            ChatBlastSellerMetadataResponse::class.java
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors = gqlResponse.getError(ChatBlastSellerMetadataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<ChatBlastSellerMetadataResponse>()
            return mapper.mapDomainDataModelToUiDataModel(data)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val QUERY = """
            query chatBlastSellerMetadata {
              chatBlastSellerMetadata {
                promo
                promoType
                url
              }
            }
        """
    }
}