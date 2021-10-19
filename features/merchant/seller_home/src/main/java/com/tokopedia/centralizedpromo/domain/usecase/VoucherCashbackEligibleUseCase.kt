package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.MerchantPromotionListEligibleMapper
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class VoucherCashbackEligibleUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val mapper: MerchantPromotionListEligibleMapper
) : GraphqlUseCase<MerchantPromotionGetPromoListResponse>(repository) {

    companion object {

        val QUERY = """
            query GetPromoList(${'$'}shopId: Int!) {
              merchantPromotionGetPromoList(shop_id: ${'$'}shopId, tab_id: 0, search: "") {
                header {
                  process_time
                  message
                  reason
                  error_code
                }
                data {
                  pages {
                    page_id
                    is_eligible
                  }
                }
              }
            }
        """.trimIndent()

        private const val SHOP_ID_KEY = "shopId"

        private fun createRequestParams(shopId: Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID_KEY, shopId)
            }
        }
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(MerchantPromotionGetPromoListResponse::class.java)
    }

    suspend fun execute(shopId: String): Boolean {
        setRequestParams(createRequestParams(shopId.toLongOrZero()).parameters)
        val response = executeOnBackground().merchantPromotionGetPromoList
        val errors = response.header.messages
        if (errors.isNullOrEmpty()) {
            return mapper.isVoucherCashbackEligible(response.data.pages)
        } else {
            throw MessageErrorException(errors.joinToString { it })
        }
    }

}