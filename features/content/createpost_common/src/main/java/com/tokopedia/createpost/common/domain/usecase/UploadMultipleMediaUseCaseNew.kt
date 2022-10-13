package com.tokopedia.createpost.common.domain.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.createpost.common.di.ActivityContext
import com.tokopedia.createpost.common.view.util.FileUtil
import com.tokopedia.createpost.common.view.util.PostUpdateProgressManager
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Use this usecase after migrating both image & video uploader
 */
class UploadMultipleMediaUseCaseNew @Inject constructor(
    @ActivityContext private val context: Context,
    private val uploaderUseCase: UploaderUseCase,
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<List<SubmitPostMedium>>(graphqlRepository) {

    var postUpdateProgressManager: PostUpdateProgressManager? = null
    var tempFilePath = ""

    suspend fun executeOnBackground(mediaList: List<SubmitPostMedium>): List<SubmitPostMedium> {
        return mediaList.map {
            uploadMedia(it).also {
                updateProgress()
            }
        }
    }

    private suspend fun uploadMedia(medium: SubmitPostMedium): SubmitPostMedium {
        val param = uploaderUseCase.createParams(
            sourceId = getUploadSourceId(medium),
            filePath = File(setTempFilePath(medium)),
            withTranscode = getUploadWithTranscode(medium),
        )

        val result = uploaderUseCase(param)

        when(result) {
            is UploadResult.Success -> {
                if(isVideo(medium)) {
                    medium.videoID = result.uploadId
                    medium.mediaURL = result.videoUrl
                }
                else {
                    medium.mediaUploadID = result.uploadId
                    medium.mediaURL = ""
                    medium.type = SubmitPostMedium.TYPE_MEDIA_IMAGE_UPLOAD_ID
                }

                deleteCacheFile()
            }
        }

        return medium
    }

    private fun isVideo(medium: SubmitPostMedium): Boolean {
        return medium.type == SubmitPostMedium.TYPE_VIDEO
    }

    private fun getUploadSourceId(medium: SubmitPostMedium): String {
        return if(isVideo(medium)) UPLOAD_VIDEO_SOURCE_ID
        else UPLOAD_IMAGE_SOURCE_ID
    }

    private fun getUploadWithTranscode(medium: SubmitPostMedium): Boolean {
        return !isVideo(medium)
    }

    private fun updateProgress() {
        postUpdateProgressManager?.addProgress()
        postUpdateProgressManager?.onAddProgress()
    }

    private fun handleUri(medium: SubmitPostMedium): String{
       val filePath =  context?.let { FileUtil.createFilePathFromUri(it,Uri.parse(medium.mediaURL)) }
        if (filePath != null) {
            medium.mediaURL= filePath
        }
        return medium.mediaURL
    }

    private fun setTempFilePath(medium: SubmitPostMedium): String {
        if (medium.mediaURL.contains("${ContentResolver.SCHEME_CONTENT}://")) {
            tempFilePath = handleUri(medium)
        } else {
            tempFilePath = medium.mediaURL
        }
        return tempFilePath
    }

    private fun deleteCacheFile() {
        if (File(tempFilePath).exists()) {
            File(tempFilePath).delete()
        }
    }

    companion object {
        private const val UPLOAD_IMAGE_SOURCE_ID = "ZiLyCt"
        private const val UPLOAD_VIDEO_SOURCE_ID = "jbPRuq"
    }
}
