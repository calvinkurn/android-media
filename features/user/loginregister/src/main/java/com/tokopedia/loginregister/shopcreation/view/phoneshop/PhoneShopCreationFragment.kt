package com.tokopedia.loginregister.shopcreation.view.phoneshop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_REGISTRATION_SHOP_CREATION
import com.tokopedia.loginregister.databinding.FragmentPhoneShopCreationBinding
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.data.RegisterCheckData
import com.tokopedia.loginregister.shopcreation.data.UserProfileValidate
import com.tokopedia.loginregister.shopcreation.util.PhoneNumberTextWatcher
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationFragment
import com.tokopedia.loginregister.shopcreation.view.ShopCreationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.style.UserConsentTncPolicyUtil
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-18.
 * ade.hadian@tokopedia.com
 */

class PhoneShopCreationFragment : BaseShopCreationFragment(), IOnBackPressed {

    private var phone: String = ""

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val shopCreationViewModel by lazy {
        viewModelProvider.get(ShopCreationViewModel::class.java)
    }

    private var viewBinding by autoClearedNullable<FragmentPhoneShopCreationBinding>()

    override fun getScreenName(): String = SCREEN_REGISTRATION_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentPhoneShopCreationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
        viewBinding?.textFieldPhone?.textFieldInput.let {
            it?.isFocusableInTouchMode = true
            it?.isFocusable = true
        }
    }

    override fun onPause() {
        super.onPause()
        viewBinding?.textFieldPhone?.textFieldInput.let {
            it?.isFocusableInTouchMode = false
            it?.isFocusable = false
        }
    }

    override fun getToolbar(): Toolbar = viewBinding?.toolbarShopCreation as Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    LoginConstants.Request.REQUEST_LOGIN_PHONE -> {
                        data?.extras?.run {
                            val accessToken = getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                            val phoneNumber = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                            goToChooseAccountPage(accessToken, phoneNumber)
                        }
                    }
                    LoginConstants.Request.REQUEST_REGISTER_PHONE -> {
                        if (phone.isNotEmpty()) {
                            val validateToken =
                                data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                                    .orEmpty()
                            goToRegisterAddNamePage(phone.replace("-", ""), validateToken)
                        }
                    }
                    REQUEST_NAME_SHOP_CREARION -> {
                        activity?.let {
                            it.setResult(Activity.RESULT_OK)
                            it.finish()
                        }
                    }
                    REQUEST_COTP_PHONE_VERIFICATION -> {
                        if (phone.isNotEmpty()) {
                            val validateToken =
                                data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                                    .orEmpty()
                            shopCreationViewModel.addPhone(phone.replace("-", ""), validateToken)
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
        viewBinding?.textFieldPhone?.textFieldInput?.setTextColor(
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
        )
        viewBinding?.textFieldPhone?.textFieldInput?.let {
            it.addTextChangedListener(object :
                PhoneNumberTextWatcher(it) {
                override fun onTextChanged(
                    s: CharSequence,
                    cursorPosition: Int,
                    before: Int,
                    count: Int,
                ) {
                    super.onTextChanged(s, cursorPosition, before, count)
                    if (isValidPhone(s.toString())) {
                        viewBinding?.btnContinue?.isEnabled = true
                        viewBinding?.textFieldPhone?.setError(false)
                        clearMessageFieldPhone()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    super.afterTextChanged(s)
                    removeFirstZeroPhoneNUmber(s)
                    removePhoneMaskingAtFirst(s)
                }
            })
        }

        viewBinding?.textFieldPhone?.textFieldInput?.setOnClickListener {
            it.isFocusableInTouchMode = true
            it.isFocusable = true
        }

        viewBinding?.textFieldPhone?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                LetUtil.ifLet(context, v) { (context, view) ->
                    showKeyboardFrom(context as Context, view as View)
                }
            }
        }

        renderTncPolicy()

        viewBinding?.btnContinue?.setOnClickListener {
            shopCreationAnalytics.eventClickContinuePhoneShopCreation()
            phone = viewBinding?.textFieldPhone?.textFieldInput?.text.toString().trim()
            if (phone.isNotEmpty()) {
                phone = "0$phone"
                viewBinding?.btnContinue?.isLoading = true
                if (userSession.isLoggedIn) {
                    shopCreationViewModel.validateUserProfile(phone.replace("-", ""))
                } else {
                    shopCreationViewModel.registerCheck(phone.replace("-", ""))
                }
            } else {
                emptyStatePhoneField()
            }
        }

        viewBinding?.container?.setOnClickListener {
            viewBinding?.textFieldPhone?.textFieldInput.let {
                it?.isFocusableInTouchMode = false
                it?.isFocusable = false
            }

            LetUtil.ifLet(context, view?.parent) { (context, view) ->
                hideKeyboardFrom(context as Context, view as View)
            }
        }
    }

    private fun renderTncPolicy() {
        viewBinding?.tncPolicy?.text = UserConsentTncPolicyUtil.generateText(
            message = getString(R.string.phone_shop_tnc_policy),
            tncTitle = getString(R.string.phone_shop_tnc_title),
            policyTitle = getString(R.string.phone_shop_policy_title),
            actionTnc = {
                RouteManager.route(
                    activity,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TNC)
                )
            },
            actionPolicy = {
                RouteManager.route(
                    activity,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_POLICY)
                )
            },
            colorSpan = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        )
        viewBinding?.tncPolicy?.movementMethod = LinkMovementMethod.getInstance()
        viewBinding?.layoutTnc?.visibility = View.VISIBLE
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
            LoginConstants.LoginType.PHONE_TYPE -> {
                if (registerCheckData.isExist) {
                    goToLoginPhoneVerifyPage(registerCheckData.view.replace("-", ""))
                } else {
                    goToRegisterPhoneVerifyPage(registerCheckData.view.replace("-", ""))
                }
            }
            else -> invalidTypeStatePhoneField()
        }

        viewBinding?.btnContinue?.isLoading = false
    }

    private fun onFailedRegisterCheck(throwable: Throwable) {
        toastError(throwable)
        viewBinding?.btnContinue?.isLoading = false
        viewBinding?.btnContinue?.isEnabled = false
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

        viewBinding?.btnContinue?.isLoading = false
    }

    private fun onFailedValidateUserProfile(throwable: Throwable) {
        toastError(throwable)
        viewBinding?.btnContinue?.isLoading = false
        viewBinding?.btnContinue?.isEnabled = false
    }

    private fun emptyStatePhoneField() {
        viewBinding?.textFieldPhone?.setError(true)
        setMessageFieldPhone(getString(R.string.please_fill_phone_number))
    }

    private fun invalidTypeStatePhoneField() {
        viewBinding?.textFieldPhone?.setError(true)
        setMessageFieldPhone(getString(R.string.phone_number_invalid))
    }

    private fun setMessageFieldPhone(message: String) {
        viewBinding?.errorMessage?.text = message
        context?.let {
            viewBinding?.errorMessage?.text = message
            viewBinding?.errorMessage?.setTextColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
            )
        }
    }

    private fun clearMessageFieldPhone() {
        context?.let {
            viewBinding?.errorMessage?.text = getString(R.string.desc_phone_shop_creation)
            viewBinding?.errorMessage?.setTextColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
            )
        }
    }

    private fun toastError(throwable: Throwable) {
        throwable.message?.let { toastError(it) }
    }

    private fun toastError(message: String) {
        view?.run {
            Toaster.build(this, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
        }
    }

    private fun goToLoginPhoneVerifyPage(phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_OTP_TYPE,
            LoginConstants.OtpType.OTP_LOGIN_PHONE_NUMBER
        )
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW,
            getIsLoginRegisterFlow()
        )
        startActivityForResult(intent, LoginConstants.Request.REQUEST_LOGIN_PHONE)
    }

    private fun goToRegisterPhoneVerifyPage(phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_OTP_TYPE,
            RegisterConstants.OtpType.OTP_REGISTER_PHONE_NUMBER
        )
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW,
            getIsLoginRegisterFlow()
        )
        startActivityForResult(intent, LoginConstants.Request.REQUEST_REGISTER_PHONE)
    }

    private fun goToAddPhoneVerifyPage(phoneNumber: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW,
            getIsLoginRegisterFlow()
        )
        startActivityForResult(intent, REQUEST_COTP_PHONE_VERIFICATION)
    }

    private fun getIsLoginRegisterFlow(): Boolean {
        return if (GlobalConfig.isSellerApp()) {
            true
        } else {
            return !userSession.isLoggedIn
        }
    }

    private fun goToRegisterAddNamePage(phone: String, validateToken: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.NAME_SHOP_CREATION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)

        startActivityForResult(intent, REQUEST_NAME_SHOP_CREARION)
    }

    private fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
    }

    private fun removeFirstZeroPhoneNUmber(s: Editable) {
        s.toString().let {
            if (it.isNotEmpty() && it.first().toString() == "0") {
                viewBinding?.textFieldPhone?.textFieldInput?.setText(it.drop(1))
            }
        }
    }

    private fun removePhoneMaskingAtFirst(s: Editable) {
        s.toString().let {
            if (it.isNotEmpty() && it.first().toString() == "-") {
                viewBinding?.textFieldPhone?.textFieldInput?.setText(it.drop(1))
            }
        }
    }

    companion object {

        private const val REQUEST_NAME_SHOP_CREARION = 114
        private const val REQUEST_COTP_PHONE_VERIFICATION = 115
        private const val REQUEST_CHOOSE_ACCOUNT = 116

        private const val OTP_TYPE_PHONE_VERIFICATION = 11

        private const val MIN_PHONE_LENGTH = 6

        private val URL_TNC = "${TokopediaUrl.getInstance().WEB}terms?lang=id"
        private val URL_POLICY = "${TokopediaUrl.getInstance().WEB}privacy?lang=id"

        fun createInstance(bundle: Bundle): PhoneShopCreationFragment {
            val fragment = PhoneShopCreationFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun isValidPhone(phone: String): Boolean =
            Patterns.PHONE.matcher(phone).matches() && phone.length >= MIN_PHONE_LENGTH
    }
}
