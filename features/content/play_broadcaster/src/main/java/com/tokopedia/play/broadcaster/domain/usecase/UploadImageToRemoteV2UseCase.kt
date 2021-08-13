package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
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
        params = uploaderUseCase.createParams(PLAY_BROADCAST_COVER_SOURCE_ID, mediaPath)
        return params
    }

    override suspend fun executeOnBackground(): UploadResult {
        return uploaderUseCase.execute(params)
    }

    companion object {

        private const val PLAY_BROADCAST_COVER_SOURCE_ID = "jJtrdn"
    }
}