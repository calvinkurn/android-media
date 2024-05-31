package com.tokopedia.developer_options.tracker

import android.content.Context
import com.tokopedia.skynet.IdType
import com.tokopedia.skynet.InfluxInteractor
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

internal object DevOpsTracker {

    private val scope =
        CoroutineScope(
            CoroutineName("influxTracker") + SupervisorJob() +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    Timber.e(throwable)
                }
        )

    private var influx: InfluxInteractor? = null

    fun init(context: Context) {
        if (influx == null) {
            try {
                val id = UserSession(context).androidId
                scope.launch(Dispatchers.IO) {
                    val i = InfluxInteractor.Builder("tkpd:tkpd")
                        .measurement("devops_analytics")
                        .setIdentity(IdType.CUSTOM(id))
                        .build()
                    if (i.ping()) {
                        influx = i
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun trackEntryEvent(page: DevopsFeature) {
        if (influx == null) return
        scope.launch(Dispatchers.IO) {
            influx?.send(
                tags = mapOf("eventType" to "entry", "feature" to page.toString()),
                values = mapOf("count" to 1)
            )
        }
    }
}

internal enum class DevopsFeature {
    LOGIN_HELPER,
    ENABLE_DEVOPS_ON_NOTIF,
    PRODUCT_DETAIL_DEV,
    MOCK_DYNAMIC_WIDGET,
    MSSDK_DEV,
    SHOP_PAGE_DEV,
    SYSTEM_APPS,
    NON_SYSTEM_APPS,
    RESET_ONBOARDING,
    FORCE_LOGOUT,
    FORCE_CRASH,
    SCREEN_RECORDER,
    USE_SCP,
    ENABLE_NEW_TYPO_GUIDE,
    ENABLE_BANNER,
    FORCE_DARKMODE,
    ROUTEMANAGER_ROUTE,
    VIEW_APPLINK_LIST,
    TRANSLATOR_TOGGLE,
    CONVERT_RESOURCE_ID,
    VIEW_HANSELPATCH,
    EXTRACT_BRANCH_LINK,
    SEND_FIREBASE_EXCEPTION,
    VIEW_NETWORK_LOG,
    VIEW_TOPADS_LOG,
    VIEW_APPLINK_LOG,
    VIEW_JOURNEY_LOG,
    VIEW_FPM_LOG,
    CASSAVA,
    VIEW_ANALYTICS_LOG,
    VIEW_IRIS_SAVE_LOG,
    VIEW_IRIS_SEND_LOG,
    REMOTE_CONFIG_EDITOR,
    SEND_LOG_TO_SERVER,
    VIEW_SERVER_LOGGER,
    SHARED_PREF_EDITOR,
    VERSION_CHANGER,
    ENV_CHANGER,
    VIEW_FAKE_RESPONSE,
    VIEW_DATA_EXPLORER,
    AB_TEST_MANUAL_APPLY,
    VIEW_AB_TEST_LIST,
    VIEW_SSE_LOGGING,
    VIEW_PLAY_WEBSOCKET_LOG,
    VIEW_TOPCHAT_WEBSOCKET_LOG,
    ENABLE_FPI_MONITORING,
    USER_ID,
    SHOP_ID,
    OK_HTTP_TIMEOUT_HANDLER
}
