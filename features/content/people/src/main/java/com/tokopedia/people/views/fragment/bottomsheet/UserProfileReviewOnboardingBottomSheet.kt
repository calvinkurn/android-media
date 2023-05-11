package com.tokopedia.people.views.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.people.databinding.BottomSheetReviewOnboardingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
class UserProfileReviewOnboardingBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetReviewOnboardingBinding? = null

    private val binding: BottomSheetReviewOnboardingBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetReviewOnboardingBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {

    }

    private fun setupListener() {
        binding.icClose.setOnClickListener {

        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "UserProfileReviewOnboardingBottomSheet"


        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): UserProfileReviewOnboardingBottomSheet {
            return fragmentManager.findFragmentByTag(TAG) as? UserProfileReviewOnboardingBottomSheet ?: run {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    UserProfileReviewOnboardingBottomSheet::class.java.name
                ) as UserProfileReviewOnboardingBottomSheet
            }
        }
    }
}
