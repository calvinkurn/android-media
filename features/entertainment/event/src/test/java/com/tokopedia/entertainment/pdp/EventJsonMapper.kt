package com.tokopedia.entertainment.pdp

import java.io.File

object EventJsonMapper{
    fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}