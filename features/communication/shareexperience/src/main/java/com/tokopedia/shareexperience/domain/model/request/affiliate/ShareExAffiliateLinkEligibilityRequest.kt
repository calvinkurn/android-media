package com.tokopedia.shareexperience.domain.model.request.affiliate

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ShareExAffiliateLinkEligibilityRequest(
    @SerializedName("generateAffiliateLinkEligibilityRequest")
    val affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest? = ShareExAffiliateEligibilityRequest()
) : GqlParam
