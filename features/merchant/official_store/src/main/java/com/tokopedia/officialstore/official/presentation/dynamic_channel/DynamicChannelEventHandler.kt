package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import com.tokopedia.officialstore.official.presentation.widget.CountDownView
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface DynamicChannelEventHandler : CountDownView.CountDownListener {
    // Lego layout event handlers
    fun onClickLegoHeaderActionText(applink: String)
    fun onClickLegoImage(channelModel: ChannelModel, position: Int)
    fun legoImpression(channelModel: ChannelModel)

    // Flash Sale layout event handlers
    fun onClickFlashSaleActionText(applink: String, channelId: String, headerName: String): View.OnClickListener
    fun onClickFlashSaleImage(channelData: Channel, position: Int): View.OnClickListener
    fun flashSaleImpression(channelData: Channel)

    //mix top & mix left
    fun onClickMixTopBannerItem(applink: String)
    fun onClickMixTopBannerCtaButton(cta: Cta, channelId: String, applink: String, headerName: String, channelBannerAttribution: String = "")
    fun onClickMixLeftBannerImage(channel: ChannelModel, position: Int)
    fun onMixLeftBannerImpressed(channel: ChannelModel, position: Int)
    fun onProductCardImpressed(position: Int, grid: ChannelGrid, channel: ChannelModel)
    fun onProductCardClicked(position: Int, channel: ChannelModel, grid: ChannelGrid, applink: String)
    fun onCarouselSeeAllCardClicked(channel: ChannelModel, applink: String)
    fun onCarouselSeeAllHeaderClicked(channel: ChannelModel, applink: String)

    //featured shop OS
    fun onFeaturedShopDCClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onFeaturedShopDCImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onSeeAllFeaturedShopDCClicked(channel: ChannelModel, position: Int, applink: String)
    fun goToApplink(applink: String)
    
    //recommendation widget
    fun onBestSellerClick(appLink: String)
    fun onBestSellerThreeDotsClick(recommendationItem: RecommendationItem, widgetPosition: Int)
    fun onBestSellerSeeMoreTextClick(appLink: String)
    fun onBestSellerSeeAllCardClick(appLink: String)

    fun getOSCategory(): Category?
    fun isLogin(): Boolean
    fun getUserId(): String
    fun getTrackingObject(): OfficialStoreTracking?

}
