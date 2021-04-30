package com.tokopedia.buyerorderdetail.domain.usecases

import com.google.gson.Gson
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
        return mapper.mapDomainModelToUiModel(Gson().fromJson(FAKE_RESPONSE, GetBuyerOrderDetailResponse.Data::class.java).buyerOrderDetail)
    }

    private fun createRequestParam(params: GetBuyerOrderDetailParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    companion object {
        private const val PARAM_INPUT = "input"

        private const val QUERY = """
            query MPBOMDetail(${'$'}input:MPOrderDetailArgs!) {
             mpBOMDetail(input:${'$'}input) {
                order_id
                order_finished_time
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
                  action_text
                  action_key
                  action_url
                  type
                }
                deadline {
                  label
                  value
                  color
                }
                shop {
                  shop_id
                  shop_name
                  shop_url
                }
                products [{
                  order_detail_id
                  product_id
                  product_name
                  product_url
                  snapshot_url
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
                    color
                    style
                    url
                    popup {
                      title
                      body
                      action_button {
                        key
                        display_name
                        color
                        style
                        url
                      }
                    }
                  }
                }]
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
                    title
                    notes
                    url_detail
                    url_text
                  }
                }
                payment {
                  payment_method {
                    label
                    value
                  }
                  payment_details [{
                    label
                    value
                  }]
                  payment_amount {
                    label
                    value
                  }
                }
                button {
                  key
                  display_name
                  color
                  style
                  url
                  popup {
                    title
                    body
                    action_button {
                      key
                      display_name
                      color
                      style
                      url
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
                      style
                      url
                    }
                  }
                }
                meta {
                  is_os
                  is_pm
                  is_bebas_ongkir
                  bo_image_url
                }
              }
            }
        """

        private const val FAKE_RESPONSE = """
            {
              "mpBOMDetail": {
                "order_id": 675721450,
                "order_finished_time": 132301300,
                "invoice": "INV/20210104/XXI/I/717282191",
                "invoice_url": "https://staging.tokopedia.com/invoice.pl?id=166515916&pdf=Invoice-8969212-479933-20210204144519-cnJycnJycnIx",
                "payment_date": "4 Feb 2021, 14:45 WIB",
                "cashback_info": "Cashback Rp30.000 (30.000 OVO Points)",
                "order_status": {
                  "id": 220,
                  "status_name": "Menunggu Konfirmasi",
                  "indicator_color": "#FFC400"
                },
                "ticker_info": {
                  "text": "",
                  "action_text": "",
                  "action_key": "",
                  "action_url": "",
                  "type": ""
                },
                "deadline": {
                  "label": "Batal Otomatis",
                  "value": "2 Jam",
                  "color": "#FF8800"
                },
                "shop": {
                  "shop_id": 6551490,
                  "shop_name": "stagingaj",
                  "shop_url": "https://staging.tokopedia.com/stagingaj"
                },
                "products": [
                  {
                    "order_detail_id": 324092389,
                    "product_id": 2147534898,
                    "product_name": "baju 567",
                    "product_url": "https://staging.tokopedia.com/stagingaj/baju-567",
                    "snapshot_url": "https://staging.tokopedia.com/snapshot_product?order_id=166806597&dtl_id=20518952&source=bom",
                    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/hDjmkQ/2021/4/8/6188a16d-f725-4017-ba36-109a6d45037c.jpg",
                    "price": 1000,
                    "price_text": "Rp 1.000",
                    "quantity": 2,
                    "total_price": 2000,
                    "total_price_text": "Rp 2.000",
                    "notes": "warna hitam",
                    "button": {}
                  }
                ],
                "shipment": {
                  "shipping_name": "Gojek",
                  "shipping_product_name": "SameDay",
                  "shipping_display_name": "Gojek - SameDay",
                  "shipping_ref_num": "GK-20913829",
                  "eta": "Estimasi tiba 9 - 11 Apr",
                  "is_bebas_ongkir": true,
                  "receiver": {
                    "name": "ismaa",
                    "phone": "6287887725577",
                    "street": "jalan jalan kecamatan grogol",
                    "postal": "11440",
                    "district": "Grogol",
                    "city": "Kota Administrasi Jakarta Barat",
                    "province": "DKI Jakarta"
                  },
                  "driver": {
                    "name": "pak budi",
                    "phone": "082348342434",
                    "photo_url": "",
                    "license_number": "A 123 BC"
                  },
                  "shipping_info": {
                    "title": "Jaminan Tepat Waktu",
                    "notes": "Ongkir kembali jika pesanan tiba lebih dari <b>04 Feb 2021</b> 18:00 WIB.",
                    "url_detail": "https://gw-staging.tokopedia.com/rates/otdg_info",
                    "url_text": "S&K Berlaku"
                  }
                },
                "payment": {
                  "payment_method": {
                    "label": "Metode Pembayaran",
                    "value": "Saldo"
                  },
                  "payment_details": [
                    {
                      "label": "Total Harga (1 barang)",
                      "value": "Rp 22.000"
                    },
                    {
                      "label": "Total Ongkos Kirim (500 gr)",
                      "value": "Rp 14.000"
                    }
                  ],
                  "payment_amount": {
                    "label": "Total Bayar",
                    "value": "Rp 36.000"
                  }
                },
                "button": {
                  "key": "track",
                  "display_name": "Lacak",
                  "color": "#03AC0E",
                  "style": "filled",
                  "url": "https://staging.tokopedia.com/ajax/logistic/v1/tracking,1,JNE18273,1617904754,0",
                  "popup": {
                    "title": "",
                    "body": "",
                    "action_button": []
                  }
                },
                "dot_menu": [
                  {
                    "key": "ask_seller",
                    "display_name": "Chat Penjual",
                    "url": "",
                    "popup": {
                      "title": "",
                      "body": "",
                      "action_button": []
                    }
                  },
                  {
                    "key": "help",
                    "display_name": "Bantuan",
                    "url": "",
                    "popup": {
                      "title": "",
                      "body": "",
                      "action_button": []
                    }
                  },
                  {
                    "key": "request_cancel",
                    "display_name": "Batalkan Pesanan",
                    "url": "",
                    "popup": {
                      "title": "",
                      "body": "",
                      "action_button": []
                    }
                  }
                ],
                "meta": {
                  "is_os": false,
                  "is_pm": true,
                  "is_bebas_ongkir": true,
                  "bo_image_url": ""
                }
              }
            }
        """
    }
}