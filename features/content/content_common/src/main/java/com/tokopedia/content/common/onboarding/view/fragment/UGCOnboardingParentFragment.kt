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
        get() = arguments?.getInt(
            KEY_ONBOARDING_TYPE,
            OnboardingType.Unknown.value
        ) ?: OnboardingType.Unknown.value

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
                    override fun clickUsernameFieldOnCompleteOnboarding() {
                        mListener?.clickUsernameFieldOnCompleteOnboarding()
                    }

                    override fun clickAcceptTnc() {
                        mListener?.clickAcceptTnc()
                    }

                    override fun clickNextOnCompleteOnboarding() {
                        mListener?.clickNextOnCompleteOnboarding()
                    }

                    override fun onSuccess() {
                        mListener?.onSuccess()
                    }

                    override fun clickCloseIcon() {
                        mListener?.clickCloseIcon()
                    }
                })
            }
            is UserTnCOnboardingBottomSheet -> {
                childFragment.setListener(object : UserTnCOnboardingBottomSheet.Listener,
                        BaseUserOnboardingBottomSheet.Listener {
                    override fun clickAcceptTnc() {
                        mListener?.clickAcceptTnc()
                    }

                    override fun clickNextOnTncOnboarding() {
                        mListener?.clickNextOnTncOnboarding()
                    }

                    override fun onSuccess() {
                        mListener?.onSuccess()
                    }

                    override fun clickCloseIcon() {
                        mListener?.clickCloseIcon()
                    }
                })
            }
        }
    }

    private fun showBottomSheet() {
        when (onboardingType) {
            OnboardingType.Complete.value -> {
                mListener?.impressCompleteOnboarding()
                UserCompleteOnboardingBottomSheet.getFragment(
                    childFragmentManager,
                    requireContext().classLoader
                ).apply {
                    arguments = createArgument()
                }.showNow(childFragmentManager)
            }
            OnboardingType.Tnc.value -> {
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
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onSuccess() {}
        fun impressTncOnboarding() {}
        fun impressCompleteOnboarding() {}
        fun clickNextOnTncOnboarding() {}
        fun clickUsernameFieldOnCompleteOnboarding() {}
        fun clickAcceptTnc() {}
        fun clickNextOnCompleteOnboarding() {}
        fun clickCloseIcon() {}
    }

    enum class OnboardingType(val value: Int) {
        Unknown(0), Complete(1), Tnc(2)
    }

    companion object {
        const val TAG = "FeedUGCOnboardingParentFragment"

        const val KEY_ONBOARDING_TYPE = "onboarding_type"

        fun createBundle(onboardingType: OnboardingType): Bundle {
            return Bundle().apply {
                putInt(KEY_ONBOARDING_TYPE, onboardingType.value)
            }
        }
    }
}
