package com.tokopedia.loginfingerprint.view.helper

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tokopedia.loginfingerprint.R

object BiometricPromptHelper {

    fun isBiometricAvailable(context: FragmentActivity?): Boolean {
        context?.let {
            return BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
        }
        return false
    }

    fun showBiometricPrompt(fragmentActivity: FragmentActivity, onSuccess: () -> Unit, onFailed: () -> Unit, onError: (Int) -> Unit) {
        val executor = ContextCompat.getMainExecutor(fragmentActivity)
        var biometricPrompt: BiometricPrompt? = null

        val callback =  object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode)
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        }

        biometricPrompt = BiometricPrompt(fragmentActivity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragmentActivity.getString(R.string.title_main))
            .setNegativeButtonText(fragmentActivity.getString(R.string.button_close_fingerprint))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}