package com.tokopedia.feedcomponent.bottomsheets.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.databinding.BottomsheetFeedUserTncOnboardingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class FeedUserTnCOnboardingBottomSheet : BottomSheetUnify() {

    private var _binding: BottomsheetFeedUserTncOnboardingBinding? = null
    private val binding: BottomsheetFeedUserTncOnboardingBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFeedUserTncOnboardingBinding.inflate(layoutInflater)

        setChild(binding.root)
        showCloseIcon = false
        showKnob = true
        showHeader = false

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showNow(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "FeedUserCompleteOnboarding"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): FeedUserTnCOnboardingBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FeedUserTnCOnboardingBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedUserTnCOnboardingBottomSheet::class.java.name
            ) as FeedUserTnCOnboardingBottomSheet
        }
    }
}