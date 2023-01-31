package com.tokopedia.home.beranda.listener

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import java.util.*

/**
 * @author by errysuprayogi on 11/29/17.
 */

interface HomeCategoryListener {

    val parentPool: RecyclerView.RecycledViewPool

    val isMainViewVisible: Boolean

    val isHomeFragment: Boolean

    val eggListener: HomeEggListener

    fun getTrackingQueueObj(): TrackingQueue?

    val childsFragmentManager: FragmentManager

    val windowHeight: Int

    val homeMainToolbarHeight: Int

    val userId: String

    fun onSectionItemClicked(actionLink: String)

    fun onCloseTicker()

    fun onPromoClick(position: Int, slidesModel: BannerSlidesModel)

    fun openShop()

    fun onOpenPlayChannelList(appLink: String)

    fun sendIrisTrackerHashMap(tracker: HashMap<String, Any>)

    fun onOpenPlayActivity(root: android.view.View, channelId: String?)

    fun actionAppLinkWalletHeader(appLinkBalance: String)

    fun onRequestPendingCashBack()

    fun actionInfoPendingCashBackTokocash(cashBackData: CashBackData, appLinkActionButton: String)

    fun actionTokoPointClicked(appLink: String, tokoPointUrl: String, pageTitle: String)

    fun showNetworkError(message: String)

    fun onDynamicChannelClicked(applink: String)

    fun onRetryMembership(position: Int, headerTitle: String)

    fun onRetryWalletApp(position: Int, headerTitle: String)

    fun onLegoBannerClicked(actionLink: String, trackingAttribution: String)

    fun onPromoScrolled(bannerSlidesModel: BannerSlidesModel)

    fun onPromoAllClick()

    fun onPromoDragStart()

    fun onPromoDragEnd()

    fun setActivityStateListener(activityStateListener: ActivityStateListener)

    fun onPageDragStateChanged(isDragged: Boolean)

    fun onSpotlightItemClicked(actionLink: String)

    fun onTokopointCheckNowClicked(applink: String)

    fun sendEETracking(data: HashMap<String, Any>)

    fun putEEToTrackingQueue(data: HashMap<String, Any>)

    fun putEEToIris(data: HashMap<String, Any>)

    fun getWindowWidth(): Int

    fun refreshHomeData(forceRefresh: Boolean = false)

    fun isShowSeeAllCard(): Boolean

    fun getTabBusinessWidget(position: Int)

    fun getBusinessUnit(tabId: Int, position: Int, tabName: String)

    fun removeViewHolderAtPosition(position: Int)

    fun onDynamicChannelRetryClicked()

    fun getTopAdsBannerNextPage(): String

    fun getDynamicChannelData(visitable: Visitable<*>, channelModel: ChannelModel, channelPosition: Int)

    fun getUserIdFromViewModel(): String

    fun recommendationListOnCloseBuyAgain(id : String, position: Int)

    fun getOneClickCheckoutHomeComponent(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int)

    fun declineRechargeRecommendationItem(requestParams: Map<String, String>)

    fun declineSalamItem(requestParams: Map<String, Int>)

    fun getRechargeBUWidget(source: WidgetSource)

    fun onChooseAddressUpdated()

    fun initializeChooseAddressWidget(chooseAddressWidget: ChooseAddressWidget, needToShowChooseAddress: Boolean)

    fun onChooseAddressServerDown()

    fun needToRotateTokopoints(): Boolean

    fun setRotateTokopointsDone(boolean: Boolean)

    fun onTokonowViewCaptured(view: View?, parentPosition: Int)

    fun dismissTokonowCoachmark(parentPosition: Int)

    fun showBalanceWidgetCoachMark(homeBalanceModel: HomeBalanceModel)

    fun refreshBalanceWidget()

    fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView?)

    fun styleAtf(): String
}
