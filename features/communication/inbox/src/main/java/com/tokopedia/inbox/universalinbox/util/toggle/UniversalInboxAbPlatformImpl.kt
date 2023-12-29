package com.tokopedia.inbox.universalinbox.util.toggle

import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

class UniversalInboxAbPlatformImpl @Inject constructor(
    private val abTestPlatform: AbTestPlatform
) : UniversalInboxAbPlatform {

    override fun getString(key: String, defaultValue: String): String {
        return abTestPlatform.getString(key, defaultValue)
    }
}
