package com.tokopedia.otp.verification.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Ade Fulki on 28/04/20.
 */

class OnboardingMisscallViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_miscall_onboarding

    var containerView: View? = null
    var imgAnimation: LottieAnimationView? = null
    var title: Typography? = null
    var subtitle: Typography? = null
    var btnCallMe: UnifyButton? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                imgAnimation = findViewById(R.id.img_animation_miscall_onboarding)
                title = findViewById(R.id.title)
                subtitle = findViewById(R.id.subtitle)
                btnCallMe = findViewById(R.id.btn_call_me)
                toolbar = findViewById(R.id.toolbar_otp)
            }
}