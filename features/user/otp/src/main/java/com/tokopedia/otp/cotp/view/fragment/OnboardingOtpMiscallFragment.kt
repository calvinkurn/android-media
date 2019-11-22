package com.tokopedia.otp.cotp.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker

import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.OTPAnalytics
import com.tokopedia.otp.cotp.view.viewlistener.OnboardingOtpMiscall
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel
import com.tokopedia.unifycomponents.UnifyButton

class OnboardingOtpMiscallFragment : BaseDaggerFragment(), OnboardingOtpMiscall.View {

    private lateinit var textStep1: TextView
    private lateinit var textStep2: TextView
    private lateinit var imgAnimationPreview: LottieAnimationView
    private lateinit var btnCallMe: UnifyButton

    private lateinit var passModel: VerificationViewModel

    override fun getScreenName(): String = OTPAnalytics.Screen.SCREEN_COTP_MISCALL

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cotp_miscall_onboarding, container, false)
        textStep1 = view.findViewById(R.id.otp_onboarding_desc_1)
        textStep2 = view.findViewById(R.id.otp_onboarding_desc_2)
        imgAnimationPreview = view.findViewById(R.id.otp_onboarding_image_animation)
        btnCallMe = view.findViewById(R.id.call_me)

        startAnimation()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        passModel = arguments?.getParcelable(ARGS_PASS_DATA) as VerificationViewModel

        textStep1.text = MethodChecker.fromHtml(getString(R.string.cotp_miscall_onboarding_step_1))
        textStep2.text = MethodChecker.fromHtml(getString(R.string.cotp_miscall_onboarding_step_2))
        btnCallMe.setOnClickListener {
            if (fragmentManager?.findFragmentById(R.id.parent_view) !is VerificationOtpMiscallFragment) {

                fragmentManager?.popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val fragmentTransaction = fragmentManager?.beginTransaction()

                val fragment = VerificationOtpMiscallFragment.createInstance(passModel)
                fragmentTransaction?.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
                fragmentTransaction?.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG)
                fragmentTransaction?.addToBackStack(FIRST_FRAGMENT_TAG)
                fragmentTransaction?.commit()
            }
        }
    }

    override fun startAnimation() {
        imgAnimationPreview.speed = ANIMATION_SPEED
        imgAnimationPreview.playAnimation()
    }

    override fun stopAnimation() {
        imgAnimationPreview.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        if (!imgAnimationPreview.isAnimating) {
            startAnimation()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAnimation()
    }

    companion object {

        fun createInstance(passModel: VerificationViewModel): Fragment {
            val fragment = OnboardingOtpMiscallFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARGS_PASS_DATA, passModel)
            fragment.arguments = bundle
            return fragment
        }

        const val ARGS_PASS_DATA = "pass_data"
        const val FIRST_FRAGMENT_TAG = "first"
        const val ANIMATION_SPEED = 0.5F
    }
}
