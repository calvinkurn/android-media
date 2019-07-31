package com.tokopedia.loginphone.choosetokocashaccount.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.text.format.DateFormat
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginphone.R
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountList
import com.tokopedia.loginphone.choosetokocashaccount.data.ChooseTokoCashAccountViewModel
import com.tokopedia.loginphone.choosetokocashaccount.data.UserDetail
import com.tokopedia.loginphone.choosetokocashaccount.di.DaggerChooseAccountComponent
import com.tokopedia.loginphone.choosetokocashaccount.view.adapter.TokocashAccountAdapter
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract
import com.tokopedia.loginphone.choosetokocashaccount.view.presenter.ChooseTokocashAccountPresenter
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.HashMap

/**
 * @author by nisie on 12/4/17.
 */

class ChooseTokocashAccountFragment : BaseDaggerFragment(), ChooseTokocashAccountContract.View,
        ChooseTokocashAccountContract.ViewAdapter {

    private val REQUEST_SECURITY_QUESTION = 101

    lateinit var message: TextView
    lateinit var listAccount: RecyclerView
    lateinit var mainView: View
    lateinit var progressBar: ProgressBar
    lateinit var adapter: TokocashAccountAdapter

    lateinit var viewModel: ChooseTokoCashAccountViewModel

    @Inject
    lateinit var presenter: ChooseTokocashAccountPresenter

    @Inject
    lateinit var analytics: LoginPhoneNumberAnalytics

    lateinit var mIris: Iris

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val promptText: Spanned
        get() = MethodChecker.fromHtml(getString(R.string.prompt_choose_tokocash_account,
                viewModel.accountList.accountListPojo.userDetails.size,
                viewModel.phoneNumber))

    override fun getScreenName(): String {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT
    }

    override fun initInjector() {
        if (activity != null) {

            val appComponent = (activity!!.application as BaseMainApplication)
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
            savedInstanceState != null -> viewModel = ChooseTokoCashAccountViewModel(
                    savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, ""),
                    savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_UUID, ""))
            arguments != null -> viewModel = ChooseTokoCashAccountViewModel(
                    arguments!!.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, ""),
                    arguments!!.getString(ApplinkConstInternalGlobal.PARAM_UUID, ""))
            activity != null -> activity!!.finish()
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
        presenter.attachView(this)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (activity != null) {
            menu?.let{
                it.add(Menu.NONE, MENU_ID_LOGOUT, 0, "")
                val menuItem = it.findItem(MENU_ID_LOGOUT)
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menuItem.icon = getDraw(activity)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDraw(context: Context?): Drawable {
        val drawable = TextDrawable(context!!)
        drawable.text = resources.getString(R.string.action_logout)
        return drawable
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
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

    private fun prepareView() {
        adapter = TokocashAccountAdapter.createInstance(this, ArrayList())

        listAccount.layoutManager = LinearLayoutManager(activity, LinearLayoutManager
                .VERTICAL, false)
        listAccount.adapter = adapter
    }


    override fun onSelectedTokocashAccount(accountTokocash: UserDetail) {
        presenter.loginWithTokocash(
                viewModel.accountList.accountListPojo.key,
                accountTokocash,
                viewModel.phoneNumber)
    }

    override fun onSuccessLogin(userId: String) {
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

    private fun loginEventAppsFlyer(userId:String, userEmail:String){
        var dataMap = HashMap<String, Any>()
        dataMap.put("user_id", userId)
        dataMap.put("user_email", userEmail)
        val date = Date()
        val stringDate = DateFormat.format("EEEE, MMMM d, yyyy ", date.time)
        dataMap.put("timestamp", stringDate)
        TrackApp.getInstance().appsFlyer.sendTrackEvent("Login Successful", dataMap)
    }

    override fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit {
        return {
            presenter.getUserInfo()
        }
    }

    override fun onErrorLoginToken(): (Throwable) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, true))
            logUnknownError(Throwable("Login Phone Number Login Token is not success"))
        }
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

    override fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit {
        return {
            onSuccessLogin(it.profileInfo.userId)
        }
    }

    override fun onErrorGetUserInfo(): (Throwable) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, true))
            logUnknownError(Throwable("Login Phone Number Get User Info is not success"))
        }
    }

    //Impossible Flow
    override fun onGoToActivationPage(): (MessageErrorException) -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getErrorMessage(it, context, false))
            logUnknownError(Throwable("Login Phone Number Login Token go to activation"))
        }
    }

    //Impossible Flow
    override fun onGoToSecurityQuestion(): () -> Unit {
        return {
            onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context))
            logUnknownError(Throwable("Login Phone Number Login Token go to sq"))
        }
    }

    //Impossible Flow
    override fun onGoToCreatePassword(): (String, String) -> Unit {
        return { fullName: String, userId: String ->
            onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, context))
            logUnknownError(Throwable("Login Phone Number Login Token go to create password"))
        }
    }

    override fun showLoadingProgress() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissLoadingProgress() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onSuccessGetAccountList(accountList: AccountList) {
        if (activity != null) {
            this.viewModel.accountList = accountList
            activity!!.setResult(Activity.RESULT_OK)

            if (accountList.accountListPojo.userDetails.size == 1) {
                val userDetail = accountList.accountListPojo.userDetails[0]
                presenter.loginWithTokocash(viewModel.accountList.accountListPojo.key,
                        userDetail,
                        viewModel.phoneNumber)
            } else {
                dismissLoadingProgress()
                adapter.setList(accountList.accountListPojo.userDetails)
                message.text = promptText
            }
        }
    }

    override fun onErrorGetAccountList(e: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandlerSession.getErrorMessage(e, context, true)
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            showLoadingProgress()
            presenter.getAccountList(viewModel.accessToken, viewModel.phoneNumber)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingProgress()
        presenter.getAccountList(viewModel.accessToken, viewModel.phoneNumber)
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        private val MENU_ID_LOGOUT = 111

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseTokocashAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
