package com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ShareExAffiliateLinkPropertiesWrapperRequest(
    @SerializedName("input")
    val input: ShareExAffiliateLinkPropertiesRequest = ShareExAffiliateLinkPropertiesRequest()
): GqlParam
