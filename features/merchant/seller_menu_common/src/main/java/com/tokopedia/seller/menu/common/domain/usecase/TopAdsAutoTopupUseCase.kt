package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.seller.menu.common.domain.entity.TopAdsAutoTopupDataModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TopAdsAutoTopupUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<Boolean>() {

    companion object {
        const val QUERY = "query GetTopAdsAutoTopup(\$shopId: String!) {\n" +
                "  topAdsAutoTopup(shop_id: \$shopId) {\n" +
                "    data {\n" +
                "      status\n" +
                "    }\n" +
                "    errors {\n" +
                "      Code\n" +
                "      Title\n" +
                "      Detail\n" +
                "      Object {\n" +
                "       Text\n" +
                "       Type\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val SHOP_ID_KEY = "shopId"

        private const val IS_NOT_AUTO_TOPADS = "0"
        private const val IS_AUTO_TOPADS = "1"

        fun createRequestParams(shopId: String) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, shopId)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(QUERY, TopAdsAutoTopupDataModel::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(TopAdsAutoTopupDataModel::class.java)
        if (gqlError.isNullOrEmpty()) {
            val topAdsAutoTopupResponse : TopAdsAutoTopupDataModel = gqlResponse.getData(TopAdsAutoTopupDataModel::class.java)
            val responseError = topAdsAutoTopupResponse.topAdsAutoTopup?.error
            if (responseError.isNullOrEmpty()) {
                topAdsAutoTopupResponse.topAdsAutoTopup?.autoTopupStatus?.status?.mapToBooleanValue()?.let {
                    return it
                }
            } else {
                val topadsErrorObjectMessage = responseError.joinToString {
                    it.errorObject?.errorTextList.let { errorList ->
                        if (errorList.isNullOrEmpty()) {
                            it.detail.orEmpty()
                        } else {
                            errorList.joinToString()
                        }
                    }
                }
                throw MessageErrorException(topadsErrorObjectMessage)
            }
        }
        throw MessageErrorException(gqlError.joinToString { it.message })
    }

    private fun String?.mapToBooleanValue() : Boolean? {
        return this?.let {
            when(it) {
                IS_NOT_AUTO_TOPADS -> false
                IS_AUTO_TOPADS -> true
                else -> throw ResponseErrorException()
            }
        }
    }
}