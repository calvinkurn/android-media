package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LANG_ID
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_LANG
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_ORDERID
import com.tokopedia.sellerorder.detail.data.model.GetSomDetailResponse
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceRequest
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceResponse
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class SomGetOrderDetailUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
) {
    private fun createParamDynamicPrice(orderId: String): Map<String, SomDynamicPriceRequest> {
        return mapOf(SomConsts.PARAM_INPUT to SomDynamicPriceRequest(order_id = orderId.toLongOrZero()))
    }

    private fun createParamGetOrderDetail(orderId: String): Map<String, String> {
        return mapOf(VAR_PARAM_ORDERID to orderId, VAR_PARAM_LANG to PARAM_LANG_ID)
    }

    suspend fun execute(orderId: String): GetSomDetailResponse {
        val getSomDetailResponse = GetSomDetailResponse()
        val somDynamicPriceParams = createParamDynamicPrice(orderId)
        val somDetailRequestParam = createParamGetOrderDetail(orderId)

        val somDetailRequest = GraphqlRequest(QUERY_SOM_DETAIL, SomDetailOrder.Data::class.java, somDetailRequestParam)
        val somDynamicPriceRequest = GraphqlRequest(QUERY_DYNAMIC_PRICE, SomDynamicPriceResponse::class.java, somDynamicPriceParams)

        val multipleRequest = mutableListOf(somDetailRequest, somDynamicPriceRequest)

        return try {
            val gqlResponse = graphQlRepository.response(multipleRequest)
            getSomDetailResponse.getSomDetail = requireNotNull(gqlResponse.getData<SomDetailOrder.Data>(SomDetailOrder.Data::class.java).getSomDetail)
            getSomDetailResponse.somDynamicPriceResponse = requireNotNull(gqlResponse.getData<SomDynamicPriceResponse>(SomDynamicPriceResponse::class.java).getSomDynamicPrice)
            return getSomDetailResponse
        } catch (e: Throwable) {
            throw e
        }
    }

    companion object {
        val QUERY_SOM_DETAIL = """
            query GetSOMDetail(${'$'}orderID: String!, ${'$'}lang: String!) {
              get_som_detail(orderID: ${'$'}orderID, lang: ${'$'}lang) {
                order_id
                status
                has_reso_status
                status_text
                status_text_color
                status_indicator_color
                invoice
                invoice_url
                checkout_date
                payment_date
                notes
                customer {
                  id
                  name
                  image
                  phone
                }
                dropshipper {
                  phone
                  name
                }
                shipment {
                  id
                  name
                  product_id
                  product_name
                  courier_info
                  is_same_day
                  awb
                  awb_upload_proof_text
                  awb_text_color
                  awb_upload_url
                }
                booking_info {
                  driver {
                    name
                    phone
                    photo
                    license_number
                    tracking_url
                  }
                  pickup_point {
                    store_code
                    district_id
                    address
                    geo_location
                    store_name
                    pickup_code
                  }
                  online_booking {
                    booking_code
                    state
                    message
                    message_array
                    barcode_type
                  }
                }
                receiver {
                  name
                  phone
                  street
                  postal
                  district
                  city
                  province
                }
                deadline {
                  text
                  color
                  style
                }
                insurance {
                  type
                  name
                  note
                }
                warehouse {
                  warehouse_id
                  fulfill_by
                }
                exclusive_promo {
                  amount
                  note
                }
                buyer_request_cancel {
                  is_request_cancel
                  request_cancel_time
                  reason
                  status
                }
                flag_order_type {
                  is_order_cod
                  is_order_now
                  is_order_kelontong
                  is_order_sampai
                  is_order_trade_in
                }
                flag_order_meta {
                  is_free_shipping_campaign
                  is_topads
                  is_tokocabang
                  is_shipping_printed
                  is_broadcast_chat
                  shipment_logo
                }
                label_info {
                  flag_name
                  flag_color
                  flag_background
                }
                logistic_info {
                  all {
                    id
                    priority
                    description
                    info_text_short
                    info_text_long
                  }
                  priority {
                    id
                    priority
                    description
                    info_text_short
                    info_text_long
                  }
                  others {
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
                    template {
                        code
                        params 
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
                details {
                  bundles {
                    bundle_id
                    bundle_name
                    bundle_price
                    bundle_subtotal_price
                    order_detail {
                      order_detail_id
                      id
                      name
                      quantity
                      price_text
                      note
                      thumbnail
                      addon_summary {
                        addons {
                          order_id
                          id
                          reference_id
                          level
                          name
                          price
                          price_str
                          subtotal_price
                          subtotal_price_str
                          quantity
                          type
                          image_url
                          metadata {
                            add_on_note {
                              from
                              notes
                              to
                            }
                          }
                          create_time
                        }
                        total
                        total_price
                        total_price_str
                        total_quantity
                      }
                    }
                  }
                  non_bundles {
                    order_detail_id
                    id
                    name
                    quantity
                    price_text
                    note
                    thumbnail
                    addon_summary {
                      addons {
                        order_id
                        id
                        reference_id
                        level
                        name
                        price
                        price_str
                        subtotal_price
                        subtotal_price_str
                        quantity
                        type
                        image_url
                        metadata {
                          add_on_note {
                            from
                            notes
                            to
                          }
                        }
                        create_time
                      }
                      total
                      total_price
                      total_price_str
                      total_quantity
                    }
                  }
                  bundle_icon
                  addon_icon
                  addon_label
                }
                addon_info {
                  order_level {
                    addons {
                      order_id
                      id
                      reference_id
                      level
                      name
                      price
                      price_str
                      subtotal_price
                      subtotal_price_str
                      quantity
                      type
                      image_url
                      metadata {
                        add_on_note {
                          from
                          notes
                          to
                        }
                      }
                      create_time
                    }
                    total
                    total_price
                    total_price_str
                    total_quantity
                  }
                  label
                  icon_url
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
                }
                pricing_data{
                  label
                  value
                }
                promo_shipping {
                  label
                  value
                  value_detail
                }
                pof_data {
                  label
                  value
                  header
                  footer
               }
            }
        }
        """.trimIndent()
    }
}
