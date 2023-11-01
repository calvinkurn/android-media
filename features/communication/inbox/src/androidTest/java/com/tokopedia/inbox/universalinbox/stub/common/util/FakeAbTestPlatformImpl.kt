package com.tokopedia.inbox.universalinbox.stub.common.util

import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform

class FakeAbTestPlatformImpl : UniversalInboxAbPlatform {

    private val map = hashMapOf<String, String>()

    override fun getString(key: String, defaultValue: String): String {
        return map[key] ?: defaultValue
    }

    fun editValue(key: String, defaultValue: String) {
        map[key] = defaultValue
    }
}
