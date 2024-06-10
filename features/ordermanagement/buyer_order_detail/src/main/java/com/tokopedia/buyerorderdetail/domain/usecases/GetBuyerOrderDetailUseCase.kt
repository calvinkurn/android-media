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
import kotlinx.coroutines.flow.onCompletion
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
                order_id
                group_type
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
                  title
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
                  buttons {
                    key
                    icon
                    action_type
                    value
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
                      addon_summary {
                        addons {
                          id
                          name
                          price_str
                          quantity
                          type
                          image_url
                          metadata {
                            info_link
                            add_on_note {
                              from
                              to
                              notes
                              short_notes
                              tips
                            }
                          }
                          create_time
                        }
                        total_price_str
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
                        id
                        name
                        price_str
                        quantity
                        type
                        image_url
                        metadata {
                          info_link
                          add_on_note {
                            from
                            to
                            notes
                            short_notes
                            tips
                          }
                        }
                        create_time
                      }
                      total_price_str
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
                            id
                            name
                            price_str
                            quantity
                            type
                            image_url
                            metadata {
                              info_link
                              add_on_note {
                                from
                                to
                                notes
                                short_notes
                                tips
                              }
                            }
                            create_time
                          }
                          total_price_str
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
                  bmgm_icon
                  bmgms {
                    id
                    bmgm_tier_name
                    total_price_note
                    tier_discount_amount
                    tier_discount_amount_formatted
                    price_before_benefit
                    price_before_benefit_formatted
                    price_after_benefit
                    price_after_benefit_formatted
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
                      addon_summary {
                        total_price_str
                        addons {
                          id
                          name
                          price_str
                          quantity
                          type
                          image_url
                          metadata {
                            info_link
                            add_on_note {
                              is_custom_note
                              from
                              to
                              notes
                              short_notes
                              tips
                            }
                          }
                        }
                      }
                      flags {
                        is_ppp
                      }
                    }
                    product_benefit {
                      label
                      icon_url
                      order_detail {
                        order_detail_id
                        product_id
                        product_name
                        product_url
                        thumbnail
                        quantity
                        total_price_text
                      }
                    }
                  }
                }
                addon_info {
                  label
                  icon_url
                  order_level {
                    addons {
                      id
                      name
                      price_str
                      quantity
                      type
                      image_url
                      metadata {
                        info_link
                        add_on_note {
                          from
                          to
                          notes
                          short_notes
                          tips
                        }
                      }
                    }
                    total_price_str
                  }
                }
                additional_data {
                  group_order_data {
                    tx_id
                    icon_url
                    title
                    description
                  }
                  plus_savings {
                    ticker {
                      left
                      right
                      image_url
                    }
                    components {
                      details {
                        label
                        value
                        image_url
                      }
                      footer {
                        total {
                          label
                          value
                        }
                      }
                    }
                  }
                  epharmacy_data {
                    consultation_name
                    consultation_date
                    consultation_doctor_name
                    consultation_prescription_number
                    consultation_expiry_date
                    consultation_patient_name
                  }
                }
                is_plus
                is_pof
                widget {
                  reso_status {
                    show
                  }
                  reso_csat {
                    show
                    help_url
                  }
                  ppp {
                    show
                  }
                }
              }
            }
        """
    }
}
