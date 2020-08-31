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
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*
import javax.inject.Inject

class InactivePhoneOnboardingFragment : BaseDaggerFragment() {

//    @Inject
//    lateinit var userSession: UserSession

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentTransactionInterface = activity as FragmentTransactionInterface

        btnUploadData?.setOnClickListener {
            // check has multiple account ?
            // check login with phone number ?
            // need improvement on login / register page to set login method
//            if (userSession.loginMethod != UserSessionInterface.LOGIN_METHOD_PHONE) {
//
//            }

//            gotoChooseAccount()
            gotoOnboardingPage()
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
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, "")
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, "")

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