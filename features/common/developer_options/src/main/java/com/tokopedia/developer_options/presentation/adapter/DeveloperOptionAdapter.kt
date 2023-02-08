package com.tokopedia.developer_options.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactoryImpl
import com.tokopedia.developer_options.presentation.model.*

/**
 * @author Said Faisal on 24/11/2021
 */

class DeveloperOptionAdapter(
    typeFactory: DeveloperOptionTypeFactoryImpl,
    differ: DeveloperOptionDiffer
) : BaseDeveloperOptionAdapter<Visitable<*>, DeveloperOptionTypeFactoryImpl>(typeFactory, differ) {

    /**
     * @see KEYWORD prefix
     *
     * Keyword you need while searching something
     **/
    companion object {
        const val KEYWORD_DEVELOPER_OPTIONS_ON_NOTIFICATION = "Enable Developer Options on Notification"
        const val KEYWORD_PRODUCT_DETAIL_DEV = "Product Detail Dev"
        const val KEYWORD_ACCESS_TOKEN = "Access Token"
        const val KEYWORD_SYSTEM_APPS = "System Apps"
        const val KEYWORD_NON_SYSTEM_APPS = "Non System Apps"
        const val KEYWORD_RESET_ONBOARDING = "Reset OnBoarding"
        const val KEYWORD_FORCE_CRASH = "Force Crash"
        const val KEYWORD_FORCE_LOGOUT = "Force Logout"
        const val KEYWORD_SEND_FIREBASE_EXCEPTION = "Send Firebase Exception"
        const val KEYWORD_OPEN_SCREEN_RECORDER = "Open Screen Recorder"
        const val KEYWORD_ENABLE_NETWORK_LOG_ON_NOTIFICATION = "Enable Network Log on Notification"
        const val KEYWORD_VIEW_NETWORK_LOG = "View Network Log"
        const val KEYWORD_DEVICE_ID = "Device Id"
        const val KEYWORD_FORCE_DARK_MODE = "Force Dark Mode"
        const val KEYWORD_ENABLE_TOPADS_LOG_ON_NOTIFICATION = "Enable TopAds Log on Notification"
        const val KEYWORD_VIEW_TOPADS_LOG = "View TopAds Log"
        const val KEYWORD_ENABLE_APPLINK_LOG_ON_NOTIFICATION = "Enable Applink Log on Notification"
        const val KEYWORD_ENABLE_JOURNEY_LOG_ON_NOTIFICATION = "Enable Journey Log on Notification"
        const val KEYWORD_VIEW_APPLINK_LOG = "View Applink Log"
        const val KEYWORD_VIEW_JOURNEY_LOG = "View Journey Log"
        const val KEYWORD_ENABLE_FPM_LOG_ON_FILE = "Enable FPM Log on File (Download/fpm-auto-log.txt)"
        const val KEYWORD_ENABLE_FPM_LOG_ON_NOTIFICATION = "Enable FPM Log on Notification"
        const val KEYWORD_VIEW_FPM_LOG = "View FPM Log"
        const val KEYWORD_ENABLE_ANALYTICS_LOG_ON_NOTIFICATION = "Enable Analytics Log on Notification"
        const val KEYWORD_CASSAVA = "Cassava"
        const val KEYWORD_VIEW_ANALYTICS_LOG = "View Analytics Log"
        const val KEYWORD_VIEW_IRIS_SAVE_LOG = "View Iris Save Log"
        const val KEYWORD_VIEW_IRIS_SEND_LOG = "View Iris Send Log"
        const val KEYWORD_ENABLE_LEAK_CANARY = "Enable Leak Canary"
        const val KEYWORD_ENABLE_STRICT_MODE_LEAK_CANARY = "Enable Strict Mode"
        const val KEYWORD_REMOTE_CONFIG_EDITOR = "Remote Config Editor"
        const val KEYWORD_ROUTE_MANAGER = "Try RouteManager.route"
        const val KEYWORD_VIEW_APPLINK_LIST = "View Applink List"
        const val KEYWORD_LOGGING_TO_SERVER = "Logging To Server"
        const val KEYWORD_SEND_LOG_TO_SERVER = "Send Log To Server"
        const val KEYWORD_VIEW_SERVER_LOGGER = "View Server Logger"
        const val KEYWORD_SHARED_PREFERENCES_EDITOR = "Shared Preferences Editor"
        const val KEYWORD_APP_VERSION = "Version change is for api purpose - api kill will change back"
        const val KEYWORD_CHOOSE_URL_ENVIRONMENT = "Choose URL Environment"
        const val KEYWORD_STAGING = "Staging"
        const val KEYWORD_LIVE = "Live"
        const val KEYWORD_FAKE_RESPONSE_ACTIVITY = "Fake Response Activity"
        const val KEYWORD_DATA_EXPLORER_ACTIVITY = "Data Explrorer Activity"
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
        const val KEYWORD_LIST_AB_TEST_ROLLENCE_KEYS = "List AB Test Rollence Keys"
        const val KEYWORD_REQUEST_NEW_FCM_TOKEN = "Request New FCM Token"
        const val KEYWORD_RESET_ONBOARDING_NAVIGATION = "Reset OnBoarding Navigation"
        const val KEYWORD_TRANSLATOR = "Translator (ON/OFF)"
        const val KEYWORD_API_KEY_SETTING = "API Key Setting"
        const val KEYWORD_VISIT_BELOW_FOR_API_KEY = "Visit Below For New API Key"
        const val KEYWORD_LANGUAGE_SETTING = "Language Setting"
        const val KEYWORD_CURRENTLY_SELECTED_LANGUAGES = "Currently selected languages is from Indonesian to English"
        const val KEYWORD_TOTAL_TRANSLATED_TEXT = "Total Translated Text"
        const val KEYWORD_APP_AUTH_SECRET = "App Auth Secret (IT Risk)"
        const val KEYWORD_ENABLE_SELLER_APP_REVIEW_DEBUGGING = "Enable Seller App Review Debugging"
        const val KEYWORD_SHOW_APPLINK_ON_TOAST = "Show Applink on Toast and Copy the Link to Clipboard"
        const val KEYWORD_PLAY_WEB_SOCKET_SSE_LOGGING = "Play - Web Socket and SSE Logging"
        const val KEYWORD_VIEW_SSE_LOGGING = "View SSE Logging"
        const val KEYWORD_TYPOGRAPHY_NEW_FONT = "Switch Typography Guideline"
        const val KEYWORD_CONVERT_RESOURCE_ID = "Convert Resource ID to Resource Name"
        const val KEYWORD_VIEW_HANSEL_PATCH_LIST = "View Hansel Patch List"
        const val KEYWORD_TOPCHAT_WEB_SOCKET_LOGGING = "Topchat - Web Socket Logging"
    }

    /**
     * @see defaultItems
     *
     * Variable contains UiModels that you want to show in RecyclerView, put keyword as param
     **/
    private val defaultItems = mutableListOf(
        DeveloperOptionsOnNotificationUiModel(listOf(KEYWORD_DEVELOPER_OPTIONS_ON_NOTIFICATION)),
        PdpDevUiModel(listOf(KEYWORD_PRODUCT_DETAIL_DEV)),
        AccessTokenUiModel(listOf(KEYWORD_ACCESS_TOKEN)),
        AppAuthSecretUiModel(listOf(KEYWORD_APP_AUTH_SECRET)),
        SystemNonSystemAppsUiModel(
            listOf(
                KEYWORD_SYSTEM_APPS,
                KEYWORD_NON_SYSTEM_APPS
            )
        ),
        ResetOnBoardingUiModel(listOf(KEYWORD_RESET_ONBOARDING)),
        ForceLogoutUiModel(listOf(KEYWORD_FORCE_LOGOUT)),
        ForceCrashUiModel(listOf(KEYWORD_FORCE_CRASH)),
        SendFirebaseCrashExceptionUiModel(listOf(KEYWORD_SEND_FIREBASE_EXCEPTION)),
        OpenScreenRecorderUiModel(listOf(KEYWORD_OPEN_SCREEN_RECORDER)),
        TypographySwitchUiModel(listOf(KEYWORD_TYPOGRAPHY_NEW_FONT)),
        ShowApplinkOnToastUiModel(listOf(KEYWORD_SHOW_APPLINK_ON_TOAST)),
        NetworkLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_NETWORK_LOG_ON_NOTIFICATION)),
        ViewNetworkLogUiModel(listOf(KEYWORD_VIEW_NETWORK_LOG)),
        DeviceIdUiModel(listOf(KEYWORD_DEVICE_ID)),
        ForceDarkModeUiModel(listOf(KEYWORD_FORCE_DARK_MODE)),
        TopAdsLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_TOPADS_LOG_ON_NOTIFICATION)),
        ViewTopAdsLogUiModel(listOf(KEYWORD_VIEW_TOPADS_LOG)),
        ApplinkLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_APPLINK_LOG_ON_NOTIFICATION)),
        ViewApplinkLogUiModel(listOf(KEYWORD_VIEW_APPLINK_LOG)),
        JourneyLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_JOURNEY_LOG_ON_NOTIFICATION)),
        ViewJourneyLogUiModel(listOf(KEYWORD_VIEW_JOURNEY_LOG)),
        FpmLogOnFileUiModel(listOf(KEYWORD_ENABLE_FPM_LOG_ON_FILE)),
        FpmLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_FPM_LOG_ON_NOTIFICATION)),
        ViewFpmLogUiModel(listOf(KEYWORD_VIEW_FPM_LOG)),
        AnalyticsLogOnNotificationUiModel(listOf(KEYWORD_ENABLE_ANALYTICS_LOG_ON_NOTIFICATION)),
        CassavaUiModel(listOf(KEYWORD_CASSAVA)),
        ViewAnalyticsLogUiModel(listOf(KEYWORD_VIEW_ANALYTICS_LOG)),
        ViewIrisLogUiModel(
            listOf(
                KEYWORD_VIEW_IRIS_SAVE_LOG,
                KEYWORD_VIEW_IRIS_SEND_LOG
            )
        ),
        LeakCanaryUiModel(listOf(KEYWORD_ENABLE_LEAK_CANARY)),
        StrictModeLeakPublisherUiModel(listOf(KEYWORD_ENABLE_STRICT_MODE_LEAK_CANARY)),
        RemoteConfigEditorUiModel(listOf(KEYWORD_REMOTE_CONFIG_EDITOR)),
        RouteManagerUiModel(listOf(KEYWORD_ROUTE_MANAGER, KEYWORD_VIEW_APPLINK_LIST)),
        LoggingToServerUiModel(
            listOf(
                KEYWORD_LOGGING_TO_SERVER,
                KEYWORD_SEND_LOG_TO_SERVER,
                KEYWORD_VIEW_SERVER_LOGGER
            )
        ),
        SellerAppReviewDebuggingUiModel(listOf(KEYWORD_ENABLE_SELLER_APP_REVIEW_DEBUGGING)),
        SharedPreferencesEditorUiModel(listOf(KEYWORD_SHARED_PREFERENCES_EDITOR)),
        AppVersionUiModel(listOf(KEYWORD_APP_VERSION)),
        UrlEnvironmentUiModel(
            listOf(
                KEYWORD_CHOOSE_URL_ENVIRONMENT,
                KEYWORD_STAGING,
                KEYWORD_LIVE
            )
        ),
        FakeResponseActivityUiModel(listOf(KEYWORD_FAKE_RESPONSE_ACTIVITY)),
        DataExplorerActivityUiModel(listOf(KEYWORD_DATA_EXPLORER_ACTIVITY)),
        TranslatorUiModel(
            listOf(
                KEYWORD_API_KEY_SETTING,
                KEYWORD_VISIT_BELOW_FOR_API_KEY,
                KEYWORD_LANGUAGE_SETTING,
                KEYWORD_CURRENTLY_SELECTED_LANGUAGES,
                KEYWORD_TOTAL_TRANSLATED_TEXT
            )
        ),
        RequestNewFcmTokenUiModel(listOf(KEYWORD_REQUEST_NEW_FCM_TOKEN)),
        ResetOnBoardingNavigationUiModel(listOf(KEYWORD_RESET_ONBOARDING_NAVIGATION)),
        RollenceAbTestingManualSwitcherUiModel(
            listOf(
                KEYWORD_ROLLENCE_AB_TESTING_MANUAL_SWITCHER,
                KEYWORD_LIST_AB_TEST_ROLLENCE_KEYS
            )
        ),
        HomeAndNavigationRevampSwitcherUiModel(
            listOf(
                KEYWORD_TRANSLATOR,
                KEYWORD_HOME_AND_NAVIGATION_REVAMP_SWITCHER,
                KEYWORD_NEW_NAVIGATION,
                KEYWORD_ALWAYS_OS_EXPERIMENT,
                KEYWORD_OLD_BALANCE_WIDGET,
                KEYWORD_NEW_BALANCE_WIDGET,
                KEYWORD_OLD_INBOX,
                KEYWORD_NEW_INBOX,
                KEYWORD_OLD_CART_CHECKOUT,
                KEYWORD_NEW_CART_CHECKOUT
            )
        ),
        PlayWebSocketSseLoggingUiModel(
            listOf(
                KEYWORD_PLAY_WEB_SOCKET_SSE_LOGGING,
                KEYWORD_VIEW_SSE_LOGGING
            )
        ),
        ConvertResourceIdUiModel(
            listOf(KEYWORD_CONVERT_RESOURCE_ID)
        ),
        ViewHanselPatchUiModel(listOf(KEYWORD_VIEW_HANSEL_PATCH_LIST)),
        TopchatWebSocketLoggingUiModel(listOf(KEYWORD_TOPCHAT_WEB_SOCKET_LOGGING))
    )

    init {
        if (GlobalConfig.isSellerApp()) {
            removeSellerAppItems()
        } else {
            removeMainAppItems()
        }
    }

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

    private fun removeSellerAppItems() {
        removeWidget(AppAuthSecretUiModel::class.java)
    }

    private fun removeMainAppItems() {
        removeWidget(SellerAppReviewDebuggingUiModel::class.java)
        removeWidget(AppAuthSecretUiModel::class.java)
    }

    private fun <T> removeWidget(itemClass: Class<T>) {
        val items = defaultItems
        val widget = getItem(itemClass)
        widget?.let {
            items.remove(it)
        }
    }

    private fun <T> getItem(itemClass: Class<T>): Visitable<*>? {
        return defaultItems.find { it.javaClass == itemClass }
    }
}
