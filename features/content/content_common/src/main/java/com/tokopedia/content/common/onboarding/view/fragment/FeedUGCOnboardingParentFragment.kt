package com.tokopedia.content.common.onboarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.databinding.FragmentFeedUgcOnboardingParentBinding
import com.tokopedia.content.common.onboarding.view.bottomsheet.FeedUserCompleteOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.bottomsheet.FeedUserTnCOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.bottomsheet.base.BaseFeedUserOnboardingBottomSheet

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingParentFragment : TkpdBaseV4Fragment() {

    private var mListener: Listener? = null

    private var _binding: FragmentFeedUgcOnboardingParentBinding? = null
    private val binding get() = _binding!!

    override fun getScreenName() = TAG

    private val usernameArg: String
        get() = arguments?.getString(KEY_USERNAME).orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedUgcOnboardingParentBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is FeedUserCompleteOnboardingBottomSheet -> {
                childFragment.setListener(object : FeedUserCompleteOnboardingBottomSheet.Listener,
                    BaseFeedUserOnboardingBottomSheet.Listener {
                    override fun clickNextOnCompleteOnboarding() {
                        mListener?.clickNextOnCompleteOnboarding()
                    }

                    override fun onSuccess() {
                        mListener?.onSuccess()
                    }
                })
            }
            is FeedUserTnCOnboardingBottomSheet -> {
                childFragment.setListener(object : FeedUserTnCOnboardingBottomSheet.Listener,
                        BaseFeedUserOnboardingBottomSheet.Listener {
                        override fun clickNextOnTncOnboarding() {
                            mListener?.clickNextOnTncOnboarding()
                        }

                        override fun onSuccess() {
                            mListener?.onSuccess()
                        }
                    })
            }
        }
    }

    private fun showBottomSheet() {
        if(usernameArg.isEmpty()) {
            mListener?.impressCompleteOnboarding()
            FeedUserCompleteOnboardingBottomSheet.getFragment(
                childFragmentManager,
                requireContext().classLoader
            ).apply {
                arguments = createArgument()
            }.showNow(childFragmentManager)
        }
        else {
            mListener?.impressTncOnboarding()
            FeedUserTnCOnboardingBottomSheet.getFragment(
                childFragmentManager,
                requireContext().classLoader
            ).apply {
                arguments = createArgument()
            }.showNow(childFragmentManager)
        }
    }

    private fun createArgument() = Bundle().apply {
        putString(KEY_USERNAME, usernameArg)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onSuccess()
        fun impressTncOnboarding()
        fun impressCompleteOnboarding()
        fun clickNextOnTncOnboarding()
        fun clickNextOnCompleteOnboarding()
    }

    companion object {
        const val TAG = "FeedUGCOnboardingParentFragment"
        const val KEY_USERNAME = "username"
    }
}