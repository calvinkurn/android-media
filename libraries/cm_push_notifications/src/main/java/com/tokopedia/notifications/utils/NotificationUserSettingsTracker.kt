package com.tokopedia.notifications.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.NotificationSettingTrackerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class NotificationUserSettingsTracker @Inject constructor(
    val context: Context
): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val graphRepository: GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }

    private val postNotificationPermission = "android.permission.POST_NOTIFICATIONS"
    private val sdkLevel33 = 33

    private fun getSettingTrackerUseCase() : NotificationSettingTrackerUseCase {
        return NotificationSettingTrackerUseCase(graphRepository)
    }

    fun sendNotificationUserSettings() {
        if (Build.VERSION.SDK_INT >= sdkLevel33) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    postNotificationPermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    getSettingTrackerUseCase().sendTrackerUserSettings({}, {})
                } catch (_: Exception) {

                }
            }
        }
    }
}
