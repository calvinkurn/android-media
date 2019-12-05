package com.tokopedia.loginregister.registerinitial.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
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
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.graphql.util.getParamBoolean
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.PartialRegisterInputUtils
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.registerinitial.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView
import com.tokopedia.loginregister.registerinitial.viewmodel.RegisterInitialViewModel
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.Token.Companion.GOOGLE_API_KEY
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_initial_register.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 10/24/18.
 */
class RegisterInitialFragment : BaseDaggerFragment(), PartialRegisterInputView.PartialRegisterInputViewListener {

    lateinit var optionTitle: TextView
    lateinit var separator: View
    lateinit var partialRegisterInputView: PartialRegisterInputView
    lateinit var registerButton: LoginTextView
    lateinit var loginButton: TextView
    lateinit var container: ScrollView
    lateinit var progressBar: RelativeLayout
    lateinit var tickerAnnouncement: Ticker
    private lateinit var socmedButton: ButtonCompat
    private lateinit var bottomSheet: BottomSheetUnify
    private lateinit var socmedButtonsContainer: LinearLayout

    private var phoneNumber: String? = ""
    private var source: String = ""
    private var email: String = ""
    private var isSmartLogin: Boolean = false
    private var isPending: Boolean = false

    @field:Named(SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val registerInitialViewModel by lazy {
        viewModelProvider.get(RegisterInitialViewModel::class.java)
    }

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
        clearData()
        callbackManager = CallbackManager.Factory.create()

        activity?.run {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(GOOGLE_API_KEY)
                    .requestEmail()
                    .requestProfile()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        phoneNumber = getParamString(PHONE_NUMBER, arguments, savedInstanceState, "")
        source = getParamString(ApplinkConstInternalGlobal.PARAM_SOURCE, arguments, savedInstanceState, "")
        isSmartLogin = getParamBoolean(ApplinkConstInternalGlobal.PARAM_IS_SMART_LOGIN, arguments, savedInstanceState, false)
        isPending = getParamBoolean(ApplinkConstInternalGlobal.PARAM_IS_PENDING, arguments, savedInstanceState, false)
        email = getParamString(ApplinkConstInternalGlobal.PARAM_EMAIL, arguments, savedInstanceState, "")
    }

    private fun clearData() {
        userSession.logoutSession()
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
        separator = view.findViewById(R.id.separator)
        partialRegisterInputView = view.findViewById(R.id.register_input_view)
        registerButton = view.findViewById(R.id.register)
        socmedButton = view.findViewById(R.id.socmed_btn)
        loginButton = view.findViewById(R.id.login_button)
        container = view.findViewById(R.id.container)
        progressBar = view.findViewById(R.id.progress_bar)
        tickerAnnouncement = view.findViewById(R.id.ticker_announcement)
        prepareView()
        setViewListener()
        if (isSmartLogin) {
            if (isPending) {
                goToPendingOtpValidator(email)
            } else {
                goToOtpValidator(email)
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
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
        showLoadingDiscover()
        registerInitialViewModel.getProvider()
        partialRegisterInputView.setListener(this)
        registerInitialViewModel.getTickerInfo()

        val emailExtensionList = mutableListOf<String>()
        emailExtensionList.addAll(resources.getStringArray(R.array.email_extension))
        partialRegisterInputView.setEmailExtension(emailExtension, emailExtensionList)
        partialRegisterInputView.initKeyboardListener(view)
    }

    @SuppressLint("RtlHardcoded")
    private fun prepareView() {
        activity?.run {
            val viewBottomSheetDialog = View.inflate(context, R.layout.layout_socmed_bottomsheet, null)
            socmedButtonsContainer = viewBottomSheetDialog.findViewById(R.id.socmed_container)

            bottomSheet = BottomSheetUnify()
            bottomSheet.setTitle(getString(R.string.choose_social_media))
            bottomSheet.setChild(viewBottomSheetDialog)
            bottomSheet.setCloseClickListener {
                registerAnalytics.trackClickCloseSocmedButton()
                bottomSheet.dismiss()
            }

            socmedButton.setOnClickListener {
                registerAnalytics.trackClickSocmedButton()
                bottomSheet.show(supportFragmentManager, getString(R.string.bottom_sheet_show))
            }

            registerButton.visibility = View.GONE
            partialRegisterInputView.visibility = View.VISIBLE
            partialRegisterInputView.setButtonValidator(true)
            checkPermissionGetPhoneNumber()
            optionTitle.setText(R.string.register_option_title)

            registerButton.setColor(Color.WHITE)
            registerButton.setBorderColor(MethodChecker.getColor(activity, R.color.black_38))
            registerButton.setRoundCorner(10)
            registerButton.setImageResource(R.drawable.ic_email)
            registerButton.setOnClickListener {
                TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_EMAIL)
                goToRegisterEmailPage()

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

    private fun setViewListener() {
        loginButton.setOnClickListener {
            registerAnalytics.trackClickBottomSignInButton()
            activity?.run {
                finish()
                analytics.eventClickOnLoginFromRegister()
                goToLoginPage()
            }
        }
    }

    private fun initObserver() {
        registerInitialViewModel.getProviderResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetProvider(it.data)
                is Fail -> onFailedGetProvider(it.throwable)
            }
        })
        registerInitialViewModel.getFacebookCredentialResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetFacebookCredential(it.data)
                is Fail -> onFailedGetFacebookCredential(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenFacebookResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessRegisterFacebook(it.data)
                is Fail -> onFailedRegisterFacebook(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenFacebookPhoneResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessRegisterFacebookPhone(it.data)
                is Fail -> onFailedRegisterFacebookPhone(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenGoogleResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessRegisterGoogle()
                is Fail -> onFailedRegisterGoogle(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenAfterSQResponse.observe(this, Observer {
            if (it is Success) {
                onSuccessReloginAfterSQ()
            }
        })
        registerInitialViewModel.loginTokenAfterSQResponse
                .combineWith(registerInitialViewModel.validateToken) { loginToken: Result<LoginTokenPojo>?, validateToken: String? ->
                    if (loginToken is Fail) {
                        validateToken?.let { onFailedReloginAfterSQ(it, loginToken.throwable) }
                    }
                }
        registerInitialViewModel.getUserInfoResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> onFailedGetUserInfo(it.throwable)
            }
        })
        registerInitialViewModel.getTickerInfoResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetTickerInfo(it.data)
                is Fail -> onErrorGetTickerInfo(it.throwable)
            }
        })
        registerInitialViewModel.registerCheckResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessRegisterCheck(it.data)
                is Fail -> onFailedRegisterCheck(it.throwable)
            }
        })
        registerInitialViewModel.activateUserResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessActivateUser(it.data)
                is Fail -> onFailedActivateUser(it.throwable)
            }
        })
        registerInitialViewModel.goToActivationPage.observe(this, Observer {
            if (it != null) onGoToActivationPage(it)
        })
        registerInitialViewModel.goToSecurityQuestion.observe(this, Observer {
            if (it != null) onGoToSecurityQuestion(it)
        })
        registerInitialViewModel.goToActivationPageAfterRelogin.observe(this, Observer {
            if (it != null) onGoToActivationPageAfterRelogin()
        })
        registerInitialViewModel.goToSecurityQuestionAfterRelogin.observe(this, Observer {
            if (it != null) onGoToSecurityQuestionAfterRelogin()
        })
    }

    private fun onSuccessGetProvider(discoverItems: ArrayList<DiscoverItemViewModel>) {
        dismissLoadingDiscover()

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.dp_52))
        layoutParams.setMargins(0, 10, 0, 10)

        socmedButtonsContainer.removeAllViews()

        for (i in discoverItems.indices) {
            val item = discoverItems[i]
            if (item.id != PHONE_NUMBER) {
                val loginTextView = LoginTextView(activity, MethodChecker.getColor(activity, R.color.white))
                loginTextView.setText(item.name)
                loginTextView.setBorderColor(MethodChecker.getColor(activity, R.color
                        .black_38))
                loginTextView.setImage(item.image)
                loginTextView.setRoundCorner(10)

                setDiscoverOnClickListener(item, loginTextView)

                socmedButtonsContainer.addView(loginTextView, socmedButtonsContainer.childCount,
                        layoutParams)
            }
        }
    }

    private fun onFailedGetProvider(throwable: Throwable) {
        dismissLoadingDiscover()

        ErrorHandlerSession.getErrorMessage(object : ErrorHandlerSession.ErrorForbiddenListener {
            override fun onForbidden() {
                onGoToForbiddenPage()
            }

            override fun onError(errorMessage: String) {
                NetworkErrorHelper.createSnackbarWithAction(activity,
                        errorMessage) { registerInitialViewModel.getProvider() }.showRetrySnackbar()
                loginButton.isEnabled = false
            }
        }, throwable, context)
    }

    private fun onSuccessGetFacebookCredential(facebookCredentialData: FacebookCredentialData) {
        try {
            if (facebookCredentialData.email.isNotEmpty()) {
                registerInitialViewModel.registerFacebook(
                        facebookCredentialData.accessToken.token,
                        facebookCredentialData.email
                )
            } else if (facebookCredentialData.phone.isNotEmpty()) {
                registerInitialViewModel.registerFacebookPhone(
                        facebookCredentialData.accessToken.token,
                        facebookCredentialData.phone
                )
            }
        } catch (e: Exception) {
            e.message?.let { onErrorRegister(it) }
        }
    }

    private fun onFailedGetFacebookCredential(throwable: Throwable) {
        if (isAdded && activity != null) {
            throwable.message?.let { onErrorRegister(ErrorHandler.getErrorMessage(context, throwable)) }
        }
    }

    private fun onSuccessRegisterFacebook(loginTokenPojo: LoginTokenPojo) {
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedRegisterFacebook(throwable: Throwable) {
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        onErrorRegister(errorMessage)
    }

    private fun onSuccessRegisterFacebookPhone(loginTokenPojo: LoginTokenPojo) {
        if (loginTokenPojo.loginToken.action == 1) {
            goToChooseAccountPageFacebook(loginTokenPojo.loginToken.accessToken)
        } else {
            registerInitialViewModel.getUserInfo()
        }
    }

    private fun onFailedRegisterFacebookPhone(throwable: Throwable) {
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        onErrorRegister(errorMessage)
    }

    private fun onSuccessRegisterGoogle() {
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedRegisterGoogle(throwable: Throwable) {
        logoutGoogleAccountIfExist()
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        onErrorRegister(errorMessage)
    }

    private fun onSuccessGetUserInfo(profileInfo: ProfileInfo) {
        val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

        if (profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
            onGoToChangeName()
        } else {
            onSuccessRegister()
        }
    }

    private fun onFailedGetUserInfo(throwable: Throwable) {
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        onErrorRegister(errorMessage)
    }

    private fun onSuccessGetTickerInfo(listTickerInfo: List<TickerInfoPojo>) {
        if (listTickerInfo.isNotEmpty()) {
            tickerAnnouncement.visibility = View.VISIBLE
            if (listTickerInfo.size > 1) {
                val mockData = arrayListOf<TickerData>()
                listTickerInfo.forEach {
                    mockData.add(TickerData(it.title, it.message, getTickerType(it.color), true))
                }
                val adapter = TickerPagerAdapter(activity!!, mockData)
                adapter.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        registerAnalytics.trackClickLinkTicker(linkUrl.toString())
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
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
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        registerAnalytics.trackClickLinkTicker(linkUrl.toString())
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                        registerAnalytics.trackClickCloseTickerButton()
                    }

                })
            }
            tickerAnnouncement.setOnClickListener {
                registerAnalytics.trackClickTicker()
            }

        }
    }

    private fun onErrorGetTickerInfo(error: Throwable) {
        error.printStackTrace()
    }

    private fun onSuccessRegisterCheck(registerCheckData: RegisterCheckData) {
        when (registerCheckData.registerType) {
            PHONE_TYPE -> {
                setTempPhoneNumber(registerCheckData.view)
                if (registerCheckData.isExist) {
                    showRegisteredPhoneDialog(registerCheckData.view)
                } else {
                    showProceedWithPhoneDialog(registerCheckData.view)
                }
            }
            EMAIL_TYPE -> {
                registerAnalytics.trackClickEmailSignUpButton()
                if (registerCheckData.isExist) {
                    if (!registerCheckData.isPending) {
                        showRegisteredEmailDialog(registerCheckData.view)
                    } else {
                        goToPendingOtpValidator(registerCheckData.view)
                    }
                } else {
                    goToOtpValidator(registerCheckData.view)
                }
            }
        }
    }

    private fun onFailedRegisterCheck(throwable: Throwable) {
        val messageError = ErrorHandlerSession.getErrorMessage(context, throwable)
        registerAnalytics.trackFailedClickSignUpButton(messageError)
        partialRegisterInputView.onErrorValidate(messageError)
        phoneNumber = ""
    }

    private fun onSuccessActivateUser(activateUserPojo: ActivateUserPojo) {
        userSession.clearToken()
        userSession.setToken(activateUserPojo.accessToken, activateUserPojo.tokenType, activateUserPojo.refreshToken)
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedActivateUser(throwable: Throwable) {
        throwable.message?.let { onErrorRegister(ErrorHandler.getErrorMessage(context, throwable)) }
    }

    //Flow should not be possible
    private fun onGoToActivationPageAfterRelogin() {
        val errorMessage = ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context)
        onErrorRegister(errorMessage)
    }

    //Flow should not be possible
    private fun onGoToSecurityQuestionAfterRelogin() {
        val errorMessage = ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context)
        onErrorRegister(errorMessage)
    }

    private fun onSuccessReloginAfterSQ() {
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedReloginAfterSQ(validateToken: String, throwable: Throwable) {
        val errorMessage = ErrorHandlerSession.getErrorMessage(throwable, context, true)
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            registerInitialViewModel.reloginAfterSQ(validateToken)
        }.showRetrySnackbar()
    }

    //Wrong flow implementation
    private fun onGoToActivationPage(errorMessage: MessageErrorException) {
        NetworkErrorHelper.showSnackbar(activity, ErrorHandlerSession.getErrorMessage(context, errorMessage))
    }

    private fun onGoToSecurityQuestion(email: String) {
        val intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                activity, RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, "", email)
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
    }

    private fun goToLoginPage() {
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

    private fun goToRegisterEmailPageWithEmail(email: String, token: String, source: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL

        activity?.let {
            showProgressBar()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.EMAIL_REGISTER)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, token)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
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

    private fun goToOtpValidator(email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.OTP_VALIDATOR)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_REGISTER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        startActivityForResult(intent, REQUEST_OTP_VALIDATE)
    }

    private fun goToPendingOtpValidator(email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.OTP_VALIDATOR)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ACTIVATE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        startActivityForResult(intent, REQUEST_PENDING_OTP_VALIDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        activity?.let {
            if (requestCode == REQUEST_LOGIN_GOOGLE && data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                task?.let { taskGoogleSignInAccount ->
                    handleGoogleSignInResult(taskGoogleSignInAccount)
                }
            } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity.RESULT_OK) {
                registerInitialViewModel.getUserInfo()
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
            } else if (requestCode == REQUEST_SECURITY_QUESTION
                    && resultCode == Activity.RESULT_OK
                    && data != null) {
                data.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")?.let { validateToken ->
                    registerInitialViewModel.reloginAfterSQ(validateToken)
                }

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
                registerInitialViewModel.getUserInfo()
            } else if (requestCode == REQUEST_VERIFY_PHONE_TOKOCASH
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null) {
                val accessToken = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                val phoneNumber = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                goToChooseAccountPage(accessToken, phoneNumber)

            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                it.setResult(Activity.RESULT_OK)
                if (data != null) {
                    data.extras?.let { bundle ->
                        if (bundle.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, false)) {
                            onGoToSecurityQuestion("")
                        } else {
                            it.finish()
                        }
                    }
                } else {
                    it.finish()
                }
            } else if (requestCode == REQUEST_CHANGE_NAME && resultCode == Activity.RESULT_OK) {
                registerInitialViewModel.getUserInfo()
            } else if (requestCode == REQUEST_CHANGE_NAME && resultCode == Activity.RESULT_CANCELED) {
                userSession.logoutSession()
                dismissProgressBar()
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            } else if (requestCode == REQUEST_OTP_VALIDATE
                    && resultCode == Activity.RESULT_OK
                    && data != null) {
                data.extras?.let { bundle ->
                    val email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL)
                    val token = bundle.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                    val source = bundle.getString(ApplinkConstInternalGlobal.PARAM_SOURCE)
                    if (!email.isNullOrEmpty() && !token.isNullOrEmpty()) {
                        if (!source.isNullOrEmpty()) goToRegisterEmailPageWithEmail(email, token, source)
                        else goToRegisterEmailPageWithEmail(email, token, "")
                    }
                }
            } else if (requestCode == REQUEST_OTP_VALIDATE && resultCode == Activity.RESULT_CANCELED) {
                it.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_PENDING_OTP_VALIDATE
                    && resultCode == Activity.RESULT_OK
                    && data != null) {
                data.extras?.let { bundle ->
                    val email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL)
                    val token = bundle.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                    val source = bundle.getString(ApplinkConstInternalGlobal.PARAM_SOURCE)
                    if (!email.isNullOrEmpty() && !token.isNullOrEmpty())
                        registerInitialViewModel.activateUser(email, token)
                }
            } else if (requestCode == REQUEST_PENDING_OTP_VALIDATE && resultCode == Activity.RESULT_CANCELED) {
                it.setResult(Activity.RESULT_CANCELED)
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
                registerInitialViewModel.registerGoogle(accessToken, email)
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
        registerInitialViewModel.registerCheck(id)
    }

    private fun showLoadingDiscover() {
        val pb = ProgressBar(activity, null, android.R.attr.progressBarStyle)
        val lastPos = socmedButtonsContainer.childCount - 1
        if (socmedButtonsContainer.getChildAt(lastPos) !is ProgressBar) {
            socmedButtonsContainer.addView(pb, socmedButtonsContainer.childCount)
        }
        emailExtension?.hide()
    }

    private fun setDiscoverOnClickListener(discoverItemViewModel: DiscoverItemViewModel,
                                           loginTextView: LoginTextView) {

        when (discoverItemViewModel.id.toLowerCase()) {
            FACEBOOK -> loginTextView.setOnClickListener { onRegisterFacebookClick() }
            GPLUS -> loginTextView.setOnClickListener { onRegisterGoogleClick() }
        }
    }

    private fun onRegisterFacebookClick() {
        activity?.let {
            bottomSheet.dismiss()
            registerAnalytics.trackClickFacebookButton(it.applicationContext)
            TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_FACEBOOK)
            registerInitialViewModel.getFacebookCredential(this, callbackManager)
        }

    }

    private fun onRegisterGoogleClick() {
        activity?.let {
            bottomSheet.dismiss()
            registerAnalytics.trackClickGoogleButton(it.applicationContext)
            TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_GMAIL)
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, REQUEST_LOGIN_GOOGLE)
        }

    }

    private fun dismissLoadingDiscover() {
        val lastPos = socmedButtonsContainer.childCount - 1
        if (socmedButtonsContainer.getChildAt(lastPos) is ProgressBar) {
            socmedButtonsContainer.removeViewAt(socmedButtonsContainer.childCount - 1)
        }
    }

    private fun showProgressBar() {

        progressBar.visibility = View.VISIBLE
        container.visibility = View.GONE
        loginButton.visibility = View.GONE

    }

    private fun dismissProgressBar() {

        progressBar.visibility = View.GONE

        container.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE
    }

    private fun onErrorRegister(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
        registerAnalytics.trackErrorRegister(errorMessage, userSession.loginMethod)
    }

    private fun showRegisteredEmailDialog(email: String) {
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
            dialog.setOnCancelClickListener {
                registerAnalytics.trackClickChangeButtonRegisteredEmailDialog()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun showRegisteredPhoneDialog(phone: String) {
        registerAnalytics.trackClickPhoneSignUpButton()
        registerAnalytics.trackFailedClickPhoneSignUpButton(RegisterAnalytics.LABEL_PHONE_EXIST)
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.phone_number_already_registered))
        dialog.setDesc(
                String.format(resources.getString(
                        R.string.reigster_page_phone_number_already_registered_info), phone))
        dialog.setBtnOk(getString(R.string.already_registered_yes))
        dialog.setOnOkClickListener {
            registerAnalytics.trackClickYesButtonRegisteredPhoneDialog()
            dialog.dismiss()
            phoneNumber = phone
            goToVerifyAccountPage(phoneNumber)
        }
        dialog.setBtnCancel(getString(R.string.already_registered_no))
        dialog.setOnCancelClickListener {
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

    private fun goToChooseAccountPageFacebook(accessToken: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, FACEBOOK_LOGIN_TYPE)

            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun getChooseAccountData(data: Intent): ChooseTokoCashAccountViewModel {
        return data.getParcelableExtra(ChooseTokoCashAccountViewModel.ARGS_DATA)
    }


    private fun showProceedWithPhoneDialog(phone: String) {
        registerAnalytics.trackClickPhoneSignUpButton()
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(phone)
        dialog.setDesc(resources.getString(R.string.phone_number_not_registered_info))
        dialog.setBtnOk(getString(R.string.proceed_with_phone_number))
        dialog.setOnOkClickListener {
            registerAnalytics.trackClickYesButtonPhoneDialog()
            dialog.dismiss()
            goToVerificationPhoneRegister(phone)
        }
        dialog.setBtnCancel(getString(R.string.already_registered_no))
        dialog.setOnCancelClickListener {
            registerAnalytics.trackClickChangeButtonPhoneDialog()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setTempPhoneNumber(maskedPhoneNumber: String) {
        //use masked phone number form backend when needed
        //we need unmasked phone number (without dash) to be provided to backend
        this.phoneNumber = partialRegisterInputView.textValue
    }

    private fun onSuccessRegister() {
        activity?.let {
            registerAnalytics.trackSuccessRegister(userSession.loginMethod)

            if (isFromAccount()) {
                val intent = RouteManager.getIntent(context, ApplinkConst.DISCOVERY_NEW_USER)
                startActivity(intent)
            }

            it.setResult(Activity.RESULT_OK)
            it.finish()
        }


    }

    private fun isFromAccount(): Boolean {
        return source == "account"
    }

    private fun onGoToChangeName() {
        activity?.let {
            val intent = (it.applicationContext as ApplinkRouter).getApplinkIntent(activity, ApplinkConst.ADD_NAME_PROFILE)
            startActivityForResult(intent, REQUEST_CHANGE_NAME)
        }
    }

    private fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    fun onGoToCreatePassword(): (fullName: String, userId: String) -> Unit {
        return { fullName: String, userId: String ->

            activity?.let {
                val intent = (it.applicationContext as ApplinkRouter).getApplinkIntent(activity, ApplinkConst.CREATE_PASSWORD)
                intent.putExtra("name", fullName)
                intent.putExtra("user_id", userId)
                startActivityForResult(intent, REQUEST_CREATE_PASSWORD)
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

    private fun checkPermissionGetPhoneNumber() {
        permissionCheckerHelper.checkPermission(this, PermissionCheckerHelper.Companion.PERMISSION_READ_PHONE_STATE, object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
                context?.let {
                    permissionCheckerHelper.onPermissionDenied(it, permissionText)
                }
            }

            override fun onNeverAskAgain(permissionText: String) {
                context?.let {
                    permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                }
            }

            override fun onPermissionGranted() {
                getPhoneNumber()
            }

        })
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getPhoneNumber() {
        activity?.let {
            val phoneNumbers = arrayListOf<String>()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val subscription = it.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                if (subscription.activeSubscriptionInfoList != null && subscription.activeSubscriptionInfoCount > 0) {
                    for (info in subscription.activeSubscriptionInfoList) {
                        if (!info.number.isNullOrEmpty() &&
                                PartialRegisterInputUtils.getType(info.number) == PartialRegisterInputUtils.PHONE_TYPE &&
                                PartialRegisterInputUtils.isValidPhone(info.number))
                            phoneNumbers.add(info.number)
                    }
                }
            } else {
                val telephony = it.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
                if (!telephony.line1Number.isNullOrEmpty() &&
                        PartialRegisterInputUtils.getType(telephony.line1Number) == PartialRegisterInputUtils.PHONE_TYPE &&
                        PartialRegisterInputUtils.isValidPhone(telephony.line1Number))
                    phoneNumbers.add(telephony.line1Number)
            }

            if (phoneNumbers.isNotEmpty())
                partialRegisterInputView.setAdapterInputEmailPhone(ArrayAdapter(it, R.layout.select_dialog_item_material, phoneNumbers))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(PHONE_NUMBER, phoneNumber)
        outState.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        super.onSaveInstanceState(outState)
    }

    fun onBackPressed() {
        registerAnalytics.trackClickOnBackButtonRegister()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.let {
            permissionCheckerHelper.onRequestPermissionsResult(it, requestCode, permissions, grantResults)
        }
    }

    private fun logoutGoogleAccountIfExist() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (googleSignInAccount != null) mGoogleSignInClient.signOut()
    }

    private fun <T, K, R> LiveData<T>.combineWith(
            liveData: LiveData<K>,
            block: (T?, K?) -> R
    ): LiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block.invoke(this.value, liveData.value)
        }
        result.addSource(liveData) {
            result.value = block.invoke(this.value, liveData.value)
        }
        return result
    }

    companion object {

        private val ID_ACTION_LOGIN = 112

        private val REQUEST_REGISTER_EMAIL = 101
        private val REQUEST_CREATE_PASSWORD = 102
        private val REQUEST_SECURITY_QUESTION = 103
        private val REQUEST_VERIFY_PHONE_REGISTER_PHONE = 105
        private val REQUEST_ADD_NAME_REGISTER_PHONE = 107
        private val REQUEST_VERIFY_PHONE_TOKOCASH = 108
        private val REQUEST_CHOOSE_ACCOUNT = 109
        private val REQUEST_CHANGE_NAME = 111
        private val REQUEST_LOGIN_GOOGLE = 112
        private val REQUEST_OTP_VALIDATE = 113
        private val REQUEST_PENDING_OTP_VALIDATE = 114

        private const val OTP_TYPE_ACTIVATE = "143"
        private const val OTP_TYPE_REGISTER = "126"

        private const val FACEBOOK = "facebook"
        private const val GPLUS = "gplus"
        private const val PHONE_NUMBER = "phonenumber"

        private const val PHONE_TYPE = "phone"
        private const val EMAIL_TYPE = "email"

        private const val FACEBOOK_LOGIN_TYPE = "fb"

        fun createInstance(bundle: Bundle): RegisterInitialFragment {
            val fragment = RegisterInitialFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
