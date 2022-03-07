package com.tokopedia.loginregister.registerinitial.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SMART_REGISTER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.graphql.util.getParamBoolean
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.utils.PhoneUtils
import com.tokopedia.loginregister.common.utils.PhoneUtils.Companion.removeSymbolPhone
import com.tokopedia.loginregister.common.utils.RegisterUtil.removeErrorCode
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.common.view.PartialRegisterInputView
import com.tokopedia.loginregister.common.view.banner.DynamicBannerConstant
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.common.view.dialog.PopupErrorDialog
import com.tokopedia.loginregister.common.view.dialog.ProceedWithPhoneDialog
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.listener.BaseDialogConnectAccListener
import com.tokopedia.loginregister.external_register.ovo.analytics.OvoCreationAnalytics
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.view.dialog.OvoAccountDialog
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialRouter
import com.tokopedia.loginregister.registerinitial.view.util.RegisterInitialRouterHelper
import com.tokopedia.loginregister.registerinitial.viewmodel.RegisterInitialViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_initial_register.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 10/24/18.
 */
open class RegisterInitialFragment : BaseDaggerFragment(), PartialRegisterInputView.PartialRegisterInputViewListener, RegisterInitialRouter {

    private lateinit var optionTitle: Typography
    private lateinit var separator: View
    private lateinit var partialRegisterInputView: PartialRegisterInputView
    private lateinit var emailPhoneEditText: AutoCompleteTextView
    private lateinit var registerButton: LoginTextView
    private var textTermAndCondition: Typography? = null
    private lateinit var container: ScrollView
    private lateinit var progressBar: RelativeLayout
    private lateinit var tickerAnnouncement: Ticker
    private lateinit var bannerRegister: ImageUnify
    private lateinit var socmedButton: UnifyButton
    private lateinit var bottomSheet: SocmedBottomSheet
    private var socmedButtonsContainer: LinearLayout? = null
    private lateinit var sharedPrefs: SharedPreferences

    private var phoneNumber: String? = ""
    private var source: String = ""
    private var email: String = ""
    private var isSmartLogin: Boolean = false
    private var isSmartRegister: Boolean = false
    private var isPending: Boolean = false
    private var isShowTicker: Boolean = false
    private var isShowBanner: Boolean = false
    private var isHitRegisterPushNotif: Boolean = false
    private var activityShouldEnd: Boolean = true
    private var enableOvoRegister: Boolean = false
    private var validateToken: String = ""

    @Inject
    lateinit var externalRegisterPreference: ExternalRegisterPreference

    @field:Named(SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    @Inject
    lateinit var ovoCreationAnalytics: OvoCreationAnalytics

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    val registerInitialViewModel by lazy {
        viewModelProvider.get(RegisterInitialViewModel::class.java)
    }

    @Inject
    lateinit var registerInitialRouter: RegisterInitialRouterHelper

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var combineLoginTokenAndValidateToken: LiveData<Unit>

    private var isRegisterOvo = false

    override fun onStart() {
        super.onStart()
        activity?.let {
            analytics.trackScreen(it, screenName)
        }
    }

    override fun initInjector() {
        getComponent(RegisterInitialComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return RegisterAnalytics.SCREEN_REGISTER_INITIAL
    }

    private fun useOvoRegister(): Boolean = enableOvoRegister

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearData()

        activity?.let {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getGoogleClientId(it))
                    .requestEmail()
                    .requestProfile()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(it, gso)
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

    private fun checkForOvoResume(){
        if(isRegisterOvo){
            if(externalRegisterPreference.isNeedContinue()){
                goToRegisterWithPhoneNumber(externalRegisterPreference.getPhone())
                externalRegisterPreference.isNeedContinue(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkForOvoResume()
        activity?.let {
            if (userSession.isLoggedIn && activity != null && activityShouldEnd) {
                it.setResult(Activity.RESULT_OK)
                it.finish()
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
        textTermAndCondition = view.findViewById(R.id.text_term_privacy)
        container = view.findViewById(R.id.container)
        progressBar = view.findViewById(R.id.progress_bar)
        tickerAnnouncement = view.findViewById(R.id.ticker_announcement)
        bannerRegister = view.findViewById(R.id.banner_register)
        prepareView()
        if (isSmartLogin) {
            showProgressBar()
            activity?.let {
                if (isPending) {
                    val intent =  registerInitialRouter.goToVerification(email = email, otpType = RegisterConstants.OtpType.OTP_TYPE_ACTIVATE, context = requireContext())
                    startActivityForResult(intent, RegisterConstants.Request.REQUEST_PENDING_OTP_VALIDATE)
                } else {
                    val intent =  registerInitialRouter.goToVerification(email = email, otpType = RegisterConstants.OtpType.OTP_TYPE_REGISTER, context = requireContext())
                    startActivityForResult(intent, RegisterConstants.Request.REQUEST_OTP_VALIDATE)
                }
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRemoteConfig()
        initObserver()
        initData()
        setupToolbar()
    }

    private fun setupToolbar() {
        activity?.let{ activity ->
            activity.findViewById<HeaderUnify>(R.id.unifytoolbar)?.apply {
                headerTitle = getString(R.string.register)
                actionText = getString(R.string.login)
                setNavigationOnClickListener {
                    activity.onBackPressed()
                }
                actionTextView?.setOnClickListener {
                    registerAnalytics.trackClickTopSignInButton()
                    registerInitialRouter.goToLoginPage(activity)
                }
            }
        }
    }

    open fun fetchRemoteConfig() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)
            isShowTicker = firebaseRemoteConfig.getBoolean(RegisterConstants.RemoteConfigKey.REMOTE_CONFIG_KEY_TICKER_FROM_ATC, false)
            isShowBanner = firebaseRemoteConfig.getBoolean(RegisterConstants.RemoteConfigKey.REMOTE_CONFIG_KEY_BANNER_REGISTER, false)
            isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(RegisterConstants.RemoteConfigKey.REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF, false)
            enableOvoRegister = firebaseRemoteConfig.getBoolean(ExternalRegisterConstants.CONFIG_EXTERNAL_REGISTER, false)
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
        activity?.let { act ->
            bottomSheet = SocmedBottomSheet(context)
            socmedButtonsContainer = bottomSheet.getSocmedButtonContainer()
            bottomSheet.setCloseClickListener {
                registerAnalytics.trackClickCloseSocmedButton()
                bottomSheet.dismiss()
            }
            socmedButton.setOnClickListener {
                registerAnalytics.trackClickSocmedButton()
                bottomSheet.show(act.supportFragmentManager, getString(R.string.bottom_sheet_show))
            }

            registerButton.visibility = View.GONE
            partialRegisterInputView.visibility = View.VISIBLE
            partialRegisterInputView.setButtonValidator(true)
            checkPermissionGetPhoneNumber()
            optionTitle.setText(R.string.register_option_title)

            context?.let {
                registerButton.setColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }
            registerButton.setBorderColor(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
            registerButton.setRoundCorner(REGISTER_BUTTON_CORNER_SIZE)
            registerButton.setImageResource(R.drawable.ic_email)
            registerButton.setOnClickListener {
                TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_EMAIL)
                goToRegisterEmailPage()
            }

            initTermPrivacyView()
        }
    }

    private fun initObserver() {
        registerInitialViewModel.getProviderResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetProvider(it.data)
                is Fail -> onFailedGetProvider(it.throwable)
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
        registerInitialViewModel.checkOvoResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessCheckOvoAcc(it.data)
                is Fail -> onErrorCheckovoAcc(it.throwable)
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
                activity?.let { act ->
                    val intent =  registerInitialRouter.goToVerification(email = it, otpType = RegisterConstants.OtpType.OTP_SECURITY_QUESTION, context = requireContext())
                    startActivityForResult(intent, RegisterConstants.Request.REQUEST_SECURITY_QUESTION)
                }
            }
        })
        registerInitialViewModel.goToActivationPageAfterRelogin.observe(viewLifecycleOwner, Observer {
            if (it != null) onGoToActivationPageAfterRelogin()
        })
        registerInitialViewModel.goToSecurityQuestionAfterRelogin.observe(viewLifecycleOwner, Observer {
            if (it != null) onGoToSecurityQuestionAfterRelogin()
        })
        registerInitialViewModel.dynamicBannerResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> setDynamicBannerView(it.data)
                is Fail -> {
                    bannerRegister.hide()
                    showTicker()
                }
            }
        })
    }

    private fun onSuccessCheckOvoAcc(checkOvoResponse: CheckOvoResponse) {
        dismissProgressBar()
        checkOvoResponse?.data?.run {
            if (isAllow) {
                if (isRegistered) {
                    showConnectOvoDialog()
                } else {
                    showRegisterOvoDialog()
                }
            } else {
                phoneNumber?.run {
                    goToRegisterWithPhoneNumber(this)
                }
            }
        }
    }

    fun doRegisterCheck() {
        showProgressBar()
        phoneNumber?.run {
            registerInitialViewModel.registerCheck(removeSymbolPhone(this))
        }
    }

    fun showRegisterOvoDialog() {
        activity?.let {
            ovoCreationAnalytics.trackViewOvoRegisterDialog()
            phoneNumber?.run {
                OvoAccountDialog.showRegisterDialogUnify(it, this, object : BaseDialogConnectAccListener {
                    override fun onDialogPositiveBtnClicked() {
                        isRegisterOvo = true
                        ovoCreationAnalytics.trackClickCreateOvo()
                        goToOvoAddName(this@run)
                    }

                    override fun onDialogNegativeBtnClicked() {
                        phoneNumber?.run {
                            ovoCreationAnalytics.trackClickRegTkpdOnly()
                            goToRegisterWithPhoneNumber(this)
                        }
                    }
                })
            }
        }
    }

    fun showConnectOvoDialog() {
        activity?.let {
            ovoCreationAnalytics.trackViewOvoConnectDialog()
            OvoAccountDialog.showConnectDialogUnify(it, object : BaseDialogConnectAccListener {
                override fun onDialogPositiveBtnClicked() {
                    isRegisterOvo = true
                    ovoCreationAnalytics.trackClickConnectOvo()
                    goToOvoAddName(phoneNumber ?: "")
                }

                override fun onDialogNegativeBtnClicked() {
                    phoneNumber?.run {
                        ovoCreationAnalytics.trackClickConnectTkpdOnly()
                        goToRegisterWithPhoneNumber(this)
                    }
                }
            })
        }
    }

    fun goToOvoAddName(phone: String) {
        activity?.let {
            val formattedPhone = removeSymbolPhone(phone)
            val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.OVO_ADD_NAME)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, formattedPhone)
            startActivityForResult(intent, ExternalRegisterConstants.REQUEST_OVO_REGISTER)
        }
    }

    private fun onErrorCheckovoAcc(throwable: Throwable) {
        dismissProgressBar()
        doRegisterCheck()
    }

    private fun onSuccessGetProvider(discoverData: DiscoverData) {
        dismissLoadingDiscover()

        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.dp_52))
        layoutParams.setMargins(0, SOCMED_BUTTON_MARGIN_SIZE, 0, SOCMED_BUTTON_MARGIN_SIZE)

        socmedButtonsContainer?.removeAllViews()

        discoverData.providers.forEach { provider ->
            context?.let {
                val loginTextView = LoginTextView(it, MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                loginTextView.setText(provider.name)
                loginTextView.setImage(provider.image)
                loginTextView.setRoundCorner(SOCMED_BUTTON_CORNER_SIZE)

                setDiscoverOnClickListener(provider, loginTextView)

                socmedButtonsContainer?.run {
                    addView(loginTextView, childCount,
                        layoutParams)
                }
            }

        }
    }

    private fun onFailedGetProvider(throwable: Throwable) {
        dismissLoadingDiscover()
        dismissProgressBar()
        val forbiddenMessage = context?.getString(
                com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        if (errorMessage.removeErrorCode() == forbiddenMessage) {
            onGoToForbiddenPage()
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity,
                    errorMessage) { registerInitialViewModel.getProvider() }.showRetrySnackbar()
            textTermAndCondition?.isEnabled = false
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
        dismissProgressBar()
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
                val adapter = TickerPagerAdapter(requireActivity(), mockData)
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
        }
        tickerAnnouncement.setOnClickListener {
            registerAnalytics.trackClickTicker()
        }
    }

    private fun onErrorGetTickerInfo(error: Throwable) {
        error.printStackTrace()
    }

    private fun onSuccessRegisterCheck(registerCheckData: RegisterCheckData) {
        dismissProgressBar()
        when (registerCheckData.registerType) {
            LoginConstants.LoginType.PHONE_TYPE -> {
                registerAnalytics.trackClickPhoneSignUpButton()
                setTempPhoneNumber(registerCheckData.view)
                if (registerCheckData.isExist) {
                    showRegisteredPhoneDialog(registerCheckData.view)
                } else if (registerCheckData.isShowRegisterOvo && useOvoRegister()) {
                    showProgressBar()
                    registerInitialViewModel.checkHasOvoAccount(registerCheckData.view)
                } else {
                    showProceedWithPhoneDialog(registerCheckData.view)
                }
            }
            LoginConstants.LoginType.EMAIL_TYPE -> {
                registerAnalytics.trackClickEmailSignUpButton()
                if (registerCheckData.isExist) {
                    if (!registerCheckData.isPending) {
                        showRegisteredEmailDialog(registerCheckData.view)
                    } else {
                        goToOTPActivateEmail(registerCheckData.view)
                    }
                } else {
                    goToOTPRegisterEmail(registerCheckData.view)
                }
            }
        }
    }

    override fun goToOTPActivateEmail(email: String) {
        activity?.let {
            isSmartRegister = true
            val intent = registerInitialRouter.goToVerification(email = email, otpType = RegisterConstants.OtpType.OTP_TYPE_ACTIVATE, context = requireContext())
            startActivityForResult(intent, RegisterConstants.Request.REQUEST_PENDING_OTP_VALIDATE)
        }
    }

    override fun goToOTPRegisterEmail(email: String) {
        activity?.let {
            val intent = registerInitialRouter.goToVerification(email = email, otpType = RegisterConstants.OtpType.OTP_TYPE_REGISTER, context = requireContext())
            startActivityForResult(intent, RegisterConstants.Request.REQUEST_OTP_VALIDATE)
        }
    }

    private fun onFailedRegisterCheck(throwable: Throwable) {
        dismissProgressBar()
        val messageError = ErrorHandler.getErrorMessage(context, throwable)
        registerAnalytics.trackFailedClickSignUpButton(messageError.removeErrorCode())
        partialRegisterInputView.onErrorValidate(messageError)
        phoneNumber = ""
    }

    private fun onSuccessActivateUser(activateUserData: ActivateUserData) {
        userSession.clearToken()
        userSession.setToken(activateUserData.accessToken, activateUserData.tokenType, EncoderDecoder.Encrypt(activateUserData.refreshToken, userSession.refreshTokenIV))
        registerInitialViewModel.getUserInfo()
    }

    private fun onFailedActivateUser(throwable: Throwable) {
        dismissProgressBar()
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
        dismissProgressBar()
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            registerInitialViewModel.reloginAfterSQ(validateToken)
        }.showRetrySnackbar()
    }

    //Wrong flow implementation
    private fun onGoToActivationPage(errorMessage: MessageErrorException) {
        NetworkErrorHelper.showSnackbar(activity, ErrorHandler.getErrorMessage(context, errorMessage))
    }

    override fun goToLoginPage() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            it.startActivity(intent)
            it.finish()
        }
    }

    override fun goToRegisterEmailPage() {
        showProgressBar()
        registerInitialRouter.goToRegisterEmail(this)
    }

    override fun goToRegisterEmailPageWithEmail(email: String, token: String, source: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL
        activity?.let {
            showProgressBar()
            registerInitialRouter.goToRegisterEmailPageWithParams(this, email, token, source)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.let {
            dismissProgressBar()

            when(requestCode) {
                RegisterConstants.Request.REQUEST_LOGIN_GOOGLE -> { onActivityResultLoginGoogle(data) }
                RegisterConstants.Request.REQUEST_REGISTER_EMAIL -> { onActivityResultRegisterEmail(resultCode) }
                RegisterConstants.Request.REQUEST_SECURITY_QUESTION -> { onActivityResultSecurityQuestion(resultCode, data) }
                RegisterConstants.Request.REQUEST_VERIFY_PHONE_REGISTER_PHONE -> { onActivityResultVerifyPhoneRegister(resultCode, data) }
                RegisterConstants.Request.REQUEST_ADD_NAME_REGISTER_PHONE -> { processAfterAddNameRegisterPhone(data?.extras) }
                RegisterConstants.Request.REQUEST_ADD_PIN -> { registerInitialViewModel.getUserInfoAfterAddPin() }
                RegisterConstants.Request.REQUEST_VERIFY_PHONE_TOKOCASH -> { onActivityResultVerifyPhoneTokoCash(resultCode, data) }
                RegisterConstants.Request.REQUEST_CHOOSE_ACCOUNT -> { onActivityResultChooseAccount(resultCode, data) }
                RegisterConstants.Request.REQUEST_CHANGE_NAME -> { onActivityResultChangeName(resultCode) }
                RegisterConstants.Request.REQUEST_OTP_VALIDATE -> { onActivityResultOtpValidate(resultCode, data) }
                RegisterConstants.Request.REQUEST_PENDING_OTP_VALIDATE -> { onActivityResultPendingOtpValidate(resultCode, data) }
                ExternalRegisterConstants.REQUEST_OVO_REGISTER -> { onActivityResultOvoRegistration(resultCode) }
                else -> { super.onActivityResult(requestCode, resultCode, data) }
            }
        }
    }

    private fun onActivityResultLoginGoogle(data: Intent?) {
        data?.let {
            GoogleSignIn.getSignedInAccountFromIntent(it)?.let { taskGoogleSignInAccount ->
                handleGoogleSignInResult(taskGoogleSignInAccount)
            }
        }
    }

    private fun onActivityResultRegisterEmail(resultCode: Int) {
        when(resultCode) {
            Activity.RESULT_OK -> {
                registerInitialViewModel.getUserInfo()
            }

            Activity.RESULT_CANCELED -> {
                dismissProgressBar()
                activity?.setResult(Activity.RESULT_CANCELED)
                userSession.clearToken()
            }
        }
    }

    private fun onActivityResultSecurityQuestion(resultCode: Int, data: Intent?) {
        when(resultCode) {
            Activity.RESULT_OK -> {
                data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")?.let { validateToken ->
                    registerInitialViewModel.reloginAfterSQ(validateToken)
                }
            }

            Activity.RESULT_CANCELED -> {
                dismissProgressBar()
                activity?.setResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun onActivityResultVerifyPhoneRegister(resultCode: Int, data: Intent?) {
        when(resultCode) {
            Activity.RESULT_OK -> {
                validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                val uuid = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID).orEmpty()
                goToAddName(uuid)
            }

            Activity.RESULT_CANCELED -> {
                dismissProgressBar()
                activity?.setResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun onActivityResultChooseAccount(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {

            activity?.setResult(Activity.RESULT_OK, Intent().putExtras(
                Bundle().apply {
                    putBoolean(PARAM_IS_SMART_REGISTER, isSmartRegister)
                }
            ))

            if (data == null) {
                registerInitialViewModel.getUserInfo()
                return
            }

            data.extras?.let { bundle ->
                if (bundle.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, false)) {
                    activity?.let {
                        val intent = registerInitialRouter.goToVerification(otpType = RegisterConstants.OtpType.OTP_SECURITY_QUESTION, context = requireContext())
                        startActivityForResult(intent, RegisterConstants.Request.REQUEST_SECURITY_QUESTION)
                    }
                } else {
                    registerInitialViewModel.getUserInfo()
                }
            }
        }
    }

    private fun onActivityResultVerifyPhoneTokoCash(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.extras?.let {
                goToChooseAccountPage(
                    it.getString(ApplinkConstInternalGlobal.PARAM_UUID, "") ?: "",
                    it.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "") ?: ""
                )
            }
        }
    }

    private fun onActivityResultChangeName(resultCode: Int) {
       when(resultCode) {
           Activity.RESULT_OK -> {
               registerInitialViewModel.getUserInfo()
           }

           Activity.RESULT_CANCELED -> {
               userSession.logoutSession()
               dismissProgressBar()
               activity?.apply {
                   setResult(Activity.RESULT_CANCELED)
                   finish()
               }
           }
       }
    }

    private fun onActivityResultOtpValidate(resultCode: Int, data: Intent?) {
        when(resultCode) {
            Activity.RESULT_OK -> {
                data?.extras?.let { bundle ->
                    val email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL)
                    val token = bundle.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                    val source = bundle.getString(ApplinkConstInternalGlobal.PARAM_SOURCE)
                    if (!email.isNullOrEmpty() && !token.isNullOrEmpty()) {
                        if (!source.isNullOrEmpty()) goToRegisterEmailPageWithEmail(email, token, source)
                        else goToRegisterEmailPageWithEmail(email, token, "")
                    }
                }
            }

            Activity.RESULT_CANCELED -> {
                activity?.setResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun onActivityResultPendingOtpValidate(resultCode: Int, data: Intent?) {
        when(resultCode) {
            Activity.RESULT_OK -> {
                data?.extras?.let { bundle ->
                    val email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL)
                    val token = bundle.getString(ApplinkConstInternalGlobal.PARAM_TOKEN)
                    if (!email.isNullOrEmpty() && !token.isNullOrEmpty()) {
                        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
                        registerInitialViewModel.activateUser(email, token)
                    }
                }
            }

            Activity.RESULT_CANCELED -> {
                activity?.setResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun onActivityResultOvoRegistration(resultCode: Int) {
        if (resultCode == Activity.RESULT_CANCELED) {
            phoneNumber?.run { goToRegisterWithPhoneNumber(this) }
        }
    }

    /**
     * Please refer to the
     * [class reference for][com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes]
     */
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        if (context != null) {
            try {
                val account = completedTask.getResult(ApiException::class.java)
                val accessToken = account?.idToken ?: ""
                val email = account?.email ?: ""
                registerInitialViewModel.registerGoogle(accessToken, email)
            } catch (e: NullPointerException) {
                onErrorRegister(ErrorHandler.getErrorMessage(context, e))
            } catch (e: ApiException) {
                onErrorRegister(String.format(getString(R.string.loginregister_failed_login_google), e.statusCode.toString()))
            }
        }
    }

    private fun processAfterAddNameRegisterPhone(data: Bundle?) {
        val enable2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA) ?: false
        val enableSkip2FA = data?.getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA) ?: false
        val isSuccessRegister = data?.getBoolean(PARAM_IS_SUCCESS_REGISTER) ?: false
        val message = data?.getString(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY).orEmpty()

        if (!isSuccessRegister && message.isNotEmpty()) {
            showErrorToaster(message)
            return
        }

        if (enable2FA) {
            activityShouldEnd = false
            sendTrackingSuccessRegister()
            goToAddPin2FA(enableSkip2FA)
        } else {
            registerInitialViewModel.getUserInfo()
        }
    }

    private fun goToAddPin2FA(enableSkip2FA: Boolean) {
        registerInitialRouter.goToAddPin2FA(this, enableSkip2FA, validateToken)
    }

    private fun goToAddName(uuid: String) {
        phoneNumber?.run {
            registerInitialRouter.goToAddName(this@RegisterInitialFragment, uuid, this)
        }
    }

    override fun onActionPartialClick(id: String) {
        showProgressBar()
        registerAnalytics.trackClickSignUpButton()
        if (Patterns.PHONE.matcher(id).matches()) {
            setTempPhoneNumber(id)
            registerInitialViewModel.registerCheck(PhoneUtils.removeSymbolPhone(id))
        } else {
            registerInitialViewModel.registerCheck(id)
        }
    }

    private fun showLoadingDiscover() {
        LetUtil.ifLet(context, socmedButtonsContainer) { (ctx, socmedButtonsContainer) ->
            if(ctx is Context && socmedButtonsContainer is LinearLayout) {
                val pb = LoaderUnify(ctx)
                val lastPos = socmedButtonsContainer.childCount - 1
                if (socmedButtonsContainer.getChildAt(lastPos) !is LoaderUnify) {
                    socmedButtonsContainer.addView(pb, socmedButtonsContainer.childCount)
                }
                emailExtension?.hide()
            }
        }
    }

    private fun setDiscoverOnClickListener(provider: ProviderData, loginTextView: LoginTextView) {
        when (provider.id.lowercase(Locale.getDefault())) {
            LoginConstants.DiscoverLoginId.GPLUS -> loginTextView.setOnClickListener { onRegisterGoogleClick() }
        }
    }

    private fun onRegisterGoogleClick() {
        activity?.let {
            showProgressBar()
            bottomSheet.dismiss()
            registerAnalytics.trackClickGoogleButton(it.applicationContext)
            TrackApp.getInstance().moEngage.sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_GMAIL)
            goToRegisterGoogle()
        }

    }

    override fun goToRegisterGoogle() {
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RegisterConstants.Request.REQUEST_LOGIN_GOOGLE)
    }

    private fun dismissLoadingDiscover() {
        socmedButtonsContainer?.run {
            val lastPos = childCount - 1
            if (getChildAt(lastPos) is LoaderUnify) {
                removeViewAt(childCount - 1)
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        container.visibility = View.GONE
        textTermAndCondition?.visibility = View.GONE
    }

    private fun dismissProgressBar() {
        progressBar.visibility = View.GONE
        container.visibility = View.VISIBLE
        textTermAndCondition?.visibility = View.VISIBLE
    }

    private fun onErrorRegister(errorMessage: String) {
        dismissProgressBar()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
        registerAnalytics.trackErrorRegister(errorMessage.removeErrorCode(), userSession.loginMethod)
    }

    private fun showRegisteredEmailDialog(email: String) {
        registerAnalytics.trackFailedClickEmailSignUpButton(RegisterAnalytics.LABEL_EMAIL_EXIST)
        registerAnalytics.trackFailedClickEmailSignUpButtonAlreadyRegistered()
        val dialog = RegisteredDialog.createRegisteredEmailDialog(context, email)
        activity?.let {
            dialog?.setPrimaryCTAClickListener {
                isSmartRegister = true
                registerAnalytics.trackClickYesButtonRegisteredEmailDialog()
                dialog.dismiss()
                gotoLoginEmailPage(email)
            }
        }
        dialog?.setSecondaryCTAClickListener {
            registerAnalytics.trackClickChangeButtonRegisteredEmailDialog()
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun gotoLoginEmailPage(email: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.LOGIN_EMAIL, Uri.encode(email), source)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        val bundleResult = Bundle()
        bundleResult.putBoolean(PARAM_IS_SMART_REGISTER, isSmartRegister)
        activity?.setResult(Activity.RESULT_OK, Intent().putExtras(bundleResult))
        startActivity(intent)
        activity?.finish()
    }

    private fun showRegisteredPhoneDialog(phone: String) {
        registerAnalytics.trackFailedClickPhoneSignUpButton(RegisterAnalytics.LABEL_PHONE_EXIST)
        registerAnalytics.trackFailedClickPhoneSignUpButtonAlreadyRegistered()
        val dialog = RegisteredDialog.createRegisteredPhoneDialog(context, phone)
        dialog?.setPrimaryCTAClickListener {
            isSmartRegister = true
            registerAnalytics.trackClickYesButtonRegisteredPhoneDialog()
            dialog.dismiss()
            phoneNumber = phone
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
            goToLoginRegisteredPhoneNumber(phone)
        }
        dialog?.setSecondaryCTAClickListener {
            registerAnalytics.trackClickChangeButtonRegisteredPhoneDialog()
            dialog?.dismiss()
        }
        dialog?.show()
    }

    override fun goToLoginRegisteredPhoneNumber(phone: String) {
        phoneNumber = phone
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
        activity?.let {
            val intent =  registerInitialRouter.goToVerification(phone = phone, otpType = RegisterConstants.OtpType.OTP_LOGIN_PHONE_NUMBER, context = requireContext())
            startActivityForResult(intent, RegisterConstants.Request.REQUEST_VERIFY_PHONE_TOKOCASH)
        }
    }

    private fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        registerInitialRouter.goToChooseAccountPage(this, accessToken, phoneNumber)
    }

    private fun showProceedWithPhoneDialog(phone: String) {
        val dialog = ProceedWithPhoneDialog.createDialog(context, phone)
        registerAnalytics.trackClickPhoneSignUpButton()
        dialog?.setPrimaryCTAClickListener {
            showProgressBar()
            registerAnalytics.trackClickYesButtonPhoneDialog()
            dialog.dismiss()
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
            activity?.let {
                val intent = registerInitialRouter.goToVerification(phone = phone, otpType = RegisterConstants.OtpType.OTP_REGISTER_PHONE_NUMBER, context = requireContext())
                startActivityForResult(intent, RegisterConstants.Request.REQUEST_VERIFY_PHONE_REGISTER_PHONE)
            }
        }
        dialog?.setSecondaryCTAClickListener {
            registerAnalytics.trackClickChangeButtonPhoneDialog()
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun goToRegisterWithPhoneNumber(phone: String) {
        activity?.let {
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_PHONE
            val intent =  registerInitialRouter.goToVerification(phone = phone, otpType = RegisterConstants.OtpType.OTP_REGISTER_PHONE_NUMBER, context = requireContext())
            startActivityForResult(intent, RegisterConstants.Request.REQUEST_VERIFY_PHONE_REGISTER_PHONE)
        }
    }

    private fun setTempPhoneNumber(maskedPhoneNumber: String) {
        //use masked phone number form backend when needed
        //we need unmasked phone number (without dash) to be provided to backend
        this.phoneNumber = partialRegisterInputView.textValue
    }

    private fun sendTrackingSuccessRegister() {
        registerAnalytics.trackSuccessRegister(
                userSession.loginMethod,
                userSession.userId,
                userSession.name,
                userSession.email,
                userSession.phoneNumber,
                userSession.isGoldMerchant,
                userSession.shopId,
                userSession.shopName
        )
    }

    override fun onSuccessRegister() {
        activityShouldEnd = true
        registerPushNotif()
        activity?.let {
            val bundle = Bundle()

            if (isSmartLogin) {
                analytics.trackerSuccessRegisterFromLogin(userSession.loginMethod)
            }

            if (!isSmartRegister) {
                bundle.putBoolean(PARAM_IS_SUCCESS_REGISTER, true)
            }

            it.setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
            it.finish()
            saveFirstInstallTime()

            SubmitDeviceWorker.scheduleWorker(requireContext(), true)
            DataVisorWorker.scheduleWorker(requireContext(), true)
            AppAuthWorker.scheduleWorker(it, true)
            TwoFactorMluHelper.clear2FaInterval(it)
        }
    }

    private fun isFromAtc(): Boolean = source == LoginConstants.SourcePage.SOURCE_ATC

    private fun onGoToChangeName() {
        registerInitialRouter.goToChangeName(this)
    }

    private fun onGoToForbiddenPage() {
        ForbiddenActivity.startActivity(activity)
    }

    private fun getTickerType(hexColor: String): Int {
        return when (hexColor) {
            colorTickerDefault() -> Ticker.TYPE_ANNOUNCEMENT
            colorTickerWarning() -> Ticker.TYPE_WARNING
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
                partialRegisterInputView.setAdapterInputEmailPhone(ArrayAdapter(it, R.layout.select_dialog_item_material, phoneNumbers),
                        View.OnFocusChangeListener { v, hasFocus ->
                            if (v?.windowVisibility == View.VISIBLE) {
                                activity?.isFinishing?.let { isFinishing ->
                                    if (!isFinishing) {
                                        if (hasFocus && ::emailPhoneEditText.isInitialized && emailPhoneEditText.hasFocus()) {
                                            emailPhoneEditText.showDropDown()
                                        } else {
                                            emailPhoneEditText.dismissDropDown()
                                        }
                                    }
                                }
                            }
                        })
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
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs.edit().putLong(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_TIME_SEARCH, 0).apply()
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
                tickerAnnouncement.setTextDescription(String.format(getString(R.string.desc_ticker_from_atc)))
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
        if (isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                RegisterPushNotificationWorker.scheduleWorker(it)
            }
        }
    }

    private fun showPopupErrorAkamai() {
        dismissProgressBar()
        PopupErrorDialog.showPopupErrorAkamai(context)
    }

    private fun showErrorToaster(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        registerInitialViewModel.getProviderResponse.removeObservers(this)
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

    private fun initTermPrivacyView() {
        context?.let {
            val termPrivacy = SpannableString(getString(R.string.text_term_and_privacy))
            termPrivacy.setSpan(clickableSpan(PAGE_TERM_AND_CONDITION), TERM_AND_COND_START_SIZE, TERM_AND_COND_END_SIZE, 0)
            termPrivacy.setSpan(clickableSpan(PAGE_PRIVACY_POLICY), PRIVACY_POLICY_START_SIZE, PRIVACY_POLICY_END_SIZE, 0)
            termPrivacy.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), TERM_AND_COND_START_SIZE, TERM_AND_COND_END_SIZE, 0)
            termPrivacy.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), PRIVACY_POLICY_START_SIZE, PRIVACY_POLICY_END_SIZE, 0)

            textTermAndCondition?.setText(termPrivacy, TextView.BufferType.SPANNABLE)
            textTermAndCondition?.movementMethod = LinkMovementMethod.getInstance()
            textTermAndCondition?.isSelected = false
        }
    }

    private fun clickableSpan(page: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                context?.let {
                    startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.TERM_PRIVACY, page))
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
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
        private const val PHONE_NUMBER = "phonenumber"

        private const val REGISTER_BUTTON_CORNER_SIZE = 10
        private const val SOCMED_BUTTON_MARGIN_SIZE = 10
        private const val SOCMED_BUTTON_CORNER_SIZE = 10
        private const val TERM_AND_COND_START_SIZE = 34
        private const val TERM_AND_COND_END_SIZE = 54
        private const val PRIVACY_POLICY_START_SIZE = 61
        private const val PRIVACY_POLICY_END_SIZE = 78

        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

        const val TOKOPEDIA_CARE_PATH = "help"
        fun createInstance(bundle: Bundle): RegisterInitialFragment {
            val fragment = RegisterInitialFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
