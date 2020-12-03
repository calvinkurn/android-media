package com.tokopedia.devicefingerprint.utils

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.payload.InsertDeviceInfoPayload
import com.tokopedia.encryption.security.md5
import javax.inject.Inject

class InsertDeviceInfoPayloadCreator @Inject constructor(
        private val contentCreator: ContentCreator,
        private val deviceInfoPayloadCreator: DeviceInfoPayloadCreator,
        private val gson: Gson
) {

    suspend fun create(): InsertDeviceInfoPayload {
        val deviceInfoPayload = deviceInfoPayloadCreator.createDevicePayload()
        val content = contentCreator.createContent(deviceInfoPayload)
        val identifier = gson.toJson(deviceInfoPayload).md5()
        return InsertDeviceInfoPayload(content, identifier)
    }

}
