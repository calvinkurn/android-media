package com.tokopedia.content.common.onboarding.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.databinding.BottomsheetUserCompleteOnboardingBinding
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.common.R
import com.tokopedia.content.common.onboarding.view.bottomsheet.base.BaseUserOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.strategy.factory.UGCOnboardingStrategyFactory
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import com.tokopedia.content.common.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.content.common.onboarding.view.viewmodel.UGCOnboardingViewModel
import com.tokopedia.content.common.onboarding.view.viewmodel.factory.UGCOnboardingViewModelFactory
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.tokopedia.content.common.util.hideKeyboard


/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class UserCompleteOnboardingBottomSheet @Inject constructor(
    private val viewModelFactoryCreator: UGCOnboardingViewModelFactory.Creator,
    private val strategyFactory: UGCOnboardingStrategyFactory,
): BaseUserOnboardingBottomSheet() {

    private var _binding: BottomsheetUserCompleteOnboardingBinding? = null
    private val binding: BottomsheetUserCompleteOnboardingBinding
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
        _binding = BottomsheetUserCompleteOnboardingBinding.inflate(layoutInflater)

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
        binding.textFieldUsername.isClearable = false
        binding.layoutTnc.tvAcceptTnc.text = getTncText()
        binding.layoutTnc.tvAcceptTnc.movementMethod = LinkMovementMethod.getInstance()
        setTitle(getString(R.string.ugc_complete_onboarding_title))
    }

    @Suppress("ClickableViewAccessibility")
    private fun setupListener() {
        binding.textFieldUsername.editText.apply {
            setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    _listener?.clickUsernameFieldOnCompleteOnboarding()
                }
                false
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val username = p0?.toString() ?: ""
                    viewModel.submitAction(UGCOnboardingAction.InputUsername(username))
                }
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    true
                } else false
            }
        }

        binding.layoutTnc.cbxTnc.setOnCheckedChangeListener { _, _ ->
            _listener?.clickAcceptTnc(binding.layoutTnc.cbxTnc.isChecked)
            viewModel.submitAction(UGCOnboardingAction.CheckTnc)
        }

        binding.btnContinue.setOnClickListener {
            _listener?.clickNextOnCompleteOnboarding()
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
                            text = getDefaultErrorMessage(),
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
        prev: UGCOnboardingUiState?,
        curr: UGCOnboardingUiState,
    ) {
        if(prev == curr) return

        with(curr) {
            binding.layoutTnc.cbxTnc.isChecked = isCheckTnc

            binding.btnContinue.isEnabled = isCheckTnc && !isSubmit && usernameState is UsernameState.Valid
            binding.btnContinue.isLoading = isSubmit

            binding.textFieldUsername.isEnabled = !isSubmit
            binding.textFieldUsername.isLoading = usernameState is UsernameState.Loading
            binding.textFieldUsername.isInputError = usernameState is UsernameState.Invalid
            binding.textFieldUsername.setMessage(
                if(usernameState is UsernameState.Invalid) {
                    if(usernameState.message.isNotEmpty()) usernameState.message
                    else getDefaultErrorMessage()
                }
                else getString(R.string.up_input_username_info)
            )
            binding.textFieldUsername.icon1.visibility = if(usernameState is UsernameState.Valid) View.VISIBLE else View.GONE

            if(curr.hasAcceptTnc) {
                mListener?.onSuccess()
                dismiss()
            }
        }
    }

    private fun getDefaultErrorMessage() = getString(R.string.ugc_onboarding_unknown_error)

    fun showNow(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "FeedUserCompleteOnboardingBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): UserCompleteOnboardingBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserCompleteOnboardingBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserCompleteOnboardingBottomSheet::class.java.name
            ) as UserCompleteOnboardingBottomSheet
        }
    }

    interface Listener : BaseUserOnboardingBottomSheet.Listener {
        fun clickUsernameFieldOnCompleteOnboarding()
        fun clickAcceptTnc(isChecked: Boolean)
        fun clickNextOnCompleteOnboarding()
    }

}
