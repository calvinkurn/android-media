package com.tokopedia.home.beranda.data.datasource

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel

class HomeDefaultDataSource() {
    val DEFAULT_BANNER_APPLINK_1 = "tokopedia://category-explore?type=1"
    val DEFAULT_BANNER_APPLINK_2 = ApplinkConst.OFFICIAL_STORE
    val DEFAULT_BANNER_APPLINK_3 = ApplinkConst.PROMO

    val DEFAULT_BANNER_IMAGE_URL_1 = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg"
    val DEFAULT_BANNER_IMAGE_URL_2 = "https://ecs7.tokopedia.net/defaultpage/banner/banneros1000.jpg"
    val DEFAULT_BANNER_IMAGE_URL_3 = "https://ecs7.tokopedia.net/defaultpage/banner/bannerpromo1000.jpg"

    fun getDefaultHomeData(): HomeData? = HomeData(
            dynamicHomeChannel = createDefaultHomeDynamicChannel(),
            banner = createDefaultHomePageBanner(),
            dynamicHomeIcon = createDefaultHomeDynamicIcon(),
            homeFlag = createHomeFlag()
    )

    private fun createHomeFlag(): HomeFlag {
        val homeFlag = HomeFlag()
        homeFlag.flags = listOf(
                Flags(HomeFlag.DYNAMIC_ICON_WRAP_STRING, true),
                Flags(HomeFlag.HAS_TOKOPOINTS_STRING, false),
                Flags(HomeFlag.HAS_RECOM_NAV_BUTTON_STRING, false)
        )
        return homeFlag
    }

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
        val dynamicHomeIcon = DynamicHomeIcon(
                dynamicIcon = listOf(
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/icon/icon1.png",
                                applinks = "tokopedia://category-explore?type=2&tab=1",
                                name = "Semua Kategori"),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/icon/icon2.png",
                                applinks = "tokopedia://category-explore?type=1",
                                name = "Belanja"),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/icon/icon3.png",
                                applinks = "tokopedia://recharge/home",
                                name = "Top-up & Tagihan"),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/icon/icon4.png",
                                applinks = "tokopedia://travelentertainment/home",
                                name = "Travel & Entertainment"),
                        DynamicHomeIcon.DynamicIcon(
                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/icon/icon5.png",
                                applinks = "tokopedia://discovery/keuangan",
                                name = "Keuangan")
                )
        )
        return dynamicHomeIcon
    }

    fun createDefaultHomeDynamicChannel(): DynamicHomeChannel {
        return DynamicHomeChannel(
                channels = listOf(
                        DynamicHomeChannel.Channels(
                                id = "3",
                                layout = DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR,
                                banner = DynamicHomeChannel.Banner(imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channelerror.jpg")),
                        DynamicHomeChannel.Channels(
                                id = "4",
                                layout = DynamicHomeChannel.Channels.LAYOUT_6_IMAGE,
                                grids = arrayOf(
                                        DynamicHomeChannel.Grid(
                                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channel1.jpg",
                                                applink = "tokopedia://category-explore?type=1"),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channel2.jpg",
                                                applink = "tokopedia://category-explore?type=1"),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channel3.jpg",
                                                applink = "tokopedia://category-explore?type=1"),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channel4.jpg",
                                                applink = "tokopedia://category-explore?type=1"),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channel5.jpg",
                                                applink = "tokopedia://category-explore?type=1"),
                                        DynamicHomeChannel.Grid(
                                                imageUrl = "https://ecs7.tokopedia.net/defaultpage/channel/channel6.jpg",
                                                applink = "tokopedia://category-explore?type=1")
                                )
                        )
                )
        )
    }
}