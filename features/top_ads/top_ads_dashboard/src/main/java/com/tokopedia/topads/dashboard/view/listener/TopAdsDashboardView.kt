package com.tokopedia.topads.dashboard.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.topads.dashboard.data.model.AdStatusResponse
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus

/**
 * Created by hadi.putra on 23/04/18.
 */

interface TopAdsDashboardView : CustomerView {

    fun onErrorGetShopInfo(throwable: Throwable)

    fun onErrorGetStatisticsInfo(throwable: Throwable)

    fun onSuccessAdStatus(data: AdStatusResponse.TopAdsGetShopInfo.Data)

    fun onError(message:String)

}

