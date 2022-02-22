package com.tokopedia.digital_product_detail.presentation.util

import com.google.gson.Gson
import java.io.File

fun Gson.JsonToString(path: String): String {
    val uri = this.javaClass.classLoader?.getResource(path)
    val file = File(uri?.path)
    return String(file.readBytes())
}