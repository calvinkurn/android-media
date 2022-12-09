package com.tokopedia.notifications.domain.query

const val GQL_QUERY_SEND_NOTIF_SETTINGS_TRACKER_DATA = """
   query(${"$"}type: String, ${"$"}data: [Notifier_NotificationSettingsField]) {
  notifier_setUserSpecificSettings(type: ${"$"}type, data: ${"$"}data) {
    is_success
    error_message
    user_settings {
      user_id
      settings {
        pushnotif {
          transaksi_penjualan
          transaksi_pembelian
          chat_dari_tokopedia
          chat_promosi_dari_penjual
          chat_personal
          diskusi_produk
          ulasan
          aktivitas
          aktivitas_feed
          aktivitas_pemberitahuan
          aktivitas_penawaran_khusus
          promo_untuk_pembeli
          promo_untuk_penjual
          info_untuk_pembeli
          info_untuk_penjual
          info_monthly_report
          info_weekly_report
          tokopedia_play
          bulletin_newsletter
          promo
          salam_sholat_imsak
          salam_sholat_subuh
          salam_sholat_dhuha
          salam_sholat_dzuhur
          salam_sholat_ashar
          salam_sholat_maghrib
          salam_sholat_isya
          salam_quran_ayat_terakhir
          salam_quran_doa_harian
          }
        email {
            transaksi_penjualan
            transaksi_pembelian
            chat_dari_tokopedia
            chat_promosi_dari_penjual
            chat_personal
            diskusi_produk
            ulasan
            aktivitas
            aktivitas_feed
            aktivitas_pemberitahuan
            aktivitas_penawaran_khusus
            promo_untuk_pembeli
            promo_untuk_penjual
            info_untuk_pembeli
            info_untuk_penjual
            info_monthly_report
            info_weekly_report
            tokopedia_play
            bulletin_newsletter
            promo
            salam_sholat_imsak
            salam_sholat_subuh
            salam_sholat_dhuha
            salam_sholat_dzuhur
            salam_sholat_ashar
            salam_sholat_maghrib
            salam_sholat_isya
            salam_quran_ayat_terakhir
            salam_quran_doa_harian
        }
      sms {
          transaksi_penjualan
          transaksi_pembelian
          chat_dari_tokopedia
          chat_promosi_dari_penjual
          chat_personal
          diskusi_produk
          ulasan
          aktivitas
          aktivitas_feed
          aktivitas_pemberitahuan
          aktivitas_penawaran_khusus
          promo_untuk_pembeli
          promo_untuk_penjual
          info_untuk_pembeli
          info_untuk_penjual
          info_monthly_report
          info_weekly_report
          tokopedia_play
          bulletin_newsletter
          promo
          salam_sholat_imsak
          salam_sholat_subuh
          salam_sholat_dhuha
          salam_sholat_dzuhur
          salam_sholat_ashar
          salam_sholat_maghrib
          salam_sholat_isya
          salam_quran_ayat_terakhir
          salam_quran_doa_harian
      }
    }
  }
}
}"""



