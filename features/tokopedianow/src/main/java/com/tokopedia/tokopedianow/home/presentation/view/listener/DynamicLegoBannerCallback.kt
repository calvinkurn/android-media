package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class DynamicLegoBannerCallback(
    private val context: Context,
    private val viewModel: TokoNowHomeViewModel
): DynamicLegoBannerListener {

    companion object {
        //To do -> adjust grid param value (waiting for contract)
        private const val WIDGET_PARAM_TOKONOW_REFRESH = "tokonow-refresh"
    }

    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(context,
            if (channelModel.channelHeader.applink.isNotEmpty())
                channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(context,
            if (channelModel.channelHeader.applink.isNotEmpty())
                channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        //To do -> change widget param to grid param (waiting for BE)
        if(channelModel.widgetParam == WIDGET_PARAM_TOKONOW_REFRESH) {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)
            viewModel.switchService(localCacheModel)
        } else {
            RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
        }
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        RouteManager.route(context,
            if (channelGrid.applink.isNotEmpty())
                channelGrid.applink else channelGrid.url)
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {
    }
}