package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponCreateRequest
import com.tokopedia.tokomember_seller_dashboard.model.CouponCreateSingle
import javax.inject.Inject

class TmSingleCouponCreateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CouponCreateSingle>(graphqlRepository) {

    @GqlQuery("TmSingleCouponCreate", SINGLE_COUPON_CREATE)
    fun createSingleCoupon(
        success: (CouponCreateSingle) -> Unit,
        onFail: (Throwable) -> Unit,
        tmMerchantCouponUnifyRequest: TmCouponCreateRequest,
    ) {
        this.setTypeClass(CouponCreateSingle::class.java)
        this.setRequestParams(getRequestParams(tmMerchantCouponUnifyRequest))
        this.setGraphqlQuery(TmSingleCouponCreate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(tmMerchantCouponUnifyRequest: TmCouponCreateRequest): Map<String, Any> {
        return mapOf(MERCHANT_COUPON_SINGLE_INPUT to tmMerchantCouponUnifyRequest)
    }

}

const val MERCHANT_COUPON_SINGLE_INPUT = "merchantVoucherData"

const val SINGLE_COUPON_CREATE = """
     mutation merchantPromotionCreateMV(${'$'}merchantVoucherData: mvCreateData!) {
  merchantPromotionCreateMV(merchantVoucherData: ${'$'}merchantVoucherData) {
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