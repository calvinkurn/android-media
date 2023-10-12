package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhomecommon.domain.model.TopAdsShopInfoResponse
import com.tokopedia.sellerhomecommon.domain.model.TopadsGetShopInfoV2_1
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTopAdsShopInfoUseCase @Inject constructor(
    repository: GraphqlRepository,
) : GraphqlUseCase<TopAdsShopInfoResponse>(repository) {


    init {
        setGraphqlQuery(QUERY)
        setTypeClass(TopAdsShopInfoResponse::class.java)
    }

    suspend fun execute(shopId: String): TopadsGetShopInfoV2_1 {
        setRequestParams(
            createRequestParams(
                shopId
            ).parameters
        )
        val response = executeOnBackground().topadsGetShopInfoV2_1
        val errors = response.errors
        if (errors.isNullOrEmpty()) {
            return response
        } else {
            throw ResponseErrorException()
        }
    }

    companion object {
        const val QUERY = """
            query topadsGetShopInfoV2_1(${'$'}shopID: String!, ${'$'}source : String!) {
  topadsGetShopInfoV2_1(shopID: ${'$'}shopID, source: ${'$'}source) {
    data {
      ads {
        type
        is_used
      }
    }
    errors {
      code
      detail
      title
    }
  }
  }
        """
        private const val SHOP_ID_KEY = "shopID"
        private const val SOURCE_KEY = "source"
        private const val SOURCE_VALUE = "seller.home"

        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(SHOP_ID_KEY, shopId)
                putString(SOURCE_KEY, SOURCE_VALUE)
            }
        }
    }
}
