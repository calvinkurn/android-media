package com.tokopedia.devicefingerprint.model

data class InsertDeviceInfoPayload(
        val content: String,
        val identifier: String,
        val version: String = "1"
)
