package com.tokopedia.common.topupbills.widget

import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction

interface TopupBillsRecentNumberListener {
    fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: String, position: Int)
    fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>)
}