package com.tokopedia.creation.common.upload.uploader.manager

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.domain.usecase.SubmitPostUseCase
import com.tokopedia.createpost.common.view.util.FeedSellerAppReviewHelper
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadSuccessData
import com.tokopedia.creation.common.upload.uploader.notification.PostUploadNotificationManager
import com.tokopedia.creation.common.upload.util.plus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * Created By : Jonathan Darwin on September 21, 2023
 */
class PostUploadManager @AssistedInject constructor(
    @ApplicationContext private val appContext: Context,
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

        val createPostData: CreatePostViewModel = SaveInstanceCacheManager(
            appContext,
            uploadData.draftId
        ).get(
            CreatePostViewModel.TAG,
            CreatePostViewModel::class.java
        ) ?: return CreationUploadExecutionResult.Error(
            uploadData,
            Exception("Cache manager with id ${uploadData.draftId} is empty")
        )

        return try {
            broadcastInit(uploadData, notificationId)

            var uploadedMedia = 0

            val submitPostData = submitPostUseCase.execute(
                id = createPostData.postId,
                type = createPostData.authorType,
                token = createPostData.token,
                authorId = uploadData.authorId,
                caption = createPostData.caption,
                media = createPostData.completeImageList.map {
                    getFileAbsolutePath(it.path)!! to it.type
                },
                mediaList = createPostData.completeImageList,
                mediaWidth = createPostData.mediaWidth,
                mediaHeight = createPostData.mediaHeight,
                onSuccessUploadPerMedia = {
                    if (createPostData.completeImageList.isNotEmpty()) {
                        uploadedMedia++
                        updateProgress(
                            uploadData,
                            (uploadedMedia / createPostData.completeImageList.size.toDouble() * 100).toInt()
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
            var loggedThrowable = throwable

            val resaveDraftThrowable = resaveDraft(createPostData)
            if (resaveDraftThrowable != null) {
                loggedThrowable += resaveDraftThrowable
            }

            broadcastFail(uploadData)

            CreationUploadExecutionResult.Error(
                uploadData,
                loggedThrowable
            )
        }
    }

    private fun getFileAbsolutePath(path: String): String? {
        return if (path.startsWith("${ContentResolver.SCHEME_FILE}://")) {
            Uri.parse(path).path
        } else {
            path
        }
    }

    private fun addFlagOnCreatePostSuccess() {
        sellerAppReviewHelper.savePostFeedFlag()
    }

    private fun resaveDraft(createPostData: CreatePostViewModel): Throwable? {
        return try {
            val cacheManager = SaveInstanceCacheManager(appContext, true)
            cacheManager.put(
                CreatePostViewModel.TAG,
                createPostData,
                TimeUnit.DAYS.toMillis(7)
            )
            cacheManager.id?.let { newDraftId ->
                broadcastUpdateData(uploadData.copy(draftId = newDraftId))
            }

            null
        } catch (throwable: Throwable) {
            throwable
        }
    }
}
