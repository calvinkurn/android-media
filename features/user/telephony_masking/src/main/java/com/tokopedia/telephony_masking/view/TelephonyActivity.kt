package com.tokopedia.telephony_masking.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.telephony_masking.util.TelephonyMaskingRedirectionUtil

class TelephonyActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBottomSheet()
    }

    private fun showBottomSheet() {
        val telephonyMasking = TelephonyMaskingRedirectionUtil()
        TelephonyBottomSheet.show(this, {
            val intent = telephonyMasking.createIntentSavePhoneNumbers(this)
            startActivityForResult(intent, REQUEST_CODE)
        }, {
            //TODO: Tracker skip
            setResult(RESULT_CANCELED)
            finish()
        }, {
            //TODO: Tracker close
            setResult(RESULT_CANCELED)
            finish()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE) {
            when(resultCode) {
                RESULT_OK -> {
                    setResult(RESULT_OK)
                    //TODO: Tracker save
                }
                RESULT_CANCELED -> {
                    setResult(RESULT_CANCELED)
                    //TODO: Nothing
                }
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