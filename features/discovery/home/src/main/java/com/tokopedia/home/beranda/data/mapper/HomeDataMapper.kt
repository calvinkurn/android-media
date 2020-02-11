package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon.DynamicIcon
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.domain.model.ProductImage
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

class HomeDataMapper(
        private val context: Context,
        private val homeVisitableFactory: HomeVisitableFactory,
        private val trackingQueue: TrackingQueue
) {
    fun mapToHomeViewModel(homeData: HomeData?, isCache: Boolean): HomeViewModel{
        if (homeData == null) return HomeViewModel(isCache = isCache)
        val list: List<Visitable<*>> = homeVisitableFactory.buildVisitableList(
                homeData, isCache, trackingQueue, context)
                .addBannerVisitable()
                .addUserWalletVisitable()
                .addGeolocationVisitable()
                .addDynamicIconVisitable()
                .addDynamicChannelVisitable()
                .build()
        return HomeViewModel(homeData.homeFlag, list, isCache)
    }
}
