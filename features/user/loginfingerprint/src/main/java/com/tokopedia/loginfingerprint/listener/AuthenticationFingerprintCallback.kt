package com.tokopedia.loginfingerprint.listener

/**
 * Created by Yoris on 09/08/21.
 */

interface AuthenticationFingerprintCallback {
    fun onShowFingerprintAuthentication(onSuccess: () -> Unit, onFailed: () -> Unit, onError: (code: Int, msg: String) -> Unit )
}