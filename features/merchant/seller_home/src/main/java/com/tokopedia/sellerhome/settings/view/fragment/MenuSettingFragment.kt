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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.internal_review.common.InternalReviewUtils
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.data.SellerHomeSharedPref
import com.tokopedia.sellerhome.databinding.FragmentMenuSettingBinding
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.adapter.MenuSettingAdapter
import com.tokopedia.sellerhome.settings.view.bottomsheet.SocialMediaLinksBottomSheet
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.OtherSettingsUiModel
import com.tokopedia.sellerhome.settings.view.viewmodel.MenuSettingViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
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
        private const val PATH_TERM_CONDITION = "terms?lang=id"
        private const val PATH_PRIVACY_POLICY = "privacy?lang=id"

        private val WEB_DOMAIN = getInstance().WEB

        private const val LOGOUT_BUTTON_NAME = "Logout"
        private const val TERM_CONDITION_BUTTON_NAME = "Syarat dan Ketentuan"
        private const val PRIVACY_POLICY_BUTTON_NAME = "Kebijakan Privasi"
        private const val EXTRA_SCREEN_SHOOT_TRIGGER = "extra_screen_shoot_trigger"
        private const val EXTRA_TOASTER_MESSAGE = "extra_toaster_message"
        private const val EXTRA_SHOW_SETTING_BOTTOM_SHEET = "extra_show_settings"
        private const val PERSONA_MENU_COACH_MARK = "persona_menu_coach_mark"

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

    @Inject
    lateinit var sharedPref: SellerHomeSharedPref

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
    private var personaCoachMark: CoachMark2? = null

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
            context?.getString(R.string.sah_admin_restriction_message)
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
                    menuSettingViewModel.getShopLocEligible(userSession.shopId.toLongOrZero())
                }
                is Fail -> {
                    menuSettingAdapter?.removeLoading()
                    showToasterError(result.throwable.message.orEmpty())
                }
            }
        }
    }

    private fun setupLocationSettings(isEligibleMultiloc: Result<Boolean>) {
        if (isEligibleMultiloc is Success) {
            menuSettingAdapter?.showShopSetting(isEligibleMultiloc.data)
        }
    }

    private fun setupView() {
        val menuLayoutManager by getMenuLayoutManager()
        binding?.recyclerView?.layoutManager = menuLayoutManager
        menuSettingAdapter?.populateInitialMenus(userSession.isShopOwner)
        if (userSession.isShopOwner) {
            menuSettingViewModel.getShopLocEligible(userSession.shopId.toLong())
        } else {
            menuSettingViewModel.checkShopSettingAccess()
        }
        observe(menuSettingViewModel.shopLocEligible, ::setupLocationSettings)

        setupLogoutView()
        setupExtraSettingView()
        startShopActiveService()
        showPersonaCoachMark(menuLayoutManager)
    }

    private fun getMenuLayoutManager(): Lazy<LinearLayoutManager> {
        return lazy {
            object : LinearLayoutManager(context) {
                override fun scrollVerticallyBy(
                    dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?
                ): Int {
                    return try {
                        showPersonaCoachMark(this)
                        super.scrollVerticallyBy(dy, recycler, state)
                    } catch (@Suppress("SwallowedException") e: IndexOutOfBoundsException) {
                        Int.ZERO
                    }
                }
            }
        }
    }

    private fun showPersonaCoachMark(lm: LinearLayoutManager) {
        binding?.recyclerView?.post {
            context?.let { ctx ->
                val menuTitle = ctx.getString(R.string.setting_seller_persona)
                val personaMenuIndex = adapter.data.indexOfFirst {
                    (it as? MenuItemUiModel)?.title == menuTitle
                }

                val eligibleCoachMark = sharedPref.getBoolean(PERSONA_MENU_COACH_MARK, true)
                if (personaMenuIndex != RecyclerView.NO_POSITION && eligibleCoachMark) {
                    val firstVisibleIndex = lm.findFirstCompletelyVisibleItemPosition()
                    val lastVisibleIndex = lm.findLastCompletelyVisibleItemPosition()

                    if (personaCoachMark == null) {
                        personaCoachMark = CoachMark2(ctx).apply {
                            onDismissListener = {
                                sharedPref.putBoolean(PERSONA_MENU_COACH_MARK, false)
                            }
                        }
                    }

                    if (personaMenuIndex in firstVisibleIndex..lastVisibleIndex) {
                        lm.getChildAt(personaMenuIndex)?.let { anchor ->
                            personaCoachMark?.showCoachMark(getCoachMarkItem(anchor))
                        }
                    } else {
                        personaCoachMark?.dismissCoachMark()
                    }
                }
            }
        }
    }

    private fun getCoachMarkItem(anchor: View): ArrayList<CoachMark2Item> {
        return arrayListOf(
            CoachMark2Item(
                anchorView = anchor,
                title = String.EMPTY,
                description = getString(R.string.menu_setting_persona_coach_mack),
                position = CoachMark2.POSITION_BOTTOM
            )
        )
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
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.HAS_PASSWORD)
        startActivity(intent)
    }

    private fun shareApplication() {
        val urlPlayStore = URL_PLAY_STORE_HOST + activity?.application?.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            context?.resources?.getString(R.string.msg_share_apps) + "\n" + urlPlayStore
        )
        sendIntent.type = "text/plain"
        activity?.startActivity(
            Intent.createChooser(
                sendIntent,
                context?.resources?.getText(R.string.title_share)
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
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.run {
                setTitle(it.getString(R.string.seller_home_logout_title))
                setDescription(it.getString(R.string.seller_home_logout_confirm))
                setPrimaryCTAText(it.getString(R.string.seller_home_logout_button))
                setPrimaryCTAClickListener {
                    doLogout(dialog)
                }
                setSecondaryCTAText(it.getString(R.string.seller_home_cancel))
                setSecondaryCTAClickListener {
                    dialog.dismiss()
                }
                show()
            }
        }
    }

    private fun doLogout(dialog: DialogUnify) {
        val progressDialog = showProgressDialog()
        dialog.dismiss()
        RouteManager.route(context, ApplinkConstInternalUserPlatform.LOGOUT)
        progressDialog.dismiss()
        activity?.finish()
    }

    private fun showProgressDialog(): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        return progressDialog.apply {
            setMessage(context?.resources?.getString(R.string.seller_home_loading).orEmpty())
            setTitle("")
            setCancelable(false)
            show()
        }
    }

    private fun showTermsAndConditions() {
        val termUrl =
            String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, WEB_DOMAIN, PATH_TERM_CONDITION)
        val intent = RouteManager.getIntent(context, termUrl)
        context?.startActivity(intent)
    }

    private fun showPrivacyPolicy() {
        val privacyUrl =
            String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, WEB_DOMAIN, PATH_PRIVACY_POLICY)
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
            UpdateShopActiveWorker.execute(it)
        }
    }

}
