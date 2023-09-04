package com.tokopedia.notifications.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.NotificationSettingTrackerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NotificationUserSettingsTracker @Inject constructor(
    val context: Context
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val graphRepository: GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }

    private val sharedPreference: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        context.getSharedPreferences(TRACKER_PREF_NAME, BaseActivity.MODE_PRIVATE)
    }

    private val postNotificationPermission = "android.permission.POST_NOTIFICATIONS"
    private val sdkLevel33 = 33

    private fun getSettingTrackerUseCase(): NotificationSettingTrackerUseCase {
        return NotificationSettingTrackerUseCase(graphRepository)
    }

    private fun isSettingsSent(): Boolean {
        if (GlobalConfig.isSellerApp()) {
            return sharedPreference.getBoolean(NOTIFICATION_USER_SETTING_KEY_SA, false)
        }
        return sharedPreference.getBoolean(NOTIFICATION_USER_SETTING_KEY, false)
    }

    fun sendNotificationUserSettings() {
        if (Build.VERSION.SDK_INT >= sdkLevel33) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    postNotificationPermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    if (!isSettingsSent()) {
                        getSettingTrackerUseCase().sendTrackerUserSettings({}, {})
                        saveSettingsInCache(true)
                    }
                } catch (_: Exception) {
                }
            } else {
                if (isSettingsSent()) {
                    saveSettingsInCache(false)
                }
            }
        }
    }

    private fun saveSettingsInCache(isPermissionGranted: Boolean) {
        if (GlobalConfig.isSellerApp()) {
            saveInSharedPreference(isPermissionGranted, NOTIFICATION_USER_SETTING_KEY_SA)
        } else {
            saveInSharedPreference(isPermissionGranted, NOTIFICATION_USER_SETTING_KEY)
        }
    }

    private fun saveInSharedPreference(isPermissionGranted: Boolean, key: String) {
        if (isPermissionGranted) {
            sharedPreference.edit()
                .putBoolean(key, true)
                .apply()
        } else {
            sharedPreference.edit()
                .putBoolean(key, false)
                .apply()
        }
    }

    companion object {
        const val TRACKER_PREF_NAME = "NotificationUserSettings"
        const val NOTIFICATION_USER_SETTING_KEY = "isUserSettingSent"
        const val NOTIFICATION_USER_SETTING_KEY_SA = "isSellerSettingSent"
    }
}
