package com.tokopedia.oneclickcheckout.preference.list.view

import okhttp3.*
import okio.Buffer

const val GET_PREFERENCE_LIST_QUERY = "get_preference_list"
const val SET_DEFAULT_PROFILE_QUERY = "set_default_profile_occ"

val GET_PREFERENCE_LIST_DEFAULT_RESPONSE = """
                    [{
                        "data": {
                          "get_all_profiles_occ": {
                            "error_message": [],
                            "status": "OK",
                            "data": {
                              "messages": [],
                              "success": 1,
                              "max_profile": 5,
                              "profiles": [
                                {
                                  "profile_id": 583940,
                                  "status": 1,
                                  "address": {
                                    "address_id": 97361993,
                                    "receiver_name": "Jk Seller",
                                    "address_name": "Rumah",
                                    "address_street": "Tokopedia Tower, Jl. Prof. DR. Satrio, Setia Budi, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta, 12950 [Tokopedia Note: lantai]",
                                    "district_id": 2270,
                                    "district_name": "Setiabudi",
                                    "city_id": 175,
                                    "city_name": "Jakarta Selatan",
                                    "province_id": 13,
                                    "province_name": "DKI Jakarta",
                                    "phone": "6281280969197",
                                    "longitude": "106.819417",
                                    "latitude": "-6.221197900000001",
                                    "geolocation": "-6.221197900000001,106.819417",
                                    "postal_code": "12950"
                                  },
                                  "payment": {
                                    "enable": 1,
                                    "active": 1,
                                    "gateway_code": "BCAVA",
                                    "gateway_name": "BCA Virtual Account",
                                    "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/icon-bca.png",
                                    "description": "",
                                    "url": "",
                                    "metadata": "{\"success\":true,\"message\":\"Success\",\"gateway_code\":\"BCAVA\",\"express_checkout_param\":{\"account_name\":\"\",\"account_number\":\"\",\"bank_id\":\"\"},\"express_checkout_url\":\"https://pay.tokopedia.com/v2/api/payment/BCAVA\",\"high_risk_flag\":\"\",\"description\":\"BCA Virtual Account\",\"image\":\"https://ecs7.tokopedia.net/img/toppay/payment-logo/icon-bca.png\",\"signature\":\"92e851f007bd7c3d0f1793ebae14a6c764be9edd\",\"customer_name\":\"Jk Seller\",\"customer_email\":\"jean.karunadewi jks@tokopedia.com\",\"user_id\":7977957}"
                                  },
                                  "shipment": {
                                    "service_id": 1104,
                                    "service_duration": "Reguler (2-4 hari)",
                                    "service_name": "Reguler"
                                  }
                                },
                                {
                                  "profile_id": 1886195,
                                  "status": 2,
                                  "address": {
                                    "address_id": 96722965,
                                    "receiver_name": "Jk Seller",
                                    "address_name": "Rumah",
                                    "address_street": "no 21, Malendeng, Tikala, Manado City, North Sulawesi",
                                    "district_id": 5166,
                                    "district_name": "Tikala",
                                    "city_id": 410,
                                    "city_name": "Kota Manado",
                                    "province_id": 28,
                                    "province_name": "Sulawesi Utara",
                                    "phone": "6281280969197",
                                    "longitude": "124.8901752",
                                    "latitude": "1.4694799",
                                    "geolocation": "1.4694799,124.8901752",
                                    "postal_code": "95129"
                                  },
                                  "payment": {
                                    "enable": 1,
                                    "active": 1,
                                    "gateway_code": "ALFAMART",
                                    "gateway_name": "Alfamart / Alfamidi / Lawson / Dan+Dan",
                                    "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                                    "description": "",
                                    "url": "",
                                    "metadata": ""
                                  },
                                  "shipment": {
                                    "service_id": 1104,
                                    "service_duration": "Reguler (2-4 hari)",
                                    "service_name": "Reguler"
                                  }
                                }
                              ]
                            }
                          }
                        }
                    }]
    """.trimIndent()
val GET_PREFERENCE_LIST_CHANGED_RESPONSE = """
            [{
                "data" : {
                  "get_all_profiles_occ": {
                    "error_message": [],
                    "status": "OK",
                    "data": {
                      "messages": [],
                      "success": 1,
                      "max_profile": 5,
                      "profiles": [
                        {
                          "profile_id": 583940,
                          "status": 2,
                          "address": {
                            "address_id": 97361993,
                            "receiver_name": "Jk Seller",
                            "address_name": "Rumah",
                            "address_street": "Tokopedia Tower, Jl. Prof. DR. Satrio, Setia Budi, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta, 12950 [Tokopedia Note: lantai]",
                            "district_id": 2270,
                            "district_name": "Setiabudi",
                            "city_id": 175,
                            "city_name": "Jakarta Selatan",
                            "province_id": 13,
                            "province_name": "DKI Jakarta",
                            "phone": "6281280969197",
                            "longitude": "106.819417",
                            "latitude": "-6.221197900000001",
                            "geolocation": "-6.221197900000001,106.819417",
                            "postal_code": "12950"
                          },
                          "payment": {
                            "enable": 1,
                            "active": 1,
                            "gateway_code": "BCAVA",
                            "gateway_name": "BCA Virtual Account",
                            "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/icon-bca.png",
                            "description": "",
                            "url": "",
                            "metadata": "{\"success\":true,\"message\":\"Success\",\"gateway_code\":\"BCAVA\",\"express_checkout_param\":{\"account_name\":\"\",\"account_number\":\"\",\"bank_id\":\"\"},\"express_checkout_url\":\"https://pay.tokopedia.com/v2/api/payment/BCAVA\",\"high_risk_flag\":\"\",\"description\":\"BCA Virtual Account\",\"image\":\"https://ecs7.tokopedia.net/img/toppay/payment-logo/icon-bca.png\",\"signature\":\"92e851f007bd7c3d0f1793ebae14a6c764be9edd\",\"customer_name\":\"Jk Seller\",\"customer_email\":\"jean.karunadewi jks@tokopedia.com\",\"user_id\":7977957}"
                          },
                          "shipment": {
                            "service_id": 1104,
                            "service_duration": "Reguler (2-4 hari)",
                            "service_name": "Reguler"
                          }
                        },
                        {
                          "profile_id": 1886195,
                          "status": 1,
                          "address": {
                            "address_id": 96722965,
                            "receiver_name": "Jk Seller",
                            "address_name": "Rumah",
                            "address_street": "no 21, Malendeng, Tikala, Manado City, North Sulawesi",
                            "district_id": 5166,
                            "district_name": "Tikala",
                            "city_id": 410,
                            "city_name": "Kota Manado",
                            "province_id": 28,
                            "province_name": "Sulawesi Utara",
                            "phone": "6281280969197",
                            "longitude": "124.8901752",
                            "latitude": "1.4694799",
                            "geolocation": "1.4694799,124.8901752",
                            "postal_code": "95129"
                          },
                          "payment": {
                            "enable": 1,
                            "active": 1,
                            "gateway_code": "ALFAMART",
                            "gateway_name": "Alfamart / Alfamidi / Lawson / Dan+Dan",
                            "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                            "description": "",
                            "url": "",
                            "metadata": ""
                          },
                          "shipment": {
                            "service_id": 1104,
                            "service_duration": "Reguler (2-4 hari)",
                            "service_name": "Reguler"
                          }
                        }
                      ]
                    }
                  }
                }
            }]
        """.trimIndent()
val SET_DEFAULT_PROFILE_DEFAULT_RESPONSE = """
            [{
                "data": {
                    "set_default_profile_occ": {
                        "error_message": [],
                        "status": "OK",
                        "data": {
                            "success": 1,
                            "messages": []
                        }
                    }
                }
            }]
        """.trimIndent()

class PreferenceListActivityTestInterceptor : Interceptor {

    var customGetPreferenceListResponseString: String? = null
    var customSetDefaultPreferenceResponseString: String? = null

    var customGetPreferenceListThrowable: Throwable? = null
    var customSetDefaultPreferenceThrowable: Throwable? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val buffer = Buffer()
        copy.body()?.writeTo(buffer)
        val requestString = buffer.readUtf8()

        if (requestString.contains(GET_PREFERENCE_LIST_QUERY)) {
            if (customGetPreferenceListThrowable != null) {
                throw customGetPreferenceListThrowable!!
            } else if (customGetPreferenceListResponseString != null) {
                return mockResponse(copy, customGetPreferenceListResponseString!!)
            }
            return mockResponse(copy, GET_PREFERENCE_LIST_DEFAULT_RESPONSE)
        } else if (requestString.contains(SET_DEFAULT_PROFILE_QUERY)) {
            if (customSetDefaultPreferenceThrowable != null) {
                throw customSetDefaultPreferenceThrowable!!
            } else if (customSetDefaultPreferenceResponseString != null) {
                return mockResponse(copy, customSetDefaultPreferenceResponseString!!)
            }
            return mockResponse(copy, SET_DEFAULT_PROFILE_DEFAULT_RESPONSE)
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