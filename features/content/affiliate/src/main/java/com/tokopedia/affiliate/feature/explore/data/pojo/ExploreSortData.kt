package com.tokopedia.affiliate.feature.explore.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreSort

/**
 * @author by milhamj on 15/03/19.
 */
data class ExploreSortData(
        @SerializedName("topadsGetExploreSort")
        @Expose
        val exploreSort: ExploreSort = ExploreSort()
)