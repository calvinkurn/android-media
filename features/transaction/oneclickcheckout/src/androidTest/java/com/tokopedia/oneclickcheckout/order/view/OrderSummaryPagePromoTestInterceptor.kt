package com.tokopedia.oneclickcheckout.order.view

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

class OrderSummaryPagePromoTestInterceptor : Interceptor {

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