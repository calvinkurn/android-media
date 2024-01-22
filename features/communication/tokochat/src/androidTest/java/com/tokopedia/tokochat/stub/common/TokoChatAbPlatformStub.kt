package com.tokopedia.tokochat.stub.common

import com.tokopedia.tokochat.util.toggle.TokoChatAbPlatform

class TokoChatAbPlatformStub : TokoChatAbPlatform {

    val listAB: HashMap<String, String> = hashMapOf()
    override fun getString(key: String, defaultValue: String): String {
        return listAB[key] ?: defaultValue
    }
}
