package com.tokopedia.loginregister.registerinitial.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.lifecycle.*
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.graphql.util.getParamBoolean
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.data.DynamicBannerConstant
import com.tokopedia.loginregister.common.data.model.DynamicBannerDataModel
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.utils.PhoneUtils
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.common.view.dialog.PopupErrorDialog
import com.tokopedia.loginregister.common.view.dialog.ProceedWithPhoneDialog
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView
import com.tokopedia.loginregister.registerinitial.view.util.RegisterInitialRouter
import com.tokopedia.loginregister.registerinitial.viewmodel.RegisterInitialViewModel
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_initial_register.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 10/24/18.
 */
class RegisterInitialFragment : BaseDaggerFragment(), PartialRegisterInputView.PartialRegisterInputViewListener {

    private lateinit var optionTitle: TextView
    private lateinit var separator: View
    private lateinit var partialRegisterInputView: PartialRegisterInputView
    private lateinit var emailPhoneEditText: AutoCompleteTextView
    private lateinit var registerButton: LoginTextView
    private lateinit var loginButton: TextView
    private lateinit var container: ScrollView
    private lateinit var progressBar: RelativeLayout
    private lateinit var tickerAnnouncement: Ticker
    private lateinit var bannerRegister: ImageView
    private lateinit var socmedButton: ButtonCompat
    private lateinit var bottomSheet: SocmedBottomSheet
    private var socmedButtonsContainer: LinearLayout? = null
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var registerInitialRouter: RegisterInitialRouter

    private var phoneNumber: String? = ""
    private var source: String = ""
    private var email: String = ""
    private var isSmartLogin: Boolean = false
    private var isPending: Boolean = false
    private var isShowTicker: Boolean = false
    private var isShowBanner: Boolean = false
    private var isHitRegisterPushNotif: Boolean = false
    private var activityShouldEnd: Boolean = true

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
    lateinit var combineLoginTokenAndValidateToken: LiveData<Unit>

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
            registerInitialRouter = RegisterInitialRouter(this, this@RegisterInitialFragment)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getGoogleClientId(this))
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
        
        registerInitialRouter.source = source
    }

    private fun clearData() {
        userSession.logoutSession()
    }

    override fun onResume() {
        super.onResume()
        activity?.run {
            if (userSession.isLoggedIn && activity != null && activityShouldEnd) {
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
        emailPhoneEditText = partialRegisterInputView.findViewById(R.id.input_email_phone)
        registerButton = view.findViewById(R.id.register)
        socmedButton = view.findViewById(R.id.socmed_btn)
        loginButton = view.findViewById(R.id.login_button)
        container = view.findViewById(R.id.container)
        progressBar = view.findViewById(R.id.progress_bar)
        tickerAnnouncement = view.findViewById(R.id.ticker_announcement)
        bannerRegister = view.findViewById(R.id.banner_register)
        prepareView()
        setViewListener()
        if (isSmartLogin) {
            if (isPending) {
                val intent =  registerInitialRouter.goToVerification(email = email, otpType = OTP_TYPE_ACTIVATE)
                startActivityForResult(intent, REQUEST_PENDING_OTP_VALIDATE)
            } else {
                val intent =  registerInitialRouter.goToVerification(email = email, otpType = OTP_TYPE_REGISTER)
                startActivityForResult(intent, REQUEST_OTP_VALIDATE)
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRemoteConfig()
        initObserver()
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item?.let {
            activity?.run {
                val id = it.itemId
                if (id == ID_ACTION_LOGIN) {
                    if (activity != null) {
                        registerAnalytics.trackClickTopSignInButton()
                        registerInitialRouter.goToLoginPage(this)
                    }
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchRemoteConfig() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            isShowTicker = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_TICKER_FROM_ATC, false)
            isShowBanner = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_BANNER_REGISTER, false)
            isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF, false)
        }
    }

    private fun initData() {
        showLoadingDiscover()
        registerInitialViewModel.getProvider()
        partialRegisterInputView.setListener(this)

        val emailExtensionList = mutableListOf<String>()
        emailExtensionList.addAll(resources.getStringArray(R.array.email_extension))
        partialRegisterInputView.setEmailExtension(emailExtension, emailExtensionList)
        partialRegisterInputView.initKeyboardListener(view)

        if (!GlobalConfig.isSellerApp()) {
            if (isShowBanner) {
                registerInitialViewModel.getDynamicBannerData(DynamicBannerConstant.Page.REGISTER)
            } else {
                showTicker()
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun prepareView() {
        activity?.run {
            bottomSheet = SocmedBottomSheet(context)
            socmedButtonsContainer = bottomSheet.getSocmedButtonContainer()
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
                registerInitialRouter.goToLoginPage()
            }
        }
    }

    private fun initObserver() {
        registerInitialViewModel.getProviderResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetProvider(it.data)
                is Fail -> onFailedGetProvider(it.throwable)
            }
        })
        registerInitialViewModel.getFacebookCredentialResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetFacebookCredential(it.data)
                is Fail -> onFailedGetFacebookCredential(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenFacebookResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRegisterFacebook(it.data)
                is Fail -> onFailedRegisterFacebook(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenFacebookPhoneResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRegisterFacebookPhone(it.data)
                is Fail -> onFailedRegisterFacebookPhone(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenGoogleResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRegisterGoogle()
                is Fail -> onFailedRegisterGoogle(it.throwable)
            }
        })
        registerInitialViewModel.loginTokenAfterSQResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessReloginAfterSQ()
                is Fail -> {
                    if (it.throwable is AkamaiErrorException) {
                        showPopupErrorAkamai()
                    }
                }
            }
        })
        combineLoginTokenAndValidateToken = registerInitialViewModel.loginTokenAfterSQResponse
                .combineWith(registerInitialViewModel.validateToken) { loginToken: Result<LoginTokenPojo>?, validateToken: String? ->
                    if (loginToken is Fail) {
                        validateToken?.let { onFailedReloginAfterSQ(it, loginToken.throwable) }
                    }
                }
        registerInitialViewModel.getUserInfoResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> onFailedGetUserInfo(it.throwable)
            }
        })
        registerInitialViewModel.getUserInfoAfterAddPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetUserInfoAfterAddPin()
                is Fail -> onFailedGetUserInfoAfterAddPin(it.throwable)
            }
        })
        registerInitialViewModel.getTickerInfoResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetTickerInfo(it.data)
                is Fail -> onErrorGetTickerInfo(it.throwable)
            }
        })
        registerInitialViewModel.registerCheckResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRegisterCheck(it.data)
                is Fail -> onFailedRegisterCheck(it.throwable)
            }
        })
        registerInitialViewModel.activateUserResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessActivateUser(it.data)
                is Fail -> onFailedActivateUser(it.throwable)
            }
        })
        registerInitialViewModel.showPopup.observe(viewLifecycleOwner, Observer {
            if (it != null) PopupErrorDialog.createDialog(context, it.header, it.body, it.action)?.show()
        })
        registerInitialViewModel.goToActivationPage.observe(viewLifecycleOwner, Observer {
            if (it != null) onGoToActivationPage(it)
        })
        registerInitialViewModel.goToSecurityQuestion.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val intent =  registerInitialRouter.goToVerification(email = it, otpType = OTP_SECURITY_QUESTION)
                startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
            }
        })
        registerInitialViewModel.goToActivationPageAfterRelogin.observe(viewLifecycleOwner, Observer {
            if (it != null) onGoToActivationPageAfterRelogin()
        })
        registerInitialViewModel.goToSecurityQuestionAfterRelogin.observe(viewLifecycleOwner, Observer {
            if (it != null) onGoToSecurityQuestionAfterRelogin()
        })

        registerInitialViewModel.dynamicBannerResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> setDynamicBannerView(it.data)
                is Fail -> {
                    bannerRegister.hide()
                    showTicker()
                }
            }
        })
    }

    private fun onSuccessGetProvider(discoverItems: ArrayList<DiscoverItemViewModel>) {
        dismissLoadingDiscover()

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.dp_52))
        layoutParams.setMargins(0, 10, 0, 10)

        socmedButtonsContainer?.removeAllViews()

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

                socmedButtonsContainer?.run {
                    addView(loginTextView, childCount,
                            layoutParams)
                }
            }
        }
    }

    private fun onFailedGetProvider(throwable: Throwable) {
        dismissLoadingDiscover()
        val forbiddenMessage = context?.getString(
                com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        if (errorMessage == forbiddenMessage){
            onGoToForbiddenPage()
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity,
                    errorMessage) { registerInitialViewModel.getProvider() }.showRetrySnackbar()
            loginButton.isEnabled = false
        }
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
        if (throwable is AkamaiErrorException) {
            showPopupErrorAkamai()
        } else {
            val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
            onErrorRegister(errorMessage)
        }
    }

    private fun onSuccessRegisterFacebookPhone(loginTokenPojo: LoginTokenPojo) {
        if (loginTokenPojo.loginToken.action == 1) {
            goToChooseAccountPageFacebook(loginTokenPojo.loginToken.accessToken)
        } else {
            registerInitialViewModel.getUserInfo()
        }
    }

    private fun onFailedRegisterFacebookPhone(throwable: Throwable) {
        if (throwable is AkamaiErrorException) {
            showPopupErrorAkamai()
        } else {
            val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
            onErrorRegister(errorMessage)
        }
    }

    private fun onSuccessRegisterGoogle() {
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedRegisterGoogle(throwable: Throwable) {
        logoutGoogleAccountIfExist()
        if (throwable is AkamaiErrorException) {
            showPopupErrorAkamai()
        } else {
            val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
            onErrorRegister(errorMessage)
        }
    }

    private fun onSuccessGetUserInfo(profileInfoData: ProfileInfoData) {
        val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"
        if (profileInfoData.profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
            onGoToChangeName()
        } else {
            sendTrackingSuccessRegister()
            onSuccessRegister()
        }
    }

    private fun onFailedGetUserInfo(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        onErrorRegister(errorMessage)
    }

    private fun onSuccessGetUserInfoAfterAddPin() {
        onSuccessRegister()
    }

    private fun onFailedGetUserInfoAfterAddPin(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
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
                        val intent =  registerInitialRouter.goToVerification(email = registerCheckData.view, otpType = OTP_TYPE_ACTIVATE)
                        startActivityForResult(intent, REQUEST_PENDING_OTP_VALIDATE)
                    }
                } else {
                    val intent =  registerInitialRouter.goToVerification(email = registerCheckData.view, otpType = OTP_TYPE_REGISTER)
                    startActivityForResult(intent, REQUEST_OTP_VALIDATE)
                }
            }
        }
    }

    private fun onFailedRegisterCheck(throwable: Throwable) {
        val messageError = ErrorHandler.getErrorMessage(context, throwable)
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
        val errorMessage = ErrorHandler.getErrorMessage(context, Throwable())
        onErrorRegister(errorMessage)
    }

    //Flow should not be possible
    private fun onGoToSecurityQuestionAfterRelogin() {
        val errorMessage = ErrorHandler.getErrorMessage(context, Throwable())
        onErrorRegister(errorMessage)
    }

    private fun onSuccessReloginAfterSQ() {
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedReloginAfterSQ(validateToken: String, throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            registerInitialViewModel.reloginAfterSQ(validateToken)
        }.showRetrySnackbar()
    }

    //Wrong flow implementation
    private fun onGoToActivationPage(errorMessage: MessageErrorException) {
        NetworkErrorHelper.showSnackbar(activity, ErrorHandler.getErrorMessage(context, errorMessage))
    }

    private fun goToRegisterEmailPage() {
        showProgressBar()
        registerInitialRouter.goToRegisterEmail(this)
    }

    private fun goToRegisterEmailPageWithEmail(email: String, token: String, source: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL
        activity?.let {
            showProgressBar()
            registerInitialRouter.goToRegisterEmailPageWithParams(this, email, token, source)
        }
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
                processAfterAddNameRegisterPhone(data?.extras)
            } else if (requestCode == REQUEST_ADD_PIN) {
                registerInitialViewModel.getUserInfoAfterAddPin()
            }
            else if (requestCode == REQUEST_VERIFY_PHONE_TOKOCASH
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
                            val intent =  registerInitialRouter.goToVerification(otpType = OTP_SECURITY_QUESTION)
                            startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
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
                onErrorRegister(ErrorHandler.getErrorMessage(context, e))
            } catch (e: ApiException) {
                onErrorRegister(String.format(getString(R.string.loginregister_failed_login_google),
                        e.statusCode.toString()))
            }
        }
    }

    private fun processAfterAddNameRegisterPhone(data: Bundle?){
        val enable2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA) ?: false
        val enableSkip2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA) ?: false
        if(enable2FA){
            activityShouldEnd = false
            sendTrackingSuccessRegister()
            goToAddPin2FA(enableSkip2FA)
        } else {
            registerInitialViewModel.getUserInfo()
        }
    }

    private fun goToAddPin2FA(enableSkip2FA: Boolean){
        registerInitialRouter.goToAddPin2FA(this, enableSkip2FA)
    }

    private fun goToAddName(uuid: String) {
        phoneNumber?.run {
            registerInitialRouter.goToAddName(this@RegisterInitialFragment, uuid, this)
        }
    }

    override fun onActionPartialClick(id: String) {
        registerAnalytics.trackClickSignUpButton()
        if(Patterns.PHONE.matcher(id).matches()) {
            registerInitialViewModel.registerCheck(PhoneUtils.removeSymbolPhone(id))
        } else {
            registerInitialViewModel.registerCheck(id)
        }
    }

    private fun showLoadingDiscover() {
        socmedButtonsContainer?.run {
            val pb = ProgressBar(activity, null, android.R.attr.progressBarStyle)
            val lastPos = childCount - 1
            if (getChildAt(lastPos) !is ProgressBar) {
                addView(pb, childCount)
            }
            emailExtension?.hide()
        }
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
        socmedButtonsContainer?.run {
            val lastPos = childCount - 1
            if (getChildAt(lastPos) is ProgressBar) {
                removeViewAt(childCount - 1)
            }
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
        registerAnalytics.trackFailedClickEmailSignUpButtonAlreadyRegistered()
        val dialog = RegisteredDialog.createRegisteredEmailDialog(context, email)
        dialog?.setPrimaryCTAClickListener {
            registerAnalytics.trackClickYesButtonRegisteredEmailDialog()
            dialog.dismiss()
            context?.run {
                startActivity(LoginActivity.DeepLinkIntents.getIntentLoginFromRegister(this, email))
            }
            activity?.finish()
        }
        dialog?.setSecondaryCTAClickListener {
            registerAnalytics.trackClickChangeButtonRegisteredEmailDialog()
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun showRegisteredPhoneDialog(phone: String) {
        registerAnalytics.trackClickPhoneSignUpButton()
        registerAnalytics.trackFailedClickPhoneSignUpButton(RegisterAnalytics.LABEL_PHONE_EXIST)
        registerAnalytics.trackFailedClickPhoneSignUpButtonAlreadyRegistered()
        val dialog = RegisteredDialog.createRegisteredPhoneDialog(context, phone)
        dialog?.setPrimaryCTAClickListener {
            registerAnalytics.trackClickYesButtonRegisteredPhoneDialog()
            dialog.dismiss()
            phoneNumber = phone
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
            val intent =  registerInitialRouter.goToVerification(phone = phone, otpType = OTP_LOGIN_PHONE_NUMBER)
            startActivityForResult(intent, REQUEST_VERIFY_PHONE_TOKOCASH)
        }
        dialog?.setSecondaryCTAClickListener {
            registerAnalytics.trackClickChangeButtonRegisteredPhoneDialog()
            dialog?.dismiss()
        }
        dialog?.show()
    }
    
    private fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        registerInitialRouter.goToChooseAccountPage(this, accessToken, phoneNumber)
    }

    private fun goToChooseAccountPageFacebook(accessToken: String) {
        registerInitialRouter.goToChooseAccountPageFacebook(this, accessToken)
    }

    private fun showProceedWithPhoneDialog(phone: String) {
        val dialog = ProceedWithPhoneDialog.createDialog(context, phone)
        registerAnalytics.trackClickPhoneSignUpButton()
        dialog?.setPrimaryCTAClickListener {
            registerAnalytics.trackClickYesButtonPhoneDialog()
            dialog.dismiss()
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
            val intent =  registerInitialRouter.goToVerification(phone = phone, otpType = OTP_REGISTER_PHONE_NUMBER)
            startActivityForResult(intent, REQUEST_VERIFY_PHONE_REGISTER_PHONE)
        }
        dialog?.setSecondaryCTAClickListener {
            registerAnalytics.trackClickChangeButtonPhoneDialog()
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun setTempPhoneNumber(maskedPhoneNumber: String) {
        //use masked phone number form backend when needed
        //we need unmasked phone number (without dash) to be provided to backend
        this.phoneNumber = partialRegisterInputView.textValue
    }

    private fun sendTrackingSuccessRegister() {
        registerAnalytics.trackSuccessRegister(
                userSession.loginMethod,
                userSession.userId.toIntOrZero(),
                userSession.name,
                userSession.email,
                userSession.phoneNumber,
                userSession.isGoldMerchant,
                userSession.shopId,
                userSession.shopName
        )
    }

    private fun onSuccessRegister() {
        activityShouldEnd = true

        registerPushNotif()
        activity?.let {
            if (isFromAccount() || isFromOnboarding()) {
                val intent = RouteManager.getIntent(context, ApplinkConst.DISCOVERY_NEW_USER)
                startActivity(intent)
            }
            it.setResult(Activity.RESULT_OK)
            it.finish()
            saveFirstInstallTime()
        }
    }

    private fun isFromAccount(): Boolean = source == SOURCE_ACCOUNT

    private fun isFromAtc(): Boolean = source == SOURCE_ATC

    private fun isFromOnboarding(): Boolean = source == SOURCE_ONBOARDING

    private fun onGoToChangeName() {
        registerInitialRouter.goToChangeName(this)
    }

    private fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    private fun getTickerType(hexColor: String): Int {
        return when (hexColor) {
            "#cde4c3" -> Ticker.TYPE_ANNOUNCEMENT
            "#ecdb77" -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
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
    
    @SuppressLint("MissingPermission", "HardwareIds", "PrivateResource")
    fun getPhoneNumber() {
        activity?.let {
            val phoneNumbers = PhoneUtils.getPhoneNumber(it, permissionCheckerHelper)
            if (phoneNumbers.isNotEmpty()) {
                partialRegisterInputView.setAdapterInputEmailPhone(
                        ArrayAdapter(it, R.layout.select_dialog_item_material, phoneNumbers)
                )   { v, hasFocus ->
                        if (v.windowVisibility == View.VISIBLE) {
                            activity?.isFinishing?.let { isFinishing ->
                                if (!isFinishing) {
                                    if (hasFocus && this::emailPhoneEditText.isInitialized && emailPhoneEditText.hasFocus()) {
                                        emailPhoneEditText.showDropDown()
                                    } else {
                                        emailPhoneEditText.dismissDropDown()
                                    }
                                }
                            }
                        }
                    }
            }
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

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs.edit().putLong(
                    KEY_FIRST_INSTALL_TIME_SEARCH, 0).apply()
        }
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

    private fun showTicker() {
        if (!GlobalConfig.isSellerApp()) {
            if (isFromAtc() && isShowTicker) {
                tickerAnnouncement.visibility = View.VISIBLE
                tickerAnnouncement.tickerTitle = getString(R.string.title_ticker_from_atc)
                tickerAnnouncement.setTextDescription(getString(R.string.desc_ticker_from_atc))
                tickerAnnouncement.tickerShape = Ticker.TYPE_ANNOUNCEMENT
                tickerAnnouncement.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {}
                    override fun onDismiss() {
                        registerAnalytics.trackClickCloseTickerButton()
                    }
                })
                tickerAnnouncement.setOnClickListener {
                    registerAnalytics.trackClickTicker()
                }
            } else {
                registerInitialViewModel.getTickerInfo()
            }
        }
    }

    private fun setDynamicBannerView(dynamicBannerDataModel: DynamicBannerDataModel) {
        if (dynamicBannerDataModel.banner.isEnable) {
            context?.let {
                ImageUtils.loadImage(
                        imageView = bannerRegister,
                        url = dynamicBannerDataModel.banner.imgUrl,
                        imageLoaded = {
                            if (it) {
                                bannerRegister.show()
                                registerAnalytics.eventViewBanner(dynamicBannerDataModel.banner.imgUrl)
                            } else {
                                bannerRegister.hide()
                                showTicker()
                            }
                        })
            }
        } else {
            showTicker()
        }
    }

    private fun registerPushNotif() {
        if(isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                RegisterPushNotifService.startService(it.applicationContext)
            }
        }
    }

    private fun showPopupErrorAkamai() {
        PopupErrorDialog.showPopupErrorAkamai(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        registerInitialViewModel.getProviderResponse.removeObservers(this)
        registerInitialViewModel.getFacebookCredentialResponse.removeObservers(this)
        registerInitialViewModel.loginTokenFacebookResponse.removeObservers(this)
        registerInitialViewModel.loginTokenFacebookPhoneResponse.removeObservers(this)
        registerInitialViewModel.loginTokenGoogleResponse.removeObservers(this)
        registerInitialViewModel.loginTokenAfterSQResponse.removeObservers(this)
        registerInitialViewModel.getUserInfoResponse.removeObservers(this)
        registerInitialViewModel.getTickerInfoResponse.removeObservers(this)
        registerInitialViewModel.registerCheckResponse.removeObservers(this)
        registerInitialViewModel.activateUserResponse.removeObservers(this)
        registerInitialViewModel.goToActivationPage.removeObservers(this)
        registerInitialViewModel.goToSecurityQuestion.removeObservers(this)
        registerInitialViewModel.goToActivationPageAfterRelogin.removeObservers(this)
        registerInitialViewModel.goToSecurityQuestionAfterRelogin.removeObservers(this)
        registerInitialViewModel.dynamicBannerResponse.removeObservers(this)
        combineLoginTokenAndValidateToken.removeObservers(this)
        registerInitialViewModel.flush()
    }

    companion object {

        private val ID_ACTION_LOGIN = 112

        val REQUEST_REGISTER_EMAIL = 101
        private val REQUEST_CREATE_PASSWORD = 102
        private val REQUEST_SECURITY_QUESTION = 103
        private val REQUEST_VERIFY_PHONE_REGISTER_PHONE = 105
        val REQUEST_ADD_NAME_REGISTER_PHONE = 107
        private val REQUEST_VERIFY_PHONE_TOKOCASH = 108
        val REQUEST_CHOOSE_ACCOUNT = 109
        val REQUEST_CHANGE_NAME = 111
        private val REQUEST_LOGIN_GOOGLE = 112
        private val REQUEST_OTP_VALIDATE = 113
        private val REQUEST_PENDING_OTP_VALIDATE = 114
        val REQUEST_ADD_PIN = 115

        private const val OTP_TYPE_ACTIVATE = 143
        private const val OTP_TYPE_REGISTER = 126
        private const val OTP_SECURITY_QUESTION = 134
        private const val OTP_LOGIN_PHONE_NUMBER = 112
        private const val OTP_REGISTER_PHONE_NUMBER = 116

        private const val FACEBOOK = "facebook"
        private const val GPLUS = "gplus"
        private const val PHONE_NUMBER = "phonenumber"

        private const val PHONE_TYPE = "phone"
        private const val EMAIL_TYPE = "email"

        private const val SOURCE_ACCOUNT = "account"
        private const val SOURCE_ATC = "atc"
        private const val SOURCE_ONBOARDING = "onboarding"

        const val FACEBOOK_LOGIN_TYPE = "fb"

        private const val REMOTE_CONFIG_KEY_TICKER_FROM_ATC = "android_user_ticker_from_atc"
        private const val REMOTE_CONFIG_KEY_BANNER_REGISTER = "android_user_banner_register"
        private const val REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF = "android_user_register_otp_push_notif_register_page"

        private const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        private const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

        private const val BANNER_REGISTER_URL = "https://ecs7.tokopedia.net/android/others/banner_login_register_page.png"
      
        const val TOKOPEDIA_CARE_PATH = "help"
        fun createInstance(bundle: Bundle): RegisterInitialFragment {
            val fragment = RegisterInitialFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
