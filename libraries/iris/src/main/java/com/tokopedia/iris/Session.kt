package com.tokopedia.iris

interface Session {
    fun getUserId(): String
    fun getDeviceId(): String
    fun getSessionId(): String
    fun setUserId(id: String)
    fun setDeviceId(id: String)
    fun setSessionId(id: String)
}
