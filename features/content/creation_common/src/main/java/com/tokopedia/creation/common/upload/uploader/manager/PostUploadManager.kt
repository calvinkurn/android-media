package com.tokopedia.creation.common.upload.uploader.manager

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.domain.usecase.SubmitPostUseCase
import com.tokopedia.createpost.common.view.util.FeedSellerAppReviewHelper
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 21, 2023
 */
class PostUploadManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val submitPostUseCase: SubmitPostUseCase,
    private val sellerAppReviewHelper: FeedSellerAppReviewHelper,
) : CreationUploadManager(null) {

    override suspend fun execute(uploadData: CreationUploadData): Boolean {

        if (uploadData !is CreationUploadData.Post) return false

        return try {
            broadcastInit(uploadData)

            val cacheManager = SaveInstanceCacheManager(appContext, uploadData.draftId)
            val viewModel: CreatePostViewModel = cacheManager.get(
                CreatePostViewModel.TAG,
                CreatePostViewModel::class.java
            ) ?: return false

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

            true
        } catch (e: Throwable) {
            broadcastFail(uploadData)
            false
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
