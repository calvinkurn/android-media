package com.tokopedia.loginfingerprint.view.helper

import android.app.Activity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.loginfingerprint.R

object BiometricPromptHelper {

    fun isBiometricAvailable(context: FragmentActivity?): Boolean {
        return isBiometricAvailableActivity(context)
    }

    fun isBiometricAvailableActivity(context: Activity?): Boolean {
        try {
            context?.let {
                return BiometricManager.from(context)
                    .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
            }
        } catch (e: Exception) { }
        return false
    }

    fun showBiometricPromptActivity(fragmentActivity: FragmentActivity, onSuccess: () -> Unit, onFailed: () -> Unit, onError: (Int, String) -> Unit) {
        val executor = ContextCompat.getMainExecutor(fragmentActivity)
        var biometricPrompt: BiometricPrompt? = null

        val callback =  object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode, errString.toString())
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

    fun showBiometricPromptFragment(fragmentActivity: FragmentActivity, fragment: Fragment, onSuccess: () -> Unit, onFailed: () -> Unit, onError: (Int, String) -> Unit) {
        val executor = ContextCompat.getMainExecutor(fragmentActivity)
        var biometricPrompt: BiometricPrompt? = null

        val callback =  object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode, errString.toString())
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

        biometricPrompt = BiometricPrompt(fragment, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragmentActivity.getString(R.string.title_main))
            .setNegativeButtonText(fragmentActivity.getString(R.string.button_close_fingerprint))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
