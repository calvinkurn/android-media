package com.tokopedia.feedcomponent.domain.model.statistic

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-23
 */
data class GetPostStatisticResponse(

        @SerializedName("feedGetStatsPosts")
        val feedGetStatsPosts: FeedGetStatsPosts
)