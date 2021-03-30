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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.adapter.MenuSettingAdapter
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.OtherSettingsUiModel
import com.tokopedia.sellerhome.settings.view.viewmodel.MenuSettingViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.internal_review.common.InternalReviewUtils
import kotlinx.android.synthetic.main.fragment_menu_setting.*
import kotlinx.android.synthetic.main.setting_logout.view.*
import kotlinx.android.synthetic.main.setting_tc.view.*
import javax.inject.Inject

class MenuSettingFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(), SettingTrackingListener, MenuSettingAdapter.Listener {

    companion object {
        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val URL_PLAY_STORE_HOST = "https://play.google.com/store/apps/details?id="
        private const val MARKET_DETAIL_HOST = "market://details?id="
        private const val PATH_TERM_CONDITION = "terms.pl"
        private const val PATH_PRIVACY_POLICY = "privacy.pl"

        private var MOBILE_DOMAIN = getInstance().MOBILEWEB

        private const val LOGOUT_BUTTON_NAME = "Logout"
        private const val TERM_CONDITION_BUTTON_NAME = "Syarat dan Ketentuan"
        private const val PRIVACY_POLICY_BUTTON_NAME = "Kebijakan Privasi"

        private const val LOGOUT_ALIAS = "logout"

        @JvmStatic
        fun createInstance(): MenuSettingFragment = MenuSettingFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @DrawableRes
    private val logoutIconDrawable = R.drawable.sah_qc_launcher2

    private val logoutUiModel by lazy {
        OtherSettingsUiModel(LOGOUT_BUTTON_NAME, LOGOUT_ALIAS)
    }

    private val termsAndConditionUiModel by lazy {
        OtherSettingsUiModel(TERM_CONDITION_BUTTON_NAME)
    }
    private val privacyPolicyUiModel by lazy {
        OtherSettingsUiModel(PRIVACY_POLICY_BUTTON_NAME)
    }

    private val menuSettingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MenuSettingViewModel::class.java)
    }

    private val menuSettingAdapter by lazy {
        adapter as? MenuSettingAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeShopSettingAccess()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory(this)

    override fun createAdapterInstance(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> {
        return MenuSettingAdapter(context, this, adapterTypeFactory)
    }

    override fun onItemClicked(t: SettingUiModel?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
    }

    override fun onAddOrChangePassword() {
        addOrChangePassword()
    }

    override fun onShareApplication() {
        shareApplication()
    }

    override fun onReviewApplication() {
        reviewApplication()
    }

    override fun onNoAccess() {
        showToasterError(context?.getString(R.string.seller_menu_admin_no_permission_oops).orEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuSettingViewModel.shopSettingAccessLiveData.removeObservers(viewLifecycleOwner)
    }

    private fun observeShopSettingAccess() {
        menuSettingViewModel.shopSettingAccessLiveData.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    menuSettingAdapter?.showSuccessAccessMenus(result.data)
                }
                is Fail -> {
                    menuSettingAdapter?.removeLoading()
                    showToasterError(result.throwable.message.orEmpty())
                }
            }
        }
    }

    private fun setupView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        menuSettingAdapter?.populateInitialMenus(userSession.isShopOwner)
        if (!userSession.isShopOwner) {
            menuSettingViewModel.checkShopSettingAccess()
        }

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
        var intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.HAS_PASSWORD)
        startActivity(intent)
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
        InternalReviewUtils.saveFlagHasOpenedReviewApp(activity?.applicationContext, userSession.userId)
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

    private fun showToasterError(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, type = Toaster.TYPE_ERROR).show()
        }
    }

}