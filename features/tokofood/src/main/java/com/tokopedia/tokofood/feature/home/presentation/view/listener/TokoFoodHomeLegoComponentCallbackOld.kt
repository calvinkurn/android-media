package com.tokopedia.tokofood.feature.home.presentation.view.listener

import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeAnalyticsOld
import com.tokopedia.user.session.UserSessionInterface

class TokoFoodHomeLegoComponentCallbackOld(
    private val view: TokoFoodView,
    private val userSession: UserSessionInterface,
    private val analytics: TokoFoodHomeAnalyticsOld,
) : DynamicLegoBannerListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onClickGridSixImage(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.clickLego(
                userSession.userId,
                destinationId,
                channelModel,
                channelGrid,
                position
            )
        }
        TokofoodRouteManager.routePrioritizeInternal(context, channelGrid.applink)
    }

    override fun onClickGridThreeImage(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.clickBannerWidget(
                userSession.userId,
                destinationId,
                channelModel,
                channelGrid,
                position
            )
        }
        TokofoodRouteManager.routePrioritizeInternal(context, channelGrid.applink)
    }

    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        TokofoodRouteManager.routePrioritizeInternal(context, channelModel.channelHeader.applink)
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        TokofoodRouteManager.routePrioritizeInternal(context, channelModel.channelHeader.applink)
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.impressLego(userSession.userId, destinationId, channelModel)
        }
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.impressLego(userSession.userId, destinationId, channelModel)
        }
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {}

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onClickGridFourImage(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
    }

    override fun onClickGridTwoImage(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {}

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {}
}
