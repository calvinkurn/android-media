package com.tokopedia.loginfingerprint.view.helper

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper

object BiometricPromptHelper {
    fun showBiometricPrompt(fragment: Fragment, onSuccess: () -> Unit, onFailed: () -> Unit, onError: (Int) -> Unit) {
        val executor = ContextCompat.getMainExecutor(fragment.activity)
        var counter = 0
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
                counter++

                if(counter == 3){
                    biometricPrompt?.cancelAuthentication()
                    FingerprintDialogHelper.showInvalidFingerprintDialog(fragment.activity)
                }
            }
        }

        biometricPrompt = BiometricPrompt(fragment, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Masuk dengan sidik jari")
            .setNegativeButtonText("Gunakan metode lain")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}