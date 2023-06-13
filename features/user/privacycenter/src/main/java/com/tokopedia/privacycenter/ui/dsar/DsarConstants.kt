package com.tokopedia.privacycenter.ui.dsar

object DsarConstants {
    const val FILTER_TYPE_PERSONAL = "personal"
    const val FILTER_TYPE_PAYMENT = "payment"
    const val FILTER_TYPE_TRANSACTION = "transaction"

    const val DATE_RANGE_YEARS = 0
    const val DATE_RANGE_3_YEARS = 1
    const val DATE_RANGE_3_MONTHS = 2
    const val DATE_RANGE_30_DAYS = 3
    const val DATE_RANGE_WEEKLY = 4
    const val DATE_RANGE_CUSTOM = 5

    const val ONE_TRUST_FORMAT_1 = "yyyyMMdd"

    val DSAR_STATUS = listOf("rejected", "complete", "closed")

    const val PERSONAL_LABEL = "<b>Informasi Pribadi:</b><br>User ID, nama lengkap, nomor HP, e-mail, tanggal lahir, jenis kelamin, daftar alamat pengiriman"
    const val PAYMENT_LABEL = "<b>Informasi Pembayaran:</b><br>Daftar rekening bank, daftar kartu kredit, daftar kartu debit"
    const val TRANSACTION_LABEL = "<b>Riwayat Transaksi:</b><br>"

    const val SUMMARY_ERROR = "Pilih jenis data yang mau di-download dulu, ya."
    const val HTML_NEW_LINE = "<br>"

    const val TRANSACTION_HISTORY_PREFIX = "transaction_history"

    const val LABEL_RANGE_YEARS = "Selama Tahun Ini"
    const val LABEL_RANGE_3_YEARS = "3 Tahun Terakhir"
    const val LABEL_RANGE_3_MONTHS = "3 Bulan Terakhir"
    const val LABEL_RANGE_30_DAYS = "30 Hari Terakhir"
    const val LABEL_RANGE_WEEKLY = "7 Hari Terakhir"
    const val LABEL_RANGE_CUSTOM = "Pilih Tanggal Sendiri"

    val DSAR_PERSONAL_DATA = listOf(
        "full_name",
        "mailing_address",
        "phone_number",
        "email",
        "dob",
        "gender"
    )

    val DSAR_PAYMENT_DATA = listOf(
        "bank_account",
        "payment"
    )

    const val DAYS_7 = 7L
    const val DAYS_30 = 30L
    const val DAYS_90 = 90L
    const val DAYS_92 = 92L
    const val DAYS_3_YEARS = 1096L

    const val MAX_SELECTED_ITEM = 3

    const val HEADER_TEXT_PLAIN = "text/plain"
    const val APPLICATION_JSON = "application/json"

    const val HEADER_ACCEPT = "Accept"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val HEADER_AUTHORIZATION = "Authorization"

    const val BEARER = "Bearer"

    const val LANG_US = "en-us"

    const val OTP_MODE_EMAIL = "email"

    const val LABEL_ERROR_REQUEST = "Yaah, lagi ada gangguan. Tunggu sebentar & coba lagi, ya."
}
