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
import android.text.InputType
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.firebase.TkpdFirebaseAnalytics
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_GOOGLE
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiConstant
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.analytics.NeedHelpAnalytics
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.DiscoverData
import com.tokopedia.loginregister.common.domain.pojo.DynamicBannerDataModel
import com.tokopedia.loginregister.common.domain.pojo.ProviderData
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.error.LoginErrorCode
import com.tokopedia.loginregister.common.error.getMessage
import com.tokopedia.loginregister.common.utils.RegisterUtil.removeErrorCode
import com.tokopedia.loginregister.common.utils.SellerAppWidgetHelper
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheetListener
import com.tokopedia.loginregister.common.view.dialog.PopupErrorDialog
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.databinding.FragmentLoginWithPhoneBinding
import com.tokopedia.loginregister.forbidden.ForbiddenActivity
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessLoginFragment
import com.tokopedia.loginregister.goto_seamless.worker.TemporaryTokenWorker
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.const.LoginConstants.Request.REQUEST_GOTO_SEAMLESS
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.domain.model.LoginOption
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.router.LoginRouter
import com.tokopedia.loginregister.login.service.GetDefaultChosenAddressService
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_EMAIL
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_LOGIN_METHOD
import com.tokopedia.loginregister.login.view.activity.LoginActivity.Companion.PARAM_PHONE
import com.tokopedia.loginregister.login.view.bottomsheet.NeedHelpBottomSheet
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.network.TokenErrorException
import com.tokopedia.sessioncommon.util.OclUtils
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
import com.tokopedia.sessioncommon.view.admin.dialog.LocationAdminDialog
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
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
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 18/01/19.
 */
open class LoginEmailPhoneFragment : BaseDaggerFragment(), LoginEmailPhoneContract.View {

    private var isTraceStopped: Boolean = false
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginEmailPhoneViewModel::class.java)
    }

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var registerAnalytics: RegisterAnalytics

    @Inject
    lateinit var seamlessAnalytics: SeamlessLoginAnalytics

    @Inject
    lateinit var oclPreferences: OclPreference

    @Inject
    lateinit var needHelpAnalytics: NeedHelpAnalytics

    @Inject
    lateinit var gotoSeamlessHelper: GotoSeamlessHelper

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var firebaseRemoteConfig: RemoteConfig

    @Inject
    lateinit var abTestPlatform: AbTestPlatform

    @Inject lateinit var oclUtils: OclUtils

    var viewBinding by autoClearedNullable<FragmentLoginWithPhoneBinding>()

    private var source: String = ""
    protected var isAutoLogin: Boolean = false
    private var isShowTicker: Boolean = false
    private var isShowBanner: Boolean = false
    private var isEnableFingerprint = false
    private var isEnableSilentVerif = false
    private var isEnableOcl = false
    private var isEnableDirectBiometric = false
    private var isHitRegisterPushNotif: Boolean = false
    private var isEnableEncryptConfig: Boolean = false
    private var activityShouldEnd = true
    private var isFromRegister = false
    private var isFromChooseAccount = false
    private var isUseHash = false
    private var validateToken = ""
    private var isLoginAfterSq = false
    private var isReturnHomeWhenBackPressed = false
    private var socmedBottomSheet: SocmedBottomSheet? = null

    private var currentEmail = ""
    private var tempValidateToken = ""

    private var sharedPrefs: SharedPreferences? = null

    private var needHelpBottomSheetUnify: NeedHelpBottomSheet? = null
    private var isEnableSeamlessLogin = false

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
                devOpsText.setSpan(
                    ForegroundColorSpan(
                        MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
                    ),
                    0,
                    devOpsText.length,
                    0
                )
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

    private fun routeToGojekSeamlessPage() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalUserPlatform.GOTO_SEAMLESS_LOGIN)
        startActivityForResult(intent, REQUEST_GOTO_SEAMLESS)
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
        isReturnHomeWhenBackPressed = getParamBoolean(
            ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME,
            arguments,
            savedInstanceState,
            false
        )
        isEnableSeamlessLogin = isEnableSeamlessGoto()
        isEnableFingerprint = abTestPlatform.getString(LoginConstants.RollenceKey.LOGIN_PAGE_BIOMETRIC, "").isNotEmpty()
        isEnableDirectBiometric = isEnableDirectBiometric()
        isEnableOcl = isOclEnabled()
        refreshRolloutVariant()
    }

    fun isOclEnabled(): Boolean {
        return oclPreferences.getToken().isNotEmpty() &&
            arguments?.getBoolean(ApplinkConstInternalUserPlatform.PARAM_IS_FROM_OCL_LOGIN, false) == false &&
            oclUtils.isOclEnabled()
    }

    open fun refreshRolloutVariant() {
        abTestPlatform.fetchByType(null)
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
        viewBinding = FragmentLoginWithPhoneBinding.inflate(inflater, container, false)
        return viewBinding?.root
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
        if (savedInstanceState == null) {
            clearData()
        }

        initObserver()
        initListener()
        prepareView()
        setupToolbar()
        checkLoginOption()

        prepareArgData()
        viewModel.discoverLogin()

        if (!GlobalConfig.isSellerApp()) {
            if (isShowBanner) {
                viewModel.getDynamicBannerData()
            } else {
                showTicker()
            }
        }

        val emailExtensionList = mutableListOf<String>()
        emailExtensionList.addAll(requireContext().resources.getStringArray(R.array.email_extension))
        viewBinding?.loginInputView?.setEmailExtension(viewBinding?.emailExtension, emailExtensionList)
        viewBinding?.loginInputView?.initKeyboardListener(view)

        autoFillWithDataFromLatestLoggedIn()

        initInputType()
    }

    private fun initInputType() {
        viewBinding?.loginInputView?.inputEmailPhoneField?.apply {
            setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
            setLabel(requireActivity().getString(R.string.phone_or_email_input))
        }
    }

    private fun checkLoginOption() {
        if (GlobalConfig.isSellerApp()){
            viewModel.checkLoginOption(
                isEnableSeamless = false,
                isEnableFingerprint = false,
                isEnableDirectBiometric = false,
                isEnableOcl = false
            )
        } else {
            viewModel.checkLoginOption(isEnableSeamlessLogin, isEnableFingerprint, isEnableDirectBiometric, isEnableOcl)
        }
        showLoadingOverlay()
    }


    private fun showLoadingOverlay() {
        viewBinding?.loginLoadingOverlay?.root?.show()
        (activity as? LoginActivity)?.supportActionBar?.hide()
    }

    private fun hideLoadingOverlay() {
        viewBinding?.loginLoadingOverlay?.root?.hide()
        (activity as? LoginActivity)?.supportActionBar?.show()
    }

    private fun prepareArgData() {
        arguments?.let {
            val isAutoFill = it.getBoolean(LoginConstants.AutoLogin.IS_AUTO_FILL, false)
            val phone = it.getString(PARAM_PHONE, "")
            val email = it.getString(PARAM_EMAIL, "")
            val method = it.getString(PARAM_LOGIN_METHOD, "")

            isFromRegister = it.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false)

            if (phone.isNotEmpty()) {
                viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(phone)
            } else if (email.isNotEmpty()) {
                viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(email)
            }

            when {
                isAutoFill -> {
                    viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(arguments?.getString(LoginConstants.AutoLogin.AUTO_FILL_EMAIL, ""))
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
        viewModel.navigateToGojekSeamless.observe(viewLifecycleOwner) {
            if (it) {
                routeToGojekSeamlessPage()
            } else {
                hideLoadingOverlay()
            }
        }

        viewModel.registerCheckResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessRegisterCheck().invoke(it.data)
                is Fail -> onErrorRegisterCheck().invoke(it.throwable)
            }
        }

        viewModel.discoverResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessDiscoverLogin(it.data)
                is Fail -> onErrorDiscoverLogin(it.throwable)
            }
        }

        viewModel.activateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessActivateUser(it.data)
                is Fail -> onFailedActivateUser(it.throwable)
            }
        }

        viewModel.loginTokenResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessLoginEmail(it.data)
                is Fail -> onErrorLoginEmail(currentEmail).invoke(it.throwable)
            }
        }

        viewModel.loginTokenV2Response.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessLoginEmail()
                is Fail -> onErrorLoginEmail(currentEmail).invoke(it.throwable)
            }
        }

        viewModel.profileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> onErrorGetUserInfo().invoke(it.throwable)
            }
        }

        viewModel.loginTokenAfterSQResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessReloginAfterSQ(it.data)
                is Fail -> onErrorReloginAfterSQ().invoke(it.throwable)
            }
        }

        viewModel.loginTokenGoogleResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> viewModel.getUserInfo()
                is Fail -> onErrorLoginGoogle("").invoke(it.throwable)
            }
        }

        viewModel.getTickerInfoResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetTickerInfo(it.data)
                is Fail -> onErrorGetTickerInfo(it.throwable)
            }
        }

        viewModel.dynamicBannerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onGetDynamicBannerSuccess(it.data)
                is Fail -> onGetDynamicBannerError(it.throwable)
            }
        }

        viewModel.showLocationAdminPopUp.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> showLocationAdminPopUp()
                is Fail -> showGetAdminTypeError(it.throwable)
            }
        }

        viewModel.adminRedirection.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onLocationAdminRedirection()
                else -> {
                    // no-op
                }
            }
        }

        viewModel.loginBiometricResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessLoginBiometric()
                is Fail -> onErrorLoginBiometric(it.throwable)
            }
            dismissLoadingLogin()
        }

        viewModel.goToActivationPageAfterRelogin.observe(viewLifecycleOwner) {
            onGoToActivationPageAfterRelogin().invoke(it)
        }

        viewModel.goToActivationPage.observe(viewLifecycleOwner) {
            onGoToActivationPage(it)
        }

        viewModel.showPopup.observe(viewLifecycleOwner) {
            showPopup().invoke(it)
        }

        viewModel.goToSecurityQuestion.observe(viewLifecycleOwner) {
            onGoToSecurityQuestion(it).invoke()
        }

        viewModel.goToSecurityQuestionAfterRelogin.observe(viewLifecycleOwner) {
            onGoToSecurityQuestionAfterRelogin().invoke()
        }

        viewModel.getTemporaryKeyResponse.observe(viewLifecycleOwner) {
            if (it) {
                context?.run {
                    TemporaryTokenWorker.scheduleWorker(applicationContext)
                }
            }
            onSuccessLogin()
        }

        viewModel.getLoginOption.observe(viewLifecycleOwner) {
            handleLoginOption(it)
        }
    }

    private fun handleLoginOption(data: LoginOption) {
        hideLoadingOverlay()
        if (data.isEnableSeamless) {
            routeToGojekSeamlessPage()
        } else if (data.isEnableOcl) {
            goToOclChooseAccount()
        } else if (data.isEnableDirectBiometric) {
            analytics.trackClickBiometricLoginBtn()
            gotoVerifyFingerprint()
        }
        // The non direct biometric won't be affected, we still have to show the biometric login btn
        hideOrShowFingerprintBtn(data.isEnableBiometrics)
    }

    /**
     * @param isShowDirectBiometricsPrompt default false means that the param is not called from [handleLoginOption]
     */
    private fun hideOrShowFingerprintBtn(isRegistered: Boolean) {
        activity?.let {
            if (isRegistered) {
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
        analytics.trackOnLoginFingerprintFailed(throwable.message ?: "")
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
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT_FINGERPRINT
            ).apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
            }
            startActivityForResult(intent, LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT_FINGERPRINT)
        }
    }

    fun goToOclChooseAccount() {
        val intent = RouteManager.getIntent(
            requireContext(),
            ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT_OCL
        )
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        activity?.finish()
    }

    private fun fetchRemoteConfig() {
        isShowTicker = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_TICKER_FROM_ATC, false)
        isShowBanner = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_BANNER, false)
        isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(LoginConstants.RemoteConfigKey.KEY_REGISTER_PUSH_NOTIF, false)
        isEnableEncryptConfig = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_LOGIN_ENCRYPTION)
        isEnableSilentVerif = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_SILENT_VERIFICATION)
    }

    private fun clearData() {
        userSession.logoutSession()
    }

    private fun onLoginEmailClick() {
        val email = arguments?.getString(LoginConstants.AutoLogin.AUTO_LOGIN_EMAIL, "") ?: ""
        val pw = arguments?.getString(LoginConstants.AutoLogin.AUTO_LOGIN_PASS, "") ?: ""
        viewBinding?.loginInputView?.showLoginEmailView(email)
        viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(email)
        viewBinding?.loginInputView?.setPassword(pw)
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

    private fun prepareView() {
        viewBinding?.loginInputView?.showNeedHelp()
        viewBinding?.loginLoadingOverlay?.root?.background?.alpha = 178
        socmedBottomSheet = SocmedBottomSheet().apply {
            listener = object : SocmedBottomSheetListener {
                override fun onItemClick(provider: ProviderData) {
                    if (provider.id.contains(LoginConstants.DiscoverLoginId.GPLUS)) {
                        onLoginGoogleClick()
                    }
                }
            }
        }

        socmedBottomSheet?.setCloseClickListener {
            analytics.eventClickCloseSocmedButton()
            socmedBottomSheet?.dismiss()
        }

        viewBinding?.socmedBtn?.setOnClickListener {
            analytics.eventClickSocmedButton()
            socmedBottomSheet?.show(parentFragmentManager, context?.resources?.getString(R.string.bottom_sheet_show))
        }

        viewBinding?.loginInputView?.buttonContinue?.text = getString(R.string.next)
        viewBinding?.loginInputView?.buttonContinue?.contentDescription = getString(R.string.content_desc_register_btn)
        viewBinding?.loginInputView?.buttonContinue?.setOnClickListener {
            showLoadingLogin()
            analytics.trackClickOnNext(viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString())
            activity?.let { it1 -> analytics.eventClickLoginEmailButton(it1) }
            registerCheck(viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString())
        }

        viewBinding?.loginInputView?.setPasswordListener { v, _, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                loginEmail(
                    viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString().trim(),
                    v.text.toString(),
                    useHash = isUseHash
                )
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

        viewBinding?.loginInputView?.setButtonValidator(true)
        viewBinding?.loginInputView?.findViewById<Typography>(R.id.change_button)?.setOnClickListener {
            val email = viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString()
            onChangeButtonClicked()
            viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(email)
        }

        activity?.let { it ->

            viewBinding?.registerButton?.setOnClickListener {
                registerAnalytics.trackClickBottomSignUpButton()
                goToRegisterInitial(source)
            }
        }
    }

    private fun initListener() {
        val forgotPassword = viewBinding?.loginInputView?.findViewById<Typography>(R.id.need_help)
        forgotPassword?.setOnClickListener {
            needHelpAnalytics.trackPageClickButuhBantuan()
            showNeedHelpBottomSheet()
        }
    }

    private fun disableFingerprint() {
        viewBinding?.fingerprintBtn?.hide()
    }

    private fun enableFingerprint() {
        viewBinding?.fingerprintBtn?.apply {
            setLeftDrawableForFingerprint()
            show()
            setOnClickListener {
                analytics.trackClickBiometricLoginBtn()
                gotoVerifyFingerprint()
            }
        }
    }

    private fun isEnableDirectBiometric(): Boolean {
        val value = abTestPlatform.getString(LoginConstants.RollenceKey.DIRECT_LOGIN_BIOMETRIC, "")
        return value.isNotEmpty()
    }

    /**
     * function to prevent clash biometrics prompt and seamless routing
     * @param isEnableSeamless value is get from [LoginOption]
     * @return true if [isEnableDirectBiometric] true && [isEnableSeamless] false
     * @return false if [isEnableDirectBiometric] true && [isEnableSeamless] true
     */
    private fun isShowDirectBiometricsPrompt(isEnableSeamless: Boolean): Boolean = isEnableDirectBiometric && isEnableSeamless.not()

    private fun setLeftDrawableForFingerprint() {
        if (activity != null) {
            val icon = ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_fingerprint_thumb
            )
            viewBinding?.fingerprintBtn?.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        }
    }

    private fun setupSpannableText() {
        activity?.let {
            val sourceString = requireContext().resources.getString(R.string.span_not_have_tokopedia_account)

            val spannable = SpannableString(sourceString)

            spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(
                            activity,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                        ds.typeface = Typeface.create(
                            "sans-serif",
                            Typeface
                                .NORMAL
                        )
                    }
                },
                sourceString.indexOf("Daftar"),
                sourceString.length,
                0
            )

            viewBinding?.registerButton?.setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun onChangeButtonClicked() {
        analytics.trackChangeButtonClicked()

        viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.imeOptions = EditorInfo.IME_ACTION_DONE

        viewBinding?.loginInputView?.buttonContinue?.text = getString(R.string.next)
        viewBinding?.loginInputView?.buttonContinue?.contentDescription = getString(R.string.content_desc_register_btn)
        viewBinding?.loginInputView?.buttonContinue?.setOnClickListener {
            showLoadingLogin()
            registerCheck(viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString())
        }
        viewBinding?.loginInputView?.showDefaultView()
    }

    override fun goToForgotPassword() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString().trim())
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        activity?.applicationContext?.let { analytics.eventClickForgotPasswordFromLogin(it) }
    }

    private fun isEnableSeamlessGoto(): Boolean {
        val rollence = abTestPlatform.getString(
            ROLLENCE_KEY_GOTO_SEAMLESS,
            ""
        )
        return rollence.isNotEmpty()
    }

    private fun showNeedHelpBottomSheet() {
        if (needHelpBottomSheetUnify == null) {
            needHelpBottomSheetUnify = NeedHelpBottomSheet()
        }

        needHelpBottomSheetUnify?.show(childFragmentManager, TAG_NEED_HELP_BOTTOM_SHEET)
    }

    override fun goToTokopediaCareWebview() {
        RouteManager.route(
            activity,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH
            )
        )
    }

    override fun onSuccessDiscoverLogin(discoverData: DiscoverData) {
        stopTrace()

        if (discoverData.providers.isNotEmpty()) {
            discoverData.providers.onEach { provider ->
                if (userSession.name.isNotEmpty()) {
                    var name = userSession.name
                    if (name.split("\\s".toRegex()).size > 1) {
                        name = name.substring(0, name.indexOf(" "))
                    }
                    if (provider.id.equals(
                            LoginConstants.DiscoverLoginId.GPLUS,
                            ignoreCase = true
                        ) && userSession.loginMethod == UserSessionInterface.LOGIN_METHOD_GOOGLE
                    ) {
                        provider.name = "${provider.name} ${getString(R.string.socmed_account_as)} $name"
                    }
                }
            }.also {
                socmedBottomSheet?.setProviders(it)
            }
        } else {
            onErrorDiscoverLogin(
                MessageErrorException(
                    ErrorHandlerSession.getDefaultErrorCodeMessage(
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                        context
                    )
                )
            )
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
            viewBinding?.progressBarLoginWithPhone?.animate()?.setDuration(it)
                ?.alpha((if (isLoading) 1 else 0).toFloat())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (viewBinding?.progressBarLoginWithPhone != null) {
                            viewBinding?.progressBarLoginWithPhone?.visibility = if (isLoading) View.VISIBLE else View.GONE
                        }
                    }
                })

            viewBinding?.container?.animate()?.setDuration(it)
                ?.alpha((if (isLoading) 0 else 1).toFloat())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (viewBinding?.container != null) {
                            viewBinding?.container?.visibility = if (isLoading) View.GONE else View.VISIBLE
                        }
                    }
                })
        }
    }

    override fun goToRegisterInitial(source: String) {
        activity?.let {
            analytics.eventClickRegisterFromLogin()
            var intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
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
        val forbiddenMessage = context?.getString(
            com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth
        )
        val errorMessage = throwable.getMessage(requireActivity())
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

        if (viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text?.isNotBlank() == true) {
            userSession.autofillUserData = viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString()
        }

        registerPushNotif()
        submitIntegrityApi()

        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                setLoginSuccessSellerApp()
            } else {
                val bundle = Bundle()

                if (isFromRegister) {
                    bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER, true)
                }

                if (isReturnHomeWhenBackPressed) {
                    goToHome()
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
            TwoFactorMluHelper.clear2FaInterval(it)
            initTokoChatConnection()
        }

        refreshRolloutVariant()
        saveFirstInstallTime()
    }

    private fun initTokoChatConnection() {
        activity?.let {
            if (it.application is AbstractionRouter) {
                (it.application as AbstractionRouter).connectTokoChat(true)
            }
        }
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
                RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION)
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
            val ctx = activity?.applicationContext
            TkpdAppsFlyerMapper.getInstance(ctx).mapAnalytics()
            TrackApp.getInstance().gtm.pushUserId(userId)
            val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
            if (!GlobalConfig.DEBUG) {
                crashlytics.setUserId(userId)
            }
            ctx?.let {
                TkpdFirebaseAnalytics.getInstance(ctx).setUserId(userId)
            }

            if (userSession.isLoggedIn) {
                val userData = UserData()
                userData.userId = userSession.userId
                userData.medium = userSession.loginMethod

                // Identity Event
                LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData)
                )

                // Login Event
                LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData)
                )
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

    private fun goToHome() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun trackSuccessValidate() {
        analytics.trackClickOnNextSuccess(viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString())
    }

    override fun onErrorValidateRegister(throwable: Throwable) {
        dismissLoadingLogin()
        val message = getErrorMsgWithLogging(throwable, withErrorCode = false, flow = "")
        analytics.trackClickOnNextFail(viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString(), message.removeErrorCode())
        viewBinding?.loginInputView?.onErrorInputEmailPhoneValidate(message)
    }

    override fun onErrorEmptyEmailPhone() {
        dismissLoadingLogin()
        viewBinding?.loginInputView?.onErrorInputEmailPhoneValidate(getString(R.string.must_insert_email_or_phone))
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
        viewBinding?.loginInputView?.showLoginEmailView(email)
        viewBinding?.loginInputView?.buttonContinue?.setOnClickListener {
            loginEmail(
                email,
                viewBinding?.loginInputView?.getPassword().orEmpty(),
                useHash = isUseHash
            )
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
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
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
                    viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(email)
                    viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setSelection(viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text?.length.toZeroIfNull())
                }
                dialog.show()
            }
        }
    }

    override fun resetError() {
        viewBinding?.loginInputView?.resetErrorWrapper()
    }

    override fun showErrorPassword(resId: Int) {
        viewBinding?.loginInputView?.onErrorPassword(getString(resId))
    }

    override fun showErrorEmail(resId: Int) {
        viewBinding?.loginInputView?.onErrorInputEmailPhoneValidate(getString(resId))
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
                val errorMessage = it.getMessage(requireActivity())
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
            if (isEnableSeamlessLogin) {
                viewModel.getTemporaryKeyForSDK(profilePojo)
            } else {
                onSuccessLogin()
            }
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

    override fun onLocationAdminRedirection() {
        context?.let {
            dismissLoadingLogin()
            RouteManager.route(it, ApplinkConstInternalMarketplace.ADMIN_INVITATION)
        }
    }

    override fun showGetAdminTypeError(throwable: Throwable) {
        val errorMessage = throwable.getMessage(requireActivity())
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

    // Flow should not be possible
    override fun onGoToActivationPageAfterRelogin(): (MessageErrorException) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(it, LoginErrorCode.ERROR_ACTIVATION_AFTER_RELOGIN)
        }
    }

    // Flow should not be possible
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
                val errorMessage = it.getMessage(requireActivity())
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
        return (
            e is TokenErrorException &&
                e.errorDescription.isNotEmpty() &&
                e.errorDescription
                    .toLowerCase(Locale.getDefault()).contains(NOT_ACTIVATED) &&
                !TextUtils.isEmpty(email)
            )
    }

    override fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
            startActivityForResult(intent, LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun goToVerification(phone: String = "", email: String = "", otpType: Int): Intent {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
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
            if (requestCode == LoginConstants.Request.REQUEST_LOGIN_GOOGLE && data != null) {
                run {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleGoogleSignInResult(task)
                }
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
                data.extras != null
            ) {
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
                if (resultCode == Activity.RESULT_OK) {
                    data?.extras?.let {
                        val email = it.getString(ApplinkConstInternalGlobal.PARAM_EMAIL) ?: ""
                        val token = it.getString(ApplinkConstInternalGlobal.PARAM_TOKEN) ?: ""
                        onSuccessChooseAccountFingerprint(email, token)
                    }
                } else {
                    showToaster(getString(R.string.error_login_fp_error))
                }
            } else if (requestCode == LoginConstants.Request.REQUEST_CHOOSE_ACCOUNT_OCL && resultCode == Activity.RESULT_OK) {
                viewModel.getUserInfo()
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
            } else if (requestCode == REQUEST_GOTO_SEAMLESS) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        viewModel.getUserInfo()
                    }
                    Activity.RESULT_CANCELED -> {
                        activity?.finish()
                    }
                    GotoSeamlessLoginFragment.RESULT_OTHER_ACCS -> {
                        hideLoadingOverlay()
                    }
                    else -> {
                        activity?.finish()
                    }
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
        if (context != null) {
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
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PIN)
        intent.putExtras(
            Bundle().apply {
                putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
                putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
                putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
            }
        )
        startActivityForResult(intent, LoginConstants.Request.REQUEST_ADD_PIN_AFTER_REGISTER_PHONE)
    }

    private fun isFromAccountPage(): Boolean = source == LoginConstants.SourcePage.SOURCE_ACCOUNT

    private fun isFromAtcPage(): Boolean = source == LoginConstants.SourcePage.SOURCE_ATC

    override fun goToAddNameFromRegisterPhone(uuid: String, msisdn: String) {
        val applink = ApplinkConstInternalUserPlatform.ADD_NAME_REGISTER
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
            val accessToken = account?.idToken
            if (account != null && accessToken != null) {
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
            onErrorLogin(
                e,
                String.format(
                    getString(R.string.loginregister_failed_login_google),
                    e.statusCode.toString()
                )
            )
        }
    }

    override fun onGoToChangeName() {
        if (activity != null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.ADD_NAME_PROFILE)
            intent.putExtras(
                Bundle().apply {
                    putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
                }
            )
            startActivityForResult(intent, LoginConstants.Request.REQUEST_ADD_NAME)
        }
    }

    override fun onBackPressed() {
        analytics.trackOnBackPressed()
        if (viewBinding?.loginInputView?.findViewById<Typography>(R.id.change_button)?.visibility == View.VISIBLE) {
            val email = viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text.toString()
            onChangeButtonClicked()
            viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.setText(email)
        } else if (isReturnHomeWhenBackPressed) {
            goToHome()
        } else if (activity != null) {
            activity?.finish()
        }
    }

    override fun onSuccessGetTickerInfo(listTickerInfo: List<TickerInfoPojo>) {
        if (listTickerInfo.isNotEmpty()) {
            viewBinding?.tickerAnnouncement?.show()
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
                    viewBinding?.tickerAnnouncement?.addPagerView(adapter, mockData)
                }
            } else {
                listTickerInfo.first().let {
                    viewBinding?.tickerAnnouncement?.tickerTitle = it.title
                    viewBinding?.tickerAnnouncement?.setHtmlDescription(it.message)
                    viewBinding?.tickerAnnouncement?.tickerShape = getTickerType(it.color)
                }
                viewBinding?.tickerAnnouncement?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        analytics.eventClickLinkTicker(linkUrl.toString())
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                        analytics.eventClickCloseTicker()
                    }
                })
            }
            viewBinding?.tickerAnnouncement?.setOnClickListener { v ->
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
        if (id.isEmpty()) {
            onErrorEmptyEmailPhone()
        } else {
            viewModel.registerCheck(id)
        }
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
                userSession.tempPhoneNumber = viewBinding?.loginInputView?.textValue
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
                        userSession.setTempLoginEmail(viewBinding?.loginInputView?.textValue)
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
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_SEARCH,
                Context.MODE_PRIVATE
            )
            sharedPrefs?.edit()?.putLong(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_TIME_SEARCH,
                0
            )?.apply()
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
                viewBinding?.bannerLogin?.let { banner ->
                    ImageUtils.loadImage(
                        imageView = banner,
                        url = dynamicBannerDataModel.banner.imgUrl,
                        imageLoaded = {
                            if (it) {
                                viewBinding?.bannerLogin?.show()
                                analytics.eventViewBanner(dynamicBannerDataModel.banner.imgUrl)
                            } else {
                                viewBinding?.bannerLogin?.hide()
                                showTicker()
                            }
                        }
                    )
                }
            }
        } else {
            showTicker()
        }
    }

    override fun onGetDynamicBannerError(throwable: Throwable) {
        viewBinding?.bannerLogin?.hide()
        showTicker()
    }

    private fun showTicker() {
        if (!GlobalConfig.isSellerApp()) {
            if (isFromAtcPage() && isShowTicker) {
                viewBinding?.tickerAnnouncement?.visibility = View.VISIBLE
                viewBinding?.tickerAnnouncement?.tickerTitle = getString(R.string.title_ticker_from_atc)
                viewBinding?.tickerAnnouncement?.setTextDescription(String.format(getString(R.string.desc_ticker_from_atc)))
                viewBinding?.tickerAnnouncement?.tickerShape = Ticker.TYPE_ANNOUNCEMENT
                viewBinding?.tickerAnnouncement?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                    override fun onDismiss() {
                        analytics.eventClickCloseTicker()
                    }
                })
                viewBinding?.tickerAnnouncement?.setOnClickListener {
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

    private fun submitIntegrityApi() {
        if (firebaseRemoteConfig.getBoolean(IntegrityApiConstant.LOGIN_CONFIG)) {
            context?.let {
                IntegrityApiWorker.scheduleWorker(it.applicationContext, IntegrityApiConstant.EVENT_LOGIN)
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
        val mClassName = if (flow.isEmpty()) LoginEmailPhoneFragment::class.java.name else "${LoginEmailPhoneFragment::class.java.name} - $flow"
        val message = ErrorHandler.getErrorMessage(
            context,
            throwable,
            ErrorHandler.Builder().apply {
                withErrorCode(withErrorCode)
                className = mClassName
            }.build()
        )
        return message
    }

    private fun autoFillWithDataFromLatestLoggedIn() {
        if (!userSession.autofillUserData.isNullOrEmpty() && viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.text?.isEmpty() == true) {
            viewBinding?.loginInputView?.inputEmailPhoneField?.editText?.let {
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
        const val ROLLENCE_KEY_GOTO_SEAMLESS = "goto_seamless_v2"

        private const val TAG_NEED_HELP_BOTTOM_SHEET = "NEED HELP BOTTOM SHEET"

        private const val LOGIN_LOAD_TRACE = "gb_login_trace"
        private const val LOGIN_SUBMIT_TRACE = "gb_submit_login_trace"
        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"
        const val TOKOPEDIA_CARE_PATH = "help"
        const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"

        private const val PASSWORD_MIN_LENGTH = 4

        private const val SOCMED_BUTTON_CORNER_SIZE = 10

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginEmailPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
