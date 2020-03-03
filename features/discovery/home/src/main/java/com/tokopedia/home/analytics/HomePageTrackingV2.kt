package com.tokopedia.home.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

object HomePageTrackingV2 : BaseTracking() {
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    object LegoBanner{
        private const val LEGO_BANNER_4_IMAGE_NAME = "lego banner 4 image"

        fun getLegoBannerFourImageImpression(channel: DynamicHomeChannel.Channels, position: Int, isToIris: Boolean = false) = getBasicPromotionView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.grids.mapIndexed { index, grid ->
                    Promotion(
                            id = "%s_%s".format(channel.id, grid.id),
                            creative = grid.attribution,
                            creativeUrl = grid.imageUrl,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.header.name),
                            position = (index + 1).toString()
                    )
                }
        )
        fun getLegoBannerFourImageClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = grid.attribution,
                channelId = channel.id,
                categoryId = channel.categoryPersona,
                affinity = channel.persona,
                attribution = channel.galaxyAttribution,
                shopId = channel.brandId,
                promotions = channel.grids.map {
                    Promotion(
                            id = "%s_%s".format(channel.id, it.id),
                            creative = it.attribution,
                            creativeUrl = it.imageUrl,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.header.name),
                            position = position.toString()
                    )
                }
        )

        fun getLegoBannerFourImageSeeAllClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME) + " view all",
                Label.KEY, channel.header.name,
                Label.CHANNEL_LABEL, channel.id
            ) as HashMap<String, Any>
        }
    }
}
