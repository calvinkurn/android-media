package com.tokopedia.logisticcart.shipping.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import rx.Observable
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
    private val converter: ShippingDurationConverter,
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: RatesParam): Observable<ShippingRecommendationData> {
        val query = ratesQuery()
        val gqlRequest = GraphqlRequest(
            query,
            RatesGqlResponse::class.java,
            mapOf(
                "param" to param.toMap(),
                "metadata" to param.toMetadata()
            )
        )
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return Observable.just(
            """
{
  "ratesV3": {
    "ratesv3": {
      "id": ":1613:1616:2150",
      "rates_id": "1681285140211032374",
      "type": "rates",
      "error": {
        "error_id": "",
        "error_message": ""
      },
      "services": [
        {
          "service_name": "Instan",
          "service_id": 1000,
          "service_order": 0,
          "status": 200,
          "is_promo": 0,
          "ui_rates_hidden": false,
          "selected_shipper_product_id": 0,
          "range_price": {
            "min_price": 24000,
            "max_price": 24000
          },
          "etd": {
            "min_etd": 0,
            "max_etd": 0
          },
          "texts": {
            "text_range_price": "Rp24.000",
            "text_etd": "Maks. 3 jam",
            "text_notes": "",
            "text_service_desc": "",
            "text_eta_summarize": "Tiba dalam 2 jam",
            "error_code": 0
          },
          "features": {
            "dynamic_price": {
              "text_label": ""
            }
          },
          "order_priority": {
            "is_now": false,
            "inactive_message": "",
            "price": 0,
            "formatted_price": "",
            "static_messages": {
              "duration_message": "",
              "checkbox_message": "",
              "warningbox_message": "",
              "fee_message": "",
              "pdp_message": ""
            }
          },
          "error": {
            "error_id": "",
            "error_message": ""
          },
          "cod": {
            "is_cod": 0,
            "cod_text": ""
          },
          "mvc": {
            "is_mvc": 0,
            "mvc_title": "",
            "mvc_logo": "",
            "mvc_error_message": ""
          },
          "products": [
            {
              "shipper_name": "NOW! 2 jam tiba",
              "shipper_id": 29,
              "shipper_product_id": 58,
              "shipper_product_name": "Instant Courier",
              "shipper_product_desc": "Dengan kurir instan, kamu bisa pakai GoSend Car dan GoSend Bike (pengiriman maks. 3 jam \u0026 berat maks. 150kg). Setelah aktif, GoSend Car dan GoSend Bike otomatis masuk pilihan kurir, tidak ditampilkan di menu ini.",
              "is_show_map": 1,
              "shipper_weight": 3000,
              "status": 200,
              "recommend": true,
              "checksum": "f9wH6pk63IsVZfzFFEfxcAjWQ%2BA%3D",
              "ut": "1681285140",
              "promo_code": "",
              "ui_rates_hidden": false,
              "price": {
                "price": 24000,
                "formatted_price": "Rp24.000"
              },
              "etd": {
                "min_etd": 10800,
                "max_etd": 10800
              },
              "texts": {
                "text_price": "Rp24.000",
                "text_etd": "Maks. 3 jam"
              },
              "insurance": {
                "insurance_price": 0,
                "insurance_type": 3,
                "insurance_type_info": "Must insurance",
                "insurance_used_type": 2,
                "insurance_used_info": "Pesananmu dapat perlindungan dari asuransi senilai transaksi s/d Rp100.000.000 dengan proses klaim mudah, \u003cb\u003egratis layanan pengiriman retur melalui kurir yang bekerjasama dengan Tokopedia.\u003c/b\u003e",
                "insurance_used_default": 2
              },
              "error": {
                "error_id": "",
                "error_message": ""
              },
              "cod": {
                "is_cod_available": 0,
                "cod_price": 0,
                "cod_text": "",
                "formatted_price": "",
                "tnc_text": "",
                "tnc_link": ""
              },
              "features": {
                "ontime_delivery_guarantee": {
                  "available": false,
                  "value": 0,
                  "text_label": "",
                  "text_detail": "",
                  "url_detail": "",
                  "icon_url": "",
                  "url_text": ""
                },
                "mvc": {
                  "is_mvc": 0,
                  "mvc_logo": "",
                  "mvc_error_message": ""
                },
                "dynamic_price": {
                  "text_label": ""
                }
              },
              "eta": {
                "text_eta": "Tiba dalam 2 jam",
                "error_code": 0
              }
            }
          ]
        }
      ],
      "promo_stacking": {
        "is_promo": 1,
        "promo_code": "BONOWSELLY",
        "title": "Bebas Ongkir",
        "shipper_id": 29,
        "shipper_product_id": 58,
        "shipper_name": "NOW! 2 jam tiba",
        "shipper_desc": "3 jam|1000",
        "promo_detail": "",
        "benefit_desc": "",
        "point_change": 0,
        "user_point": 0,
        "promo_tnc_html": "Anda akan menggunakan promo \u003cb\u003eNOW! BEBAS ONGKIR hingga Rp75.000\u003c/b\u003e",
        "shipper_disable_text": "Tidak dapat \u003cb\u003eubah kurir\u003c/b\u003e karena promo hanya berlaku untuk kurir yang ditentukan",
        "service_id": 1000,
        "is_applied": 0,
        "image_url": "https://images.tokopedia.net/img/now/badge/now-badge.png",
        "discounted_rate": 0,
        "shipping_rate": 24000,
        "benefit_amount": 75000,
        "disabled": false,
        "hide_shipper_name": true,
        "cod": {
          "is_cod_available": 0,
          "cod_text": "",
          "cod_price": 0,
          "formatted_price": "",
          "tnc_text": "",
          "tnc_link": ""
        },
        "eta": {
          "text_eta": "Tiba dalam 2 jam",
          "error_code": 0
        },
        "is_bebas_ongkir_extra": false,
        "texts": {
          "bottom_sheet": "\u003cb\u003eNOW! 2 jam tiba\u003c/b\u003e",
          "chosen_courier": "\u003cb\u003eNOW! 2 jam tiba\u003c/b\u003e \u003cb\u003e(\u003c/b\u003e\u003cs\u003eRp24.000\u003c/s\u003e \u003cb\u003eRp0)\u003c/b\u003e",
          "ticker_courier": "",
          "bottom_sheet_description": ""
        },
        "free_shipping_metadata": {
          "sent_shipper_partner": false,
          "benefit_class": "",
          "shipping_subsidy": 75000,
          "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
        }
      },
      "promo_stackings": [
        {
          "is_promo": 1,
          "promo_code": "BONOWSELLY",
          "title": "Bebas Ongkir",
          "shipper_id": 29,
          "shipper_product_id": 58,
          "shipper_name": "NOW! 2 jam tiba",
          "shipper_desc": "3 jam|1000",
          "promo_detail": "",
          "benefit_desc": "",
          "point_change": 0,
          "user_point": 0,
          "promo_tnc_html": "Anda akan menggunakan promo \u003cb\u003eNOW! BEBAS ONGKIR hingga Rp75.000\u003c/b\u003e",
          "shipper_disable_text": "Tidak dapat \u003cb\u003eubah kurir\u003c/b\u003e karena promo hanya berlaku untuk kurir yang ditentukan",
          "service_id": 1000,
          "is_applied": 0,
          "image_url": "https://images.tokopedia.net/img/now/badge/now-badge.png",
          "discounted_rate": 0,
          "shipping_rate": 24000,
          "benefit_amount": 75000,
          "disabled": false,
          "hide_shipper_name": true,
          "cod": {
            "is_cod_available": 0,
            "cod_text": "",
            "cod_price": 0,
            "formatted_price": "",
            "tnc_text": "",
            "tnc_link": ""
          },
          "eta": {
            "text_eta": "Tiba dalam 2 jam",
            "error_code": 0
          },
          "is_bebas_ongkir_extra": false,
          "texts": {
            "bottom_sheet": "\u003cb\u003eNOW! 2 jam tiba\u003c/b\u003e",
            "chosen_courier": "\u003cb\u003eNOW! 2 jam tiba\u003c/b\u003e \u003cb\u003e(\u003c/b\u003e\u003cs\u003eRp24.000\u003c/s\u003e \u003cb\u003eRp0)\u003c/b\u003e",
            "ticker_courier": "",
            "bottom_sheet_description": "",
            "promo_message": "Beberapa promo tidak bisa dipakai di pengiriman ini.",
            "title_promo_message": "Beberapa promo tidak tersedia"
          },
          "free_shipping_metadata": {
            "sent_shipper_partner": false,
            "benefit_class": "",
            "shipping_subsidy": 75000,
            "additional_data": "{\"bo_feature_type\":[],\"fulfillment\":{\"chargeable_flag\":0}}"
          },
          "bo_campaign_id": 91
        }
      ],
      "pre_order": {
        "header": "",
        "label": "",
        "display": false
      },
      "info": {
        "blackbox_info": {
          "text_info": ""
        }
      }
    }
  }
}
"""
        ).map {
            val response = Gson().fromJson(it, RatesGqlResponse::class.java)
            converter.convertModel(response.ratesData)
        }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
//        return gql.getExecuteObservable(null)
//            .map { graphqlResponse: GraphqlResponse ->
//                val response: RatesGqlResponse =
//                    graphqlResponse.getData<RatesGqlResponse>(RatesGqlResponse::class.java)
//                        ?: throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
//                converter.convertModel(response.ratesData)
//            }
//            .subscribeOn(scheduler.io())
//            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}
