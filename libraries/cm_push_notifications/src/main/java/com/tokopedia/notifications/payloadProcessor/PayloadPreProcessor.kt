package com.tokopedia.notifications.payloadProcessor

import android.os.Bundle
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream

open class PayloadPreProcessor {

    private val dataByteKey = "dataBytes"

    fun isCompressedPayload(extras: Map<String, String>?): Boolean {
        if (extras == null)
            return false
        else if (extras.containsKey(dataByteKey)) {
            val dataBytes = extras[dataByteKey]
            return !dataBytes.isNullOrBlank()
        }
        return false
    }

    @Throws(Exception::class)
    fun getDecompressPayload(extras: Map<String, String>?): String {
        return when {
            (extras == null) -> ""
            (extras.containsKey(dataByteKey)
                    && !extras[dataByteKey].isNullOrBlank()) -> {
                val dataByteStr = extras[dataByteKey]
                return if (dataByteStr != null)
                    decodeGZIPDataToPayload(dataByteStr)
                else ""
            }
            else -> ""

        }
    }

    @Throws(Exception::class)
    private fun decodeGZIPDataToPayload(str: String): String {
        try {
            val data: ByteArray = Base64.decode(str, Base64.DEFAULT)
            val baos = ByteArrayOutputStream()
            GZIPInputStream(ByteArrayInputStream(data)).use { gis ->
                var b: Int
                while (gis.read().also { b = it } != -1) {
                    baos.write(b)
                }
            }
            return String(baos.toByteArray(), Charset.defaultCharset())
        }catch (e : Exception){
            throw Exception("Payload decode error")
        }
    }

    fun convertMapToBundle(map: Map<String, String>?): Bundle {
        val bundle = Bundle(map?.size ?: 0)
        if (map != null) {
            for ((key, value) in map) {
                bundle.putString(key, value)
            }
        }
        return bundle
    }

}