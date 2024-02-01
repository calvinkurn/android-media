package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetSharePropertiesUseCase {
    suspend fun getData(params: ShareExBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>>
    fun getDefaultData(): ShareExBottomSheetModel
}
