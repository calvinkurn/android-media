package com.tokopedia.feedcomponent.onboarding.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.databinding.BottomsheetFeedUserCompleteOnboardingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class FeedUserCompleteOnboardingBottomSheet : BottomSheetUnify() {

    private var _binding: BottomsheetFeedUserCompleteOnboardingBinding? = null
    private val binding: BottomsheetFeedUserCompleteOnboardingBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFeedUserCompleteOnboardingBinding.inflate(layoutInflater)

        setChild(_binding?.root)
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
        ): FeedUserCompleteOnboardingBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FeedUserCompleteOnboardingBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedUserCompleteOnboardingBottomSheet::class.java.name
            ) as FeedUserCompleteOnboardingBottomSheet
        }
    }
}