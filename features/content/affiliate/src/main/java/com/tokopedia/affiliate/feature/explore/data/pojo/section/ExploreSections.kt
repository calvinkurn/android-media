package com.tokopedia.affiliate.feature.explore.data.pojo.section

import com.google.gson.annotations.SerializedName

data class ExploreSections(
        @SerializedName("explore_page_section")
        val explorePageSection: List<ExplorePageSection> = listOf()
)