package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneImagePickerActivity
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*

class InactivePhoneOnboardingIdCardFragment : BaseDaggerFragment() {

    private val userSession: UserSession = UserSession(activity)

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding_id_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentTransactionInterface = activity as FragmentTransactionInterface

        btnNext?.setOnClickListener {
            // check has multiple account ?
            // check login with phone number ?
            // need improvement on login / register page to set login method
//            if (userSession.loginMethod != UserSessionInterface.LOGIN_METHOD_PHONE) {
//
//            }

            // take picture [ID CARD] [SELFIE]
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, REQUEST_CAPTURE_ID_CARD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_ID_CARD && resultCode == Activity.RESULT_OK) {
            checkHasDana()
        }
    }

    private fun checkHasDana() {
        val dana = 500
        if (dana > 500) {
            gotoOnboardingSavingBook()
        } else {
            gotoOnboardingSelfie()
        }
    }

    private fun gotoOnboardingSelfie() {
        fragmentTransactionInterface.replace(InactivePhoneOnboardingSelfieFragment())
    }
    private fun gotoOnboardingSavingBook() {
        fragmentTransactionInterface.replace(InactivePhoneOnboardingSelfieFragment())
    }

    private fun gotoSelectAccount() {

    }

    companion object {
        private const val REQUEST_CAPTURE_ID_CARD = 100
    }
}