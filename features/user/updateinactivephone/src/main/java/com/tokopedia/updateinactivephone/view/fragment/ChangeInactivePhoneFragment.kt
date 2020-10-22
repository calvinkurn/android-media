package com.tokopedia.updateinactivephone.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.view.activity.ChangeInactiveFormRequestActivity
import com.tokopedia.updateinactivephone.view.activity.ChangeInactivePhoneRequestSubmittedActivity
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IS_DUPLICATE_REQUEST
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.OLD_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.USER_ID
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneAnalytics
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone
import com.tokopedia.updateinactivephone.di.component.DaggerUpdateInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import com.tokopedia.updateinactivephone.viewmodel.ChangeInactivePhoneViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

import javax.inject.Inject

class ChangeInactivePhoneFragment : BaseDaggerFragment(), ChangeInactivePhone.View {
    private var inputMobileNumber: EditText? = null
    private var buttonContinue: Button? = null
    private var errorText: TextView? = null
    private var phoneHintTextView: TextView? = null
    private var loader: LoaderUnify? = null
    private var mainLayout: LinearLayout? = null

    @Inject
    lateinit var analytics: UpdateInactivePhoneAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChangeInactivePhoneViewModel::class.java) }

    override fun initInjector() {
        DaggerUpdateInactivePhoneComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .updateInactivePhoneModule(UpdateInactivePhoneModule(requireContext()))
                .build().inject(this)
    }

    override fun getScreenName(): String {
        return UpdateInactivePhoneEventConstants.INPUT_OLD_PHONE_SCREEN
    }

    override fun onStart() {
        super.onStart()
        analytics.screen(screenName)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_inactive_phone, parent, false)
        inputMobileNumber = view.findViewById(R.id.phone_number)
        buttonContinue = view.findViewById(R.id.button_continue)
        phoneHintTextView = view.findViewById(R.id.phone_hint_text_view)
        errorText = view.findViewById(R.id.error)
        loader = view.findViewById(R.id.progress_bar)
        mainLayout = view.findViewById(R.id.main_layout)
        prepareView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.changeInactiveFormRequestResponse.observe(viewLifecycleOwner, Observer {
            dismissLoading()
            when(it){
                is Success -> {
                    when(it.data.isSuccess) {
                        true -> {
                            onPhoneStatusSuccess(it.data.userId)
                        }
                        false -> resolveError(it.data.error)
                    }
                }
                is Fail -> {
                    showErrorPhoneNumber(getString(R.string.error_general))
                }
            }
        })
    }

    private fun prepareView() {
        inputMobileNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                setErrorText("")
                if (charSequence.toString().isEmpty()) {
                    buttonContinue?.isEnabled = false
                    buttonContinue?.isClickable = false
                    buttonContinue?.setTextColor(resources.getColor(R.color.black_26))
                } else {
                    buttonContinue?.isEnabled = true
                    buttonContinue?.isClickable = true
                    buttonContinue?.setTextColor(Color.WHITE)
                }
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        inputMobileNumber?.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                handled = true
            }
            handled
        }

        buttonContinue?.setOnClickListener { v ->
            setErrorText("")
            hideKeyboard(v)
            analytics.eventInactivePhoneClick()
            analytics.eventClickButtonSubmission()
            val status = viewModel.isValidPhoneNumber(inputMobileNumber?.text.toString())
            if(status == 0) {
                showLoading()
                viewModel.checkPhoneNumberStatus(inputMobileNumber?.text.toString())
            } else {
                showErrorPhoneNumber(status)
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setErrorText(text: String) {
        if (TextUtils.isEmpty(text)) {
            errorText?.visibility = View.GONE
            phoneHintTextView?.visibility = View.VISIBLE
        } else {
            errorText?.text = text
            errorText?.visibility = View.VISIBLE
            phoneHintTextView?.visibility = View.GONE
        }
    }

    override fun showErrorPhoneNumber(resId: Int) {
        val message = getString(resId)
        analytics.eventFailedClickButtonSubmission(message)
        setErrorText(message)
    }

    override fun showErrorPhoneNumber(errorMessage: String) {
        setErrorText(errorMessage)
    }

    override fun dismissLoading() {
        loader?.hide()
        mainLayout?.show()
    }

    override fun showLoading() {
        loader?.show()
        mainLayout?.hide()
    }

    override fun onForbidden() {}

    override fun onPhoneStatusSuccess(userid: String) {
        analytics.eventSuccessClickButtonSubmission()
        setErrorText("")
        val bundle = Bundle()
        bundle.putString(USER_ID, userid)
        bundle.putString(OLD_PHONE, inputMobileNumber?.text.toString())
        startActivity(context?.let { ChangeInactiveFormRequestActivity.createIntent(it, bundle) })
    }

    override fun onPhoneRegisteredWithEmail() {
        val dialog = context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(R.string.registered_email_dialog_title))
        dialog?.setDescription(getString(R.string.registered_email_dialog_message))
        dialog?.setPrimaryCTAText(getString(R.string.drawer_title_login))
        dialog?.setPrimaryCTAClickListener {
            analytics.eventLoginDialogClick()
            RouteManager.route(context, ApplinkConst.LOGIN)
            Unit
        }
        dialog?.setSecondaryCTAText(getString(R.string.title_cancel))
        dialog?.setSecondaryCTAClickListener {
            analytics.eventCancelDialogClick()
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun onPhoneDuplicateRequest() {
        setErrorText("")
        val bundle = Bundle()
        bundle.putBoolean(IS_DUPLICATE_REQUEST, true)
        val intent = context?.let { ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(it, bundle) }
        startActivity(intent)
    }

    override fun onPhoneServerError() {
        NetworkErrorHelper.showSnackbar(activity)
    }

    override fun onPhoneBlackListed() {
        setErrorText(getString(R.string.phone_blacklisted))
    }

    override fun onPhoneInvalid() {
        setErrorText(getString(R.string.error_invalid_phone_number))
    }

    override fun onPhoneNotRegistered() {
        setErrorText(getString(R.string.phone_not_registered))
    }

    override fun onPhoneTooShort() {
        setErrorText(getString(R.string.phone_number_invalid_min_8))
    }

    override fun onPhoneTooLong() {
        setErrorText(getString(R.string.phone_number_invalid_max_15))
    }

    private fun resolveError(error: String) {
        analytics.eventFailedClickButtonSubmission(error)
        when {
            UpdateInactivePhoneConstants.ResponseConstants.INVALID_PHONE.equals(error, ignoreCase = true) -> onPhoneInvalid()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_TOO_SHORT.equals(error, ignoreCase = true) -> onPhoneTooShort()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_TOO_LONG.equals(error, ignoreCase = true) -> onPhoneTooLong()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_BLACKLISTED.equals(error, ignoreCase = true) -> onPhoneBlackListed()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_NOT_REGISTERED.equals(error, ignoreCase = true) -> onPhoneNotRegistered()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_WITH_REGISTERED_EMAIL.equals(error, ignoreCase = true) -> onPhoneRegisteredWithEmail()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_WITH_PENDING_REQUEST.equals(error, ignoreCase = true) -> onPhoneDuplicateRequest()
            UpdateInactivePhoneConstants.ResponseConstants.SERVER_ERROR.equals(error, ignoreCase = true) -> onPhoneServerError()
            else -> showErrorPhoneNumber(getString(R.string.error_general))
        }
    }

    companion object {
        val instance: Fragment
            get() = ChangeInactivePhoneFragment()
    }
}
