package com.tokopedia.content.common.onboarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.databinding.FragmentUgcOnboardingParentBinding
import com.tokopedia.content.common.onboarding.view.bottomsheet.UserCompleteOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.bottomsheet.UserTnCOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.bottomsheet.base.BaseUserOnboardingBottomSheet

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class UGCOnboardingParentFragment : TkpdBaseV4Fragment() {

    private var mListener: Listener? = null

    private var _binding: FragmentUgcOnboardingParentBinding? = null
    private val binding get() = _binding!!

    override fun getScreenName() = TAG

    private val onboardingType: Int
        get() = arguments?.getInt(KEY_ONBOARDING_TYPE, VALUE_UNKNOWN) ?: VALUE_UNKNOWN
    private val entryPoint: Int
        get() = arguments?.getInt(KEY_ENTRY_POINT, VALUE_UNKNOWN) ?: VALUE_UNKNOWN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUgcOnboardingParentBinding.inflate(
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
            is UserCompleteOnboardingBottomSheet -> {
                childFragment.setListener(object : UserCompleteOnboardingBottomSheet.Listener,
                    BaseUserOnboardingBottomSheet.Listener {
                    override fun clickNextOnCompleteOnboarding() {
                        mListener?.clickNextOnCompleteOnboarding()
                    }

                    override fun onSuccess() {
                        mListener?.onSuccess()
                    }
                })
            }
            is UserTnCOnboardingBottomSheet -> {
                childFragment.setListener(object : UserTnCOnboardingBottomSheet.Listener,
                        BaseUserOnboardingBottomSheet.Listener {
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
        when (onboardingType) {
            VALUE_ONBOARDING_TYPE_COMPLETE -> {
                mListener?.impressCompleteOnboarding()
                UserCompleteOnboardingBottomSheet.getFragment(
                    childFragmentManager,
                    requireContext().classLoader
                ).apply {
                    arguments = createArgument()
                }.showNow(childFragmentManager)
            }
            VALUE_ONBOARDING_TYPE_TNC -> {
                mListener?.impressTncOnboarding()
                UserTnCOnboardingBottomSheet.getFragment(
                    childFragmentManager,
                    requireContext().classLoader
                ).apply {
                    arguments = createArgument()
                }.showNow(childFragmentManager)
            }
        }
    }

    private fun createArgument() = Bundle().apply {
        putInt(KEY_ONBOARDING_TYPE, onboardingType)
        putInt(KEY_ENTRY_POINT, entryPoint)
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
        const val KEY_ONBOARDING_TYPE = "onboarding_type"
        const val VALUE_ONBOARDING_TYPE_COMPLETE = 1
        const val VALUE_ONBOARDING_TYPE_TNC = 2
        const val VALUE_UNKNOWN = 0
        const val KEY_ENTRY_POINT = "entry_point"
        const val VALUE_ENTRY_POINT_FROM_PLAY_BROADCAST = 1
        const val VALUE_ENTRY_POINT_FROM_USER_PROFILE = 2
    }
}