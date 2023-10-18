package com.tokopedia.developer_options.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
import com.tokopedia.developer_options.presentation.model.ShopIdUiModel
import com.tokopedia.developer_options.presentation.model.UserIdUiModel
import com.tokopedia.developer_options.presentation.viewholder.AccessTokenViewHolder
import com.tokopedia.developer_options.presentation.viewholder.AnalyticsLogOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.AppVersionViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ApplinkLogOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.BannerEnvironmentViewHolder
import com.tokopedia.developer_options.presentation.viewholder.BranchLinkViewHolder
import com.tokopedia.developer_options.presentation.viewholder.CassavaViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ConvertResourceIdViewHolder
import com.tokopedia.developer_options.presentation.viewholder.DataExplorerActivityViewHolder
import com.tokopedia.developer_options.presentation.viewholder.DeprecatedAPISwitcherToasterViewHolder
import com.tokopedia.developer_options.presentation.viewholder.DevOptsAuthorizationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.DeveloperOptionsOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.DeviceIdViewHolder
import com.tokopedia.developer_options.presentation.viewholder.EnableFpiMonitoringViewHolder
import com.tokopedia.developer_options.presentation.viewholder.FakeResponseActivityViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ForceCrashViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ForceDarkModeViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ForceLogoutViewHolder
import com.tokopedia.developer_options.presentation.viewholder.FpmLogOnFileViewHolder
import com.tokopedia.developer_options.presentation.viewholder.FpmLogOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.HomeAndNavigationRevampSwitcherViewHolder
import com.tokopedia.developer_options.presentation.viewholder.JourneyLogOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.LeakCanaryViewHolder
import com.tokopedia.developer_options.presentation.viewholder.LoggingToServerViewHolder
import com.tokopedia.developer_options.presentation.viewholder.LoginHelperListener
import com.tokopedia.developer_options.presentation.viewholder.LoginHelperViewHolder
import com.tokopedia.developer_options.presentation.viewholder.NetworkLogOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.OpenScreenRecorderViewHolder
import com.tokopedia.developer_options.presentation.viewholder.PdpDevViewHolder
import com.tokopedia.developer_options.presentation.viewholder.PlayWebSocketSseLoggingViewHolder
import com.tokopedia.developer_options.presentation.viewholder.RemoteConfigEditorViewHolder
import com.tokopedia.developer_options.presentation.viewholder.RequestNewFcmTokenViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ResetOnBoardingNavigationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ResetOnBoardingViewHolder
import com.tokopedia.developer_options.presentation.viewholder.RollenceAbTestingManualSwitcherViewHolder
import com.tokopedia.developer_options.presentation.viewholder.RouteManagerViewHolder
import com.tokopedia.developer_options.presentation.viewholder.SellerAppReviewDebuggingViewHolder
import com.tokopedia.developer_options.presentation.viewholder.SendFirebaseCrashExceptionViewHolder
import com.tokopedia.developer_options.presentation.viewholder.SharedPreferencesEditorViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ShowApplinkOnToastViewHolder
import com.tokopedia.developer_options.presentation.viewholder.StrictModeLeakPublisherViewHolder
import com.tokopedia.developer_options.presentation.viewholder.SystemNonSystemAppsViewHolder
import com.tokopedia.developer_options.presentation.viewholder.TopAdsLogOnNotificationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.TopchatWebSocketLoggingViewHolder
import com.tokopedia.developer_options.presentation.viewholder.TranslatorSettingViewHolder
import com.tokopedia.developer_options.presentation.viewholder.TypographySwitcherViewHolder
import com.tokopedia.developer_options.presentation.viewholder.UrlEnvironmentViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewAnalyticsLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewApplinkLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewFpmLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewHanselPatchViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewIrisLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewJourneyLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewNetworkLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ViewTopAdsLogViewHolder
import com.tokopedia.developer_options.presentation.viewholder.UserIdViewHolder
import com.tokopedia.developer_options.presentation.viewholder.ShopIdViewHolder

/**
 * @author Said Faisal on 24/11/2021
 *
 * Override the type and set your ViewHolder
 */

class DeveloperOptionTypeFactoryImpl(
    private val accessTokenListener: AccessTokenViewHolder.AccessTokenListener,
    private val resetOnBoardingListener: ResetOnBoardingViewHolder.ResetOnBoardingListener,
    private val urlEnvironmentListener: UrlEnvironmentViewHolder.UrlEnvironmentListener,
    private val homeAndNavigationRevampListener: HomeAndNavigationRevampSwitcherViewHolder.HomeAndNavigationRevampListener,
    private val loginHelperListener: LoginHelperListener,
    private val authorizeListener: DevOptsAuthorizationViewHolder.DevOptsAuthorizationListener,
    private val branchListener: BranchLinkViewHolder.BranchListener,
    private val userIdListener: UserIdViewHolder.UserIdListener,
    private val shopIdListener: ShopIdViewHolder.ShopIdListener
) : BaseAdapterTypeFactory(), DeveloperOptionTypeFactory {

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
    override fun type(uiModel: SellerAppReviewDebuggingUiModel): Int = SellerAppReviewDebuggingViewHolder.LAYOUT
    override fun type(uiModel: ShowApplinkOnToastUiModel): Int = ShowApplinkOnToastViewHolder.LAYOUT
    override fun type(uiModel: PlayWebSocketSseLoggingUiModel): Int = PlayWebSocketSseLoggingViewHolder.LAYOUT
    override fun type(uiModel: TypographySwitchUiModel): Int = TypographySwitcherViewHolder.LAYOUT
    override fun type(uiModel: ConvertResourceIdUiModel): Int = ConvertResourceIdViewHolder.LAYOUT
    override fun type(uiModel: ForceLogoutUiModel): Int = ForceLogoutViewHolder.LAYOUT
    override fun type(uiModel: ViewHanselPatchUiModel): Int = ViewHanselPatchViewHolder.LAYOUT
    override fun type(uiModel: TopchatWebSocketLoggingUiModel): Int = TopchatWebSocketLoggingViewHolder.LAYOUT
    override fun type(uiModel: LoginHelperUiModel): Int = LoginHelperViewHolder.LAYOUT
    override fun type(uiModel: DevOptsAuthorizationUiModel): Int = DevOptsAuthorizationViewHolder.LAYOUT
    override fun type(uiModel: DeprecatedApiSwitcherToasterUiModel): Int = DeprecatedAPISwitcherToasterViewHolder.LAYOUT
    override fun type(uiModel: BranchLinkUiModel): Int = BranchLinkViewHolder.LAYOUT
    override fun type(uiModel: FpiMonitoringUiModel): Int = EnableFpiMonitoringViewHolder.LAYOUT
    override fun type(uiModel: BannerEnvironmentUiModel): Int = BannerEnvironmentViewHolder.LAYOUT

    override fun type(uiModel: UserIdUiModel): Int = UserIdViewHolder.LAYOUT
    override fun type(uiModel: ShopIdUiModel): Int = ShopIdViewHolder.LAYOUT
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
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
            SellerAppReviewDebuggingViewHolder.LAYOUT -> SellerAppReviewDebuggingViewHolder(view)
            ShowApplinkOnToastViewHolder.LAYOUT -> ShowApplinkOnToastViewHolder(view)
            PlayWebSocketSseLoggingViewHolder.LAYOUT -> PlayWebSocketSseLoggingViewHolder(view)
            TypographySwitcherViewHolder.LAYOUT -> TypographySwitcherViewHolder(view)
            ConvertResourceIdViewHolder.LAYOUT -> ConvertResourceIdViewHolder(view)
            ViewHanselPatchViewHolder.LAYOUT -> ViewHanselPatchViewHolder(view)
            TopchatWebSocketLoggingViewHolder.LAYOUT -> TopchatWebSocketLoggingViewHolder(view)
            LoginHelperViewHolder.LAYOUT -> LoginHelperViewHolder(view, loginHelperListener)
            DevOptsAuthorizationViewHolder.LAYOUT -> DevOptsAuthorizationViewHolder(view, authorizeListener)
            DeprecatedAPISwitcherToasterViewHolder.LAYOUT -> DeprecatedAPISwitcherToasterViewHolder(view)
            BranchLinkViewHolder.LAYOUT -> BranchLinkViewHolder(branchListener, view)
            EnableFpiMonitoringViewHolder.LAYOUT -> EnableFpiMonitoringViewHolder(view)
            UserIdViewHolder.LAYOUT -> UserIdViewHolder(view, userIdListener)
            ShopIdViewHolder.LAYOUT -> ShopIdViewHolder(view, shopIdListener)
            BannerEnvironmentViewHolder.LAYOUT -> BannerEnvironmentViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
