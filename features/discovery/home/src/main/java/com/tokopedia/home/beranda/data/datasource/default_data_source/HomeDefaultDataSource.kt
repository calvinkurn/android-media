package com.tokopedia.home.beranda.data.datasource.default_data_source

import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel

class HomeDefaultDataSource {

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
}
