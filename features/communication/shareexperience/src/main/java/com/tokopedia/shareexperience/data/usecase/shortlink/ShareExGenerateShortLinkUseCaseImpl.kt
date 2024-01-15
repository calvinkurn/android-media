package com.tokopedia.shareexperience.data.usecase.shortlink

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.AN_MIN_VERSION
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.IOS_MIN_VERSION
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkAdditionalParamRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGenerateShortLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGenerateShortLinkUseCaseImpl @Inject constructor(
    private val generateAffiliateLinkUseCase: ShareExGetAffiliateLinkUseCase,
    private val generateBranchLinkUseCase: ShareExGetBranchLinkUseCase,
    private val dispatchers: CoroutineDispatchers
) : ShareExGenerateShortLinkUseCase {

    override suspend fun getShortLink(params: ShareExShortLinkRequest): Flow<ShareExResult<String>> {
        return channelFlow {
            var successReceived = false
            // Loop for every fallback
            loop@ for (fallbackPriorityEnum in params.fallbackPriorityEnumList) {
                requestShortLink(fallbackPriorityEnum, params).collectLatest {
                    when (it) {
                        is ShareExResult.Success -> {
                            successReceived = true
                            send(ShareExResult.Success(it.data))
                            return@collectLatest // break the collect after get result
                        }
                        is ShareExResult.Error -> {
                            send(ShareExResult.Error(it.throwable)) // emit the error then proceed to next fallback
                        }
                        ShareExResult.Loading -> {
                            send(ShareExResult.Loading)
                        }
                    }
                }
                if (successReceived) {
                    break@loop
                }
            }
        }.flowOn(dispatchers.io)
    }

    private suspend fun requestShortLink(
        fallbackPriorityEnum: ShareExShortLinkFallbackPriorityEnum,
        params: ShareExShortLinkRequest
    ): Flow<ShareExResult<String>> {
        return when (fallbackPriorityEnum) {
            ShareExShortLinkFallbackPriorityEnum.AFFILIATE -> {
                generateAffiliateLinkUseCase.getLink(
                    getAffiliateLinkPropertiesRequest(params)
                )
            }
            ShareExShortLinkFallbackPriorityEnum.BRANCH -> {
                generateBranchLinkUseCase.getLink(
                    getBranchLinkPropertiesRequest(params)
                )
            }
            ShareExShortLinkFallbackPriorityEnum.DEFAULT -> {
                flowOf(ShareExResult.Success(params.defaultUrl))
            }
        }
    }

    private fun getAffiliateLinkPropertiesRequest(
        params: ShareExShortLinkRequest
    ): ShareExAffiliateLinkPropertiesRequest {
        return ShareExAffiliateLinkPropertiesRequest(
            source = "sharing",
            channel = listOf(0),
            link = listOf(
                ShareExAffiliateLinkRequest(
                    type = "pdp",
                    url = params.linkerPropertiesRequest.originalUrl,
                    identifier = params.identifierId,
                    identifierType = 0,
                    additionalParams = listOf(
                        ShareExAffiliateLinkAdditionalParamRequest("og_title", params.linkerPropertiesRequest.ogTitle),
                        ShareExAffiliateLinkAdditionalParamRequest("og_description", params.linkerPropertiesRequest.ogTitle),
                        ShareExAffiliateLinkAdditionalParamRequest("og_image_url", params.linkerPropertiesRequest.ogImageUrl)
                    )
                )
            )
        )
    }

    private fun getBranchLinkPropertiesRequest(
        params: ShareExShortLinkRequest
    ): ShareExBranchLinkPropertiesRequest {
        return ShareExBranchLinkPropertiesRequest(
            branchUniversalObjectRequest = generateBranchUniversalObjectRequest(params),
            linkerPropertiesRequest = params.linkerPropertiesRequest,
            channelEnum = params.channelEnum
        )
    }

    private fun generateBranchUniversalObjectRequest(
        params: ShareExShortLinkRequest
    ): ShareExBranchUniversalObjectRequest {
        return ShareExBranchUniversalObjectRequest(
            canonicalId = params.identifierId,
            title = params.linkerPropertiesRequest.ogTitle,
            description = params.linkerPropertiesRequest.message,
            contentImageUrl = params.linkerPropertiesRequest.ogImageUrl,
            contentMetadataMap = mapOf(
                AN_MIN_VERSION to params.linkerPropertiesRequest.androidMinVersion,
                IOS_MIN_VERSION to params.linkerPropertiesRequest.iosMinVersion
            )
        )
    }
}
