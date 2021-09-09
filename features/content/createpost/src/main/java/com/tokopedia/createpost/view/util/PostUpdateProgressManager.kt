package com.tokopedia.createpost.view.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.*

abstract class PostUpdateProgressManager(
    private val maxCount: Int,
    private val firstImage: String,
    protected val context: Context
) {

    companion object {
        private val TAG = PostUpdateProgressManager::class.java.simpleName
    }

    private var currentProgress = 0
    private var imgUrl=""

    fun onAddProgress() {
    }

    fun onSubmitPost() {
    }

    fun setFirstIcon(url: String) {
        imgUrl = url
        sendBroadcast(currentProgress++,imgUrl)
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
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

    }

    private fun sendBroadcast(currentProgress: Int,imageUrl:String) {
        val intent = Intent(UPLOAD_POST_NEW)
        intent.putExtra(UPLOAD_POST_SUCCESS_NEW, true)
        intent.putExtra(UPLOAD_POST_PROGRESS, currentProgress)
        intent.putExtra(MAX_FILE_UPLOAD, maxCount)
        intent.putExtra(UPLOAD_FIRST_IMAGE, imageUrl)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}
