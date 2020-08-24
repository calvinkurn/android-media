package com.tokopedia.oneclickcheckout.common.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PreferenceTestInterceptor : BaseOccInterceptor() {

    var customGetPreferenceListResponseString: String? = null
    var customSetDefaultPreferenceResponseString: String? = null

    var customGetPreferenceListThrowable: IOException? = null
    var customSetDefaultPreferenceThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

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
}

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
                                  "profile_id": 1,
                                  "status": 1,
                                  "address": {
                                    "address_id": 1,
                                    "receiver_name": "User 1",
                                    "address_name": "Address 1",
                                    "address_street": "Address Street 1",
                                    "district_id": 1,
                                    "district_name": "District 1",
                                    "city_id": 1,
                                    "city_name": "City 1",
                                    "province_id": 1,
                                    "province_name": "Province 1",
                                    "phone": "1",
                                    "longitude": "1",
                                    "latitude": "1",
                                    "geolocation": "1,1",
                                    "postal_code": "1"
                                  },
                                  "payment": {
                                    "enable": 1,
                                    "active": 1,
                                    "gateway_code": "PAYMENT1",
                                    "gateway_name": "Payment 1",
                                    "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/icon-bca.png",
                                    "description": "Payment Desc 1",
                                    "url": "",
                                    "metadata": "{}"
                                  },
                                  "shipment": {
                                    "service_id": 1,
                                    "service_duration": "Durasi 1",
                                    "service_name": "Service 1"
                                  }
                                },
                                {
                                  "profile_id": 2,
                                  "status": 2,
                                  "address": {
                                    "address_id": 2,
                                    "receiver_name": "User 1",
                                    "address_name": "Address 2",
                                    "address_street": "Address Street 2",
                                    "district_id": 2,
                                    "district_name": "District 2",
                                    "city_id": 2,
                                    "city_name": "City 2",
                                    "province_id": 2,
                                    "province_name": "Province 2",
                                    "phone": "2",
                                    "longitude": "2",
                                    "latitude": "2",
                                    "geolocation": "2,2",
                                    "postal_code": "2"
                                  },
                                  "payment": {
                                    "enable": 1,
                                    "active": 1,
                                    "gateway_code": "PAYMENT2",
                                    "gateway_name": "Payment 2",
                                    "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                                    "description": "",
                                    "url": "",
                                    "metadata": ""
                                  },
                                  "shipment": {
                                    "service_id": 2,
                                    "service_duration": "Durasi (2)",
                                    "service_name": "Service 2"
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
                                  "profile_id": 1,
                                  "status": 2,
                                  "address": {
                                    "address_id": 1,
                                    "receiver_name": "User 1",
                                    "address_name": "Address 1",
                                    "address_street": "Address Street 1",
                                    "district_id": 1,
                                    "district_name": "District 1",
                                    "city_id": 1,
                                    "city_name": "City 1",
                                    "province_id": 1,
                                    "province_name": "Province 1",
                                    "phone": "1",
                                    "longitude": "1",
                                    "latitude": "1",
                                    "geolocation": "1,1",
                                    "postal_code": "1"
                                  },
                                  "payment": {
                                    "enable": 1,
                                    "active": 1,
                                    "gateway_code": "PAYMENT1",
                                    "gateway_name": "Payment 1",
                                    "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/icon-bca.png",
                                    "description": "",
                                    "url": "",
                                    "metadata": "{}"
                                  },
                                  "shipment": {
                                    "service_id": 1,
                                    "service_duration": "Durasi 1",
                                    "service_name": "Service 1"
                                  }
                                },
                                {
                                  "profile_id": 2,
                                  "status": 1,
                                  "address": {
                                    "address_id": 2,
                                    "receiver_name": "User 1",
                                    "address_name": "Address 2",
                                    "address_street": "Address Street 2",
                                    "district_id": 2,
                                    "district_name": "District 2",
                                    "city_id": 2,
                                    "city_name": "City 2",
                                    "province_id": 2,
                                    "province_name": "Province 2",
                                    "phone": "2",
                                    "longitude": "2",
                                    "latitude": "2",
                                    "geolocation": "2,2",
                                    "postal_code": "2"
                                  },
                                  "payment": {
                                    "enable": 1,
                                    "active": 1,
                                    "gateway_code": "PAYMENT2",
                                    "gateway_name": "Payment 2",
                                    "image": "https://ecs7.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                                    "description": "",
                                    "url": "",
                                    "metadata": ""
                                  },
                                  "shipment": {
                                    "service_id": 2,
                                    "service_duration": "Durasi 2",
                                    "service_name": "Service 2"
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