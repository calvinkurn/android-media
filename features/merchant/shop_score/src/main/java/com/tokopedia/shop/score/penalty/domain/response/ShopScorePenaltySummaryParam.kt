package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getNPastMonthTimeStamp
import com.tokopedia.shop.score.common.getNowTimeStamp

data class ShopScorePenaltySummaryParam(
        @SerializedName("startDate")
        @Expose
        val startDate: String = format(getNPastMonthTimeStamp(1).time, PATTERN_PENALTY_DATE_PARAM),
        @SerializedName("endDate")
        @Expose
        val endDate: String = format(getNowTimeStamp(), PATTERN_PENALTY_DATE_PARAM),
        @SerializedName("source")
        @Expose
        val source: String = "android"
)