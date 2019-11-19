package com.tokopedia.wallet.ovoactivation.view

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Created by nabillasabbaha on 20/09/18.
 */
class ActivationOvoActivity : BaseOvoActivationActivity() {

    override fun getNewFragment(): Fragment? {
        val registeredApplink = intent.data?.getQueryParameter(REGISTERED_APPLINK)?: ""
        val phoneNumber = intent.data?.getQueryParameter(PHONE_NUMBER)?: ""
        val changeMsisdnApplink = intent.data?.getQueryParameter(CHANGE_MSISDN_APPLINK)?:""
        return ActivationOvoFragment.newInstance(registeredApplink, phoneNumber, changeMsisdnApplink)
    }

    companion object {

        val REGISTERED_APPLINK = "applink_registered"
        val PHONE_NUMBER = "phone_number"
        val CHANGE_MSISDN_APPLINK = "applink_change_msisdn"

        fun newInstance(context: Context): Intent {
            return Intent(context, ActivationOvoActivity::class.java)
        }
    }
}
