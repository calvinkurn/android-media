package com.tokopedia.loginregister.inactive_phone_number.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.InactivePhoneNumberAnalytics
import com.tokopedia.loginregister.common.analytics.InactivePhoneNumberAnalytics.Companion.LABEL_CLICK
import com.tokopedia.loginregister.common.analytics.InactivePhoneNumberAnalytics.Companion.LABEL_FAILED
import com.tokopedia.loginregister.common.analytics.InactivePhoneNumberAnalytics.Companion.LABEL_SUCCESS
import com.tokopedia.loginregister.inactive_phone_number.di.InactivePhoneNumberComponent
import com.tokopedia.loginregister.inactive_phone_number.view.viewmodel.InactivePhoneNumberViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneNumberFragment : BaseDaggerFragment() {

    @Inject
    lateinit var analytics: InactivePhoneNumberAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    val viewModel by lazy { viewModelProvider.get(InactivePhoneNumberViewModel::class.java) }

    private var btnNext: UnifyButton? = null
    private var tfu2Phone: TextFieldUnify2? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inactive_phone_number, container, false)

        btnNext = view.findViewById(R.id.ub_next)
        tfu2Phone = view.findViewById(R.id.tfu2_old_phone_number)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formAction()
        onClickListener()
        formStateObserver()
        statusPhoneNumberObserver()
        isLoadingObserver()
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(InactivePhoneNumberComponent::class.java).inject(this)
    }

    private fun formStateObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {

            tfu2Phone?.setMessage(
                if (it.numberError != null) {
                    val message = getString(it.numberError)
                    analytics.trackPageInactivePhoneNumberClickNext(LABEL_FAILED, message, viewModel.currentNumber)
                    message
                } else " "
            )

            tfu2Phone?.isInputError = !it.isDataValid
        }
    }

    private fun onClickListener() {
        btnNext?.setOnClickListener {
            analytics.trackPageInactivePhoneNumberClickNext(LABEL_CLICK)
            submitData()
        }
    }

    private fun formAction() {
        tfu2Phone?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitData()
                true
            } else false
        }

        tfu2Phone?.editText?.setOnKeyListener { _, keyCode, event ->
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
                    analytics.trackPageInactivePhoneNumberClickNext(LABEL_FAILED, message, viewModel.currentNumber)
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
        tfu2Phone?.setMessage(message)
        tfu2Phone?.isInputError = true
    }

    private fun isLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            btnNext?.isLoading = it
        }
    }

    private fun hideKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun submitData() {
        hideKeyboard()
        viewModel.submitNumber(tfu2Phone?.getEditableValue().toString())
    }

    private fun getErrorMsgWithLogging(
        throwable: Throwable,
        withErrorCode: Boolean = false
    ): String {
        val mClassName =
            InactivePhoneNumberFragment::class.java.name
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

        private const val SCREEN_NAME = "Inactive Phone Number Page"

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            InactivePhoneNumberFragment().apply {
                arguments = bundle
            }
    }
}