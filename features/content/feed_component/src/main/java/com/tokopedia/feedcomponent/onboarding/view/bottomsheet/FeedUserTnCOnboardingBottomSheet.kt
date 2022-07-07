package com.tokopedia.feedcomponent.onboarding.view.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.databinding.BottomsheetFeedUserTncOnboardingBinding
import com.tokopedia.feedcomponent.onboarding.view.bottomsheet.base.BaseFeedUserOnboardingBottomSheet
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.feedcomponent.onboarding.view.viewmodel.FeedUGCOnboardingViewModel
import com.tokopedia.feedcomponent.util.withCache
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class FeedUserTnCOnboardingBottomSheet : BaseFeedUserOnboardingBottomSheet() {

    private var _binding: BottomsheetFeedUserTncOnboardingBinding? = null
    private val binding: BottomsheetFeedUserTncOnboardingBinding
        get() = _binding!!

    private lateinit var viewModel: FeedUGCOnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireParentFragment()
        )[FeedUGCOnboardingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFeedUserTncOnboardingBinding.inflate(layoutInflater)

        setChild(binding.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.layoutTnc.tvAcceptTnc.text = getTncText()
        binding.layoutTnc.tvAcceptTnc.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupListener() {
        binding.layoutTnc.cbxTnc.setOnCheckedChangeListener { _, _ ->
            viewModel.submitAction(FeedUGCOnboardingAction.CheckTnc)
        }

        binding.btnContinue.setOnClickListener {
            viewModel.submitAction(FeedUGCOnboardingAction.ClickNext)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderLayout(it.prevValue, it.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is FeedUGCOnboardingUiEvent.ShowError -> {
                        Toaster.toasterCustomBottomHeight = binding.btnContinue.height + offset16
                        Toaster.build(
                            view = binding.root,
                            text = getString(R.string.feed_ugc_onboarding_unknown_error),
                            duration = Toaster.LENGTH_SHORT,
                            type = TYPE_ERROR,
                        ).show()
                    }
                }
            }
        }
    }

    /** Render Section */
    private fun renderLayout(
        prev: FeedUGCOnboardingUiState?,
        curr: FeedUGCOnboardingUiState,
    ) {
        if(prev == curr) return

        binding.layoutTnc.cbxTnc.isChecked = curr.isCheckTnc
        binding.btnContinue.isEnabled = curr.isCheckTnc && !curr.isSubmit
        binding.btnContinue.isLoading = curr.isSubmit

        if(curr.hasAcceptTnc) {
            mListener?.onSuccess()
            dismiss()
        }
    }

    fun showNow(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "FeedUserTnCOnboardingBottomSheet"

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