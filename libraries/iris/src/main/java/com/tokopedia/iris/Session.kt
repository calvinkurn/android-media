package com.tokopedia.iris

interface Session {
    fun getUserId(): String
    fun getSessionId(): String
    fun setUserId(id: String)
    fun setSessionId(id: String)
}
