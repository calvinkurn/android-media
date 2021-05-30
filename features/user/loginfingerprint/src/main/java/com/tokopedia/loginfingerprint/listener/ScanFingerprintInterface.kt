package com.tokopedia.loginfingerprint.listener

/**
 * Created by Yoris Prayogo on 2020-01-22.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

interface ScanFingerprintInterface {
    /* Called when scanned fingerprint valid and mode != LOGIN */
    fun onFingerprintValid()

    /* Called when user encounter error or scanned fingerprint is not valid and mode != LOGIN */
    fun onFingerprintError(msg: String, errCode: Int)

    /* Only called when mode = LOGIN and after login token success */
    fun onLoginFingerprintSuccess()
}