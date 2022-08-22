package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponUpdateRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmKuponUpdateMVResponse
import javax.inject.Inject

class TmKuponUpdateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmKuponUpdateMVResponse>(graphqlRepository) {

    @GqlQuery("TmKuponUpdate", KUPON_UPDATE)
    fun updateCoupon(
        success: (TmKuponUpdateMVResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        tmCouponUpdateRequest: TmCouponUpdateRequest,
    ) {
        this.setTypeClass(TmKuponUpdateMVResponse::class.java)
        this.setRequestParams(getRequestParams(tmCouponUpdateRequest))
        this.setGraphqlQuery(TmKuponUpdate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(tmCouponUpdateRequest: TmCouponUpdateRequest): Map<String, Any> {
        return mapOf( MERCHANT_VOUCHER_UPDATE to tmCouponUpdateRequest)
    }

}

const val MERCHANT_VOUCHER_UPDATE = "merchantVoucherUpdateData"

const val KUPON_UPDATE = """
     mutation merchantPromotionUpdateMV(${'$'}merchantVoucherUpdateData: mvUpdateData!) {
  merchantPromotionUpdateMV(merchantVoucherUpdateData: ${'$'}merchantVoucherUpdateData) {
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