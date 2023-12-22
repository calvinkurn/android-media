package com.tokopedia.tokochat.util.toggle

import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

class TokoChatAbPlatformImpl @Inject constructor(
    private val abTestPlatform: AbTestPlatform
) : TokoChatAbPlatform {

    override fun getString(key: String, defaultValue: String): String {
        return abTestPlatform.getString(key, defaultValue)
    }
}
