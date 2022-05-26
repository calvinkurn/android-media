package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmVoucherValidationPartialResponse
import javax.inject.Inject

class TmKuponCreateValidateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmVoucherValidationPartialResponse>(graphqlRepository) {

    @GqlQuery("TmPartialValidateKupon", KUPON_PRE_VALIDATE)
    fun getPartialValidateData(
        success: (TmVoucherValidationPartialResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        tmCouponValidateRequest: TmCouponValidateRequest,
    ) {
        this.setTypeClass(TmVoucherValidationPartialResponse::class.java)
        this.setRequestParams(getRequestParams(tmCouponValidateRequest))
        this.setGraphqlQuery(TmPartialValidateKupon.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(tmCouponValidateRequest: TmCouponValidateRequest): Map<String, Any> {
        return mapOf(INPUT to tmCouponValidateRequest)
    }

}

const val INPUT = "VoucherValidationPartialInput"

const val KUPON_PRE_VALIDATE = """
query VoucherValidationPartial(${'$'}VoucherValidationPartialInput: VoucherValidationPartialRequest!){
      VoucherValidationPartial(VoucherValidationPartialInput: ${'$'}VoucherValidationPartialInput){
    header{
      process_time
      messages
      reason
      error_code
    } 
    data{
      validation_error{
        benefit_idr
        benefit_max
        benefit_percent
        benefit_type
        code
        coupon_name
        coupon_type
        date_end
        date_start
        hour_end
        hour_start
        image
        image_square
        is_public
        min_purchase
        quota
        minimum_tier_level
      }
    }
  }
}
"""