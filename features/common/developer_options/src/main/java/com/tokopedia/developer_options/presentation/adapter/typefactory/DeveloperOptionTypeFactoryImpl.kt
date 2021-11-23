package com.tokopedia.developer_options.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.presentation.model.*
import com.tokopedia.developer_options.presentation.viewholder.*

class DeveloperOptionTypeFactoryImpl(
    private val pdpDevListener: PdpDevViewHolder.PdpDevListener,
    private val accessTokenListener: AccessTokenViewHolder.AccessTokenListener,
    private val systemNonSystemAppsListener: SystemNonSystemAppsViewHolder.SystemNonSystemAppsListener,
    private val resetOnBoardingListener: ResetOnBoardingViewHolder.ResetOnBoardingListener,
    private val forceCrashListener: ForceCrashViewHolder.ForceCrashListener,
    private val sendFirebaseCrashExceptionListener: SendFirebaseCrashExceptionViewHolder.SendFirebaseCrashListener,
    private val openScreenRecorderListener: OpenScreenRecorderViewHolder.OpenScreenRecorderListener,
    private val tickNetworkLogOnNotificationListener: NetworkLogOnNotificationViewHolder.NetworkLogOnNotificationListener,
    private val viewNetworkLogListener: ViewNetworkLogViewHolder.ViewNetworkLogListener
):  BaseAdapterTypeFactory(), DeveloperOptionTypeFactory {

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

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            PdpDevViewHolder.LAYOUT -> PdpDevViewHolder(view, pdpDevListener)
            AccessTokenViewHolder.LAYOUT -> AccessTokenViewHolder(view, accessTokenListener)
            SystemNonSystemAppsViewHolder.LAYOUT -> SystemNonSystemAppsViewHolder(view, systemNonSystemAppsListener)
            ResetOnBoardingViewHolder.LAYOUT -> ResetOnBoardingViewHolder(view, resetOnBoardingListener)
            ForceCrashViewHolder.LAYOUT -> ForceCrashViewHolder(view, forceCrashListener)
            SendFirebaseCrashExceptionViewHolder.LAYOUT -> SendFirebaseCrashExceptionViewHolder(view, sendFirebaseCrashExceptionListener)
            OpenScreenRecorderViewHolder.LAYOUT -> OpenScreenRecorderViewHolder(view, openScreenRecorderListener)
            NetworkLogOnNotificationViewHolder.LAYOUT -> NetworkLogOnNotificationViewHolder(view, tickNetworkLogOnNotificationListener)
            ViewNetworkLogViewHolder.LAYOUT -> ViewNetworkLogViewHolder(view, viewNetworkLogListener)
            DeviceIdViewHolder.LAYOUT -> DeviceIdViewHolder(view)
            ForceDarkModeViewHolder.LAYOUT -> ForceDarkModeViewHolder(view)
            TopAdsLogOnNotificationViewHolder.LAYOUT -> TopAdsLogOnNotificationViewHolder(view)
            ViewTopAdsLogViewHolder.LAYOUT -> ViewTopAdsLogViewHolder(view)
            ApplinkLogOnNotificationViewHolder.LAYOUT -> ApplinkLogOnNotificationViewHolder(view)
            ViewApplinkLogViewHolder.LAYOUT -> ViewApplinkLogViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

}