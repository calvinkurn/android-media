package com.tokopedia.createpost.view.customview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST_NEW
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS_NEW
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.service.SubmitPostServiceNew
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit

class PostProgressUpdateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var postIcon: ImageUnify? = null
    private var processingText: Typography? = null
    private var retryText: Typography? = null
    private var progressBar: ProgressBarUnify? = null
    private var mCreatePostViewModel: CreatePostViewModel? = null
    private var mPostUpdateSwipe:PostUpdateSwipe?=null

    init {
        View.inflate(this.context, R.layout.feed_upload_post_progress_view, this)
        postIcon = findViewById(R.id.product_img)
        processingText = findViewById(R.id.product_name)
        retryText = findViewById(R.id.retry_text)
        progressBar = findViewById(R.id.progress_bar)
    }

    fun setCreatePostData(createPostViewModel: CreatePostViewModel) {
        mCreatePostViewModel = createPostViewModel
    }

    fun setData(productImage: String) {
        postIcon?.loadImage(productImage)
    }

    fun setProgressUpdate(status: Int) {
        progressBar?.setValue(status, true)
    }

    fun setPostUpdateListener(postUpdateSwipe: PostUpdateSwipe) {
        mPostUpdateSwipe = postUpdateSwipe
    }

    fun handleFailedState() {
        mPostUpdateSwipe?.updateVisibility(true)
        progressBar?.progressBarColorType = ProgressBarUnify.COLOR_RED
        retryText?.show()
        retryText?.setOnClickListener {
            val cacheManager = SaveInstanceCacheManager(this.context, true)
            cacheManager.put(
                CreatePostViewModel.TAG,
                mCreatePostViewModel, TimeUnit.DAYS.toMillis(7)
            )
            cacheManager.id?.let { it1 -> SubmitPostServiceNew.startService(this.context, it1) }
        }
    }

    private val submitPostReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) {
                    return
                }

                if (intent.action == BROADCAST_SUBMIT_POST_NEW
                    && intent.extras?.getBoolean(SUBMIT_POST_SUCCESS_NEW) == true
                ) {
                    mPostUpdateSwipe?.swipeOnPostUpdate()
                } else if (intent.action == BROADCAST_SUBMIT_POST_NEW
                    && intent.extras?.getBoolean(SUBMIT_POST_SUCCESS_NEW) == false
                ) {
                    handleFailedState()
                }
            }
        }
    }

    fun registerBroadcastReceiver() {
        context?.applicationContext?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_SUBMIT_POST)

            LocalBroadcastManager
                .getInstance(it)
                .registerReceiver(submitPostReceiver, intentFilter)
        }
    }

    fun unregisterBroadcastReceiver() {
        context?.applicationContext?.let {
            LocalBroadcastManager
                .getInstance(it)
                .unregisterReceiver(submitPostReceiver)
        }
    }

    interface PostUpdateSwipe {
        fun updateVisibility(flag: Boolean)
        fun swipeOnPostUpdate()
    }

}


