package com.tokopedia.ordermanagement.snapshot.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.ordermanagement.snapshot.data.model.Data
import com.tokopedia.ordermanagement.snapshot.data.model.GetOrderSnapshot
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotParam
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.PARAM_INPUT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 1/25/21.
 */
class SnapshotUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(param: SnapshotParam): Result<GetOrderSnapshot> {
        return try {
            val request = GraphqlRequest(QUERY, Data::class.java, generateParam(param))
            val response = gqlRepository.getReseponse(listOf(request)).getSuccessData<Data>()
            Success(response.getOrderSnapshot)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: SnapshotParam): Map<String, Any?> {
        return mapOf(PARAM_INPUT to param)
    }

    companion object {
        val QUERY = """
            query GetOrderSnapshot(${'$'}input:OrderSnapshotRequest!){   
                get_order_snapshot(input:${'$'}input) {
                  pre_order
                  shop_image_primary_url
                  product_url
                  product_price_formatted
                  product_total_price_formatted
                  product_weight_formatted
                  product_total_weight_formatted
                  shop_summary{
                    user_id
                    shop_name
                    shop_id
                    shop_domain
                    logo
                  }
                  product_additional_data{
                    create_time
                    product_price
                  }
                  product_image_secondary{
                    image_url
                    file_name
                    file_path
                  }
                  order_detail{
                    order_dtl_id
                    order_id
                    product_id
                    product_name
                    product_desc
                    quantity
                    quantity_deliver
                    quantity_reject
                    product_price
                    product_weight
                    total_weight
                    subtotal_price
                    notes
                    finsurance
                    returnable
                    child_cat_id
                    currency_id
                    insurance_price
                    normal_price
                    create_time
                    min_order
                    must_insurance
                    condition
                  }
                  pre_order_duration
                  is_os
                  is_pm
                  campaign_data{
                    product_id
                    campaign{
                      discounted_price
                      discount_percentage_text
                      discount_percentage_text_color
                      discount_percentage_label_color
                      original_price
                      original_price_fmt
            
                    }
                  }
                }
            }
        """.trimIndent()
    }
}