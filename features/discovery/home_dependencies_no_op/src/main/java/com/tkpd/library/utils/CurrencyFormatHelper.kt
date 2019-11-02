package com.tkpd.library.utils

class CurrencyFormatHelper {
    companion object {
        @JvmStatic
        fun convertRupiahToInt(rupiah: String): Int {
            var rupiah = rupiah
            rupiah = rupiah.replace("Rp", "")
            rupiah = rupiah.replace(".", "")
            rupiah = rupiah.replace(" ", "")
            return Integer.parseInt(rupiah)
        }
    }
}