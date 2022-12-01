package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoList
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("MerchantGetNewPromotionListQuery", GetNewPromotionUseCase.QUERY)
class GetNewPromotionUseCase @Inject constructor(
    repository: GraphqlRepository,
) : GraphqlUseCase<MerchantPromotionGetPromoListResponse>(repository) {


    init {
        setGraphqlQuery(MerchantGetNewPromotionListQuery())
        setTypeClass(MerchantPromotionGetPromoListResponse::class.java)
    }

    suspend fun execute(shopId: String): MerchantPromotionGetPromoList {
        setRequestParams(
            createRequestParams(
                shopId.toLongOrZero()
            ).parameters
        )
        val response = executeOnBackground().merchantPromotionGetPromoList
        val errors = response.header.messages
        if (errors.isNullOrEmpty()) {
            return response
        } else {
            throw MessageErrorException(errors.joinToString { it })
        }
    }

    companion object {
        const val QUERY = """
            query GetPromoList(${'$'}shopId: Int!) {
                  merchantPromotionGetPromoList(shop_id: ${'$'}shopId, tab_id: 0, search: "baru") {
                    header {
                      process_time
                      message
                      reason
                      error_code
                    }
                    data {
                      pages {
                        page_name
                        page_name_suffix
                      }
                    }
                  }
            }
        """
        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID_KEY, shopId)
            }
        }
    }
}