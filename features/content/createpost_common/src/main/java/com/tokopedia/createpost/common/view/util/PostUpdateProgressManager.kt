package com.tokopedia.createpost.common.view.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.*
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.DRAFT_ID
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import java.util.concurrent.TimeUnit

abstract class PostUpdateProgressManager(
    private val maxCount: Int,
    private val firstImage: String,
    protected val context: Context,
    private var mCreatePostViewModel: CreatePostViewModel? = null
) {

    companion object {
        private val TAG = PostUpdateProgressManager::class.java.simpleName
        private var isEditPostValue: Boolean = false
    }

    private var currentProgress = 0
    private var imgUrl=""

    init {
        onAddProgress()
    }

    fun onAddProgress() {
        sendBroadcast(currentProgress, imgUrl)
    }
    fun addProgress() {
        ++currentProgress
    }
    fun isEditPostValue(isEdit: Boolean) {
        isEditPostValue = isEdit
    }

    fun onSubmitPost() {
    }

    fun setCreatePostData(createPostViewModel: CreatePostViewModel) {
        mCreatePostViewModel = createPostViewModel
    }

    fun setFirstIcon(url: String) {
        imgUrl = url
    }

    fun onSuccessPost() {
        val intent = Intent(BROADCAST_SUBMIT_POST_NEW)
        intent.putExtra(SUBMIT_POST_SUCCESS_NEW, true)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun onFailedPost(errorMessage: String) {
        val intent = Intent(BROADCAST_SUBMIT_POST_NEW)
        intent.putExtra(SUBMIT_POST_SUCCESS_NEW, false)
        intent.putExtra(UPLOAD_ERROR_MESSAGE, errorMessage)
        val cacheManager = SaveInstanceCacheManager(this.context, true)
        cacheManager.put(
            CreatePostViewModel.TAG,
            mCreatePostViewModel, TimeUnit.DAYS.toMillis(7)
        )
        cacheManager.id?.let { it1 -> intent.putExtra(DRAFT_ID, it1) }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

    }

    private fun sendBroadcast(currentProgress: Int, imageUrl: String) {
        val intent = Intent(UPLOAD_POST_NEW)
        intent.putExtra(UPLOAD_POST_SUCCESS_NEW, true)
        intent.putExtra(UPLOAD_POST_PROGRESS, currentProgress)
        intent.putExtra(MAX_FILE_UPLOAD, maxCount)
        intent.putExtra(UPLOAD_FIRST_IMAGE, imageUrl)
        intent.putExtra(IS_EDIT_POST, isEditPostValue)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}
