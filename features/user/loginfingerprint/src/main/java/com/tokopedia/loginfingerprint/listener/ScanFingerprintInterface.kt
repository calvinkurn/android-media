package com.tokopedia.loginfingerprint.listener

/**
 * Created by Yoris Prayogo on 2020-01-22.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

interface ScanFingerprintInterface {
    fun onFingerprintValid()
    fun onFingerprintError(msg: String, errCode: Int)
}