package com.tokopedia.privacycenter.dsar

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

    const val STATUS_REJECTED = "REJECTED"
    const val STATUS_COMPLETED = "COMPLETED"
    const val STATUS_CLOSED = "CLOSED"

    const val PERSONAL_LABEL = "<b>Informasi Pribadi:</b><br>User ID, Nama Lengkap, Nomor HP, E-mail, Tanggal Lahir, Jenis Kelamin, Daftar Alamat Pengiriman"
    const val PAYMENT_LABEL = "<b>Informasi Pembayaran:</b><br>Daftar Rekening Bank, Daftar Kartu Kredit, Daftar Kartu Debit"
    const val TRANSACTION_LABEL = "<b>Riwayat Transaksi:</b><br>"

    const val SUMMARY_ERROR = "Pilih minimal 1 data"
    const val HTML_NEW_LINE = "<br>"

    const val TRANSACTION_HISTORY_PREFIX = "transaction_history"

    const val LABEL_RANGE_YEARS = "Selama Tahun Ini"
    const val LABEL_RANGE_3_YEARS = "3 Tahun Terakhir"
    const val LABEL_RANGE_3_MONTHS = "3 Bulan Terakhir"
    const val LABEL_RANGE_30_DAYS = "30 Hari Terakhir"
    const val LABEL_RANGE_WEEKLY = "7 Hari Terakhir"
    const val LABEL_RANGE_CUSTOM = "Pilih Tanggal Sendiri"

}
