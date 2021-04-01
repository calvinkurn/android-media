package com.tokopedia.loginphone.chooseaccount.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iris.Iris
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginphone.R
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.UserDetail
import com.tokopedia.loginphone.chooseaccount.di.DaggerChooseAccountComponent
import com.tokopedia.loginphone.chooseaccount.view.adapter.AccountAdapter
import com.tokopedia.loginphone.chooseaccount.view.listener.ChooseAccountContract
import com.tokopedia.loginphone.chooseaccount.viewmodel.ChooseAccountViewModel
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.view.admin.dialog.LocationAdminDialog
import com.tokopedia.track.TrackApp
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_choose_login_phone_account.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.HashMap

/**
 * @author by nisie on 12/4/17.
 */

class ChooseAccountFragment : BaseDaggerFragment(),
        ChooseAccountContract.ViewAdapter {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: LoginPhoneNumberAnalytics

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val REQUEST_SECURITY_QUESTION = 101

    private lateinit var listAccount: RecyclerView
    private lateinit var adapter: AccountAdapter
    private lateinit var toolbarShopCreation: Toolbar
    private var crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    lateinit var mIris: Iris

    private var viewModel: com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel = com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel()
    private var selectedAccount: UserDetail? = null
    private var selectedPhoneNo: String? = null

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val chooseAccountViewModel by lazy {
        viewModelProvider.get(ChooseAccountViewModel::class.java)
    }

    override fun getScreenName(): String {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT
    }

    override fun initInjector() {
        if (activity != null) {

            val appComponent = (activity?.application as BaseMainApplication)
                    .baseAppComponent

            val loginRegisterPhoneComponent = DaggerLoginRegisterPhoneComponent.builder()
                    .baseAppComponent(appComponent).build()

            val daggerChooseAccountComponent = DaggerChooseAccountComponent.builder()
                    .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
                    .build() as DaggerChooseAccountComponent

            daggerChooseAccountComponent.inject(this)

        }
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(activity, screenName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            savedInstanceState != null -> {
                viewModel.phoneNumber = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                viewModel.accessToken = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                viewModel.loginType = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "")
                viewModel.isFromRegister = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false)
                viewModel.isFacebook = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FACEBOOK, false)
            }
            arguments != null -> {
                viewModel.phoneNumber = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                viewModel.accessToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                viewModel.loginType = arguments?.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "")
                viewModel.isFromRegister = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false) ?: false
                viewModel.isFacebook = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FACEBOOK, false) ?: false
            }
            activity != null -> activity?.finish()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_login_phone_account, parent, false)
        setHasOptionsMenu(true)
        toolbarShopCreation = view.findViewById(R.id.toolbar_shop_creation)
        listAccount = view.findViewById(R.id.chooseAccountList)
        prepareView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initObserver()
        showLoadingProgress()
        getAccountList()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarShopCreation)
            it.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(it, R.color.transparent)))
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun prepareView() {
        adapter = AccountAdapter.createInstance(this, ArrayList(), "")

        listAccount.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        listAccount.adapter = adapter
    }

    private fun initObserver() {
        chooseAccountViewModel.getAccountListFBResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })
        chooseAccountViewModel.getAccountListPhoneResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })
        chooseAccountViewModel.loginPhoneNumberResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessLoginToken(it.data)
                is Fail -> {
                    dismissLoadingProgress()
                    onErrorLoginToken(it.throwable)
                }
            }
        })
        chooseAccountViewModel.getUserInfoResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> {
                    dismissLoadingProgress()
                    onErrorGetUserInfo(it.throwable)
                }
            }
        })
        chooseAccountViewModel.showPopup.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                showPopupError(it.header, it.body, it.action)
            }
        })
        chooseAccountViewModel.goToActivationPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToActivationPage()
        })
        chooseAccountViewModel.goToSecurityQuestion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToSecurityQuestion()
        })
        chooseAccountViewModel.showAdminLocationPopUp.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it) {
                is Success -> showLocationAdminPopUp()
                is Fail -> showLocationAdminError(it.throwable)
            }
        })
    }

    private fun showLocationAdminPopUp() {
        LocationAdminDialog(context) {
            userSessionInterface.logoutSession()
            activity?.onBackPressed()
        }.show()
    }

    private fun showLocationAdminError(error: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, error)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
        dismissLoadingProgress()
    }

    override fun onSelectedAccount(account: UserDetail, phone: String) {
        showLoadingProgress()
        if (account.challenge2Fa) {
            open2FA(account, phone)
        } else {
            loginToken(account, phone)
        }
    }

    private fun open2FA(account: UserDetail, phone: String) {
        selectedAccount = account
        selectedPhoneNo = phone
        showLoadingProgress()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_AFTER_LOGIN_PHONE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, account.email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID_ENC, account.userIdEnc)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, viewModel.accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID, account.userId)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, OTP_MODE_PIN);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_CODE_PIN_CHALLENGE)
    }

    private fun getAccountList() {
        when (viewModel.loginType) {
            FACEBOOK_LOGIN_TYPE -> {
                viewModel.accessToken?.let {
                    if (it.isNotEmpty())
                        chooseAccountViewModel.getAccountListFacebook(it)
                }
            }
            else -> {
                LetUtil.ifLet(viewModel.accessToken, viewModel.phoneNumber) { (accessToken, phoneNumber) ->
                    chooseAccountViewModel.getAccountListPhoneNumber(accessToken, phoneNumber)
                }
            }
        }
    }

    private fun loginToken(account: UserDetail?, phone: String) {
        account?.let {
            when (viewModel.loginType) {
                FACEBOOK_LOGIN_TYPE -> {
                    if (phone.isNotEmpty()) {
                        viewModel.accountList?.key?.let { key ->
                            chooseAccountViewModel.loginTokenFacebook(
                                    key,
                                    it.email,
                                    phone
                            )
                        }
                    }
                }
                else -> {
                    LetUtil.ifLet(viewModel.accountList, viewModel.phoneNumber) { (accountList, phoneNumber) ->
                        if (accountList is AccountList && phoneNumber is String) {
                            chooseAccountViewModel.loginTokenPhone(
                                    accountList.key,
                                    it.email,
                                    phoneNumber
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessLogin(userId: String) {
        activity?.let {
            dismissLoadingProgress()
            if(!viewModel.isFromRegister) {
                analytics.eventSuccessLoginPhoneNumber()
            } else {
                if (viewModel.isFacebook) {
                    analytics.eventSuccessFbPhoneNumber()
                } else {
                    analytics.eventSuccessLoginPhoneNumberSmartRegister()
                }
            }
            setTrackingUserId(userId)
            setFCM()
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun setFCM() {
        CMPushNotificationManager.instance
                .refreshFCMTokenFromForeground(userSessionInterface.deviceId, true)
    }

    private fun setTrackingUserId(userId: String) {
        try {
            TkpdAppsFlyerMapper.getInstance(activity?.applicationContext).mapAnalytics()
            TrackApp.getInstance().gtm
                    .pushUserId(userId)
            if (!GlobalConfig.DEBUG && crashlytics != null)
                crashlytics.setUserId(userId)

            if (userSessionInterface.isLoggedIn) {
                val userData = UserData()
                userData.userId = userSessionInterface.userId
                userData.email = userSessionInterface.email
                userData.phoneNumber = userSessionInterface.phoneNumber
                userData.medium = userSessionInterface.loginMethod

                //Identity Event
                LinkerManager.getInstance().sendEvent(
                        LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData))

                //Login Event
                LinkerManager.getInstance().sendEvent(
                        LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData))
                loginEventAppsFlyer(userSessionInterface.userId, "")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginEventAppsFlyer(userId: String, userEmail: String) {
        var dataMap = HashMap<String, Any>()
        dataMap["user_id"] = userId
        dataMap["user_email"] = userEmail
        val date = Date()
        val stringDate = DateFormat.format("EEEE, MMMM d, yyyy ", date.time)
        dataMap["timestamp"] = stringDate
        TrackApp.getInstance().appsFlyer.sendTrackEvent("Login Successful", dataMap)
    }

    private fun onSuccessLoginToken(loginToken: LoginToken) {
        chooseAccountViewModel.getUserInfo()
    }

    private fun onErrorLoginToken(throwable: Throwable) {
        if (throwable is AkamaiErrorException) {
            showPopupErrorAkamai()
        } else {
            onErrorLogin(ErrorHandler.getErrorMessage(context, throwable))
        }
        logUnknownError(Throwable("Login Phone Number Login Token is not success"))
    }

    private fun logUnknownError(throwable: Throwable) {
        try {
            crashlytics.recordException(throwable)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun onErrorLogin(errorMessage: String) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun onSuccessGetUserInfo(profileInfo: ProfileInfo) {
        onSuccessLogin(profileInfo.userId)
    }

    private fun onErrorGetUserInfo(throwable: Throwable) {
        onErrorLogin(ErrorHandler.getErrorMessage(context, throwable))
        logUnknownError(Throwable("Login Phone Number Get User Info is not success"))
    }

    //Impossible Flow
    private fun onGoToActivationPage(): (MessageErrorException) -> Unit {
        return {
            onErrorLogin(ErrorHandler.getErrorMessage(context, it))
            logUnknownError(Throwable("Login Phone Number Login Token go to activation"))
        }
    }

    private fun onGoToSecurityQuestion(): () -> Unit {
        return {
            activity?.let {
                it.setResult(Activity.RESULT_OK, Intent().putExtra(PARAM_IS_SQ_CHECK, true))
                it.finish()
            }
        }
    }

    private fun showLoadingProgress() {
        chooseAccountTitle?.visibility = View.GONE
        chooseAccountList?.visibility = View.GONE
        chooseAccountLoader?.visibility = View.VISIBLE
    }

    private fun dismissLoadingProgress() {
        chooseAccountTitle?.visibility = View.VISIBLE
        chooseAccountList?.visibility = View.VISIBLE
        chooseAccountLoader?.visibility = View.GONE
    }

    private fun onSuccessGetAccountList(accountList: AccountList) {
        this.viewModel.accountList = accountList

        if (accountList.userDetails.size == 1 && accountList.msisdn.isNotEmpty()) {
            adapter.setList(accountList.userDetails, accountList.msisdn)
            val userDetail = accountList.userDetails[0]
            if (userDetail.challenge2Fa) {
                open2FA(userDetail, accountList.msisdn)
            } else {
                loginToken(userDetail, accountList.msisdn)
            }
        } else {
            dismissLoadingProgress()
            adapter.setList(accountList.userDetails, accountList.msisdn)
        }
    }

    private fun onErrorGetAccountList(e: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandler.getErrorMessage(context, e)
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            showLoadingProgress()
            getAccountList()
        }
    }

    private fun showPopupError(header: String, body: String, url: String) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(header)
            dialog.setDescription(body)
            dialog.setPrimaryCTAText(getString(R.string.check_full_information))
            dialog.setSecondaryCTAText(getString(R.string.close_popup))
            dialog.setPrimaryCTAClickListener {
                RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
            }
            dialog.setSecondaryCTAClickListener {
                dialog.hide()
            }
            dialog.show()
        }
    }

    private fun showPopupErrorAkamai() {
        showPopupError(
                getString(R.string.popup_error_title),
                getString(R.string.popup_error_desc),
                TokopediaUrl.getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            onSuccessLogin(userSessionInterface.temporaryUserId)
        } else if (requestCode == REQUEST_CODE_PIN_CHALLENGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (selectedAccount != null && !selectedPhoneNo.isNullOrEmpty()) {
                    loginToken(selectedAccount, selectedPhoneNo ?: "")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_UUID, viewModel.accessToken)
        outState.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, viewModel.phoneNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        chooseAccountViewModel.getAccountListFBResponse.removeObservers(this)
        chooseAccountViewModel.getAccountListPhoneResponse.removeObservers(this)
        chooseAccountViewModel.loginPhoneNumberResponse.removeObservers(this)
        chooseAccountViewModel.getUserInfoResponse.removeObservers(this)
        chooseAccountViewModel.goToActivationPage.removeObservers(this)
        chooseAccountViewModel.goToSecurityQuestion.removeObservers(this)
        chooseAccountViewModel.flush()
    }

    companion object {
        private val MENU_ID_LOGOUT = 111

        const val FACEBOOK_LOGIN_TYPE = "fb"
        const val REQUEST_CODE_PIN_CHALLENGE = 112
        const val PARAM_IS_2FA_KEY = "KEY_FROM_2FA_CHALLENGE"
        const val PARAM_IS_2FA = 113

        private const val OTP_TYPE_AFTER_LOGIN_PHONE = 148
        private const val OTP_MODE_PIN = "PIN"

        private const val TOKOPEDIA_CARE_PATH = "help"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
