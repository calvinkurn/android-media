package com.tokopedia.loginregister.login.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.iris.Iris
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.activation.view.activity.ActivationActivity
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.login.di.DaggerLoginComponent
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.presenter.LoginEmailPhonePresenter
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.google.SmartLockActivity
import com.tokopedia.loginregister.registeremail.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView
import com.tokopedia.loginregister.welcomepage.WelcomePageActivity
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.Token.Companion.GOOGLE_API_KEY
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.network.TokenErrorException
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_login_with_phone.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 18/01/19.
 */
class LoginEmailPhoneFragment : BaseDaggerFragment(), LoginEmailPhoneContract.View {

    private val ID_ACTION_REGISTER = 111

    private val REQUEST_SMART_LOCK = 101
    private val REQUEST_SAVE_SMART_LOCK = 102
    private val REQUEST_SECURITY_QUESTION = 104
    private val REQUESTS_CREATE_PASSWORD = 106
    private val REQUEST_ACTIVATE_ACCOUNT = 107
    private val REQUEST_VERIFY_PHONE = 108
    private val REQUEST_ADD_NAME = 109
    private val REQUEST_CHOOSE_ACCOUNT = 110
    private val REQUEST_LOGIN_PHONE = 112
    private val REQUEST_REGISTER_PHONE = 113
    private val REQUEST_ADD_NAME_REGISTER_PHONE = 114
    private val REQUEST_WELCOME_PAGE = 115
    private val REQUEST_LOGIN_GOOGLE = 116

    private val LOGIN_LOAD_TRACE = "gb_login_trace"
    private val LOGIN_SUBMIT_TRACE = "gb_submit_login_trace"

    private var isTraceStopped: Boolean = false
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var mIris: Iris

    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    @Inject
    lateinit var presenter: LoginEmailPhonePresenter

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var partialRegisterInputView: PartialRegisterInputView
    private lateinit var loginLayout: LinearLayout
    private lateinit var loginButtonsContainer: LinearLayout
    private lateinit var emailPhoneEditText: EditText
    private lateinit var partialActionButton: TextView
    private lateinit var passwordEditText: TextInputEditText

    companion object {

        val IS_AUTO_LOGIN = "auto_login"
        val AUTO_LOGIN_METHOD = "method"

        val AUTO_LOGIN_EMAIL = "email"
        val AUTO_LOGIN_PASS = "pw"

        val IS_AUTO_FILL = "auto_fill"
        val AUTO_FILL_EMAIL = "email"
        val IS_FROM_REGISTER = "is_from_register"

        val FACEBOOK = "facebook"
        val GPLUS = "gplus"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginEmailPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getScreenName(): String {
        return LoginRegisterAnalytics.SCREEN_LOGIN
    }

    override fun initInjector() {
        val daggerLoginComponent = DaggerLoginComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent::class.java))
                .build() as DaggerLoginComponent

        daggerLoginComponent.inject(this)
        presenter.attachView(this, this)
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.trackScreen(this, screenName)
        }
    }

    override fun onResume() {
        super.onResume()
        if (userSession.isLoggedIn && activity != null) {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()
        }
    }

    override fun stopTrace() {
        if (!isTraceStopped) {
            performanceMonitoring.stopTrace()
            isTraceStopped = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.add(Menu.NONE, ID_ACTION_REGISTER, 0, "")
        val menuItem = menu.findItem(ID_ACTION_REGISTER)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        if (getDraw() != null) {
            menuItem.icon = getDraw()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDraw(): Drawable? {
        var drawable: TextDrawable? = null
        if (activity != null) {
            drawable = TextDrawable(activity!!)
            drawable.text = resources.getString(R.string.register)
            drawable.setTextColor(MethodChecker.getColor(context, R.color.tkpd_main_green))
        }
        return drawable
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == ID_ACTION_REGISTER) {
            registerAnalytics.trackClickTopSignUpButton()
            goToRegisterInitial()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()

        activity?.run {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(GOOGLE_API_KEY)
                    .requestEmail()
                    .requestProfile()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        performanceMonitoring = PerformanceMonitoring.start(LOGIN_LOAD_TRACE)
//     TODO UNCOMMENT
//        context?.run{
//            mIris = IrisAnalytics.getInstance(this)
//        }

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_login_with_phone, container, false)
        partialRegisterInputView = view.findViewById(R.id.login_input_view)
        loginLayout = view.findViewById(R.id.ll_layout)
        loginButtonsContainer = view.findViewById(R.id.login_container)
        emailPhoneEditText = partialRegisterInputView.findViewById(R.id.input_email_phone)
        partialActionButton = partialRegisterInputView.findViewById(R.id.register_btn)
        passwordEditText = partialRegisterInputView.findViewById(R.id.password)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
        showLoadingDiscover()
        context?.run {
            presenter.discoverLogin(this)
        }

        if (arguments != null && arguments!!.getBoolean(IS_AUTO_FILL, false)) {
            emailPhoneEditText.setText(arguments!!.getString(AUTO_FILL_EMAIL, ""))
        } else if (arguments != null && arguments!!.getBoolean(IS_AUTO_LOGIN, false)) {
            when (arguments!!.getInt(AUTO_LOGIN_METHOD)) {
                LoginActivity.METHOD_FACEBOOK -> onLoginFacebookClick()
                LoginActivity.METHOD_GOOGLE -> onLoginGoogleClick()
                LoginActivity.METHOD_EMAIL -> onLoginEmailClick()
                else -> showSmartLock()
            }
        } else {
            showSmartLock()
        }
    }

    private fun onLoginEmailClick() {
        val email = arguments!!.getString(AUTO_LOGIN_EMAIL, "")
        val pw = arguments!!.getString(AUTO_LOGIN_PASS, "")
        partialRegisterInputView.showLoginEmailView(email)
        emailPhoneEditText.setText(email)
        passwordEditText.setText(pw)
        presenter.loginEmail(email, pw)
        activity?.let {
            analytics.eventClickLoginEmailButton(it.applicationContext)
        }

    }

    private fun showSmartLock() {
        val intent = Intent(activity, SmartLockActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(SmartLockActivity.STATE, SmartLockActivity.RC_READ)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_SMART_LOCK)
    }


    override fun showLoadingDiscover() {
        val pb = ProgressBar(activity, null, android.R.attr.progressBarStyle)
        val lastPos = loginLayout.childCount - 1
        if (loginLayout.childCount >= 1 && loginLayout.getChildAt(lastPos) !is ProgressBar) {
            loginLayout.addView(pb, lastPos)
        }
    }

    override fun dismissLoadingDiscover() {
        val lastPos = loginLayout.childCount - 2
        if (loginLayout.childCount >= 2 && loginLayout.getChildAt(lastPos) is ProgressBar) {
            loginLayout.removeViewAt(lastPos)
        }
    }

    private fun prepareView() {
        emailPhoneEditText.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                showLoadingLogin()
                analytics.trackClickOnNext()
                presenter.checkLoginEmailPhone(emailPhoneEditText.text.toString())
                true
            } else {
                false
            }
        }

        partialActionButton.text = getString(R.string.next)
        partialActionButton.setOnClickListener {
            showLoadingLogin()
            analytics.trackClickOnNext()
            presenter.checkLoginEmailPhone(emailPhoneEditText.text.toString())
        }

        passwordEditText.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                presenter.loginEmail(emailPhoneEditText.text.toString().trim(),
                        passwordEditText.text.toString())
                activity?.let {
                    analytics.eventClickLoginEmailButton(it.applicationContext)
                    KeyboardHandler.hideSoftKeyboard(it)
                }
                performanceMonitoring = PerformanceMonitoring.start(LOGIN_SUBMIT_TRACE)
                true
            } else {
                false
            }
        }

        partialRegisterInputView.findViewById<TextView>(R.id.change_button).setOnClickListener {
            val email = emailPhoneEditText.text.toString()
            onChangeButtonClicked()
            emailPhoneEditText.setText(email)
            emailPhoneEditText.setSelection(emailPhoneEditText.text.length)
        }

        activity?.let { it ->
            val sourceString = it.resources.getString(R.string
                    .span_not_have_tokopedia_account)

            val spannable = SpannableString(sourceString)

            spannable.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {

                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                            activity, R.color.tkpd_main_green
                    )
                    ds.typeface = Typeface.create("sans-serif", Typeface
                            .NORMAL)
                }
            }, sourceString.indexOf("Daftar"), sourceString.length, 0)

            register_button.setText(spannable, TextView.BufferType.SPANNABLE)
            register_button.setOnClickListener {
                registerAnalytics.trackClickBottomSignUpButton()
                goToRegisterInitial()
            }

            val forgotPassword = partialRegisterInputView.findViewById<TextView>(R.id.forgot_pass)
            forgotPassword.setOnClickListener {
                analytics.trackClickForgotPassword()
                goToForgotPassword()
            }
        }

    }

    private fun onChangeButtonClicked() {
        analytics.trackChangeButtonClicked()

        emailPhoneEditText.imeOptions = EditorInfo.IME_ACTION_DONE

        partialActionButton.text = getString(R.string.next)
        partialActionButton.setOnClickListener { presenter.checkLoginEmailPhone(emailPhoneEditText.text.toString()) }
        partialRegisterInputView.showDefaultView()
    }

    private fun goToForgotPassword() {

        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailPhoneEditText.text.toString().trim())
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        analytics.eventClickForgotPasswordFromLogin(activity!!.applicationContext)

    }

    override fun onSuccessDiscoverLogin(providers: ArrayList<DiscoverItemViewModel>) {

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 20, 0, 15)
        loginButtonsContainer.removeAllViews()
        providers.forEach {
            val tv = LoginTextView(activity, MethodChecker.getColor(context, R.color.white))
            tv.tag = it.id
            tv.setText(it.name)
            if (!TextUtils.isEmpty(it.image)) {
                tv.setImage(it.image)
            } else if (it.imageResource != 0) {
                tv.setImageResource(it.imageResource)
            }
            tv.setRoundCorner(10)

            setDiscoverListener(it, tv)
            loginButtonsContainer.addView(tv, loginButtonsContainer.childCount, layoutParams)
        }
    }

    private fun setDiscoverListener(discoverItemViewModel: DiscoverItemViewModel,
                                    tv: LoginTextView) {

        if (discoverItemViewModel.id.equals(FACEBOOK, ignoreCase = true)) {
            tv.setOnClickListener { onLoginFacebookClick() }
        } else if (discoverItemViewModel.id.equals(GPLUS, ignoreCase = true)) {
            tv.setOnClickListener { onLoginGoogleClick() }
        }
    }

    private fun onLoginGoogleClick() {
        if (activity != null) {

            analytics.eventClickLoginGoogle(activity!!.applicationContext)

            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, REQUEST_LOGIN_GOOGLE)
        }

    }

    private fun onLoginFacebookClick() {
        if (activity != null) {
            analytics.eventClickLoginFacebook(activity!!.applicationContext)
            presenter.getFacebookCredential(this, callbackManager)
        }
    }

    override fun getFacebookCredentialListener(): GetFacebookCredentialSubscriber.GetFacebookCredentialListener {
        return object : GetFacebookCredentialSubscriber.GetFacebookCredentialListener {

            override fun onErrorGetFacebookCredential(e: Exception) {
                if (isAdded && activity != null) {
                    onErrorLogin(ErrorHandler.getErrorMessage(context, e))
                }
            }

            override fun onSuccessGetFacebookCredential(accessToken: AccessToken, email: String) {
                context?.run {
                    presenter.loginFacebook(this, accessToken, email)
                }
            }
        }
    }

    override fun showLoadingLogin() {
        showLoading(true)
    }

    override fun dismissLoadingLogin() {
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        val shortAnimTime = resources.getInteger(
                android.R.integer.config_shortAnimTime)

        progress_bar.animate().setDuration(shortAnimTime.toLong())
                .alpha((if (isLoading) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (progress_bar != null) {
                            progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
                        }
                    }
                })

        container.animate().setDuration(shortAnimTime.toLong())
                .alpha((if (isLoading) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (container != null) {
                            container.visibility = if (isLoading) View.GONE else View.VISIBLE
                        }
                    }
                })

    }

    private fun goToRegisterInitial() {
        if (activity != null) {
            analytics.eventClickRegisterFromLogin()
            val intent = RegisterInitialActivity.getCallingIntent(activity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            activity!!.finish()
        }
    }

    override fun onErrorDiscoverLogin(errorMessage: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage)
        {
            context?.run {
                presenter.discoverLogin(this)
            }
        }.showRetrySnackbar()
    }

    override fun onSuccessLogin() {
        dismissLoadingLogin()

        if (emailPhoneEditText.text.isNotBlank())
            userSession.autofillUserData = emailPhoneEditText.text.toString()


        if (activity != null) {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()

            analytics.eventSuccessLogin(userSession.loginMethod, registerAnalytics)
            setTrackingUserId(userSession.userId)
            setFCM()
        }
    }

    private fun setFCM() {
        CMPushNotificationManager.getInstance()
                .refreshFCMTokenFromForeground(userSession.deviceId, true)
    }

    private fun setTrackingUserId(userId: String) {
        TkpdAppsFlyerMapper.getInstance(context).mapAnalytics()
        TrackApp.getInstance().gtm.pushUserId(userId)
        if (!GlobalConfig.DEBUG && Crashlytics.getInstance() != null)
            Crashlytics.setUserIdentifier(userId)

        if (userSession.isLoggedIn) {
            val userData = UserData()
            userData.userId = userSession.userId
            userData.email = userSession.email
            userData.phoneNumber = userSession.phoneNumber

            //Identity Event
            LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData))

            //Login Event
            LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData))
        }

        if (::mIris.isInitialized) {
            mIris.setUserId(userId)
            mIris.setDeviceId(userSession.deviceId)
        }

        TrackApp.getInstance().moEngage.setMoEUserAttributesLogin(
                userSession.userId,
                userSession.name,
                userSession.email,
                userSession.phoneNumber,
                userSession.isGoldMerchant,
                userSession.shopName,
                userSession.shopId,
                userSession.hasShop(),
                LoginRegisterAnalytics.LABEL_EMAIL
        )
    }

    fun onErrorLogin(errorMessage: String?) {
        analytics.eventFailedLogin(userSession.loginMethod)

        dismissLoadingLogin()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun isFromRegister(): Boolean {
        return (activity != null
                && activity!!.intent != null
                && activity!!.intent.getBooleanExtra(IS_FROM_REGISTER, false))
    }

    override fun trackSuccessValidate() {
        analytics.trackClickOnNextSuccess()
    }

    override fun onErrorValidateRegister(throwable: Throwable) {
        analytics.trackClickOnNextFail()
        dismissLoadingLogin()
        val message = ErrorHandlerSession.getErrorMessage(context, throwable)
        partialRegisterInputView.onErrorValidate(message)
    }

    override fun onErrorEmptyEmailPhone() {
        dismissLoadingLogin()
        partialRegisterInputView.onErrorValidate(getString(R.string.must_insert_email_or_phone))
    }

    override fun goToLoginPhoneVerifyPage(phoneNumber: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        analytics.trackLoginPhoneNumber()
        activity?.let {
            val intent = VerificationActivity.getShowChooseVerificationMethodIntent(it,
                    RequestOtpUseCase.OTP_TYPE_LOGIN_PHONE_NUMBER, phoneNumber, "")
            startActivityForResult(intent, REQUEST_LOGIN_PHONE)
        }

    }

    override fun goToRegisterPhoneVerifyPage(phoneNumber: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        activity?.let {
            val intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                    it,
                    RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                    phoneNumber,
                    ""
            )
            startActivityForResult(intent, REQUEST_REGISTER_PHONE)
        }
    }


    override fun onEmailExist(email: String) {
        dismissLoadingLogin()
        partialRegisterInputView.showLoginEmailView(email)
        partialActionButton.setOnClickListener {
            presenter.loginEmail(email, passwordEditText.text.toString())
            activity?.let {
                analytics.eventClickLoginEmailButton(it.applicationContext)
                KeyboardHandler.hideSoftKeyboard(it)
            }
        }
    }

    override fun showNotRegisteredEmailDialog(email: String) {
        dismissLoadingLogin()

        if (activity != null) {
            val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.email_not_registered))
            dialog.setDesc(
                    String.format(resources.getString(
                            R.string.email_not_registered_info), email))
            dialog.setBtnOk(getString(R.string.not_registered_yes))
            dialog.setOnOkClickListener { v ->
                context?.let {
                    dialog.dismiss()
                    startActivity(RegisterEmailActivity.getCallingIntentWithEmail(it, email))
                    activity!!.finish()
                }
            }
            dialog.setBtnCancel(getString(R.string.already_registered_no))
            dialog.setOnCancelClickListener { v ->
                dialog.dismiss()
                onChangeButtonClicked()
                emailPhoneEditText.setText(email)
                emailPhoneEditText.setSelection(emailPhoneEditText.text.length)
            }
            dialog.show()
        }
    }

    override fun resetError() {
        partialRegisterInputView.resetErrorWrapper()
    }

    override fun showErrorPassword(resId: Int) {
        partialRegisterInputView.onErrorPassword(getString(resId))
    }

    override fun showErrorEmail(resId: Int) {
        partialRegisterInputView.onErrorValidate(getString(resId))
    }

    override fun setSmartLock() {
        if (emailPhoneEditText.text.isNotBlank() && passwordEditText.text.isNotBlank()) {
            saveSmartLock(SmartLockActivity.RC_SAVE_SECURITY_QUESTION,
                    emailPhoneEditText.text.toString(),
                    passwordEditText.text.toString())
        }
    }

    private fun saveSmartLock(state: Int, email: String, password: String) {
        val intent = Intent(activity, SmartLockActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(SmartLockActivity.STATE, state)
        if (state == SmartLockActivity.RC_SAVE_SECURITY_QUESTION || state == SmartLockActivity.RC_SAVE) {
            bundle.putString(SmartLockActivity.USERNAME, email)
            bundle.putString(SmartLockActivity.PASSWORD, password)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_SAVE_SMART_LOCK)
    }

    override fun onErrorLoginEmail(email: String): (Throwable) -> Unit {
        return {
            analytics.trackClickOnLoginButtonError()
            stopTrace()

            if (isEmailNotActive(it, email)) {
                onGoToActivationPage(email)
            } else if (it is TokenErrorException && !it.errorDescription.isEmpty()) {
                onErrorLogin(it.errorDescription)
            } else {
                ErrorHandlerSession.getErrorMessage(object : ErrorHandlerSession.ErrorForbiddenListener {
                    override fun onForbidden() {
                        onGoToForbiddenPage()
                    }

                    override fun onError(errorMessage: String) {
                        onErrorLogin(errorMessage)

                        context?.run {
                            if (!TextUtils.isEmpty(it.message)
                                    && errorMessage.contains(this.getString(R.string
                                            .default_request_error_unknown))) {
                                analytics.logUnknownError(it)
                            }
                        }

                    }
                }, it, context)
            }
        }
    }

    override fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    override fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit {
        return {
            //TODO check analytics

            val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

            if (it.profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
                onGoToChangeName()
            } else {
                onSuccessLogin()
            }
        }
    }

    override fun onErrorGetUserInfo(): (Throwable) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, true))

        }
    }

    override fun onGoToCreatePassword(): (String, String) -> Unit {
        return { fullName: String, userId: String ->
            val intent = RouteManager.getIntent(context, ApplinkConst.CREATE_PASSWORD)
            intent.putExtra("name", fullName)
            intent.putExtra("user_id", userId)
            startActivityForResult(intent, REQUESTS_CREATE_PASSWORD)
        }
    }

    override fun onGoToPhoneVerification(): () -> Unit {
        return {
            val intent = RouteManager.getIntent(context, ApplinkConst.PHONE_VERIFICATION)
            startActivityForResult(intent, REQUEST_VERIFY_PHONE)
        }
    }

    override fun onGoToActivationPage(email: String): (MessageErrorException) -> Unit {
        return {
            val intent = ActivationActivity.getCallingIntent(activity,
                    email, passwordEditText.text.toString())
            startActivityForResult(intent, REQUEST_ACTIVATE_ACCOUNT)
        }
    }

    override fun onGoToSecurityQuestion(email: String): () -> Unit {
        return {
            val intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                    activity, RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, "", email)
            startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
        }
    }

    //Flow should not be possible
    override fun onGoToActivationPageAfterRelogin(): (MessageErrorException) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, false))
        }
    }

    //Flow should not be possible
    override fun onGoToSecurityQuestionAfterRelogin(): () -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context))
        }
    }

    override fun onErrorReloginAfterSQ(validateToken: String): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            analytics.eventFailedLogin(userSession.loginMethod)
            NetworkErrorHelper.createSnackbarWithAction(activity,
                    ErrorHandlerSession.getErrorMessage(it, context, true)) {
                presenter.reloginAfterSQ(validateToken)
            }
        }
    }

    override fun onErrorLoginFacebook(email: String): (Throwable) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, true))
        }
    }

    override fun onErrorLoginGoogle(email: String?): (Throwable) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, true))
        }
    }

    protected fun isEmailNotActive(e: Throwable, email: String): Boolean {
        val NOT_ACTIVATED = "belum diaktivasi"
        return (e is TokenErrorException
                && !e.errorDescription.isEmpty()
                && e.errorDescription
                .toLowerCase().contains(NOT_ACTIVATED)
                && !TextUtils.isEmpty(email))
    }

    private fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        if (activity != null && activity!!.applicationContext != null) {
            val intent = RouteManager.getIntent(activity,
                    ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_SMART_LOCK
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null
                    && data.extras!!.getString(SmartLockActivity.USERNAME) != null
                    && data.extras!!.getString(SmartLockActivity.PASSWORD) != null) run {
                emailPhoneEditText.setText(data.extras!!.getString(SmartLockActivity.USERNAME))
                emailPhoneEditText.setSelection(emailPhoneEditText.text.length)
                presenter.loginEmail(data.extras!!.getString(SmartLockActivity.USERNAME, ""),
                        data.extras!!.getString(SmartLockActivity.PASSWORD, ""))
                activity?.let {
                    analytics.eventClickLoginEmailButton(it.applicationContext)
                }
            } else if (requestCode == REQUEST_SMART_LOCK
                    && resultCode == SmartLockActivity.RC_READ
                    && !userSession.autofillUserData.isNullOrEmpty()) {
                emailPhoneEditText.setText(userSession.autofillUserData)
                emailPhoneEditText.setSelection(emailPhoneEditText.text.length)
            } else if (requestCode == REQUEST_LOGIN_GOOGLE && data != null) run {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            } else if (requestCode == REQUEST_SECURITY_QUESTION
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null
                    && data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID) != null) {
                val validateToken = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                presenter.reloginAfterSQ(validateToken)
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin()
                activity!!.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
                analytics.eventFailedLogin(userSession.loginMethod)
                dismissLoadingLogin()
                activity!!.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_CANCELED) {
                analytics.eventFailedLogin(userSession.loginMethod)
                dismissLoadingLogin()
                activity!!.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_VERIFY_PHONE) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_REGISTER_PHONE
                    && resultCode == Activity.RESULT_OK && data != null
                    && data.extras != null) {
                val uuid = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                val msisdn = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                goToAddNameFromRegisterPhone(uuid, msisdn)
            } else if (requestCode == REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                startActivityForResult(WelcomePageActivity.newInstance(activity),
                        REQUEST_WELCOME_PAGE)
            } else if (requestCode == REQUEST_WELCOME_PAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    goToProfileCompletionPage()
                } else {
                    onSuccessLogin()
                }
            } else if (requestCode == REQUEST_LOGIN_PHONE
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null) {
                data.extras?.run {
                    val accessToken = getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                    val phoneNumber = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                    goToChooseAccountPage(accessToken, phoneNumber)
                }
            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_LOGIN_PHONE
                    || requestCode == REQUEST_CHOOSE_ACCOUNT) {
                analytics.trackLoginPhoneNumberFailed()
                dismissLoadingLogin()
            } else {
                dismissLoadingLogin()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun goToAddNameFromRegisterPhone(uuid: String, msisdn: String) {
        val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER
        val intent = RouteManager.getIntent(context, applink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, msisdn)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
        startActivityForResult(intent, REQUEST_ADD_NAME_REGISTER_PHONE)
    }

    /**
     * Please refer to the
     * [class reference for][com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes]
     */
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null && account.idToken != null) {
                val accessToken: String = account.idToken!!
                val email = account.email
                if (email != null) {
                    presenter.loginGoogle(accessToken, email)
                } else {
                    onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.EMPTY_EMAIL, context))
                }
            } else {
                onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.EMPTY_ACCESS_TOKEN, context))
            }
        } catch (e: ApiException) {
            onErrorLogin(String.format(getString(R.string.loginregister_failed_login_google),
                    e.statusCode.toString())
            )
        }

    }

    private fun goToProfileCompletionPage() {
        if (activity != null) {
            RouteManager.route(context, ApplinkConst.PROFILE_COMPLETION)
        }
    }

    private fun onGoToChangeName() {
        if (activity != null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.ADD_NAME_PROFILE)
            startActivityForResult(intent, REQUEST_ADD_NAME)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onBackPressed() {
        analytics.trackOnBackPressed()
        if (partialRegisterInputView.findViewById<TextView>(R.id.change_button).visibility ==
                View.VISIBLE) {
            val email = emailPhoneEditText.text.toString()
            onChangeButtonClicked()
            emailPhoneEditText.setText(email)
            emailPhoneEditText.setSelection(emailPhoneEditText.text.length)
        } else if (activity != null) {
            activity?.finish()
        }
    }

}