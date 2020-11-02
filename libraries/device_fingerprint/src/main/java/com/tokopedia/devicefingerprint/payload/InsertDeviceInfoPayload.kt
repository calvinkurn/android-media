package com.tokopedia.devicefingerprint.payload

data class InsertDeviceInfoPayload(
        val content: String,
        val identifier: String,
        val version: String = "1"
)
