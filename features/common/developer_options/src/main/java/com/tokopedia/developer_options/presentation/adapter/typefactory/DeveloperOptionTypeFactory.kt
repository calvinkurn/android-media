package com.tokopedia.developer_options.presentation.adapter.typefactory

import com.tokopedia.developer_options.presentation.model.AccessTokenUiModel
import com.tokopedia.developer_options.presentation.model.AnalyticsLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.AppVersionUiModel
import com.tokopedia.developer_options.presentation.model.ApplinkLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.BannerEnvironmentUiModel
import com.tokopedia.developer_options.presentation.model.BranchLinkUiModel
import com.tokopedia.developer_options.presentation.model.CassavaUiModel
import com.tokopedia.developer_options.presentation.model.ConvertResourceIdUiModel
import com.tokopedia.developer_options.presentation.model.DataExplorerActivityUiModel
import com.tokopedia.developer_options.presentation.model.DeprecatedApiSwitcherToasterUiModel
import com.tokopedia.developer_options.presentation.model.DevOptsAuthorizationUiModel
import com.tokopedia.developer_options.presentation.model.DeveloperOptionsOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.DeviceIdUiModel
import com.tokopedia.developer_options.presentation.model.FakeResponseActivityUiModel
import com.tokopedia.developer_options.presentation.model.ForceCrashUiModel
import com.tokopedia.developer_options.presentation.model.ForceDarkModeUiModel
import com.tokopedia.developer_options.presentation.model.ForceLogoutUiModel
import com.tokopedia.developer_options.presentation.model.FpiMonitoringUiModel
import com.tokopedia.developer_options.presentation.model.FpmLogOnFileUiModel
import com.tokopedia.developer_options.presentation.model.FpmLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.HomeAndNavigationRevampSwitcherUiModel
import com.tokopedia.developer_options.presentation.model.JourneyLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.LeakCanaryUiModel
import com.tokopedia.developer_options.presentation.model.LoggingToServerUiModel
import com.tokopedia.developer_options.presentation.model.LoginHelperUiModel
import com.tokopedia.developer_options.presentation.model.NetworkLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.OpenScreenRecorderUiModel
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.developer_options.presentation.model.PlayWebSocketSseLoggingUiModel
import com.tokopedia.developer_options.presentation.model.RemoteConfigEditorUiModel
import com.tokopedia.developer_options.presentation.model.RequestNewFcmTokenUiModel
import com.tokopedia.developer_options.presentation.model.ResetOnBoardingNavigationUiModel
import com.tokopedia.developer_options.presentation.model.ResetOnBoardingUiModel
import com.tokopedia.developer_options.presentation.model.RollenceAbTestingManualSwitcherUiModel
import com.tokopedia.developer_options.presentation.model.RouteManagerUiModel
import com.tokopedia.developer_options.presentation.model.SellerAppReviewDebuggingUiModel
import com.tokopedia.developer_options.presentation.model.SendFirebaseCrashExceptionUiModel
import com.tokopedia.developer_options.presentation.model.SharedPreferencesEditorUiModel
import com.tokopedia.developer_options.presentation.model.ShowApplinkOnToastUiModel
import com.tokopedia.developer_options.presentation.model.StrictModeLeakPublisherUiModel
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel
import com.tokopedia.developer_options.presentation.model.TopAdsLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.TopchatWebSocketLoggingUiModel
import com.tokopedia.developer_options.presentation.model.TranslatorUiModel
import com.tokopedia.developer_options.presentation.model.TypographySwitchUiModel
import com.tokopedia.developer_options.presentation.model.UrlEnvironmentUiModel
import com.tokopedia.developer_options.presentation.model.ViewAnalyticsLogUiModel
import com.tokopedia.developer_options.presentation.model.ViewApplinkLogUiModel
import com.tokopedia.developer_options.presentation.model.ViewFpmLogUiModel
import com.tokopedia.developer_options.presentation.model.ViewHanselPatchUiModel
import com.tokopedia.developer_options.presentation.model.ViewIrisLogUiModel
import com.tokopedia.developer_options.presentation.model.ViewJourneyLogUiModel
import com.tokopedia.developer_options.presentation.model.ViewNetworkLogUiModel
import com.tokopedia.developer_options.presentation.model.ViewTopAdsLogUiModel
import com.tokopedia.developer_options.presentation.model.UserIdUiModel
import com.tokopedia.developer_options.presentation.model.ShopIdUiModel
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
    fun type(uiModel: SellerAppReviewDebuggingUiModel): Int
    fun type(uiModel: ShowApplinkOnToastUiModel): Int
    fun type(uiModel: PlayWebSocketSseLoggingUiModel): Int
    fun type(uiModel: TypographySwitchUiModel): Int
    fun type(uiModel: ConvertResourceIdUiModel): Int
    fun type(uiModel: ViewHanselPatchUiModel): Int
    fun type(uiModel: TopchatWebSocketLoggingUiModel): Int
    fun type(uiModel: LoginHelperUiModel): Int
    fun type(uiModel: DevOptsAuthorizationUiModel): Int
    fun type(uiModel: DeprecatedApiSwitcherToasterUiModel): Int
    fun type(uiModel: BannerEnvironmentUiModel): Int
    fun type(uiModel: BranchLinkUiModel): Int
    fun type(uiModel: FpiMonitoringUiModel): Int
    fun type(uiModel: UserIdUiModel): Int
    fun type(uiModel: ShopIdUiModel): Int
}
