package com.tokopedia.sessioncommon.data.fingerprint

interface FingerprintPreference {
    fun saveUniqueIdIfEmpty(id: String)
    fun saveUniqueId(id: String)
    fun getUniqueId(): String
    fun isUniqueIdEmpty(): Boolean
}