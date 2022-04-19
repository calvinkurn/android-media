package com.tokopedia.common.payment.utils

/**
 * Created by Yoris on 01/10/21.
 */

object LinkStatusMatcher {
    fun getStatus(status: String): String {
        return when(status) {
            "1" -> "Hore! GoPay kamu udah aktif. Kamu bisa pakai GoPay & GoPay Coins di Tokopedia."
            "2" -> "Hore! Akunmu udah kesambung. Kamu bisa pakai GoPay & GoPay Coins di Tokopedia."
            "3" -> "Akunmu kesambung, tapi level Rewards & GoClub masih dicocokin. Tunggu notifikasi dari kami."
            "4" -> "Akunmu kesambung, tapi level Rewards & GoClub gagal dicocokin. Cek infonya di Tokopedia Care."
            "5" -> "Akunmu kesambung, tapi GoPay & GoPay Coins kamu masih diaktifin. Tunggu notifikasi dari kami."
            "6" -> "Akunmu kesambung, tapi semua keuntungannya masih diproses. Tunggu notifikasi dari kami."
            "7" -> "GoPay & GoPay Coins lagi diaktifin, level Rewards & GoClub gagal dicocokin. Cek infonya di Akun Saya."
            "8" -> "Akunmu kesambung, tapi GoPay & GoPay Coins kamu belum aktif. Kamu bisa coba aktifin lagi."
            "9" -> "Level Rewards & GoClub lagi dicocokin, GoPay & GoPay Coins belum aktif. Cek infonya di Akun Saya."
            "10" -> "Akunmu kesambung, tapi semua keuntungannya gagal diproses. Coba hubungi Tim Tokopedia Care."
            else -> ""
        }
    }
}

const val LINK_ACCOUNT_BACK_BUTTON_APPLINK = "tokopedia://back"
const val LINK_ACCOUNT_SOURCE_PAYMENT = "app_payment"