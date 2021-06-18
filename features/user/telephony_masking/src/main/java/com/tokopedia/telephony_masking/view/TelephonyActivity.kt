package com.tokopedia.telephony_masking.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.telephony_masking.analytic.TelephonyAnalytics
import com.tokopedia.telephony_masking.util.TelephonyMaskingConst
import com.tokopedia.telephony_masking.util.TelephonyMaskingRedirectionUtil
import com.tokopedia.user.session.UserSession

/**
 * How to use:
 * RouteManager.getIntent(this, [ApplinkConst.TELEPHONY_MASKING]), use startActivityForResult
 * Check the result code in [TelephonyMaskingConst] for other results beside OK and Canceled
 */
class TelephonyActivity: BaseSimpleActivity() {

    private var telephonyAnalytics: TelephonyAnalytics? = null
    private var remoteConfig: RemoteConfig? = null
    private var sharedPreference: SharedPreferences? = null
    private var numbers: String = ""
    private var redirectPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        telephonyAnalytics = TelephonyAnalytics(UserSession(this))
        sharedPreference = getSharedPreferences(
                TelephonyMaskingConst.PREFERENCE_NAME, Context.MODE_PRIVATE)
        numbers = getNumbers()
        redirectPath = intent.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_REDIRECT_PATH)
        if(isNumbersInLocalCache()) {
            onContactAlreadyExist()
        } else {
            deleteNumbersInLocalCache()
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        TelephonyBottomSheet.show(this,
                ::onClickGiveAccess, ::onClickNantiSaja, ::onClickClose)
    }

    private fun getListNumbers(): List<String> {
        return numbers.split(",")
    }

    private fun getNumbers(): String {
        if(remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }
        return remoteConfig?.getString(
                TelephonyMaskingConst.TELEPHONY_MASKING_KEY,
                TelephonyMaskingConst.CONTACT_NUMBERS_DEFAULT
        ) ?: TelephonyMaskingConst.CONTACT_NUMBERS_DEFAULT
    }

    private fun isNumbersInLocalCache(): Boolean {
        val localCacheNumbers: String = sharedPreference?.getString(
                TelephonyMaskingConst.KEY_LOCAL_NUMBERS, "")?: ""
        return numbers == localCacheNumbers
    }

    private fun saveNumbersToLocalCache() {
        sharedPreference?.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.putString(TelephonyMaskingConst.KEY_LOCAL_NUMBERS, numbers)
            editor.apply()
        }
    }

    private fun deleteNumbersInLocalCache() {
        sharedPreference?.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.remove(TelephonyMaskingConst.KEY_LOCAL_NUMBERS)
            editor.apply()
        }
    }

    private fun onClickGiveAccess() {
        telephonyAnalytics?.eventGiveAccess()
        val telephonyMasking = TelephonyMaskingRedirectionUtil()
        val intent = telephonyMasking.createIntentSavePhoneNumbers(this, getListNumbers())
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun onClickNantiSaja() {
        telephonyAnalytics?.eventNantiSaja()
        setResult(TelephonyMaskingConst.RESULT_SKIP)
        redirect()
        finish()
    }

    private fun onClickClose() {
        telephonyAnalytics?.eventClose()
        setResult(RESULT_CANCELED)
        redirect()
        finish()
    }

    private fun onSaveContact() {
        telephonyAnalytics?.eventSaveContact()
        saveNumbersToLocalCache()
        setResult(RESULT_OK)
    }

    private fun onContactAlreadyExist() {
        setResult(TelephonyMaskingConst.RESULT_ALREADY_EXIST)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE) {
            when(resultCode) {
                RESULT_OK -> onSaveContact()
                RESULT_CANCELED -> setResult(TelephonyMaskingConst.RESULT_NOT_SAVED)
            }
            if(!redirectPath.isNullOrEmpty()) {
                redirect()
            }
            finish()
        }
    }

    private fun isWebViewPage(): Boolean {
        return redirectPath?.startsWith(TelephonyMaskingConst.REDIRECT_WEB) == true
    }

    private fun redirect() {
        try {
            if(isWebViewPage()) {
                val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.WEBVIEW, redirectPath)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        } catch (ignored: Throwable) {}
    }

    override fun getNewFragment(): Fragment? = null

    override fun getToolbarResourceID(): Int = 0

    companion object {
        private const val REQUEST_CODE = 9901
    }
}