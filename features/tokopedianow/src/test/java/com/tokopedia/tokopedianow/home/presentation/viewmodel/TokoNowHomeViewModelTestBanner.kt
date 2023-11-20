package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse.Banners
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse.GetHomeBannerV2Response
import com.tokopedia.tokopedianow.data.createHomeLayoutData
import com.tokopedia.tokopedianow.data.createHomeLayoutListForBannerOnly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TokoNowHomeViewModelTestBanner: TokoNowHomeViewModelTestFixture() {

    @Test
    fun `given banner response when getLayoutComponentData should add banner item to visitable list`() {
        val homeBannerResponse = GetHomeBannerV2Response(banners = listOf(
            Banners(
                id = "1",
                title = "Banner 1991",
                imageUrl = "https://www.tokopedia.net/image.png",
                applink = "tokopedia://now",
                url = "https://www.tokopedia.net/someweburl",
                backColor = "#FFFFFF"
            )
        ))

        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())
        onGetHomeBannerUseCase_thenReturn(homeBannerResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(createHomeLayoutData()))

        viewModel.onScroll(1, LocalCacheModel(), listOf())

        val expectedBannerItem = BannerDataModel(
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
        )

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
        verifyGetBannerItem(expectedBannerItem)
    }

    @Test
    fun `given getBanner throws error when getLayoutComponentData should NOT add banner item to visitable list`() {
        val error = NullPointerException()

        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())
        onGetHomeBannerUseCase_thenThrows(error)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(createHomeLayoutData()))

        viewModel.onScroll(1, LocalCacheModel(), listOf())

        val expectedBannerItem = null

        verifyGetBannerItem(expectedBannerItem)
    }
}
