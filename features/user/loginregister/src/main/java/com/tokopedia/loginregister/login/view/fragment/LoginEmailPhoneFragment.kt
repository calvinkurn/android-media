package com.tokopedia.loginregister.login.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.loginregister.LoginRegisterPhoneRouter
import com.tokopedia.loginregister.LoginRegisterRouter
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.R.id.register_button
import com.tokopedia.loginregister.activation.view.activity.ActivationActivity
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.login.di.DaggerLoginComponent
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.presenter.LoginEmailPhonePresenter
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.google.GoogleSignInActivity
import com.tokopedia.loginregister.loginthirdparty.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT
import com.tokopedia.loginregister.loginthirdparty.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN
import com.tokopedia.loginregister.loginthirdparty.google.SmartLockActivity
import com.tokopedia.loginregister.registeremail.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel
import com.tokopedia.sessioncommon.data.model.GetUserInfoData
import com.tokopedia.sessioncommon.data.model.SecurityPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.view.LoginSuccessRouter
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
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
    val RC_SIGN_IN_GOOGLE = 7777

    private val REQUEST_SMART_LOCK = 101
    private val REQUEST_SAVE_SMART_LOCK = 102
    private val REQUEST_SECURITY_QUESTION = 104
    private val REQUESTS_CREATE_PASSWORD = 106
    private val REQUEST_ACTIVATE_ACCOUNT = 107
    private val REQUEST_VERIFY_PHONE = 108
    private val REQUEST_ADD_NAME = 109
    private val REQUEST_CHOOSE_ACCOUNT = 110
    private val REQUEST_NO_TOKOCASH_ACCOUNT = 111

    val IS_AUTO_LOGIN = "auto_login"
    val AUTO_LOGIN_METHOD = "method"

    val AUTO_LOGIN_EMAIL = "email"
    val AUTO_LOGIN_PASS = "pw"

    val IS_AUTO_FILL = "auto_fill"
    val AUTO_FILL_EMAIL = "email"
    val IS_FROM_REGISTER = "is_from_register"

    val FACEBOOK = "facebook"
    val GPLUS = "gplus"
    val PHONE_NUMBER = "tokocash"

    private lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var presenter: LoginEmailPhonePresenter

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    //*For analytics
    private var actionLoginMethod: String = ""

    private lateinit var partialRegisterInputView: PartialRegisterInputView
    private lateinit var loginLayout: LinearLayout
    private lateinit var loginButtonsContainer: LinearLayout
    private lateinit var emailPhoneEditText: EditText
    private lateinit var partialActionButton: TextView
    private lateinit var passwordEditText: TextInputEditText

    companion object {
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
        analytics.trackScreen(activity, screenName)
    }

    override fun onResume() {
        super.onResume()
        if (userSession.isLoggedIn && activity != null) {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()
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
            drawable.setTextColor(resources.getColor(R.color.tkpd_main_green))
        }
        return drawable
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == ID_ACTION_REGISTER) {
            analytics.trackClickRegisterOnMenu()

            goToRegisterInitial()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
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
        } else if (arguments!!.getBoolean(IS_AUTO_LOGIN, false)) {
            when (arguments!!.getInt(AUTO_LOGIN_METHOD)) {
                LoginActivity.METHOD_FACEBOOK -> onLoginFacebookClick()
                LoginActivity.METHOD_GOOGLE -> onLoginGoogleClick()

                LoginActivity.METHOD_EMAIL -> {
                    actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_EMAIL
                    val email = arguments!!.getString(AUTO_LOGIN_EMAIL, "")
                    val pw = arguments!!.getString(AUTO_LOGIN_PASS, "")
                    partialRegisterInputView.showLoginEmailView(email)
                    emailPhoneEditText.setText(email)
                    passwordEditText.setText(pw)
                    presenter.login(email, pw)
                }
                else -> showSmartLock()
            }
        } else {
            showSmartLock()
        }

        passwordEditText.set
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
        if (loginLayout.getChildAt(lastPos) !is ProgressBar) {
            loginLayout.addView(pb, loginLayout.childCount - 1)
        }
    }

    override fun dismissLoadingDiscover() {
        val lastPos = loginLayout.childCount - 2
        if (loginLayout.getChildAt(lastPos) is ProgressBar) {
            loginLayout.removeViewAt(loginLayout.childCount - 2)
        }
    }

    private fun prepareView() {
        emailPhoneEditText.setOnEditorActionListener { textView, id, keyEvent ->
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
                actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_EMAIL
                presenter.login(emailPhoneEditText.text.toString().trim(),
                        passwordEditText.text.toString())
                KeyboardHandler.hideSoftKeyboard(activity)
                true
            } else {
                false
            }
        }

        partialRegisterInputView.findViewById<TextView>(R.id.change_button).setOnClickListener { it ->
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
                analytics.trackClickRegisterOnFooter()
                goToRegisterInitial()
            }

            val forgotPassword = partialRegisterInputView.findViewById<TextView>(R.id.forgot_pass)
            forgotPassword.setOnClickListener {
                analytics.trackClickForgotPassword()
                goToForgotPassword() }
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
        if (activity != null && activity!!.applicationContext is LoginRegisterRouter) {
            val intent = (activity!!.applicationContext as LoginRegisterRouter)
                    .getForgotPasswordIntent(activity, emailPhoneEditText.text.toString().trim())
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            startActivity(intent)
            analytics.eventClickForgotPasswordFromLogin(activity!!.applicationContext)
        }
    }

    override fun onSuccessDiscoverLogin(listProvider: ArrayList<DiscoverItemViewModel>) {
        val COLOR_WHITE = "#FFFFFF"

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 20, 0, 15)
        loginButtonsContainer.removeAllViews()
        for (i in listProvider.indices) {
            val colorInt = Color.parseColor(COLOR_WHITE)
            val tv = LoginTextView(activity, colorInt)
            tv.tag = listProvider[i].id
            tv.setText(listProvider[i].name)
            if (!TextUtils.isEmpty(listProvider[i].image)) {
                tv.setImage(listProvider[i].image)
            } else if (listProvider[i].imageResource != 0) {
                tv.setImageResource(listProvider[i].imageResource)
            }
            tv.setRoundCorner(10)

            setDiscoverListener(listProvider[i], tv)
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
            actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_GOOGLE

            analytics.eventClickLoginGoogle(activity!!.applicationContext)
            val intent = Intent(activity, GoogleSignInActivity::class.java)
            startActivityForResult(intent, RC_SIGN_IN_GOOGLE)
        }

    }

    private fun onLoginFacebookClick() {
        if (activity != null) {
            actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_FACEBOOK

            analytics.eventClickLoginFacebook(activity!!.applicationContext)
            presenter.getFacebookCredential(this, callbackManager)
        }
    }

    override fun getFacebookCredentialListener(): GetFacebookCredentialSubscriber.GetFacebookCredentialListener {
        return object : GetFacebookCredentialSubscriber.GetFacebookCredentialListener {

            override fun onErrorGetFacebookCredential(e: Exception) {
                if (isAdded && activity != null) {
                    NetworkErrorHelper.showSnackbar(activity, ErrorHandler.getErrorMessage(context, e))
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

    override fun getLoginRouter(): LoginSuccessRouter {
        return object : LoginSuccessRouter {
            override fun onGoToActivationPage(email: String) {
                val intent = ActivationActivity.getCallingIntent(activity,
                        email, passwordEditText.text.toString())
                startActivityForResult(intent, REQUEST_ACTIVATE_ACCOUNT)
            }

            override fun onForbidden() {
                ForbiddenActivity.startActivity(activity)
            }

            override fun onErrorLogin(errorMessage: String) {
                dismissLoadingLogin()
                NetworkErrorHelper.showSnackbar(activity, errorMessage)
            }

            override fun onGoToCreatePasswordPage(info: GetUserInfoData) {
                if (activity != null) {
                    val intent = (activity!!.applicationContext as ApplinkRouter).getApplinkIntent(activity, ApplinkConst.CREATE_PASSWORD)
                    intent.putExtra("name", info.fullName)
                    intent.putExtra("user_id", info.userId.toString())
                    startActivityForResult(intent, REQUESTS_CREATE_PASSWORD)
                }
            }

            override fun onGoToPhoneVerification() {
                if (activity != null) {
                    val intent = (activity!!.applicationContext as ApplinkRouter)
                            .getApplinkIntent(activity, ApplinkConst.PHONE_VERIFICATION)
                    startActivityForResult(intent, REQUEST_VERIFY_PHONE)
                }
            }

            override fun onGoToSecurityQuestion(securityPojo: SecurityPojo, fullName: String, email: String, phone: String) {
                val intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                        activity, RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, phone, email)
                startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
            }

            override fun logUnknownError(message: Throwable) {
                try {
                    Crashlytics.logException(message)
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onSuccessLogin() {
        if (activity != null) {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()

            analytics.eventSuccessLogin(actionLoginMethod)
            (activity!!.applicationContext as LoginRegisterRouter).setTrackingUserId(userSession.userId, activity!!.applicationContext)
            (activity!!.applicationContext as LoginRegisterRouter).onLoginSuccess()
        }
    }

    override fun onErrorLogin(errorMessage: String?) {
        if (!TextUtils.isEmpty(actionLoginMethod)) {
            analytics.eventFailedLogin(actionLoginMethod)
        }

        dismissLoadingLogin()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessLoginSosmed(loginMethod: String?) {
        dismissLoadingLogin()

        analytics.eventSuccessLoginSosmed(loginMethod)
        if (activity != null) {
            (activity!!.applicationContext as LoginRegisterRouter).setMoEUserAttributesLogin(userSession.userId,
                    userSession.name,
                    userSession.email,
                    userSession.phoneNumber,
                    userSession.isGoldMerchant,
                    userSession.shopName,
                    userSession.shopId,
                    userSession.hasShop(),
                    loginMethod
            )
        }
        onSuccessLogin()
    }

    private fun onSuccessLoginPhoneNumber() {
        actionLoginMethod = "phone"
        dismissLoadingLogin()

        analytics.eventSuccessLogin(actionLoginMethod)
        if (activity != null) {
            (activity!!.applicationContext as LoginRegisterRouter).setMoEUserAttributesLogin(userSession.userId,
                    userSession.name,
                    userSession.email,
                    userSession.phoneNumber,
                    userSession.isGoldMerchant,
                    userSession.shopName,
                    userSession.shopId,
                    userSession.hasShop(),
                    actionLoginMethod
            )
        }
        onSuccessLogin()
    }

    override fun onErrorLoginSosmed(loginMethodName: String, errorMessage: String) {
        onErrorLogin(errorMessage)
    }

    override fun isFromRegister(): Boolean {
        return (activity != null
                && activity!!.intent != null
                && activity!!.intent.getBooleanExtra(IS_FROM_REGISTER, false))
    }

    override fun onErrorValidateRegister(throwable: Throwable) {
        dismissLoadingLogin()
        val message = ErrorHandlerSession.getErrorMessage(context, throwable)
        partialRegisterInputView.onErrorValidate(message)
    }

    override fun onErrorEmptyEmailPhone() {
        dismissLoadingLogin()
        partialRegisterInputView.onErrorValidate(getString(R.string.must_insert_email_or_phone))
    }

    override fun goToLoginPhoneVerifyPage(phoneNumber: String) {
        activity?.let {
            val intent = (it.applicationContext as LoginRegisterPhoneRouter).getTokoCashOtpIntent(
                    it, phoneNumber, true, RequestOtpUseCase.MODE_SMS
            )
            startActivityForResult(intent, REQUEST_VERIFY_PHONE)
        }
    }

    override fun goToRegisterPhoneVerifyPage(phoneNumber: String) {

        activity?.let {
            val intent = VerificationActivity.getCallingIntent(
                    it,
                    phoneNumber,
                    RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                    true,
                    RequestOtpUseCase.MODE_SMS
            )
            startActivityForResult(intent, REQUEST_VERIFY_PHONE)
        }
    }


    override fun onEmailExist(email: String) {
        dismissLoadingLogin()
        partialRegisterInputView.showLoginEmailView(email)
        partialActionButton.setOnClickListener {
            analytics.trackClickOnLoginButton()
            KeyboardHandler.hideSoftKeyboard(activity)
            presenter.login(email, passwordEditText.text.toString())
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

    override fun onGoToAddName() {
        if (activity != null) {
            val intent = (activity!!.applicationContext as ApplinkRouter)
                    .getApplinkIntent(activity, ApplinkConst.ADD_NAME_PROFILE)
            startActivityForResult(intent, REQUEST_ADD_NAME)
        }
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

    override fun onSuccessLoginEmail() {
        dismissLoadingLogin()
        analytics.eventSuccessLoginEmail()
        if (activity != null) {
            (activity!!.applicationContext as LoginRegisterRouter).setMoEUserAttributesLogin(userSession.userId,
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

        onSuccessLogin()
    }

    private fun goToNoTokocashAccountPage(phoneNumber: String) {
        if (activity != null && activity!!.applicationContext != null) {
            val intent = (activity!!.applicationContext as LoginRegisterPhoneRouter)
                    .getNoTokocashAccountIntent(
                            activity!!,
                            phoneNumber)
            startActivityForResult(intent, REQUEST_NO_TOKOCASH_ACCOUNT)
        }
    }

    private fun goToChooseAccountPage(data: ChooseTokoCashAccountViewModel) {
        if (activity != null && activity!!.applicationContext != null) {

            val intent = (activity!!.applicationContext as LoginRegisterPhoneRouter)
                    .getChooseTokocashAccountIntent(activity!!, data)

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
                presenter.login(data.extras!!.getString(SmartLockActivity.USERNAME),
                        data.extras!!.getString(SmartLockActivity.PASSWORD))
            } else if (requestCode == RC_SIGN_IN_GOOGLE && data != null) run {
                val googleSignInAccount = data.getParcelableExtra<GoogleSignInAccount>(KEY_GOOGLE_ACCOUNT)
                val email = googleSignInAccount.email
                val accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN)
                presenter.loginGoogle(accessToken, email)
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin()
                activity!!.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin()
                activity!!.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin()
                activity!!.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_VERIFY_PHONE
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.getParcelableExtra<Parcelable>(ChooseTokoCashAccountViewModel.ARGS_DATA) != null) {
                val chooseTokoCashAccountViewModel = data.getParcelableExtra<ChooseTokoCashAccountViewModel>(ChooseTokoCashAccountViewModel.ARGS_DATA)
                if (!chooseTokoCashAccountViewModel.listAccount.isEmpty()) {
                    goToChooseAccountPage(chooseTokoCashAccountViewModel)
                } else {
                    val phoneNumber = emailPhoneEditText.text.toString()
                    goToNoTokocashAccountPage(phoneNumber)
                }
            } else if (requestCode == REQUEST_VERIFY_PHONE && resultCode ==
                    LoginRegisterPhoneRouter.RESULT_SUCCESS_AUTO_LOGIN) run {
                onSuccessLoginPhoneNumber()
            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                onSuccessLoginPhoneNumber()
            } else {
                dismissLoadingLogin()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


    override fun setAutoCompleteAdapter(listId: ArrayList<String>?) {
        //NOT IMPLEMENTED
    }

    override fun disableArrow() {
        //NOT IMPLEMENTED
    }

    override fun enableArrow() {
        //NOT IMPLEMENTED
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