package com.tokopedia.shareexperience.domain.model.request.imagegenerator

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ShareExImageGeneratorRequest(
    @SerializedName("sourceID")
    val sourceId: String?,
    @SerializedName("args")
    val args: List<ShareExImageGeneratorArgRequest>?
) : GqlParam
