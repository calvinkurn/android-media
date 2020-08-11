package com.tokopedia.oneclickcheckout.common.interceptor

import okhttp3.*
import okio.Buffer

const val VALIDATE_USE_PROMO_REVAMP_QUERY = "validate_use_promo_revamp"

const val VALIDATE_USE_PROMO_REVAMP_DEFAULT_RESPONSE = """
[
  {
    "data": {
      "validate_use_promo_revamp": {
        "status": "OK",
        "message": [],
        "error_code": "200",
        "code": "200000",
        "promo": {
          "global_success": true,
          "message": {
            "state": "",
            "color": "",
            "text": ""
          },
          "success": true,
          "codes": [],
          "promo_code_id": 0,
          "title_description": "",
          "discount_amount": 0,
          "cashback_wallet_amount": 0,
          "cashback_advocate_referral_amount": 0,
          "cashback_voucher_description": "",
          "invoice_description": "",
          "gateway_id": "",
          "is_coupon": 0,
          "coupon_description": "",
          "is_tokopedia_gerai": false,
          "voucher_orders": [],
          "benefit_details": [],
          "benefit_summary_info": {
            "final_benefit_text": "",
            "final_benefit_amount": 0,
            "final_benefit_amount_str": "",
            "summaries": []
          },
          "clashing_info_detail": {
            "clash_message": "",
            "clash_reason": "",
            "is_clashed_promos": false,
            "options": []
          },
          "tracking_details": [],
          "tokopoints_detail": {
            "conversion_rate": {
              "rate": 0,
              "points_coefficient": 0,
              "external_currency_coefficient": 0
            }
          },
          "ticker_info": {
            "unique_id": "",
            "status_code": 0,
            "message": ""
          },
          "additional_info": {
            "message_info": {
              "message": "Makin hemat pakai promo",
              "detail": ""
            },
            "error_detail": {
              "message": ""
            },
            "empty_cart_info": {
              "image_url": "",
              "message": "",
              "detail": ""
            },
            "usage_summaries": [],
            "sp_ids": []
          }
        }
      }
    }
  }
]
"""

const val VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE = """
[
  {
    "data": {
      "validate_use_promo_revamp": {
        "status": "OK",
        "message": [],
        "error_code": "200",
        "code": "200000",
        "promo": {
          "global_success": true,
          "message": {
            "state": "",
            "color": "",
            "text": ""
          },
          "success": true,
          "codes": [],
          "promo_code_id": 0,
          "title_description": "Promo ",
          "discount_amount": 0,
          "cashback_wallet_amount": 0,
          "cashback_advocate_referral_amount": 0,
          "cashback_voucher_description": "",
          "invoice_description": "",
          "gateway_id": "71066624823132159",
          "is_coupon": 0,
          "coupon_description": "",
          "is_tokopedia_gerai": false,
          "voucher_orders": [
            {
              "code": "EKSCEPAT",
              "success": true,
              "unique_id": "480176-0-2034-4690461",
              "cart_id": 0,
              "order_id": 0,
              "shop_id": 480176,
              "is_po": 0,
              "duration": "0",
              "warehouse_id": 2034,
              "address_id": 4690461,
              "type": "logistic",
              "cashback_wallet_amount": 0,
              "discount_amount": 16000,
              "invoice_description": "",
              "title_description": "Potongan Ongkir Hingga Rp20.000",
              "message": {
                "state": "green",
                "color": "#ade3af",
                "text": "Kamu dapat potongan ongkir dari Tokopedia"
              },
              "benefit_details": [
                {
                  "code": "EKSCEPAT",
                  "type": "",
                  "order_id": 1,
                  "unique_id": "480176-0-2034-4690461",
                  "discount_amount": 16000,
                  "discount_details": [
                    {
                      "amount": 16000,
                      "data_type": "total_shipping_price"
                    }
                  ],
                  "cashback_amount": 0,
                  "cashback_details": [],
                  "promo_type": {
                    "is_bebas_ongkir": true,
                    "is_exclusive_shipping": true
                  },
                  "benefit_product_details": [
                    {
                      "product_id": 15147773,
                      "cashback_amount": 0,
                      "cashback_amount_idr": 0,
                      "discount_amount": 0,
                      "is_bebas_ongkir": true
                    }
                  ]
                }
              ]
            }
          ],
          "benefit_details": [],
          "benefit_summary_info": {
            "final_benefit_text": "Total benefit anda: ",
            "final_benefit_amount": 16000,
            "final_benefit_amount_str": "Rp16.000",
            "summaries": [
              {
                "description": "Total diskon: ",
                "type": "discount",
                "amount_str": "Rp16.000",
                "amount": 16000,
                "section_name": "Discount",
                "section_description": "",
                "details": [
                  {
                    "section_name": "",
                    "description": "Total Potongan Ongkos Kirim ",
                    "type": "shipping_discount",
                    "amount": 16000,
                    "amount_str": "Rp16.000",
                    "points": 0,
                    "points_str": ""
                  }
                ]
              }
            ]
          },
          "clashing_info_detail": {
            "clash_message": "",
            "clash_reason": "",
            "is_clashed_promos": false,
            "options": []
          },
          "tracking_details": [
            {
              "product_id": 15147773,
              "promo_codes_tracking": "EKSCEPAT",
              "promo_details_tracking": "L:3:16000:green"
            }
          ],
          "tokopoints_detail": {
            "conversion_rate": {
              "rate": 0,
              "points_coefficient": 0,
              "external_currency_coefficient": 0
            }
          },
          "ticker_info": {
            "unique_id": "480176-0-2034-4690461",
            "status_code": 700001,
            "message": "Success"
          },
          "additional_info": {
            "message_info": {
              "message": "Makin hemat pakai promo",
              "detail": ""
            },
            "error_detail": {
              "message": ""
            },
            "empty_cart_info": {
              "image_url": "",
              "message": "",
              "detail": ""
            },
            "usage_summaries": [],
            "sp_ids": []
          }
        }
      }
    }
  }
]
"""

class PromoTestInterceptor : Interceptor {

    var customValidateUseResponseString: String? = null

    var customValidateUseThrowable: Throwable? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val buffer = Buffer()
        copy.body()?.writeTo(buffer)
        val requestString = buffer.readUtf8()

        if (requestString.contains(VALIDATE_USE_PROMO_REVAMP_QUERY)) {
            if (customValidateUseThrowable != null) {
                throw customValidateUseThrowable!!
            } else if (customValidateUseResponseString != null) {
                return mockResponse(copy, customValidateUseResponseString!!)
            }
            return mockResponse(copy, VALIDATE_USE_PROMO_REVAMP_DEFAULT_RESPONSE)
        }
        return chain.proceed(chain.request())
    }

    private fun mockResponse(copy: Request, responseString: String): Response {
        return Response.Builder()
                .request(copy)
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(ResponseBody.create(MediaType.parse("application/json"),
                        responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
    }
}