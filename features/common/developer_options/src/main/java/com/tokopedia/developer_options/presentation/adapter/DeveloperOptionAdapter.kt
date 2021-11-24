package com.tokopedia.developer_options.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactoryImpl
import com.tokopedia.developer_options.presentation.model.*

class DeveloperOptionAdapter(
    typeFactory: DeveloperOptionTypeFactoryImpl,
    differ: DeveloperOptionDiffer
) : BaseDeveloperOptionAdapter<Visitable<*>, DeveloperOptionTypeFactoryImpl>(typeFactory, differ) {

    companion object {
        const val KEYWORD_PRODUCT_DETAIL_DEV = "Product Detail Dev"
        const val KEYWORD_ACCESS_TOKEN = "Access Token"
        const val KEYWORD_SYSTEM_APPS_NON_SYSTEM_APPS = "System Apps Non System Apps"
        const val KEYWORD_RESET_ONBOARDING = "Reset OnBoarding"
        const val KEYWORD_FORCE_CRASH = "Force Crash"
        const val KEYWORD_SEND_FIREBASE_EXCEPTION = "Send Firebase Exception"
        const val KEYWORD_OPEN_SCREEN_RECORDER = "Open Screen Recorder"
        const val KEYWORD_ENABLE_NETWORK_LOG_ON_NOTIFICATION = "Enable Network Log on Notification"
        const val KEYWORD_VIEW_NETWORK_LOG = "View Network Log"
        const val KEYWORD_DEVICE_ID = "Device Id"
        const val KEYWORD_FORCE_DARK_MODE = "Force Dark Mode"
        const val KEYWORD_ENABLE_TOPADS_LOG_ON_NOTIFICATION = "Enable TopAds Log on Notification"
        const val KEYWORD_VIEW_TOPADS_LOG = "View TopAds Log"
        const val KEYWORD_ENABLE_APPLINK_LOG_ON_NOTIFICATION = "Enable Applink Log on Notification"
        const val KEYWORD_VIEW_APPLINK_LOG = "View Applink Log"
        const val KEYWORD_ENABLE_FPM_LOG_ON_FILE = "Enable FPM Log on File (Download/fpm-auto-log.txt)"
        const val KEYWORD_ENABLE_FPM_LOG_ON_NOTIFICATION = "Enable FPM Log on Notification"
        const val KEYWORD_VIEW_FPM_LOG = "View FPM Log"
        const val KEYWORD_ENABLE_ANALYTICS_LOG_ON_NOTIFICATION = "Enable Analytics Log on Notification"
        const val KEYWORD_CASSAVA = "Cassava"
        const val KEYWORD_VIEW_ANALYTICS_LOG = "View Analytics Log"
        const val KEYWORD_VIEW_IRIS_LOG = "View Iris Save Log View Iris Send Log"
    }

    private val defaultItems = listOf(
        PdpDevUiModel(KEYWORD_PRODUCT_DETAIL_DEV),
        AccessTokenUiModel(KEYWORD_ACCESS_TOKEN),
        SystemNonSystemAppsUiModel(KEYWORD_SYSTEM_APPS_NON_SYSTEM_APPS),
        ResetOnBoardingUiModel(KEYWORD_RESET_ONBOARDING),
        ForceCrashUiModel(KEYWORD_FORCE_CRASH),
        SendFirebaseCrashExceptionUiModel(KEYWORD_SEND_FIREBASE_EXCEPTION),
        OpenScreenRecorderUiModel(KEYWORD_OPEN_SCREEN_RECORDER),
        NetworkLogOnNotificationUiModel(KEYWORD_ENABLE_NETWORK_LOG_ON_NOTIFICATION),
        ViewNetworkLogUiModel(KEYWORD_VIEW_NETWORK_LOG),
        DeviceIdUiModel(KEYWORD_DEVICE_ID),
        ForceDarkModeUiModel(KEYWORD_FORCE_DARK_MODE),
        TopAdsLogOnNotificationUiModel(KEYWORD_ENABLE_TOPADS_LOG_ON_NOTIFICATION),
        ViewTopAdsLogUiModel(KEYWORD_VIEW_TOPADS_LOG),
        ApplinkLogOnNotificationUiModel(KEYWORD_ENABLE_APPLINK_LOG_ON_NOTIFICATION),
        ViewApplinkLogUiModel(KEYWORD_VIEW_APPLINK_LOG),
        FpmLogOnFileUiModel(KEYWORD_ENABLE_FPM_LOG_ON_FILE),
        FpmLogOnNotificationUiModel(KEYWORD_ENABLE_FPM_LOG_ON_NOTIFICATION),
        ViewFpmLogUiModel(KEYWORD_VIEW_FPM_LOG),
        AnalyticsLogOnNotificationUiModel(KEYWORD_ENABLE_ANALYTICS_LOG_ON_NOTIFICATION),
        CassavaUiModel(KEYWORD_CASSAVA),
        ViewAnalyticsLogUiModel(KEYWORD_VIEW_ANALYTICS_LOG),
        ViewIrisLogUiModel(KEYWORD_VIEW_IRIS_LOG)
    )

    fun searchItem(keyword: String) {
        val newItems = mutableListOf<OptionItemUiModel>()
        defaultItems.forEach {
            if (it.keyword.lowercase().contains(keyword.lowercase())) {
                newItems.add(it)
            }
        }
        submitList(newItems)
    }

    fun setDefaultItem() {
        submitList(defaultItems)
    }
}