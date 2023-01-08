package com.tokopedia.updateinactivephone.features.inputoldphonenumber.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.model.PhoneFormState
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.updateinactivephone.common.analytics.InputOldPhoneNumberAnalytics
import com.tokopedia.updateinactivephone.common.analytics.InputOldPhoneNumberAnalytics.Companion.LABEL_CLICK
import com.tokopedia.updateinactivephone.common.analytics.InputOldPhoneNumberAnalytics.Companion.LABEL_FAILED
import com.tokopedia.updateinactivephone.common.analytics.InputOldPhoneNumberAnalytics.Companion.LABEL_SUCCESS
import com.tokopedia.updateinactivephone.databinding.FragmentInputOldPhoneNumberBinding
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.viewmodel.InputOldPhoneNumberViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class InputOldPhoneNumberFragment : BaseDaggerFragment() {

    private val binding: FragmentInputOldPhoneNumberBinding? by viewBinding()

    @Inject
    lateinit var analytics: InputOldPhoneNumberAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            InputOldPhoneNumberViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_input_old_phone_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formAction()
        onClickListener()
        formStateObserver()
        statusPhoneNumberObserver()
        isLoadingObserver()
    }

    override fun getScreenName(): String =
        InputOldPhoneNumberFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(InactivePhoneComponent::class.java).inject(this)
    }

    private fun formStateObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {

            binding?.textInputOldPhoneNumber?.setMessage(
                if (!it.isDataValid && it.numberError != PhoneFormState.DEFAULT_NUMBER_ERROR) {
                    val message = getString(it.numberError)
                    analytics.trackPageInactivePhoneNumberClickNext(LABEL_FAILED, message)
                    message
                } else " "
            )

            binding?.textInputOldPhoneNumber?.isInputError = !it.isDataValid
        }
    }

    private fun onClickListener() {
        binding?.ubNext?.setOnClickListener {
            analytics.trackPageInactivePhoneNumberClickNext(LABEL_CLICK)
            submitData()
        }
    }

    private fun formAction() {
        binding?.textInputOldPhoneNumber?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitData()
                true
            } else false
        }

        binding?.textInputOldPhoneNumber?.editText?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                submitData()
                true
            } else false
        }
    }

    private fun statusPhoneNumberObserver() {
        viewModel.statusPhoneNumber.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    analytics.trackPageInactivePhoneNumberClickNext(LABEL_SUCCESS)
                    onGoToInactivePhoneNumber(it.data)
                }
                is Fail -> {
                    val message = getErrorMsgWithLogging(it.throwable)
                    analytics.trackPageInactivePhoneNumberClickNext(LABEL_FAILED, message)
                    onError(message)
                }
            }
        }
    }

    private fun onGoToInactivePhoneNumber(currentNumber: String) {
        context?.let {
            val intent =
                RouteManager.getIntent(it, ApplinkConstInternalUserPlatform.CHANGE_INACTIVE_PHONE)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, currentNumber)
            startActivity(intent)
        }
    }

    private fun onError(message: String) {
        binding?.textInputOldPhoneNumber?.apply {
            setMessage(message)
            isInputError = true
        }
    }

    private fun isLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.ubNext?.isLoading = it
        }
    }

    private fun hideKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun submitData() {
        hideKeyboard()
        viewModel.submitNumber(binding?.textInputOldPhoneNumber?.getEditableValue().toString())
    }

    private fun getErrorMsgWithLogging(
        throwable: Throwable,
        withErrorCode: Boolean = false
    ): String {
        val mClassName =
            InputOldPhoneNumberFragment::class.java.name
        val message = ErrorHandler.getErrorMessage(
            context, throwable,
            ErrorHandler.Builder().apply {
                withErrorCode(withErrorCode)
                className = mClassName
            }.build()
        )
        return message
    }

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            InputOldPhoneNumberFragment().apply {
                arguments = bundle
            }
    }
}