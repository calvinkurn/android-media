package com.tokopedia.home_account.view

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.Analytics.ABOUT_US
import com.tokopedia.home_account.AccountConstants.Analytics.ACCOUNT_BANK
import com.tokopedia.home_account.AccountConstants.Analytics.ADDRESS_LIST
import com.tokopedia.home_account.AccountConstants.Analytics.APPLICATION_REVIEW
import com.tokopedia.home_account.AccountConstants.Analytics.LOGOUT
import com.tokopedia.home_account.AccountConstants.Analytics.PAYMENT_METHOD
import com.tokopedia.home_account.AccountConstants.Analytics.PERSONAL_DATA
import com.tokopedia.home_account.AccountConstants.Analytics.PRIVACY_POLICY
import com.tokopedia.home_account.AccountConstants.Analytics.TERM_CONDITION
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.home_account_coordinator_layout.*
import kotlinx.android.synthetic.main.home_account_item_profile.*
import kotlinx.android.synthetic.main.home_account_user_fragment.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserFragment : BaseDaggerFragment(), HomeAccountUserListener {

    var adapter: HomeAccountUserAdapter? = null

    var appBarCollapseListener: onAppBarCollapseListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }

    @Inject
    lateinit var mapper: DataViewMapper

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var accountPref: AccountPreference

    @Inject
    lateinit var homeAccountAnalytic: HomeAccountAnalytics

    override fun getScreenName(): String = "homeAccountUserFragment"

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeAccountUserActivity) appBarCollapseListener = context
    }

    private fun isItemSelected(key: String, defaultValue: Boolean): Boolean {
        val settings = PreferenceManager.getDefaultSharedPreferences(activity)
        return settings.getBoolean(key, defaultValue)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_account_coordinator_layout, container, false)
    }

    private fun setupObserver() {
        viewModel.settingData.observe(viewLifecycleOwner, Observer {
            addItem(it)
        })
        viewModel.settingApplication.observe(viewLifecycleOwner, Observer {
            addItem(it)
        })
        viewModel.aboutTokopedia.observe(viewLifecycleOwner, Observer {
            addItem(it)
            addItem(
                    SettingDataView("", mutableListOf(
                            CommonDataView(id = AccountConstants.SettingCode.SETTING_OUT_ID, title = "Keluar Akun", body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_sign_out, endText = "Versi ${GlobalConfig.VERSION_NAME}")
                    ), isExpanded = true)
            )
        })

        viewModel.buyerAccountDataData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetBuyerAccount(it.data)
            }
        })
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = status_bar_bg.background
        drawable.alpha = alpha.toInt()
        status_bar_bg.background = drawable
    }

    private fun onSuccessGetBuyerAccount(buyerAccount: UserAccountDataModel){
        hideLoading()
        addItem(mapper.mapToProfileDataView(buyerAccount))
        viewModel.getInitialData()

//        setLayoutParams(200)

//        createCoachmark()
    }

    fun showLoading() {
//        home_account_shimmer_layout?.show()
//        home_account_main_container?.invisible()
        container_main?.displayedChild = CONTAINER_LOADER
    }

    fun hideLoading() {
//        home_account_shimmer_layout?.hide()
//        home_account_main_container?.show()
        container_main?.displayedChild = CONTAINER_DATA
    }

//    private fun hideStatusBar() {
//        home_account_coordinator!!.fitsSystemWindows = false
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            home_account_coordinator!!.requestApplyInsets()
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var flags = home_account_coordinator!!.systemUiVisibility
//            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            home_account_coordinator!!.systemUiVisibility = flags
//            activity!!.window.statusBarColor = Color.WHITE
//        }
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            activity!!.window.statusBarColor = Color.TRANSPARENT
//        }
//    }

//    fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
//        val win = activity!!.window
//        val winParams = win.attributes
//        if (on) {
//            winParams.flags = winParams.flags or bits
//        } else {
//            winParams.flags = winParams.flags and bits.inv()
//        }
//        win.attributes = winParams
//    }

//    private fun setStatusBarViewHeight() {
//        if (activity != null) status_bar_bg?.layoutParams?.height = getStatusBarHeight(activity)
//    }

    private fun setupStatusBar() {
        activity?.let {
            status_bar_bg.background = ColorDrawable(
                    ContextCompat.getColor(it, R.color.green_600)
            )
        }
        //status bar background compability, we show view background for android >= Kitkat
//because in that version, status bar can't forced to dark mode, we must set background
//to keep status bar icon visible
        status_bar_bg.layoutParams.height = ViewHelper.getStatusBarHeight(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            status_bar_bg.visibility = View.INVISIBLE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            status_bar_bg.visibility = View.VISIBLE
        } else {
            status_bar_bg.visibility = View.GONE
        }
        //initial condition for status and searchbar
        setStatusBarAlpha(0f)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        hideStatusBar()

//        setStatusBarViewHeight()

//        (activity as BaseSimpleActivity?)?.run {
//            setSupportActionBar(toolbar_tokopoint)
//            toolbar_tokopoint?.title = "Akun Saya"
//        }

//        collapsing_toolbar?.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
//        collapsing_toolbar?.setTitle(" ")
//        home_account_user_fragment_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val offset = recyclerView.computeVerticalScrollOffset()
//                handleAppBarOffsetChange(offset)
//                handleAppBarIconChange(offset)
//            }
//        })

        home_account_user_toolbar?.let {
            it.setIcon(IconBuilder()
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            )

        }
        activity?.run {
            home_account_user_toolbar?.setupToolbarWithStatusBar(this)
        }

        setupStatusBar()
        setupObserver()
        adapter = HomeAccountUserAdapter(this)
        setupList()

        getData()

        home_account_user_fragment_rv?.addOnScrollListener(NavRecyclerViewScrollListener(
                navToolbar = home_account_user_toolbar,
                startTransitionPixel = 200,
                toolbarTransitionRangePixel = 50,
                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                    override fun onAlphaChanged(offsetAlpha: Float) {
                        setStatusBarAlpha(offsetAlpha)
                    }

                    override fun onSwitchToDarkToolbar() {

                    }

                    override fun onSwitchToLightToolbar() {

                    }
                }
        ))

//        home_account_user_fragment_swipe_refresh?.setOnRefreshListener {
//            home_account_user_fragment_swipe_refresh?.isRefreshing = false
//            getData()
//        }
    }

    private fun getData(){
        showLoading()
        viewModel.getBuyerData()
    }

//    private fun setLayoutParams(cardheight: Int) {
//        val statusBarHeight = getStatusBarHeight(activity)
//        val layoutParams = toolbar_tokopoint!!.layoutParams as FrameLayout.LayoutParams
//        layoutParams.topMargin = statusBarHeight
//        toolbar_tokopoint!!.layoutParams = layoutParams
//        val imageEggLp = view?.findViewById<AppCompatImageView>(R.id.account_user_item_profile_avatar)?.layoutParams as? RelativeLayout.LayoutParams
//        imageEggLp?.topMargin = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_top_margin_big_image)).toInt()
//        view?.findViewById<AppCompatImageView>(R.id.account_user_item_profile_avatar)?.layoutParams = imageEggLp
//        val imageBigLp = view?.findViewById<ImageView>(R.id.account_user_item_profile_backdrop)?.layoutParams as? RelativeLayout.LayoutParams
//        imageBigLp?.height = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_home_top_bg_height) + cardheight).toInt()
//        view?.findViewById<ImageView>(R.id.account_user_item_profile_backdrop)?.layoutParams = imageBigLp
//    }


    fun getStatusBarHeight(context: Context?): Int {
        var height = 0
        context?.run {
            val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resId > 0) {
                height = context.resources.getDimensionPixelSize(resId)
            }
        }
        return height
    }

//    private fun handleAppBarOffsetChange(offset: Int) {
//        val toolbarTransitionRange = (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height)
//                - toolbar_tokopoint!!.height - getStatusBarHeight(activity))
//        var offsetAlpha = 255f / toolbarTransitionRange * (toolbarTransitionRange - offset)
//        if (offsetAlpha < 0) {
//            offsetAlpha = 0f
//        }
//        if (offsetAlpha >= 255) {
//            offsetAlpha = 255f
//        }
//        var alpha = offsetAlpha / 255 - 1
//        if (alpha < 0) alpha = alpha * -1
//        status_bar_bg?.alpha = alpha
//        if (alpha > 0.5) toolbar_tokopoint?.switchToDarkMode() else toolbar_tokopoint?.switchToTransparentMode()
//        toolbar_tokopoint?.applyAlphaToToolbarBackground(alpha)
//    }

    private fun showDialogClearCache() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.account_home_clear_cache_warning))
            dialog.setDescription(getString(R.string.account_home_clear_cache_message))
            dialog.setPrimaryCTAText(getString(R.string.account_home_label_clear_cache_ok))
            dialog.setPrimaryCTAClickListener {
                clearCache()
            }
            dialog.setSecondaryCTAText(getString(R.string.account_home_label_clear_cache_cancel))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun clearCache() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE)
            if (activityManager is ActivityManager) {
                activityManager.clearApplicationUserData()
            }
        } else {
            try {
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear ${context?.packageName}")
            } catch (e: Exception) {}
        }
    }

//    private fun handleAppBarIconChange(verticalOffset: Int) {
//        val verticalOffset1 = Math.abs(verticalOffset)
//        if (verticalOffset1 >= (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height))) {
//            toolbar_tokopoint?.showToolbarIcon()
//        } else
//            toolbar_tokopoint?.hideToolbarIcon()
//    }

    private fun setupList() {
        home_account_user_fragment_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        home_account_user_fragment_rv?.adapter = adapter
        home_account_user_fragment_rv?.isNestedScrollingEnabled = false
    }

    private fun addItem(item: Any) {
        adapter?.addItem(item)
        adapter?.notifyDataSetChanged()
    }

    override fun onEditProfileClicked() {
        goToApplink(ApplinkConstInternalGlobal.SETTING_PROFILE)
    }

    private fun goToApplink(applink: String) {
        if (applink.isNotEmpty()) {
            val intent = RouteManager.getIntent(context, applink)
            startActivity(intent)
        }
    }

    override fun onMemberItemClicked(applink: String) {
        goToApplink(applink)
    }

    override fun onSettingItemClicked(item: CommonDataView) {
        mapSettingId(item)
    }

    override fun onSwitchChanged(item: CommonDataView, isActive: Boolean) {
        when (item.id) {
            AccountConstants.SettingCode.SETTING_SHAKE_ID -> {
                accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SHAKE, isActive)
            }
            AccountConstants.SettingCode.SETTING_GEOLOCATION_ID -> {
                createAndShowLocationAlertDialog(isActive)
            }
            AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID -> {
                createAndShowSafeModeAlertDialog(isActive)
            }
            else -> {
            }
        }
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

        context?.run {
            val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.apply  {
                setTitle(dialogTitleMsg)
                setDescription(dialogBodyMsg)
                setPrimaryCTAText(dialogPositiveButton)
                setPrimaryCTAClickListener {
                    viewModel.setSafeMode(currentValue)
                }
                setSecondaryCTAText(dialogNegativeButton)
                setSecondaryCTAClickListener { dismiss() }
            }
        }

    }


    private fun mapSettingId(item: CommonDataView) {
        when (item.id) {
            AccountConstants.SettingCode.SETTING_ACCOUNT_PERSONAL_DATA_ID -> {
                homeAccountAnalytic.eventClickAccountSetting(PERSONAL_DATA)
//                goToApplink(item.applink)
//                intent = RouteManager.getIntent(activity, ApplinkConst.SETTING_PROFILE)
//                activity!!.startActivityForResult(intent, 0)
            }
            AccountConstants.SettingCode.SETTING_ACCOUNT_ADDRESS_ID -> {
                homeAccountAnalytic.eventClickAccountSetting(ADDRESS_LIST)
                goToApplink(item.applink)
            }
//            AccountConstants.SettingCode.SETTING_ACCOUNT_KYC_ID -> onKycMenuClicked()
//            AccountConstants.SettingCode.SETTING_ACCOUNT_SAMPAI_ID -> goToTokopediaCorner()
            AccountConstants.SettingCode.SETTING_BANK_ACCOUNT_ID -> {
                homeAccountAnalytic.eventClickPaymentSetting(ACCOUNT_BANK)
                if (userSession.hasPassword()) {
                    goToApplink(item.applink)
                } else {
                    showNoPasswordDialog()
                }
            }

            AccountConstants.SettingCode.SETTING_TKPD_PAY_ID -> {
                homeAccountAnalytic.eventClickSetting(PAYMENT_METHOD)
                goToApplink(item.applink)
//                startActivity(TkpdPaySettingActivity.createIntent(activity))
            }

            AccountConstants.SettingCode.SETTING_TNC_ID -> {
                homeAccountAnalytic.eventClickSetting(TERM_CONDITION)
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_TERM_CONDITION)
            }

            AccountConstants.SettingCode.SETTING_ABOUT_US -> {
                homeAccountAnalytic.eventClickSetting(ABOUT_US)
                RouteManager.getIntent(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK
                        + AccountConstants.Url.BASE_MOBILE
                        + AccountConstants.Url.PATH_ABOUT_US).run {
                    startActivity(this)
                }
            }

            AccountConstants.SettingCode.SETTING_PRIVACY_ID -> {
                homeAccountAnalytic.eventClickSetting(PRIVACY_POLICY)
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_PRIVACY_POLICY)
            }

            AccountConstants.SettingCode.SETTING_APP_REVIEW_ID -> {
                homeAccountAnalytic.eventClickSetting(APPLICATION_REVIEW)
                goToPlaystore()
            }

            AccountConstants.SettingCode.SETTING_OUT_ID -> {
                homeAccountAnalytic.eventClickSetting(LOGOUT)
                showDialogLogout()
            }

            AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE -> {
                showDialogClearCache()
            }

            AccountConstants.SettingCode.SETTING_SECURITY -> {
                val intent = RouteManager.getIntent(context, item.applink).apply {
                    putExtras(Bundle().apply {
                        putExtra(ApplinkConstInternalGlobal.PARAM_NEW_HOME_ACCOUNT, true)
                    })
                }
                startActivity(intent)
            }

            else -> {
                goToApplink(item.applink)
            }
        }
    }

    private fun doLogout() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.LOGOUT))
        }
    }

    private fun showDialogLogout() {

        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.account_home_label_logout))
            dialog.setDescription(getString(R.string.account_home_label_logout_confirmation))
            dialog.setPrimaryCTAText(getString(R.string.account_home_button_logout))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                doLogout()
            }
            dialog.setSecondaryCTAText(getString(R.string.account_home_label_cancel))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }


//        context?.run {
//            val dialog = Dialog(this, Dialog.Type.PROMINANCE)
//            dialog.setTitle(getString(R.string.account_home_label_logout))
//            dialog.setDesc(getString(R.string.account_home_label_logout_confirmation))
//            dialog.setBtnOk(getString(R.string.account_home_button_logout))
//            dialog.setBtnCancel(getString(R.string.account_home_label_cancel))
//            dialog.setOnOkClickListener { v ->
//                dialog.dismiss()
//                sendNotif()
//            }
//            dialog.setOnCancelClickListener { v -> dialog.dismiss() }
//            dialog.show()
//        }
    }

    private fun createAndShowLocationAlertDialog(currentValue: Boolean) {
        if (!currentValue) {
            homeAccountAnalytic.eventClickToggleOnGeolocation(activity)
        } else {
            homeAccountAnalytic.eventClickToggleOffGeolocation(activity)
        }

        context?.run {
            val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
//                setTitle(getString(R.string.account_home_title_geolocation_alertdialog))
                setDescription(getString(R.string.account_home_title_geolocation_alertdialog))
                setPrimaryCTAText(getString(R.string.account_home_ok_geolocation_alertdialog))
                setPrimaryCTAClickListener {
                    goToApplicationDetailActivity()
                }
                setSecondaryCTAText(getString(R.string.account_home_batal_geolocation_alertdialog))
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }
            dialog.show()
        }
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

    private fun goToPlaystore() {
        activity?.let {
            val uri = Uri.parse("market://details?id=" + it.application.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                it.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                it.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(AccountConstants.SettingCode.PLAYSTORE_URL + it.application.packageName)))
            }
        }
    }

    private fun showNoPasswordDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(resources.getString(R.string.error_bank_no_password_title))
        builder.setMessage(resources.getString(R.string.error_bank_no_password_content))
        builder.setPositiveButton(resources.getString(R.string.error_no_password_yes)) { dialogInterface: DialogInterface, i: Int ->
            intentToAddPassword()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.error_no_password_no)) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(activity, R.color.colorSheetTitle))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(activity, R.color.tkpd_main_green))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
    }

    private fun intentToAddPassword() {
        if (activity != null) {
            startActivityForResult(RouteManager.getIntent(activity,
                    ApplinkConstInternalGlobal.ADD_PASSWORD), AccountConstants.REQUEST.REQUEST_ADD_PASSWORD)
        }
    }

    private fun createCoachmark(){
        val coachMarkItem = ArrayList<CoachMarkItem>().apply {
            add(
                    CoachMarkItem(
                            account_user_item_profile_edit,
                            "Ubah data diri",
                            "Kamu bisa ubah nama, foto profil, kontak, dan biodata di sini.",
                            CoachMarkContentPosition.BOTTOM,
                            scrollView = home_account_user_fragment_rv
                    )
            )

            add(
                    CoachMarkItem(
                            home_account_profile_financial_section,
                            "Cek jumlah dana dan investasimu",
                            "Punya dana dan investasi di Tokopedia? Mulai dari Saldo Tokopedia sampai emas, bisa cek di sini.",
                            CoachMarkContentPosition.BOTTOM,
                            scrollView = home_account_user_fragment_rv
                    )
            )

            add(
                    CoachMarkItem(
                            home_account_profile_member_section,
                            "Lihat keuntunganmu di sini",
                            "Cek keuntunganmu di TokoMember, Membership, dan daftar kupon, atau selesaikan tantangan untuk dapatkan keuntungan baru.",
                            CoachMarkContentPosition.TOP,
                            scrollView = home_account_user_fragment_rv
                    )
            )

        }
        val coachMark = CoachMark()

        coachMark.show(activity, "homeAccountUserFragmentCoachmark", coachMarkItem)
    }

    companion object {

        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2

        fun newInstance(bundle: Bundle?): Fragment {
            return HomeAccountUserFragment().apply {
                arguments = bundle
            }
        }
    }
}