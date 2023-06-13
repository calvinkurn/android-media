package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.play_common.websocket.WebSocketResponse

/**
 * Created by jegul on 15/07/21
 */
class PlaySocketResponseBuilder {

    private val gson = Gson()

    private val channelInteractiveExistStatus = """
        {
          "type" : "CHANNEL_INTERACTIVE_STATUS",
          "data" : {
            "channel_id" : 67990,
            "exist" : true 
          }
        }
    """.trimIndent()

    private val channelInteractiveNotExistStatus = """
        {
          "type" : "CHANNEL_INTERACTIVE_STATUS",
          "data" : {
            "channel_id" : 67990,
            "exist" : false 
          }
        }
    """.trimIndent()

    private val merchantVoucher = """
        {
           "type":"MERCHANT_VOUCHERS",
           "data":[
              {
                 "voucher_id":4274415,
                 "shop_id":11104475,
                 "voucher_name":"August Merchant Voucher",
                 "title":"Cashback 7%",
                 "subtitle":"min. pembelian 200rb",
                 "voucher_type":3,
                 "voucher_image":"https://images.tokopedia.net/img/official_store/promoos/default-cashback.jpg",
                 "voucher_image_square":"",
                 "voucher_quota":300,
                 "voucher_finish_time":"2021-08-31T23:59:00Z",
                 "voucher_code":"LPAUG",
                 "is_quota_available":57,
                 "used_quota":243,
                 "tnc":"\u003col\u003e\u003cli\u003eVoucher Cashback dapat digunakan dengan membuka halaman promo di Keranjang.\u003c/li\u003e\u003cli\u003eVoucher Cashback hanya berlaku apabila pembelian Pengguna sudah memenuhi syarat dan ketentuan yang tertera pada voucher.\u003c/li\u003e\u003cli\u003eNominal Cashback yang bisa didapatkan sebesar 7% hingga Rp 20.000.\u003c/li\u003e\u003cli\u003eVoucher Cashback ini berlaku untuk transaksi minimal Rp 200.000 (tidak termasuk ongkos kirim dan biaya tambahan lainnya).\u003c/li\u003e\u003cli\u003eVoucher Cashback ini hanya berlaku untuk pembelanjaan di L'Oreal Professionnel Official Store.\u003c/li\u003e\u003cli\u003e1 (satu) Pengguna Tokopedia hanya boleh menggunakan 1 (satu) akun Tokopedia untuk menggunakan Voucher Cashback ini.\u003c/li\u003e\u003cli\u003ePengguna akan mendapatkan promo cashback dalam bentuk TokoPoints maksimal 1x24 jam setelah transaksi selesai serta telah sesuai dengan \u003ca href=\"https://www.tokopedia.com/help/article/apa-itu-tokopoints\"\u003eSyarat dan Ketentuan Toko Points\u003c/a\u003e.\u003c/li\u003e\u003cli\u003eBenefit cashback akan diberikan secara pro rata jika menggunakan pembayaran sebagian dengan OVO Points dan/ atau TokoPoints. Selengkapnya \u003ca href=\"https://www.tokopedia.com/help/article/apa-itu-tokopoints\"\u003edisini\u003c/a\u003e.\u003cli\u003ePromo tidak berlaku untuk produk Logam Mulia, Emas Batangan, Voucher, dan Paket Data.\u003c/li\u003e\u003cli\u003eApabila terjadi pengembalikan dana sebagian maka terdapat penyesuaikan cashback.\u003c/li\u003e\u003cli\u003eJika transaksi gagal atau masuk ke Pusat Resolusi, dana yang kembali ke Pembeli akan sesuai dengan nominal pembayaran yang dilakukan oleh Pembeli.\u003c/li\u003e\u003cli\u003eTokopedia berhak melakukan tindakan yang diperlukan apabila diduga terjadi tindakan kecurangan yang dilakukan oleh Pengguna dan/atau yang melanggar Syarat dan Ketentuan Situs dan/atau merugikan pihak Tokopedia.\u003c/li\u003e\u003cli\u003eDengan menggunakan Voucher Cashback ini, Pengguna dianggap telah memahami dan menyetujui semua Syarat \u0026 Ketentuan yang berlaku.\u003c/li\u003e\u003c/ol\u003e",
                 "is_copyable":false,
                 "is_highlighted":false,
                 "is_private":false
              },
              {
                 "voucher_id":4495944,
                 "shop_id":11104475,
                 "voucher_name":"NEWFOLLOWERS",
                 "title":"Cashback 5%",
                 "subtitle":"min. pembelian 200rb",
                 "voucher_type":3,
                 "voucher_image":"https://images.tokopedia.net/img/BTJGre/2021/8/12/509a34ac-1ee5-4bd2-b3e3-437f1b66a142.jpg",
                 "voucher_image_square":"https://images.tokopedia.net/img/nNLhqY/2021/8/12/65b16fdd-e93f-4d59-b78f-f535be9e19b7.jpg",
                 "voucher_quota":100,
                 "voucher_finish_time":"2021-08-31T23:30:00Z",
                 "voucher_code":"LORELR6PLN",
                 "is_quota_available":89,
                 "used_quota":11,
                 "tnc":"\u003col\u003e\u003cli\u003eVoucher Cashback hanya berlaku untuk Follower Baru.\u003c/li\u003e\u003cli\u003eVoucher Cashback dapat digunakan dengan membuka halaman promo di Keranjang.\u003c/li\u003e\u003cli\u003eVoucher Cashback hanya berlaku apabila pembelian Pengguna sudah memenuhi syarat dan ketentuan yang tertera pada voucher.\u003c/li\u003e\u003cli\u003eNominal Cashback yang bisa didapatkan sebesar 5% hingga Rp 10.000.\u003c/li\u003e\u003cli\u003eVoucher Cashback ini berlaku untuk transaksi minimal Rp 200.000 (tidak termasuk ongkos kirim dan biaya tambahan lainnya).\u003c/li\u003e\u003cli\u003eVoucher Cashback ini hanya berlaku untuk pembelanjaan di L\u0026#39;Oreal Professionnel Official Store.\u003c/li\u003e\u003cli\u003e1 (satu) Pengguna Tokopedia hanya boleh menggunakan 1 (satu) akun Tokopedia untuk menggunakan Voucher Cashback ini.\u003c/li\u003e\u003cli\u003ePengguna akan mendapatkan promo cashback dalam bentuk TokoPoints maksimal 1x24 jam setelah transaksi selesai serta telah sesuai dengan \u003ca href=\"https://www.tokopedia.com/help/article/apa-itu-tokopoints\"\u003eSyarat dan Ketentuan Toko Points\u003c/a\u003e.\u003c/li\u003e\u003cli\u003eBenefit cashback akan diberikan secara pro rata jika menggunakan pembayaran sebagian dengan OVO Points dan/ atau TokoPoints. Selengkapnya \u003ca href=\"https://www.tokopedia.com/help/article/apa-itu-tokopoints\"\u003edisini\u003c/a\u003e.\u003cli\u003ePromo tidak berlaku untuk produk Logam Mulia, Emas Batangan, Voucher, dan Paket Data.\u003c/li\u003e\u003cli\u003eApabila terjadi pengembalikan dana sebagian maka terdapat penyesuaikan cashback.\u003c/li\u003e\u003cli\u003eJika transaksi gagal atau masuk ke Pusat Resolusi, dana yang kembali ke Pembeli akan sesuai dengan nominal pembayaran yang dilakukan oleh Pembeli.\u003c/li\u003e\u003cli\u003eTokopedia berhak melakukan tindakan yang diperlukan apabila diduga terjadi tindakan kecurangan yang dilakukan oleh Pengguna dan/atau yang melanggar Syarat dan Ketentuan Situs dan/atau merugikan pihak Tokopedia.\u003c/li\u003e\u003cli\u003eDengan menggunakan Voucher Cashback ini, Pengguna dianggap telah memahami dan menyetujui semua Syarat \u0026 Ketentuan yang berlaku.\u003c/li\u003e\u003c/ol\u003e",
                 "is_copyable":false,
                 "is_highlighted":false,
                 "is_private":false
              }
           ]
        }
    """.trimIndent()

    private val productTag = """
        {
            "type": "PRODUCT_TAG_UPDATE",
            "data": {
                "sections": [
                    {
                        "type": "active",
                        "title": "Pembasmi Lapar!",
                        "countdown": {
                            "copy": "Berakhir dalam"
                        },
                        "background": {
                            "gradient": [
                                "#ff23de",
                                "#2244aa"
                            ],
                            "image_url": "https://via.placeholder.com/150"
                        },
                        "start_time": "2022-01-02T15:04:05Z07:00",
                        "end_time": "2022-01-02T16:04:05Z07:00",
                        "server_time": "2022-01-02T15:14:05Z07:00",
                        "products": [
                            {
                                "id": "15240013",
                                "name": "Indomie Soto Lamongan",
                                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                                "shop_id": "479887",
                                "original_price": 60000,
                                "original_price_formatted": "Rp 60.000",
                                "discount": 0,
                                "price": 0,
                                "price_formatted": "",
                                "quantity": 9988,
                                "is_variant": false,
                                "is_available": false,
                                "order": 0,
                                "app_link": "tokopedia://product/15240013",
                                "web_link": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                                "min_quantity": 1,
                                "is_free_shipping": true
                            }
                        ]
                    },
                    {
                        "type": "other",
                        "title": "Produk Lainnya",
                        "countdown": {
                            "copy": ""
                        },
                        "background": {
                            "gradient": [],
                            "image_url": ""
                        },
                        "start_time": "",
                        "end_time": "",
                        "server_time": "",
                        "products": [
                            {
                                "id": "15240013",
                                "name": "Indomie Soto Lamongan",
                                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                                "shop_id": "479887",
                                "original_price": 60000,
                                "original_price_formatted": "Rp 60.000",
                                "discount": 0,
                                "price": 0,
                                "price_formatted": "",
                                "quantity": 9988,
                                "is_variant": false,
                                "is_available": false,
                                "order": 0,
                                "app_link": "tokopedia://product/15240013",
                                "web_link": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                                "min_quantity": 1,
                                "is_free_shipping": true
                            }
                        ]
                    },
                    {
                        "type": "other",
                        "title": "Produk Habis",
                        "countdown": {
                            "copy": ""
                        },
                        "background": {
                            "gradient": [],
                            "image_url": ""
                        },
                        "start_time": "",
                        "end_time": "",
                        "server_time": "",
                        "products": [
                            {
                                "id": "15240013",
                                "name": "Indomie Soto Lamongan",
                                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                                "shop_id": "479887",
                                "original_price": 60000,
                                "original_price_formatted": "Rp 60.000",
                                "discount": 0,
                                "price": 0,
                                "price_formatted": "",
                                "quantity": 9988,
                                "is_variant": false,
                                "is_available": false,
                                "order": 0,
                                "app_link": "tokopedia://product/15240013",
                                "web_link": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                                "min_quantity": 1,
                                "is_free_shipping": true
                            }
                        ]
                    }
                ],
                "config" : {
                    "peek_product_count" : 15,
                    "title_bottomsheet": "Promo dan Produk Lainnya"
                }
            }
        }
    """.trimIndent()

    private val channelQuizActive = """
    {
      "type": "CHANNEL_QUIZ",
      "data": {
        "channel_id": "123456",
        "interactive_id": "234567",
        "interactive_type": 1, 
        "status": 1, 
        "question": "Kapan Indonesia Raya merdeka?",
        "countdown_end": 30,
        "prize": "sepasang sepatu",
        "choices": [
          {
            "id": "345678",
            "text": "17 Agustus 2022"
          },
          {
            "id": "456789",
            "text": "18 Agustus 2023"
          }
        ],
        "waiting_duration": 15
      }
    }
    """.trimIndent()
    //we dont fetch correct bcz Channel_Quiz only send once when user enters room with quiz already started, user hasnt answer yet but already given answer would be weird

    fun buildChannelInteractiveResponse(isExist: Boolean = true): WebSocketResponse = gson.fromJson(
            if (isExist) channelInteractiveExistStatus else channelInteractiveNotExistStatus,
            WebSocketResponse::class.java
    )

    fun buildProductTagResponse(): WebSocketResponse = gson.fromJson(
            productTag, WebSocketResponse::class.java
    )

    fun buildQuiz(): WebSocketResponse = gson.fromJson(channelQuizActive, WebSocketResponse::class.java)

    private val freezeStatus = """
        "type":"FREEZE",
        "data":{
          "channel_id": "12269",
          "is_freeze":true,
          "timestamp":1592392273000
        }
    """.trimIndent()

    private val bannedStatus = """
        "type": "BANNED",
        "data": {
          "channel_id": "12665,
          "user": {
            "id": 1,
            "name: "",
            "image": ""
          },
          "is_banned": true,
          "timestamp": 1579064126000
        }
    """.trimIndent()

    fun buildFreeze(): WebSocketResponse = gson.fromJson(freezeStatus, WebSocketResponse::class.java)

    fun buildBanned(): WebSocketResponse = gson.fromJson(bannedStatus, WebSocketResponse::class.java)
}
