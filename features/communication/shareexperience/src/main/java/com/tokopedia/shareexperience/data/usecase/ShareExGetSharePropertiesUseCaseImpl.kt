package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.ShareExWrapperResponseDto
import com.tokopedia.shareexperience.data.mapper.ShareExPropertyMapper
import com.tokopedia.shareexperience.data.query.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetWrapperRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.domain.util.asFlowResult
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
    override suspend fun getData(params: ShareExBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return flow {
            val request = getRequest(params)
            val dto = repository.request<ShareExBottomSheetWrapperRequest, ShareExWrapperResponseDto>(
                sharePropertiesQuery,
                request
            )
            val result = mapper.map(dto.response)
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    override fun getDefaultData(): ShareExBottomSheetModel {
        return mapper.mapDefault()
    }

    private fun getRequest(params: ShareExBottomSheetRequest): ShareExBottomSheetWrapperRequest {
        return ShareExBottomSheetWrapperRequest(params)
    }
}
