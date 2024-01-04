package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGetSharePropertiesUseCase {
    suspend fun getData(params: ShareExRequest): Flow<ShareExBottomSheetModel>
    fun getDefaultData(
        defaultUrl: String,
        defaultImageUrl: String
    ): Flow<ShareExBottomSheetModel>
}
