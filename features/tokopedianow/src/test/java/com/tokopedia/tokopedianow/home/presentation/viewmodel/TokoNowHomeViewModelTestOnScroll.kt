package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.data.createHomeLayoutData
import com.tokopedia.tokopedianow.data.createHomeLayoutListForBannerOnly
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestOnScroll : TokoNowHomeViewModelTestFixture() {

    @Test
    fun `given user scroll home page when load more should add all banner to home layout list`() {
        val firstBanner = HomeLayoutResponse(
            id = "2222",
            layout = "banner_carousel_v2",
            header = Header(
                name = "Banner Tokonow",
                serverTimeUnix = 0
            ),
            token = "==advdf299c" // dummy token
        )

        val secondBanner = HomeLayoutResponse(
            id = "3333",
            layout = "banner_carousel_v2",
            header = Header(
                name = "Banner Tokonow",
                serverTimeUnix = 0
            )
        )

        val homeLayoutResponse = listOf(firstBanner)
        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(secondBanner))

        viewModel.onScroll(1, LocalCacheModel(), listOf(), true)

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            BannerDataModel(
                channelModel = ChannelModel(
                    id = "2222",
                    groupId = "",
                    style = ChannelStyle.ChannelHome,
                    channelHeader = ChannelHeader(name = "Banner Tokonow"),
                    channelConfig = ChannelConfig(layout = "banner_carousel_v2"),
                    layout = "banner_carousel_v2"
                )
            ),
            BannerDataModel(
                channelModel = ChannelModel(
                    id = "3333",
                    groupId = "",
                    style = ChannelStyle.ChannelHome,
                    channelHeader = ChannelHeader(name = "Banner Tokonow"),
                    channelConfig = ChannelConfig(layout = "banner_carousel_v2"),
                    layout = "banner_carousel_v2"
                )
            )
        )

        val expected = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.LOAD_MORE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)

        viewModel.homeLayoutList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `given index is NOT between visible item index when onScroll should use case once`() {
        val index = 1

        val homeLayoutResponse = listOf(
            createHomeLayoutData(),
            createHomeLayoutData(),
            createHomeLayoutData()
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(index, LocalCacheModel(), listOf(), true)

        verifyGetHomeLayoutDataUseCaseCalled(times = 1)
    }

    @Test
    fun `given load more token is empty when scroll home page should call use case twice`() {
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "12345",
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                ),
                token = "==abcd" // dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val loadMoreLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "12346",
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                ),
                token = "" // dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(loadMoreLayoutResponse)

        viewModel.onScroll(1, LocalCacheModel(), listOf(), true)
        viewModel.onScroll(0, LocalCacheModel(), listOf(), true)
        viewModel.onScroll(0, LocalCacheModel(), listOf(), true)

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `given home contains progress bar when onScroll should call use case once`() {
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "34923",
                layout = "lego_3_image",
                header = Header(
                    name = "Lego Banner",
                    serverTimeUnix = 0
                ),
                token = "==sfvf" // dummy token
            ),
            HomeLayoutResponse(
                id = "11111",
                layout = "category_tokonow",
                header = Header(
                    name = "Category Tokonow",
                    serverTimeUnix = 0
                )
            ),
            HomeLayoutResponse(
                id = "2222",
                layout = "banner_carousel_v2",
                header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.onScroll(4, LocalCacheModel(), listOf(), true)

        verifyGetHomeLayoutDataUseCaseCalled(times = 1)
    }

    @Test
    fun `when scroll home error should do nothing`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(MessageErrorException())

        viewModel.onScroll(1, LocalCacheModel(), listOf(), true)

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }
}
