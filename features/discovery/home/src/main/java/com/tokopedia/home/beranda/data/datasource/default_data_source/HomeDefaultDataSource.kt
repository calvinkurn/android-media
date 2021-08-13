package com.tokopedia.home.beranda.data.datasource.default_data_source

import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel

class HomeDefaultDataSource {
    fun getDefaultHomeData(): HomeData = HomeData(
            dynamicHomeChannel = createDefaultHomeDynamicChannel(),
            banner = createDefaultHomePageBanner(),
            dynamicHomeIcon = createDefaultHomeDynamicIcon(),
            homeFlag = createHomeFlag()
    )

    fun createDefaultHomePageBanner(): BannerDataModel{
        val defaultSlides = mutableListOf<BannerSlidesModel>()
        val defaultBannerSlidesModel1 = BannerSlidesModel()
        defaultBannerSlidesModel1.applink = DEFAULT_BANNER_APPLINK_1
        defaultBannerSlidesModel1.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
        defaultBannerSlidesModel1.imageUrl = DEFAULT_BANNER_IMAGE_URL_1

        val defaultBannerSlidesModel2 = BannerSlidesModel()
        defaultBannerSlidesModel2.applink = DEFAULT_BANNER_APPLINK_2
        defaultBannerSlidesModel2.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
        defaultBannerSlidesModel2.imageUrl = DEFAULT_BANNER_IMAGE_URL_2

        val defaultBannerSlidesModel3 = BannerSlidesModel()
        defaultBannerSlidesModel3.applink = DEFAULT_BANNER_APPLINK_3
        defaultBannerSlidesModel3.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
        defaultBannerSlidesModel3.imageUrl = DEFAULT_BANNER_IMAGE_URL_3

        defaultSlides.add(defaultBannerSlidesModel1)
        defaultSlides.add(defaultBannerSlidesModel2)
        defaultSlides.add(defaultBannerSlidesModel3)

        return BannerDataModel(slides = defaultSlides)
    }

    fun createDefaultHomeDynamicIcon(): DynamicHomeIcon {
        return DynamicHomeIcon(
                dynamicIcon = listOf(
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = DEFAULT_DYNAMIC_ICON_IMAGE_URL_1,
                                applinks = DEFAULT_DYNAMIC_ICON_APPLINK_1,
                                name = DEFAULT_DYNAMIC_ICON_NAME_1),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = DEFAULT_DYNAMIC_ICON_IMAGE_URL_2,
                                applinks = DEFAULT_DYNAMIC_ICON_APPLINK_2,
                                name = DEFAULT_DYNAMIC_ICON_NAME_2),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = DEFAULT_DYNAMIC_ICON_IMAGE_URL_3,
                                applinks = DEFAULT_DYNAMIC_ICON_APPLINK_3,
                                name = DEFAULT_DYNAMIC_ICON_NAME_3),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = DEFAULT_DYNAMIC_ICON_IMAGE_URL_4,
                                applinks = DEFAULT_DYNAMIC_ICON_APPLINK_4,
                                name = DEFAULT_DYNAMIC_ICON_NAME_4),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = DEFAULT_DYNAMIC_ICON_IMAGE_URL_5,
                                applinks = DEFAULT_DYNAMIC_ICON_APPLINK_5,
                                name = DEFAULT_DYNAMIC_ICON_NAME_5)
                )
        )
    }

    fun createDefaultHomeDynamicChannel(): DynamicHomeChannel {
        return DynamicHomeChannel(
                channels = listOf(
                        DynamicHomeChannel.Channels(
                                id = DEFAULT_DYNAMIC_CHANNEL_1_ID,
                                layout = DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR,
                                banner = DynamicHomeChannel.Banner(imageUrl = DEFAULT_DYNAMIC_CHANNEL_1_BANNER)),
                        DynamicHomeChannel.Channels(
                                id = DEFAULT_DYNAMIC_CHANNEL_2_ID,
                                layout = DynamicHomeChannel.Channels.LAYOUT_6_IMAGE,
                                grids = arrayOf(
                                        DynamicHomeChannel.Grid(
                                                imageUrl = DEFAULT_DYNAMIC_CHANNEL_2_GRID_1_IMAGE_URL,
                                                applink = DEFAULT_DYNAMIC_CHANNEL_2_GRID_1_APPLINK),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = DEFAULT_DYNAMIC_CHANNEL_2_GRID_2_IMAGE_URL,
                                                applink = DEFAULT_DYNAMIC_CHANNEL_2_GRID_2_APPLINK),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = DEFAULT_DYNAMIC_CHANNEL_2_GRID_3_IMAGE_URL,
                                                applink = DEFAULT_DYNAMIC_CHANNEL_2_GRID_3_APPLINK),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = DEFAULT_DYNAMIC_CHANNEL_2_GRID_4_IMAGE_URL,
                                                applink = DEFAULT_DYNAMIC_CHANNEL_2_GRID_4_APPLINK,
                                                url = DEFAULT_DYNAMIC_CHANNEL_2_GRID_4_URL),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = DEFAULT_DYNAMIC_CHANNEL_2_GRID_5_IMAGE_URL,
                                                applink = DEFAULT_DYNAMIC_CHANNEL_2_GRID_5_APPLINK,
                                                url = DEFAULT_DYNAMIC_CHANNEL_2_GRID_5_URL),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = DEFAULT_DYNAMIC_CHANNEL_2_GRID_6_IMAGE_URL,
                                                applink = DEFAULT_DYNAMIC_CHANNEL_2_GRID_6_APPLINK,
                                                url = DEFAULT_DYNAMIC_CHANNEL_2_GRID_6_URL)
                                )
                        )
                )
        )
    }

    private fun createHomeFlag(): HomeFlag {
        val homeFlag = HomeFlag()
        homeFlag.flags = listOf(
                Flags(HomeFlag.DYNAMIC_ICON_WRAP_STRING, true),
                Flags(HomeFlag.HAS_TOKOPOINTS_STRING, false),
                Flags(HomeFlag.HAS_RECOM_NAV_BUTTON_STRING, false)
        ).toMutableList()
        return homeFlag
    }
}