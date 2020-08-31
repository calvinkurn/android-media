package com.tokopedia.loginfingerprint.data.preference

/**
 * Created by Yoris Prayogo on 2020-02-27.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
interface FingerprintSetting {
    fun registerFingerprint()
    fun saveUserId(userId: String)
    fun removeUserId()
    fun getFingerprintUserId(): String
    fun unregisterFingerprint()
    fun isFingerprintRegistered(): Boolean
}