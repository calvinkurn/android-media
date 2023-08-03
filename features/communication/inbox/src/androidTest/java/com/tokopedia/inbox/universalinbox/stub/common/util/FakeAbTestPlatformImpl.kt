package com.tokopedia.inbox.universalinbox.stub.common.util

import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.ROLLENCE_KEY
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.ROLLENCE_TYPE_A
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform

class FakeAbTestPlatformImpl : UniversalInboxAbPlatform {

    private val map = hashMapOf(Pair(ROLLENCE_KEY, ROLLENCE_TYPE_A))

    init {
    }

    override fun getString(key: String, defaultValue: String): String {
        return map[key] ?: defaultValue
    }

    fun editValue(key: String, defaultValue: String) {
        map[key] = defaultValue
    }
}
