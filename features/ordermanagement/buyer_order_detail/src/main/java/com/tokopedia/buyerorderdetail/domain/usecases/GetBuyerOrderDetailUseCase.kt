package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.GetBuyerOrderDetailMapper
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBuyerOrderDetailUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<GetBuyerOrderDetailResponse.Data>,
        private val mapper: GetBuyerOrderDetailMapper
) {

    init {
        useCase.setTypeClass(GetBuyerOrderDetailResponse.Data::class.java)
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(params: GetBuyerOrderDetailParams): BuyerOrderDetailUiModel {
        useCase.setRequestParams(createRequestParam(params))
        // TODO: Use actual data
        val dummyData = useCase.executeOnBackground().buyerOrderDetail.copy(
                bundleDetail = getDummyBundleDetail()
        )
        return mapper.mapDomainModelToUiModel(dummyData)
//        return mapper.mapDomainModelToUiModel(useCase.executeOnBackground().buyerOrderDetail)
    }

    private fun createRequestParam(params: GetBuyerOrderDetailParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    private fun getDummyBundleDetail(): GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail {
        return GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail(
                bundleList = listOf(
                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail.Bundle(
                                bundleName = "Paket Tahun Baru",
                                bundlePrice = 150000.0,
                                bundleQty = 1,
                                bundleSubtotalPrice = 150000.0,
                                orderDetailList = listOf(
                                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail.Bundle.OrderDetail(
                                                productName = "iPhone XR Garansi Resmi Apple indonesia IBOX - 256 GB, Black",
                                                quantity = 2,
                                                productPrice = 8399000.0,
                                                subtotalPrice = 0.0,
                                                notes = "43 Size. Packing rapi plis.",
                                                thumbnail = "https://i.pinimg.com/originals/84/d8/38/84d838c22a08bb9356d49a028a275ba0.jpg"
                                        ),
                                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail.Bundle.OrderDetail(
                                                productName = "Case iPhone Xr - Original Ring Fusion Kit - Clear",
                                                quantity = 2,
                                                productPrice = 95000.0,
                                                subtotalPrice = 0.0,
                                                notes = "43 Size. Packing rapi plis.",
                                                thumbnail = "https://i.pinimg.com/originals/84/d8/38/84d838c22a08bb9356d49a028a275ba0.jpg"
                                        ),
                                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail.Bundle.OrderDetail(
                                                productName = "Xiaomi Powerbank 10000mAh Type-C (Garansi Resmi) Indonesia",
                                                quantity = 1,
                                                productPrice = 150000.0,
                                                subtotalPrice = 0.0,
                                                notes = "43 Size. Packing rapi plis.",
                                                thumbnail = "https://i.pinimg.com/originals/84/d8/38/84d838c22a08bb9356d49a028a275ba0.jpg"
                                        )
                                )
                        )
                )
        )
    }

    companion object {
        private const val PARAM_INPUT = "input"

        // Use This for correct query for product bundling
        private val DESIGNATED_QUERY = """
            query MPBOMDetail(${'$'}input: BomDetailV2Request!) {
              mp_bom_detail(input: ${'$'}input) {
                order_id
                invoice
                invoice_url
                payment_date
                cashback_info
                order_status {
                  id
                  status_name
                  indicator_color
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
                products {
                  product_id
                  order_detail_id
                  product_name
                  thumbnail
                  price
                  price_text
                  quantity
                  total_price
                  total_price_text
                  notes
                  category
                  category_id
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
                }
                have_product_bundle
                bundle_detail {
                  total_product
                  bundle {
                    bundle_id
                    bundle_name
                    bundle_price
                    bundle_quantity
                    bundle_subtotal_price
                    order_detail {
                      order_id
                      order_detail_id
                      product_id
                      product_name
                      quantity
                      product_price
                      subtotal_price
                      notes
                      thumbnail
                      bundle_id
                    }
                    non_bundle {
                      order_id
                      product_id
                      product_name
                      quantity
                      product_price
                      subtotal_price
                      notes
                      thumbnail
                      bundle_id
                    }
                  }
                }
                shipment {
                  shipping_name
                  shipping_product_name
                  shipping_display_name
                  shipping_ref_num
                  eta
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
              }
            }
        """.trimIndent()

        private val QUERY = """
            query MPBOMDetail(${'$'}input: BomDetailV2Request!) {
              mp_bom_detail(input: ${'$'}input) {
                order_id
                invoice
                invoice_url
                payment_date
                cashback_info
                order_status {
                  id
                  status_name
                  indicator_color
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
                products {
                  product_id
                  order_detail_id
                  product_name
                  thumbnail
                  price
                  price_text
                  quantity
                  total_price
                  total_price_text
                  notes
                  category
                  category_id
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
                }
                shipment {
                  shipping_name
                  shipping_product_name
                  shipping_display_name
                  shipping_ref_num
                  eta
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
              }
            }
        """.trimIndent()
    }
}