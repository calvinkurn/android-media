package com.tokopedia.recentview.ext

import java.lang.Exception

/**
 * Created by Lukas on 13/11/20.
 */

fun String.convertRupiahToInt(): Int {
    return try {
        var rupiah = this
        rupiah = rupiah.replace("Rp", "")
        rupiah = rupiah.replace(".", "")
        rupiah = rupiah.replace(" ", "")
        rupiah.toInt()
    } catch (e: Exception){
        0
    }
}