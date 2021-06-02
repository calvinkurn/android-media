package com.tokopedia.home.account.presentation.fragment.setting

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.dialog.AccessRequestDialogFragment
import com.tokopedia.home.account.AccountConstants.Analytics.ABOUT_US
import com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT
import com.tokopedia.home.account.AccountConstants.Analytics.ADVANCED_SETTING
import com.tokopedia.home.account.AccountConstants.Analytics.APPLICATION_REVIEW
import com.tokopedia.home.account.AccountConstants.Analytics.DEVELOPER_OPTIONS
import com.tokopedia.home.account.AccountConstants.Analytics.HELP_CENTER
import com.tokopedia.home.account.AccountConstants.Analytics.LOGOUT
import com.tokopedia.home.account.AccountConstants.Analytics.NOTIFICATION
import com.tokopedia.home.account.AccountConstants.Analytics.PAYMENT_METHOD
import com.tokopedia.home.account.AccountConstants.Analytics.PRIVACY_POLICY
import com.tokopedia.home.account.AccountConstants.Analytics.SAFE_MODE
import com.tokopedia.home.account.AccountConstants.Analytics.SHAKE_SHAKE
import com.tokopedia.home.account.AccountConstants.Analytics.TERM_CONDITION
import com.tokopedia.home.account.R
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.constant.SettingConstant
import com.tokopedia.home.account.constant.SettingConstant.Url.PATH_CHECKOUT_TEMPLATE
import com.tokopedia.home.account.data.util.NotifPreference
import com.tokopedia.home.account.di.component.DaggerAccountLogoutComponent
import com.tokopedia.home.account.di.module.SettingsModule
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity
import com.tokopedia.home.account.presentation.adapter.setting.GeneralSettingAdapter
import com.tokopedia.home.account.presentation.listener.RedDotGimmickView
import com.tokopedia.home.account.presentation.listener.SettingOptionsView
import com.tokopedia.home.account.presentation.presenter.RedDotGimmickPresenter
import com.tokopedia.home.account.presentation.presenter.SettingsPresenter
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel
import com.tokopedia.internal_review.factory.createReviewHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.util.initializeSellerMigrationAccountSettingTicker
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_general_setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class GeneralSettingFragment : BaseGeneralSettingFragment(), RedDotGimmickView, GeneralSettingAdapter.SwitchSettingListener, SettingOptionsView {
    @Inject
    internal lateinit var presenter: RedDotGimmickPresenter
    @Inject
    internal lateinit var walletPref: WalletPref

    @Inject
    internal lateinit var settingsPresenter: SettingsPresenter


    private lateinit var loadingView: View
    private lateinit var baseSettingView: View
    private lateinit var updateButton: UnifyButton

    private lateinit var accountAnalytics: AccountAnalytics
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var notifPreference: NotifPreference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(context) }
    private val reviewHelper by lazy { createReviewHelper(context?.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountAnalytics = AccountAnalytics(activity)
        permissionCheckerHelper = PermissionCheckerHelper()
        context?.let {
            notifPreference = NotifPreference(it)
        }

        activity?.run {
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getGoogleClientId(this))
                    .requestEmail()
                    .requestProfile()
                    .build()

            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter?.updateSettingItem(SettingConstant.SETTING_GEOLOCATION_ID)
    }

    private fun hasLocationPermission(): Boolean {
        activity?.let {
            permissionCheckerHelper.let { permission ->
                return permission.hasPermission(it,
                        arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION))
            }
        }

        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.let {
            val component = DaggerAccountLogoutComponent.builder().baseAppComponent(
                    (it.application as BaseMainApplication)
                            .baseAppComponent).settingsModule(SettingsModule(activity)).build()
            component.inject(this)
            settingsPresenter.attachView(this)
            if (savedInstanceState == null) {
                settingsPresenter.verifyUserAge()
            } else {
                setupSafeSearchLocally()
            }
        }
        presenter.attachView(this)

        return inflater.inflate(R.layout.fragment_general_setting, container, false)
    }

    private fun setupSafeSearchLocally() {
        val isAdultAge = isItemSelected(
                SettingsPresenter.PREFERENCE_ADULT_AGE_VERIFIED_KEY, false)
        settingsPresenter.adultAgeVerified = isAdultAge
        if(isAdultAge) {
            refreshSafeSearchOption()
        }
    }

    //Request to hide Dark Mode regardless RemoteConfig
    private fun showDarkModeSetting(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView = view.findViewById(R.id.logout_status)
        baseSettingView = view.findViewById(R.id.setting_layout)
        adapter.setSwitchSettingListener(this)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(DividerItemDecoration(activity))
        val appVersion = view.findViewById<TextView>(R.id.text_view_app_version)
        updateButton = view.findViewById(R.id.force_update_button)
        appVersion.text = getString(R.string.application_version_fmt, GlobalConfig.RAW_VERSION_NAME)
        showForceUpdate()
    }

    override fun getSettingItems(): List<SettingItemViewModel> {
        val settingItems = ArrayList<SettingItemViewModel>()
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_ACCOUNT_ID,
                getString(R.string.general_setting_title_account_setting_item), getString(R.string.subtitle_account_setting)))

        val walletModel = try { walletPref.retrieveWallet() } catch (throwable: Throwable) { null }
        val walletName = if (walletModel != null) {
            walletModel.text + ", "
        } else {
            ""
        }
        val settingDescTkpdPay = walletName + getString(R.string.subtitle_tkpd_pay_setting)

        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_TKPD_PAY_ID,
                getString(R.string.title_tkpd_pay_setting), settingDescTkpdPay))
        activity?.let {
            if (remoteConfig.getBoolean(
                            RemoteConfigKey.CHECKOUT_TEMPLATE_SETTING_TOGGLE, false)
            ) {
                settingItems.add(SettingItemViewModel(SettingConstant.SETTING_TEMPLATE_ID,
                        getString(R.string.title_tkpd_template_setting), getString(R.string.subtitle_template_setting)))
            }
        }

        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_NOTIFICATION_ID,
                getString(R.string.title_notification_setting), getString(R.string.subtitle_notification_setting)))
        settingItems.add(SwitchSettingItemViewModel(SettingConstant.SETTING_SHAKE_ID,
                getString(R.string.title_shake_setting), getString(R.string.subtitle_shake_setting), false))
        settingItems.add(SwitchSettingItemViewModel(SettingConstant.SETTING_GEOLOCATION_ID,
                getString(R.string.title_geolocation_setting), getString(R.string.subtitle_geolocation_setting), true))

        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_IMAGE_QUALITY,
                getString(R.string.image_quality_setting_screen), getString(R.string.image_quality_setting_title)))

        if (settingsPresenter.adultAgeVerified)
            settingItems.add(SwitchSettingItemViewModel(SettingConstant.SETTING_SAFE_SEARCH_ID,
                    getString(R.string.title_safe_mode_setting), getString(R.string.subtitle_safe_mode_setting), true))

        val isShowDarkMode = remoteConfig.getBoolean(
                RemoteConfigKey.SETTING_SHOW_DARK_MODE_TOGGLE, false)
        if(isShowDarkMode && showDarkModeSetting()) {
            settingItems.add(SwitchSettingItemViewModel(SettingConstant.SETTING_DARK_MODE,
                    getString(R.string.title_dark_mode), getString(R.string.subtitle_dark_mode), false))
        }

        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_ABOUT_US,
                getString(R.string.title_about_us)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_TNC_ID,
                getString(R.string.title_tnc_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_PRIVACY_ID,
                getString(R.string.title_privacy_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_APP_REVIEW_ID,
                getString(R.string.title_app_review_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_HELP_CENTER_ID,
                getString(R.string.title_help_center_setting)))
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_APP_ADVANCED_SETTING,
                getString(R.string.title_app_advanced_setting)))


        if (GlobalConfig.APPLICATION_TYPE == 3) {
            settingItems.add(SettingItemViewModel(SettingConstant.SETTING_FEEDBACK_FORM,
                    getString(R.string.feedback_form)))
        }

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
                accountAnalytics.eventClickSetting(ACCOUNT)
                RouteManager.route(activity, ApplinkConstInternalGlobal.ACCOUNT_SETTING)
            }
            SettingConstant.SETTING_TKPD_PAY_ID -> {
                accountAnalytics.eventClickSetting(PAYMENT_METHOD)
                startActivity(TkpdPaySettingActivity.createIntent(activity))
            }
            SettingConstant.SETTING_TEMPLATE_ID -> if (activity != null) {
                val applink = String.format("%s?url=%s", ApplinkConst.WEBVIEW, TokopediaUrl.getInstance().MOBILEWEB + PATH_CHECKOUT_TEMPLATE)
                RouteManager.route(activity, applink)
            }
            SettingConstant.SETTING_NOTIFICATION_ID -> {
                RouteManager.route(context, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING)
                accountAnalytics.eventClickSetting(NOTIFICATION)
                accountAnalytics.eventTroubleshooterClicked()
            }
            SettingConstant.SETTING_TNC_ID -> {
                accountAnalytics.eventClickSetting(TERM_CONDITION)
                RouteManager.route(activity, SettingConstant.Url.BASE_WEBVIEW_APPLINK + SettingConstant.Url.BASE_MOBILE + SettingConstant.Url.PATH_TERM_CONDITION)
            }
            SettingConstant.SETTING_ABOUT_US -> {
                accountAnalytics.eventClickSetting(ABOUT_US)
                RouteManager.getIntent(activity, SettingConstant.Url.BASE_WEBVIEW_APPLINK
                        + SettingConstant.Url.BASE_MOBILE
                        + SettingConstant.Url.PATH_ABOUT_US).run {
                    startActivity(this)
                }
            }
            SettingConstant.SETTING_PRIVACY_ID -> {
                accountAnalytics.eventClickSetting(PRIVACY_POLICY)
                RouteManager.route(activity, SettingConstant.Url.BASE_WEBVIEW_APPLINK + SettingConstant.Url.BASE_MOBILE + SettingConstant.Url.PATH_PRIVACY_POLICY)
            }
            SettingConstant.SETTING_APP_REVIEW_ID -> {
                accountAnalytics.eventClickSetting(APPLICATION_REVIEW)
                goToReviewApp()
            }
            SettingConstant.SETTING_HELP_CENTER_ID -> {
                accountAnalytics.eventClickSetting(HELP_CENTER)
                RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
            }
            SettingConstant.SETTING_OUT_ID -> {
                accountAnalytics.eventClickSetting(LOGOUT)
                showDialogLogout()
            }
            SettingConstant.SETTING_APP_ADVANCED_SETTING -> {
                accountAnalytics.eventClickSetting(ADVANCED_SETTING)
                RouteManager.route(context, ApplinkConstInternalGlobal.ADVANCED_SETTING)
            }
            SettingConstant.SETTING_DEV_OPTIONS -> if (GlobalConfig.isAllowDebuggingTools()) {
                accountAnalytics.eventClickSetting(DEVELOPER_OPTIONS)
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
            }
            SettingConstant.SETTING_FEEDBACK_FORM -> {
                RouteManager.route(context, ApplinkConst.FEEDBACK_FORM)
            }
            SettingConstant.SETTING_IMAGE_QUALITY -> {
                RouteManager.route(context, ApplinkConstInternalGlobal.MEDIA_QUALITY_SETTING)
            }
            else -> {
            }
        }
    }

    private fun goToReviewApp() {
        if (reviewHelper != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                reviewHelper?.checkForCustomerReview(context, childFragmentManager, ::goToPlaystore)
            }
        } else {
            goToPlaystore()
        }
    }

    private fun sendNotif() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(RED_DOT_GIMMICK_REMOTE_CONFIG_KEY, false)
        val redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif
        if (redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus) {
            notifPreference.isDisplayedGimmickNotif = true
            presenter.sendNotif()
        } else {
            doLogout()
        }
    }

    private fun goToPlaystore() {
        activity?.let {
            val uri = Uri.parse("market://details?id=" + it.application.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                it.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                it.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(SettingConstant.PLAYSTORE_URL + it.application.packageName)))
            }
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
            sendNotif()
        }
        dialog.setOnCancelClickListener { v -> dialog.dismiss() }
        dialog.show()
    }

    private fun doLogout() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.LOGOUT))
        }
    }

    private fun showForceUpdate() {
            val dataAppUpdate = remoteConfig.getString(RemoteConfigKey.CUSTOMER_APP_UPDATE)
            if (!TextUtils.isEmpty(dataAppUpdate)) {
                val gson = Gson()
                val dataUpdateApp = gson.fromJson(dataAppUpdate, DataUpdateApp::class.java)
                if(GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionForceUpdate) {
                    updateButton.visibility = View.VISIBLE
                    updateButton.setOnClickListener {
                        if (activity != null) {
                            val intent = RouteManager.getIntent(activity, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, dataUpdateApp.link))
                            this.startActivity(intent)
                        }
                    }
                } else {
                    updateButton.visibility = View.GONE
                }

            }
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
            SettingConstant.SETTING_SAFE_SEARCH_ID -> return isItemSelected(getString(R.string.pref_safe_mode), false)
            SettingConstant.SETTING_DARK_MODE -> return isItemSelected(TkpdCache.Key.KEY_DARK_MODE, false)
            else -> return false
        }
    }

    override fun onChangeChecked(settingId: Int, value: Boolean) {
        when (settingId) {
            SettingConstant.SETTING_SHAKE_ID -> {
                accountAnalytics.eventClickSetting(SHAKE_SHAKE)
                saveSettingValue(getString(R.string.pref_receive_shake), value)
            }
            SettingConstant.SETTING_SAFE_SEARCH_ID ->
                accountAnalytics.eventClickSetting(SAFE_MODE)
            SettingConstant.SETTING_DARK_MODE -> setupDarkMode(value)
            else -> {
            }
        }
    }

    private fun setupDarkMode(isDarkMode: Boolean) {
        setAppCompatMode(isDarkMode)
        saveSettingValue(TkpdCache.Key.KEY_DARK_MODE, isDarkMode)
        accountAnalytics.eventClickThemeSetting(isDarkMode)
    }

    private fun setAppCompatMode(isDarkMode: Boolean) {
        val screenMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(screenMode)
    }

    override fun onClicked(settingId: Int, currentValue: Boolean) {
        when (settingId) {
            SettingConstant.SETTING_GEOLOCATION_ID -> createAndShowLocationAlertDialog(currentValue)
            SettingConstant.SETTING_SAFE_SEARCH_ID ->
                    createAndShowSafeModeAlertDialog(currentValue)

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

    private fun goToApplicationDetailActivity() {
        activity?.let {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", it.packageName, null)
            intent.data = uri
            it.startActivity(intent)
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    private fun createAndShowLocationAlertDialog(currentValue: Boolean) {
        if (!currentValue) {
            accountAnalytics.eventClickToggleOnGeolocation(activity)
        } else {
            accountAnalytics.eventClickToggleOffGeolocation(activity)
        }

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(R.string.account_home_title_geolocation_alertdialog)
        builder.setPositiveButton(R.string.account_home_ok_geolocation_alertdialog) { dialog, id -> goToApplicationDetailActivity() }
        builder.setNegativeButton(R.string.account_home_batal_geolocation_alertdialog) { dialog, id -> }
        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }


    private fun createAndShowSafeModeAlertDialog(currentValue: Boolean) {
        var dialogTitleMsg = getString(R.string.account_home_safe_mode_selected_dialog_title)
        var dialogBodyMsg = getString(R.string.account_home_safe_mode_selected_dialog_msg)
        var dialogPositiveButton = getString(R.string.account_home_safe_mode_selected_dialog_positive_button)
        val dialogNegativeButton = getString(R.string.account_home_label_cancel)

        if (currentValue) {
            dialogTitleMsg = getString(R.string.account_home_safe_mode_unselected_dialog_title)
            dialogBodyMsg = getString(R.string.account_home_safe_mode_unselected_dialog_msg)
            dialogPositiveButton = getString(R.string.account_home_safe_mode_unselected_dialog_positive_button)
        }

        val accessDialog = AccessRequestDialogFragment.newInstance()
        accessDialog.setTitle(dialogTitleMsg)
        accessDialog.setBodyText(dialogBodyMsg)
        accessDialog.setPositiveButton(dialogPositiveButton)
        accessDialog.setNegativeButton(dialogNegativeButton)
        accessDialog.show(requireActivity().supportFragmentManager, AccessRequestDialogFragment.TAG)
    }

    override fun refreshSafeSearchOption() {
        if (adapter != null)
            adapter.updateSettingItem(SettingConstant.SETTING_SAFE_SEARCH_ID)
    }

    override fun refreshSettingOptionsList() {
        if (adapter != null) {
            setupTickerSellerMigration()
            adapter.setSettingItems(settingItems)
            adapter.notifyDataSetChanged()
        }
    }

    fun onClickAcceptButton() {
        settingsPresenter.onClickAcceptButton()
    }

    override fun onSuccessSendNotif() {
        doLogout()
    }

    override fun onErrorSendNotif(throwable: Throwable) {
        if (view != null) {
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.showError(requireView(), errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun setupTickerSellerMigration() {
        if(isSellerMigrationEnabled(context) && userSession.hasShop()) {
            initializeSellerMigrationAccountSettingTicker(tickerSellerMigrationAccountSetting)
        } else {
            tickerSellerMigrationAccountSetting.hide()
        }
    }

    companion object {

        private val RED_DOT_GIMMICK_REMOTE_CONFIG_KEY = "android_red_dot_gimmick_view"

        @JvmStatic
        fun createInstance(): Fragment {
            return GeneralSettingFragment()
        }

        private val TAG = GeneralSettingFragment::class.java.simpleName
    }
}
