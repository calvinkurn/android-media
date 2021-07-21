package com.tokopedia.home.account.presentation.fragment.setting

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import android.text.Html
import android.view.View
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.R
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.constant.SettingConstant
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.util.*

class AdvancedSettingFragment : BaseGeneralSettingFragment() {

    private val TAG = AdvancedSettingFragment::class.java.simpleName

    private lateinit var accountAnalytics: AccountAnalytics

    private var isShowScreenRecorder = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            isShowScreenRecorder = FirebaseRemoteConfigImpl(it).getBoolean(RemoteConfigKey.SETTING_SHOW_SCREEN_RECORDER, false)
        }
        accountAnalytics = AccountAnalytics(context)
    }

    override fun getSettingItems(): MutableList<SettingItemViewModel> {
        val settingItems = ArrayList<SettingItemViewModel>()
        settingItems.add(SettingItemViewModel(SettingConstant.SETTING_APP_ADVANCED_CLEAR_CACHE,
                getString(R.string.title_app_advanced_clear_cache)))
        if (isShowScreenRecorder) {
            settingItems.add(SettingItemViewModel(SettingConstant.SETTING_APP_ADVANCED_SCREEN_RECORDER,
                    getString(R.string.title_app_advanced_screen_recorder)))
        }
        return settingItems
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(DividerItemDecoration(activity))
    }

    override fun onItemClicked(settingId: Int) {
        if (settingId == SettingConstant.SETTING_APP_ADVANCED_CLEAR_CACHE) {
            accountAnalytics.eventClickAdvancedSetting(AccountConstants.Analytics.CLEAR_CACHE)
            showDialogClearCache()
        } else if (settingId == SettingConstant.SETTING_APP_ADVANCED_SCREEN_RECORDER) {
            context?.let {
                accountAnalytics.eventClickScreenRecorder()
                RouteManager.route(it, ApplinkConstInternalGlobal.SCREEN_RECORDER)
            }
        }
    }

    override fun getScreenName(): String {
        return TAG
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

    companion object {

        @JvmStatic
        fun createInstance(): Fragment {
            return AdvancedSettingFragment()
        }
    }
}