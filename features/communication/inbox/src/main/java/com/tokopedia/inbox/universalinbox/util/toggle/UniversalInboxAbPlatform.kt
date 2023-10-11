package com.tokopedia.inbox.universalinbox.util.toggle

interface UniversalInboxAbPlatform {
    fun getString(key: String, defaultValue: String): String
}
