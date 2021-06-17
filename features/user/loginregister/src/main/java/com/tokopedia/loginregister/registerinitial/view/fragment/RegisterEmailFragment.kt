package com.tokopedia.loginregister.registerinitial.view.fragment

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics.Companion.SCREEN_REGISTER_EMAIL
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestData
import com.tokopedia.loginregister.registerinitial.viewmodel.RegisterInitialViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.util.PasswordUtils
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 10/25/18.
 */
class RegisterEmailFragment : BaseDaggerFragment() {
    var NAME = "NAME"
    var PASSWORD = "PASSWORD"
    var EMAIL = "EMAIL"
    var isEnableEncryption = false
    var redirectView: View? = null
    var registerButton: UnifyButton? = null
    var wrapperName: TextFieldUnify? = null
    var wrapperEmail: TextFieldUnify? = null
    var wrapperPassword: TextFieldUnify? = null
    var registerNextTAndC: Typography? = null
    var progressBar: LoaderUnify? = null
    var source = ""
    var token = ""

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModelProvider: ViewModelProvider
    lateinit var registerInitialViewModel: RegisterInitialViewModel

    //** see fragment_register_email
    private val REGISTER_BUTTON_IME = 123321
    override fun onStart() {
        super.onStart()
        activity?.let {
            analytics?.trackScreen(it, screenName)
        }
    }

    override fun initInjector() {
        val daggerLoginComponent = DaggerRegisterInitialComponent
                .builder()
                .loginRegisterComponent(getComponent(LoginRegisterComponent::class.java))
                .build() as DaggerRegisterInitialComponent
        daggerLoginComponent.inject(this)
    }

    override fun getScreenName(): String {
        return SCREEN_REGISTER_EMAIL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        registerInitialViewModel = viewModelProvider?.get(RegisterInitialViewModel::class.java)
        fetchRemoteConfig()
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register_with_email, parent, false)
        redirectView = view.findViewById(R.id.redirect_reset_password)
        registerButton = view.findViewById(R.id.register_button)
        wrapperName = view.findViewById(R.id.wrapper_name)
        wrapperEmail = view.findViewById(R.id.wrapper_email)
        wrapperPassword = view.findViewById(R.id.wrapper_password)
        progressBar = view.findViewById(R.id.progress_bar)
        registerNextTAndC = view.findViewById(R.id.register_next_detail_t_and_p)
        prepareView()
        setViewListener()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
    private fun prepareView() {
        arguments?.run {
            wrapperEmail?.textFieldInput?.setText(arguments?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, ""))
            wrapperEmail?.textFieldInput?.isEnabled = false
            token = getString(ApplinkConstInternalGlobal.PARAM_TOKEN, "")
            source = getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "")
        }
        initObserver()
        initTermPrivacyView()
        showPasswordHint()
        showNameHint()
    }

    private fun initObserver() {
        registerInitialViewModel?.registerRequestResponse?.observe(viewLifecycleOwner, Observer { registerRequestDataResult: Result<RegisterRequestData>? ->
            if (registerRequestDataResult is Success) {
                val data = (registerRequestDataResult).data
                userSession?.clearToken()
                userSession?.setToken(data.accessToken, data.tokenType, EncoderDecoder.Encrypt(data.refreshToken, userSession.refreshTokenIV))
                onSuccessRegister()
                if (activity != null) {
                    val intent = Intent()
                    intent.putExtra(ApplinkConstInternalGlobal.PARAM_ACTION, data.action)
                    intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
            } else if (registerRequestDataResult is Fail) {
                val throwable = registerRequestDataResult.throwable
                dismissLoadingProgress()
                val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                if(throwable is MessageErrorException){
                    throwable.message?.run {
                        if(this.contains(ALREADY_REGISTERED)){
                            showInfo()
                        } else {
                            onErrorRegister(errorMessage)
                        }
                    }
                } else {
                    if (context != null) {
                        val forbiddenMessage = context?.getString(
                                com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
                        if (errorMessage == forbiddenMessage) {
                            onForbidden()
                        } else {
                            onErrorRegister(errorMessage)
                        }
                    }
                }
            }
        })
    }

    private fun fetchRemoteConfig() {
        if (context != null) {
            val firebaseRemoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
            isEnableEncryption = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_REGISTER_ENCRYPTION, false)
        }
    }

    private fun initTermPrivacyView() {
        context?.run {
            val termPrivacy = SpannableString(getString(R.string.detail_term_and_privacy))
            termPrivacy.setSpan(clickableSpan(PAGE_TERM_AND_CONDITION), 34, 54, 0)
            termPrivacy.setSpan(clickableSpan(PAGE_PRIVACY_POLICY), 61, 78, 0)
            termPrivacy.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500)), 34, 54, 0)
            termPrivacy.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500)), 61, 78, 0)
            registerNextTAndC?.setText(termPrivacy, TextView.BufferType.SPANNABLE)
            registerNextTAndC?.movementMethod = LinkMovementMethod.getInstance()
            registerNextTAndC?.isSelected = false
        }
    }

    private fun clickableSpan(page: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.TERM_PRIVACY, page))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }
    }

    private fun getSpannable(sourceString: String, hyperlinkString: String): Spannable {
        val spannable: Spannable = SpannableString(sourceString)
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {}
            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }, sourceString.indexOf(hyperlinkString), sourceString.length, 0)
        return spannable
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            wrapperName?.textFieldInput?.setText(savedInstanceState.getString(NAME, ""))
            wrapperEmail?.textFieldInput?.setText(savedInstanceState.getString(EMAIL, ""))
            wrapperEmail?.textFieldInput?.isEnabled = false
            wrapperPassword?.textFieldInput?.setText(savedInstanceState.getString(PASSWORD, ""))
        }
    }

    override fun onResume() {
        super.onResume()
        wrapperEmail?.textFieldInput?.addTextChangedListener(emailWatcher(wrapperEmail))
        wrapperPassword?.textFieldInput?.addTextChangedListener(passwordWatcher(wrapperPassword))
        wrapperName?.textFieldInput?.addTextChangedListener(nameWatcher(wrapperName))
        if(userSession?.isLoggedIn == true) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    private fun nameWatcher(wrapper: TextFieldUnify?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperErrorNew(wrapper, null)
                }
            }

            override fun afterTextChanged(s: Editable) {
                showNameHint()
                when {
                    s.isEmpty() -> { setWrapperErrorNew(wrapper, getString(R.string.error_field_required)) }
                    s.length < 3 -> { setWrapperErrorNew(wrapper, getString(R.string.error_minimal_name)) }
                    RegisterUtil.checkRegexNameLocal(wrapperName?.textFieldInput?.text.toString()) -> {
                        setWrapperErrorNew(wrapper, getString(R.string.error_illegal_character))
                    }
                    RegisterUtil.isExceedMaxCharacter(wrapperName?.textFieldInput?.text.toString()) -> {
                        setWrapperErrorNew(wrapper, getString(R.string.error_max_35_character))
                    }
                }
                checkIsValidForm()
            }
        }
    }

    private fun passwordWatcher(wrapper: TextFieldUnify?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperErrorNew(wrapper, null)
                }
            }

            override fun afterTextChanged(s: Editable) {
                showPasswordHint()
                if (s.isEmpty()) { setWrapperErrorNew(wrapper, getString(R.string.error_field_required)) }
                checkIsValidForm()
            }
        }
    }

    private fun emailWatcher(wrapper: TextFieldUnify?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperErrorNew(wrapper, null)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_field_required))
                } else if (!Patterns.EMAIL_ADDRESS.matcher(wrapperEmail?.textFieldInput?.text.toString()).matches()) {
                    setWrapperErrorNew(wrapper, getString(R.string.wrong_email_format))
                }
                checkIsValidForm()
            }
        }
    }

    val emailListOfAccountsUserHasLoggedInto: List<String>
        get() {
            val listOfAddresses: MutableSet<String> = LinkedHashSet()
            val emailPattern = Patterns.EMAIL_ADDRESS
            val accounts = AccountManager.get(activity).getAccountsByType("com.google")
            for (account in accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    listOfAddresses.add(account.name)
                }
            }
            return ArrayList(listOfAddresses)
        }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewListener() {
        wrapperPassword?.textFieldInput?.setOnEditorActionListener { v: TextView?, id: Int, event: KeyEvent? ->
            if (id == REGISTER_BUTTON_IME || id == EditorInfo.IME_NULL) {
                registerEmail()
                return@setOnEditorActionListener true
            }
            false
        }
        registerButton?.setOnClickListener { v: View? -> registerEmail() }
    }

    private fun validatePasswordInput(): Boolean {
        wrapperPassword?.textFieldInput?.text?.toString()?.run {
            return when {
                isEmpty() -> {
                    setWrapperErrorNew(wrapperPassword, getString(R.string.error_field_required))
                    false
                }
                PasswordUtils.isTooShortLength(this) -> {
                    setWrapperErrorNew(wrapperPassword, getString(R.string.error_minimal_password))
                    false
                }
                PasswordUtils.isExceedMaximumLength(this) -> {
                    setWrapperErrorNew(wrapperPassword, getString(R.string.error_maximal_password))
                    false
                } else -> true
            }
        }
        return false
    }

    private fun registerEmail() {
        showLoadingProgress()
        if(validatePasswordInput()) {
            registerAnalytics?.trackClickSignUpButtonEmail()
            if (isUseEncryption()) {
                registerInitialViewModel?.registerRequestV2(
                        wrapperEmail?.textFieldInput?.text.toString(),
                        wrapperPassword?.textFieldInput?.text.toString(),
                        wrapperName?.textFieldInput?.text.toString(),
                        token
                )
            } else {
                registerInitialViewModel?.registerRequest(
                        wrapperEmail?.textFieldInput?.text.toString(),
                        wrapperPassword?.textFieldInput?.text.toString(),
                        wrapperName?.textFieldInput?.text.toString(),
                        token
                )
            }
        } else {
            dismissLoadingProgress()
        }
    }

    private fun checkIsValidForm() {
        if (RegisterUtil.isCanRegister(wrapperName?.textFieldInput?.text.toString(), wrapperEmail?.textFieldInput?.text.toString(), wrapperPassword?.textFieldInput?.text.toString())) {
            setRegisterButtonEnabled()
        } else {
            setRegisterButtonDisabled()
        }
    }

    private fun setRegisterButtonEnabled() {
        if (activity != null) {
            registerButton?.isEnabled = true
        }
    }

    private fun setRegisterButtonDisabled() {
        if (activity != null) {
            registerButton?.isEnabled = false
        }
    }

    private fun setWrapperErrorNew(wrapper: TextFieldUnify?, s: String?) {
        if (s == null) {
            wrapper?.setMessage("")
            wrapper?.setError(false)
        } else {
            wrapper?.setError(true)
            wrapper?.setMessage(s)
        }
    }

    private fun setWrapperHint(wrapper: TextFieldUnify?, s: String) {
        wrapper?.setError(false)
        wrapper?.setMessage(s)
    }

    fun resetError() {
        setWrapperErrorNew(wrapperName, null)
        setWrapperErrorNew(wrapperEmail, null)
        showPasswordHint()
        showNameHint()
    }

    fun showPasswordHint() {
        wrapperPassword?.setError(false)
        wrapperPassword?.setMessage(resources.getString(R.string.minimal_8_character))
    }

    fun showNameHint() {
        setWrapperHint(wrapperName, "  ")
    }

    fun setActionsEnabled(isEnabled: Boolean) {
        wrapperEmail?.textFieldInput?.isEnabled = isEnabled
        wrapperName?.textFieldInput?.isEnabled = isEnabled
        wrapperPassword?.textFieldInput?.isEnabled = isEnabled
        registerButton?.isEnabled = isEnabled
    }

    fun showLoadingProgress() {
        setActionsEnabled(false)
        progressBar?.visibility = View.VISIBLE
    }

    fun dismissLoadingProgress() {
        setActionsEnabled(true)
        progressBar?.visibility = View.GONE
    }

    fun onErrorRegister(errorMessage: String?) {
        dismissLoadingProgress()
        onFailedRegisterEmail(errorMessage)
        setActionsEnabled(true)
        if (errorMessage == "") NetworkErrorHelper.showSnackbar(activity) else NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    fun onSuccessRegister() {
        if (activity != null) {
            dismissLoadingProgress()
            setActionsEnabled(true)
            lostViewFocus()
            registerAnalytics?.trackSuccessClickSignUpButtonEmail()
        }
    }

    fun lostViewFocus() {
        wrapperEmail?.textFieldInput?.clearFocus()
        wrapperName?.textFieldInput?.clearFocus()
        wrapperPassword?.textFieldInput?.clearFocus()
        registerButton?.clearFocus()
    }

    private val isEmailAddressFromDevice: Boolean
        private get() {
            val list = emailListOfAccountsUserHasLoggedInto
            var result = false
            if (list.size > 0) {
                for (e in list) {
                    if (e == wrapperEmail?.textFieldInput?.text.toString()) {
                        result = true
                        break
                    }
                }
            }
            return result
        }

    fun showInfo() {
        dismissLoadingProgress()
        val view: Typography? = redirectView?.findViewById(R.id.body)
        val emailString = wrapperEmail?.textFieldInput?.text.toString()
        val text = getString(R.string.account_registered_body, emailString)
        val part = getString(R.string.account_registered_body_part)
        val spannable = getSpannable(text, part)
        spannable.setSpan(StyleSpan(Typeface.BOLD), text.indexOf(emailString), text.indexOf(emailString) + emailString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view?.setText(spannable, TextView.BufferType.SPANNABLE)
        view?.setOnClickListener {
            if (activity != null) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailString)
                startActivity(intent)
            }
        }
        redirectView?.visibility = View.VISIBLE
    }

    fun onForbidden() {
        ForbiddenActivity.startActivity(activity)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(NAME, wrapperName?.textFieldInput?.text.toString())
        outState.putString(EMAIL, wrapperEmail?.textFieldInput?.text.toString())
        outState.putString(PASSWORD, wrapperPassword?.textFieldInput?.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_AUTO_LOGIN -> if (activity != null && resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } else {
                dismissLoadingProgress()
            }
            REQUEST_ACTIVATE_ACCOUNT -> if (resultCode == Activity.RESULT_OK && activity != null) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } else {
                dismissLoadingProgress()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val abTestingRemoteConfig: RemoteConfig
        get() = RemoteConfigInstance.getInstance().abTestPlatform

    fun isEnableEncryptRollout(): Boolean {
        val rolloutKey = if(GlobalConfig.isSellerApp()) {
            SessionConstants.Rollout.ROLLOUT_REGISTER_ENCRYPTION_SELLER
        } else {
            SessionConstants.Rollout.ROLLOUT_REGISTER_ENCRYPTION
        }

        val variant = abTestingRemoteConfig.getString(rolloutKey)
        return variant.isNotEmpty()
    }

    private fun isUseEncryption(): Boolean {
        return isEnableEncryptRollout() && isEnableEncryption
    }

    private fun onFailedRegisterEmail(errorMessage: String?) {
        registerAnalytics?.trackFailedClickEmailSignUpButton(errorMessage ?: "")
        registerAnalytics?.trackFailedClickSignUpButtonEmail(errorMessage ?: "")
    }

    val isAutoVerify: Int
        get() = if (isEmailAddressFromDevice) 1 else 0


    fun onBackPressed() {
        registerAnalytics?.trackClickOnBackButtonRegisterEmail()
    }

    companion object {
        private const val REQUEST_AUTO_LOGIN = 101
        private const val REQUEST_ACTIVATE_ACCOUNT = 102

        private const val ALREADY_REGISTERED = "sudah terdaftar"
        private const val GO_TO_REGISTER = 0
        private const val GO_TO_ACTIVATION_PAGE = 1
        private const val GO_TO_LOGIN = 2
        private const val GO_TO_RESET_PASSWORD = 3
        private const val STATUS_ACTIVE = 1
        private const val STATUS_PENDING = -1
        private const val STATUS_INACTIVE = 0
        fun createInstance(bundle: Bundle?): RegisterEmailFragment {
            val fragment = RegisterEmailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}