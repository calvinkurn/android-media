package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExImageGeneratorWrapperResponseDto
import com.tokopedia.shareexperience.data.query.ShareExImageGeneratorQuery
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
        params: ShareExImageGeneratorWrapperRequest
    ): Flow<ShareExResult<ShareExImageGeneratorModel>> {
        return flow {
            val result = if (params.params.sourceId != null &&
                !params.params.args.isNullOrEmpty()
            ) {
                val request = getCompletedImageGeneratorParams(params)
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
        originalParam: ShareExImageGeneratorWrapperRequest
    ): ShareExImageGeneratorRequest {
        val replacementMap = getReplacementsArgRequest(
            platform = originalParam.platform,
            imageResolution = originalParam.imageResolution,
            originalImage = originalParam.originalImageUrl
        )
        var updatedArgs: List<ShareExImageGeneratorArgRequest> = listOf()
        originalParam.params.args?.let { list ->
            updatedArgs = list.map { originalArg ->
                if (originalArg.value.isBlank() && replacementMap.containsKey(originalArg.key)) {
                    replacementMap[originalArg.key] ?: originalArg
                } else {
                    originalArg
                }
            }
        }
        return originalParam.params.copy(
            args = updatedArgs
        )
    }

    private fun getReplacementsArgRequest(
        platform: String,
        imageResolution: String,
        originalImage: String
    ): Map<String, ShareExImageGeneratorArgRequest> {
        return mapOf(
            PLATFORM_KEY to ShareExImageGeneratorArgRequest(
                key = PLATFORM_KEY,
                value = platform
            ),
            OUTPUT_RESOLUTION_KEY to ShareExImageGeneratorArgRequest(
                key = OUTPUT_RESOLUTION_KEY,
                value = imageResolution
            ),
            PRODUCT_IMAGE_URL_KEY to ShareExImageGeneratorArgRequest(
                key = PRODUCT_IMAGE_URL_KEY,
                value = originalImage
            )
        )
    }

    companion object {
        private const val PLATFORM_KEY = "platform"
        private const val OUTPUT_RESOLUTION_KEY = "output_resolution"
        private const val PRODUCT_IMAGE_URL_KEY = "product_image_url"
    }
}
