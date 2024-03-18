package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ShareExBottomSheetWrapperRequest(
    @SerializedName("params")
    val params: ShareExBottomSheetRequest
): GqlParam
