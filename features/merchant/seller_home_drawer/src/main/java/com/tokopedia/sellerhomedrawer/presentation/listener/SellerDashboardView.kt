package com.tokopedia.sellerhomedrawer.presentation.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.core.common.ticker.model.Ticker
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult

interface SellerDashboardView: CustomerView {

    fun onSuccessGetShopInfoAndScore(shopInfoDashboardModel: ShopInfoDashboardModel,
                                     goldGetPmOsStatus: GoldGetPmOsStatus,
                                     shopScoreResult: ShopScoreResult)

    fun onErrorShopInfoAndScore(t: Throwable)

    fun onErrorGetTickers(throwable: Throwable)

    fun onSuccessGetTickers(tickers: Array<Ticker.Tickers>)

    fun onErrorGetNotifiction(message: String)

    fun onSuccessGetNotification(drawerNotification: DrawerNotification)

    fun showLoading()

    fun hideLoading()

    fun onSuccessOpenShop()

    fun onErrorOpenShop()

    fun getContext(): Context

    fun getApprovalStatusListener(): GetApprovalStatusSubscriber.GetApprovalStatusListener
}