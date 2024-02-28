package com.tokopedia.shareexperience.stub.data

import com.tokopedia.shareexperience.data.dto.ShareExWrapperResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.eligibility.ShareExAffiliateLinkWrapperResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.generatelink.ShareExGenerateAffiliateLinkWrapperResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExImageGeneratorWrapperResponseDto
import com.tokopedia.shareexperience.data.query.ShareExGetAffiliateEligibilityQuery
import com.tokopedia.shareexperience.data.query.ShareExGetAffiliateLinkQuery
import com.tokopedia.shareexperience.data.query.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.data.query.ShareExImageGeneratorQuery

object GqlResponseStub {

    init {
        reset()
    }

    lateinit var affiliateEligibilityResponse: ResponseStub<ShareExAffiliateLinkWrapperResponseDto>
    lateinit var sharePropertiesResponse: ResponseStub<ShareExWrapperResponseDto>
    lateinit var generatedImageResponse: ResponseStub<ShareExImageGeneratorWrapperResponseDto>
    lateinit var generateAffiliateLinkResponse: ResponseStub<ShareExGenerateAffiliateLinkWrapperResponseDto>

    fun reset() {
        affiliateEligibilityResponse = ResponseStub(
            filePath = "affiliate/affiliate_eligibility.json",
            type = ShareExAffiliateLinkWrapperResponseDto::class.java,
            query = ShareExGetAffiliateEligibilityQuery.OPERATION_NAME,
            isError = false
        )
        sharePropertiesResponse = ResponseStub(
            filePath = "properties/share_properties.json",
            type = ShareExWrapperResponseDto::class.java,
            query = ShareExGetSharePropertiesQuery.OPERATION_NAME,
            isError = false
        )
        generatedImageResponse = ResponseStub(
            filePath = "shortlink/affiliate_link.json",
            type = ShareExGenerateAffiliateLinkWrapperResponseDto::class.java,
            query = ShareExImageGeneratorQuery.OPERATION_NAME,
            isError = false
        )
        generateAffiliateLinkResponse = ResponseStub(
            filePath = "shortlink/affiliate_link.json",
            type = ShareExGenerateAffiliateLinkWrapperResponseDto::class.java,
            query = ShareExGetAffiliateLinkQuery.OPERATION_NAME,
            isError = false
        )
    }
}
