package com.tokopedia.home.beranda.listener

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.util.JankyFrameMonitoringUtil

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

import java.util.HashMap

/**
 * @author by errysuprayogi on 11/29/17.
 */

interface HomeCategoryListener {

    val parentPool: RecyclerView.RecycledViewPool

    val isMainViewVisible: Boolean

    val isHomeFragment: Boolean

    val eggListener: HomeEggListener

    val trackingQueue: TrackingQueue

    val childFragmentManager: FragmentManager

    val windowHeight: Int

    val homeMainToolbarHeight: Int

    fun onSectionItemClicked(actionLink: String)

    fun onCloseTicker()

    fun onPromoClick(position: Int, slidesModel: BannerSlidesModel)

    fun openShop()

    fun onOpenPlayActivity(root: android.view.View, channelId: String?)

    fun actionAppLinkWalletHeader(appLinkBalance: String)

    fun onRequestPendingCashBack()

    fun actionInfoPendingCashBackTokocash(cashBackData: CashBackData, appLinkActionButton: String)

    fun actionTokoPointClicked(appLink: String, tokoPointUrl: String, pageTitle: String)

    fun showNetworkError(message: String)

    fun onDynamicChannelClicked(applink: String)

    fun onRefreshTokoPointButtonClicked()

    fun onRefreshTokoCashButtonClicked()

    fun onLegoBannerClicked(actionLink: String, trackingAttribution: String)

    fun onPromoScrolled(bannerSlidesModel: BannerSlidesModel)

    fun onPromoAllClick()

    fun onPromoDragStart()

    fun onPromoDragEnd()

    fun setActivityStateListener(activityStateListener: ActivityStateListener)

    fun onPageDragStateChanged(isDragged: Boolean)

    fun onSpotlightItemClicked(actionLink: String)

    fun onTokopointCheckNowClicked(applink: String)

    fun launchPermissionChecker()

    fun onCloseGeolocationView()

    fun sendEETracking(data: HashMap<String, Any>)

    fun putEEToTrackingQueue(data: HashMap<String, Any>)

    fun putEEToIris(data: HashMap<String, Any>)

    fun getWindowWidth(): Int

    fun refreshHomeData()

    fun getHomeJankyFramesUtil(): JankyFrameMonitoringUtil?

    fun getTabBusinessWidget(position: Int)

    fun getBusinessUnit(tabId: Int, position: Int)

    fun getPlayChannel(position: Int)
}
