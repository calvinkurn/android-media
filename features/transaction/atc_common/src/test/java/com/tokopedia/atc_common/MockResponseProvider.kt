package com.tokopedia.atc_common

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse

/**
 * Created by Irfan Khoirul on 2019-09-11.
 */

object MockResponseProvider {

    fun getResponseAtcSuccess(): AddToCartGqlResponse {
        return Gson().fromJson(responseAtcSuccess, AddToCartGqlResponse::class.java)
    }

    fun getResponseAtcError(): AddToCartGqlResponse {
        return Gson().fromJson(responseAtcError, AddToCartGqlResponse::class.java)
    }

}

val responseAtcSuccess = """
    {
     "add_to_cart_v2": {
       "error_message": [
         "Produk berhasil dimasukkan ke Keranjang Belanja"
       ],
       "status": "OK",
       "data": {
         "success": 1,
         "cart_id": "33398482",
         "product_id": 15244838,
         "quantity": 1,
         "notes": "",
         "shop_id": 480579,
         "customer_id": 3297896,
         "warehouse_id": 2240,
         "tracker_attribution": "",
         "tracker_list_name": "",
         "uc_ut_param": "",
         "is_trade_in": false,
         "message": [
           "Produk berhasil dimasukkan ke Keranjang Belanja"
         ]
       },
       "error_reporter": {
         "eligible": false,
         "texts": {
           "submit_title": "Barang belum bisa dimasukkan ke keranjang",
           "submit_description": "Ayo beri tahu kami tentang gangguan ini biar bisa segera diperbaiki!",
           "submit_button": "Laporkan Gangguan",
           "cancel_button": "Batal"
         }
       }
     }
   }
""".trimIndent()

val responseAtcError = """
    {
    "add_to_cart_v2": {
      "error_message": [
        "Yaah, barang ini sudah nggak tersedia. Move on ke barang lainnya, yuk!"
      ],
      "status": "OK",
      "data": {
        "success": 0,
        "cart_id": "0",
        "product_id": 0,
        "quantity": 0,
        "notes": "",
        "shop_id": 0,
        "customer_id": 0,
        "warehouse_id": 0,
        "tracker_attribution": "",
        "tracker_list_name": "",
        "uc_ut_param": "",
        "is_trade_in": false,
        "message": [
          "Yaah, barang ini sudah nggak tersedia. Move on ke barang lainnya, yuk!"
        ]
      },
      "error_reporter": {
        "eligible": false,
        "texts": {
          "submit_title": "Barang belum bisa dimasukkan ke keranjang",
          "submit_description": "Ayo beri tahu kami tentang gangguan ini biar bisa segera diperbaiki!",
          "submit_button": "Laporkan Gangguan",
          "cancel_button": "Batal"
        }
      }
    }
  }
""".trimIndent()