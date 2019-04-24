package com.tokopedia.loginphone.addname

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.loginphone.addname.fragment.AddNameRegisterPhoneFragment
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 22/04/19.
 */

class AddNameRegisterPhoneActivity : BaseSimpleActivity() {

    companion object {
        val PARAM_PHONE = "phone"
    }

    object DeepLinkIntents {
        @DeepLink(ApplinkConst.ADD_NAME_REGISTER)
        @JvmStatic
        fun getCallingApplinkIntent(context: Context, bundle: Bundle): Intent {
            val uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon()
            val intent = Intent(context, AddNameRegisterPhoneActivity::class.java)
            intent.putExtras(bundle)
            val userSession = UserSession(context)
            val phone = bundle.getString(PARAM_PHONE, "0")

            if (userSession.isLoggedIn) {
                return if (context.applicationContext is ApplinkRouter) {
                    (context.applicationContext as ApplinkRouter).getApplinkIntent(context, ApplinkConst.HOME)
                } else {
                    throw RuntimeException("Applinks intent unsufficient")
                }
            } else {
                val intent = newInstance(context, phone)
                return intent.setData(uri.build())
            }
        }

        fun newInstance(context: Context, phoneNumber: String): Intent {
            val intent = Intent(context, AddNameRegisterPhoneActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_PHONE, phoneNumber)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddNameRegisterPhoneFragment.createInstance(bundle)
    }

}