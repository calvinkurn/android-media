package com.tokopedia.updateinactivephone.features.onboarding.regular

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.REQUEST_CAPTURE_SELFIE
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.imagepicker.InactivePhoneImagePickerActivity
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*

class InactivePhoneCaptureSelfieFragment : BaseDaggerFragment() {

    private val tracker: InactivePhoneTracker = InactivePhoneTracker()

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding_selfie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext?.setOnClickListener {
            tracker?.clickOnNextButtonSelfiewOnboarding()

            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
            startActivityForResult(intent, REQUEST_CAPTURE_SELFIE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_SELFIE && resultCode == Activity.RESULT_OK) {
            context?.let {
                (it as InactivePhoneRegularActivity).gotoUploadData()
            }
        }
    }
}