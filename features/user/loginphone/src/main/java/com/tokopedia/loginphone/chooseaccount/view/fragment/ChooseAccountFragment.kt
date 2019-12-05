package com.tokopedia.loginphone.chooseaccount.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spanned
import android.text.format.DateFormat
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
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
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.HashMap

/**
 * @author by nisie on 12/4/17.
 */

class ChooseAccountFragment : BaseDaggerFragment(),
        ChooseAccountContract.ViewAdapter {

    private val REQUEST_SECURITY_QUESTION = 101

    lateinit var message: TextView
    lateinit var listAccount: RecyclerView
    lateinit var mainView: View
    lateinit var progressBar: ProgressBar
    lateinit var adapter: AccountAdapter

    lateinit var viewModel: com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val chooseAccountViewModel by lazy {
        viewModelProvider.get(ChooseAccountViewModel::class.java)
    }

    @Inject
    lateinit var analytics: LoginPhoneNumberAnalytics

    lateinit var mIris: Iris

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val promptText: Spanned
        get() = MethodChecker.fromHtml(getString(R.string.prompt_choose_tokocash_account,
                viewModel.accountList.userDetails.size,
                viewModel.phoneNumber))

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
            savedInstanceState != null -> viewModel = com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel(
                    savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, ""),
                    savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_UUID, ""),
                    savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, ""))
            arguments != null -> viewModel = com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel(
                    arguments?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, ""),
                    arguments?.getString(ApplinkConstInternalGlobal.PARAM_UUID, ""),
                    arguments?.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, ""))
            activity != null -> activity?.finish()
        }

        context?.run {
            mIris = IrisAnalytics.getInstance(this)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_login_phone_account, parent, false)
        setHasOptionsMenu(true)
        message = view.findViewById(R.id.message)
        listAccount = view.findViewById(R.id.list_account)
        mainView = view.findViewById(R.id.main_view)
        progressBar = view.findViewById(R.id.progress_bar)
        prepareView()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (activity != null) {
            menu?.let {
                it.add(Menu.NONE, MENU_ID_LOGOUT, 0, "")
                val menuItem = it.findItem(MENU_ID_LOGOUT)
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menuItem.icon = getDraw(activity)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDraw(context: Context?): Drawable {
        val drawable = TextDrawable(context)
        drawable.text = resources.getString(R.string.action_logout)
        return drawable
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == MENU_ID_LOGOUT) {
            goToLoginPage()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToLoginPage() {
        if (activity != null) {
            RouteManager.route(activity, ApplinkConst.LOGIN)
        }
    }

    @SuppressLint("WrongConstant")
    private fun prepareView() {
        adapter = AccountAdapter.createInstance(this, ArrayList(), "")

        listAccount.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        listAccount.adapter = adapter
    }

    private fun initObserver() {
        chooseAccountViewModel.getAccountListFBResponse.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })
        chooseAccountViewModel.getAccountListPhoneResponse.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })
        chooseAccountViewModel.loginPhoneNumberResponse.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessLoginToken()
                is Fail -> onErrorLoginToken(it.throwable)
            }
        })
        chooseAccountViewModel.getUserInfoResponse.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> onErrorGetUserInfo(it.throwable)
            }
        })
        chooseAccountViewModel.goToActivationPage.observe(this, androidx.lifecycle.Observer {
            onGoToActivationPage()
        })
        chooseAccountViewModel.goToSecurityQuestion.observe(this, androidx.lifecycle.Observer {
            onGoToSecurityQuestion()
        })
    }

    override fun onSelectedAccount(account: UserDetail, phone: String) {
        loginToken(account, phone)
    }

    private fun getAccountList() {
        when (viewModel.loginType) {
            FACEBOOK_LOGIN_TYPE -> {
                if (viewModel.accessToken.isNotEmpty())
                    chooseAccountViewModel.getAccountListFacebook(viewModel.accessToken)
            }
            else -> {
                if (viewModel.accessToken.isNotEmpty() && viewModel.phoneNumber.isNotEmpty())
                    chooseAccountViewModel.getAccountListPhoneNumber(viewModel.accessToken, viewModel.phoneNumber)
            }
        }
    }

    private fun loginToken(account: UserDetail?, phone: String) {
        account?.let {
            when (viewModel.loginType) {
                FACEBOOK_LOGIN_TYPE -> {
                    if (phone.isNotEmpty()) {
                        chooseAccountViewModel.loginTokenFacebook(
                                viewModel.accountList.key,
                                it.email,
                                phone
                        )
                    }
                }
                else -> {
                    chooseAccountViewModel.loginTokenPhone(
                            viewModel.accountList.key,
                            it.email,
                            viewModel.phoneNumber
                    )
                }
            }
        }
    }

    private fun onSuccessLogin(userId: String) {
        activity?.let {
            dismissLoadingProgress()
            analytics.eventSuccessLoginPhoneNumber()
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
            if (!GlobalConfig.DEBUG && Crashlytics.getInstance() != null)
                Crashlytics.setUserIdentifier(userId)

            if (userSessionInterface.isLoggedIn) {
                val userData = UserData()
                userData.userId = userSessionInterface.userId
                userData.email = userSessionInterface.email
                userData.phoneNumber = userSessionInterface.phoneNumber

                //Identity Event
                LinkerManager.getInstance().sendEvent(
                        LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData))

                //Login Event
                LinkerManager.getInstance().sendEvent(
                        LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData))
                loginEventAppsFlyer(userSessionInterface.userId, userSessionInterface.email)
            }

            if (::mIris.isInitialized) {
                mIris.setUserId(userId)
                mIris.setDeviceId(userSessionInterface.deviceId)
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

    private fun onSuccessLoginToken() {
        chooseAccountViewModel.getUserInfo()
    }

    private fun onErrorLoginToken(throwable: Throwable) {
        onErrorLogin(ErrorHandlerSession.getErrorMessage(throwable, context, true))
        logUnknownError(Throwable("Login Phone Number Login Token is not success"))
    }

    private fun logUnknownError(throwable: Throwable) {
        try {
            Crashlytics.logException(throwable)
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
        onErrorLogin(ErrorHandlerSession.getErrorMessage(throwable, context, true))
        logUnknownError(Throwable("Login Phone Number Get User Info is not success"))
    }

    //Impossible Flow
    private fun onGoToActivationPage(): (MessageErrorException) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, false))
            logUnknownError(Throwable("Login Phone Number Login Token go to activation"))
        }
    }

    private fun onGoToSecurityQuestion(): () -> Unit {
        return {
            8
            activity?.let {
                it.setResult(Activity.RESULT_OK, Intent().putExtra(PARAM_IS_SQ_CHECK, true))
                it.finish()
            }
        }
    }

    private fun showLoadingProgress() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoadingProgress() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun onSuccessGetAccountList(accountList: AccountList) {
        if (activity != null) {
            this.viewModel.accountList = accountList
            activity?.setResult(Activity.RESULT_OK)

            if (accountList.userDetails.size == 1 && accountList.msisdn.isNotEmpty()) {
                val userDetail = accountList.userDetails[0]
                loginToken(userDetail, accountList.msisdn)
            } else {
                dismissLoadingProgress()
                adapter.setList(accountList.userDetails, accountList.msisdn)
                message.text = promptText
            }
        }
    }

    private fun onErrorGetAccountList(e: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandlerSession.getErrorMessage(e, context, true)
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            showLoadingProgress()
            getAccountList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        showLoadingProgress()
        getAccountList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            onSuccessLogin(userSessionInterface.temporaryUserId)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_UUID, viewModel.accessToken)
        outState.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, viewModel.phoneNumber)
    }

    companion object {
        private val MENU_ID_LOGOUT = 111

        const val FACEBOOK_LOGIN_TYPE = "fb"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
