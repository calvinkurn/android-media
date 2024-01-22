package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.state.UploadResult

class UploaderFactory constructor(
    private val video: UploaderManager,
    private val image: UploaderManager
) {

    /**
     * A unify the UploaderManager.
     *
     * Create a single gate to handle both Image (as well as secure) and Video upload,
     * by provide a dynamic [BaseUploaderParam] to ensure the similar parameters could be
     * use the same object.
     */
    suspend fun createUploader(param: UseCaseParam): UploadResult {
        return if (param.isVideo()) {
            video.upload(param.video)
        } else {
            image.upload(param.image)
        }
    }
}
