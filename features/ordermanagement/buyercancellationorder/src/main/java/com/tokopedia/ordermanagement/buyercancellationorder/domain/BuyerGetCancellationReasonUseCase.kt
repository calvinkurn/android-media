package com.tokopedia.ordermanagement.buyercancellationorder.domain

import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.PARAM_INPUT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerGetCancellationReasonUseCase @Inject constructor(private val useCase: GraphqlUseCase<BuyerGetCancellationReasonData.Data>) {

    suspend fun execute(getCancellationReasonParam: BuyerGetCancellationReasonParam): Result<BuyerGetCancellationReasonData.Data> {
        useCase.setGraphqlQuery(getQuery())
        useCase.setTypeClass(BuyerGetCancellationReasonData.Data::class.java)
        useCase.setRequestParams(generateParam(getCancellationReasonParam))

        return try {
            val cancellationReason = useCase.executeOnBackground()
            Success(cancellationReason)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(cancellationReasonParam: BuyerGetCancellationReasonParam): Map<String, BuyerGetCancellationReasonParam> {
        return mapOf(PARAM_INPUT to cancellationReasonParam)
    }

    private fun getQuery(): String {
        return """
            query GETCANCELREASON(${'$'}input :CancelReasonRequest!) {
              get_cancellation_reason(input: ${'$'}input) {
                is_requested_cancel,
                is_requested_cancel_available,
                is_eligible_instant_cancel,
                is_show_ticker,
                cancellation_min_time,
                cancellation_notes,
                ticker_info{
                    text,
                    type,
                    action_text,
                    action_key,
                    action_url
                },
                reasons{
                  title,
                  question,
                  sub_reasons{
                    r_code,
                    reason
                  }
                },
                have_product_bundle,
                bundle_detail {
                  total_product,
                  product_bundling_icon,
                  bundle {
                    bundle_name,
                    order_detail {
                      product_id,
                      product_name,
                      product_price,
                      picture
                    }
                  },
                  non_bundle {
                    product_id,
                    product_name,
                    product_price,
                    picture
                  }
                },
                order_details{
                    product_id,
                    product_name,
                    product_price,
                    picture,
                    bundle_id,
                    bundle_variant_id
                }
              }
            }
        """.trimIndent()
    }
}