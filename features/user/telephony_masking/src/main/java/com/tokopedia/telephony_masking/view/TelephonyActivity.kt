package com.tokopedia.telephony_masking.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.telephony_masking.analytic.TelephonyAnalytics
import com.tokopedia.telephony_masking.util.TelephonyMaskingConst
import com.tokopedia.telephony_masking.util.TelephonyMaskingRedirectionUtil
import com.tokopedia.track.TrackApp
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        telephonyAnalytics = TelephonyAnalytics(UserSession(this), TrackApp.getInstance())
        sharedPreference = getSharedPreferences(
                TelephonyMaskingConst.PREFERENCE_NAME, Context.MODE_PRIVATE)
        numbers = getNumbers()
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
        finish()
    }

    private fun onClickClose() {
        telephonyAnalytics?.eventClose()
        setResult(RESULT_CANCELED)
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
            finish()
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getToolbarResourceID(): Int = 0

    companion object {
        private const val REQUEST_CODE = 9901
    }
}