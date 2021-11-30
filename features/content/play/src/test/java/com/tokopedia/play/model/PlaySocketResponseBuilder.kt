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
                 "voucher_image":"https://ecs7.tokopedia.net/img/official_store/promoos/default-cashback.jpg",
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
                 "voucher_image":"https://ecs7.tokopedia.net/img/BTJGre/2021/8/12/509a34ac-1ee5-4bd2-b3e3-437f1b66a142.jpg",
                 "voucher_image_square":"https://ecs7.tokopedia.net/img/nNLhqY/2021/8/12/65b16fdd-e93f-4d59-b78f-f535be9e19b7.jpg",
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
           "type":"PRODUCT_TAG",
           "data":{
              "is_show_product_tagging":true,
              "products":[
                 {
                    "id":"1722121446",
                    "name":"L'Oreal Professionnel Serioxyl Denser Hair Serum Rambut Rontok",
                    "image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/8/16/3b692b82-f077-4b25-b148-54b7ba54a60e.jpg",
                    "shop_id":"11104475",
                    "original_price":508000,
                    "original_price_formatted":"Rp 508.000",
                    "discount":25,
                    "price":381000,
                    "price_formatted":"Rp 381.000",
                    "quantity":185,
                    "is_variant":false,
                    "is_available":true,
                    "order":0,
                    "app_link":"tokopedia://product/1722121446",
                    "web_link":"https://www.tokopedia.com/lorealprofessionnel/l-oreal-professionnel-serioxyl-denser-hair-serum-rambut-rontok",
                    "min_quantity":1,
                    "is_free_shipping":false
                 },
                 {
                    "id":"1722127217",
                    "name":"L'Oreal Professionnel Mythic Oil Hair Serum Untuk Rambut Lebih Halus",
                    "image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/8/16/f7d92c01-5dc0-433a-8b16-d423147ea552.jpg",
                    "shop_id":"11104475",
                    "original_price":293000,
                    "original_price_formatted":"Rp 293.000",
                    "discount":35,
                    "price":190450,
                    "price_formatted":"Rp 190.450",
                    "quantity":437,
                    "is_variant":false,
                    "is_available":true,
                    "order":1,
                    "app_link":"tokopedia://product/1722127217",
                    "web_link":"https://www.tokopedia.com/lorealprofessionnel/l-oreal-professionnel-mythic-oil-hair-serum-untuk-rambut-lebih-halus",
                    "min_quantity":1,
                    "is_free_shipping":false
                 }
              ]
           }
        }
    """.trimIndent()

    fun buildChannelInteractiveResponse(isExist: Boolean = true): WebSocketResponse = gson.fromJson(
            if (isExist) channelInteractiveExistStatus else channelInteractiveNotExistStatus,
            WebSocketResponse::class.java
    )

    fun buildProductTagResponse(): WebSocketResponse = gson.fromJson(
            productTag, WebSocketResponse::class.java
    )
}