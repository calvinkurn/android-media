package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.response.ShareExWrapperResponseDto
import com.tokopedia.shareexperience.data.mapper.ShareExPropertyMapper
import com.tokopedia.shareexperience.data.repository.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetSharePropertiesUseCaseImpl @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val mapper: ShareExPropertyMapper,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetSharePropertiesUseCase {

    private val sharePropertiesQuery = ShareExGetSharePropertiesQuery()
    override suspend fun getData(params: ShareExRequest): Flow<ShareExBottomSheetModel> {
        return flow {
            //                val response = repository.request<ShareExRequest, ShareExWrapperResponseDto>(
//                    sharePropertiesQuery, params
//                )
            val dto = ShareExWrapperResponseDto()
            val result = mapper.map(dto.response.bottomSheet)
            emit(result)
        }.flowOn(dispatchers.io)
    }
}
