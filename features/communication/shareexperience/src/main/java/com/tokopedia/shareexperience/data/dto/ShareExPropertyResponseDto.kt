package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateEligibilityResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateRegistrationWidgetResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorResponseDto

data class ShareExPropertyResponseDto(
    @SerializedName("chipTitle")
    val chipTitle: String = "",
    @SerializedName("shareBody")
    val shareBody: ShareExShareBodyResponseDto = ShareExShareBodyResponseDto(),
    @SerializedName("affiliateRegistrationWidget")
    val affiliateRegistrationWidget: ShareExAffiliateRegistrationWidgetResponseDto? = null,
    @SerializedName("affiliateEligibility")
    val affiliateEligibility: ShareExAffiliateEligibilityResponseDto? = null,
    @SerializedName("imageGeneratorPayload")
    val imageGeneratorPayload: ShareExPropertyImageGeneratorResponseDto? = null,
    @SerializedName("generateLinkProperties")
    val generateLinkProperties: ShareExGenerateLinkPropertiesResponseDto = ShareExGenerateLinkPropertiesResponseDto()
)
