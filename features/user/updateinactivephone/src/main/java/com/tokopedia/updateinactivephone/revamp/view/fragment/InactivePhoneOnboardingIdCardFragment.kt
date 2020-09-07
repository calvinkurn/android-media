package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneImagePickerActivity
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding_id_card.*

class InactivePhoneOnboardingIdCardFragment : BaseDaggerFragment() {

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
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, REQUEST_CAPTURE_ID_CARD)
        }

        ImageUtils.loadImage(imgHeader, IMAGE_ID_CARD_SAMPLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_ID_CARD && resultCode == Activity.RESULT_OK) {
            gotoOnboardingSelfie()
        }
    }

    private fun gotoOnboardingSelfie() {
        fragmentTransactionInterface.replace(InactivePhoneOnboardingSelfieFragment())
    }

    companion object {
        const val IMAGE_ID_CARD_SAMPLE = "https://ecs7.tokopedia.net/android/others/account_verification_ktp_onboarding.png"
    }
}