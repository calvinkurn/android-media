package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse.Banners
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse.GetHomeBannerV2Response
import com.tokopedia.tokopedianow.data.createHomeLayoutData
import com.tokopedia.tokopedianow.data.createHomeLayoutListForBannerOnly
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.mapper.HomeHeaderMapper.createHomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestOnScroll : TokoNowHomeViewModelTestFixture() {

    @Test
    fun `given user scroll home page when load more should add all banner to home layout list`() {
        runTest {
            val firstBanner = HomeLayoutResponse(
                id = "2222",
                layout = "tokonow_banner",
                header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
                ),
                token = "==advdf299c" // dummy token
            )

            val secondBanner = HomeLayoutResponse(
                id = "3333",
                layout = "tokonow_banner",
                header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
                )
            )

            val getHomeBannerV2Response = GetHomeBannerV2Response(
                banners = listOf(
                    Banners(
                        id = "1",
                        title = "Banner 1991",
                        imageUrl = "https://www.tokopedia.net/image.png",
                        applink = "tokopedia://now",
                        url = "https://www.tokopedia.net/someweburl",
                        backColor = "#FFFFFF"
                    )
                )
            )

            val homeLayoutResponse = listOf(firstBanner)
            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetHomeBannerUseCase_thenReturn(getHomeBannerV2Response)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetHomeLayoutData_thenReturn(listOf(secondBanner))

            viewModel.onScroll(1, LocalCacheModel(), listOf())

            val layoutList = listOf(
                createHomeHeaderUiModel(),
                BannerDataModel(
                    channelModel = ChannelModel(
                        id = "2222",
                        groupId = "",
                        style = ChannelStyle.ChannelHome,
                        channelHeader = ChannelHeader(name = "Banner Tokonow"),
                        channelConfig = ChannelConfig(layout = "tokonow_banner"),
                        layout = "tokonow_banner",
                        channelGrids = listOf(
                            ChannelGrid(
                                id = "1",
                                attribution = "Banner 1991",
                                imageUrl = "https://www.tokopedia.net/image.png",
                                applink = "tokopedia://now",
                                url = "https://www.tokopedia.net/someweburl",
                                backColor = "#FFFFFF"
                            )
                        )
                    )
                ),
                BannerDataModel(
                    channelModel = ChannelModel(
                        id = "3333",
                        groupId = "",
                        style = ChannelStyle.ChannelHome,
                        channelHeader = ChannelHeader(name = "Banner Tokonow"),
                        channelConfig = ChannelConfig(layout = "tokonow_banner"),
                        layout = "tokonow_banner",
                        channelGrids = listOf(
                            ChannelGrid(
                                id = "1",
                                attribution = "Banner 1991",
                                imageUrl = "https://www.tokopedia.net/image.png",
                                applink = "tokopedia://now",
                                url = "https://www.tokopedia.net/someweburl",
                                backColor = "#FFFFFF"
                            )
                        )
                    )
                )
            )

            val expected = Success(
                HomeLayoutListUiModel(
                    items = layoutList,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled(times = 2)

            viewModel.homeLayoutList
                .verifySuccessEquals(expected)
        }
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
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(index, LocalCacheModel(), listOf())

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
            removeAbleWidgets = listOf()
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

        viewModel.onScroll(1, LocalCacheModel(), listOf())
        viewModel.onScroll(0, LocalCacheModel(), listOf())
        viewModel.onScroll(0, LocalCacheModel(), listOf())

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
                layout = "tokonow_banner",
                header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.onScroll(4, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 1)
    }

    @Test
    fun `when scroll home error should do nothing`() {
        val getHomeBannerV2Response = GetHomeBannerV2Response(
            banners = listOf(
                Banners(
                    id = "1",
                    title = "Banner 1991",
                    imageUrl = "https://www.tokopedia.net/image.png",
                    applink = "tokopedia://now",
                    url = "https://www.tokopedia.net/someweburl",
                    backColor = "#FFFFFF"
                )
            )
        )
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())
        onGetHomeBannerUseCase_thenReturn(getHomeBannerV2Response)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(MessageErrorException())

        viewModel.onScroll(1, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }
}
