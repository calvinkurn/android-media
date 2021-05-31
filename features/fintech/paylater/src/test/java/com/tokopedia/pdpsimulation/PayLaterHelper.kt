package com.tokopedia.pdpsimulation

import java.io.File

object PayLaterHelper {

    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}