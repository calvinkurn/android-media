package com.tokopedia.affiliate.feature.explore.data.pojo.section

import com.google.gson.annotations.SerializedName

data class ExploreSectionResponse(
        @SerializedName("affiliateCPAExploreSections")
        val exploreSections: ExploreSections = ExploreSections()
)