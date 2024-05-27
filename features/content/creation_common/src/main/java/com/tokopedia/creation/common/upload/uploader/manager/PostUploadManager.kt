package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.createpost.common.view.util.FeedSellerAppReviewHelper
import com.tokopedia.creation.common.upload.domain.usecase.post.SubmitPostUseCase
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadSuccessData
import com.tokopedia.creation.common.upload.uploader.notification.PostUploadNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on September 21, 2023
 */
class PostUploadManager @AssistedInject constructor(
    @Assisted private val uploadData: CreationUploadData.Post,
    private val notificationManager: PostUploadNotificationManager,
    private val submitPostUseCase: SubmitPostUseCase,
    private val sellerAppReviewHelper: FeedSellerAppReviewHelper,
) : CreationUploadManager(notificationManager) {

    @AssistedFactory
    interface Factory {
        fun create(uploadData: CreationUploadData.Post): PostUploadManager
    }

    override suspend fun execute(
        notificationId: Int,
    ): CreationUploadExecutionResult {

        return try {
            broadcastInit(uploadData, notificationId)

            var uploadedMedia = 0

            val submitPostData = submitPostUseCase.execute(
                data = uploadData,
                onSuccessUploadPerMedia = {
                    if (uploadData.mediaList.isNotEmpty()) {
                        uploadedMedia++
                        updateProgress(
                            uploadData,
                            (uploadedMedia / uploadData.mediaList.size.toDouble() * 100).toInt()
                        )
                    }
                }
            )

            addFlagOnCreatePostSuccess()

            val successData = CreationUploadSuccessData.Post(
                activityId = submitPostData.feedContentSubmit.meta.content.activityID
            )

            val updatedUploadData = uploadData.copy(
                activityId = successData.activityId
            )

            broadcastUpdateData(updatedUploadData)

            broadcastComplete(updatedUploadData, successData)

            CreationUploadExecutionResult.Success
        } catch (throwable: Throwable) {
            broadcastFail(uploadData)

            CreationUploadExecutionResult.Error(
                uploadData,
                throwable
            )
        }
    }

    private fun addFlagOnCreatePostSuccess() {
        sellerAppReviewHelper.savePostFeedFlag()
    }
}
