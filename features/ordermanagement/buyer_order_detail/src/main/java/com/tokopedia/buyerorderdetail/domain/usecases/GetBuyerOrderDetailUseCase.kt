package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBuyerOrderDetailUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : BaseGraphqlUseCase<GetBuyerOrderDetailParams, GetBuyerOrderDetailRequestState>(dispatchers) {

    override fun graphqlQuery() = QUERY

    override suspend fun execute(params: GetBuyerOrderDetailParams) = flow {
        emit(GetBuyerOrderDetailRequestState.Requesting)
        emit(GetBuyerOrderDetailRequestState.Complete.Success(sendRequest(params).buyerOrderDetail))
    }.catch {
        emit(GetBuyerOrderDetailRequestState.Complete.Error(it))
    }

    private fun createRequestParam(params: GetBuyerOrderDetailParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    private suspend fun sendRequest(
        params: GetBuyerOrderDetailParams
    ): GetBuyerOrderDetailResponse.Data {
        return repository.request(
            graphqlQuery(),
            createRequestParam(params),
            getCacheStrategy(params.shouldCheckCache)
        )
    }

    companion object {
        private const val PARAM_INPUT = "input"

        const val QUERY = """
            query MPBOMDetail(${'$'}$PARAM_INPUT: BomDetailV2Request!) {
              mp_bom_detail(input: ${'$'}$PARAM_INPUT) {
                has_reso_status
                order_id
                invoice
                invoice_url
                payment_date
                cashback_info
                ads_page_name
                order_status {
                  id
                  status_name
                  indicator_color
                  labels {
                    label
                  }
                }
                ticker_info {
                  text
                  type
                  action_text
                  action_key
                  action_url
                }
                preorder {
                  is_preorder
                  label
                  value
                }
                deadline {
                  label
                  value
                  color
                }
                shop {
                  shop_id
                  shop_name
                  shop_type
                  badge_url
                }
                shipment {
                  shipping_name
                  shipping_product_name
                  shipping_display_name
                  shipping_ref_num
                  eta
                  eta_is_updated
                  user_updated_info
                  receiver {
                    name
                    phone
                    street
                    postal
                    district
                    city
                    province
                  }
                  driver {
                    name
                    phone
                    photo_url
                    license_number
                  }
                  shipping_info {
                    text
                    type
                    action_key
                    action_url
                    action_text
                  }
                }
                payment {
                  payment_method {
                    label
                    value
                  }
                  payment_details {
                    label
                    value
                  }
                  payment_amount {
                    label
                    value
                  }
                  payment_refund {
                    summary_info {
                      details {
                        label
                        value
                      }
                      total_amount {
                        label
                        value
                      }
                      footer
                    }
                    estimate_info {
                      title
                      info
                    }
                    total_amount {
                      label
                      value
                    }
                    is_refunded
                  }
                }
                button {
                  key
                  display_name
                  type
                  variant
                  url
                  popup {
                    title
                    body
                    action_button {
                      key
                      display_name
                      color
                      type
                      uri_type
                      uri
                    }
                  }
                }
                dot_menus {
                  key
                  display_name
                  url
                  popup {
                    title
                    body
                    action_button {
                      key
                      display_name
                      color
                      type
                      uri_type
                      uri
                    }
                  }
                }
                meta {
                  is_bo
                  bo_image_url
                }
                dropship {
                  name
                  phone_number
                }
                logistic_section_info {
                  index
                  id
                  image_link
                  title
                  subtitle
                  action {
                    name
                    link
                  }
                }
                details {
                  total_products
                  bundle_icon
                  addon_icon
                  addon_label
                  bundles {
                    bundle_id
                    bundle_variant_id
                    bundle_name
                    bundle_price
                    bundle_quantity
                    bundle_subtotal_price
                    order_detail {
                      order_detail_id
                      product_id
                      product_name
                      product_url
                      thumbnail
                      price
                      price_text
                      quantity
                      total_price
                      total_price_text
                      notes
                      category_id
                      category
                      button {
                        key
                        display_name
                        type
                        variant
                        url
                        popup {
                          title
                          body
                          action_button {
                            key
                            display_name
                            color
                            type
                            uri
                          }
                        }
                      }
                    }
                  }
                  non_bundles {
                    order_detail_id
                    product_id
                    product_name
                    product_url
                    thumbnail
                    price
                    price_text
                    quantity
                    total_price
                    total_price_text
                    notes
                    category_id
                    category
                    button {
                      key
                      display_name
                      type
                      variant
                      url
                      popup {
                        title
                        body
                        action_button {
                          key
                          display_name
                          color
                          type
                          uri
                        }
                      }
                    }
                    addon_summary {
                      addons {
                        order_id
                        id
                        level
                        name
                        price_str
                        subtotal_price
                        subtotal_price_str
                        quantity
                        type
                        image_url
                        metadata {
                          add_on_note {
                            from
                            to
                            notes
                            short_notes
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
                  partial_fulfillment {
                    fulfilled {
                      header {
                        title
                        quantity
                      }
                    }
                    unfulfilled {
                      header {
                        title
                        quantity
                      }
                      details {
                        order_detail_id
                        product_id
                        product_name
                        product_url
                        thumbnail
                        price
                        price_text
                        quantity
                        total_price
                        total_price_text
                        notes
                        category_id
                        category
                        button {
                          key
                          display_name
                          type
                          variant
                          url
                          popup {
                            title
                            body
                            action_button {
                              key
                              display_name
                              color
                              type
                              uri
                            }
                          }
                        }
                        addon_summary {
                          addons {
                            order_id
                            id
                            level
                            name
                            price_str
                            subtotal_price
                            subtotal_price_str
                            quantity
                            type
                            image_url
                            metadata {
                              add_on_note {
                                from
                                to
                                notes
                                short_notes
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
                  }
                  ticker_info {
                    text
                    action_text
                    action_key
                    action_url
                    type
                  }
                }
                addon_info {
                  label
                  icon_url
                  order_level {
                    addons {
                      order_id
                      id
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
                          to
                          notes
                          short_notes
                        }
                      }
                    }
                    total
                    total_price
                    total_price_str
                    total_quantity
                  }
                }
                additional_data {
                  epharmacy_data {
                    consultation_name
                    consultation_date
                    consultation_doctor_name
                    consultation_prescription_number
                    consultation_expiry_date
                    consultation_patient_name
                  }
                }
                is_pof
                has_ppp
              }
            }
        """
    }
}
