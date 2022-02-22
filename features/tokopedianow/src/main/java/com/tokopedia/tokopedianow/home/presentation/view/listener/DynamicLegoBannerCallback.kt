package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.constant.RequestCode.REQUEST_CODE_LOGIN
import com.tokopedia.tokopedianow.common.util.TokoNowSwitcherUtil.switchService
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class DynamicLegoBannerCallback(
    private val view: TokoNowView,
    private val viewModel: TokoNowHomeViewModel,
    private val userSession: UserSessionInterface
    ): DynamicLegoBannerListener {

    private val context by lazy { view.getFragmentPage().context }

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
        context?.let {
            switchService(
                context = it,
                param = channelGrid.param,
                onRefreshPage = { localCacheModel ->
                    onRefreshPage(localCacheModel)
                },
                onRedirectPage = {
                    onRedirectPage(channelGrid)
                }
            )
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

    private fun onRedirectPage(channelGrid: ChannelGrid) {
        val appLink = if (channelGrid.applink.isNotEmpty()) {
            channelGrid.applink
        } else {
            channelGrid.url
        }
        RouteManager.route(context, appLink)
    }

    private fun onRefreshPage(localCacheModel: LocalCacheModel) {
        if (userSession.isLoggedIn) {
            viewModel.switchService(localCacheModel)
        } else {
            openLoginPage()
        }
    }

    private fun openLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        view.getFragmentPage().startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }
}