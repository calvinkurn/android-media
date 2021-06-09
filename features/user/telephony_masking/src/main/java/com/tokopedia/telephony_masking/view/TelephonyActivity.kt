package com.tokopedia.telephony_masking.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.telephony_masking.analytic.TelephonyAnalytics
import com.tokopedia.telephony_masking.util.TelephonyMaskingConst
import com.tokopedia.telephony_masking.util.TelephonyMaskingRedirectionUtil
import com.tokopedia.user.session.UserSession

class TelephonyActivity: BaseSimpleActivity() {

    private var telephonyAnalytics: TelephonyAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        telephonyAnalytics = TelephonyAnalytics(UserSession(this))
        showBottomSheet()
    }

    private fun showBottomSheet() {
        TelephonyBottomSheet.show(this,
                ::onClickGiveAccess, ::onClickNantiSaja, ::onClickClose)
    }

    private fun onClickGiveAccess() {
        telephonyAnalytics?.eventGiveAccess()
        val telephonyMasking = TelephonyMaskingRedirectionUtil()
        val intent = telephonyMasking.createIntentSavePhoneNumbers(this)
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
        setResult(RESULT_OK)
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