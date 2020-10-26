package com.tokopedia.home_account.view

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.home_account_coordinator_layout.*
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
                is Success -> {
//                    toolbar_tokopoint?.addItem(mapper.mapToProfileDataView(it.data))
                    addItem(mapper.mapToProfileDataView(it.data))
                    setLayoutParams(200)
//                    activity?.run {
//                        if(this is HomeAccountUserActivity){
//                            setupToolbarContent(mapper.mapToProfileDataView(it.data))
//                        }
//                    }
                    viewModel.getInitialData()

                    hideLoading()
                }
            }
        })
    }

    fun showLoading() {
        container_main?.displayedChild = CONTAINER_LOADER
    }

    fun hideLoading() {
        container_main?.displayedChild = CONTAINER_DATA
    }

    private fun hideStatusBar() {
        home_account_coordinator!!.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            home_account_coordinator!!.requestApplyInsets()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = home_account_coordinator!!.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            home_account_coordinator!!.systemUiVisibility = flags
            activity!!.window.statusBarColor = Color.WHITE
        }
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity!!.window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
        val win = activity!!.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun setStatusBarViewHeight() {
        if (activity != null) status_bar_bg?.layoutParams?.height = getStatusBarHeight(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()

        setStatusBarViewHeight()

        (activity as BaseSimpleActivity?)?.run {
            setSupportActionBar(toolbar_tokopoint)
            toolbar_tokopoint?.title = "Akun Saya"
        }

        collapsing_toolbar?.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
        collapsing_toolbar?.setTitle(" ")
        home_account_user_fragment_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = recyclerView.computeVerticalScrollOffset()
                handleAppBarOffsetChange(offset)
                handleAppBarIconChange(offset)
            }
        })

        setupObserver()
        adapter = HomeAccountUserAdapter(this)
        setupList()

        viewModel.getBuyerData()
    }


    private fun setLayoutParams(cardheight: Int) {
        val statusBarHeight = getStatusBarHeight(activity)
        val layoutParams = toolbar_tokopoint!!.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = statusBarHeight
        toolbar_tokopoint!!.layoutParams = layoutParams
        val imageEggLp = view?.findViewById<AppCompatImageView>(R.id.account_user_item_profile_avatar)?.layoutParams as? RelativeLayout.LayoutParams
        imageEggLp?.topMargin = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_top_margin_big_image)).toInt()
        view?.findViewById<AppCompatImageView>(R.id.account_user_item_profile_avatar)?.layoutParams = imageEggLp
        val imageBigLp = view?.findViewById<ImageView>(R.id.account_user_item_profile_backdrop)?.layoutParams as? RelativeLayout.LayoutParams
        imageBigLp?.height = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_home_top_bg_height) + cardheight).toInt()
        view?.findViewById<ImageView>(R.id.account_user_item_profile_backdrop)?.layoutParams = imageBigLp
    }


    fun getStatusBarHeight(context: Context?): Int {
        var height = 0
        val resId = context!!.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

    private fun handleAppBarOffsetChange(offset: Int) {
        val toolbarTransitionRange = (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height)
                - toolbar_tokopoint!!.height - getStatusBarHeight(activity))
        var offsetAlpha = 255f / toolbarTransitionRange * (toolbarTransitionRange - offset)
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        var alpha = offsetAlpha / 255 - 1
        if (alpha < 0) alpha = alpha * -1
        status_bar_bg?.alpha = alpha
        if (alpha > 0.5) toolbar_tokopoint?.switchToDarkMode() else toolbar_tokopoint?.switchToTransparentMode()
        toolbar_tokopoint?.applyAlphaToToolbarBackground(alpha)
    }

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

    private fun handleAppBarIconChange(verticalOffset: Int) {
        val verticalOffset1 = Math.abs(verticalOffset)
        if (verticalOffset1 >= (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height))) {
            toolbar_tokopoint?.showToolbarIcon()
        } else
            toolbar_tokopoint?.hideToolbarIcon()
    }

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
//            AccountConstants.SettingCode.SETTING_GEOLOCATION_ID -> hasLocationPermission()
//            AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID -> isItemSelected(getString(R.string.pref_safe_mode), isActive)
            else -> {
            }
        }
    }


    private fun mapSettingId(item: CommonDataView) {
        when (item.id) {
            AccountConstants.SettingCode.SETTING_ACCOUNT_PERSONAL_DATA_ID -> {
//                accountAnalytics.eventClickAccountSetting(PERSONAL_DATA)
//                goToApplink(item.applink)
//                intent = RouteManager.getIntent(activity, ApplinkConst.SETTING_PROFILE)
//                activity!!.startActivityForResult(intent, 0)
            }
            AccountConstants.SettingCode.SETTING_ACCOUNT_ADDRESS_ID -> {
//                accountAnalytics.eventClickAccountSetting(ADDRESS_LIST)
                goToApplink(item.applink)
            }
//            AccountConstants.SettingCode.SETTING_ACCOUNT_KYC_ID -> onKycMenuClicked()
//            AccountConstants.SettingCode.SETTING_ACCOUNT_SAMPAI_ID -> goToTokopediaCorner()
            AccountConstants.SettingCode.SETTING_BANK_ACCOUNT_ID -> {
//                accountAnalytics.eventClickPaymentSetting(ACCOUNT_BANK)
                if (userSession.hasPassword()) {
                    goToApplink(item.applink)
                } else {
                    showNoPasswordDialog()
                }
            }

            AccountConstants.SettingCode.SETTING_TKPD_PAY_ID -> {
//                accountAnalytics.eventClickSetting(PAYMENT_METHOD)
                goToApplink(item.applink)
//                startActivity(TkpdPaySettingActivity.createIntent(activity))
            }

            AccountConstants.SettingCode.SETTING_TNC_ID -> {
//                accountAnalytics.eventClickSetting(TERM_CONDITION)
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_TERM_CONDITION)
            }

            AccountConstants.SettingCode.SETTING_ABOUT_US -> {
//                accountAnalytics.eventClickSetting(ABOUT_US)
                RouteManager.getIntent(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK
                        + AccountConstants.Url.BASE_MOBILE
                        + AccountConstants.Url.PATH_ABOUT_US).run {
                    startActivity(this)
                }
            }

            AccountConstants.SettingCode.SETTING_PRIVACY_ID -> {
//                accountAnalytics.eventClickSetting(PRIVACY_POLICY)
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_PRIVACY_POLICY)
            }

            AccountConstants.SettingCode.SETTING_APP_REVIEW_ID -> {
//                accountAnalytics.eventClickSetting(APPLICATION_REVIEW)
                goToPlaystore()
            }

            AccountConstants.SettingCode.SETTING_OUT_ID -> {
//                accountAnalytics.eventClickSetting(LOGOUT)
                showDialogLogout()
            }

            AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE -> {
                showDialogClearCache()
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