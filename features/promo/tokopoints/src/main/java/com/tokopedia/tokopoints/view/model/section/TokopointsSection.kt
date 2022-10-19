package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName

data class TokopointsSection(
    @SerializedName("sectionContent")
    var sectionContent: MutableList<SectionContent> = mutableListOf()
)
