package com.tokopedia.shareexperience.stub.domain

import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetSharePropertiesUseCaseStub @Inject constructor(): ShareExGetSharePropertiesUseCase {
    override suspend fun getData(params: ShareExBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return flow {

        }
    }

    override fun getDefaultData(): ShareExBottomSheetModel {
        return ShareExBottomSheetModel()
    }
}
