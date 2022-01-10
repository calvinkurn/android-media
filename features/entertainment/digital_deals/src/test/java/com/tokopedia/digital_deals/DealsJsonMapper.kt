package com.tokopedia.digital_deals

import java.io.File

object DealsJsonMapper{
    fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}