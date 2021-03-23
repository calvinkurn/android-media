package com.tokopedia.topads.dashboard.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData

/**
 * Created by Pika on 22/7/20.
 */

interface TopAdsInsightView : CustomerView{

    fun onSuccessKeywordInsight(it: InsightKeyData)

    fun onSuccessEditKeywords(it: FinalAdResponse)

    fun onErrorEditKeyword(throwable: List<FinalAdResponse.TopadsManageGroupAds.ErrorsItem>?)
}