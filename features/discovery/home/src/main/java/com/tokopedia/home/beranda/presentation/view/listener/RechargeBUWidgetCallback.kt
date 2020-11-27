package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.RechargeBUWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.recharge_component.listener.RechargeBUWidgetListener
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.WidgetSource

class RechargeBUWidgetCallback (val context: Context?, val viewModel: HomeViewModel,
                                val homeCategoryListener: HomeCategoryListener): RechargeBUWidgetListener {

    override fun onRechargeBUWidgetImpression(rechargePerso: RechargePerso, channelModel: ChannelModel) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_IMPRESSION }?.let { tracking ->
            val promotions = rechargePerso.items.mapIndexed { index, item ->
                hashMapOf(
                        "creative" to "${rechargePerso.title} - ${item.title}",
                        "id" to channelModel.id,
                        "name" to "/ - p1 - dynamic channel bu carousel - banner_card - ${rechargePerso.title}",
                        "position" to index
                )
            }
            val promoView = hashMapOf<String, Any>("promoView" to hashMapOf<String, Any>("promotions" to promotions))
            val trackingData = mapOf(
                    "ecommerce" to promoView,
                    "userId" to homeCategoryListener.userId
            )

            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data, trackingData)
            }
        }
    }

    override fun onRechargeBUWidgetBannerImpression(rechargePerso: RechargePerso, channelModel: ChannelModel) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_IMPRESSION }?.let { tracking ->
            val bannerPromotions = listOf(hashMapOf(
                    "creative" to rechargePerso.mediaUrl,
                    "id" to channelModel.id,
                    "name" to "/ - p1 - dynamic channel bu carousel - banner - ${rechargePerso.title}",
                    "position" to 0
            ))
            val bannerPromoView = hashMapOf<String, Any>("promoView" to hashMapOf<String, Any>("promotions" to bannerPromotions))
            val bannerTrackingData = mapOf(
                    "ecommerce" to bannerPromoView,
                    "userId" to homeCategoryListener.userId
            )

            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data, bannerTrackingData)
            }
        }
    }

    override fun onRechargeBUWidgetClickSeeAllButton(rechargePerso: RechargePerso, channelModel: ChannelModel) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_CLICK }?.let { tracking ->
            val trackingData = mapOf(
                    "eventLabel" to "${channelModel.id} - ${rechargePerso.title}",
                    "userId" to homeCategoryListener.userId
            )

            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data, trackingData)
            }
        }

        context?.let {
            RouteManager.route(it, rechargePerso.applink)
        }
    }

    override fun onRechargeBUWidgetClickSeeAllCard(rechargePerso: RechargePerso, channelModel: ChannelModel) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_CLICK }?.let { tracking ->
            val trackingData = mapOf(
                    "eventLabel" to "${channelModel.id} - ${rechargePerso.title}",
                    "userId" to homeCategoryListener.userId
            )

            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data, trackingData)
            }
        }

        context?.let {
            RouteManager.route(it, rechargePerso.applink)
        }
    }

    override fun onRechargeBUWidgetClickBanner(rechargePerso: RechargePerso, channelModel: ChannelModel) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_CLICK }?.let { tracking ->
            val promotions = listOf(hashMapOf(
                    "creative" to rechargePerso.mediaUrl,
                    "id" to channelModel.id,
                    "name" to "/ - p1 - dynamic channel bu carousel - banner - ${rechargePerso.title}",
                    "position" to 0
            ))
            val promoClick = hashMapOf<String, Any>("promoClick" to hashMapOf<String, Any>("promotions" to promotions))
            val trackingData = mapOf(
                    "eventLabel" to "${channelModel.id} - ${rechargePerso.title}",
                    "ecommerce" to promoClick,
                    "userId" to homeCategoryListener.userId
            )

            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data, trackingData)
            }

            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data)
            }
        }

        context?.let {
            RouteManager.route(it, rechargePerso.bannerApplink)
        }
    }

    override fun onRechargeBUWidgetItemClick(rechargePerso: RechargePerso, position: Int, channelModel: ChannelModel) {
        if (position < rechargePerso.items.size) {
            val rechargePersoItem = rechargePerso.items[position]
            rechargePersoItem.tracking.firstOrNull { it.action == ACTION_CLICK }?.let { tracking ->
                val promotions = listOf(hashMapOf(
                        "creative" to "${rechargePerso.title} - ${rechargePersoItem.title}",
                        "id" to channelModel.id,
                        "name" to "/ - p1 - dynamic channel bu carousel - banner_card - ${rechargePerso.title}",
                        "position" to position
                ))
                val promoClick = hashMapOf<String, Any>("promoClick" to hashMapOf<String, Any>("promotions" to promotions))
                val trackingData = mapOf(
                        "eventLabel" to "${channelModel.id} - ${rechargePerso.title}",
                        "ecommerce" to promoClick,
                        "userId" to homeCategoryListener.userId
                )

                homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                    RechargeBUWidgetTracking.sendEvent(trackingQueue, tracking.data, trackingData)
                }
            }

            context?.let {
                RouteManager.route(it, rechargePersoItem.applink)
            }
        }
    }

    override fun getRechargeBUWidget(source: WidgetSource) {
        viewModel.getRechargeBUWidget(source)
    }

    companion object {
        const val ACTION_IMPRESSION = "impression"
        const val ACTION_CLICK = "click"
    }

}