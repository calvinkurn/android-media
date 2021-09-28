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
        private const val QUERY = "query GetPromoList(\$shopId: Int!) {\n" +
                "  merchantPromotionGetPromoList(shop_id: \$shopId, tab_id: 0, search: \"\") {\n" +
                "    header {\n" +
                "      process_time\n" +
                "      message\n" +
                "      reason\n" +
                "      error_code\n" +
                "    }\n" +
                "    data {\n" +
                "      pages {\n" +
                "        page_id\n" +
                "        is_eligible\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

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