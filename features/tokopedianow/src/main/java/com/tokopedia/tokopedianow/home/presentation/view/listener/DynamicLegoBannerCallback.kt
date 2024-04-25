package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics

class DynamicLegoBannerCallback(
    private val view: TokoNowView,
    private val analytics: HomeAnalytics
) : DynamicLegoBannerListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(
            context,
            if (channelModel.channelHeader.applink.isNotEmpty()) {
                channelModel.channelHeader.applink
            } else {
                channelModel.channelHeader.url
            }
        )
        trackClickLego6ViewAll(channelModel, position)
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(
            context,
            if (channelModel.channelHeader.applink.isNotEmpty()) {
                channelModel.channelHeader.applink
            } else {
                channelModel.channelHeader.url
            }
        )
    }

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        trackClickLego6Banner(channelModel, channelGrid, position, parentPosition)
        onClickLego6Banner(channelGrid)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        RouteManager.route(
            context,
            channelGrid.applink.ifEmpty {
                channelGrid.url
            }
        )
        trackClickLego4Banner(channelModel, channelGrid, position, parentPosition)
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        RouteManager.route(
            context,
            if (channelGrid.applink.isNotEmpty()) {
                channelGrid.applink
            } else {
                channelGrid.url
            }
        )
        analytics.trackClickLego3Banner(position, channelModel, channelGrid)
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {
        analytics.trackImpressionLego3Banner(channelModel)
    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        context?.let {
            val warehouseId = ChooseAddressUtils.getLocalizingAddressData(it).warehouse_id
            analytics.trackImpressionLego6Banner(channelModel, warehouseId, parentPosition)
        }
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        analytics.trackImpressionLego4Banner(channelModel)
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        analytics.trackImpressionLego3Banner(channelModel)
    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {
    }

    private fun onRedirectPage(channelGrid: ChannelGrid) {
        val appLink = if (channelGrid.applink.isNotEmpty()) {
            channelGrid.applink
        } else {
            channelGrid.url
        }
        RouteManager.route(context, appLink)
    }

    private fun trackClickLego4Banner(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        context?.let {
            val warehouseId = ChooseAddressUtils.getLocalizingAddressData(it).warehouse_id
            analytics.trackClickLego4Banner(channelModel, channelGrid, warehouseId, position, parentPosition)
        }
    }

    private fun trackClickLego6Banner(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        context?.let {
            val warehouseId = ChooseAddressUtils.getLocalizingAddressData(it).warehouse_id
            analytics.trackClickLego6Banner(channelModel, channelGrid, warehouseId, position, parentPosition)
        }
    }

    private fun trackClickLego6ViewAll(channelModel: ChannelModel, position: Int) {
        context?.let {
            val warehouseId = ChooseAddressUtils.getLocalizingAddressData(it).warehouse_id
            analytics.trackClickLego6ViewAll(channelModel, warehouseId, position)
        }
    }

    private fun onClickLego6Banner(channelGrid: ChannelGrid) {
        onRedirectPage(channelGrid)
    }
}
