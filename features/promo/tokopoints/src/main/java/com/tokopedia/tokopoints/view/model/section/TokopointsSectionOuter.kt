package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName

data class TokopointsSectionOuter(
    @SerializedName("tokopointsHomepage")
    var sectionContent: TokopointsSection = TokopointsSection()
)
