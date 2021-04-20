package com.tokopedia.sellerorder.detail.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LANG_ID
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_LANG
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_ORDERID
import com.tokopedia.sellerorder.detail.data.model.GetSomDetailResponse
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceRequest
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class SomGetOrderDetailUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
) {

    private var params: RequestParams = RequestParams.EMPTY

    fun setParamDynamicPrice(param: SomDynamicPriceRequest) {
        params = RequestParams.create().apply {
            putObject(SomConsts.PARAM_INPUT, param)
        }
    }

    suspend fun execute(orderId: String): Result<GetSomDetailResponse> {

        val getSomDetailResponse = GetSomDetailResponse()
        val dynamicPriceParam = params.getObject(SomConsts.PARAM_INPUT) as SomDynamicPriceRequest
        val somDynamicPriceParams = mapOf(SomConsts.PARAM_INPUT to dynamicPriceParam)

        val somDetailRequest = GraphqlRequest(QUERY_SOM_DETAIL, SomDetailOrder.Data::class.java, generateParam(orderId))
        val somDynamicPriceRequest = GraphqlRequest(QUERY_DYNAMIC_PRICE, SomDynamicPriceResponse::class.java, somDynamicPriceParams)

        val multipleRequest = mutableListOf(somDetailRequest, somDynamicPriceRequest)

        return try {
            val gqlResponse = graphQlRepository.getReseponse(multipleRequest)
            getSomDetailResponse.getSomDetail = requireNotNull(gqlResponse.getData<SomDetailOrder.Data>(SomDetailOrder.Data::class.java).getSomDetail)
            getSomDetailResponse.somDynamicPriceResponse = requireNotNull(gqlResponse.getData<SomDynamicPriceResponse>(SomDynamicPriceResponse::class.java).getSomDynamicPrice)
            Success(getSomDetailResponse)
        } catch (e: Throwable) {
            Fail(e)
        }
    }

    private fun generateParam(orderId: String): HashMap<String, Any> {
        return hashMapOf(VAR_PARAM_ORDERID to orderId, VAR_PARAM_LANG to PARAM_LANG_ID)
    }

    companion object {
        val QUERY_SOM_DETAIL = """
            query GetSOMDetail(${'$'}orderID: String!, ${'$'}lang: String!){
              get_som_detail(orderID: ${'$'}orderID, lang: ${'$'}lang){
                order_id
                status
                status_text
                status_text_color
                status_indicator_color
                invoice
                invoice_url
                checkout_date
                payment_date
                notes
                products{
                  id
                  order_detail_id
                  name
                  product_url
                  snapshot_url
                  currency_type
                  currency_rate
                  thumbnail
                  price
                  price_text
                  weight
                  weight_text
                  quantity
                  note
                  free_return
                  free_return_message
                  purchase_protection_fee
                  purchase_protection_fee_text
                  purchase_protection_quantity
                }
                customer{
                  id
                  name
                  image
                  phone
                }
                dropshipper{
                  phone
                  name
                }
                shipment{
                  id
                  name
                  product_id
                  product_name
                  is_same_day
                  awb
                  awb_upload_proof_text
                  awb_text_color
                  awb_upload_url
                }
                shipment_change{
                  id
                  name
                  product_id
                  product_name
                  is_same_day
                  awb
                }
                booking_info{
                  driver{
                    name
                    phone
                    photo
                    license_number
                    tracking_url
                  }
                  pickup_point{
                    store_code
                    district_id
                    address
                    geo_location
                    store_name
                    pickup_code
                  }
                  online_booking{
                    booking_code
                    state
                    message
                    message_array
                    barcode_type
                  }
                }
                receiver{
                  name
                  phone
                  street
                  postal
                  district
                  city
                  province
                }
                deadline{
                  text
                  color
                }
                insurance{
                  type
                  name
                  note
                }
                warehouse{
                  warehouse_id
                  fulfill_by
                }
                exclusive_promo{
                  amount
                  note
                }
                buyer_request_cancel {
                  is_request_cancel
                  request_cancel_time
                  reason
                  status
                }
                flag_order_type{
                  is_order_cod
                  is_order_now
                  is_order_kelontong
                  is_order_sampai
                  is_order_trade_in
                }
                flag_order_meta{
                    is_free_shipping_campaign
                    is_topads
                    is_tokocabang
                    is_shipping_printed
                }
                label_info{
                  flag_name
                  flag_color
                  flag_background
                }
                logistic_info{
                  all{
                    id
                    priority
                    description
                    info_text_short
                    info_text_long
                  }
                   priority{
                    id
                    priority
                    description
                    info_text_short
                    info_text_long
                  }
                   others{
                    id
                    priority
                    description
                    info_text_short
                    info_text_long
                  }
                }
                button {
                  key
                  display_name
                  priority
                  color
                  type
                  bulk_url
                  bulk_method
                  url
                  method
                  complexity
                  title
                  content
                  param
                  popup {
                    title
                    body
                    actionButton {
                      displayName
                      color
                      type
                    }
                  }
                }
                online_booking {
                  is_hide_input_awb
                  is_remove_input_awb
                  is_show_info
                  info_text
                }
                penalty_reject_info {
                  is_penalty_reject
                  penalty_reject_wording
                }
                ticker_info {
                  text
                  type
                  action_text
                  action_key
                  action_url
                }
              }
            }
        """.trimIndent()

        val QUERY_DYNAMIC_PRICE = """
        query GetSOMDynamicPrice(${'$'}input: SOMDynamicPriceRequest!) {
              get_som_dynamic_price(input: ${'$'}input) {
                payment_method {
                  label
                  value
                }
                payment_data {
                  label
                  value
                  text_color
                }
                pricing_data{
                  label
                  value
                  text_color
                }
            }
        }
        """.trimIndent()
    }
}