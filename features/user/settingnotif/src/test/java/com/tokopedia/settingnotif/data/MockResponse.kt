package com.tokopedia.settingnotif.data

object MockResponse {

    val pushNotificationResponse = """
        {
            "notifier_notificationGetUserAllSettings": {
              "pushnotif": [
                {
                  "section": "Aktivitas",
                  "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/aktivitas.png",
                  "list_settings": [
                    {
                      "name": "Chat Promosi dari Penjual",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/chat.png",
                      "key": "chat_promosi_dari_penjual",
                      "status": true,
                      "description": "",
                      "list_settings": []
                    },
                    {
                      "name": "Feed",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/feed.png",
                      "key": "aktivitas_feed",
                      "status": true,
                      "description": "",
                      "list_settings": []
                    },
                    {
                      "name": "Diskusi Produk",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/diskusi.png",
                      "key": "diskusi_produk",
                      "status": true,
                      "description": "",
                      "list_settings": []
                    },
                    {
                      "name": "Emas",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/emas.png",
                      "key": "emas",
                      "status": true,
                      "description": "",
                      "list_settings": []
                    },
                    {
                      "name": "TokoMember",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/toko_member.png",
                      "key": "toko_member",
                      "status": true,
                      "description": "",
                      "list_settings": []
                    },
                    {
                      "name": "Games",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/games.png",
                      "key": "games",
                      "status": true,
                      "description": "",
                      "list_settings": []
                    }
                  ]
                },
                {
                  "section": "Promo",
                  "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/promo.png",
                  "list_settings": [
                    {
                      "name": "Rekomendasi Untukmu",
                      "icon": "https://images.tokopedia.net/img/notif-center/notification-settings/produk.png",
                      "key": "promo",
                      "status": true,
                      "description": "Dapatkan info promosi terkini di Tokopedia",
                      "list_settings": []
                    }
                  ]
                }
              ]
            }
        }
    """.trimIndent()

}
