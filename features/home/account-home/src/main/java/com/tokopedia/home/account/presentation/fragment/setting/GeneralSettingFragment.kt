package com.tokopedia.home.account.presentation.fragment.setting

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView

import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.Dialog
import com.tokopedia.home.account.AccountHomeRouter
import com.tokopedia.home.account.R
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.constant.SettingConstant
import com.tokopedia.home.account.di.component.AccountLogoutComponent
import com.tokopedia.home.account.di.component.DaggerAccountLogoutComponent
import com.tokopedia.home.account.presentation.activity.AccountSettingActivity
import com.tokopedia.home.account.presentation.activity.SettingWebViewActivity
import com.tokopedia.home.account.presentation.activity.StoreSettingActivity
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity
import com.tokopedia.home.account.presentation.adapter.setting.GeneralSettingAdapter
import com.tokopedia.home.account.presentation.listener.LogoutView
import com.tokopedia.home.account.presentation.presenter.LogoutPresenter
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.url.TokopediaUrl

import java.util.ArrayList

import javax.inject.Inject

import com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT
import com.tokopedia.home.account.AccountConstants.Analytics.APPLICATION_REVIEW
import com.tokopedia.home.account.AccountConstants.Analytics.CLEAR_CACHE
import com.tokopedia.home.account.AccountConstants.Analytics.DEVELOPER_OPTIONS
import com.tokopedia.home.account.AccountConstants.Analytics.HELP_CENTER
import com.tokopedia.home.account.AccountConstants.Analytics.LOGOUT
import com.tokopedia.home.account.AccountConstants.Analytics.NOTIFICATION
import com.tokopedia.home.account.AccountConstants.Analytics.PAYMENT_METHOD
import com.tokopedia.home.account.AccountConstants.Analytics.PRIVACY_POLICY
import com.tokopedia.home.account.AccountConstants.Analytics.SETTING
import com.tokopedia.home.account.AccountConstants.Analytics.SHAKE_SHAKE
import com.tokopedia.home.account.AccountConstants.Analytics.SHOP
import com.tokopedia.home.account.AccountConstants.Analytics.TERM_CONDITION
import com.tokopedia.home.account.constant.SettingConstant.Url.PATH_CHECKOUT_TEMPLATE

class GeneralSettingFragment : BaseGeneralSettingFragment(), LogoutView, GeneralSettingAdapter.SwitchSettingListener {

    @Inject
    internal var presenter: LogoutPresenter? = null
    @Inject
    internal var walletPref: WalletPref? = null

    private var loadingView: View? = null
    private var baseSettingView: View? = null

    private var accountAnalytics: AccountAnalytics? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountAnalytics = AccountAnalytics(activity)
        permissionCheckerHelper = PermissionCheckerHelper()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) {
            adapter.updateSettingItem(SettingConstant.SETTING_GEOLOCATION_ID)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return if (activity != null) {
            permissionCheckerHelper!!.hasPermission(activity!!,
                    arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION))

        } else false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val component = DaggerAccountLogoutComponent.builder().baseAppComponent(
                (activity!!.application as BaseMainApplication)
                        .baseAppComponent).build()
        component.inject(this)
        presenter!!.attachView(this)

        return inflater.inflate(R.layout.fragment_general_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView = view.findViewById(R.id.logout_status)
        baseSettingView = view.findViewById(R.id.setting_layout)
        adapter.setSwitchSettingListener(this)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(DividerItemDecoration(activity))
        val appVersion = view.findViewById<TextView>(R.id.text_view_app_version)
        appVersion.text = getString(R.string.application_version_fmt, GlobalConfig.VERSION_NAME)
    }

    override fun getSettingItems(): List<SettingItemViewModel> {
        val settingItems = ArrayList<SettingItemViewModel>()
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_ACCOUNT_ID,
                getString(R.string.title_account_setting), getString(R.string.subtitle_account_setting)))
        if (userSession.hasShop()) {
            settingItems.add(SettingItemViewModel(SettingConstant.SETTING_SHOP_ID,
                    getString(R.string.account_home_title_shop_setting), getString(R.string.account_home_subtitle_shop_setting)))
        }

        val walletModel = walletPref!!.retrieveWallet()
        val walletName = if (walletModel != null) walletModel.text + ", " else ""
        val settingDescTkpdPay = walletName + getString(R.string.subtitle_tkpd_pay_setting)
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_TKPD_PAY_ID,
                getString(R.string.title_tkpd_pay_setting), settingDescTkpdPay))
        if (activity != null && activity!!.application is AccountHomeRouter &&
                (activity!!.application as AccountHomeRouter).getBooleanRemoteConfig(
                        RemoteConfigKey.CHECKOUT_TEMPLATE_SETTING_TOGGLE, false)) {
            settingItems.add(SettingItemViewModel(SettingConstant.SETTING_TEMPLATE_ID,
                    getString(R.string.title_tkpd_template_setting), getString(R.string.subtitle_template_setting)))
        }
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_NOTIFICATION_ID,
                getString(R.string.title_notification_setting), getString(R.string.subtitle_notification_setting)))
        settingItems.add(SwitchSettingItemViewModel(SettingConstant.SETTING_SHAKE_ID,
                getString(R.string.title_shake_setting), getString(R.string.subtitle_shake_setting), false))
        settingItems.add(SwitchSettingItemViewModel(SettingConstant.SETTING_GEOLOCATION_ID,
                getString(R.string.title_geolocation_setting), getString(R.string.subtitle_geolocation_setting), true))

        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_TNC_ID,
                getString(R.string.title_tnc_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_PRIVACY_ID,
                getString(R.string.title_privacy_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_APP_REVIEW_ID,
                getString(R.string.title_app_review_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_APP_CLEAR_CACHE,
                getString(R.string.title_app_clear_cache)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_HELP_CENTER_ID,
                getString(R.string.title_help_center_setting)))

        if (GlobalConfig.isAllowDebuggingTools()) {
            settingItems.add(SettingItemViewModel(SettingConstant.SETTING_DEV_OPTIONS,
                    getString(R.string.title_dev_options)))
        }

        val itemOut = SettingItemViewModel(SettingConstant.SETTING_OUT_ID, getString(R.string.account_home_button_logout))
        itemOut.iconResource = R.drawable.ic_setting_out
        itemOut.isHideArrow = true
        settingItems.add(itemOut)
        return settingItems
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onItemClicked(settingId: Int) {
        when (settingId) {
            SettingConstant.SETTING_ACCOUNT_ID -> {
                accountAnalytics!!.eventClickSetting(ACCOUNT)
                startActivity(AccountSettingActivity.createIntent(activity))
            }
            SettingConstant.SETTING_SHOP_ID -> {
                accountAnalytics!!.eventClickSetting(String.format("%s %s", SHOP, SETTING))
                startActivity(StoreSettingActivity.createIntent(activity))
            }
            SettingConstant.SETTING_TKPD_PAY_ID -> {
                accountAnalytics!!.eventClickSetting(PAYMENT_METHOD)
                startActivity(TkpdPaySettingActivity.createIntent(activity))
            }
            SettingConstant.SETTING_TEMPLATE_ID -> if (activity != null) {
                val applink = String.format("%s?url=%s", ApplinkConst.WEBVIEW, TokopediaUrl.getInstance().MOBILEWEB + PATH_CHECKOUT_TEMPLATE)
                RouteManager.route(activity, applink)
            }
            SettingConstant.SETTING_NOTIFICATION_ID -> {
                RouteManager.route(context, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING)
                accountAnalytics!!.eventClickSetting(NOTIFICATION)
            }
            SettingConstant.SETTING_TNC_ID -> {
                accountAnalytics!!.eventClickSetting(TERM_CONDITION)
                gotoWebviewActivity(SettingConstant.Url.PATH_TERM_CONDITION, getString(R.string.title_tnc_setting))
            }
            SettingConstant.SETTING_PRIVACY_ID -> {
                accountAnalytics!!.eventClickSetting(PRIVACY_POLICY)
                gotoWebviewActivity(SettingConstant.Url.PATH_PRIVACY_POLICY, getString(R.string.title_privacy_setting))
            }
            SettingConstant.SETTING_APP_REVIEW_ID -> {
                accountAnalytics!!.eventClickSetting(APPLICATION_REVIEW)
                goToPlaystore()
            }
            SettingConstant.SETTING_HELP_CENTER_ID -> {
                accountAnalytics!!.eventClickSetting(HELP_CENTER)
                RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
            }
            SettingConstant.SETTING_OUT_ID -> {
                accountAnalytics!!.eventClickSetting(LOGOUT)
                showDialogLogout()
            }
            SettingConstant.SETTING_APP_CLEAR_CACHE -> {
                accountAnalytics!!.eventClickSetting(CLEAR_CACHE)
                clearCache()
            }
            SettingConstant.SETTING_DEV_OPTIONS -> if (GlobalConfig.isAllowDebuggingTools()) {
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
            }
            else -> {
            }
        }
    }

    private fun clearCache() {}

    private fun goToPlaystore() {

        val uri = Uri.parse("market://details?id=" + activity!!.application.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            activity!!.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            activity!!.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse(SettingConstant.PLAYSTORE_URL + activity!!.application.packageName)))
        }

    }

    private fun showDialogLogout() {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.account_home_label_logout))
        dialog.setDesc(getString(R.string.account_home_label_logout_confirmation))
        dialog.setBtnOk(getString(R.string.account_home_button_logout))
        dialog.setBtnCancel(getString(R.string.account_home_label_cancel))
        dialog.setOnOkClickListener { v ->
            dialog.dismiss()
            doLogout()
        }
        dialog.setOnCancelClickListener { v -> dialog.dismiss() }
        dialog.show()
    }

    private fun doLogout() {
        FacebookSdk.sdkInitialize(activity!!.applicationContext)
        showLoading(true)
        presenter!!.doLogout()
    }

    private fun showLoading(isLoading: Boolean) {
        val shortAnimTime = resources.getInteger(
                android.R.integer.config_shortAnimTime)

        loadingView!!.animate().setDuration(shortAnimTime.toLong())
                .alpha((if (isLoading) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (loadingView != null) {
                            loadingView!!.visibility = if (isLoading) View.VISIBLE else View.GONE
                        }
                    }
                })

        baseSettingView!!.animate().setDuration(shortAnimTime.toLong())
                .alpha((if (isLoading) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (baseSettingView != null) {
                            baseSettingView!!.visibility = if (isLoading) View.GONE else View.VISIBLE
                        }
                    }
                })
    }

    private fun saveSettingValue(key: String, isChecked: Boolean) {
        val settings = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = settings.edit()
        editor.putBoolean(key, isChecked)
        editor.apply()
    }

    override fun isSwitchSelected(settingId: Int): Boolean {
        when (settingId) {
            SettingConstant.SETTING_SHAKE_ID -> return isItemSelected(getString(R.string.pref_receive_shake), true)
            SettingConstant.SETTING_GEOLOCATION_ID -> return hasLocationPermission()
            else -> return false
        }
    }

    override fun onChangeChecked(settingId: Int, value: Boolean) {
        when (settingId) {
            SettingConstant.SETTING_SHAKE_ID -> {
                accountAnalytics!!.eventClickSetting(SHAKE_SHAKE)
                saveSettingValue(getString(R.string.pref_receive_shake), value)
            }
            else -> {
            }
        }
    }


    override fun onClicked(settingId: Int, currentValue: Boolean) {
        when (settingId) {
            SettingConstant.SETTING_GEOLOCATION_ID -> createAndShowLocationAlertDialog(currentValue)
        }
    }

    private fun isItemSelected(key: String): Boolean {
        val settings = PreferenceManager.getDefaultSharedPreferences(activity)
        return settings.getBoolean(key, false)
    }

    private fun isItemSelected(key: String, defaultValue: Boolean): Boolean {
        val settings = PreferenceManager.getDefaultSharedPreferences(activity)
        return settings.getBoolean(key, defaultValue)
    }

    private fun gotoWebviewActivity(path: String, title: String) {
        val intent: Intent
        val url = String.format("%s%s", SettingConstant.Url.BASE_MOBILE, path)
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent = SettingWebViewActivity.createIntent(activity, url, title)
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
        }
        startActivity(intent)
    }

    private fun goToApplicationDetailActivity() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.data = uri
        activity!!.startActivity(intent)
    }

    override fun logoutFacebook() {
        LoginManager.getInstance().logOut()
    }

    override fun onErrorLogout(throwable: Throwable) {
        showLoading(false)
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, throwable))
    }

    override fun onSuccessLogout() {
        showLoading(false)
        if (activity!!.application is AccountHomeRouter) {
            (activity!!.application as AccountHomeRouter).doLogoutAccount(activity)
        }

        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)
    }

    override fun onDestroyView() {
        presenter!!.detachView()
        super.onDestroyView()
    }

    private fun createAndShowLocationAlertDialog(currentValue: Boolean) {
        if (!currentValue) {
            accountAnalytics!!.eventClickToggleOnGeolocation(activity)
        } else {
            accountAnalytics!!.eventClickToggleOffGeolocation(activity)
        }

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(R.string.account_home_title_geolocation_alertdialog)
        builder.setPositiveButton(R.string.account_home_ok_geolocation_alertdialog) { dialog, id -> goToApplicationDetailActivity() }
        builder.setNegativeButton(R.string.account_home_batal_geolocation_alertdialog) { dialog, id -> }
        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }

    companion object {

        fun createInstance(): Fragment {
            return GeneralSettingFragment()
        }

        private val TAG = GeneralSettingFragment::class.java.simpleName
    }
}
