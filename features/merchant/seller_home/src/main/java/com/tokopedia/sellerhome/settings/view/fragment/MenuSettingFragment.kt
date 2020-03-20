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
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingListener
import com.tokopedia.sellerhome.settings.analytics.sendShopInfoImpressionData
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.DividerType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
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

        private const val URL_PLAY_STORE_HOST = "https://play.google.com/store/apps/details?id="
        private const val MARKET_DETAIL_HOST = "market://details?id="
        private const val PATH_TERM_CONDITION = "terms.pl"
        private const val PATH_PRIVACY_POLICY = "privacy.pl"

        private var MOBILE_DOMAIN = getInstance().MOBILEWEB

        private const val DEVELOPER_OPTION_INDEX = 23

        @JvmStatic
        fun createInstance(): MenuSettingFragment = MenuSettingFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @DrawableRes
    private val logoutIconDrawable = R.drawable.sah_qc_launcher2

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
        context?.run { settingShopInfoImpressionTrackable.sendShopInfoImpressionData(this, userSession) }
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
                        settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
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
                        settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING) { addOrChangePassword() },
                DividerUiModel(DividerType.THICK),
                SettingTitleMenuUiModel(resources.getString(R.string.setting_menu_app_setting), R.drawable.ic_app_setting),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_chat_and_notification),
                        clickApplink = ApplinkConstInternalGlobal.MANAGE_NOTIFICATION,
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
            settingList.add(DEVELOPER_OPTION_INDEX, MenuItemUiModel(resources.getString(R.string.setting_menu_developer_options)) {
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
            })
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)

        logoutLayout.run {
            appVersionText.text = getString(R.string.setting_application_version, GlobalConfig.VERSION_NAME)
            setOnClickListener { showLogoutDialog() }
        }

        tcLayout.run {
            settingTC.setOnClickListener { showTermsAndConditions() }
            settingPrivacy.setOnClickListener { showPrivacyPolicy() }
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
                showProgressDialog()
                dialogInterface.dismiss()
                RouteManager.route(context, ApplinkConstInternalGlobal.LOGOUT)
            }
            setNegativeButton(context.getString(R.string.seller_home_cancel)) {
                dialogInterface, _ -> dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(context)
        progressDialog.apply {
            setMessage(resources.getString(R.string.seller_home_loading))
            setTitle("")
            setCancelable(false)
            show()
        }
    }

    private fun showTermsAndConditions() {
        val intent = RouteManager.getIntent(context, ApplinkConst.WEBVIEW)
        val termUrl = String.format("%s%s", MOBILE_DOMAIN, PATH_TERM_CONDITION)
        intent.putExtra(OtherMenuFragment.URL_KEY, termUrl)
        context?.startActivity(intent)
    }

    private fun showPrivacyPolicy() {
        val intent = RouteManager.getIntent(context, ApplinkConst.WEBVIEW)
        val termUrl = String.format("%s%s", MOBILE_DOMAIN, PATH_PRIVACY_POLICY)
        intent.putExtra(OtherMenuFragment.URL_KEY, termUrl)
        context?.startActivity(intent)
    }

}