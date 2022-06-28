package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.databinding.BottomsheetFeedUserCompleteOnboardingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class FeedUserCompleteOnboarding : BottomSheetUnify() {

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
    }
}