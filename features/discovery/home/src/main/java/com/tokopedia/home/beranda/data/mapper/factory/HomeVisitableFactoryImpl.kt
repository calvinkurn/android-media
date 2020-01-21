package com.tokopedia.home.beranda.data.mapper.factory

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.user.session.UserSessionInterface
import java.util.ArrayList

class HomeVisitableFactoryImpl(val userSessionInterface: UserSessionInterface) : HomeVisitableFactory {
    val DEFAULT_BANNER_APPLINK_1 = "tokopedia://category-explore?type=2&tab=1"
    val DEFAULT_BANNER_APPLINK_2 = ApplinkConst.OFFICIAL_STORE
    val DEFAULT_BANNER_IMAGE_URL_1 = "https://ecs7.tokopedia.net/android/others/home_banner_default_1.jpg"
    val DEFAULT_BANNER_IMAGE_URL_2 = "https://ecs7.tokopedia.net/android/others/home_banner_default_2.jpg"

    override fun createOvoTokopointVisitable(hasTokopoints: Boolean, isCache: Boolean): HeaderViewModel? {
        if(hasTokopoints) {
            val headerViewModel = HeaderViewModel()
            headerViewModel.isPendingTokocashChecked = false
            headerViewModel.isUserLogin = userSessionInterface.isLoggedIn
            return headerViewModel
        }
        return null
    }

    override fun createBannerVisitable(bannerDataModel: BannerDataModel?,
                                       isCache: Boolean): BannerViewModel {
        val bannerViewModel = BannerViewModel()
        if (bannerDataModel == null || bannerDataModel.slides == null || bannerDataModel.slides.isEmpty()) {
            val defaultSlides = mutableListOf<BannerSlidesModel>()
            val defaultBannerSlidesModel1 = BannerSlidesModel()
            defaultBannerSlidesModel1.applink = DEFAULT_BANNER_APPLINK_1
            defaultBannerSlidesModel1.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
            defaultBannerSlidesModel1.imageUrl = DEFAULT_BANNER_IMAGE_URL_1

            val defaultBannerSlidesModel2 = BannerSlidesModel()
            defaultBannerSlidesModel2.applink = DEFAULT_BANNER_APPLINK_2
            defaultBannerSlidesModel2.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
            defaultBannerSlidesModel2.imageUrl = DEFAULT_BANNER_IMAGE_URL_2

            defaultSlides.add(defaultBannerSlidesModel1)
            defaultSlides.add(defaultBannerSlidesModel2)
            bannerViewModel.slides = defaultSlides
        } else {
            bannerDataModel.slides.forEachIndexed { index, bannerSlidesModel ->
                bannerSlidesModel.position = index+1
            }
            bannerViewModel.slides = bannerDataModel.slides
        }
        bannerViewModel.isCache = isCache
        return bannerViewModel
    }
}