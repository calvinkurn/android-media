package com.tokopedia.topads.dashboard.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus

/**
 * Created by hadi.putra on 23/04/18.
 */

interface TopAdsDashboardView : CustomerView {
    fun onLoadTopAdsShopDepositError(throwable: Throwable)

    fun onLoadTopAdsShopDepositSuccess(dataDeposit: DataDeposit)

    fun onErrorGetShopInfo(throwable: Throwable)

    fun onSuccessGetShopInfo(shopInfo: ShopInfo)

    fun onErrorPopulateTotalAds(throwable: Throwable)

    fun onSuccessPopulateTotalAds(totalAd: TotalAd)

    fun onErrorGetStatisticsInfo(throwable: Throwable)

    fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic)

    fun onErrorPopulateData(throwable: Throwable)

    fun onSuccessPopulateData(dashboardPopulateResponse: DashboardPopulateResponse)

    fun onSuccessGetTicker(message: List<String>)

    fun onSuccessAdsInfo(data: AutoAdsResponse.TopAdsGetAutoAds.Data)

    fun onErrorGetTicker(e: Throwable)

    fun onErrorGetAutoTopUpStatus(throwable: Throwable)

    fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus)

    fun onSuccessAdStatus(data: AdStatusResponse.TopAdsGetShopInfo.Data)

}
