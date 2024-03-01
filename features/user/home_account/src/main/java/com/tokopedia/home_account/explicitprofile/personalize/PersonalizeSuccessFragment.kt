package com.tokopedia.home_account.explicitprofile.personalize

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.home_account.databinding.FragmentPersonalizeSuccessBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PersonalizeSuccessFragment: TkpdBaseV4Fragment() {

    private var binding by autoClearedNullable<FragmentPersonalizeSuccessBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalizeSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAnimation()
    }

    private fun setUpAnimation() {
        val imageUrl = "https://assets.tokopedia.net/asts/android/user/kyc/img_url_goto_kyc_status_submission_verified.json"
        binding?.apply {
            LottieCompositionFactory.fromUrl(requireContext(), imageUrl).addListener { result: LottieComposition? ->
                result?.let { animation1.setComposition(it) }
                animation1.repeatCount = ValueAnimator.INFINITE
                animation1.playAnimation()
            }

            LottieCompositionFactory.fromUrl(requireContext(), imageUrl).addListener { result: LottieComposition? ->
                result?.let { animation2.setComposition(it) }
                animation2.repeatCount = ValueAnimator.INFINITE
                animation2.playAnimation()
            }
        }

    }

    override fun getScreenName(): String = ""

    companion object {
        const val TAG = "PersonalizeSuccessFragment"
        fun createInstance(): Fragment = PersonalizeSuccessFragment()
    }
}
