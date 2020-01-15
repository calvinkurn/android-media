package com.tokopedia.wallet.ovoactivation.view

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Created by nabillasabbaha on 20/09/18.
 */
class ActivationOvoActivity : BaseOvoActivationActivity() {

    override fun getNewFragment(): Fragment? {
        var registeredApplink = ""
        var phoneNumber= ""
        var changeMsisdnApplink= ""

        intent.data?.let {
             registeredApplink = it.getQueryParameter(REGISTERED_APPLINK) ?: ""
             phoneNumber = it.getQueryParameter(PHONE_NUMBER) ?: ""
             changeMsisdnApplink = it.getQueryParameter(CHANGE_MSISDN_APPLINK) ?: ""
        }
        if (registeredApplink.isNullOrBlank() && phoneNumber.isNullOrBlank() && changeMsisdnApplink.isNullOrBlank()){
            intent.extras?.let {
                registeredApplink = it.getString(REGISTERED_APPLINK) ?: ""
                phoneNumber = it.getString(PHONE_NUMBER) ?: ""
                changeMsisdnApplink = it.getString(CHANGE_MSISDN_APPLINK) ?: ""
            }
        }

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
