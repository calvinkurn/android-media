package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.CouponCreateMultiple
import javax.inject.Inject

class TmKuponCreateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CouponCreateMultiple>(graphqlRepository) {

    @GqlQuery("TmKuponCreate", KUPON_CREATE)
    fun createKupon(
        success: (CouponCreateMultiple) -> Unit,
        onFail: (Throwable) -> Unit,
        tmMerchantCouponUnifyRequest: TmMerchantCouponUnifyRequest,
    ) {
        this.setTypeClass(CouponCreateMultiple::class.java)
        this.setRequestParams(getRequestParams(tmMerchantCouponUnifyRequest))
        this.setGraphqlQuery(TmKuponCreate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(tmMerchantCouponUnifyRequest: TmMerchantCouponUnifyRequest): Map<String, Any> {
        return mapOf(MERCHANT_COUPON_MULTIPLE_INPUT to tmMerchantCouponUnifyRequest)
    }

}

const val MERCHANT_COUPON_MULTIPLE_INPUT = "merchantVoucherMultipleData"

const val KUPON_CREATE = """
     mutation merchantPromotionCreateMultipleMV(${'$'}merchantVoucherMultipleData: mvCreateMultipleData!) {
  merchantPromotionCreateMultipleMV(merchantVoucherMultipleData: ${'$'}merchantVoucherMultipleData) {
  status
  message
  process_time
  data{
    redirect_url
    voucher_id
    status
  }
}
}
"""