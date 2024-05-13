package com.tokopedia.shareexperience.data.usecase.shortlink

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkAdditionalParamRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExConstants.ShortLinkKey.OG_DESCRIPTION_RAW
import com.tokopedia.shareexperience.domain.util.ShareExConstants.ShortLinkKey.OG_IMAGE_URL_RAW
import com.tokopedia.shareexperience.domain.util.ShareExConstants.ShortLinkKey.OG_TITLE_RAW
import com.tokopedia.shareexperience.domain.util.ShareExConstants.ShortLinkValue.SOURCE_SHARING
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetShortLinkUseCaseImpl @Inject constructor(
    private val generateAffiliateLinkUseCase: ShareExGetAffiliateLinkUseCase,
    private val generateBranchLinkUseCase: ShareExGetBranchLinkUseCase,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetShortLinkUseCase {

    override suspend fun getShortLink(
        params: ShareExShortLinkRequest
    ): Flow<Pair<ShareExShortLinkFallbackPriorityEnum, ShareExResult<String>>> {
        return channelFlow {
            var successReceived = false
            // Loop for every fallback
            loop@ for (fallbackPriorityEnum in params.fallbackPriorityEnumList) {
                requestShortLink(fallbackPriorityEnum, params).collectLatest {
                    when (it) {
                        is ShareExResult.Success -> {
                            successReceived = true
                            send(Pair(fallbackPriorityEnum, ShareExResult.Success(it.data)))
                            return@collectLatest // break the collect after get result
                        }
                        is ShareExResult.Error -> {
                            send(Pair(fallbackPriorityEnum, ShareExResult.Error(it.throwable))) // emit the error then proceed to next fallback
                        }
                        ShareExResult.Loading -> {
                            send(Pair(fallbackPriorityEnum, ShareExResult.Loading))
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
    ): ShareExAffiliateLinkPropertiesWrapperRequest {
        return ShareExAffiliateLinkPropertiesWrapperRequest(
            ShareExAffiliateLinkPropertiesRequest(
                source = SOURCE_SHARING,
                channel = listOf(params.channelEnum.id),
                link = listOf(
                    ShareExAffiliateLinkRequest(
                        type = if (params.pageTypeEnum == ShareExPageTypeEnum.ORDER_DETAIL) {
                            ShareExPageTypeEnum.AFFILIATE_TYPE_PDP
                        } else {
                            params.pageTypeEnum.value
                        },
                        url = params.linkerPropertiesRequest.originalUrl,
                        identifier = params.identifierId,
                        identifierType = 0, // Currently not used, same as IOS
                        additionalParams = listOf(
                            ShareExAffiliateLinkAdditionalParamRequest(OG_TITLE_RAW, params.linkerPropertiesRequest.ogTitle),
                            ShareExAffiliateLinkAdditionalParamRequest(OG_DESCRIPTION_RAW, params.linkerPropertiesRequest.ogDescription),
                            ShareExAffiliateLinkAdditionalParamRequest(OG_IMAGE_URL_RAW, params.linkerPropertiesRequest.ogImageUrl)
                        )
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
            canonicalId = params.linkerPropertiesRequest.canonicalIdentifier,
            title = params.linkerPropertiesRequest.ogTitle,
            description = params.linkerPropertiesRequest.messageObject.getFinalMessage(),
            contentImageUrl = params.linkerPropertiesRequest.ogImageUrl,
            customMetaTags = params.linkerPropertiesRequest.customMetaTags,
            anMinVersion = params.linkerPropertiesRequest.anMinVersion
        )
    }
}
