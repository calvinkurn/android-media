package com.tokopedia.kyc_centralized.view.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel
import com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE
import com.tokopedia.user_identification_common.KycUrl

class UserIdentificationFormFailedFragment:
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>(),
        UserIdentificationFormActivity.Listener {

    override fun setContentView() {
        title.setText(R.string.kyc_face_failed_time_limit_title)
        subtitle.text = getString(R.string.kyc_face_failed_time_limit_subtitle)
        subtitle.gravity = Gravity.CENTER
        button.setText(R.string.kyc_face_failed_time_limit_button)
        button.setOnClickListener { v ->
//            analytics.eventClickNextKtpPage()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LIVENESS_DETECTION)
            startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE)
        }
        ImageHandler.LoadImage(onboardingImage, KycUrl.SCAN_FACE_FAIL_TIME)
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_face))
        }
    }


    override fun getScreenName(): String? { return null }

    override fun trackOnBackPressed() {
        //add analytics
    }

    companion object{

        var fragment: Fragment? = null

        @JvmStatic
        fun createInstance(): Fragment? {
            return if(fragment == null){
                val fragment = UserIdentificationFormFailedFragment()
                val args = Bundle()
                fragment.arguments = args
                fragment
            }else{
                fragment
            }
        }
    }

}