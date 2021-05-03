package com.tokopedia.loginregister.login.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.hardware.fingerprint.FingerprintManager
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
import android.widget.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
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
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_FACEBOOK
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_GOOGLE
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.listener.ScanFingerprintInterface
import com.tokopedia.loginfingerprint.view.ScanFingerprintDialog
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.utils.SellerAppWidgetHelper
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.common.view.PartialRegisterInputView
import com.tokopedia.loginregister.common.view.banner.DynamicBannerConstant
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.common.view.dialog.PopupErrorDialog
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.login.di.LoginComponentBuilder
import com.tokopedia.loginregister.login.domain.StatusFingerprint
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.router.LoginRouter
import com.tokopedia.loginregister.login.service.GetDefaultChosenAddressService
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_EMAIL
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_LOGIN_METHOD
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_PHONE
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.loginthirdparty.google.SmartLockActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
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
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
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
open class LoginEmailPhoneFragment : BaseDaggerFragment(), ScanFingerprintInterface, LoginEmailPhoneContract.View {

    private var isTraceStopped: Boolean = false
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private lateinit var callbackManager: CallbackManager
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
    lateinit var fingerprintPreferenceHelper: FingerprintSetting

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    private var source: String = ""
    protected var isAutoLogin: Boolean = false
    protected var isEnableSmartLock = true
    private var isShowTicker: Boolean = false
    private var isShowBanner: Boolean = false
    protected var isEnableFingerprint = true
    private var isHitRegisterPushNotif: Boolean = false
    private var isEnableEncryptConfig: Boolean = false
    private var activityShouldEnd = true
    private var isFromRegister = false
    private var isUseHash = false
    private var validateToken = ""

    private lateinit var remoteConfigInstance: RemoteConfigInstance
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

    override fun getScreenName(): String {
        return LoginRegisterAnalytics.SCREEN_LOGIN
    }

    override fun initInjector() {
        activity?.application?.let { LoginComponentBuilder.getComponent(it).inject(this) }
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
            menu.add(Menu.NONE, ID_ACTION_DEVOPS, 1, devOpsText)
            menu.findItem(ID_ACTION_DEVOPS).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
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
        if (id == ID_ACTION_DEVOPS) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
                return true
            }
        }
        if (id == ID_ACTION_REGISTER) {
            registerAnalytics.trackClickTopSignUpButton()
            goToRegisterInitial(source)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackgroundColor()
        callbackManager = CallbackManager.Factory.create()
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
        isAutoLogin = getParamBoolean(IS_AUTO_LOGIN, arguments, savedInstanceState, false)
        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)
    }

    private fun setupBackgroundColor() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                    MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRemoteConfig()
        clearData()
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

        setupToolbar()
    }

    private fun prepareArgData() {
        arguments?.let {
            val isAutoFill = it.getBoolean(IS_AUTO_FILL, false)
            val phone = it.getString(PARAM_PHONE, "")
            val email = it.getString(PARAM_EMAIL, "")
            val method = it.getString(PARAM_LOGIN_METHOD, "")

            if (phone.isNotEmpty()) {
                emailPhoneEditText?.setText(phone)
            } else if(email.isNotEmpty()) {
                emailPhoneEditText?.setText(email)
            }

            when {
                isAutoFill -> {
                    emailPhoneEditText?.setText(arguments?.getString(AUTO_FILL_EMAIL, ""))
                }
                isAutoLogin -> {
                    when(method) {
                        METHOD_LOGIN_FACEBOOK -> onLoginFacebookClick()
                        METHOD_LOGIN_GOOGLE -> onLoginGoogleClick()
                        METHOD_LOGIN_EMAIL -> onLoginEmailClick()
                        else -> showSmartLock()
                    }
                }
                else -> showSmartLock()
            }
        }
    }

    private fun initObserver(){
        viewModel.registerCheckResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessRegisterCheck().invoke(it.data)
                is Fail -> onErrorRegisterCheck().invoke(it.throwable)
            }
        })

        viewModel.discoverResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessDiscoverLogin(it.data.providers)
                is Fail -> onErrorDiscoverLogin(it.throwable)
            }
        })

        viewModel.activateResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessActivateUser(it.data)
                is Fail -> onFailedActivateUser(it.throwable)
            }
        })

        viewModel.loginTokenResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessLoginEmail(it.data)
                is Fail -> onErrorLoginEmail(currentEmail).invoke(it.throwable)
            }
        })

        viewModel.loginTokenV2Response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessLoginEmail()
                is Fail -> onErrorLoginEmail(currentEmail).invoke(it.throwable)
            }
        })

        viewModel.profileResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessGetUserInfo().invoke(it.data)
                is Fail -> onErrorGetUserInfo().invoke(it.throwable)
            }
        })

        viewModel.loginTokenAfterSQResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessReloginAfterSQ(it.data)
                is Fail -> onErrorReloginAfterSQ().invoke(it.throwable)
            }
        })

        viewModel.getFacebookCredentialResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessGetFacebookCredential(it.data)
                is Fail -> onErrorGetFacebookCredential(it.throwable)
            }
        })

        viewModel.loginTokenFacebookResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> viewModel.getUserInfo()
                is Fail -> onErrorLoginFacebook("").invoke(it.throwable)
            }
        })

        viewModel.loginTokenFacebookPhoneResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessLoginFacebookPhone().invoke(it.data)
                is Fail -> onErrorLoginFacebookPhone().invoke(it.throwable)
            }
        })

        viewModel.loginTokenGoogleResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> viewModel.getUserInfo()
                is Fail -> onErrorLoginGoogle("").invoke(it.throwable)
            }
        })

        viewModel.getTickerInfoResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessGetTickerInfo(it.data)
                is Fail -> onErrorGetTickerInfo(it.throwable)
            }
        })

        viewModel.dynamicBannerResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> onGetDynamicBannerSuccess(it.data)
                is Fail -> onGetDynamicBannerError(it.throwable)
            }
        })

        viewModel.showLocationAdminPopUp.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Success -> showLocationAdminPopUp()
                is Fail -> showGetAdminTypeError(it.throwable)
            }
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

    private fun fetchRemoteConfig() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            isShowTicker = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_TICKER_FROM_ATC, false)
            isShowBanner = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_BANNER, false)
            isEnableFingerprint = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_LOGIN_FP, true)
            isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF, false)
            isEnableEncryptConfig = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_LOGIN_ENCRYPTION)
        }
    }

    private fun clearData() {
        userSession.logoutSession()
    }

    private fun onLoginEmailClick() {
        val email = arguments?.getString(AUTO_LOGIN_EMAIL, "") ?: ""
        val pw = arguments?.getString(AUTO_LOGIN_PASS, "") ?: ""
        partialRegisterInputView?.showLoginEmailView(email)
        emailPhoneEditText?.setText(email)
        wrapper_password?.textFieldInput?.setText(pw)
        loginEmail(email, pw)
        activity?.let {
            analytics.eventClickLoginEmailButton(it.applicationContext)
        }

    }

    private fun loginEmail(email: String, password: String, isSmartLock: Boolean = false, useHash: Boolean = false) {
        showLoadingLogin()
        currentEmail = email
        resetError()
        if(isValid(email, password)) {
            if(isEnableEncryption() && useHash) {
                viewModel.loginEmailV2(email = email, password = password, isSmartLock = isSmartLock, useHash = useHash)
            }else {
                viewModel.loginEmail(email, password, isSmartLock = isSmartLock)
            }
        } else {
            stopTrace()
        }
    }


    private fun showSmartLock() {
        if (isEnableSmartLock) {
            val intent = Intent(activity, SmartLockActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(SmartLockActivity.STATE, SmartLockActivity.RC_READ)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_SMART_LOCK)
        }
    }

    private fun doLoginAfterSmartLock(data: Intent) {
        val username = data.extras?.getString(SmartLockActivity.USERNAME).orEmpty()
        val password = data.extras?.getString(SmartLockActivity.PASSWORD).orEmpty()

        emailPhoneEditText?.let {
            it.setText(username)
            it.setSelection(it.text.length)
        }

        loginEmail(username, password, isSmartLock = true)
        analytics.eventClickSmartLock(activity?.applicationContext)
    }

    override fun showLoadingDiscover() {
        LetUtil.ifLet(context, socmedButtonsContainer) { (context, socmedButtonsContainer) ->
            if(context is Context && socmedButtonsContainer is LinearLayout) {
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

        if (isEnableFingerprint && ScanFingerprintDialog.isFingerprintAvailable(activity) && fingerprintPreferenceHelper.isFingerprintRegistered()) {
            activity?.let {
                val icon = VectorDrawableCompat.create(it.resources, R.drawable.ic_fingerprint_thumb, it.theme)
                fingerprint_btn.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
            }

            fingerprint_btn.visibility = View.VISIBLE
            fingerprint_btn.setOnClickListener {
                activity?.let { act ->
                    ScanFingerprintDialog(act, this@LoginEmailPhoneFragment).showWithMode(ScanFingerprintDialog.MODE_LOGIN)
                }
            }
        }

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
        partialRegisterInputView?.findViewById<TextView>(R.id.change_button)?.setOnClickListener {
            val email = emailPhoneEditText?.text.toString()
            onChangeButtonClicked()
            emailPhoneEditText?.setText(email)
            emailPhoneEditText?.setSelection(emailPhoneEditText?.text?.length.orZero())
        }

        activity?.let { it ->

            register_button.setOnClickListener {
                registerAnalytics.trackClickBottomSignUpButton()
                goToRegisterInitial(source)
            }

            val forgotPassword = partialRegisterInputView?.findViewById<TextView>(R.id.forgot_pass)
            forgotPassword?.setOnClickListener {
                analytics.trackClickForgotPassword()
                goToForgotPassword()
            }
        }

        showLoadingDiscover()
        context?.run {
            viewModel.discoverLogin()
        }
    }

    private fun setupSpannableText(){
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
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
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

    override fun goToTokopediaCareWebview() {
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH))
    }

    override fun onSuccessDiscoverLogin(providers: ArrayList<DiscoverItemDataModel>) {
        stopTrace()
        dismissLoadingDiscover()
        if (providers.isNotEmpty()) {
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            layoutParams.setMargins(0, 10, 0, 10)
            socmedButtonsContainer?.removeAllViews()
            providers.forEach {
                val tv = LoginTextView(activity, MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                tv.tag = it.id
                tv.setText(it.name)
                if (userSession.name.isNotEmpty()) {
                    var name = userSession.name
                    if (name.split("\\s".toRegex()).size > 1)
                        name = name.substring(0, name.indexOf(" "))
                    if ((it.id.equals(FACEBOOK, ignoreCase = true) && userSession.loginMethod == UserSessionInterface.LOGIN_METHOD_FACEBOOK) ||
                        (it.id.equals(GPLUS, ignoreCase = true) && userSession.loginMethod == UserSessionInterface.LOGIN_METHOD_GOOGLE)) {
                        tv.setText("${it.name} ${getString(R.string.socmed_account_as)} $name")
                    }
                }
                if (!TextUtils.isEmpty(it.image)) {
                    tv.setImage(it.image)
                } else if (it.imageResource != 0) {
                    tv.setImageResource(it.imageResource)
                }
                tv.setRoundCorner(10)

                setDiscoverListener(it, tv)
                socmedButtonsContainer?.childCount?.let {
                    childCount -> socmedButtonsContainer?.addView(tv, childCount, layoutParams)
                }
            }
        } else {
            onErrorDiscoverLogin(MessageErrorException(ErrorHandlerSession.getDefaultErrorCodeMessage(
                    ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                    context)))
        }

    }

    private fun setDiscoverListener(discoverItemDataModel: DiscoverItemDataModel,
                                    tv: LoginTextView) {

        if (discoverItemDataModel.id.equals(FACEBOOK, ignoreCase = true)) {
            tv.setOnClickListener { onLoginFacebookClick() }
        } else if (discoverItemDataModel.id.equals(GPLUS, ignoreCase = true)) {
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

    override fun openGoogleLoginIntent(){
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, REQUEST_LOGIN_GOOGLE)
    }

    private fun onLoginFacebookClick() {
        if (activity != null) {
            onDismissBottomSheet()
            activity?.let { analytics.eventClickLoginFacebook(it) }
            viewModel.getFacebookCredential(this, callbackManager)
        }
    }

    private fun onDismissBottomSheet() {
        try {
            socmedBottomSheet?.dismiss()
        } catch (e: Exception) { }
    }

    private fun onSuccessGetFacebookCredential(facebookCredentialData: FacebookCredentialData) {
        try {
            showLoadingLogin()
            if (facebookCredentialData.email.isNotEmpty()) {
                viewModel.loginFacebook(
                        facebookCredentialData.accessToken,
                        facebookCredentialData.email
                )
            } else if (facebookCredentialData.phone.isNotEmpty()) {
                viewModel.loginFacebookPhone(
                        facebookCredentialData.accessToken,
                        facebookCredentialData.phone
                )
            }
        } catch (e: Exception) {
            e.message?.let { onErrorLogin(it) }
        }
    }

    private fun onErrorGetFacebookCredential(errorMessage: Throwable?){
        dismissLoadingLogin()
        if (isAdded && activity != null) {
            onErrorLogin(ErrorHandler.getErrorMessage(context, errorMessage))
        }
    }

    private fun isValid(email: String, password: String): Boolean {

        var isValid = true

        view?.let { view ->
            if (TextUtils.isEmpty(password)) {
                showErrorPassword(R.string.error_field_password_required)
                isValid = false
            } else if (password.length < 4) {
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

    override fun getFacebookCredentialListener(): GetFacebookCredentialSubscriber.GetFacebookCredentialListener {
        return object : GetFacebookCredentialSubscriber.GetFacebookCredentialListener {

            override fun onErrorGetFacebookCredential(errorMessage: Exception?) {
                dismissLoadingLogin()
                if (isAdded && activity != null) {
                    onErrorLogin(ErrorHandler.getErrorMessage(context, errorMessage))
                }
            }

            override fun onSuccessGetFacebookEmailCredential(accessToken: AccessToken?, email: String?) {
                context?.run {
                    LetUtil.ifLet(context, accessToken, email) { (context, accessToken, email) ->
                        viewModel.loginFacebook(accessToken as AccessToken, email as String)
                    }
                }
            }

            override fun onSuccessGetFacebookPhoneCredential(accessToken: AccessToken?, phone: String?) {
                context?.run {
                    LetUtil.ifLet(context, accessToken, phone) { (context, accessToken, phone) ->
                        viewModel.loginFacebookPhone(accessToken as AccessToken, phone as String)
                    }
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

    override fun onErrorDiscoverLogin(throwable: Throwable) {
        stopTrace()
        dismissLoadingDiscover()
        val forbiddenMessage = context?.getString(
                com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        if (errorMessage == forbiddenMessage) {
            onGoToForbiddenPage()
        } else {
            activity?.let {
                NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage)
                {
                    context?.run {
                        viewModel.discoverLogin()
                    }
                }.showRetrySnackbar()
            }
        }
    }

    override fun onLoginFingerprintSuccess() {
        viewModel.getUserInfo()
    }

    override fun onSuccessLoginEmail(loginTokenPojo: LoginTokenPojo?) {
        currentEmail = ""
        setSmartLock()
        viewModel.getUserInfo()
    }

    override fun onSuccessReloginAfterSQ(loginTokenPojo: LoginTokenPojo) {
        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)
        viewModel.getUserInfo()
    }

    override fun onSuccessLogin() {
        dismissLoadingLogin()
        activityShouldEnd = true

        if (userSession.userId != fingerprintPreferenceHelper.getFingerprintUserId()) {
            removeFingerprintData()
        }

        if (emailPhoneEditText?.text?.isNotBlank() == true)
            userSession.autofillUserData = emailPhoneEditText?.text.toString()

        registerPushNotif()

        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                setLoginSuccessSellerApp()
            } else {
                it.setResult(Activity.RESULT_OK)
                it.finish()
            }

            if (userSession.loginMethod == SeamlessLoginAnalytics.LOGIN_METHOD_SEAMLESS) {
                seamlessAnalytics.eventClickLoginSeamless(SeamlessLoginAnalytics.LABEL_SUCCESS)
            } else {
                analytics.eventSuccessLogin(userSession.loginMethod, isFromRegister())
            }

            setTrackingUserId(userSession.userId)
            setFCM()
            SubmitDeviceWorker.scheduleWorker(it, true)
            DataVisorWorker.scheduleWorker(it, true)
            AppAuthWorker.scheduleWorker(it, true)
            TwoFactorMluHelper.clear2FaInterval(it)
        }

        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)

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

    fun onErrorLogin(errorMessage: String?) {
        analytics.eventFailedLogin(userSession.loginMethod, errorMessage)

        dismissLoadingLogin()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun isFromRegister(): Boolean {
        return arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false) ?: false
    }

    override fun trackSuccessValidate() {
        analytics.trackClickOnNextSuccess(emailPhoneEditText?.text.toString())
    }

    override fun onErrorValidateRegister(throwable: Throwable) {
        dismissLoadingLogin()
        val message = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable)
        analytics.trackClickOnNextFail(emailPhoneEditText?.text.toString(), message)
        partialRegisterInputView?.onErrorValidate(message)
    }

    override fun onErrorEmptyEmailPhone() {
        dismissLoadingLogin()
        partialRegisterInputView?.onErrorValidate(getString(R.string.must_insert_email_or_phone))
    }

    override fun goToLoginPhoneVerifyPage(phoneNumber: String) {
        analytics.trackLoginPhoneNumber()
        routeToVerifyPage(phoneNumber, REQUEST_LOGIN_PHONE, OTP_LOGIN_PHONE_NUMBER)
    }

    override fun routeToVerifyPage(phoneNumber: String, requestCode: Int, otpType: Int){
        activity?.let {
            val intent =  goToVerification(phone = phoneNumber, otpType = otpType)
            startActivityForResult(intent, requestCode)
        }
    }

    override fun goToRegisterPhoneVerifyPage(phoneNumber: String) {
        activity?.let {
            val intent = goToVerification(phone = phoneNumber, otpType = OTP_REGISTER_PHONE_NUMBER)
            startActivityForResult(intent, REQUEST_REGISTER_PHONE)
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

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    fun isEnableEncryptRollout(): Boolean {
        val rolloutKey = if(GlobalConfig.isSellerApp()) {
            SessionConstants.Rollout.ROLLOUT_LOGIN_ENCRYPTION_SELLER
        } else {
            SessionConstants.Rollout.ROLLOUT_LOGIN_ENCRYPTION
        }

        val variant = getAbTestPlatform().getString(rolloutKey)
        return variant.isNotEmpty()
    }

    fun isEnableEncryptConfig(): Boolean {
        return isEnableEncryptConfig
    }

    fun isEnableEncryption(): Boolean {
        return isEnableEncryptRollout() && isEnableEncryptConfig()
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

    override fun setSmartLock() {
        wrapper_password?.textFieldInput?.text?.let {
            if (emailPhoneEditText?.text?.isNotBlank() == true && it.isNotBlank()) {
                saveSmartLock(SmartLockActivity.RC_SAVE_SECURITY_QUESTION,
                        emailPhoneEditText?.text.toString(),
                        it.toString())
            }
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
                onErrorLogin(it.errorDescription)
            } else {
                val forbiddenMessage = context?.getString(
                        com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
                val errorMessage = ErrorHandler.getErrorMessage(context, it)
                if (errorMessage == forbiddenMessage) {
                    onGoToForbiddenPage()
                } else {
                    onErrorLogin(errorMessage)

                    context?.run {
                        if (!TextUtils.isEmpty(it.message)
                                && errorMessage.contains(this.getString(R.string
                                        .default_request_error_unknown))) {
                            analytics.logUnknownError(it)
                        }
                    }
                }
            }
        }
    }

    override fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    override fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit {
        return {
            if (it.profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
                onGoToChangeName()
            } else {
                onSuccessLogin()
            }
            getDefaultChosenAddress()
        }
    }

    override fun onErrorGetUserInfo(): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(ErrorHandler.getErrorMessage(context, it))
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
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
        dismissLoadingLogin()
    }

    override fun onGoToActivationPage(email: String) {
        if(email.isNotEmpty()) {
            val intent = goToVerification(email = email, otpType = OTP_TYPE_ACTIVATE)
            startActivityForResult(intent, REQUEST_PENDING_OTP_VALIDATE)
        }
    }

    override fun onGoToSecurityQuestion(email: String): () -> Unit {
        return {
            userSession.setTempLoginEmail(email)

            val intent =  goToVerification(email = email, otpType = OTP_SECURITY_QUESTION)
            startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
        }
    }

    //Flow should not be possible
    override fun onGoToActivationPageAfterRelogin(): (MessageErrorException) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(ErrorHandler.getErrorMessage(context, it))
        }
    }

    //Flow should not be possible
    override fun onGoToSecurityQuestionAfterRelogin(): () -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context))
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

                analytics.eventFailedLogin(userSession.loginMethod, errorMessage)
            }
        }
    }

    override fun onErrorLoginFacebook(email: String): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            if (it is AkamaiErrorException) {
                showPopupErrorAkamai()
            } else {
                onErrorLogin(ErrorHandler.getErrorMessage(context, it))
            }
        }
    }

    override fun onSuccessLoginFacebookPhone(): (LoginTokenPojo) -> Unit {
        return {
            if (it.loginToken.action == 1) {
                goToChooseAccountPageFacebook(it.loginToken.accessToken)
            } else {
                viewModel.getUserInfo()
            }
        }
    }

    override fun onErrorLoginFacebookPhone(): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            if (it is AkamaiErrorException) {
                showPopupErrorAkamai()
            } else {
                onErrorLogin(ErrorHandler.getErrorMessage(context, it))
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
                onErrorLogin(ErrorHandler.getErrorMessage(context, it))
            }
        }
    }

    override fun onSuccessActivateUser(activateUserData: ActivateUserData) {
        dismissLoadingLogin()
        userSession.clearToken()
        userSession.setToken(activateUserData.accessToken, activateUserData.tokenType, activateUserData.refreshToken)
        viewModel.getUserInfo()
    }

    override fun onFailedActivateUser(throwable: Throwable) {
        dismissLoadingLogin()
        onErrorLogin(ErrorHandler.getErrorMessage(context, throwable))
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
        if (activity != null){
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    override fun goToChooseAccountPageFacebook(accessToken: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, FACEBOOK_LOGIN_TYPE)

            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
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
            callbackManager.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_SMART_LOCK && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null
                    && data.extras?.getString(SmartLockActivity.USERNAME) != null
                    && data.extras?.getString(SmartLockActivity.PASSWORD) != null) {
                doLoginAfterSmartLock(data)
            } else if (requestCode == REQUEST_SMART_LOCK
                    && resultCode == SmartLockActivity.RC_READ
                    && !userSession.autofillUserData.isNullOrEmpty()) {
                        emailPhoneEditText?.let {
                            it.setText(userSession.autofillUserData)
                            it.setSelection(it.text.length)
                        }
            } else if (requestCode == REQUEST_LOGIN_GOOGLE && data != null) run {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            } else if (requestCode == REQUEST_SECURITY_QUESTION
                    && resultCode == Activity.RESULT_OK
                    && data != null) {
                data.extras?.let {
                    tempValidateToken = it.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                    viewModel.reloginAfterSQ(tempValidateToken)
                }
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
                logoutGoogleAccountIfExist()
                dismissLoadingLogin()
                activity?.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
                analytics.eventFailedLogin(userSession.loginMethod, getString(R.string.error_login_user_cancel_create_password))
                dismissLoadingLogin()
                activity?.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_CANCELED) {
                analytics.eventFailedLogin(userSession.loginMethod, getString(R.string.error_login_user_cancel_activate_account))
                dismissLoadingLogin()
                activity?.setResult(Activity.RESULT_CANCELED)
            } else if (requestCode == REQUEST_VERIFY_PHONE) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_REGISTER_PHONE
                    && resultCode == Activity.RESULT_OK && data != null
                    && data.extras != null) {
                val uuid = data.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "") ?: ""
                val msisdn = data.extras?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "") ?: ""
                validateToken = data.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).toString()
                goToAddNameFromRegisterPhone(uuid, msisdn)
            } else if (requestCode == REQUEST_ADD_NAME) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                isAutoLogin = true
                isFromRegister = true
                showLoading(true)
                activityShouldEnd = false
                processAfterAddNameRegisterPhone(data?.extras)
            } else if (requestCode == REQUEST_LOGIN_PHONE
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.extras != null) {
                data.extras?.run {
                    val accessToken = getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                    val phoneNumber = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                    goToChooseAccountPage(accessToken, phoneNumber)
                }
            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT
                    && resultCode == Activity.RESULT_OK) {
                activityShouldEnd = false
                if (data != null) {
                    data.extras?.let {
                        if (it.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, false)) {
                            onGoToSecurityQuestion("")
                        } else {
                            onSuccessLogin()
                        }
                    }
                } else {
                    onSuccessLogin()
                }

            } else if (requestCode == REQUEST_LOGIN_PHONE
                    || requestCode == REQUEST_CHOOSE_ACCOUNT) {
                analytics.trackLoginPhoneNumberFailed(getString(R.string.error_login_user_cancel_login_phone))
                dismissLoadingLogin()
            } else if (requestCode == REQUEST_ADD_PIN) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ADD_PIN_AFTER_SQ) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_COTP_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ADD_PIN_AFTER_REGISTER_PHONE) {
                viewModel.getUserInfo()
            } else if (requestCode == REQUEST_PENDING_OTP_VALIDATE && resultCode == Activity.RESULT_OK) {
                data?.extras?.let { bundle ->
                    val email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL)
                    val token = bundle.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                    if (!email.isNullOrEmpty() && !token.isNullOrEmpty()) {
                        showLoadingLogin()
                        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
                        viewModel.activateUser(email, token)
                    }
                }
            } else {
                dismissLoadingLogin()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun processAfterAddNameRegisterPhone(data: Bundle?) {
        val enable2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA) ?: false
        val enableSkip2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA) ?: false
        if (enable2FA) {
            goToAddPin2FA(enableSkip2FA)
        } else {
            viewModel.getUserInfo()
        }
    }

    override fun goToAddPin2FA(enableSkip2FA: Boolean){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtras(Bundle().apply {
            putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        })
        startActivityForResult(intent, REQUEST_ADD_PIN_AFTER_REGISTER_PHONE)
    }

    private fun isFromAccountPage(): Boolean = source == SOURCE_ACCOUNT

    private fun isFromAtcPage(): Boolean = source == SOURCE_ATC

    override fun goToAddNameFromRegisterPhone(uuid: String, msisdn: String) {
        val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER_CLEAN_VIEW
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
                val accessToken: String = account?.idToken
                val email = account.email
                if (email != null) {
                    showLoadingLogin()
                    viewModel.loginGoogle(accessToken, email)
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

    override fun onGoToChangeName() {
        if (activity != null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.ADD_NAME_PROFILE)
            startActivityForResult(intent, REQUEST_ADD_NAME)
        }
    }

    override fun onBackPressed() {
        analytics.trackOnBackPressed()
        if (partialRegisterInputView?.findViewById<TextView>(R.id.change_button)?.visibility == View.VISIBLE) {
            val email = emailPhoneEditText?.text.toString()
            onChangeButtonClicked()
            emailPhoneEditText?.let {
                it.setText(email)
                it.setSelection(it.text.length)
            }
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
            tickerAnnouncement ?.setOnClickListener { v ->
                analytics.eventClickTicker()
            }

        }
    }

    override fun onErrorGetTickerInfo(error: Throwable) {
        error.printStackTrace()
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

    override fun onErrorCheckStatusFingerprint(e: Throwable) {
        dismissLoadingLogin()
        e.printStackTrace()
    }

    override fun onSuccessCheckStatusFingerprint(data: StatusFingerprint) {
        dismissLoadingLogin()
        onSuccessLogin()
        if (!data.isValid && isFromAccountPage()) {
            goToFingerprintRegisterPage()
        }
    }

    override fun goToFingerprintRegisterPage() {
        context?.run {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_FINGERPRINT_ONBOARDING)
            startActivity(intent)
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

            if (TextUtils.equals(it.registerType, PHONE_TYPE)) {
                userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
                userSession.tempPhoneNumber = partialRegisterInputView?.textValue
                if (it.isExist) {
                    goToLoginPhoneVerifyPage(it.view.replace("-", ""))
                } else {
                    goToRegisterPhoneVerifyPage(it.view.replace("-", ""))
                }
            }

            if (TextUtils.equals(it.registerType, EMAIL_TYPE)) {
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
                    showNotRegisteredEmailDialog(it.view, false)
                }
            }
        }
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putLong(
                    KEY_FIRST_INSTALL_TIME_SEARCH, 0)?.apply()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        outState.putBoolean(IS_AUTO_LOGIN, isAutoLogin)
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
            activity?.let {
                RegisterPushNotifService.startService(it.applicationContext)
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
    }

    override fun onFingerprintValid() {}

    override fun onFingerprintError(msg: String, errCode: Int) {
        if (errCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
            view?.run {
                Toaster.showError(this, msg, Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun removeFingerprintData() {
        fingerprintPreferenceHelper.removeUserId()
        fingerprintPreferenceHelper.unregisterFingerprint()
    }

    companion object {

        const val IS_AUTO_LOGIN = "auto_login"

        const val AUTO_LOGIN_EMAIL = "email"
        const val AUTO_LOGIN_PASS = "pw"

        const val IS_AUTO_FILL = "auto_fill"
        const val AUTO_FILL_EMAIL = "email"

        const val FACEBOOK = "facebook"
        const val GPLUS = "gplus"

        const val ID_ACTION_REGISTER = 111
        const val ID_ACTION_DEVOPS = 112

        const val REQUEST_SMART_LOCK = 101
        const val REQUEST_SAVE_SMART_LOCK = 102
        const val REQUEST_SECURITY_QUESTION = 104
        const val REQUESTS_CREATE_PASSWORD = 106
        const val REQUEST_ACTIVATE_ACCOUNT = 107
        const val REQUEST_VERIFY_PHONE = 108
        const val REQUEST_ADD_NAME = 109
        const val REQUEST_CHOOSE_ACCOUNT = 110
        const val REQUEST_LOGIN_PHONE = 112
        const val REQUEST_REGISTER_PHONE = 113
        const val REQUEST_ADD_NAME_REGISTER_PHONE = 114
        const val REQUEST_LOGIN_GOOGLE = 116
        const val REQUEST_ADD_PIN = 117
        const val REQUEST_COTP_PHONE_VERIFICATION = 118
        const val REQUEST_ADD_PIN_AFTER_SQ = 119
        private val REQUEST_PENDING_OTP_VALIDATE = 121
        const val REQUEST_ADD_PIN_AFTER_REGISTER_PHONE = 122

        private const val PHONE_TYPE = "phone"
        private const val EMAIL_TYPE = "email"

        private const val OTP_SECURITY_QUESTION = 134
        private const val OTP_LOGIN_PHONE_NUMBER = 112
        private const val OTP_REGISTER_PHONE_NUMBER = 116
        private const val OTP_TYPE_ACTIVATE = 143

        private const val LOGIN_LOAD_TRACE = "gb_login_trace"
        private const val LOGIN_SUBMIT_TRACE = "gb_submit_login_trace"

        private const val SOURCE_ACCOUNT = "account"
        private const val SOURCE_ATC = "atc"

        private const val FACEBOOK_LOGIN_TYPE = "fb"

        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

        private const val REMOTE_CONFIG_KEY_TICKER_FROM_ATC = "android_user_ticker_from_atc"
        private const val REMOTE_CONFIG_KEY_BANNER = "android_user_banner_login"
        private const val REMOTE_CONFIG_KEY_LOGIN_FP = "android_user_fingerprint_login"
        private const val REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF = "android_user_register_otp_push_notif_login_page"

        private const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        private const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

        private const val TOKOPEDIA_CARE_PATH = "help"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginEmailPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
