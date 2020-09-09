package com.tokopedia.devicefingerprint.utils

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.payload.InsertDeviceInfoPayload
import java.security.MessageDigest
import javax.inject.Inject

class InsertDeviceInfoPayloadCreator @Inject constructor(
        private val contentCreator: ContentCreator,
        private val deviceInfoPayloadCreator: DeviceInfoPayloadCreator,
        private val gson: Gson
) {

    suspend fun create(): InsertDeviceInfoPayload {
        val deviceInfoPayload = deviceInfoPayloadCreator.createDevicePayload()
        val content = contentCreator.createContent(deviceInfoPayload)
        val identifier = md5(gson.toJson(deviceInfoPayload))
        return InsertDeviceInfoPayload(content, identifier)
    }

    private fun md5(string: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(string.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

}
