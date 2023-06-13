package com.tokopedia.kyc_centralized.fakes

import com.tokopedia.kyc_centralized.util.KycSharedPreference

class FakeKycPreferences: KycSharedPreference {
    override fun saveByteArrayCache(key: String, data: ByteArray) {
        return
    }

    override fun getByteArrayCache(key: String): ByteArray? {
        return "".toByteArray(Charsets.ISO_8859_1)
    }

    override fun removeCache(key: String) {
        return
    }

    override fun saveStringCache(key: String, value: String) {
        return
    }

    override fun getStringCache(key: String): String {
        return ""
    }

    override fun removeStringCache(key: String) {
        return
    }
}
