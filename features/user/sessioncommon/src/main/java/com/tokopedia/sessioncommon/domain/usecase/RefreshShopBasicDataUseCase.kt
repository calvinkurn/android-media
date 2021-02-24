package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.RefreshShopDataResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.exception.RefreshShopDataException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class RefreshShopBasicDataUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): GraphqlUseCase<ShopData>(gqlRepository) {

    companion object {
        private const val QUERY = "query RefreshShopBasicData {\n" +
                "  shopBasicData {\n" +
                "    result {\n" +
                "      shopID\n" +
                "      name\n" +
                "      logo\n" +
                "      level\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): ShopData {
        try {
            GraphqlRequest(QUERY, RefreshShopDataResponse::class.java, RequestParams.EMPTY.parameters).let { request ->
                gqlRepository.getReseponse(listOf(request)).let { response ->
                    response.getError(RefreshShopDataResponse::class.java).let { errors ->
                        if (errors.isNullOrEmpty()) {
                            response.getData<RefreshShopDataResponse>(RefreshShopDataResponse::class.java).shopInfo.shopData.let {
                                return it
                            }
                        } else {
                            throw MessageErrorException(errors.joinToString())
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            throw RefreshShopDataException(ex.message.orEmpty())
        }
    }
}