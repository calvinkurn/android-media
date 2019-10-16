package com.tokopedia.createpost.data.pojo.productsuggestion.affiliate


import com.google.gson.annotations.SerializedName

data class AffiliateParticularSections(
    @SerializedName("explore_page_section")
    val explorePageSection: List<ExplorePageSection> = listOf()
)