package com.tokopedia.mvcwidget.usecases

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mvcwidget.data.entity.PromoCatalogResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetPromoBenefitBottomSheetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, PromoCatalogResponse>(dispatcher.io) {

    override suspend fun execute(params: String): PromoCatalogResponse {
        val param = mapOf("metaData" to params)
        delay(500)
//        return Gson().fromJson(responseSampleJson, PromoCatalogResponse::class.java)
        return repository.request(graphqlQuery(), param)
    }

    override fun graphqlQuery(): String = """
        query getPromoBenefit(${'$'}metaData: String!){
          promoCatalogGetPDEBottomSheet(source: "pdp", jsonMetadata: ${'$'}metaData) {
            resultStatus {
              code
              message
              status
            }
            resultList {
              productID
              widgetList {
                id
                widgetType
                componentList {
                  id
                  componentType
                  attributeList {
                    type
                    value
                  }
                }
              }
            }
          }
        }
    """.trimIndent()

    private val responseSampleJson = """
        {
    "promoCatalogGetPDEBottomSheet": {
      "resultStatus": {
        "code": "200",
        "message": [
          "Success"
        ],
        "status": "OK"
      },
      "resultList": [
        {
          "productID": 1000,
          "widgetList": [
            {
              "id": "1",
              "widgetType": "pdp_bottomsheet",
              "componentList": [
                {
                  "id": 1,
                  "componentType": "pdp_bs_final_price",
                  "attributeList": [
                    {
                      "type": "text_title_value",
                      "value": "Perkiraan harga hemat"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_title_format",
                      "value": "normal"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp9.000.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 2,
                  "componentType": "pdp_bs_nett_price",
                  "attributeList": [
                    {
                      "type": "text_title_value",
                      "value": "Harga tanpa promo"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_title_format",
                      "value": "normal"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp9.500.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 3,
                  "componentType": "pdp_bs_benefit_cashback",
                  "attributeList": [
                    {
                      "type": "icon_url",
                      "value": "https://images.tokopedia.net/img/gopay_coins.png"
                    },
                    {
                      "type": "text_title_value",
                      "value": "Cashback GoPay Coins"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#0094CF"
                    },
                    {
                      "type": "text_title_format",
                      "value": "bold"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp300.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 4,
                  "componentType": "pdp_bs_benefit_discount",
                  "attributeList": [
                    {
                      "type": "icon_url",
                      "value": "https://images.tokopedia.net/img/discount_icon.png"
                    },
                    {
                      "type": "text_title_value",
                      "value": "Diskon"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#FF7F17"
                    },
                    {
                      "type": "text_title_format",
                      "value": "bold"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp200.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 5,
                  "componentType": "pdp_bs_benefit_tnc",
                  "attributeList": [
                    {
                      "type": "text_html",
                      "value": "<ul><li>Nominal promo bisa berubah dikarenakan waktu pembelian, ketersediaan produk, periode promosi, ketentuan promo.</li><li>Harga akhir akan ditampilkan di halaman \"Pengiriman / Checkout\". Perhatikan sebelum mengkonfirmasi pesanan.</li></ul>"
                    },
                    {
                      "type": "text_color",
                      "value": "#6D7588"
                    }
                  ]
                },
                {
                  "id": 6,
                  "componentType": "pdp_bs_background",
                  "attributeList": [
                    {
                      "type": "background_color",
                      "value": "#FFF5F6"
                    },
                    {
                      "type": "background_image",
                      "value": "https://images.tokopedia.net/img/bs_background_regular.png"
                    }
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
  }
    """.trimIndent()

}

val metaDataSample = """
        {"request_list":[{"product_id":1,"is_low_benefit":false,"additional_data":[{"field":"background_color","value":"#FFF5F6"},{"field":"background_image","value":"https://images.tokopedia.net/img/bs_background_regular.png"},{"field":"nett_price","value":"9000000"},{"field":"price","value":"9500000"},{"field":"benefit_cashback","value":"300000"},{"field":"benefit_cashback_currency","value":"GoPay Coins"},{"field":"benefit_discount","value":"200000"}]}]}
    """.trimIndent()
