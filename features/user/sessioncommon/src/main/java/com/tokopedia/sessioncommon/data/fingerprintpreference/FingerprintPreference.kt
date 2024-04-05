package com.tokopedia.sessioncommon.data.fingerprintpreference

interface FingerprintPreference {
    fun saveUniqueIdIfEmpty(id: String)
    fun saveUniqueId(id: String)
    fun getUniqueId(): String
    fun isUniqueIdEmpty(): Boolean
    fun getOrCreateUniqueId(): String
    fun removeUniqueId()
}
