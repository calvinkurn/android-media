package com.tokopedia.devicefingerprint.submitdevice.utils

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.submitdevice.payload.InsertDeviceInfoPayload
import com.tokopedia.encryption.security.md5
import javax.inject.Inject

class InsertDeviceInfoPayloadCreator @Inject constructor(
        private val contentCreator: ContentCreator,
        private val deviceInfoPayloadCreator: DeviceInfoPayloadCreator,
        private val gson: Gson
) {

    suspend fun create(): InsertDeviceInfoPayload {
        val deviceInfoPayload = deviceInfoPayloadCreator.createDevicePayload()
        val json = gson.toJson(deviceInfoPayload)
        val content = contentCreator.createContent(json)
        val identifier = json.md5()
        return InsertDeviceInfoPayload(content, identifier)
    }

}
