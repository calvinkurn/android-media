package com.tokopedia.devicefingerprint.submitdevice.utils

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.submitdevice.payload.InsertDeviceInfoPayload
import com.tokopedia.encryption.security.md5
import javax.inject.Inject

class InsertDeviceInfoPayloadCreator @Inject constructor(
        private val contentCreator: dagger.Lazy<ContentCreator>,
        private val deviceInfoPayloadCreator: dagger.Lazy<DeviceInfoPayloadCreator>,
        private val gson: dagger.Lazy<Gson>
) {

    suspend fun create(): InsertDeviceInfoPayload {
        val deviceInfoPayload = deviceInfoPayloadCreator.get().createDevicePayload()
        val json = gson.get().toJson(deviceInfoPayload)
        val content = contentCreator.get().createContent(json)
        val identifier = json.md5()
        return InsertDeviceInfoPayload(content, identifier)
    }

}
