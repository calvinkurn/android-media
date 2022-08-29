package com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.TokenResponse
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): BaseGqlUseCase<String>() {

    companion object {
        const val QUERY = "query GetToken{\n" +
                "\tgetInitiateVoucherPage(Action: \"update\"){\n" +
                "\t\theader{\n" +
                "          process_time\n" +
                "          messages\n" +
                "          reason\n" +
                "          error_code\n" +
                "        }\n" +
                "        data{\n" +
                "          token\n" +
                "        }\n" +
                "    }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): String {
        val request = GraphqlRequest(QUERY, TokenResponse::class.java, RequestParams.EMPTY.parameters)
        val response = gqlRepository.response(listOf(request))

        val errors = response.getError(TokenResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return response.getData<TokenResponse>().tokenModel.data.token
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }
}