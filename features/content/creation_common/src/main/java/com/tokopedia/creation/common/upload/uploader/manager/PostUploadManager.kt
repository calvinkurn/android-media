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
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on September 21, 2023
 */
class PostUploadManager @AssistedInject constructor(
    @ApplicationContext private val appContext: Context,
    private val submitPostUseCase: SubmitPostUseCase,
    private val sellerAppReviewHelper: FeedSellerAppReviewHelper,
) : CreationUploadManager(null) {

    @AssistedFactory
    interface Factory {
        fun create(): PostUploadManager
    }

    override suspend fun execute(
        uploadData: CreationUploadData,
        notificationId: Int,
    ): CreationUploadExecutionResult {

        if (uploadData !is CreationUploadData.Post) return CreationUploadExecutionResult.Error(
            uploadData,
            Exception("PostUploadManager is not receiving CreationUploadData.Post data")
        )

        return try {
            broadcastInit(uploadData, notificationId)

            val cacheManager = SaveInstanceCacheManager(appContext, uploadData.draftId)
            val viewModel: CreatePostViewModel = cacheManager.get(
                CreatePostViewModel.TAG,
                CreatePostViewModel::class.java
            ) ?: throw Exception("Cache manager with id ${uploadData.draftId} is empty")

            var uploadedMedia = 0

            submitPostUseCase.execute(
                id = viewModel.postId,
                type = viewModel.authorType,
                token = viewModel.token,
                authorId = uploadData.authorId,
                caption = viewModel.caption,
                media = viewModel.completeImageList.map {
                    getFileAbsolutePath(it.path)!! to it.type
                },
                mediaList = viewModel.completeImageList,
                mediaWidth = viewModel.mediaWidth,
                mediaHeight = viewModel.mediaHeight,
                onSuccessUploadPerMedia = {
                    uploadedMedia++
                    updateProgress(
                        uploadData,
                        (uploadedMedia / viewModel.completeImageList.size.toDouble() * 100).toInt(),
                    )
                }
            )

            addFlagOnCreatePostSuccess()

            broadcastComplete(uploadData)

            CreationUploadExecutionResult.Success
        } catch (throwable: Throwable) {
            broadcastFail(uploadData)

            CreationUploadExecutionResult.Error(
                uploadData,
                throwable
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
}
