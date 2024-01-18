package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExDiscoveryBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExShopBottomSheetRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGetSharePropertiesUseCase {
    suspend fun getData(params: ShareExProductBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>>
    suspend fun getData(params: ShareExShopBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>>
    suspend fun getData(params: ShareExDiscoveryBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>>
    fun getDefaultData(): Flow<ShareExResult<ShareExBottomSheetModel>>
}
