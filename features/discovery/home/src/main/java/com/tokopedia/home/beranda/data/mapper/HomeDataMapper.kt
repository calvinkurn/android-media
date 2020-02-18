package com.tokopedia.home.beranda.data.mapper

import android.content.Context
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
                .addUserWalletVisitable()
                .addGeolocationVisitable()
                .addDynamicIconVisitable()
                .addDynamicChannelVisitable()
                .build()
        return HomeDataModel(homeData.homeFlag, list, isCache)
    }

    fun mapToHomeHeaderWalletAction(walletBalanceModel: WalletBalanceModel): HomeHeaderWalletAction? {
        val data = HomeHeaderWalletAction()
        data.isLinked = walletBalanceModel.link
        data.balance = walletBalanceModel.balance
        data.labelTitle = walletBalanceModel.titleText
        data.appLinkBalance = walletBalanceModel.applinks
        data.labelActionButton = walletBalanceModel.actionBalanceModel!!.labelAction
        data.isVisibleActionButton = (walletBalanceModel.actionBalanceModel!!.visibility != null
                && walletBalanceModel.actionBalanceModel!!.visibility == "1")
        data.appLinkActionButton = walletBalanceModel.actionBalanceModel!!.applinks
        data.abTags = if (walletBalanceModel.abTags == null) ArrayList() else walletBalanceModel.abTags
        data.pointBalance = walletBalanceModel.pointBalance
        data.rawPointBalance = walletBalanceModel.rawPointBalance
        data.cashBalance = walletBalanceModel.cashBalance
        data.rawCashBalance = walletBalanceModel.rawCashBalance
        data.walletType = walletBalanceModel.walletType
        data.isShowAnnouncement = walletBalanceModel.isShowAnnouncement
        return data
    }
}
