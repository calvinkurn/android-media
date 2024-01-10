package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExImageGeneratorWrapperResponseDto
import com.tokopedia.shareexperience.data.query.ShareExImageGeneratorQuery
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.asFlowResult
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
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
        params: ShareExImageGeneratorRequest
    ): Flow<ShareExResult<ShareExImageGeneratorModel>> {
        return flow {
            val response = repository.request<ShareExImageGeneratorRequest, ShareExImageGeneratorWrapperResponseDto>(
                query,
                params
            )
            val result = mapToModel(response.imageGeneratorModel.imageUrl)
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    private fun mapToModel(generatedImageUrl: String): ShareExImageGeneratorModel {
        return ShareExImageGeneratorModel(generatedImageUrl)
    }
}
