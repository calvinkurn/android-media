package com.tokopedia.loginregister.shopcreation.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_REGISTRATION_SHOP_CREATION
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.shopcreation.view.util.PhoneNumberTextWatcher
import com.tokopedia.loginregister.shopcreation.viewmodel.ShopCreationViewModel
import com.tokopedia.profilecommon.domain.pojo.UserProfileValidate
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-18.
 * ade.hadian@tokopedia.com
 */

class PhoneShopCreationFragment : BaseShopCreationFragment(), IOnBackPressed {

    private lateinit var toolbarShopCreation: Toolbar
    private lateinit var container: View
    private lateinit var buttonContinue: UnifyButton
    private lateinit var textFieldPhone: TextFieldUnify
    private lateinit var errorMessage: Typography

    private var phone: String = ""

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val shopCreationViewModel by lazy {
        viewModelProvider.get(ShopCreationViewModel::class.java)
    }

    override fun getScreenName(): String = SCREEN_REGISTRATION_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_phone_shop_creation, container, false)
        toolbarShopCreation = view.findViewById(R.id.toolbar_shop_creation)
        buttonContinue = view.findViewById(R.id.btn_continue)
        textFieldPhone = view.findViewById(R.id.text_field_phone)
        errorMessage = view.findViewById(R.id.error_message)
        this.container = view.findViewById(R.id.container)
        return view
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
        textFieldPhone.textFieldInput.let {
            it.isFocusableInTouchMode = true
            it.isFocusable = true
        }
    }

    override fun onPause() {
        super.onPause()
        textFieldPhone.textFieldInput.let {
            it.isFocusableInTouchMode = false
            it.isFocusable = false
        }
    }

    override fun getToolbar(): Toolbar = toolbarShopCreation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_LOGIN_PHONE -> {
                        data?.extras?.run {
                            val accessToken = getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                            val phoneNumber = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                            goToChooseAccountPage(accessToken, phoneNumber)
                        }
                    }
                    REQUEST_REGISTER_PHONE -> {
                        if (phone.isNotEmpty())
                            goToRegisterAddNamePage(phone.replace("-", ""))
                    }
                    REQUEST_NAME_SHOP_CREARION -> {
                        activity?.let {
                            it.setResult(Activity.RESULT_OK)
                            it.finish()
                        }
                    }
                    REQUEST_COTP_PHONE_VERIFICATION -> {
                        if (phone.isNotEmpty()) {
                            shopCreationViewModel.addPhone(phone.replace("-", ""))
                        } else {
                            toastError(getString(R.string.please_fill_phone_number))
                        }
                    }
                    REQUEST_CHOOSE_ACCOUNT -> {
                        activity?.let {
                            it.setResult(Activity.RESULT_OK)
                            it.finish()
                        }
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        shopCreationAnalytics.eventClickBackPhoneShopCreation()
        LetUtil.ifLet(context, view?.parent) { (context, view) ->
            hideKeyboardFrom(context as Context, view as View)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        shopCreationViewModel.addPhoneResponse.removeObservers(this)
        shopCreationViewModel.validateUserProfileResponse.removeObservers(this)
        shopCreationViewModel.registerCheckResponse.removeObservers(this)
        shopCreationViewModel.flush()
    }

    private fun initView() {
        textFieldPhone.textFieldInput.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        textFieldPhone.textFieldInput.addTextChangedListener(object : PhoneNumberTextWatcher(textFieldPhone.textFieldInput) {
            override fun onTextChanged(s: CharSequence, cursorPosition: Int, before: Int, count: Int) {
                super.onTextChanged(s, cursorPosition, before, count)
                if (isValidPhone(s.toString())) {
                    buttonContinue.isEnabled = true
                    textFieldPhone.setError(false)
                    clearMessageFieldPhone()
                }
            }

            override fun afterTextChanged(s: Editable) {
                super.afterTextChanged(s)
                removeFirstZeroPhoneNUmber(s)
            }
        })

        textFieldPhone.textFieldInput.setOnClickListener {
            it.isFocusableInTouchMode = true
            it.isFocusable = true
        }

        textFieldPhone.textFieldInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                LetUtil.ifLet(context, v) { (context, view) ->
                    showKeyboardFrom(context as Context, view as View)
                }
            }
        }

        buttonContinue.setOnClickListener {
            shopCreationAnalytics.eventClickContinuePhoneShopCreation()
            phone = textFieldPhone.textFieldInput.text.toString().trim()
            if (phone.isNotEmpty()) {
                phone = "0$phone"
                buttonContinue.isLoading = true
                if (userSession.isLoggedIn) {
                    shopCreationViewModel.validateUserProfile(phone.replace("-", ""))
                } else {
                    shopCreationViewModel.registerCheck(phone.replace("-", ""))
                }
            } else {
                emptyStatePhoneField()
            }
        }

        container.setOnClickListener {
            textFieldPhone.textFieldInput.let {
                it.isFocusableInTouchMode = false
                it.isFocusable = false
            }

            LetUtil.ifLet(context, view?.parent) { (context, view) ->
                hideKeyboardFrom(context as Context, view as View)
            }
        }
    }

    private fun initObserver() {
        shopCreationViewModel.addPhoneResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessAddPhone()
                }
                is Fail -> {
                    onFailedAddPhone(it.throwable)
                }
            }
        })
        shopCreationViewModel.validateUserProfileResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessValidateUserProfile(it.data)
                }
                is Fail -> {
                    onFailedValidateUserProfile(it.throwable)
                }
            }
        })
        shopCreationViewModel.registerCheckResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessRegisterCheck(it.data)
                }
                is Fail -> {
                    onFailedRegisterCheck(it.throwable)
                }
            }
        })
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    private fun storeLocalSession(phone: String) {
        userSession.setIsMSISDNVerified(true)
        userSession.phoneNumber = phone
    }

    private fun onSuccessRegisterCheck(registerCheckData: RegisterCheckData) {
        when (registerCheckData.registerType) {
            PHONE_TYPE -> {
                if (registerCheckData.isExist) {
                    goToLoginPhoneVerifyPage(registerCheckData.view.replace("-", ""))
                } else {
                    goToRegisterPhoneVerifyPage(registerCheckData.view.replace("-", ""))
                }
            }
            else -> invalidTypeStatePhoneField()
        }

        buttonContinue.isLoading = false
    }

    private fun onFailedRegisterCheck(throwable: Throwable) {
        toastError(throwable)
        buttonContinue.isLoading = false
        buttonContinue.isEnabled = false
    }

    private fun onSuccessAddPhone() {
        storeLocalSession(phone.replace("-", ""))
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedAddPhone(throwable: Throwable) {
        toastError(throwable)
    }

    private fun onSuccessValidateUserProfile(userProfileValidate: UserProfileValidate) {
        if (userProfileValidate.isValid && phone.isNotEmpty()) {
            goToAddPhoneVerifyPage(phone.replace("-", ""))
        } else {
            toastError(getString(R.string.please_fill_phone_number))
        }

        buttonContinue.isLoading = false
    }

    private fun onFailedValidateUserProfile(throwable: Throwable) {
        toastError(throwable)
        buttonContinue.isLoading = false
        buttonContinue.isEnabled = false
    }

    private fun emptyStatePhoneField() {
        textFieldPhone.setError(true)
        setMessageFieldPhone(getString(R.string.please_fill_phone_number))
    }

    private fun invalidTypeStatePhoneField() {
        textFieldPhone.setError(true)
        setMessageFieldPhone(getString(R.string.phone_number_invalid))
    }

    private fun setMessageFieldPhone(message: String) {
        errorMessage.text = message
        context?.let {
            errorMessage.text = message
            errorMessage.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
        }
    }

    private fun clearMessageFieldPhone() {
        context?.let {
            errorMessage.text = getString(R.string.desc_phone_shop_creation)
            errorMessage.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
        }
    }

    private fun toastError(throwable: Throwable) {
        throwable.message?.let { toastError(it) }
    }

    private fun toastError(message: String) {
        view?.run {
            Toaster.make(this, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
        }
    }

    private fun goToLoginPhoneVerifyPage(phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_LOGIN_PHONE_NUMBER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_LOGIN_PHONE)
    }

    private fun goToRegisterPhoneVerifyPage(phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_REGISTER_PHONE_NUMBER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_REGISTER_PHONE)
    }

    private fun goToAddPhoneVerifyPage(phoneNumber: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_COTP_PHONE_VERIFICATION)
    }

    private fun goToRegisterAddNamePage(phone: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.NAME_SHOP_CREATION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phone)

        startActivityForResult(intent, REQUEST_NAME_SHOP_CREARION)
    }

    private fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
    }

    private fun removeFirstZeroPhoneNUmber(s: Editable) {
        s.toString()?.let {
            if (it.isNotEmpty() && it.first().toString() == "0") {
                textFieldPhone.textFieldInput.setText(it.drop(1))
            }
        }
    }

    companion object {

        private const val REQUEST_LOGIN_PHONE = 112
        private const val REQUEST_REGISTER_PHONE = 113
        private const val REQUEST_NAME_SHOP_CREARION = 114
        private const val REQUEST_COTP_PHONE_VERIFICATION = 115
        private const val REQUEST_CHOOSE_ACCOUNT = 116

        private const val OTP_TYPE_PHONE_VERIFICATION = 11
        private const val OTP_TYPE_REGISTER_PHONE_NUMBER = 116
        private const val OTP_TYPE_LOGIN_PHONE_NUMBER = 112

        private const val PHONE_TYPE = "phone"

        fun createInstance(bundle: Bundle): PhoneShopCreationFragment {
            val fragment = PhoneShopCreationFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun isValidPhone(phone: String): Boolean = Patterns.PHONE.matcher(phone).matches() && phone.length >= 6
    }
}