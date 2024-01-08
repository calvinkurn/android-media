package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExDiscoveryBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExShopBottomSheetRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGetSharePropertiesUseCase {
    suspend fun getData(params: ShareExProductBottomSheetRequest): Flow<ShareExBottomSheetModel>
    suspend fun getData(params: ShareExShopBottomSheetRequest): Flow<ShareExBottomSheetModel>
    suspend fun getData(params: ShareExDiscoveryBottomSheetRequest): Flow<ShareExBottomSheetModel>
    fun getDefaultData(
        defaultUrl: String,
        defaultImageUrl: String
    ): Flow<ShareExBottomSheetModel>
}
