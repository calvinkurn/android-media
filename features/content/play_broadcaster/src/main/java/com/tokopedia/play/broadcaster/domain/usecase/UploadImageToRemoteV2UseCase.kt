package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.play_common.const.PlayUploadSourceIdConst
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.File
import javax.inject.Inject

/**
 * Created by jegul on 15/12/20
 */
class UploadImageToRemoteV2UseCase @Inject constructor(
        private val uploaderUseCase: UploaderUseCase
) : UseCase<UploadResult>() {

    private var params: RequestParams = RequestParams.EMPTY

    fun setParams(mediaPath: File): RequestParams {
        params = uploaderUseCase.createParams(PlayUploadSourceIdConst.uploadImageSourceId, mediaPath)
        return params
    }

    override suspend fun executeOnBackground(): UploadResult {
        return uploaderUseCase(params)
    }
}
