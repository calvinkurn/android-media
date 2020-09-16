package com.tokopedia.sellerhome.settings.view.fragment

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.seller.menu.common.analytics.*
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.OtherSettingsUiModel
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_menu_setting.*
import kotlinx.android.synthetic.main.setting_logout.view.*
import kotlinx.android.synthetic.main.setting_tc.view.*
import javax.inject.Inject

class MenuSettingFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(), SettingTrackingListener {

    companion object {
        private const val REQUEST_CHANGE_PASSWORD = 123
        private const val REQUEST_ADD_PASSWORD = 1234

        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val URL_PLAY_STORE_HOST = "https://play.google.com/store/apps/details?id="
        private const val MARKET_DETAIL_HOST = "market://details?id="
        private const val PATH_TERM_CONDITION = "terms.pl"
        private const val PATH_PRIVACY_POLICY = "privacy.pl"

        private var MOBILE_DOMAIN = getInstance().MOBILEWEB

        private const val DEVELOPER_OPTION_INDEX = 19

        private const val LOGOUT_BUTTON_NAME = "Logout"
        private const val TERM_CONDITION_BUTTON_NAME = "Syarat dan Ketentuan"
        private const val PRIVACY_POLICY_BUTTON_NAME = "Kebijakan Privasi"

        private const val SHIPPING_SERVICE_ALIAS = "shipping service"
        private const val PASSWORD_ALIAS = "password"
        private const val LOGOUT_ALIAS = "logout"

        @JvmStatic
        fun createInstance(): MenuSettingFragment = MenuSettingFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @DrawableRes
    private val logoutIconDrawable = R.drawable.sah_qc_launcher2

    private val trackingAliasHashMap by lazy {
        mapOf<String, String?>(
                resources.getString(R.string.setting_menu_set_shipment_method) to SHIPPING_SERVICE_ALIAS,
                resources.getString(R.string.setting_menu_password) to PASSWORD_ALIAS,
                LOGOUT_BUTTON_NAME to LOGOUT_ALIAS)
    }
    private val logoutUiModel by lazy {
        OtherSettingsUiModel(LOGOUT_BUTTON_NAME, trackingAliasHashMap[LOGOUT_BUTTON_NAME])
    }

    private val termsAndConditionUiModel by lazy {
        OtherSettingsUiModel(TERM_CONDITION_BUTTON_NAME)
    }
    private val privacyPolicyUiModel by lazy {
        OtherSettingsUiModel(PRIVACY_POLICY_BUTTON_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory(this)

    override fun onItemClicked(t: SettingUiModel?) {

    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {

    }

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
    }

    private fun setupView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        val settingList = mutableListOf(
                SettingTitleMenuUiModel(resources.getString(R.string.setting_menu_shop_setting), R.drawable.ic_pengaturan_toko),
                IndentedSettingTitleUiModel(resources.getString(R.string.setting_menu_shop_profile)),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_basic_info),
                        clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO,
                        settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_shop_notes),
                        clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES,
                        settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_shop_working_hours),
                        clickApplink = ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE,
                        settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
                DividerUiModel(DividerType.THIN_INDENTED),
                IndentedSettingTitleUiModel(resources.getString(R.string.setting_menu_location_and_shipment)),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_add_and_shop_location),
                        clickApplink =  ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS,
                        settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_set_shipment_method),
                        clickApplink = ApplinkConst.SELLER_SHIPPING_EDITOR,
                        settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                        trackingAlias = trackingAliasHashMap[resources.getString(R.string.setting_menu_set_shipment_method)]),
                DividerUiModel(DividerType.THICK),
                SettingTitleMenuUiModel(resources.getString(R.string.setting_menu_account_setting), R.drawable.ic_account),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_self_profile),
                        clickApplink = ApplinkConst.SETTING_PROFILE,
                        settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_bank_account),
                        clickApplink = ApplinkConstInternalGlobal.SETTING_BANK,
                        settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_password),
                        settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING,
                        trackingAlias = trackingAliasHashMap[resources.getString(R.string.setting_menu_password)]) { addOrChangePassword() },
                DividerUiModel(DividerType.THICK),
                SettingTitleMenuUiModel(resources.getString(R.string.setting_menu_app_setting), R.drawable.ic_app_setting),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_chat_and_notification),
                        clickApplink = ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING,
                        settingTypeInfix = SettingTrackingConstant.APP_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_notification_troubleshooter),
                        clickApplink = ApplinkConstInternalGlobal.PUSH_NOTIFICATION_TROUBLESHOOTER,
                        settingTypeInfix = SettingTrackingConstant.APP_SETTING),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_share_app),
                        settingTypeInfix = SettingTrackingConstant.APP_SETTING) { shareApplication() },
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_review_app),
                        settingTypeInfix = SettingTrackingConstant.APP_SETTING) { reviewApplication() },
                DividerUiModel(DividerType.THIN_INDENTED)
        )
        if (GlobalConfig.isAllowDebuggingTools())
            settingList.add(DEVELOPER_OPTION_INDEX, MenuItemUiModel(
                    resources.getString(R.string.setting_menu_developer_options),
                    settingTypeInfix = SettingTrackingConstant.APP_SETTING) {
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
            })
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)

        setupLogoutView()
        setupExtraSettingView()
    }

    private fun setupLogoutView() {
        logoutLayout?.run {
            sendSettingShopInfoImpressionTracking(logoutUiModel) {
                it.sendShopInfoImpressionData()
            }
            appVersionText.text = getString(R.string.setting_application_version, GlobalConfig.VERSION_NAME)
            setOnClickListener {
                logoutUiModel.sendSettingShopInfoClickTracking()
                showLogoutDialog()
            }
        }
    }

    private fun setupExtraSettingView() {
        tcLayout?.run {
            settingTC?.run {
                setOnClickListener {
                    termsAndConditionUiModel.sendSettingShopInfoClickTracking()
                    showTermsAndConditions()
                }
            }
            settingPrivacy?.run {
                setOnClickListener {
                    privacyPolicyUiModel.sendSettingShopInfoClickTracking()
                    showPrivacyPolicy()
                }
            }
        }
    }

    private fun addOrChangePassword() {
        var intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.ADD_PASSWORD)
        var requestCode = REQUEST_ADD_PASSWORD
        if (userSession.hasPassword()) {
            intent = RouteManager.getIntent(activity, ApplinkConst.CHANGE_PASSWORD)
            requestCode = REQUEST_CHANGE_PASSWORD
        }
        startActivityForResult(intent, requestCode)
    }

    private fun shareApplication() {
        val urlPlayStore = URL_PLAY_STORE_HOST + activity?.application?.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.msg_share_apps).toString() + "\n" + urlPlayStore)
        sendIntent.type = "text/plain"
        activity?.startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.title_share)))
    }

    private fun reviewApplication() {
        val uri = Uri.parse(MARKET_DETAIL_HOST + activity?.application?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAY_STORE_HOST + activity?.application?.packageName)))
        }
    }

    private fun showLogoutDialog() {
        var dialogBuilder: AlertDialog.Builder? = null
        context?.let { dialogBuilder = AlertDialog.Builder(it) }
        dialogBuilder?.apply {
            setIcon(logoutIconDrawable)
            setTitle(context.getString(R.string.seller_home_logout_title))
            setMessage(context.getString(R.string.seller_home_logout_confirm))
            setPositiveButton(context.getString(R.string.seller_home_logout_button)) { dialogInterface, _ ->
                val progressDialog = showProgressDialog()
                dialogInterface.dismiss()
                RouteManager.route(context, ApplinkConstInternalGlobal.LOGOUT)
                progressDialog.dismiss()
                activity?.finish()
            }
            setNegativeButton(context.getString(R.string.seller_home_cancel)) {
                dialogInterface, _ -> dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun showProgressDialog(): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        return progressDialog.apply {
            setMessage(resources.getString(R.string.seller_home_loading))
            setTitle("")
            setCancelable(false)
            show()
        }
    }

    private fun showTermsAndConditions() {
        val termUrl = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, MOBILE_DOMAIN, PATH_TERM_CONDITION)
        val intent = RouteManager.getIntent(context, termUrl)
        context?.startActivity(intent)
    }

    private fun showPrivacyPolicy() {
        val privacyUrl = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, MOBILE_DOMAIN, PATH_PRIVACY_POLICY)
        val intent = RouteManager.getIntent(context, privacyUrl)
        context?.startActivity(intent)
    }

}