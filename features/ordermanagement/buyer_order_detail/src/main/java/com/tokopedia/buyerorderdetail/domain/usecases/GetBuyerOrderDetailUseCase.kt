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
        return mapper.mapDomainModelToUiModel(useCase.executeOnBackground().buyerOrderDetail)
    }

    private fun createRequestParam(params: GetBuyerOrderDetailParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    companion object {
        private const val PARAM_INPUT = "input"

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
              }
            }
        """.trimIndent()
    }
}