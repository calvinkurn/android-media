package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.repository.ShareExShortLinkRepository
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetShortLinkUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetShortLinkUseCaseImpl @Inject constructor(
    private val shortLinkRepository: ShareExShortLinkRepository,
    private val getGeneratedImageUseCase: ShareExGetGeneratedImageUseCase,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetShortLinkUseCase {

    /**
     * 1. Check if Image Generator params are not null
     * 2.A Get Generated Image form Image Generator API with ShareExImageGeneratorRequest
     * 3.A. If image generator success then use generated image for short link
     * 3.B. If image generator fail then use original image for short link
     *
     * 2.B Directly get short link with original image
     */
    @OptIn(FlowPreview::class)
    override suspend fun getShortLink(
        imageGeneratorParams: ShareExImageGeneratorWrapperRequest,
        linkPropertiesParams: ShareExBranchLinkPropertiesRequest
    ): Flow<ShareExResult<String>> {
        val resultFlow = getImageGeneratorFlow(imageGeneratorParams, linkPropertiesParams.channelEnum)
            .flatMapConcat {
                var shortLinkFlow: Flow<ShareExResult<String>> = flowOf()
                when (it) {
                    is ShareExResult.Success -> {
                        shortLinkFlow = generateShortLinkWithGeneratedImage(
                            linkPropertiesParams,
                            it.data.imageUrl
                        )
                    }
                    is ShareExResult.Error -> {
                        // Using original image
                        shortLinkFlow = shortLinkRepository.generateShortLink(linkPropertiesParams)
                    }
                    ShareExResult.Loading -> Unit
                }
                shortLinkFlow
            }
            .flowOn(dispatchers.io)

        return resultFlow
    }

    private suspend fun generateShortLinkWithGeneratedImage(
        linkPropertiesParams: ShareExBranchLinkPropertiesRequest,
        generatedImageUrl: String
    ): Flow<ShareExResult<String>> {
        // Using generated image
        val generatedImageLinkProperties = linkPropertiesParams.copy(
            branchUniversalObjectRequest = linkPropertiesParams.branchUniversalObjectRequest.copy(
                contentImageUrl = generatedImageUrl
            ),
            linkerPropertiesRequest = linkPropertiesParams.linkerPropertiesRequest.copy(
                ogImageUrl = generatedImageUrl
            )
        )
        return shortLinkRepository.generateShortLink(generatedImageLinkProperties)
    }

    private suspend fun getImageGeneratorFlow(
        imageGeneratorParams: ShareExImageGeneratorWrapperRequest,
        channelEnum: ShareExChannelEnum
    ): Flow<ShareExResult<ShareExImageGeneratorModel>> {
        return if (imageGeneratorParams.params.sourceId != null &&
            !imageGeneratorParams.params.args.isNullOrEmpty()
        ) {
            val newParams = getCompletedImageGeneratorParams(
                imageGeneratorParams.params,
                imageGeneratorParams.originalImageUrl,
                channelEnum
            )
            getGeneratedImageUseCase.getData(newParams)
        } else {
            // Without image generator
            flow {
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(imageGeneratorParams.originalImageUrl)
                    )
                )
            }
        }
    }

    private fun getCompletedImageGeneratorParams(
        originalParam: ShareExImageGeneratorRequest,
        originalImageUrl: String,
        channelEnum: ShareExChannelEnum
    ): ShareExImageGeneratorRequest {
        val updatedArgs = originalParam.args?.toMutableList()
        updatedArgs?.add(ShareExImageGeneratorArgRequest(PLATFORM_KEY, channelEnum.label))
        updatedArgs?.add(ShareExImageGeneratorArgRequest(PRODUCT_IMAGE_URL_KEY, originalImageUrl))
        return originalParam.copy(
            args = updatedArgs
        )
    }

    companion object {
        private const val PLATFORM_KEY = "platform"
        private const val PRODUCT_IMAGE_URL_KEY = "product_image_url"
    }
}
