package com.tokopedia.vouchercreation.voucherlist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.voucherlist.model.remote.ChatBlastSellerMetadata
import com.tokopedia.vouchercreation.voucherlist.model.remote.GetBroadCastMetaDataResponse
import javax.inject.Inject

class GetBroadCastMetaDataUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<ChatBlastSellerMetadata>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ChatBlastSellerMetadata {
        val graphqlRequest = GraphqlRequest(query, GetBroadCastMetaDataResponse::class.java, params.parameters)
        val graphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors = graphqlResponse.getError(GetBroadCastMetaDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = graphqlResponse.getData<GetBroadCastMetaDataResponse>(GetBroadCastMetaDataResponse::class.java)
            return data.chatBlastSellerMetadata
        } else {
            throw MessageErrorException(errors.joinToString(", ") {
                it.message
            })
        }
    }

    companion object {
        private val query = """
            query GetBroadCastMetaData {
              chatBlastSellerMetadata {
                quota
                status
              }
            }
        """.trimIndent()
    }
}