package com.tokopedia.developer_options.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.presentation.model.*
import com.tokopedia.developer_options.presentation.viewholder.*

/**
 * @author Said Faisal on 24/11/2021
 *
 * Override the type and set your ViewHolder
 */

class DeveloperOptionTypeFactoryImpl(
    private val accessTokenListener: AccessTokenViewHolder.AccessTokenListener,
    private val resetOnBoardingListener: ResetOnBoardingViewHolder.ResetOnBoardingListener,
    private val urlEnvironmentListener: UrlEnvironmentViewHolder.UrlEnvironmentListener,
    private val homeAndNavigationRevampListener: HomeAndNavigationRevampSwitcherViewHolder.HomeAndNavigationRevampListener
):  BaseAdapterTypeFactory(), DeveloperOptionTypeFactory {

    override fun type(uiModel: DeveloperOptionsOnNotificationUiModel): Int = DeveloperOptionsOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: PdpDevUiModel): Int = PdpDevViewHolder.LAYOUT
    override fun type(uiModel: AccessTokenUiModel): Int = AccessTokenViewHolder.LAYOUT
    override fun type(uiModel: SystemNonSystemAppsUiModel): Int = SystemNonSystemAppsViewHolder.LAYOUT
    override fun type(uiModel: ResetOnBoardingUiModel): Int = ResetOnBoardingViewHolder.LAYOUT
    override fun type(uiModel: ForceCrashUiModel): Int = ForceCrashViewHolder.LAYOUT
    override fun type(uiModel: SendFirebaseCrashExceptionUiModel): Int = SendFirebaseCrashExceptionViewHolder.LAYOUT
    override fun type(uiModel: OpenScreenRecorderUiModel): Int = OpenScreenRecorderViewHolder.LAYOUT
    override fun type(uiModel: NetworkLogOnNotificationUiModel): Int = NetworkLogOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: ViewNetworkLogUiModel): Int = ViewNetworkLogViewHolder.LAYOUT
    override fun type(uiModel: DeviceIdUiModel): Int = DeviceIdViewHolder.LAYOUT
    override fun type(uiModel: ForceDarkModeUiModel): Int = ForceDarkModeViewHolder.LAYOUT
    override fun type(uiModel: TopAdsLogOnNotificationUiModel): Int = TopAdsLogOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: ViewTopAdsLogUiModel): Int = ViewTopAdsLogViewHolder.LAYOUT
    override fun type(uiModel: ApplinkLogOnNotificationUiModel): Int = ApplinkLogOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: ViewApplinkLogUiModel): Int = ViewApplinkLogViewHolder.LAYOUT
    override fun type(uiModel: JourneyLogOnNotificationUiModel): Int = JourneyLogOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: ViewJourneyLogUiModel): Int = ViewJourneyLogViewHolder.LAYOUT
    override fun type(uiModel: FpmLogOnFileUiModel): Int = FpmLogOnFileViewHolder.LAYOUT
    override fun type(uiModel: FpmLogOnNotificationUiModel): Int = FpmLogOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: ViewFpmLogUiModel): Int = ViewFpmLogViewHolder.LAYOUT
    override fun type(uiModel: AnalyticsLogOnNotificationUiModel): Int = AnalyticsLogOnNotificationViewHolder.LAYOUT
    override fun type(uiModel: CassavaUiModel): Int = CassavaViewHolder.LAYOUT
    override fun type(uiModel: ViewAnalyticsLogUiModel): Int = ViewAnalyticsLogViewHolder.LAYOUT
    override fun type(uiModel: ViewIrisLogUiModel): Int = ViewIrisLogViewHolder.LAYOUT
    override fun type(uiModel: LeakCanaryUiModel): Int = LeakCanaryViewHolder.LAYOUT
    override fun type(uiModel: StrictModeLeakPublisherUiModel): Int = StrictModeLeakPublisherViewHolder.LAYOUT
    override fun type(uiModel: RemoteConfigEditorUiModel): Int = RemoteConfigEditorViewHolder.LAYOUT
    override fun type(uiModel: RouteManagerUiModel): Int = RouteManagerViewHolder.LAYOUT
    override fun type(uiModel: LoggingToServerUiModel): Int = LoggingToServerViewHolder.LAYOUT
    override fun type(uiModel: SharedPreferencesEditorUiModel): Int = SharedPreferencesEditorViewHolder.LAYOUT
    override fun type(uiModel: AppVersionUiModel): Int = AppVersionViewHolder.LAYOUT
    override fun type(uiModel: UrlEnvironmentUiModel): Int = UrlEnvironmentViewHolder.LAYOUT
    override fun type(uiModel: FakeResponseActivityUiModel): Int = FakeResponseActivityViewHolder.LAYOUT
    override fun type(uiModel: DataExplorerActivityUiModel): Int = DataExplorerActivityViewHolder.LAYOUT
    override fun type(uiModel: HomeAndNavigationRevampSwitcherUiModel): Int = HomeAndNavigationRevampSwitcherViewHolder.LAYOUT
    override fun type(uiModel: RollenceAbTestingManualSwitcherUiModel): Int = RollenceAbTestingManualSwitcherViewHolder.LAYOUT
    override fun type(uiModel: RequestNewFcmTokenUiModel): Int = RequestNewFcmTokenViewHolder.LAYOUT
    override fun type(uiModel: ResetOnBoardingNavigationUiModel): Int = ResetOnBoardingNavigationViewHolder.LAYOUT
    override fun type(uiModel: TranslatorUiModel): Int = TranslatorSettingViewHolder.LAYOUT
    override fun type(uiModel: AppAuthSecretUiModel): Int = AppAuthSecretViewHolder.LAYOUT
    override fun type(uiModel: SellerAppReviewDebuggingUiModel): Int = SellerAppReviewDebuggingViewHolder.LAYOUT
    override fun type(uiModel: ShowApplinkOnToastUiModel): Int = ShowApplinkOnToastViewHolder.LAYOUT
    override fun type(uiModel: PlayWebSocketSseLoggingUiModel): Int = PlayWebSocketSseLoggingViewHolder.LAYOUT
    override fun type(uiModel: TypographySwitchUiModel): Int = TypographySwitcherViewHolder.LAYOUT
    override fun type(uiModel: ConvertResourceIdUiModel): Int = ConvertResourceIdViewHolder.LAYOUT
    override fun type(uiModel: ForceLogoutUiModel): Int = ForceLogoutViewHolder.LAYOUT
    override fun type(uiModel: ViewHanselPatchUiModel): Int = ViewHanselPatchViewHolder.LAYOUT
    override fun type(uiModel: TopchatWebSocketLoggingUiModel): Int = TopchatWebSocketLoggingViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            DeveloperOptionsOnNotificationViewHolder.LAYOUT -> DeveloperOptionsOnNotificationViewHolder(view)
            PdpDevViewHolder.LAYOUT -> PdpDevViewHolder(view)
            AccessTokenViewHolder.LAYOUT -> AccessTokenViewHolder(view, accessTokenListener)
            SystemNonSystemAppsViewHolder.LAYOUT -> SystemNonSystemAppsViewHolder(view)
            ResetOnBoardingViewHolder.LAYOUT -> ResetOnBoardingViewHolder(view, resetOnBoardingListener)
            ForceCrashViewHolder.LAYOUT -> ForceCrashViewHolder(view)
            ForceLogoutViewHolder.LAYOUT -> ForceLogoutViewHolder(view)
            SendFirebaseCrashExceptionViewHolder.LAYOUT -> SendFirebaseCrashExceptionViewHolder(view)
            OpenScreenRecorderViewHolder.LAYOUT -> OpenScreenRecorderViewHolder(view)
            NetworkLogOnNotificationViewHolder.LAYOUT -> NetworkLogOnNotificationViewHolder(view)
            ViewNetworkLogViewHolder.LAYOUT -> ViewNetworkLogViewHolder(view)
            DeviceIdViewHolder.LAYOUT -> DeviceIdViewHolder(view)
            ForceDarkModeViewHolder.LAYOUT -> ForceDarkModeViewHolder(view)
            TopAdsLogOnNotificationViewHolder.LAYOUT -> TopAdsLogOnNotificationViewHolder(view)
            ViewTopAdsLogViewHolder.LAYOUT -> ViewTopAdsLogViewHolder(view)
            ApplinkLogOnNotificationViewHolder.LAYOUT -> ApplinkLogOnNotificationViewHolder(view)
            ViewApplinkLogViewHolder.LAYOUT -> ViewApplinkLogViewHolder(view)
            JourneyLogOnNotificationViewHolder.LAYOUT -> JourneyLogOnNotificationViewHolder(view)
            ViewJourneyLogViewHolder.LAYOUT -> ViewJourneyLogViewHolder(view)
            FpmLogOnFileViewHolder.LAYOUT -> FpmLogOnFileViewHolder(view)
            FpmLogOnNotificationViewHolder.LAYOUT -> FpmLogOnNotificationViewHolder(view)
            ViewFpmLogViewHolder.LAYOUT -> ViewFpmLogViewHolder(view)
            AnalyticsLogOnNotificationViewHolder.LAYOUT -> AnalyticsLogOnNotificationViewHolder(view)
            CassavaViewHolder.LAYOUT -> CassavaViewHolder(view)
            ViewAnalyticsLogViewHolder.LAYOUT -> ViewAnalyticsLogViewHolder(view)
            ViewIrisLogViewHolder.LAYOUT -> ViewIrisLogViewHolder(view)
            LeakCanaryViewHolder.LAYOUT -> LeakCanaryViewHolder(view)
            StrictModeLeakPublisherViewHolder.LAYOUT -> StrictModeLeakPublisherViewHolder(view)
            RemoteConfigEditorViewHolder.LAYOUT -> RemoteConfigEditorViewHolder(view)
            RouteManagerViewHolder.LAYOUT -> RouteManagerViewHolder(view)
            LoggingToServerViewHolder.LAYOUT -> LoggingToServerViewHolder(view)
            SharedPreferencesEditorViewHolder.LAYOUT -> SharedPreferencesEditorViewHolder(view)
            AppVersionViewHolder.LAYOUT -> AppVersionViewHolder(view)
            UrlEnvironmentViewHolder.LAYOUT -> UrlEnvironmentViewHolder(view, urlEnvironmentListener)
            FakeResponseActivityViewHolder.LAYOUT -> FakeResponseActivityViewHolder(view)
            DataExplorerActivityViewHolder.LAYOUT -> DataExplorerActivityViewHolder(view)
            HomeAndNavigationRevampSwitcherViewHolder.LAYOUT -> HomeAndNavigationRevampSwitcherViewHolder(view, homeAndNavigationRevampListener)
            RollenceAbTestingManualSwitcherViewHolder.LAYOUT -> RollenceAbTestingManualSwitcherViewHolder(view)
            RequestNewFcmTokenViewHolder.LAYOUT -> RequestNewFcmTokenViewHolder(view)
            ResetOnBoardingNavigationViewHolder.LAYOUT -> ResetOnBoardingNavigationViewHolder(view)
            TranslatorSettingViewHolder.LAYOUT -> TranslatorSettingViewHolder(view)
            AppAuthSecretViewHolder.LAYOUT -> AppAuthSecretViewHolder(view)
            SellerAppReviewDebuggingViewHolder.LAYOUT -> SellerAppReviewDebuggingViewHolder(view)
            ShowApplinkOnToastViewHolder.LAYOUT -> ShowApplinkOnToastViewHolder(view)
            PlayWebSocketSseLoggingViewHolder.LAYOUT -> PlayWebSocketSseLoggingViewHolder(view)
            TypographySwitcherViewHolder.LAYOUT -> TypographySwitcherViewHolder(view)
            ConvertResourceIdViewHolder.LAYOUT -> ConvertResourceIdViewHolder(view)
            ViewHanselPatchViewHolder.LAYOUT -> ViewHanselPatchViewHolder(view)
            TopchatWebSocketLoggingViewHolder.LAYOUT -> TopchatWebSocketLoggingViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

}
