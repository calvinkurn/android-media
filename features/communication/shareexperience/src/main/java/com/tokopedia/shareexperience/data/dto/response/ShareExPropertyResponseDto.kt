package com.tokopedia.shareexperience.data.dto.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.shareexperience.data.dto.response.affiliate.ShareExAffiliateEligibilityResponseDto
import com.tokopedia.shareexperience.data.dto.response.affiliate.ShareExAffiliateRegistrationWidgetResponseDto

data class ShareExPropertyResponseDto(
    @SerializedName("shareBody")
    val shareBody: ShareExShareBodyResponseDto = ShareExShareBodyResponseDto(),
    @SerializedName("affiliateRegistrationWidget")
    val affiliateRegistrationWidget: ShareExAffiliateRegistrationWidgetResponseDto = ShareExAffiliateRegistrationWidgetResponseDto(),
    @SerializedName("affiliateEligibility")
    val affiliateEligibility: ShareExAffiliateEligibilityResponseDto = ShareExAffiliateEligibilityResponseDto()
)
