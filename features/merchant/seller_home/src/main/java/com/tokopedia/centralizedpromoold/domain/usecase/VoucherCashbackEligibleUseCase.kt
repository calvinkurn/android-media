package com.tokopedia.centralizedpromoold.domain.usecase

import com.tokopedia.centralizedpromoold.domain.model.MerchantPromotionGetPromoListResponseOld
import com.tokopedia.centralizedpromoold.domain.usecase.VoucherCashbackEligibleGqlQuery
import com.tokopedia.centralizedpromoold.domain.mapper.MerchantPromotionListEligibleMapperOld
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("VoucherCashbackEligibleGqlQuery", VoucherCashbackEligibleUseCase.QUERY)
class VoucherCashbackEligibleUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val mapper: MerchantPromotionListEligibleMapperOld
) : GraphqlUseCase<MerchantPromotionGetPromoListResponseOld>(repository) {

    init {
        setGraphqlQuery(VoucherCashbackEligibleGqlQuery())
        setTypeClass(MerchantPromotionGetPromoListResponseOld::class.java)
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

    companion object {
        const val QUERY = """
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
        """

        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID_KEY, shopId)
            }
        }
    }
}