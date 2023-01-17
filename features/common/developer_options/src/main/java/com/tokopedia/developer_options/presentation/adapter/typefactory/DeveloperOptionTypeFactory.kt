package com.tokopedia.developer_options.presentation.adapter.typefactory

import com.tokopedia.developer_options.presentation.model.*

/**
 * @author Said Faisal on 24/11/2021
 *
 * Set type of your UiModel here, so it can be overridden later
 */

interface DeveloperOptionTypeFactory {
    fun type(uiModel: DeveloperOptionsOnNotificationUiModel): Int
    fun type(uiModel: PdpDevUiModel): Int
    fun type(uiModel: AccessTokenUiModel): Int
    fun type(uiModel: SystemNonSystemAppsUiModel): Int
    fun type(uiModel: ResetOnBoardingUiModel): Int
    fun type(uiModel: ForceCrashUiModel): Int
    fun type(uiModel: ForceLogoutUiModel): Int
    fun type(uiModel: SendFirebaseCrashExceptionUiModel): Int
    fun type(uiModel: OpenScreenRecorderUiModel): Int
    fun type(uiModel: NetworkLogOnNotificationUiModel): Int
    fun type(uiModel: ViewNetworkLogUiModel): Int
    fun type(uiModel: DeviceIdUiModel): Int
    fun type(uiModel: ForceDarkModeUiModel): Int
    fun type(uiModel: TopAdsLogOnNotificationUiModel): Int
    fun type(uiModel: ViewTopAdsLogUiModel): Int
    fun type(uiModel: ApplinkLogOnNotificationUiModel): Int
    fun type(uiModel: ViewApplinkLogUiModel): Int
    fun type(uiModel: JourneyLogOnNotificationUiModel): Int
    fun type(uiModel: ViewJourneyLogUiModel): Int
    fun type(uiModel: FpmLogOnFileUiModel): Int
    fun type(uiModel: FpmLogOnNotificationUiModel): Int
    fun type(uiModel: ViewFpmLogUiModel): Int
    fun type(uiModel: AnalyticsLogOnNotificationUiModel): Int
    fun type(uiModel: CassavaUiModel): Int
    fun type(uiModel: ViewAnalyticsLogUiModel): Int
    fun type(uiModel: ViewIrisLogUiModel): Int
    fun type(uiModel: LeakCanaryUiModel): Int
    fun type(uiModel: StrictModeLeakPublisherUiModel): Int
    fun type(uiModel: RemoteConfigEditorUiModel): Int
    fun type(uiModel: RouteManagerUiModel): Int
    fun type(uiModel: LoggingToServerUiModel): Int
    fun type(uiModel: SharedPreferencesEditorUiModel): Int
    fun type(uiModel: AppVersionUiModel): Int
    fun type(uiModel: UrlEnvironmentUiModel): Int
    fun type(uiModel: FakeResponseActivityUiModel): Int
    fun type(uiModel: DataExplorerActivityUiModel): Int
    fun type(uiModel: HomeAndNavigationRevampSwitcherUiModel): Int
    fun type(uiModel: RollenceAbTestingManualSwitcherUiModel): Int
    fun type(uiModel: RequestNewFcmTokenUiModel): Int
    fun type(uiModel: ResetOnBoardingNavigationUiModel): Int
    fun type(uiModel: TranslatorUiModel): Int
    fun type(uiModel: AppAuthSecretUiModel): Int
    fun type(uiModel: SellerAppReviewDebuggingUiModel): Int
    fun type(uiModel: ShowApplinkOnToastUiModel): Int
    fun type(uiModel: PlayWebSocketSseLoggingUiModel): Int
    fun type(uiModel: TypographySwitchUiModel): Int
    fun type(uiModel: ConvertResourceIdUiModel): Int
    fun type(uiModel: ViewHanselPatchUiModel): Int
}
