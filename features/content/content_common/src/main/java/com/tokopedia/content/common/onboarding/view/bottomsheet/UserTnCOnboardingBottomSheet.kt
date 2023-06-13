package com.tokopedia.content.common.onboarding.view.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.onboarding.view.bottomsheet.base.BaseUserOnboardingBottomSheet
import com.tokopedia.content.common.R
import com.tokopedia.content.common.onboarding.view.strategy.factory.UGCOnboardingStrategyFactory
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import com.tokopedia.content.common.onboarding.view.viewmodel.UGCOnboardingViewModel
import com.tokopedia.content.common.onboarding.view.viewmodel.factory.UGCOnboardingViewModelFactory
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.common.databinding.BottomsheetUserTncOnboardingBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class UserTnCOnboardingBottomSheet @Inject constructor(
    private val viewModelFactoryCreator: UGCOnboardingViewModelFactory.Creator,
    private val strategyFactory: UGCOnboardingStrategyFactory,
): BaseUserOnboardingBottomSheet() {

    private var _binding: BottomsheetUserTncOnboardingBinding? = null
    private val binding: BottomsheetUserTncOnboardingBinding
        get() = _binding!!

    private val viewModel: UGCOnboardingViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            strategyFactory.create(onboardingType),
        )
    }

    private val _listener: Listener?
        get() = mListener as? Listener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetUserTncOnboardingBinding.inflate(layoutInflater)

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
        setTitle(getString(R.string.ugc_tnc_onboarding_title))
    }

    private fun setupListener() {
        binding.layoutTnc.cbxTnc.setOnCheckedChangeListener { _, _ ->
            _listener?.clickAcceptTnc(binding.layoutTnc.cbxTnc.isChecked)
            viewModel.submitAction(UGCOnboardingAction.CheckTnc)
        }

        binding.btnContinue.setOnClickListener {
            _listener?.clickNextOnTncOnboarding()
            viewModel.submitAction(UGCOnboardingAction.ClickNext)
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
                    is UGCOnboardingUiEvent.ShowError -> {
                        Toaster.toasterCustomBottomHeight = binding.btnContinue.height + offset16
                        Toaster.build(
                            view = binding.root,
                            text = getString(R.string.ugc_onboarding_unknown_error),
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
        prev: UGCOnboardingUiState?,
        curr: UGCOnboardingUiState,
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
        ): UserTnCOnboardingBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserTnCOnboardingBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserTnCOnboardingBottomSheet::class.java.name
            ) as UserTnCOnboardingBottomSheet
        }
    }

    interface Listener : BaseUserOnboardingBottomSheet.Listener {
        fun clickAcceptTnc(isChecked: Boolean)
        fun clickNextOnTncOnboarding()
    }

}
