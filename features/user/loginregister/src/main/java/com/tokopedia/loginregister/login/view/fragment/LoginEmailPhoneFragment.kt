package com.tokopedia.loginregister.login.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LANDING_SHOP_CREATION
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_GOOGLE
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.NeedHelpAnalytics
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.error.LoginErrorCode
import com.tokopedia.loginregister.common.utils.RegisterUtil.removeErrorCode
import com.tokopedia.loginregister.common.utils.SellerAppWidgetHelper
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.common.view.PartialRegisterInputView
import com.tokopedia.loginregister.common.view.banner.DynamicBannerConstant
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.login.view.bottomsheet.NeedHelpBottomSheet
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.common.view.dialog.PopupErrorDialog
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprintResult
import com.tokopedia.loginregister.login.router.LoginRouter
import com.tokopedia.loginregister.login.service.GetDefaultChosenAddressService
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_EMAIL
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_LOGIN_METHOD
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_PHONE
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.network.TokenErrorException
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
import com.tokopedia.sessioncommon.view.admin.dialog.LocationAdminDialog
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_login_with_phone.*
import kotlinx.android.synthetic.main.layout_partial_register_input.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named


/**
 * @author by nisie on 18/01/19.
 */
open class LoginEmailPhoneFragment : BaseDaggerFragment(), LoginEmailPhoneContract.View {

    private var isTraceStopped: Boolean = false
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    val viewModel by lazy { viewModelProvider.get(LoginEmailPhoneViewModel::class.java) }

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    @Inject
    lateinit var seamlessAnalytics: SeamlessLoginAnalytics

    @Inject
    lateinit var needHelpAnalytics: NeedHelpAnalytics

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    private var source: String = ""
    protected var isAutoLogin: Boolean = false
    private var isShowTicker: Boolean = false
    private var isShowBanner: Boolean = false
    private var isEnableFingerprint = true
    private var isEnableSilentVerif = false
    private var isHitRegisterPushNotif: Boolean = false
    private var isEnableEncryptConfig: Boolean = false
    private var activityShouldEnd = true
    private var isFromRegister = false
    private var isFromChooseAccount = false
    private var isUseHash = false
    private var validateToken = ""
    private var isLoginAfterSq = false

    private var socmedButtonsContainer: LinearLayout? = null
    private var socmedBottomSheet: SocmedBottomSheet? = null
    private var socmedButton: UnifyButton? = null

    private var currentEmail = ""
    private var tempValidateToken = ""

    private var partialRegisterInputView: PartialRegisterInputView? = null
    private var emailPhoneEditText: EditText? = null
    var partialActionButton: UnifyButton? = null
    private var tickerAnnouncement: Ticker? = null
    private var bannerLogin: ImageUnify? = null
    private var callTokopediaCare: Typography? = null
    private var sharedPrefs: SharedPreferences? = null

    private var needHelpBottomSheetUnify: NeedHelpBottomSheet? = null

    override fun getScreenName(): String {
        return LoginRegisterAnalytics.SCREEN_LOGIN
    }

    override fun initInjector() {
        getComponent(LoginComponent::class.java).inject(this)
    }

    override fun onStart() {
        super.onStart()
        setupSpannableText()
        activity?.let {
            analytics.trackScreen(it, screenName)
        }
    }

    override fun onResume() {
        super.onResume()
        if (userSession.isLoggedIn && activity != null && activityShouldEnd) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    override fun getFingerprintConfig(): Boolean = isEnableFingerprint

    override fun stopTrace() {
        if (!isTraceStopped) {
            performanceMonitoring.stopTrace()
            isTraceStopped = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            val devOpsText = SpannableString(getString(R.string.developer_options))
            context?.let {
                devOpsText.setSpan(ForegroundColorSpan(
                        MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)),
                        0, devOpsText.length, 0)
            }
            menu.add(Menu.NONE, LoginConstants.MenuItemId.ID_ACTION_DEVOPS, 1, devOpsText)
            menu.findItem(LoginConstants.MenuItemId.ID_ACTION_DEVOPS).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    open fun setupToolbar() {
        activity?.findViewById<HeaderUnify>(R.id.unifytoolbar)?.apply {
            headerTitle = getString(R.string.login)
            actionText = getString(R.string.register)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            actionTextView?.setOnClickListener {
                registerAnalytics.trackClickTopSignUpButton()
                goToRegisterInitial(source)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == LoginConstants.MenuItemId.ID_ACTION_DEVOPS) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
                return true
            }
        }
        if (id == LoginConstants.MenuItemId.ID_ACTION_REGISTER) {
            registerAnalytics.trackClickTopSignUpButton()
            goToRegisterInitial(source)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackgroundColor()
        activity?.let {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getGoogleClientId(it))
                    .requestEmail()
                    .requestProfile()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(it, gso)
        }

        performanceMonitoring = PerformanceMonitoring.start(LOGIN_LOAD_TRACE)

        source = getParamString(ApplinkConstInternalGlobal.PARAM_SOURCE, arguments, savedInstanceState, "")
        isAutoLogin = getParamBoolean(LoginConstants.AutoLogin.IS_AUTO_LOGIN, arguments, savedInstanceState, false)
        refreshRolloutVariant()
    }

    open fun refreshRolloutVariant() {
        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)
    }

    private fun setupBackgroundColor() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                    MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_login_with_phone, container, false)
        partialRegisterInputView = view.findViewById(R.id.login_input_view)
        emailPhoneEditText = partialRegisterInputView?.findViewById(R.id.input_email_phone)
        partialActionButton = partialRegisterInputView?.findViewById(R.id.register_btn)
        tickerAnnouncement = view.findViewById(R.id.ticker_announcement)
        bannerLogin = view.findViewById(R.id.banner_login)
        callTokopediaCare = view.findViewById(R.id.to_tokopedia_care)
        socmedButton = view.findViewById(R.id.socmed_btn)
        return view
    }

    /*
    * check for savedInstanceState, to prevent clearData() being called when user low on memory
    * when the device is on low memory the system will kill the background activity/page,
    * when activity is killed by the system it will called onViewCreated when user back to login page after finishing otp flow and the token will be cleared using clearData() method
    * it means previous token we got from choose account/otp will be erased, and user will get 401 when hit get user info api.
    * to prevent this when user back to login page it will check whether savedInstanceState is null or not,
    * if null it means the activity is first launch, and if it isn't null it means activity is resuming.
    * we only clear the data when activity is resuming after only being killed by system
    * */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRemoteConfig()
        if(savedInstanceState == null) {
            clearData()
        }
        initObserver()
        prepareView()
        prepareArgData()

        if (!GlobalConfig.isSellerApp()) {
            if (isShowBanner) {
                viewModel.getDynamicBannerData(DynamicBannerConstant.Page.LOGIN)
            } else {
                showTicker()
            }
        }

        val emailExtensionList = mutableListOf<String>()
        emailExtensionList.addAll(resources.getStringArray(R.array.email_extension))
        partialRegisterInputView?.setEmailExtension(emailExtension, emailExtensionList)
        partialRegisterInputView?.initKeyboardListener(view)
        initKeyboardListener(view)

        autoFillWithDataFromLatestLoggedIn()

        setupToolbar()
    }

    private fun prepareArgData() {
        arguments?.let {
            val isAutoFill = it.getBoolean(LoginConstants.AutoLogin.IS_AUTO_FILL, false)
            val phone = it.getString(PARAM_PHONE, "")
            val email = it.getString(PARAM_EMAIL, "")
            val method = it.getString(PARAM_LOGIN_METHOD, "")

            isFromRegister = it.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false)

            if (phone.isNotEmpty()) {
                emailPhoneEditText?.setText(phone)
            } else if (email.isNotEmpty()) {
                emailPhoneEditText?.setText(email)
            }

            when {
                isAutoFill -> {
                    emailPhoneEditText?.setText(arguments?.getString(LoginConstants.AutoLogin.AUTO_FILL_EMAIL, ""))
                }
                isAutoLogin -> {
                    when (method) {
                        METHOD_LOGIN_GOOGLE -> onLoginGoogleClick()
                        METHOD_LOGIN_EMAIL -> onLoginEmailClick()
                        else -> {
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.registerCheckResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessRegisterCheck().invoke(it.data)
                is Fail -> onErrorRegisterCheck().invoke(it.throwable)
            }
        })

        viewModel.registerCheckFingerprint.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    onSuccessRegisterCheckFingerprint(it.data.data)
                }
                is Fail -> disableFingerprint()
            }
        })

        viewModel.discoverResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessDiscoverLogin(it.data)
                is Fail -> onErrorDiscoverLogin(it.throwable)
            }
        })

        viewModel.activateResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessActivateUser(it.data)
                is Fail -> onFailedActivateUser(it.throwable)
            }
        })

        viewModel.loginTokenResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessLoginEmail(it.data)
                is Fail -> onErrorLoginEmail(currentEmail).invoke(it.throwable)
            }
        })

        viewModel.loginTokenV2Response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessLoginEmail()
                is Fail -> onErrorLoginEmail(currentEmail).invoke(it.throwable)
            }
        })

        viewModel.profileResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> onErrorGetUserInfo().invoke(it.throwable)
            }
        })

        viewModel.loginTokenAfterSQResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessReloginAfterSQ(it.data)
                is Fail -> onErrorReloginAfterSQ().invoke(it.throwable)
            }
        })

        viewModel.loginTokenGoogleResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> viewModel.getUserInfo()
                is Fail -> onErrorLoginGoogle("").invoke(it.throwable)
            }
        })

        viewModel.getTickerInfoResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetTickerInfo(it.data)
                is Fail -> onErrorGetTickerInfo(it.throwable)
            }
        })

        viewModel.dynamicBannerResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onGetDynamicBannerSuccess(it.data)
                is Fail -> onGetDynamicBannerError(it.throwable)
            }
        })

        viewModel.showLocationAdminPopUp.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> showLocationAdminPopUp()
                is Fail -> showGetAdminTypeError(it.throwable)
            }
        })

        viewModel.loginBiometricResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessLoginBiometric()
                is Fail -> onErrorLoginBiometric(it.throwable)
            }
            dismissLoadingLogin()
        })

        viewModel.goToActivationPageAfterRelogin.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToActivationPageAfterRelogin().invoke(it)
        })

        viewModel.goToActivationPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToActivationPage(it)
        })

        viewModel.showPopup.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showPopup().invoke(it)
        })

        viewModel.goToSecurityQuestion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToSecurityQuestion(it).invoke()
        })

        viewModel.goToSecurityQuestionAfterRelogin.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToSecurityQuestionAfterRelogin().invoke()
        })
    }

    private fun onSuccessRegisterCheckFingerprint(data: RegisterCheckFingerprintResult) {
        activity?.let {
            if (isEnableFingerprint && data.isRegistered) {
                enableFingerprint()
            } else {
                disableFingerprint()
            }
        }
    }

    private fun onSuccessLoginBiometric() {
        analytics.trackOnLoginFingerprintSuccess()
        viewModel.getUserInfo()
    }

    private fun onErrorLoginBiometric(throwable: Throwable) {
        analytics.trackOnLoginFingerprintFailed(throwable.message ?:"")
        onErrorLogin(throwable, LoginErrorCode.ERROR_BIOMETRIC)
    }

    private fun gotoVerifyFingerprint() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalUserPlatform.VERIFY_BIOMETRIC)
        startActivityForResult(intent, LoginConstants.Request.REQUEST_VERIFY_BIOMETRIC)
    }

    private fun onSuccessVerifyFingerprint(validateToken: String) {
        goToChooseAccountPageFingerprint(validateToken)
    }

    override fun goToChooseAccountPageFingerprint(validateToken: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConstInternalGlobal.CHOOSE_ACCOUNT_FINGERPRINT).apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
            }
            startActivityForResult(intent, LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT_FINGERPRINT)
        }
    }

    private fun fetchRemoteConfig() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            isShowTicker = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_TICKER_FROM_ATC, false)
            isShowBanner = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_BANNER, false)
            isEnableFingerprint = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_LOGIN_FP, true)
            isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_REGISTER_PUSH_NOTIF, false)
            isEnableEncryptConfig = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_LOGIN_ENCRYPTION)
            isEnableSilentVerif = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_SILENT_VERIFICATION)
        }
    }

    private fun clearData() {
        userSession.logoutSession()
    }

    private fun onLoginEmailClick() {
        val email = arguments?.getString(LoginConstants.AutoLogin.AUTO_LOGIN_EMAIL, "") ?: ""
        val pw = arguments?.getString(LoginConstants.AutoLogin.AUTO_LOGIN_PASS, "") ?: ""
        partialRegisterInputView?.showLoginEmailView(email)
        emailPhoneEditText?.setText(email)
        wrapper_password?.textFieldInput?.setText(pw)
        loginEmail(email, pw)
        activity?.let {
            analytics.eventClickLoginEmailButton(it.applicationContext)
        }
    }

    private fun loginEmail(email: String, password: String, useHash: Boolean = false) {
        currentEmail = email
        resetError()
        if (isValid(email, password)) {
            showLoadingLogin()
            if (isEnableEncryptConfig() && useHash) {
                viewModel.loginEmailV2(email = email, password = password, useHash = useHash)
            } else {
                viewModel.loginEmail(email, password)
            }
        } else {
            stopTrace()
        }
    }

    private fun loginBiometric(email: String, validateToken: String) {
        if (email.isNotEmpty() && validateToken.isNotEmpty()) {
            showLoadingLogin()
            viewModel.loginTokenBiometric(email, validateToken)
        }
    }

    override fun showLoadingDiscover() {
        LetUtil.ifLet(context, socmedButtonsContainer) { (context, socmedButtonsContainer) ->
            if (context is Context && socmedButtonsContainer is LinearLayout) {
                val pb = LoaderUnify(context)
                val lastPos = socmedButtonsContainer.childCount - 1
                if (socmedButtonsContainer.childCount >= 1 && socmedButtonsContainer.getChildAt(lastPos) !is LoaderUnify) {
                    socmedButtonsContainer.addView(pb, lastPos)
                }
            }
        }
    }

    override fun dismissLoadingDiscover() {
        socmedButtonsContainer?.let {
            val lastPos = it.childCount - 2
            if (it.childCount >= 2 && it.getChildAt(lastPos) is LoaderUnify) {
                it.removeViewAt(lastPos)
            }
        }
    }

    private fun prepareView() {
        partialRegisterInputView?.showForgotPassword()
        socmedBottomSheet = SocmedBottomSheet(context)
        socmedButtonsContainer = socmedBottomSheet?.getSocmedButtonContainer()
        socmedBottomSheet?.setCloseClickListener {
            analytics.eventClickCloseSocmedButton()
            socmedBottomSheet?.dismiss()
        }

        socmedButton?.setOnClickListener {
            analytics.eventClickSocmedButton()
            fragmentManager?.let {
                socmedBottomSheet?.show(it, getString(R.string.bottom_sheet_show))
            }
        }

        checkFingerprintAvailability()

        partialActionButton?.text = getString(R.string.next)
        partialActionButton?.contentDescription = getString(R.string.content_desc_register_btn)
        partialActionButton?.setOnClickListener {
            showLoadingLogin()
            analytics.trackClickOnNext(emailPhoneEditText?.text.toString())
            activity?.let { it1 -> analytics.eventClickLoginEmailButton(it1) }
            registerCheck(emailPhoneEditText?.text.toString())
        }

        wrapper_password?.textFieldInput?.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                loginEmail(emailPhoneEditText?.text.toString().trim(),
                        wrapper_password?.textFieldInput?.text.toString(), useHash = isUseHash)
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

        partialRegisterInputView?.setButtonValidator(true)
        partialRegisterInputView?.findViewById<Typography>(R.id.change_button)?.setOnClickListener {
            val email = emailPhoneEditText?.text.toString()
            onChangeButtonClicked()
            emailPhoneEditText?.setText(email)
        }

        activity?.let { it ->

            register_button.setOnClickListener {
                registerAnalytics.trackClickBottomSignUpButton()
                goToRegisterInitial(source)
            }

            val forgotPassword = partialRegisterInputView?.findViewById<Typography>(R.id.forgot_pass)
            forgotPassword?.text = setUpForgotPasswordTitle()
            forgotPassword?.setOnClickListener {
                if (isUsingInactivePhoneNumber()) {
                    needHelpAnalytics.trackPageClickButuhBantuan()
                    showNeedHelpBottomSheet()
                } else {
                    analytics.trackClickForgotPassword()
                    goToForgotPassword()
                }
            }
        }

        showLoadingDiscover()
        context?.run {
            viewModel.discoverLogin()
        }
    }

    private fun initKeyboardListener(view: View?) {
        view?.run {
            com.tokopedia.loginregister.common.utils.KeyboardHandler(view, object : com.tokopedia.loginregister.common.utils.KeyboardHandler.OnKeyBoardVisibilityChangeListener {
                override fun onKeyboardShow() {
                    callTokopediaCare?.hide()
                }

                override fun onKeyboardHide() {
                    callTokopediaCare?.show()
                }
            })
        }
    }

    private fun checkFingerprintAvailability() {
        if (!GlobalConfig.isSellerApp()) {
            viewModel.registerCheckFingerprint()
        }
    }

    private fun disableFingerprint() {
        fingerprint_btn.hide()
    }

    private fun enableFingerprint() {
        fingerprint_btn?.apply {
            setLeftDrawableForFingerprint()
            show()
            setOnClickListener {
                analytics.trackClickBiometricLoginBtn()
                gotoVerifyFingerprint()
            }
        }
    }

    private fun setLeftDrawableForFingerprint() {
        if (activity != null) {
            val icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_fingerprint_thumb
            )
            fingerprint_btn?.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        }
    }

    private fun setupSpannableText() {
        activity?.let {
            val sourceString = resources.getString(R.string.span_not_have_tokopedia_account)

            val spannable = SpannableString(sourceString)

            spannable.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {

                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                            activity, com.tokopedia.unifyprinciples.R.color.Unify_G400
                    )
                    ds.typeface = Typeface.create("sans-serif", Typeface
                            .NORMAL)
                }
            }, sourceString.indexOf("Daftar"), sourceString.length, 0)

            register_button?.setText(spannable, TextView.BufferType.SPANNABLE)

            initTokopediaCareText()
        }
    }

    private fun initTokopediaCareText() {
        val message = getString(R.string.need_help_call_tokopedia_care)
        val spannable = SpannableString(message)
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        goToTokopediaCareWebview()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                message.indexOf(getString(R.string.call_tokopedia_care)),
                message.indexOf(getString(R.string.call_tokopedia_care)) + getString(R.string.call_tokopedia_care).length,
                0
        )
        callTokopediaCare?.movementMethod = LinkMovementMethod.getInstance()
        callTokopediaCare?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun onChangeButtonClicked() {
        analytics.trackChangeButtonClicked()

        emailPhoneEditText?.imeOptions = EditorInfo.IME_ACTION_DONE

        partialActionButton?.text = getString(R.string.next)
        partialActionButton?.contentDescription = getString(R.string.content_desc_register_btn)
        partialActionButton?.setOnClickListener { registerCheck(emailPhoneEditText?.text.toString()) }
        partialRegisterInputView?.showDefaultView()
    }

    override fun goToForgotPassword() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailPhoneEditText?.text.toString().trim())
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        activity?.applicationContext?.let { analytics.eventClickForgotPasswordFromLogin(it) }
    }

    private fun setUpForgotPasswordTitle(): String?{
        return context?.getString(
            if (isUsingInactivePhoneNumber())
                R.string.loginregister_need_help
            else
                R.string.title_forgot_password
        )
    }

    private fun isUsingInactivePhoneNumber(): Boolean {
        val newInactivePhoneNumberAbTestKey = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
            ROLLENCE_KEY_INACTIVE_PHONE_NUMBER,
            ""
        ).orEmpty()
        return newInactivePhoneNumberAbTestKey.isNotEmpty()
    }

    private fun showNeedHelpBottomSheet(){
        if (needHelpBottomSheetUnify == null)
            needHelpBottomSheetUnify = NeedHelpBottomSheet()

        needHelpBottomSheetUnify?.show(childFragmentManager, TAG_NEED_HELP_BOTTOM_SHEET)
    }

    override fun goToTokopediaCareWebview() {
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH))
    }

    override fun onSuccessDiscoverLogin(discoverData: DiscoverData) {
        stopTrace()
        dismissLoadingDiscover()
        if (discoverData.providers.isNotEmpty()) {
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            layoutParams.setMargins(0, SOCMED_BUTTON_MARGIN_SIZE, 0, SOCMED_BUTTON_MARGIN_SIZE)
            socmedButtonsContainer?.removeAllViews()
            discoverData.providers.forEach { provider ->
                context?.let { context ->
                    val tv = LoginTextView(context, MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                    tv.setText(provider.name)
                    if (userSession.name.isNotEmpty()) {
                        var name = userSession.name
                        if (name.split("\\s".toRegex()).size > 1)
                            name = name.substring(0, name.indexOf(" "))
                        if ((provider.id.equals(LoginConstants.DiscoverLoginId.GPLUS, ignoreCase = true) && userSession.loginMethod == UserSessionInterface.LOGIN_METHOD_GOOGLE)) {
                            tv.setText("${provider.name} ${getString(R.string.socmed_account_as)} $name")
                        }
                    }
                    if (!TextUtils.isEmpty(provider.image)) {
                        tv.setImage(provider.image)
                    }
                    tv.setRoundCorner(SOCMED_BUTTON_CORNER_SIZE)

                    setDiscoverListener(provider, tv)

                    socmedButtonsContainer?.childCount?.let { childCount ->
                        socmedButtonsContainer?.addView(tv, childCount, layoutParams)
                    }
                }
            }
        } else {
            onErrorDiscoverLogin(MessageErrorException(ErrorHandlerSession.getDefaultErrorCodeMessage(
                    ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                    context)))
        }

    }

    private fun setDiscoverListener(provider: ProviderData, tv: LoginTextView) {
        if (provider.id.equals(LoginConstants.DiscoverLoginId.GPLUS, ignoreCase = true)) {
            tv.setOnClickListener { onLoginGoogleClick() }
        }
    }

    private fun onLoginGoogleClick() {
        if (activity != null) {
            onDismissBottomSheet()
            activity?.applicationContext?.let { analytics.eventClickLoginGoogle(it) }

            openGoogleLoginIntent()
        }
    }

    override fun openGoogleLoginIntent() {
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, LoginConstants.Request.REQUEST_LOGIN_GOOGLE)
    }

    private fun onDismissBottomSheet() {
        try {
            socmedBottomSheet?.dismiss()
        } catch (e: Exception) {
        }
    }

    private fun isValid(email: String, password: String): Boolean {

        var isValid = true

        view?.let { view ->
            if (TextUtils.isEmpty(password)) {
                showErrorPassword(R.string.error_field_password_required)
                isValid = false
            } else if (password.length < PASSWORD_MIN_LENGTH) {
                showErrorPassword(R.string.error_incorrect_password)
                isValid = false
            }

            if (TextUtils.isEmpty(email)) {
                showErrorEmail(R.string.error_field_required)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorEmail(R.string.error_invalid_email)
                isValid = false
            }
        }

        return isValid
    }

    override fun showLoadingLogin() {
        showLoading(true)
    }

    override fun dismissLoadingLogin() {
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        val shortAnimTime = context?.resources?.getInteger(android.R.integer.config_shortAnimTime)

        shortAnimTime?.toLong()?.let {
            progressBarLoginWithPhone?.animate()?.setDuration(it)
                    ?.alpha((if (isLoading) 1 else 0).toFloat())
                    ?.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            if (progressBarLoginWithPhone != null) {
                                progressBarLoginWithPhone?.visibility = if (isLoading) View.VISIBLE else View.GONE
                            }
                        }
                    })

            container?.animate()?.setDuration(it)
                    ?.alpha((if (isLoading) 0 else 1).toFloat())
                    ?.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            if (container != null) {
                                container.visibility = if (isLoading) View.GONE else View.VISIBLE
                            }
                        }
                    })
        }
        emailExtension?.hide()
        callTokopediaCare?.showWithCondition(!isLoading)
    }

    override fun goToRegisterInitial(source: String) {
        activity?.let {
            analytics.eventClickRegisterFromLogin()
            var intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.INIT_REGISTER)
            if (GlobalConfig.isSellerApp()) {
                intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
            }
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
            startActivity(intent)
            it.finish()
        }
    }

    private fun getErrorHandlerBuilder(): ErrorHandler.Builder {
        return ErrorHandler.Builder().apply {
            className = this.javaClass.name
        }.build()
    }

    override fun onErrorDiscoverLogin(throwable: Throwable) {
        stopTrace()
        dismissLoadingDiscover()
        val forbiddenMessage = context?.getString(
                com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable, getErrorHandlerBuilder())
        if (errorMessage.removeErrorCode() == forbiddenMessage) {
            onGoToForbiddenPage()
        } else {
            activity?.let {
                NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
                    context?.run {
                        viewModel.discoverLogin()
                    }
                }.showRetrySnackbar()
            }
        }
    }

    override fun onSuccessLoginEmail(loginTokenPojo: LoginTokenPojo?) {
        currentEmail = ""
        viewModel.getUserInfo()
    }

    override fun onSuccessReloginAfterSQ(loginTokenPojo: LoginTokenPojo) {
        refreshRolloutVariant()
        viewModel.getUserInfo()
    }

    override fun onSuccessLogin() {
        dismissLoadingLogin()
        activityShouldEnd = true

        if (emailPhoneEditText?.text?.isNotBlank() == true)
            userSession.autofillUserData = emailPhoneEditText?.text.toString()

        registerPushNotif()

        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                setLoginSuccessSellerApp()
            } else {

                val bundle = Bundle()

                if (isFromRegister) {
                    bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER, true)
                }

                it.setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
                it.finish()
            }

            if (userSession.loginMethod == SeamlessLoginAnalytics.LOGIN_METHOD_SEAMLESS) {
                seamlessAnalytics.eventClickLoginSeamless(SeamlessLoginAnalytics.LABEL_SUCCESS)
            } else {
                analytics.eventSuccessLogin(userSession.loginMethod, isFromRegister, isLoginAfterSq)

                if (isFromChooseAccount) {
                    analytics.eventSuccessLoginFromChooseAccount(userSession.loginMethod, isFromRegister)
                    isFromChooseAccount = false
                }
            }

            setTrackingUserId(userSession.userId)
            setFCM()
            SubmitDeviceWorker.scheduleWorker(it, true)
            DataVisorWorker.scheduleWorker(it, true)
            AppAuthWorker.scheduleWorker(it, true)
            TwoFactorMluHelper.clear2FaInterval(it)
        }

        refreshRolloutVariant()
        saveFirstInstallTime()
    }

    override fun setLoginSuccessSellerApp() {
        view?.run {
            (context.applicationContext as? LoginRouter)?.let {
                it.setOnboardingStatus(true)
                SellerAppWidgetHelper.fetchSellerAppWidgetData(context)
            }
            val intent = if (userSession.hasShop()) {
                RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME)
            } else {
                RouteManager.getIntent(context, LANDING_SHOP_CREATION)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun setFCM() {
        CMPushNotificationManager.instance
                .refreshFCMTokenFromForeground(userSession.deviceId, true)
    }

    private fun setTrackingUserId(userId: String) {
        try {
            TkpdAppsFlyerMapper.getInstance(activity?.applicationContext).mapAnalytics()
            TrackApp.getInstance().gtm.pushUserId(userId)
            val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
            if (!GlobalConfig.DEBUG && crashlytics != null)
                crashlytics.setUserId(userId)

            if (userSession.isLoggedIn) {
                val userData = UserData()
                userData.userId = userSession.userId
                userData.email = userSession.email
                userData.phoneNumber = userSession.phoneNumber
                userData.medium = userSession.loginMethod

                //Identity Event
                LinkerManager.getInstance().sendEvent(
                        LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData))

                //Login Event
                LinkerManager.getInstance().sendEvent(
                        LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData))
                loginEventAppsFlyer(userSession.userId, "")
            }

            if (isFromRegister) {
                TrackApp.getInstance().moEngage.sendMoengageRegisterEvent(
                        "",
                        userSession.userId,
                        "",
                        analytics.getLoginMethodMoengage(userSession.loginMethod),
                        "",
                        userSession.isGoldMerchant,
                        userSession.shopId,
                        userSession.shopName
                )
            } else {
                TrackApp.getInstance().moEngage.setMoEUserAttributesLogin(
                        userSession.userId,
                        "",
                        "",
                        "",
                        userSession.isGoldMerchant,
                        userSession.shopName,
                        userSession.shopId,
                        userSession.hasShop(),
                        analytics.getLoginMethodMoengage(userSession.loginMethod)
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginEventAppsFlyer(userId: String, userEmail: String) {
        val dataMap = HashMap<String, Any>()
        dataMap["user_id"] = userId
        dataMap["user_email"] = userEmail
        val date = Date()
        val stringDate = DateFormat.format("EEEE, MMMM d, yyyy ", date.time)
        dataMap["timestamp"] = stringDate
        TrackApp.getInstance().appsFlyer.sendTrackEvent("Login Successful", dataMap)
    }

    private fun onErrorLogin(throwable: Throwable, flow: String) {
        val errMsg = getErrorMsgWithLogging(throwable, flow)
        analytics.eventFailedLogin(userSession.loginMethod, errMsg.removeErrorCode(), isFromRegister)
        dismissLoadingLogin()
        showToaster(errMsg)
    }

    override fun trackSuccessValidate() {
        analytics.trackClickOnNextSuccess(emailPhoneEditText?.text.toString())
    }

    override fun onErrorValidateRegister(throwable: Throwable) {
        dismissLoadingLogin()
        val message = getErrorMsgWithLogging(throwable, withErrorCode = false, flow = "")
        analytics.trackClickOnNextFail(emailPhoneEditText?.text.toString(), message.removeErrorCode())
        partialRegisterInputView?.onErrorValidate(message)
    }

    override fun onErrorEmptyEmailPhone() {
        dismissLoadingLogin()
        partialRegisterInputView?.onErrorValidate(getString(R.string.must_insert_email_or_phone))
    }

    override fun goToLoginPhoneVerifyPage(phoneNumber: String) {
        analytics.trackLoginPhoneNumber()
        routeToVerifyPage(phoneNumber, LoginConstants.Request.REQUEST_LOGIN_PHONE, LoginConstants.OtpType.OTP_LOGIN_PHONE_NUMBER)
    }

    override fun routeToVerifyPage(phoneNumber: String, requestCode: Int, otpType: Int) {
        activity?.let {
            val intent = goToVerification(phone = phoneNumber, otpType = otpType)
            startActivityForResult(intent, requestCode)
        }
    }

    override fun goToRegisterPhoneVerifyPage(phoneNumber: String) {
        activity?.let {
            val intent = goToVerification(phone = phoneNumber, otpType = LoginConstants.OtpType.OTP_REGISTER_PHONE_NUMBER)
            startActivityForResult(intent, LoginConstants.Request.REQUEST_REGISTER_PHONE)
        }
    }

    override fun onEmailExist(email: String) {
        dismissLoadingLogin()
        partialRegisterInputView?.showLoginEmailView(email)
        partialActionButton?.setOnClickListener {
            loginEmail(email, wrapper_password?.textFieldInput?.text.toString(), useHash = isUseHash)
            activity?.let {
                analytics.eventClickLoginEmailButton(it.applicationContext)
                KeyboardHandler.hideSoftKeyboard(it)
            }
        }
    }

    open fun isEnableEncryptConfig(): Boolean {
        return isEnableEncryptConfig
    }

    override fun showNotRegisteredEmailDialog(email: String, isPending: Boolean) {
        dismissLoadingLogin()
        activity?.let {
            val notRegisteredDialog: DialogUnify? = RegisteredDialog.createNotRegisteredEmailDialog(context, email)
            notRegisteredDialog?.let { dialog ->
                dialog.setPrimaryCTAClickListener {
                    analytics.eventClickYesSmartLoginDialogButton()
                    dialog.dismiss()
                    if (GlobalConfig.isSellerApp()) {
                        goToRegisterInitial(source)
                    } else {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.INIT_REGISTER)
                        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
                        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
                        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SMART_LOGIN, true)
                        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_PENDING, isPending)
                        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
                        it.startActivity(intent)
                        it.finish()
                    }
                }
                dialog.setSecondaryCTAClickListener {
                    analytics.eventClickNoSmartLoginDialogButton()
                    dialog.dismiss()
                    onChangeButtonClicked()
                    emailPhoneEditText?.setText(email)
                    emailPhoneEditText?.setSelection(emailPhoneEditText?.text?.length.toZeroIfNull())
                }
                dialog.show()
            }
        }
    }

    override fun resetError() {
        partialRegisterInputView?.resetErrorWrapper()
    }

    override fun showErrorPassword(resId: Int) {
        partialRegisterInputView?.onErrorPassword(getString(resId))
    }

    override fun showErrorEmail(resId: Int) {
        partialRegisterInputView?.onErrorValidate(getString(resId))
    }

    override fun onErrorLoginEmail(email: String): (Throwable) -> Unit {
        return {
            currentEmail = ""
            stopTrace()
            if (isEmailNotActive(it, email)) {
                onGoToActivationPage(email)
                view?.let { view ->
                    if (!it.message.isNullOrEmpty()) {
                        it.message?.let { message ->
                            Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            } else if (it is AkamaiErrorException) {
                dismissLoadingLogin()
                showPopupErrorAkamai()
            } else if (it is TokenErrorException && !it.errorDescription.isEmpty()) {
                onErrorLogin(it, "${it.errorDescription} - ${LoginErrorCode.ERROR_EMAIL_TOKEN_EXCEPTION}")
            } else {
                val forbiddenMessage = context?.getString(com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
                val errorMessage = ErrorHandler.getErrorMessage(context, it,
                    ErrorHandler.Builder().apply {
                        sendToScalyr = false
                    }.build())
                if (errorMessage.removeErrorCode() == forbiddenMessage) {
                    onGoToForbiddenPage()
                } else {
                    onErrorLogin(it, errorMessage)
                }
            }
        }
    }

    override fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    override fun onSuccessGetUserInfo(profilePojo: ProfilePojo) {
        if (profilePojo.profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
            onGoToChangeName()
        } else {
            onSuccessLogin()
        }
        getDefaultChosenAddress()
    }

    override fun onErrorGetUserInfo(): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(it, LoginErrorCode.ERROR_GET_USER_INFO)
        }
    }

    override fun showLocationAdminPopUp() {
        LocationAdminDialog(context) {
            clearData()
            dismissLoadingLogin()
        }.show()
    }

    override fun showGetAdminTypeError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToaster(errorMessage)
        dismissLoadingLogin()
    }

    override fun onGoToActivationPage(email: String) {
        if (email.isNotEmpty()) {
            val intent = goToVerification(email = email, otpType = LoginConstants.OtpType.OTP_TYPE_ACTIVATE)
            startActivityForResult(intent, LoginConstants.Request.REQUEST_PENDING_OTP_VALIDATE)
        }
    }

    override fun onGoToSecurityQuestion(email: String): () -> Unit {
        return {
            userSession.setTempLoginEmail(email)

            val intent = goToVerification(email = email, otpType = LoginConstants.OtpType.OTP_SECURITY_QUESTION)
            startActivityForResult(intent, LoginConstants.Request.REQUEST_SECURITY_QUESTION)
        }
    }

    //Flow should not be possible
    override fun onGoToActivationPageAfterRelogin(): (MessageErrorException) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(it, LoginErrorCode.ERROR_ACTIVATION_AFTER_RELOGIN)
        }
    }

    //Flow should not be possible
    override fun onGoToSecurityQuestionAfterRelogin(): () -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(MessageErrorException(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context)), LoginErrorCode.ERROR_ACTIVATION_AFTER_RELOGIN)
        }
    }

    override fun onErrorReloginAfterSQ(): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            if (it is AkamaiErrorException) {
                showPopupErrorAkamai()
            } else {
                val errorMessage = ErrorHandler.getErrorMessage(context, it)
                NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
                    viewModel.reloginAfterSQ(tempValidateToken)
                }.showRetrySnackbar()

                analytics.eventFailedLogin(userSession.loginMethod, errorMessage.removeErrorCode())
            }
        }
    }

    override fun onErrorLoginGoogle(email: String?): (Throwable) -> Unit {
        return {
            logoutGoogleAccountIfExist()
            if (it is AkamaiErrorException) {
                dismissLoadingLogin()
                showPopupErrorAkamai()
            } else {
                onErrorLogin(it, LoginErrorCode.ERROR_GMAIL)
            }
        }
    }

    override fun onSuccessActivateUser(activateUserData: ActivateUserData) {
        dismissLoadingLogin()
        userSession.clearToken()
        userSession.setToken(activateUserData.accessToken, activateUserData.tokenType, EncoderDecoder.Encrypt(activateUserData.refreshToken, userSession.refreshTokenIV))
        viewModel.getUserInfo()
    }

    override fun onFailedActivateUser(throwable: Throwable) {
        dismissLoadingLogin()
        onErrorLogin(throwable, LoginErrorCode.ERROR_ACTIVATE_USER)
    }

    protected fun isEmailNotActive(e: Throwable, email: String): Boolean {
        val NOT_ACTIVATED = "belum diaktivasi"
        return (e is TokenErrorException
                && e.errorDescription.isNotEmpty()
                && e.errorDescription
                .toLowerCase(Locale.getDefault()).contains(NOT_ACTIVATED)
                && !TextUtils.isEmpty(email))
    }

    override fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
            startActivityForResult(intent, LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun goToVerification(phone: String = "", email: String = "", otpType: Int): Intent {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            if (requestCode == LoginConstants.Request.REQUEST_LOGIN_GOOGLE && data != null) run {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            } else if (requestCode == LoginConstants.Request.REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK && data != null) {
                data.extras?.let {
                    isLoginAfterSq = true
                    tempValidateToken = it.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                    viewModel.reloginAfterSQ(tempValidateToken)
                }
            } else if (requestCode == LoginConstants.Request.REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
                logoutGoogleAccountIfExist()
                dismissLoadingLogin()
                activity?.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == LoginConstants.Request.REQUEST_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                val uuid = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "") ?: ""
                val msisdn = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                        ?: ""
                validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).toString()
                goToAddNameFromRegisterPhone(uuid, msisdn)
            } else if (requestCode == LoginConstants.Request.REQUEST_ADD_NAME) {
                onSuccessLogin()
            } else if (requestCode == LoginConstants.Request.REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                isAutoLogin = true
                isFromRegister = true
                showLoading(true)
                activityShouldEnd = false
                processAfterAddNameRegisterPhone(data?.extras)
            } else if (requestCode == LoginConstants.Request.REQUEST_LOGIN_PHONE &&
                    resultCode == Activity.RESULT_OK &&
                    data != null &&
                    data.extras != null) {
                data?.extras?.run {
                    val accessToken = getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                    val phoneNumber = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                    goToChooseAccountPage(accessToken, phoneNumber)
                }
            } else if (requestCode == LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                activityShouldEnd = false
                isFromChooseAccount = true

                if (data != null) {
                    data.extras?.let {
                        if (it.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, false)) {
                            onGoToSecurityQuestion("")
                        } else {
                            viewModel.getUserInfo()
                        }
                    }
                } else {
                    viewModel.getUserInfo()
                }
            } else if (requestCode == LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT_FINGERPRINT) {
                if(resultCode == Activity.RESULT_OK) {
                    data?.extras?.let {
                        val email = it.getString(ApplinkConstInternalGlobal.PARAM_EMAIL) ?: ""
                        val token = it.getString(ApplinkConstInternalGlobal.PARAM_TOKEN) ?: ""
                        onSuccessChooseAccountFingerprint(email, token)
                    }
                } else {
                    showToaster(getString(R.string.error_login_fp_error))
                }
            } else if (requestCode == LoginConstants.Request.REQUEST_LOGIN_PHONE || requestCode == LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT) {
                analytics.trackLoginPhoneNumberFailed(getString(R.string.error_login_user_cancel_login_phone))
                dismissLoadingLogin()
            } else if (requestCode == LoginConstants.Request.REQUEST_ADD_PIN_AFTER_REGISTER_PHONE) {
                viewModel.getUserInfo()
            } else if (requestCode == LoginConstants.Request.REQUEST_PENDING_OTP_VALIDATE && resultCode == Activity.RESULT_OK) {
                data?.extras?.let { bundle ->
                    val email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL)
                    val token = bundle.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                    if (!email.isNullOrEmpty() && !token.isNullOrEmpty()) {
                        showLoadingLogin()
                        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
                        viewModel.activateUser(email, token)
                    }
                }
            } else if (requestCode == LoginConstants.Request.REQUEST_VERIFY_BIOMETRIC) {
                if (resultCode == Activity.RESULT_OK && data?.hasExtra(ApplinkConstInternalGlobal.PARAM_TOKEN) == true) {
                    val validateToken = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_TOKEN)
                            ?: ""
                    if (validateToken.isNotEmpty()) {
                        onSuccessVerifyFingerprint(validateToken)
                    } else {
                        onErrorVerifyFingerprint()
                    }
                } else {
                    onErrorVerifyFingerprint()
                }
            } else {
                dismissLoadingLogin()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun onErrorVerifyFingerprint() {
        showToaster(getString(R.string.error_login_fp_error))
    }

    private fun showToaster(message: String?) {
        if(context != null) {
            view?.let {
                Toaster.build(
                    it,
                    message ?: getString(R.string.error_register_webview),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    private fun onSuccessChooseAccountFingerprint(email: String, validateToken: String) {
        loginBiometric(email, validateToken)
    }

    private fun processAfterAddNameRegisterPhone(data: Bundle?) {
        val enable2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA) ?: false
        val enableSkip2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA) ?: false
        analytics.trackerSuccessRegisterFromLogin(userSession.loginMethod)

        if (enable2FA) {
            goToAddPin2FA(enableSkip2FA)
        } else {
            viewModel.getUserInfo()
        }
    }

    override fun goToAddPin2FA(enableSkip2FA: Boolean) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtras(Bundle().apply {
            putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        })
        startActivityForResult(intent, LoginConstants.Request.REQUEST_ADD_PIN_AFTER_REGISTER_PHONE)
    }

    private fun isFromAccountPage(): Boolean = source == LoginConstants.SourcePage.SOURCE_ACCOUNT

    private fun isFromAtcPage(): Boolean = source == LoginConstants.SourcePage.SOURCE_ATC

    override fun goToAddNameFromRegisterPhone(uuid: String, msisdn: String) {
        val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER_CLEAN_VIEW
        val intent = RouteManager.getIntent(context, applink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, msisdn)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
        startActivityForResult(intent, LoginConstants.Request.REQUEST_ADD_NAME_REGISTER_PHONE)
    }

    /**
     * Please refer to the
     * [class reference for][com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes]
     */
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null && account.idToken != null) {
                val accessToken: String = account?.idToken
                val email = account.email
                if (email != null) {
                    showLoadingLogin()
                    viewModel.loginGoogle(accessToken, email)
                } else {
                    onErrorLogin(MessageErrorException(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.EMPTY_EMAIL, context)), LoginErrorCode.ERROR_ON_GMAIL_NULL_EMAIL)
                }
            } else {
                onErrorLogin(MessageErrorException(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.EMPTY_ACCESS_TOKEN, context)), LoginErrorCode.ERROR_ON_GMAIL_CATCH)
            }
        } catch (e: ApiException) {
            onErrorLogin(e, String.format(getString(R.string.loginregister_failed_login_google),
                e.statusCode.toString()))
        }

    }

    override fun onGoToChangeName() {
        if (activity != null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.ADD_NAME_PROFILE)
            startActivityForResult(intent, LoginConstants.Request.REQUEST_ADD_NAME)
        }
    }

    override fun onBackPressed() {
        analytics.trackOnBackPressed()
        if (partialRegisterInputView?.findViewById<Typography>(R.id.change_button)?.visibility == View.VISIBLE) {
            val email = emailPhoneEditText?.text.toString()
            onChangeButtonClicked()
            emailPhoneEditText?.setText(email)
        } else if (activity != null) {
            activity?.finish()
        }
    }

    override fun onSuccessGetTickerInfo(listTickerInfo: List<TickerInfoPojo>) {
        if (listTickerInfo.isNotEmpty()) {
            tickerAnnouncement?.visibility = View.VISIBLE
            if (listTickerInfo.size > 1) {
                val mockData = arrayListOf<TickerData>()
                listTickerInfo.forEach {
                    mockData.add(TickerData(it.title, it.message, getTickerType(it.color), true))
                }
                activity?.let {
                    val adapter = TickerPagerAdapter(it, mockData)
                    adapter.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            analytics.eventClickLinkTicker(linkUrl.toString())
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                        }

                        override fun onDismiss() {
                            analytics.eventClickCloseTicker()
                        }

                    })
                    tickerAnnouncement?.addPagerView(adapter, mockData)
                }
            } else {
                listTickerInfo.first().let {
                    tickerAnnouncement?.tickerTitle = it.title
                    tickerAnnouncement?.setHtmlDescription(it.message)
                    tickerAnnouncement?.tickerShape = getTickerType(it.color)
                }
                tickerAnnouncement?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        analytics.eventClickLinkTicker(linkUrl.toString())
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                        analytics.eventClickCloseTicker()
                    }

                })
            }
            tickerAnnouncement?.setOnClickListener { v ->
                analytics.eventClickTicker()
            }

        }
    }

    override fun onErrorGetTickerInfo(error: Throwable) {
        error.printStackTrace()
    }

    private fun getTickerType(hexColor: String): Int {
        return when (hexColor) {
            colorTickerDefault() -> Ticker.TYPE_ANNOUNCEMENT
            colorTickerWarning() -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    private fun registerCheck(id: String) {
        if (id.isEmpty()) onErrorEmptyEmailPhone()
        else viewModel.registerCheck(id)
    }

    private fun onErrorRegisterCheck(): (Throwable) -> Unit {
        return {
            onErrorValidateRegister(it)
        }
    }

    private fun onSuccessRegisterCheck(): (RegisterCheckData) -> Unit {
        return {
            trackSuccessValidate()

            if (TextUtils.equals(it.registerType, LoginConstants.LoginType.PHONE_TYPE)) {
                userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
                userSession.tempPhoneNumber = partialRegisterInputView?.textValue
                if (it.isExist) {
                    goToLoginPhoneVerifyPage(it.view.replace("-", ""))
                } else {
                    analytics.trackerOnPhoneNumberNotExist()
                    goToRegisterPhoneVerifyPage(it.view.replace("-", ""))
                }
            }

            if (TextUtils.equals(it.registerType, LoginConstants.LoginType.EMAIL_TYPE)) {
                userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL
                if (it.isExist) {
                    if (!it.isPending) {
                        isUseHash = it.useHash
                        userSession.setTempLoginEmail(partialRegisterInputView?.textValue)
                        onEmailExist(it.view)
                    } else {
                        showNotRegisteredEmailDialog(it.view, true)
                    }
                } else {
                    analytics.trackerOnEmailNotExist()
                    showNotRegisteredEmailDialog(it.view, false)
                }
            }
        }
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    LoginConstants.PrefKey.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putLong(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_TIME_SEARCH, 0)?.apply()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        outState.putBoolean(LoginConstants.AutoLogin.IS_AUTO_LOGIN, isAutoLogin)
    }

    private fun logoutGoogleAccountIfExist() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (googleSignInAccount != null) mGoogleSignInClient.signOut()
    }

    override fun onGetDynamicBannerSuccess(dynamicBannerDataModel: DynamicBannerDataModel) {
        if (dynamicBannerDataModel.banner.isEnable) {
            context?.run {
                bannerLogin?.let { banner ->
                    ImageUtils.loadImage(
                            imageView = banner,
                            url = dynamicBannerDataModel.banner.imgUrl,
                            imageLoaded = {
                                if (it) {
                                    bannerLogin?.show()
                                    analytics.eventViewBanner(dynamicBannerDataModel.banner.imgUrl)
                                } else {
                                    bannerLogin?.hide()
                                    showTicker()
                                }
                            })
                }
            }
        } else {
            showTicker()
        }
    }

    override fun onGetDynamicBannerError(throwable: Throwable) {
        bannerLogin?.hide()
        showTicker()
    }

    private fun showTicker() {
        if (!GlobalConfig.isSellerApp()) {
            if (isFromAtcPage() && isShowTicker) {
                tickerAnnouncement?.visibility = View.VISIBLE
                tickerAnnouncement?.tickerTitle = getString(R.string.title_ticker_from_atc)
                tickerAnnouncement?.setTextDescription(String.format(getString(R.string.desc_ticker_from_atc)))
                tickerAnnouncement?.tickerShape = Ticker.TYPE_ANNOUNCEMENT
                tickerAnnouncement?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                    override fun onDismiss() {
                        analytics.eventClickCloseTicker()
                    }
                })
                tickerAnnouncement?.setOnClickListener {
                    analytics.eventClickTicker()
                }
            } else {
                viewModel.getTickerInfo()
            }
        }
    }

    private fun registerPushNotif() {
        if (isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                RegisterPushNotificationWorker.scheduleWorker(it)
            }
        }
    }

    private fun getDefaultChosenAddress() {
        activity?.let {
            GetDefaultChosenAddressService.startService(it.applicationContext)
        }
    }

    override fun showPopup(): (PopupError) -> Unit {
        return {
            dismissLoadingLogin()
            showPopupError(
                    it.header,
                    it.body,
                    it.action
            )
        }
    }

    private fun showPopupError(header: String, body: String, url: String) {
        context?.let {
            PopupErrorDialog.createDialog(context, header, body, url)?.show()
        }
    }

    private fun showPopupErrorAkamai() {
        showPopupError(
                getString(R.string.popup_error_title),
                getString(R.string.popup_error_desc),
                getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH
        )
        getErrorMsgWithLogging(MessageErrorException("Akamai Error"), "Login")
    }

    private fun getErrorMsgWithLogging(throwable: Throwable, flow: String, withErrorCode: Boolean = true): String {
        val mClassName = if(flow.isEmpty()) LoginEmailPhoneFragment::class.java.name else "${LoginEmailPhoneFragment::class.java.name} - $flow"
        val message = ErrorHandler.getErrorMessage(context, throwable,
            ErrorHandler.Builder().apply {
                withErrorCode(withErrorCode)
                className = mClassName
            }.build())
        return message
    }

    private fun autoFillWithDataFromLatestLoggedIn() {
        if (!userSession.autofillUserData.isNullOrEmpty() && emailPhoneEditText?.text?.isEmpty() == true) {
            emailPhoneEditText?.let {
                it.setText(userSession.autofillUserData)
                it.setSelection(it.text.length)
            }
        }
    }

    private fun colorTickerDefault(): String {
        return getString(R.string.color_ticker_default)
    }

    private fun colorTickerWarning(): String {
        return getString(R.string.color_ticker_warning)
    }

    companion object {

        private const val ROLLENCE_KEY_INACTIVE_PHONE_NUMBER = "inactivephone_login"

        private const val TAG_NEED_HELP_BOTTOM_SHEET = "NEED HELP BOTTOM SHEET"

        private const val LOGIN_LOAD_TRACE = "gb_login_trace"
        private const val LOGIN_SUBMIT_TRACE = "gb_submit_login_trace"
        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"
        const val TOKOPEDIA_CARE_PATH = "help"

        private const val PASSWORD_MIN_LENGTH = 4

        private const val SOCMED_BUTTON_MARGIN_SIZE = 10
        private const val SOCMED_BUTTON_CORNER_SIZE = 10

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginEmailPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
