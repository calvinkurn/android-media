package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

class HomeDataMapper(
        private val context: Context,
        private val homeVisitableFactory: HomeVisitableFactory,
        private val trackingQueue: TrackingQueue
) {
    fun mapToHomeViewModel(homeData: HomeData?, isCache: Boolean): HomeDataModel{
        if (homeData == null) return HomeDataModel(isCache = isCache)
        val list: List<Visitable<*>> = homeVisitableFactory.buildVisitableList(
                homeData, isCache, trackingQueue, context)
                .addBannerVisitable()
                .addTickerVisitable()
                .addUserWalletVisitable()
                .addGeolocationVisitable()
                .addDynamicIconVisitable()
                .addDynamicChannelVisitable()
                .build()
        Log.d("testNoSkeleton", "Map to home viewModel " + homeData.toString())
        Log.d("testNoSkeleton", "Is cache " + isCache.toString())
        return HomeDataModel(homeData.homeFlag, list, isCache)
    }
}
