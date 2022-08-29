package com.tokopedia.loginregister.redefine_register_email.input_phone.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.KeyboardHandler
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RedefineRegisterInputPhoneViewModel
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class RedefineRegisterInputPhoneFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            RedefineRegisterInputPhoneViewModel::class.java
        )
    }

    private val binding: FragmentRedefineRegisterInputPhoneBinding? by viewBinding()

    private var source: String = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        source = getParamString(ApplinkConstInternalGlobal.PARAM_SOURCE, arguments, savedInstanceState, EMPTY_STRING)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_redefine_register_input_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
        editorChangesListener()
        setUpKeyboardListener(view)
    }

    private fun setUpKeyboardListener(view: View) {
        KeyboardHandler(view, object : KeyboardHandler.OnKeyBoardVisibilityChangeListener {
            override fun onKeyboardShow() {
                binding?.ivInputPhone?.hide()
            }

            override fun onKeyboardHide() {
                binding?.ivInputPhone?.show()
            }
        })
    }

    private fun editorChangesListener() {
        binding?.fieldInputPhone?.editText?.afterTextChanged {
            viewModel.validatePhone(it)
        }

        binding?.fieldInputPhone?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitForm()
                true
            } else false
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        viewModel.submitForm(
            binding?.fieldInputPhone?.editText?.text.toString()
        )
    }

    private fun initObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldInputPhone?.setMessageField(it)
        }

        viewModel.isRegisteredPhone.observe(viewLifecycleOwner) {
            showDialogOfferLogin()
        }
    }

    private fun showDialogOfferLogin() {
        val phone = binding?.fieldInputPhone?.editText?.text.toString()
        val offerLoginDialog = RegisteredDialog.createRedefineRegisterInputPhoneOfferLogin(requireActivity(), phone)

        offerLoginDialog.setPrimaryCTAClickListener {
            goToVerification(
                phone,
                otpType = RegisterConstants.OtpType.OTP_LOGIN_PHONE_NUMBER,
                requireActivity()
            )
            offerLoginDialog.dismiss()
        }

        offerLoginDialog.setSecondaryCTAClickListener {
            offerLoginDialog.dismiss()
        }

        offerLoginDialog.show()
    }

    private fun showDialogSuccess() {
        val phone = binding?.fieldInputPhone?.editText?.text.toString()
        val confirmDialog = RegisteredDialog.createRedefineRegisterInputPhoneOfferSuccess(requireActivity(), phone)

        confirmDialog.setPrimaryCTAClickListener {
            confirmDialog.dismiss()
            //goto
        }

        confirmDialog.setSecondaryCTAClickListener {
            confirmDialog.dismiss()
        }

        confirmDialog.show()
    }

    private fun showDialogFailed() {
        val failedDialog = RegisteredDialog.createRedefineRegisterInputPhoneFailed(requireActivity())

        failedDialog.setPrimaryCTAClickListener {
            goToTokopediaCare()
            failedDialog.dismiss()
        }

        failedDialog.setSecondaryCTAClickListener {
            failedDialog.dismiss()
        }

        failedDialog.show()
    }

    private fun goToVerification(phone: String = "", otpType: Int, context: Context) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivity(intent)
    }

    private fun goToTokopediaCare() {
        RouteManager.route(activity, String.format(
            TOKOPEDIA_CARE_STRING_FORMAT, ApplinkConst.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
        ))
    }

    private fun TextFieldUnify2.setMessageField(stringResource: Int) {
        isInputError = if (stringResource != RedefineRegisterInputPhoneViewModel.NOTHING_RESOURCE && stringResource != RedefineRegisterInputPhoneViewModel.RESOURCE_NOT_CHANGED) {
            setMessage(getString(stringResource))
            true
        } else {
            setMessage(SPACE)
            false
        }
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(RedefineRegisterEmailComponent::class.java).inject(this)
    }

    companion object {

        private const val EMPTY_STRING = ""

        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"

        private const val SPACE = " "
        private val SCREEN_NAME = RedefineRegisterInputPhoneFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = RedefineRegisterInputPhoneFragment()
    }
}