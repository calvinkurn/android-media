package com.tokopedia.pdp.fintech.domain.usecase

import com.google.gson.Gson
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import javax.inject.Inject

class FintechWidgetUseCase @Inject constructor() {


    fun setWidget(): WidgetDetail {
        val gson = Gson()
        return gson.fromJson(jsonValue, WidgetDetail::class.java)
    }


    val jsonValue = " {\n" +
            "    \"list\": [\n" +
            "      {\n" +
            "        \"price\": 15000000,\n" +
            "        \"title\": \"Buy now, pay next month or with installments\",\n" +
            "        \"chips\": [\n" +
            "          {\n" +
            "            \"gateway_id\": 6,\n" +
            "            \"name\": \"Paylater\",\n" +
            "            \"product_code\": \"PEMUDA\",\n" +
            "            \"is_active\": false,\n" +
            "            \"is_disabled\": false,\n" +
            "            \"tenure\": 1,\n" +
            "            \"header\": \"Bayar 24 Nov 2021\",\n" +
            "            \"subheader\": \"Activate\",\n" +
            "            \"subheader_color\": \"green\",\n" +
            "            \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "            \"product_icon_dark\": \"images.tokopedia.net\",\n" +
            "            \"cta\": {\n" +
            "              \"type\": 5,\n" +
            "              \"web_url\": \"www.tokopedia.com\",\n" +
            "              \"android_url\": \"www.tokopedia.com\",\n" +
            "              \"ios_url\": \"www.tokopedia.com\",\n" +
            "              \"bottomsheet\": {\n" +
            "                  \"show\": true,\n" +
            "                  \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"title\": \"You have been approved for GoPayLater Cicil!\",\n" +
            "                  \"buttons\": [\n" +
            "                    {\n" +
            "                      \"button_text\": \"Cancel\",\n" +
            "                      \"button_text_color\": \"green\",\n" +
            "                      \"button_color\": \"white\",\n" +
            "                      \"button_url\": \"tokopedia.com\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                      \"button_text\": \"Get it Now!\",\n" +
            "                      \"button_text_color\": \"white\",\n" +
            "                      \"button_color\": \"green\",\n" +
            "                      \"button_url\": \"gopay.com\"\n" +
            "                    }\n" +
            "                  ],\n" +
            "                  \"descriptions\": [\n" +
            "                      {\n" +
            "                          \"line_icon_dark\":\"images.tokopedia.net\",\n" +
            "                          \"line_icon_light\":\"images.tokopedia.net\",\n" +
            "                          \"text\":\"Get Rp. 15.000.000 limit instantly\"\n" +
            "                      }\n" +
            "                  ],\n" +
            "                  \"product_footnote\": \"Powered by\",\n" +
            "                  \"product_footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_footnote_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"footnote\": \"Terdaftar dan diawasi oleh\",\n" +
            "                  \"footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"footnote_icon_dark\": \"images.tokopedia.net\"\n" +
            "              }\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"gateway_id\": 7,\n" +
            "            \"name\": \"Paylater\",\n" +
            "            \"product_code\": \"PEMUDACICIL\",\n" +
            "            \"is_active\": false,\n" +
            "            \"is_disabled\": false,\n" +
            "            \"tenure\": 12,\n" +
            "            \"header\": \"Rp. 1.250.000 x12\",\n" +
            "            \"subheader\": \"Activate\",\n" +
            "            \"subheader_color\": \"green\",\n" +
            "            \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "            \"product_icon_dark\": \"images.tokopedia.net\", \n" +
            "            \"cta\": {\n" +
            "              \"type\": 5,\n" +
            "              \"web_url\": \"www.tokopedia.com\",\n" +
            "              \"android_url\": \"www.tokopedia.com\",\n" +
            "              \"ios_url\": \"www.tokopedia.com\",\n" +
            "              \"bottomsheet\": {\n" +
            "                  \"show\": true,\n" +
            "                  \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"title\": \"You have been approved for GoPayLater Cicil!\",\n" +
            "                  \"buttons\": [\n" +
            "                    {\n" +
            "                      \"button_text\": \"Cancel\",\n" +
            "                      \"button_text_color\": \"green\",\n" +
            "                      \"button_color\": \"white\",\n" +
            "                      \"button_url\": \"tokopedia.com\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                      \"button_text\": \"Get it Now!\",\n" +
            "                      \"button_text_color\": \"white\",\n" +
            "                      \"button_color\": \"green\",\n" +
            "                      \"button_url\": \"gopay.com\"\n" +
            "                    }\n" +
            "                  ],\n" +
            "                  \"descriptions\": [\n" +
            "                      {\n" +
            "                          \"line_icon_dark\":\"images.tokopedia.net\",\n" +
            "                          \"line_icon_light\":\"images.tokopedia.net\",\n" +
            "                          \"text\":\"Get Rp. 15.000.000 limit instantly\"\n" +
            "                      }\n" +
            "                  ],\n" +
            "                  \"product_footnote\": \"Powered by\",\n" +
            "                  \"product_footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_footnote_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"footnote\": \"Terdaftar dan diawasi oleh\",\n" +
            "                  \"footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"footnote_icon_dark\": \"images.tokopedia.net\"\n" +
            "              }\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"gateway_id\": 0,\n" +
            "            \"name\": \"Etc\",\n" +
            "            \"product_code\": \"\",\n" +
            "            \"is_active\": false,\n" +
            "            \"is_disabled\": false,\n" +
            "            \"tenure\": 0,\n" +
            "            \"header\": \"Lihat Semua\",\n" +
            "            \"subheader\": \"\",\n" +
            "            \"subheader_color\": \"\",\n" +
            "            \"product_icon_light\": \"\",\n" +
            "            \"product_icon_dark\": \"\", \n" +
            "            \"cta\": {}\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"price\": 10000000,\n" +
            "        \"title\": \"Buy now with installments\",\n" +
            "        \"chips\": [\n" +
            "          {\n" +
            "            \"gateway_id\": 6,\n" +
            "            \"name\": \"Paylater\",\n" +
            "            \"product_code\": \"PEMUDA\",\n" +
            "            \"is_active\": false,\n" +
            "            \"is_disabled\": false,\n" +
            "            \"tenure\": 1,\n" +
            "            \"header\": \"Bayar 24 Nov 2021\",\n" +
            "            \"subheader\": \"Activate\",\n" +
            "            \"subheader_color\": \"green\",\n" +
            "            \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "            \"product_icon_dark\": \"images.tokopedia.net\",\n" +
            "            \"cta\": {\n" +
            "              \"type\": 5,\n" +
            "              \"web_url\": \"www.tokopedia.com\",\n" +
            "              \"android_url\": \"www.tokopedia.com\",\n" +
            "              \"ios_url\": \"www.tokopedia.com\",\n" +
            "              \"bottomsheet\": {\n" +
            "                  \"show\": true,\n" +
            "                  \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"title\": \"You have been approved for GoPayLater Cicil!\",\n" +
            "                  \"buttons\": [\n" +
            "                    {\n" +
            "                      \"button_text\": \"Cancel\",\n" +
            "                      \"button_text_color\": \"green\",\n" +
            "                      \"button_color\": \"white\",\n" +
            "                      \"button_url\": \"tokopedia.com\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                      \"button_text\": \"Get it Now!\",\n" +
            "                      \"button_text_color\": \"white\",\n" +
            "                      \"button_color\": \"green\",\n" +
            "                      \"button_url\": \"gopay.com\"\n" +
            "                    }\n" +
            "                  ],\n" +
            "                  \"descriptions\": [\n" +
            "                      {\n" +
            "                          \"line_icon_dark\":\"images.tokopedia.net\",\n" +
            "                          \"line_icon_light\":\"images.tokopedia.net\",\n" +
            "                          \"text\":\"Get Rp. 15.000.000 limit instantly\"\n" +
            "                      }\n" +
            "                  ],\n" +
            "                  \"product_footnote\": \"Powered by\",\n" +
            "                  \"product_footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_footnote_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"footnote\": \"Terdaftar dan diawasi oleh\",\n" +
            "                  \"footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"footnote_icon_dark\": \"images.tokopedia.net\"\n" +
            "              }\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"gateway_id\": 7,\n" +
            "            \"name\": \"Paylater\",\n" +
            "            \"product_code\": \"PEMUDACICIL\",\n" +
            "            \"is_active\": false,\n" +
            "            \"is_disabled\": false,\n" +
            "            \"tenure\": 12,\n" +
            "            \"header\": \"Rp. 1.250.000 x12\",\n" +
            "            \"subheader\": \"Activate\",\n" +
            "            \"subheader_color\": \"green\",\n" +
            "            \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "            \"product_icon_dark\": \"images.tokopedia.net\", \n" +
            "            \"cta\": {\n" +
            "              \"type\": 5,\n" +
            "              \"web_url\": \"www.tokopedia.com\",\n" +
            "              \"android_url\": \"www.tokopedia.com\",\n" +
            "              \"ios_url\": \"www.tokopedia.com\",\n" +
            "              \"bottomsheet\": {\n" +
            "                  \"show\": true,\n" +
            "                  \"product_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"title\": \"You have been approved for GoPayLater Cicil!\",\n" +
            "                  \"buttons\": [\n" +
            "                    {\n" +
            "                      \"button_text\": \"Cancel\",\n" +
            "                      \"button_text_color\": \"green\",\n" +
            "                      \"button_color\": \"white\",\n" +
            "                      \"button_url\": \"tokopedia.com\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                      \"button_text\": \"Get it Now!\",\n" +
            "                      \"button_text_color\": \"white\",\n" +
            "                      \"button_color\": \"green\",\n" +
            "                      \"button_url\": \"gopay.com\"\n" +
            "                    }\n" +
            "                  ],\n" +
            "                  \"descriptions\": [\n" +
            "                      {\n" +
            "                          \"line_icon_dark\":\"images.tokopedia.net\",\n" +
            "                          \"line_icon_light\":\"images.tokopedia.net\",\n" +
            "                          \"text\":\"Get Rp. 15.000.000 limit instantly\"\n" +
            "                      }\n" +
            "                  ],\n" +
            "                  \"product_footnote\": \"Powered by\",\n" +
            "                  \"product_footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"product_footnote_icon_dark\": \"images.tokopedia.net\",\n" +
            "                  \"footnote\": \"Terdaftar dan diawasi oleh\",\n" +
            "                  \"footnote_icon_light\": \"images.tokopedia.net\",\n" +
            "                  \"footnote_icon_dark\": \"images.tokopedia.net\"\n" +
            "              }\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"gateway_id\": 0,\n" +
            "            \"name\": \"Etc\",\n" +
            "            \"product_code\": \"\",\n" +
            "            \"is_active\": false,\n" +
            "            \"is_disabled\": false,\n" +
            "            \"tenure\": 0,\n" +
            "            \"header\": \"Lihat Semua\",\n" +
            "            \"subheader\": \"\",\n" +
            "            \"subheader_color\": \"\",\n" +
            "            \"product_icon_light\": \"\",\n" +
            "            \"product_icon_dark\": \"\", \n" +
            "            \"cta\": {}\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }"

}