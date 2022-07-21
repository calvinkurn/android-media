package com.tokopedia.feedcomponent.onboarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedcomponent.databinding.FragmentFeedUgcOnboardingParentBinding
import com.tokopedia.feedcomponent.onboarding.view.bottomsheet.FeedUserCompleteOnboardingBottomSheet
import com.tokopedia.feedcomponent.onboarding.view.bottomsheet.FeedUserTnCOnboardingBottomSheet
import com.tokopedia.feedcomponent.onboarding.view.bottomsheet.base.BaseFeedUserOnboardingBottomSheet
import com.tokopedia.feedcomponent.onboarding.view.strategy.factory.FeedUGCOnboardingStrategyFactory
import com.tokopedia.feedcomponent.onboarding.view.viewmodel.FeedUGCOnboardingViewModel
import com.tokopedia.feedcomponent.onboarding.view.viewmodel.factory.FeedUGCOnboardingViewModelFactory
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingParentFragment @Inject constructor(
    private val viewModelFactoryCreator: FeedUGCOnboardingViewModelFactory.Creator,
    private val strategyFactory: FeedUGCOnboardingStrategyFactory,
): TkpdBaseV4Fragment() {

    private var mListener: Listener? = null

    private var _binding: FragmentFeedUgcOnboardingParentBinding? = null
    private val binding get() = _binding!!

    override fun getScreenName() = TAG

    private val usernameArg: String
        get() = arguments?.getString(KEY_USERNAME) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProvider(
            this,
            viewModelFactoryCreator.create(
                this,
                usernameArg,
                strategyFactory.create(usernameArg),
            )
        )[FeedUGCOnboardingViewModel::class.java]
    }

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
            ).showNow(childFragmentManager)
        }
        else {
            mListener?.impressTncOnboarding()
            FeedUserTnCOnboardingBottomSheet.getFragment(
                childFragmentManager,
                requireContext().classLoader
            ).showNow(childFragmentManager)
        }
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