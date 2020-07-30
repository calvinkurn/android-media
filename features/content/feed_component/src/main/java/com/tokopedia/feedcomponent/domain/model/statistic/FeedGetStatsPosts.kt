package com.tokopedia.feedcomponent.domain.model.statistic

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class FeedGetStatsPosts(

        @SerializedName("stats")
        val stats: List<PostStat> = emptyList()
)