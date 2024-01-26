package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExImageGeneratorWrapperResponseDto
import com.tokopedia.shareexperience.data.query.ShareExImageGeneratorQuery
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.domain.util.asFlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetGeneratedImageUseCaseImpl @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetGeneratedImageUseCase {

    private val query = ShareExImageGeneratorQuery()
    override suspend fun getData(
        params: ShareExImageGeneratorWrapperRequest,
        channelEnum: ShareExChannelEnum
    ): Flow<ShareExResult<ShareExImageGeneratorModel>> {
        return flow {
            val result = if (params.params.sourceId != null &&
                !params.params.args.isNullOrEmpty()
            ) {
                val request = getCompletedImageGeneratorParams(params, channelEnum)
                val response = repository.request<ShareExImageGeneratorRequest, ShareExImageGeneratorWrapperResponseDto>(
                    query,
                    request
                )
                mapToModel(response.imageGeneratorModel.imageUrl)
            } else {
                ShareExImageGeneratorModel(
                    params.originalImageUrl,
                    ShareExImageTypeEnum.DEFAULT
                )
            }
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    private fun mapToModel(generatedImageUrl: String): ShareExImageGeneratorModel {
        return ShareExImageGeneratorModel(generatedImageUrl, ShareExImageTypeEnum.CONTEXTUAL_IMAGE)
    }

    private fun getCompletedImageGeneratorParams(
        originalParam: ShareExImageGeneratorWrapperRequest,
        channelEnum: ShareExChannelEnum
    ): ShareExImageGeneratorRequest {
        val originalImageUrl = originalParam.originalImageUrl
        val updatedArgs = originalParam.params.args?.toMutableList()
        updatedArgs?.add(ShareExImageGeneratorArgRequest(PLATFORM_KEY, channelEnum.label))
        updatedArgs?.add(ShareExImageGeneratorArgRequest(PRODUCT_IMAGE_URL_KEY, originalImageUrl))
        return originalParam.params.copy(
            args = updatedArgs
        )
    }

    companion object {
        private const val PLATFORM_KEY = "platform"
        private const val PRODUCT_IMAGE_URL_KEY = "product_image_url"
    }
}
