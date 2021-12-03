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

    // Old lego layout event handlers - deprecated - exist for remote config
    fun onClickLegoHeaderActionTextListener(applink: String): View.OnClickListener
    fun onClickLegoImage(channelData: Channel, position: Int): View.OnClickListener
    fun legoImpression(channelData: Channel)

    // Flash Sale layout event handlers
    fun onClickFlashSaleActionText(applink: String, headerId: Long): View.OnClickListener
    fun onClickFlashSaleImage(channelData: Channel, position: Int): View.OnClickListener
    fun flashSaleImpression(channelData: Channel)

    // Thematic layout event handlers
    fun onClickMixActionText(applink: String): View.OnClickListener
    fun onClickMixBanner(channelData: Channel): View.OnClickListener
    fun onClickMixImage(channelData: Channel, position: Int): View.OnClickListener
    fun mixImageImpression(channelData: Channel)
    fun mixBannerImpression(channelData: Channel)

    //flash sale card listener
    fun onFlashSaleCardImpressed(position: Int,grid: Grid, channel: Channel)
    fun onMixFlashSaleSeeAllClicked(channel: Channel, applink: String)
    fun onFlashSaleCardClicked(position: Int, channel: Channel, grid: Grid, applink: String)
    fun onClickMixTopBannerItem(applink: String)
    fun onClickMixTopBannerCtaButton(cta: Cta, channelId: String, applink: String, channelBannerAttribution: String = "")

    fun onClickMixLeftBannerImage(channel: Channel, position: Int)
    fun onMixLeftBannerImpressed(channel: Channel, position: Int)

    //mixleft global component
    fun onClickMixLeftBannerImage(channel: ChannelModel, position: Int)
    fun onMixLeftBannerImpressed(channel: ChannelModel, position: Int)
    fun onFlashSaleCardImpressedComponent(position: Int,grid: ChannelGrid, channel: ChannelModel)
    fun onMixFlashSaleSeeAllClickedComponent(channel: ChannelModel, applink: String)
    fun onFlashSaleCardClickedComponent(position: Int, channel: ChannelModel, grid: ChannelGrid, applink: String)
    fun onSeeAllBannerClickedComponent(channel: ChannelModel, applink: String)

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
