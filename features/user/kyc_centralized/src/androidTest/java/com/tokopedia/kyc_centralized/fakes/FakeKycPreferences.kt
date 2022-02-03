package com.tokopedia.kyc_centralized.fakes

import com.tokopedia.kyc_centralized.util.KycSharedPreferenceInterface

class FakeKycPreferences: KycSharedPreferenceInterface {
    override fun saveByteArrayCache(key: String, data: ByteArray) {
        return
    }

    override fun getByteArrayCache(key: String): ByteArray? {
        return "".toByteArray(Charsets.ISO_8859_1)
    }

    override fun removeCache(key: String) {
        return
    }
}