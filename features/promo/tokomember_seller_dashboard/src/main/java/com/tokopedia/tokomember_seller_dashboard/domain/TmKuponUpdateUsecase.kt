package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponUpdateRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmKuponCreateMVResponse
import javax.inject.Inject

class TmKuponUpdateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmKuponCreateMVResponse>(graphqlRepository) {

    @GqlQuery("TmKuponCreate", KUPON_UPDATE)
    fun updateCoupon(
        success: (TmKuponCreateMVResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        tmCouponUpdateRequest: TmCouponUpdateRequest,
    ) {
        this.setTypeClass(TmKuponCreateMVResponse::class.java)
        this.setRequestParams(getRequestParams(tmCouponUpdateRequest))
        this.setGraphqlQuery(TmKuponCreate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(tmCouponUpdateRequest: TmCouponUpdateRequest): Map<String, Any> {
        return mapOf("merchantVoucherUpdateData" to tmCouponUpdateRequest)
    }

}

const val KUPON_UPDATE = """
     mutation merchantPromotionUpdateMV(${'$'}merchantVoucherUpdateData: TmCouponUpdateRequest!) {
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