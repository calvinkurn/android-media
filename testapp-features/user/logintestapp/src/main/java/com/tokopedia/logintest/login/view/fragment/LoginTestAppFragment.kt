package com.tokopedia.logintest.login.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.logintest.R
import com.tokopedia.logintest.common.data.DynamicBannerConstant
import com.tokopedia.logintest.common.data.model.DynamicBannerDataModel
import com.tokopedia.logintest.common.di.LoginRegisterTestAppComponent
import com.tokopedia.logintest.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.logintest.customview.PartialRegisterInputView
import com.tokopedia.logintest.login.di.DaggerLoginTestAppComponent
import com.tokopedia.logintest.login.domain.pojo.RegisterCheckData
import com.tokopedia.logintest.login.domain.pojo.StatusPinData
import com.tokopedia.logintest.login.router.LoginTestAppRouter
import com.tokopedia.logintest.login.view.activity.LoginTestAppActivity
import com.tokopedia.logintest.login.view.listener.LoginTestAppContract
import com.tokopedia.logintest.login.view.presenter.LoginTestAppPresenter
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.network.TokenErrorException
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_login_testapp_with_phone.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 18/01/19.
 */
class LoginTestAppFragment : BaseDaggerFragment(), LoginTestAppContract.View {

    @Inject
    lateinit var presenter: LoginTestAppPresenter

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    private var source: String = ""
    private var isAutoLogin: Boolean = false
    private var isShowTicker: Boolean = false
    private var isShowBanner: Boolean = false
    private var activityShouldEnd = true

    private lateinit var partialRegisterInputView: PartialRegisterInputView
    private lateinit var socmedButtonsContainer: LinearLayout
    private lateinit var emailPhoneEditText: EditText
    private lateinit var partialActionButton: TextView
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var tickerAnnouncement: Ticker
    private lateinit var bottomSheet: BottomSheetUnify
    private lateinit var bannerLogin: ImageView
    private lateinit var callTokopediaCare: Typography
    private lateinit var sharedPrefs: SharedPreferences

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val daggerLoginComponent = DaggerLoginTestAppComponent
                .builder().loginRegisterTestAppComponent(getComponent(LoginRegisterTestAppComponent::class.java))
                .build() as DaggerLoginTestAppComponent

        daggerLoginComponent.inject(this)
        presenter.attachView(this, this)
    }

    override fun onStart() {
        super.onStart()
        activity?.run {

        }
    }

    override fun onResume() {
        super.onResume()
        if (userSession.isLoggedIn && activity != null && activityShouldEnd) {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()
        }
    }

    override fun stopTrace() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, ID_ACTION_REGISTER, 0, "")
        val menuItem = menu.findItem(ID_ACTION_REGISTER)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        if (getDraw() != null) {
            menuItem.icon = getDraw()
        }
        if (GlobalConfig.isAllowDebuggingTools()) {
            menu.add(Menu.NONE, ID_ACTION_DEVOPS, 1, getString(R.string.developer_options))
            menu.findItem(ID_ACTION_DEVOPS).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == ID_ACTION_REGISTER) {
            goToRegisterInitial(source)
            return true
        }
        if (id == ID_ACTION_DEVOPS) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        source = getParamString(ApplinkConstInternalGlobal.PARAM_SOURCE, arguments, savedInstanceState, "")
        isAutoLogin = getParamBoolean(IS_AUTO_LOGIN, arguments, savedInstanceState, false)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_login_testapp_with_phone, container, false)
        partialRegisterInputView = view.findViewById(R.id.login_input_view)
        emailPhoneEditText = partialRegisterInputView.findViewById(R.id.input_email_phone)
        partialActionButton = partialRegisterInputView.findViewById(R.id.register_btn)
        passwordEditText = partialRegisterInputView.findViewById(R.id.password)
        tickerAnnouncement = view.findViewById(R.id.ticker_announcement)
        bannerLogin = view.findViewById(R.id.banner_login)
        callTokopediaCare = view.findViewById(R.id.to_tokopedia_care)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRemoteConfig()
        clearData()
        prepareView()
        if (arguments != null && arguments!!.getBoolean(IS_AUTO_FILL, false)) {
            emailPhoneEditText.setText(arguments!!.getString(AUTO_FILL_EMAIL, ""))
        } else if (isAutoLogin) {
            when (arguments!!.getInt(AUTO_LOGIN_METHOD)) {
                LoginTestAppActivity.METHOD_EMAIL -> onLoginEmailClick()
            }
        }

        if (!GlobalConfig.isSellerApp()) {
            if (isShowBanner) {
                presenter.getDynamicBanner(DynamicBannerConstant.Page.LOGIN)
            } else {
                showTicker()
            }
        }

        val emailExtensionList = mutableListOf<String>()
        emailExtensionList.addAll(resources.getStringArray(R.array.email_extension))
        partialRegisterInputView.setEmailExtension(emailExtension, emailExtensionList)
        partialRegisterInputView.initKeyboardListener(view)

    }

    private fun fetchRemoteConfig() {
        //replace remote config with direct true/false value, so remote config can be removed from this logintestapp module
        context?.let {
            isShowTicker = false
            isShowBanner = false
        }
    }

    private fun clearData() {
        userSession.logoutSession()
    }

    private fun onLoginEmailClick() {
        val email = arguments!!.getString(AUTO_LOGIN_EMAIL, "")
        val pw = arguments!!.getString(AUTO_LOGIN_PASS, "")
        partialRegisterInputView.showLoginEmailView(email)
        emailPhoneEditText.setText(email)
        passwordEditText.setText(pw)
        presenter.loginEmail(email, pw)

    }

    private fun prepareView() {

        emailPhoneEditText.setText("android.automation.seller.h5+frontendtest@tokopedia.com")
        passwordEditText.setText("tokopedia789")

        initTokopediaCareText()

        val viewBottomSheetDialog = View.inflate(context, R.layout.layout_testapp_socmed_bottomsheet, null)
        socmedButtonsContainer = viewBottomSheetDialog.findViewById(R.id.socmed_container)

        bottomSheet = BottomSheetUnify()
        bottomSheet.setTitle(getString(R.string.choose_social_media))
        bottomSheet.setChild(viewBottomSheetDialog)
        bottomSheet.setCloseClickListener {
            onDismissBottomSheet()
        }

        socmed_btn.setOnClickListener {
            fragmentManager?.let {
                bottomSheet.show(it, getString(R.string.bottom_sheet_show))
            }
        }

        partialActionButton.text = getString(R.string.next)
        partialActionButton.setOnClickListener {
            showLoadingLogin()
            registerCheck(emailPhoneEditText.text.toString())
        }

        passwordEditText.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                presenter.loginEmail(emailPhoneEditText.text.toString().trim(),
                        passwordEditText.text.toString())
                activity?.let {
                    KeyboardHandler.hideSoftKeyboard(it)
                }
                true
            } else {
                false
            }
        }

        partialRegisterInputView.setButtonValidator(true)
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
                goToRegisterInitial(source)
            }

            val forgotPassword = partialRegisterInputView.findViewById<TextView>(R.id.forgot_pass)
            forgotPassword.setOnClickListener {
                goToForgotPassword()
            }
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
                        ds.color = MethodChecker.getColor(context, R.color.Green_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                message.indexOf(getString(R.string.call_tokopedia_care)),
                message.indexOf(getString(R.string.call_tokopedia_care)) + getString(R.string.call_tokopedia_care).length,
                0
        )
        callTokopediaCare.movementMethod = LinkMovementMethod.getInstance()
        callTokopediaCare.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun onChangeButtonClicked() {

        emailPhoneEditText.imeOptions = EditorInfo.IME_ACTION_DONE

        partialActionButton.text = getString(R.string.next)
        partialActionButton.setOnClickListener { registerCheck(emailPhoneEditText.text.toString()) }
        partialRegisterInputView.showDefaultView()
    }

    private fun goToForgotPassword() {

        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailPhoneEditText.text.toString().trim())
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)

    }

    private fun goToTokopediaCareWebview() {
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH))
    }

    private fun onDismissBottomSheet() {
        try {
            if (bottomSheet != null) {
                bottomSheet.dismiss()
            }
        } catch (e: Exception) {
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
        emailExtension?.hide()

    }

    private fun goToRegisterInitial(source: String) {
        activity?.let {
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

    override fun onSuccessLoginEmail() {
        presenter.getUserInfo()
    }

    override fun onSuccessLogin() {
        dismissLoadingLogin()
        activityShouldEnd = true

        if (emailPhoneEditText.text.isNotBlank())
            userSession.autofillUserData = emailPhoneEditText.text.toString()

        activity?.run {
            if (GlobalConfig.isSellerApp()) {
                setLoginSuccessSellerApp()
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
            setFCM()
        }

        saveFirstInstallTime()
    }

    private fun setLoginSuccessSellerApp() = view?.run {
        if (context.applicationContext is LoginTestAppRouter) {
            (context.applicationContext as LoginTestAppRouter).setOnboardingStatus(true)
        }
        val intent = if (userSession.hasShop()) {
            RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.OPEN_SHOP)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.finish()
    }

    private fun setFCM() {
        CMPushNotificationManager.instance
                .refreshFCMTokenFromForeground(userSession.deviceId, true)
    }

    fun onErrorLogin(errorMessage: String?) {

        dismissLoadingLogin()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun isFromRegister(): Boolean {
        return (activity != null
                && activity!!.intent != null
                && activity!!.intent.getBooleanExtra(IS_FROM_REGISTER, false))
    }

    override fun onErrorValidateRegister(throwable: Throwable) {
        dismissLoadingLogin()
        val message = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable)
        partialRegisterInputView.onErrorValidate(message)
    }

    override fun onErrorEmptyEmailPhone() {
        dismissLoadingLogin()
        partialRegisterInputView.onErrorValidate(getString(R.string.must_insert_email_or_phone))
    }

    override fun onEmailExist(email: String) {
        dismissLoadingLogin()
        partialRegisterInputView.showLoginEmailView(email)
        partialActionButton.setOnClickListener {
            presenter.loginEmail(email, passwordEditText.text.toString())
            activity?.let {
                KeyboardHandler.hideSoftKeyboard(it)
            }
        }
    }

    override fun showNotRegisteredEmailDialog(email: String, isPending: Boolean) {
        dismissLoadingLogin()

        activity?.let {
            val dialog = Dialog(it, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.email_not_registered))
            dialog.setDesc(
                    String.format(resources.getString(
                            R.string.email_not_registered_info), email))
            dialog.setBtnOk(getString(R.string.not_registered_yes))
            dialog.setOnOkClickListener { v ->
                dialog.dismiss()
                val intent = RouteManager.getIntent(context, ApplinkConst.REGISTER)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SMART_LOGIN, true)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_PENDING, isPending)
                it.startActivity(intent)
                it.finish()
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

    override fun onErrorLoginEmail(email: String): (Throwable) -> Unit {
        return {
            stopTrace()

            if (isEmailNotActive(it, email)) {

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
        }
    }

    override fun onSuccessGetUserInfoAddPin(): (ProfilePojo) -> Unit {
        return {
            if (it.profileInfo.fullName.contains(CHARACTER_NOT_ALLOWED)) {
                onGoToChangeName()
            } else {
                checkAdditionalLoginOptionsAfterSQ()
            }
        }
    }

    override fun onErrorGetUserInfo(): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            onErrorLogin(ErrorHandler.getErrorMessage(context, it))

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

    override fun onErrorReloginAfterSQ(validateToken: String): (Throwable) -> Unit {
        return {
            dismissLoadingLogin()
            val errorMessage = ErrorHandler.getErrorMessage(context, it)
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
                presenter.reloginAfterSQ(validateToken)
            }.showRetrySnackbar()

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
            if (requestCode == REQUEST_SECURITY_QUESTION
                    && resultCode == Activity.RESULT_OK
                    && data != null) {
                data.extras?.let {
                    val validateToken = it.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                    presenter.reloginAfterSQ(validateToken)
                }
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
            } else if (requestCode == REQUEST_VERIFY_PHONE) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_REGISTER_PHONE
                    && resultCode == Activity.RESULT_OK && data != null
                    && data.extras != null) {
                val uuid = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                val msisdn = data.extras!!.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                goToAddNameFromRegisterPhone(uuid, msisdn)
            } else if (requestCode == REQUEST_ADD_NAME) {
                checkAdditionalLoginOptionsAfterSQ()
            } else if (requestCode == REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                isAutoLogin = true
                showLoading(true)
                presenter.getUserInfo()
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

                        } else {
                            checkAdditionalLoginOptions()
                            checkAdditionalLoginOptions()
                        }
                    }
                } else {
                    checkAdditionalLoginOptions()
                }

            } else if (requestCode == REQUEST_LOGIN_PHONE
                    || requestCode == REQUEST_CHOOSE_ACCOUNT) {
                dismissLoadingLogin()
            } else if (requestCode == REQUEST_ADD_PIN) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_ADD_PIN_AFTER_SQ) {
                onSuccessLogin()
            } else if (requestCode == REQUEST_COTP_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
                onSuccessLogin()
            } else {
                dismissLoadingLogin()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun isFromAccountPage(): Boolean = source == SOURCE_ACCOUNT

    private fun isFromAtcPage(): Boolean = source == SOURCE_ATC

    private fun goToAddNameFromRegisterPhone(uuid: String, msisdn: String) {
        val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER_CLEAN_VIEW
        val intent = RouteManager.getIntent(context, applink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, msisdn)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
        startActivityForResult(intent, REQUEST_ADD_NAME_REGISTER_PHONE)
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
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {

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
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                    }

                })
            }
            tickerAnnouncement.setOnClickListener { v ->
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

    private fun checkAdditionalLoginOptions() {
        presenter.checkStatusPin(onSuccessCheckStatusPin(), onErrorCheckStatusPin())
    }

    private fun checkAdditionalLoginOptionsAfterSQ() {
        presenter.checkStatusPin(onSuccessCheckStatusPinAfterSQ(), onErrorCheckStatusPin())
    }

    private fun onErrorCheckStatusPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            dismissLoadingLogin()
            view?.run {
                val errorMessage = ErrorHandler.getErrorMessage(context, it)
                Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun onSuccessCheckStatusPin(): (StatusPinData) -> Unit {
        return {
            dismissLoadingLogin()
            if (!it.isRegistered &&
                    isFromAccountPage() &&
                    userSession.phoneNumber.isNotEmpty() &&
                    userSession.isMsisdnVerified) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
                startActivityForResult(intent, REQUEST_ADD_PIN)
            } else {
                onSuccessLogin()
            }
        }
    }

    private fun onSuccessCheckStatusPinAfterSQ(): (StatusPinData) -> Unit {
        return {
            dismissLoadingLogin()
            if (!it.isRegistered &&
                    isFromAccountPage() &&
                    userSession.phoneNumber.isNotEmpty() &&
                    userSession.isMsisdnVerified) {
                activity?.run {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING)
                    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
                    startActivityForResult(intent, REQUEST_ADD_PIN_AFTER_SQ)
                }
            } else {
                onSuccessLogin()
            }
        }
    }

    private fun registerCheck(id: String) {
        if (id.isEmpty()) onErrorEmptyEmailPhone()
        else presenter.registerCheck(id, onSuccessRegisterCheck(), onErrorRegisterCheck())
    }

    private fun onErrorRegisterCheck(): (Throwable) -> Unit {
        return {
            onErrorValidateRegister(it)
        }
    }

    private fun onSuccessRegisterCheck(): (RegisterCheckData) -> Unit {
        return {

            if (TextUtils.equals(it.registerType, EMAIL_TYPE)) {
                if (it.isExist) {
                    if (!it.isPending) {
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
            sharedPrefs.edit().putLong(
                    KEY_FIRST_INSTALL_TIME_SEARCH, 0).apply()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        outState.putBoolean(IS_AUTO_LOGIN, isAutoLogin)
    }

    override fun onGetDynamicBannerSuccess(dynamicBannerDataModel: DynamicBannerDataModel) {
        if (dynamicBannerDataModel.banner.isEnable) {
            context?.let {
                ImageUtils.loadImage(
                        imageView = bannerLogin,
                        url = dynamicBannerDataModel.banner.imgUrl,
                        imageLoaded = {
                            if (it) {
                                bannerLogin.show()
                            } else {
                                bannerLogin.hide()
                                showTicker()
                            }
                        })
            }
        } else {
            showTicker()
        }
    }

    override fun onGetDynamicBannerError(throwable: Throwable) {
        bannerLogin.hide()
        showTicker()
    }

    private fun showTicker() {
        if (!GlobalConfig.isSellerApp()) {
            if (isFromAtcPage() && isShowTicker) {
                tickerAnnouncement.visibility = View.VISIBLE
                tickerAnnouncement.tickerTitle = getString(R.string.title_ticker_from_atc)
                tickerAnnouncement.setTextDescription(getString(R.string.desc_ticker_from_atc))
                tickerAnnouncement.tickerShape = Ticker.TYPE_ANNOUNCEMENT
                tickerAnnouncement.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                    override fun onDismiss() {
                    }
                })
                tickerAnnouncement.setOnClickListener {
                }
            } else {
                presenter.getTickerInfo()
            }
        }
    }

    companion object {

        const val IS_AUTO_LOGIN = "auto_login"
        const val AUTO_LOGIN_METHOD = "method"

        const val AUTO_LOGIN_EMAIL = "email"
        const val AUTO_LOGIN_PASS = "pw"

        const val IS_AUTO_FILL = "auto_fill"
        const val AUTO_FILL_EMAIL = "email"
        const val IS_FROM_REGISTER = "is_from_register"

        const val ID_ACTION_REGISTER = 111
        const val ID_ACTION_DEVOPS = 112

        const val REQUEST_SECURITY_QUESTION = 104
        const val REQUESTS_CREATE_PASSWORD = 106
        const val REQUEST_ACTIVATE_ACCOUNT = 107
        const val REQUEST_VERIFY_PHONE = 108
        const val REQUEST_ADD_NAME = 109
        const val REQUEST_CHOOSE_ACCOUNT = 110
        const val REQUEST_LOGIN_PHONE = 112
        const val REQUEST_REGISTER_PHONE = 113
        const val REQUEST_ADD_NAME_REGISTER_PHONE = 114
        const val REQUEST_ADD_PIN = 117
        const val REQUEST_COTP_PHONE_VERIFICATION = 118
        const val REQUEST_ADD_PIN_AFTER_SQ = 119

        private const val PHONE_TYPE = "phone"
        private const val EMAIL_TYPE = "email"

        private const val LOGIN_LOAD_TRACE = "gb_login_trace"
        private const val LOGIN_SUBMIT_TRACE = "gb_submit_login_trace"

        private const val SOURCE_ACCOUNT = "account"
        private const val SOURCE_ATC = "atc"

        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

        private const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        private const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

        private const val BANNER_LOGIN_URL = "https://ecs7.tokopedia.net/android/others/banner_login_register_page.png"

        private const val TOKOPEDIA_CARE_PATH = "help"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginTestAppFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
