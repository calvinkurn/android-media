package com.tokopedia.home_account.explicitprofile.personalize

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.home_account.databinding.FragmentPersonalizeSuccessBinding
import com.tokopedia.home_account.R
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        runTimer()
    }

    private fun runTimer() {
        lifecycleScope.launch(Dispatchers.Default) {
            delay(THREE_SECOND)
            withContext(Dispatchers.Main) {
                finishResultOk()
            }
        }
    }

    private fun finishResultOk() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun setUpAnimation() {
        binding?.apply {
            val imageUrlSuccessTop = getString(R.string.explicit_personalize_success_top)
            LottieCompositionFactory.fromUrl(requireContext(), imageUrlSuccessTop).addListener { result: LottieComposition? ->
                result?.let { animation1.setComposition(it) }
                animation1.repeatCount = ValueAnimator.INFINITE
                animation1.playAnimation()
            }

            val imageUrlSuccessBottom = getString(R.string.explicit_personalize_success_bottom)
            LottieCompositionFactory.fromUrl(requireContext(), imageUrlSuccessBottom).addListener { result: LottieComposition? ->
                result?.let { animation2.setComposition(it) }
                animation2.repeatCount = ValueAnimator.INFINITE
                animation2.playAnimation()
            }
        }

    }

    override fun getScreenName(): String = ""

    companion object {
        private const val THREE_SECOND = 3000L
        const val TAG = "PersonalizeSuccessFragment"
        fun createInstance(): Fragment = PersonalizeSuccessFragment()
    }
}
