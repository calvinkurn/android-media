package com.tokopedia.tokochat.util.toggle

interface TokoChatAbPlatform {
    fun getString(key: String, defaultValue: String): String
}
