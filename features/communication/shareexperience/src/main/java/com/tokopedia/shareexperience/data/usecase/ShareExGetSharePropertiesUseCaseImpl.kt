package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.ShareExWrapperResponseDto
import com.tokopedia.shareexperience.data.mapper.ShareExPropertyMapper
import com.tokopedia.shareexperience.data.query.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.asFlowResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExDiscoveryBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExShopBottomSheetRequest
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

    override fun getDefaultData(): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return flow {
            val result = mapper.mapDefault()
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    override suspend fun getData(params: ShareExProductBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return getShareBottomSheetResponse(params)
    }

    override suspend fun getData(params: ShareExShopBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return getShareBottomSheetResponse(params)
    }

    override suspend fun getData(params: ShareExDiscoveryBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return getShareBottomSheetResponse(params)
    }

    private suspend fun getShareBottomSheetResponse(params: ShareExBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
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

    private fun getRequest(params: ShareExBottomSheetRequest): ShareExBottomSheetWrapperRequest {
        return ShareExBottomSheetWrapperRequest(params)
    }
}
