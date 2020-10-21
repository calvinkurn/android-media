package com.tokopedia.notifcenter.presentation.subscriber

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.data.entity.NotificationCenterDetail
import com.tokopedia.notifcenter.data.model.NotificationViewData

class GetNotificationUpdateSubscriber(
        val mapper: GetNotificationUpdateMapper,
        private val onSuccessInitiateData: (NotificationViewData) -> Unit,
        private val onErrorInitiateData: (Throwable) -> Unit
) : BaseNotificationSubscriber() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        onErrorInitiateData(e)
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse,
                NotificationCenterDetail::class.java,
                routingOnNext(graphqlResponse),
                onErrorInitiateData)
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val data = Gson().fromJson<NotificationCenterDetail>(MOCK, NotificationCenterDetail::class.java);
            val viewModel = mapper.map(data)
            onSuccessInitiateData(viewModel)
        }
    }

    companion object {
        const val MOCK = """
            {
                "notifcenter_detail": {
                  "paging": {
                    "has_next": false,
                    "has_prev": false
                  },
                  "list": [
                    {
                      "notif_id": "cefe01c4-f5b9-11ea-b599-00163e02b0aa",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "filtron_this_is_me_pn",
                      "title": "Ada aktivitas login di perangkat baru",
                      "short_description": "Akunmu telah login melalui perangkat  pada Minggu,13/09/2020 19:08 WIB. Kalau ini bukan kamu, amankan akunmu di sini!",
                      "short_description_html": "Akunmu telah login melalui perangkat  pada Minggu,13/09/2020 19:08 WIB. Kalau ini bukan kamu, amankan akunmu di sini!",
                      "is_longer_content": false,
                      "button_text": "Klik di sini!",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "Kemarin",
                      "create_time_unix": 1599998897,
                      "update_time": "2020-09-13 19:08:24.830915072 +0700 WIB",
                      "update_time_unix": 1599998904,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 4,
                      "show_bottomsheet": false,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://webview?allow_override=false&url=https%3A%2F%2Fm.tokopedia.com%2Fverify%2Fdevice%2Fdace840d-895b-4a60-a4a2-6052fa5d8f4e%3Fentry%3Dnotif_center",
                        "desktop_link": "https://tkp.me/tm/dace840d-895b-4a60-a4a2-6052fa5d8f4e?entry=notif_center",
                        "info_thumbnail_url": "https://images-staging.tokopedia.net/img/cache/700/emojuk/2020/8/31/89cf3566-2a78-44d2-b00d-9eb7b20ede90.jpg",
                        "mobile_link": "https://m.tokopedia.com/verify/device/dace840d-895b-4a60-a4a2-6052fa5d8f4e?entry=notif_center",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    },
                    {
                      "notif_id": "cefe01c4-f5b9-11ea-b599-00163e02b0aa",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "filtron_this_is_me_pn",
                      "title": "Ada aktivitas login di perangkat baru",
                      "short_description": "Akunmu telah login melalui perangkat  pada Minggu,13/09/2020 19:08 WIB. Kalau ini bukan kamu, amankan akunmu di sini!",
                      "short_description_html": "Akunmu telah login melalui perangkat  pada Minggu,13/09/2020 19:08 WIB. Kalau ini bukan kamu, amankan akunmu di sini!",
                      "is_longer_content": false,
                      "button_text": "Klik di sini!",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "Kemarin",
                      "create_time_unix": 1599998897,
                      "update_time": "2020-09-13 19:08:24.830915072 +0700 WIB",
                      "update_time_unix": 1599998904,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 4,
                      "show_bottomsheet": false,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://webview?allow_override=false&url=https%3A%2F%2Fm.tokopedia.com%2Fverify%2Fdevice%2Fdace840d-895b-4a60-a4a2-6052fa5d8f4e%3Fentry%3Dnotif_center",
                        "desktop_link": "https://tkp.me/tm/dace840d-895b-4a60-a4a2-6052fa5d8f4e?entry=notif_center",
                        "info_thumbnail_url": "https://images-staging.tokopedia.net/img/martin/2020/9/7/e0ce27f1-4299-4afe-bb4c-f2fd72c57595.jpg?b=APL4W-D4.AtS",
                        "mobile_link": "https://m.tokopedia.com/verify/device/dace840d-895b-4a60-a4a2-6052fa5d8f4e?entry=notif_center",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    },
                    {
                      "notif_id": "e4e0590d-f5ac-11ea-a2b1-00163e04bc82",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "filtron_this_is_me_pn",
                      "title": "Ada aktivitas login di perangkat baru",
                      "short_description": "Akunmu telah login melalui perangkat  pada Minggu,13/09/2020 17:35 WIB. Kalau ini bukan kamu, amankan akunmu di sini!",
                      "short_description_html": "Akunmu telah login melalui perangkat  pada Minggu,13/09/2020 17:35 WIB. Kalau ini bukan kamu, amankan akunmu di sini!",
                      "is_longer_content": false,
                      "button_text": "Klik di sini!",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "Kemarin",
                      "create_time_unix": 1599993351,
                      "update_time": "2020-09-13 17:36:02.414105991 +0700 WIB",
                      "update_time_unix": 1599993362,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 0,
                      "show_bottomsheet": false,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://webview?allow_override=false&url=https%3A%2F%2Fm.tokopedia.com%2Fverify%2Fdevice%2Faf2e0732-581c-4b5d-863c-2934e81fbfef%3Fentry%3Dnotif_center",
                        "desktop_link": "https://tkp.me/tm/af2e0732-581c-4b5d-863c-2934e81fbfef?entry=notif_center",
                        "info_thumbnail_url": "",
                        "mobile_link": "https://m.tokopedia.com/verify/device/af2e0732-581c-4b5d-863c-2934e81fbfef?entry=notif_center",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    },
                    {
                      "notif_id": "af6a7066-f151-11ea-abf1-00163e02a327",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "buyerinfo_create_242",
                      "title": "Hai, Muh! Listrik Kamu Mulai Habis?",
                      "short_description": "Jangan lupa beli Token Listrik! Sebelum token terlanjur habis, yuk beli lewat Tokopedia biar rumahmu aman dari mati listrik mendadak.",
                      "short_description_html": "Jangan lupa beli Token Listrik! Sebelum token terlanjur habis, yuk beli lewat Tokopedia biar rumahmu aman dari mati listrik mendadak.",
                      "is_longer_content": false,
                      "button_text": "Beli Sekarang",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "08 Sep",
                      "create_time_unix": 1599514372,
                      "update_time": "2020-09-09 11:14:57.824453356 +0700 WIB",
                      "update_time_unix": 1599624897,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 0,
                      "show_bottomsheet": false,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://digital/form?category_id=3&operator_id=6",
                        "desktop_link": "https://www.tokopedia.com/pln/token-listrik/",
                        "info_thumbnail_url": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/10/30/22796090/22796090_43334119-4672-46fe-950c-4af617294354.png",
                        "mobile_link": "https://www.tokopedia.com/pln/token-listrik/",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    },
                    {
                      "notif_id": "a2d5e863-ebed-11ea-926a-00163e02b0aa",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "rechargeapp_billstatement_notification",
                      "title": "Total Top-up & Tagihan kamu Rp 297.250",
                      "short_description": "Lihat rincian pengeluaranmu pada bulan Agustus 2020 di sini!",
                      "short_description_html": "Lihat rincian pengeluaranmu pada bulan Agustus 2020 di sini!",
                      "is_longer_content": false,
                      "button_text": "Klik di sini",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "01 Sep",
                      "create_time_unix": 1598921645,
                      "update_time": "2020-09-02 22:58:27.203882284 +0700 WIB",
                      "update_time_unix": 1599062307,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 0,
                      "show_bottomsheet": false,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://webview?allow_override=false&url=https%3A%2F%2Fpulsa.tokopedia.com%2Fbill-statement%2F%3Fperiod%3D202008",
                        "desktop_link": "https://pulsa.tokopedia.com/bill-statement?period=202008",
                        "info_thumbnail_url": "https://ecs7.tokopedia.net/recharge/langganan/mbs.png",
                        "mobile_link": "https://pulsa.tokopedia.com/bill-statement?period=202008",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    },
                    {
                      "notif_id": "af018cac-e9cc-11ea-ad9b-00163e02a327",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "ctx_notify_buyer_newphone",
                      "title": "Cobain yuk Langsung Laku!",
                      "short_description": "Hi, terima kasih sudah berbelanja di Tokopedia, tahukah kamu, sekarang kamu bisa menjual HP secara cepat, dan mudah dengan fitur Langsung Laku? Cobain yuk!",
                      "short_description_html": "Hi, terima kasih sudah berbelanja di Tokopedia, tahukah kamu, sekarang kamu bisa menjual HP secara cepat, dan mudah dengan fitur Langsung Laku? Cobain yuk!",
                      "is_longer_content": true,
                      "button_text": "Ke Langsung Laku",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "29 Agu",
                      "create_time_unix": 1598687590,
                      "update_time": "2020-09-02 22:58:27.203882284 +0700 WIB",
                      "update_time_unix": 1599062307,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 0,
                      "show_bottomsheet": true,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://discovery/langsung-laku",
                        "desktop_link": "https://tokopedia.com/discovery/langsung-laku",
                        "info_thumbnail_url": "",
                        "mobile_link": "https://m.tokopedia.com/notifikasi?tb=1",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    },
                    {
                      "notif_id": "4f229114-e838-11ea-8315-00163e02a327",
                      "user_id": 49032181,
                      "shop_id": 0,
                      "section_id": "for_you",
                      "section_key": "Info",
                      "section_icon": "https://cdn.tokopedia.net/img/notif-center/info.png",
                      "subsection_key": "",
                      "template_key": "buyerinfo_create_246",
                      "title": "Saldo Uang Elektronik Kamu Menipis? ",
                      "short_description": "Saldo menipis bikin nggak asyik, segera top-up di Tokopedia! Cuma butuh waktu singkat, saldonya bisa langsung dipakai untuk bayar tol, parkir, sampai belanjaan di minimarket.",
                      "short_description_html": "Saldo menipis bikin nggak asyik, segera top-up di Tokopedia! Cuma butuh waktu singkat, saldonya bisa langsung dipakai untuk bayar tol, parkir, sampai belanjaan di minimarket.",
                      "is_longer_content": true,
                      "button_text": "Top-Up Sekarang",
                      "content": "",
                      "type_of_user": 1,
                      "create_time": "27 Agu",
                      "create_time_unix": 1598513913,
                      "update_time": "2020-09-02 22:58:27.203882284 +0700 WIB",
                      "update_time_unix": 1599062307,
                      "status": 0,
                      "read_status": 1,
                      "type_link": 0,
                      "show_bottomsheet": true,
                      "type_bottomsheet": 0,
                      "data_notification": {
                        "app_link": "tokopedia://digital/form?category_id=34",
                        "desktop_link": "https://www.tokopedia.com/top-up/emoney/",
                        "info_thumbnail_url": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/10/31/22796090/22796090_2ef10800-68a0-44f1-a407-32a0f5ed1fde.png",
                        "mobile_link": "https://www.tokopedia.com/top-up/emoney/",
                        "checkout_url": ""
                      },
                      "product_data": [],
                      "total_product": 0
                    }
                  ],
                  "options": {
                    "longer_content": 150
                  },
                  "user_info": {
                    "user_id": 49032181,
                    "shop_id": 6543079,
                    "email": "",
                    "fullname": "Muh Isfhani Ghiath"
                  }
                }
            }
        """
    }

}