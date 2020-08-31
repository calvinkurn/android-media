package com.tokopedia.oneclickcheckout.common.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CheckoutTestInterceptor : BaseOccInterceptor() {

    var customCheckoutResponseString: String? = null

    var customCheckoutThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(CHECKOUT_QUERY)) {
            if (customCheckoutThrowable != null) {
                throw customCheckoutThrowable!!
            } else if (customCheckoutResponseString != null) {
                return mockResponse(copy, customCheckoutResponseString!!)
            }
            return mockResponse(copy, CHECKOUT_DEFAULT_RESPONSE)
        }
        return chain.proceed(chain.request())
    }
}

const val CHECKOUT_QUERY = "one_click_checkout"

const val CHECKOUT_DEFAULT_RESPONSE = """
[
  {
    "data": {
      "one_click_checkout": {
        "header": {
          "process_time": "",
          "reason": "",
          "error_code": "",
          "messages" : []
        },
        "data": {
          "success": 1,
          "error": {
            "code": "",
            "image_url": "",
            "message": "",
            "additional_info": {
              "price_validation": {
                "is_updated": "",
                "message": {
                  "title": "",
                  "desc": "",
                  "action": ""
                },
                "tracker_data": {
                  "product_changes_type": "",
                  "campaign_type": "",
                  "product_ids": ""
                }
              }
            }
          },
          "payment_parameter": {
            "callback_url": "",
            "payload": "",
            "redirect_param": {
              "url": "https://www.tokopedia.com/payment",
              "gateway": "",
              "method": "POST",
              "form": "transaction_id=123"
            }
          }
        },
        "status": "OK"
      }
    }
  }
]
"""