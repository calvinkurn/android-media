package com.tokopedia.loginregister.registerinitial.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.*
import android.widget.*
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
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.registeremail.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract
import com.tokopedia.loginregister.registerinitial.view.presenter.RegisterInitialPresenter
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.welcomepage.WelcomePageActivity
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.Token.Companion.GOOGLE_API_KEY
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 10/24/18.
 */
class RegisterInitialFragment : BaseDaggerFragment(), RegisterInitialContract.View,
        PartialRegisterInputView.PartialRegisterInputViewListener {

    companion object {

        private val ID_ACTION_LOGIN = 112

        private val REQUEST_REGISTER_EMAIL = 101
        private val REQUEST_CREATE_PASSWORD = 102
        private val REQUEST_SECURITY_QUESTION = 103
        private val REQUEST_VERIFY_PHONE_REGISTER_PHONE = 105
        private val REQUEST_WELCOME_PAGE = 106
        private val REQUEST_ADD_NAME_REGISTER_PHONE = 107
        private val REQUEST_VERIFY_PHONE_TOKOCASH = 108
        private val REQUEST_CHOOSE_ACCOUNT = 109
        private val REQUEST_CHANGE_NAME = 111
        private val REQUEST_LOGIN_GOOGLE = 112

        private val FACEBOOK = "facebook"
        private val GPLUS = "gplus"
        private val PHONE_NUMBER = "phonenumber"

        fun createInstance(): RegisterInitialFragment {
            return RegisterInitialFragment()
        }
    }

    lateinit var optionTitle: TextView
    lateinit var partialRegisterInputView: PartialRegisterInputView
    lateinit var registerContainer: LinearLayout
    lateinit var registerButton: LoginTextView
    lateinit var loginButton: TextView
    lateinit var container: ScrollView
    lateinit var progressBar: RelativeLayout
    lateinit var tickerAnnouncement: Ticker

    private var phoneNumber: String? = ""

    @Inject
    lateinit var presenter: RegisterInitialPresenter

    @field:Named(SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    lateinit var callbackManager: CallbackManager
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val draw: Drawable?
        get() {
            var drawable: TextDrawable? = null
            if (activity != null) {
                drawable = TextDrawable(activity!!)
                drawable.text = resources.getString(R.string.login)
                drawable.setTextColor(resources.getColor(R.color.tkpd_main_green))
                drawable.textSize = 14f
            }
            return drawable
        }

    override val facebookCredentialListener: GetFacebookCredentialSubscriber.GetFacebookCredentialListener
        get() = object : GetFacebookCredentialSubscriber.GetFacebookCredentialListener {

            override fun onErrorGetFacebookCredential(e: Exception) {
                if (isAdded && activity != null) {
                    e.message?.let { onErrorRegister(ErrorHandler.getErrorMessage(context, e)) }
                }
            }

            override fun onSuccessGetFacebookCredential(accessToken: AccessToken, email: String) {
                try {
                    presenter.registerFacebook(accessToken, email)
                } catch (e: Exception) {
                    e.message?.let { onErrorRegister(it) }
                }
            }
        }


    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.trackScreen(this, screenName)
        }
    }

    override fun initInjector() {
        val daggerLoginComponent = DaggerRegisterInitialComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent::class.java))
                .build() as DaggerRegisterInitialComponent

        daggerLoginComponent.inject(this)
    }

    override fun getScreenName(): String {
        return RegisterAnalytics.SCREEN_REGISTER_INITIAL
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

        if (savedInstanceState != null && savedInstanceState.containsKey(PHONE_NUMBER)) {
            phoneNumber = savedInstanceState.getString(PHONE_NUMBER)
        }
    }


    override fun onResume() {
        super.onResume()

        activity?.run {
            if (userSession.isLoggedIn && activity != null) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_initial_register, parent, false)
        optionTitle = view.findViewById(R.id.register_option_title)
        partialRegisterInputView = view.findViewById(R.id
                .register_input_view)
        registerContainer = view.findViewById(R.id.register_container)
        registerButton = view.findViewById(R.id.register)
        loginButton = view.findViewById(R.id.login_button)
        container = view.findViewById(R.id.container)
        progressBar = view.findViewById(R.id.progress_bar)
        tickerAnnouncement = view.findViewById(R.id.ticker_announcement)
        prepareView()
        setViewListener()
        presenter.attachView(this)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.let {
            it.add(Menu.NONE, ID_ACTION_LOGIN, 0, "")
            val menuItem = it.findItem(ID_ACTION_LOGIN)
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            if (draw != null) {
                menuItem.icon = draw
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            activity?.run {
                val id = it.itemId
                if (id == ID_ACTION_LOGIN) {
                    if (activity != null) {
                        registerAnalytics.trackClickTopSignInButton()
                        finish()
                        goToLoginPage()
                    }
                    return true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        presenter.getProvider()
        partialRegisterInputView.setListener(this)
        presenter.getTickerInfo()
    }

    protected fun prepareView() {
        activity?.run {
            registerButton.visibility = View.GONE
            partialRegisterInputView.visibility = View.GONE

            if (!GlobalConfig.isSellerApp()) {
                optionTitle.setText(R.string.register_option_title)
                optionTitle.typeface = Typeface.DEFAULT
                optionTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            }

            registerButton.setColor(Color.WHITE)
            registerButton.setBorderColor(MethodChecker.getColor(activity, R.color.black_38))
            registerButton.setRoundCorner(10)
            registerButton.setImageResource(R.drawable.ic_email)
            registerButton.setOnClickListener { v ->
                TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_EMAIL)
                goToRegisterEmailPage()

            }

            if (GlobalConfig.isSellerApp()) {
                registerButton.visibility = View.VISIBLE
            } else {
                partialRegisterInputView.visibility = View.VISIBLE
            }

            val sourceString = resources.getString(R.string
                    .span_already_have_tokopedia_account)

            val spannable = SpannableString(sourceString)

            spannable.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {}

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                            activity, R.color.tkpd_main_green
                    )
                    ds.typeface = Typeface.create("sans-serif-medium", Typeface
                            .NORMAL)
                }
            }, sourceString.indexOf("Masuk"), sourceString.length, 0)

            loginButton.setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    protected fun setViewListener() {
        loginButton.setOnClickListener { v ->
            registerAnalytics.trackClickBottomSignInButton()
            activity?.run {
                finish()
                analytics.eventClickOnLoginFromRegister()
                goToLoginPage()
            }
        }
    }

    override fun goToLoginPage() {
        activity?.let {
            val intent = LoginActivity.DeepLinkIntents.getCallingIntent(it)
            startActivity(intent)
        }
    }

    private fun goToRegisterEmailPage() {
        showProgressBar()
        val intent = RegisterEmailActivity.getCallingIntent(activity)
        startActivityForResult(intent, REQUEST_REGISTER_EMAIL)
    }

    override fun goToRegisterEmailPageWithEmail(email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL

        activity?.let {
            registerAnalytics.trackClickEmailSignUpButton()
            showProgressBar()
            val intent = RegisterEmailActivity.getCallingIntentWithEmail(it, email)
            startActivityForResult(intent, REQUEST_REGISTER_EMAIL)
        }
    }

    private fun goToVerificationPhoneRegister(phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        val intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                activity,
                RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                phone,
                ""
        )
        startActivityForResult(intent, REQUEST_VERIFY_PHONE_REGISTER_PHONE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        activity?.let {
            if (requestCode == REQUEST_LOGIN_GOOGLE && data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task != null) {
                    handleGoogleSignInResult(task)
                }
            } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity.RESULT_OK) {
                presenter.getUserInfo()
            } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity
                            .RESULT_CANCELED) {
                dismissProgressBar()
                it.setResult(Activity.RESULT_CANCELED)
                userSession.clearToken()
            } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity
                            .RESULT_CANCELED) {
                dismissProgressBar()
                it.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity
                            .RESULT_CANCELED) {
                dismissProgressBar()
                it.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_VERIFY_PHONE_REGISTER_PHONE
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null) {
                val uuid = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                goToAddName(uuid)
            } else if (requestCode == REQUEST_VERIFY_PHONE_REGISTER_PHONE && resultCode == Activity
                            .RESULT_CANCELED) {
                dismissProgressBar()
                it.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                presenter.getUserInfo(false)
            } else if (requestCode == REQUEST_WELCOME_PAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    goToProfileCompletionPage()
                }
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else if (requestCode == REQUEST_VERIFY_PHONE_TOKOCASH
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null) {
                val accessToken = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                val phoneNumber = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                goToChooseAccountPage(accessToken, phoneNumber)

            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else if (requestCode == REQUEST_CHANGE_NAME && resultCode == Activity.RESULT_OK) {
                presenter.getUserInfo()
            } else if (requestCode == REQUEST_CHANGE_NAME && resultCode == Activity.RESULT_CANCELED) {
                userSession.logoutSession()
                dismissProgressBar()
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    /**
     * Please refer to the
     * [class reference for][com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes]
     */
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        if (getContext() != null) {
            try {
                val account = completedTask.getResult(ApiException::class.java)
                val accessToken = account?.idToken ?: ""
                val email = account?.email ?: ""
                presenter.registerGoogle(accessToken, email)
            } catch (e: NullPointerException) {
                onErrorRegister(ErrorHandlerSession.getDefaultErrorCodeMessage(
                        ErrorHandlerSession.ErrorCode.GOOGLE_FAILED_ACCESS_TOKEN,
                        context))
            } catch (e: ApiException) {
                onErrorRegister(String.format(getString(R.string.loginregister_failed_login_google),
                        e.statusCode.toString()))
            }
        }
    }


    private fun goToAddName(uuid: String) {
        if (activity != null) {
            val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER
            val intent = RouteManager.getIntent(getContext(), applink)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phoneNumber)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
            startActivityForResult(intent, REQUEST_ADD_NAME_REGISTER_PHONE)
        }
    }

    private fun goToProfileCompletionPage() {
        activity?.let {
            (it.applicationContext as ApplinkRouter).goToApplinkActivity(activity, ApplinkConst.PROFILE_COMPLETION)
        }
    }

    override fun onActionPartialClick(id: String) {
        registerAnalytics.trackClickSignUpButton()
        presenter.validateRegister(id)
    }

    override fun showLoadingDiscover() {
        val pb = ProgressBar(activity, null, android.R.attr.progressBarStyle)
        val lastPos = registerContainer.childCount - 1
        if (registerContainer.getChildAt(lastPos) !is ProgressBar) {
            registerContainer.addView(pb, registerContainer.childCount)
        }
    }

    override fun onErrorDiscoverRegister(e: Throwable) {

        ErrorHandlerSession.getErrorMessage(object : ErrorHandlerSession.ErrorForbiddenListener {
            override fun onForbidden() {
                onGoToForbiddenPage()
            }

            override fun onError(errorMessage: String) {
                NetworkErrorHelper.createSnackbarWithAction(activity,
                        errorMessage) { presenter.getProvider() }.showRetrySnackbar()
                loginButton.isEnabled = false
            }
        }, e, context)

    }

    override fun onSuccessDiscoverRegister(listProvider: ArrayList<DiscoverItemViewModel>) {

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.dp_52))
        val topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
                resources.displayMetrics).toInt()

        layoutParams.setMargins(0, topMargin, 0, 0)

        for (i in listProvider.indices) {
            val item = listProvider[i]
            if (item.id != PHONE_NUMBER) {
                val loginTextView = LoginTextView(activity, MethodChecker.getColor(activity, R.color.white))
                loginTextView.setText(item.name)
                loginTextView.setBorderColor(MethodChecker.getColor(activity, R.color
                        .black_38))
                loginTextView.setImage(item.image)
                loginTextView.setRoundCorner(10)

                setDiscoverOnClickListener(item, loginTextView)

                registerContainer.addView(loginTextView, registerContainer.childCount,
                        layoutParams)
            }
        }

    }

    private fun setDiscoverOnClickListener(discoverItemViewModel: DiscoverItemViewModel,
                                           loginTextView: LoginTextView) {

        when (discoverItemViewModel.id.toLowerCase()) {
            FACEBOOK -> loginTextView.setOnClickListener { v -> onRegisterFacebookClick() }
            GPLUS -> loginTextView.setOnClickListener { v -> onRegisterGooglelick() }
        }
    }

    private fun onRegisterFacebookClick() {
        activity?.let {
            registerAnalytics.trackClickFacebookButton(it.applicationContext)
            TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_FACEBOOK)
            presenter.getFacebookCredential(this, callbackManager)
        }

    }

    private fun onRegisterGooglelick() {
        activity?.let {
            registerAnalytics.trackClickGoogleButton(it.applicationContext)
            TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_GMAIL)
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, REQUEST_LOGIN_GOOGLE)
        }

    }

    override fun dismissLoadingDiscover() {
        val lastPos = registerContainer.childCount - 1
        if (registerContainer.getChildAt(lastPos) is ProgressBar) {
            registerContainer.removeViewAt(registerContainer.childCount - 1)
        }
    }

    override fun showProgressBar() {

        progressBar.visibility = View.VISIBLE
        container.visibility = View.GONE
        loginButton.visibility = View.GONE

    }

    override fun dismissProgressBar() {

        progressBar.visibility = View.GONE

        container.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE
    }

    fun onErrorRegister(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
        registerAnalytics.trackErrorRegister(errorMessage, userSession.loginMethod)
    }

    override fun showRegisteredEmailDialog(email: String) {
        registerAnalytics.trackClickEmailSignUpButton()
        registerAnalytics.trackFailedClickEmailSignUpButton(RegisterAnalytics.LABEL_EMAIL_EXIST)
        activity?.let {
            val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.email_already_registered))
            dialog.setDesc(
                    String.format(resources.getString(
                            R.string.email_already_registered_info), email))
            dialog.setBtnOk(getString(R.string.already_registered_yes))
            dialog.setOnOkClickListener { v ->
                registerAnalytics.trackClickYesButtonRegisteredEmailDialog()
                dialog.dismiss()
                startActivity(LoginActivity.DeepLinkIntents.getIntentLoginFromRegister(it, email))
                it.finish()
            }
            dialog.setBtnCancel(getString(R.string.already_registered_no))
            dialog.setOnCancelClickListener { v ->
                registerAnalytics.trackClickChangeButtonRegisteredEmailDialog()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun showRegisteredPhoneDialog(phone: String) {
        registerAnalytics.trackClickPhoneSignUpButton()
        registerAnalytics.trackFailedClickPhoneSignUpButton(RegisterAnalytics.LABEL_PHONE_EXIST)
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.phone_number_already_registered))
        dialog.setDesc(
                String.format(resources.getString(
                        R.string.reigster_page_phone_number_already_registered_info), phone))
        dialog.setBtnOk(getString(R.string.already_registered_yes))
        dialog.setOnOkClickListener { v ->
            registerAnalytics.trackClickYesButtonRegisteredPhoneDialog()
            dialog.dismiss()
            phoneNumber = phone
            goToVerifyAccountPage(phoneNumber)
        }
        dialog.setBtnCancel(getString(R.string.already_registered_no))
        dialog.setOnCancelClickListener { v ->
            registerAnalytics.trackClickChangeButtonRegisteredPhoneDialog()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun goToVerifyAccountPage(phoneNumber: String?) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE

        activity?.let {
            val intent = VerificationActivity.getShowChooseVerificationMethodIntent(it,
                    RequestOtpUseCase.OTP_TYPE_LOGIN_PHONE_NUMBER, phoneNumber, "")
            startActivityForResult(intent, REQUEST_VERIFY_PHONE_TOKOCASH)
        }
    }

    private fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)

            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun getChooseAccountData(data: Intent): ChooseTokoCashAccountViewModel {
        return data.getParcelableExtra(ChooseTokoCashAccountViewModel.ARGS_DATA)
    }


    override fun showProceedWithPhoneDialog(phone: String) {
        registerAnalytics.trackClickPhoneSignUpButton()
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(phone)
        dialog.setDesc(resources.getString(R.string.phone_number_not_registered_info))
        dialog.setBtnOk(getString(R.string.proceed_with_phone_number))
        dialog.setOnOkClickListener { v ->
            registerAnalytics.trackClickYesButtonPhoneDialog()
            dialog.dismiss()
            goToVerificationPhoneRegister(phone)
        }
        dialog.setBtnCancel(getString(R.string.already_registered_no))
        dialog.setOnCancelClickListener { v ->
            registerAnalytics.trackClickChangeButtonPhoneDialog()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onErrorValidateRegister(message: Throwable) {
        val messageError = ErrorHandlerSession.getErrorMessage(context, message)
        registerAnalytics.trackFailedClickSignUpButton(messageError)
        partialRegisterInputView.onErrorValidate(messageError)
        phoneNumber = ""
    }

    override fun setTempPhoneNumber(maskedPhoneNumber: String) {
        //use masked phone number form backend when needed
        //we need unmasked phone number (without dash) to be provided to backend
        this.phoneNumber = partialRegisterInputView.textValue
    }

    override fun onErrorLoginFacebook(email: String): (e: Throwable) -> Unit {
        return {
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, it)
            onErrorRegister(errorMessage)
        }
    }

    override fun onErrorLoginGoogle(email: String): (e: Throwable) -> Unit {
        return {
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, it)
            onErrorRegister(errorMessage)
        }
    }

    //Wrong flow implementation
    override fun onGoToActivationPage(email: String): (errorMessage: MessageErrorException) -> Unit {
        return {
            NetworkErrorHelper.showSnackbar(activity, ErrorHandlerSession.getErrorMessage(context, it))
        }
    }

    override fun onGoToSecurityQuestion(email: String): () -> Unit {
        return {
            val intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                    activity, RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, "", email)
            startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
        }
    }

    override fun onSuccessGetUserInfo(): (pojo: ProfilePojo) -> Unit {
        return {

            val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

            if (it.profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
                onGoToChangeName()
            } else {
                onSuccessRegister()
            }
        }
    }

    private fun onSuccessRegister() {
        registerAnalytics.trackSuccessRegister(userSession.loginMethod)
        startActivityForResult(WelcomePageActivity.newInstance(activity),
                REQUEST_WELCOME_PAGE)
    }

    override fun onGoToChangeName() {
        activity?.let {
            val intent = (it.applicationContext as ApplinkRouter).getApplinkIntent(activity, ApplinkConst.ADD_NAME_PROFILE)
            startActivityForResult(intent, REQUEST_CHANGE_NAME)
        }
    }

    override fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    override fun onErrorGetUserInfo(): (e: Throwable) -> Unit {
        return {
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, it)
            onErrorRegister(errorMessage)
        }
    }

    override fun onGoToCreatePassword(): (fullName: String, userId: String) -> Unit {
        return { fullName: String, userId: String ->

                activity?.let {
                    val intent = (it.applicationContext as ApplinkRouter).getApplinkIntent(activity, ApplinkConst.CREATE_PASSWORD)
                    intent.putExtra("name", fullName)
                    intent.putExtra("user_id", userId)
                    startActivityForResult(intent, REQUEST_CREATE_PASSWORD)
                }

        }
    }

    override fun onGoToPhoneVerification(): () -> Unit {
        return {
            activity?.let {
                (it.applicationContext as ApplinkRouter)
                        .goToApplinkActivity(activity, ApplinkConst.PHONE_VERIFICATION)
            }
        }
    }


    override fun onSuccessGetTickerInfo(listTickerInfo: List<TickerInfoPojo>) {
        if (listTickerInfo.isNotEmpty()) {
            tickerAnnouncement.visibility = View.VISIBLE
            if (listTickerInfo.size > 1) {
                val mockData = arrayListOf<TickerData>()
                listTickerInfo.forEach {
                    mockData.add(TickerData(it.title, it.message, getTickerType(it.color), true))
                }
                val adapter = TickerPagerAdapter(activity!!, mockData)
                adapter.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(link: CharSequence?) {
                        registerAnalytics.trackClickLinkTicker(link.toString())
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, link))
                    }

                    override fun onDismiss() {
                        registerAnalytics.trackClickCloseTickerButton()
                    }

                })
                tickerAnnouncement.addPagerView(adapter, mockData)
            } else {
                listTickerInfo.first().let {
                    tickerAnnouncement.tickerTitle = it.title
                    tickerAnnouncement.setHtmlDescription(it.message)
                    tickerAnnouncement.tickerShape = getTickerType(it.color)
                }
                tickerAnnouncement.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(link: CharSequence?) {
                        registerAnalytics.trackClickLinkTicker(link.toString())
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, link))
                    }

                    override fun onDismiss() {
                        registerAnalytics.trackClickCloseTickerButton()
                    }

                })
            }
            tickerAnnouncement.setOnClickListener { v ->
                registerAnalytics.trackClickTicker()
            }

        }
    }

    private fun getTickerType(hexColor: String): Int {
        return when (hexColor) {
            "#cde4c3" -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
            "#ecdb77" -> {
                Ticker.TYPE_WARNING
            }
            else -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
        }
    }

    override fun onErrorGetTickerInfo(error: Throwable) {
        error.printStackTrace()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(PHONE_NUMBER, phoneNumber)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        registerAnalytics.trackClickOnBackButtonRegister()
    }
}
