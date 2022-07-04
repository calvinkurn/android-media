package com.tokopedia.feedcomponent.onboarding.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.databinding.BottomsheetFeedUserCompleteOnboardingBinding
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.feedcomponent.onboarding.view.viewmodel.FeedUGCOnboardingViewModel
import com.tokopedia.feedcomponent.util.withCache
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import com.tokopedia.abstraction.R as abstractionR

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class FeedUserCompleteOnboardingBottomSheet : BottomSheetUnify() {

    private var _binding: BottomsheetFeedUserCompleteOnboardingBinding? = null
    private val binding: BottomsheetFeedUserCompleteOnboardingBinding
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
        _binding = BottomsheetFeedUserCompleteOnboardingBinding.inflate(layoutInflater)

        setChild(_binding?.root)
        showCloseIcon = false
        showKnob = true
        showHeader = false

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
        binding.textFieldUsername.isClearable = false
    }

    private fun setupListener() {
        binding.textFieldUsername.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val username = p0?.toString() ?: ""
                viewModel.submitAction(FeedUGCOnboardingAction.InputUsername(username))
            }
        })

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
                    is FeedUGCOnboardingUiEvent.ErrorAcceptTnc,
                    is FeedUGCOnboardingUiEvent.ErrorCheckUsername -> {
                        /** TODO: toaster is still not showing */
                        Toaster.build(
                            view = binding.btnContinue,
                            text = getString(abstractionR.string.default_request_error_unknown),
                            duration = Toaster.LENGTH_SHORT,
                            type = Toaster.TYPE_ERROR,
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
        binding.btnContinue.isEnabled = curr.isCheckTnc &&
                                        !curr.isSubmit &&
                                        curr.usernameState is UsernameState.Valid &&
                                        curr.username.length >= 3
        binding.btnContinue.isLoading = curr.isSubmit

        when(curr.usernameState) {
            is UsernameState.Loading -> {
                binding.textFieldUsername.isLoading = true
                binding.textFieldUsername.isInputError = false
                binding.textFieldUsername.setMessage(getString(R.string.up_input_username_info))
                /** TODO: remove centang icon */
            }
            is UsernameState.Invalid -> {
                binding.textFieldUsername.isLoading = false
                binding.textFieldUsername.isInputError = true
                binding.textFieldUsername.setMessage(curr.usernameState.message)
                /** TODO: remove centang icon */
            }
            is UsernameState.Valid -> {
                binding.textFieldUsername.isLoading = false
                binding.textFieldUsername.isInputError = false
                binding.textFieldUsername.setMessage(getString(R.string.up_input_username_info))
                /** TODO: show centang icon */
            }
            else -> {
                binding.textFieldUsername.isLoading = false
                binding.textFieldUsername.isInputError = false
                binding.textFieldUsername.setMessage(getString(R.string.up_input_username_info))
                /** TODO: remove centang icon */
            }
        }

        if(curr.hasAcceptTnc) {
            /** TODO: call callback */
            dismiss()
        }
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