package com.tokopedia.sellerhome.settings.view.fragment

import android.app.Activity
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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.internal_review.common.InternalReviewUtils
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.FragmentMenuSettingBinding
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.adapter.MenuSettingAdapter
import com.tokopedia.sellerhome.settings.view.bottomsheet.SocialMediaLinksBottomSheet
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.OtherSettingsUiModel
import com.tokopedia.sellerhome.settings.view.viewmodel.MenuSettingViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MenuSettingFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(),
    SettingTrackingListener, MenuSettingAdapter.Listener {

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
        private const val EXTRA_SCREEN_SHOOT_TRIGGER = "extra_screen_shoot_trigger"
        private const val EXTRA_TOASTER_MESSAGE = "extra_toaster_message"
        private const val EXTRA_SHOW_SETTING_BOTTOM_SHEET = "extra_show_settings"

        private const val LOGOUT_ALIAS = "logout"
        private const val REQ_CODE_GLOBAL_FEEDBACK = 8043
        private const val TOASTER_HEIGHT = 104
        private const val TOASTER_CTA_WIDTH = 120
        private const val TOASTER_DURATION = 5000
        private const val TOASTER_DELAY = 1000L

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

    private var binding by autoClearedNullable<FragmentMenuSettingBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeShopSettingAccess()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory =
        OtherMenuAdapterTypeFactory(this)

    override fun createAdapterInstance(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> {
        var isShowScreenRecorder = false
        context?.let {
            isShowScreenRecorder = FirebaseRemoteConfigImpl(it).getBoolean(
                RemoteConfigKey.SETTING_SHOW_SCREEN_RECORDER,
                false
            )
        }
        return MenuSettingAdapter(context, this, isShowScreenRecorder, adapterTypeFactory)
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

    override fun onGiveFeedback() {
        openGlobalFeedback()
    }

    override fun onOpenSocialMediaLinks() {

        openSocialMediaLinksBottomSheet()
    }

    override fun onNoAccess() {
        showToasterError(
            context?.getString(com.tokopedia.seller.menu.common.R.string.seller_menu_admin_no_permission_oops)
                .orEmpty()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuSettingViewModel.shopSettingAccessLiveData.removeObservers(viewLifecycleOwner)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleGlobalFeedbackResult(requestCode, resultCode, data)
    }

    private fun handleGlobalFeedbackResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_GLOBAL_FEEDBACK) {
            showFeedbackToaster(data)
        }
    }

    private fun showFeedbackToaster(data: Intent?) {
        val rootView = view?.rootView
        rootView?.postDelayed({
            val isScreenShootTriggerEnabled =
                data?.getBooleanExtra(EXTRA_SCREEN_SHOOT_TRIGGER, false).orFalse()
            val toasterMessage = data?.getStringExtra(EXTRA_TOASTER_MESSAGE).orEmpty()
            Toaster.toasterCustomBottomHeight = rootView.context.dpToPx(TOASTER_HEIGHT).toInt()
            if (isScreenShootTriggerEnabled) {
                Toaster.build(
                    rootView,
                    text = toasterMessage,
                    duration = Toaster.LENGTH_LONG
                ).show()
            } else {
                Toaster.toasterCustomCtaWidth = rootView.context.dpToPx(TOASTER_CTA_WIDTH).toInt()
                Toaster.build(rootView,
                    text = toasterMessage,
                    actionText = rootView.context.getString(R.string.menu_setting_title),
                    duration = TOASTER_DURATION,
                    clickListener = {
                        openFeedbackForm()
                    }
                ).show()
            }
        }, TOASTER_DELAY)
    }

    private fun openFeedbackForm() {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalSellerapp.SELLER_FEEDBACK)
            intent.putExtra(EXTRA_SHOW_SETTING_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQ_CODE_GLOBAL_FEEDBACK)
        }
    }

    private fun observeShopSettingAccess() {
        menuSettingViewModel.shopSettingAccessLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
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
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        menuSettingAdapter?.populateInitialMenus(userSession.isShopOwner)
        if (!userSession.isShopOwner) {
            menuSettingViewModel.checkShopSettingAccess()
        }

        setupLogoutView()
        setupExtraSettingView()
        startShopActiveService()
    }

    private fun setupLogoutView() {
        binding?.layoutLogout?.run {
            logoutLayout.sendSettingShopInfoImpressionTracking(logoutUiModel) {
                it.sendShopInfoImpressionData()
            }
            appVersionText.text =
                getString(R.string.setting_application_version, GlobalConfig.VERSION_NAME)
            root.setOnClickListener {
                logoutUiModel.sendSettingShopInfoClickTracking()
                showLogoutDialog()
            }
        }
    }

    private fun setupExtraSettingView() {
        binding?.tcLayout?.run {
            settingTC.run {
                setOnClickListener {
                    termsAndConditionUiModel.sendSettingShopInfoClickTracking()
                    showTermsAndConditions()
                }
            }
            settingPrivacy.run {
                setOnClickListener {
                    privacyPolicyUiModel.sendSettingShopInfoClickTracking()
                    showPrivacyPolicy()
                }
            }
        }
    }

    private fun addOrChangePassword() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.HAS_PASSWORD)
        startActivity(intent)
    }

    private fun shareApplication() {
        val urlPlayStore = URL_PLAY_STORE_HOST + activity?.application?.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            resources.getString(R.string.msg_share_apps) + "\n" + urlPlayStore
        )
        sendIntent.type = "text/plain"
        activity?.startActivity(
            Intent.createChooser(
                sendIntent,
                resources.getText(R.string.title_share)
            )
        )
    }

    private fun reviewApplication() {
        InternalReviewUtils.saveFlagHasOpenedReviewApp(
            activity?.applicationContext,
            userSession.userId
        )
        val uri = Uri.parse(MARKET_DETAIL_HOST + activity?.application?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(URL_PLAY_STORE_HOST + activity?.application?.packageName)
                )
            )
        }
    }

    private fun openGlobalFeedback() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_FEEDBACK)
        startActivityForResult(intent, REQ_CODE_GLOBAL_FEEDBACK)
    }

    private fun openSocialMediaLinksBottomSheet() {
        if (isActivityResumed()) {
            SocialMediaLinksBottomSheet.createInstance().show(childFragmentManager)
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
            setNegativeButton(context.getString(R.string.seller_home_cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
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
        val termUrl =
            String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, MOBILE_DOMAIN, PATH_TERM_CONDITION)
        val intent = RouteManager.getIntent(context, termUrl)
        context?.startActivity(intent)
    }

    private fun showPrivacyPolicy() {
        val privacyUrl =
            String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, MOBILE_DOMAIN, PATH_PRIVACY_POLICY)
        val intent = RouteManager.getIntent(context, privacyUrl)
        context?.startActivity(intent)
    }

    private fun showToasterError(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, type = Toaster.TYPE_ERROR).show()
        }
    }

    private fun isActivityResumed(): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }

    private fun startShopActiveService() {
        context?.let {
            UpdateShopActiveService.startService(it)
        }
    }

}