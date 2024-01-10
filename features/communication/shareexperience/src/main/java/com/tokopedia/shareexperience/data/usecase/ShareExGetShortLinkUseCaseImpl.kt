package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.repository.ShareExShortLinkRepository
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetShortLinkUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetShortLinkUseCaseImpl @Inject constructor(
    private val shortLinkRepository: ShareExShortLinkRepository,
    private val getGeneratedImageUseCase: ShareExGetGeneratedImageUseCase,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetShortLinkUseCase {
    @OptIn(FlowPreview::class)
    override suspend fun getShortLink(
        imageGeneratorParams: ShareExImageGeneratorWrapperRequest,
        channelEnum: ShareExChannelEnum
    ): Flow<ShareExResult<String>> {
        val resultFlow = getImageGeneratorFlow(imageGeneratorParams, channelEnum)
            .flatMapConcat {
                when (it) {
                    is ShareExResult.Error -> TODO()
                    ShareExResult.Loading -> TODO()
                    is ShareExResult.Success -> TODO()
                }
                val shortLinkFlow = shortLinkRepository.generateShortLink()
                shortLinkFlow
            }
            .flowOn(dispatchers.io)

        return resultFlow
    }

    private suspend fun getImageGeneratorFlow(
        imageGeneratorParams: ShareExImageGeneratorWrapperRequest,
        channelEnum: ShareExChannelEnum
    ): Flow<ShareExResult<ShareExImageGeneratorModel>> {
        return if (imageGeneratorParams.params != null) {
            val updatedArgs = imageGeneratorParams.params.args.toMutableList()
            updatedArgs.add(ShareExImageGeneratorArgRequest("platform", channelEnum.label))
            updatedArgs.add(ShareExImageGeneratorArgRequest("product_image_url", imageGeneratorParams.originalImageUrl))
            val params = imageGeneratorParams.params.copy(
                args = updatedArgs
            )
            getGeneratedImageUseCase.getData(params)
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
}
