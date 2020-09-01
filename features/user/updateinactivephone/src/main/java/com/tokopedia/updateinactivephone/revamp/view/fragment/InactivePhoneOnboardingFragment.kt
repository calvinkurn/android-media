package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*

class InactivePhoneOnboardingFragment : BaseDaggerFragment() {

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface
    private lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentTransactionInterface = activity as FragmentTransactionInterface
        userSession = UserSession(context)

        btnUploadData?.setOnClickListener {
            // check has multiple account ?
            // check login with phone number ?
            // need improvement on login / register page to set login method
            if (userSession.loginMethod == UserSessionInterface.LOGIN_METHOD_PHONE) {
                gotoChooseAccount()
            } else {
                gotoOnboardingPage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
            gotoOnboardingPage()
        }
    }

    private fun gotoChooseAccount() {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, TokenGenerator().createBasicTokenGQL())
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun gotoOnboardingPage() {
        fragmentTransactionInterface.replace(InactivePhoneOnboardingIdCardFragment())
    }

    companion object {
        private const val REQUEST_CHOOSE_ACCOUNT = 100
    }
}