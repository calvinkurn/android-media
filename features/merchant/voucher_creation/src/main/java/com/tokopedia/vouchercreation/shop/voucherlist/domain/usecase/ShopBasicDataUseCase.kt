package com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResponse
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import javax.inject.Inject

class ShopBasicDataUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): BaseGqlUseCase<ShopBasicDataResult>() {

    companion object {
        const val QUERY = "query ShopBasicData {\n" +
                "\tshopBasicData {\n" +
                "    result {\n" +
                "      domain\n" +
                "      logo\n" +
                "      name\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): ShopBasicDataResult {
        val request = GraphqlRequest(QUERY, ShopBasicDataResponse::class.java, RequestParams.EMPTY.parameters)
        val response = gqlRepository.response(listOf(request))

        val errors = response.getError(ShopBasicDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<ShopBasicDataResponse>()
            val errorMessage = data.shopBasicData.error.errorMessage
            if (errorMessage.isEmpty()) {
                return data.shopBasicData.result
            } else {
                throw MessageErrorException(errorMessage)
            }
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }
}