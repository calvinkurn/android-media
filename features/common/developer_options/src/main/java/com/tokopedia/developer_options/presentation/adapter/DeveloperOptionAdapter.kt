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
        const val KEYWORD_SYSTEM_APPS = "System Apps"
        const val KEYWORD_NON_SYSTEM_APPS = "Non System Apps"
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
        const val KEYWORD_VIEW_IRIS_SAVE_LOG = "View Iris Save Log"
        const val KEYWORD_VIEW_IRIS_SEND_LOG = "View Iris Send Log"
        const val KEYWORD_ENABLE_LEAK_CANARY = "Enable Leak Canary"
        const val KEYWORD_REMOTE_CONFIG_EDITOR = "Remote Config Editor"
        const val KEYWORD_ROUTE_MANAGER = "Try RouteManager.route"
        const val KEYWORD_LOGGING_TO_SERVER = "Logging To Server"
        const val KEYWORD_SEND_LOG_TO_SERVER = "Send Log To Server"
        const val KEYWORD_SHARED_PREFERENCES_EDITOR = "Shared Preferences Editor"
        const val KEYWORD_APP_VERSION = "Version change is for api purpose - api kill will change back"
        const val KEYWORD_CHOOSE_URL_ENVIRONMENT = "Choose URL Environment"
        const val KEYWORD_STAGING = "Staging"
        const val KEYWORD_LIVE = "Live"
        const val KEYWORD_FAKE_RESPONSE_ACTIVITY = "Fake Response Activity"
        const val KEYWORD_HOME_AND_NAVIGATION_REVAMP_SWITCHER = "Home and Navigation Revamp Switcher"
        const val KEYWORD_NEW_NAVIGATION = "New Navigation"
        const val KEYWORD_ALWAYS_OS_EXPERIMENT = "Always OS Experiment"
        const val KEYWORD_OLD_BALANCE_WIDGET = "Old Balance Widget"
        const val KEYWORD_NEW_BALANCE_WIDGET = "New Balance Widget"
        const val KEYWORD_OLD_INBOX = "Old Inbox"
        const val KEYWORD_NEW_INBOX = "New Inbox"
        const val KEYWORD_OLD_CART_CHECKOUT = "Old Cart Checkout"
        const val KEYWORD_NEW_CART_CHECKOUT = "New Cart Checkout Bundling"
        const val KEYWORD_ROLLENCE_AB_TESTING_MANUAL_SWITCHER = "Rollence AB Testing Manual Switcher"
    }

    private val defaultItems = listOf(
        PdpDevUiModel(listOf(KEYWORD_PRODUCT_DETAIL_DEV)),
        AccessTokenUiModel(listOf(KEYWORD_ACCESS_TOKEN)),
        SystemNonSystemAppsUiModel(listOf(
            KEYWORD_SYSTEM_APPS,
            KEYWORD_NON_SYSTEM_APPS)
        ),
        ResetOnBoardingUiModel(listOf(KEYWORD_RESET_ONBOARDING)),
        ForceCrashUiModel(listOf(KEYWORD_FORCE_CRASH)),
        SendFirebaseCrashExceptionUiModel(listOf(KEYWORD_SEND_FIREBASE_EXCEPTION)),
        OpenScreenRecorderUiModel(listOf(KEYWORD_OPEN_SCREEN_RECORDER)),
        NetworkLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_NETWORK_LOG_ON_NOTIFICATION)),
        ViewNetworkLogUiModel(listOf(KEYWORD_VIEW_NETWORK_LOG)),
        DeviceIdUiModel(listOf(KEYWORD_DEVICE_ID)),
        ForceDarkModeUiModel(listOf(KEYWORD_FORCE_DARK_MODE)),
        TopAdsLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_TOPADS_LOG_ON_NOTIFICATION)),
        ViewTopAdsLogUiModel(listOf(KEYWORD_VIEW_TOPADS_LOG)),
        ApplinkLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_APPLINK_LOG_ON_NOTIFICATION)),
        ViewApplinkLogUiModel(listOf(KEYWORD_VIEW_APPLINK_LOG)),
        FpmLogOnFileUiModel(listOf(KEYWORD_ENABLE_FPM_LOG_ON_FILE)),
        FpmLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_FPM_LOG_ON_NOTIFICATION)),
        ViewFpmLogUiModel(listOf(KEYWORD_VIEW_FPM_LOG)),
        AnalyticsLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_ANALYTICS_LOG_ON_NOTIFICATION)),
        CassavaUiModel(listOf(KEYWORD_CASSAVA)),
        ViewAnalyticsLogUiModel(listOf(KEYWORD_VIEW_ANALYTICS_LOG)),
        ViewIrisLogUiModel(listOf(
            KEYWORD_VIEW_IRIS_SAVE_LOG,
            KEYWORD_VIEW_IRIS_SEND_LOG
        )),
        LeakCanaryUiModel(listOf(KEYWORD_ENABLE_LEAK_CANARY)),
        RemoteConfigEditorUiModel(listOf(KEYWORD_REMOTE_CONFIG_EDITOR)),
        RouteManagerUiModel(listOf(KEYWORD_ROUTE_MANAGER)),
        LoggingToServerUiModel(listOf(
            KEYWORD_LOGGING_TO_SERVER,
            KEYWORD_SEND_LOG_TO_SERVER)
        ),
        SharedPreferencesEditorUiModel(listOf(KEYWORD_SHARED_PREFERENCES_EDITOR)),
        AppVersionUiModel(listOf(KEYWORD_APP_VERSION)),
        UrlEnvironmentUiModel(listOf(
            KEYWORD_CHOOSE_URL_ENVIRONMENT,
            KEYWORD_STAGING, KEYWORD_LIVE)
        ),
        FakeResponseActivityUiModel(listOf(KEYWORD_FAKE_RESPONSE_ACTIVITY)),
        RollenceAbTestingManualSwitcherUiModel(listOf(KEYWORD_ROLLENCE_AB_TESTING_MANUAL_SWITCHER)),
        HomeAndNavigationRevampSwitcherUiModel(listOf(
            KEYWORD_HOME_AND_NAVIGATION_REVAMP_SWITCHER,
            KEYWORD_NEW_NAVIGATION,
            KEYWORD_ALWAYS_OS_EXPERIMENT,
            KEYWORD_OLD_BALANCE_WIDGET,
            KEYWORD_NEW_BALANCE_WIDGET,
            KEYWORD_OLD_INBOX,
            KEYWORD_NEW_INBOX,
            KEYWORD_OLD_CART_CHECKOUT,
            KEYWORD_NEW_CART_CHECKOUT
        )),
    )

    fun searchItem(keyword: String) {
        val newItems = mutableListOf<OptionItemUiModel>()
        defaultItems.forEach { model ->
            if (!model.keyword.filter { it.lowercase().contains(keyword.lowercase()) }.isNullOrEmpty()) {
                newItems.add(model)
            }
        }
        submitList(newItems)
    }

    fun setDefaultItem() {
        submitList(defaultItems)
    }
}