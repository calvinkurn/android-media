package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.MerchantPromotionListEligibleMapper
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoList
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("MerchantGetPromotionListQuery", GetPromotionUseCase.QUERY)
class GetPromotionUseCase @Inject constructor(
    repository: GraphqlRepository,
) : GraphqlUseCase<MerchantPromotionGetPromoListResponse>(repository) {


    init {
        setGraphqlQuery(MerchantGetPromotionListQuery())
        setTypeClass(MerchantPromotionGetPromoListResponse::class.java)
    }

    suspend fun execute(shopId: String, tabId: String = "0"): MerchantPromotionGetPromoList {
        setRequestParams(
            createRequestParams(
                shopId.toLongOrZero(),
                tabId.toLongOrZero()
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
            query GetPromoList(${'$'}shopId: Int!, ${'$'}tabId: Int!) {
                  merchantPromotionGetPromoList(shop_id: ${'$'}shopId, tab_id: ${'$'}tabId, search: "") {
                    header {
                      process_time
                      message
                      reason
                      error_code
                    }
                    data {
                      tab {
                        id
                        name
                        description
                        recommendation_text {
                          headline_text
                          detail_text
                        }
                      }
                      pages {
                        page_id
                        page_name
                        page_name_suffix
                        keyword
                        icon_image
                        banner_image
                        not_available_text
                        header_text
                        bottom_text
                        cta_text
                        cta_link
                        is_eligible
                        info_text
                      }
                    }
                  }
            }
        """
        private const val SHOP_ID_KEY = "shopId"
        private const val TAB_ID_KEY = "tabId"

        fun createRequestParams(shopId: Long, tabId: Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID_KEY, shopId)
                putLong(TAB_ID_KEY, tabId)
            }
        }
    }
}