package com.tokopedia.feedplus.view.customview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.*
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.DRAFT_ID
import com.tokopedia.createpost.common.view.service.SubmitPostService
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit
import com.tokopedia.feedplus.R
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel

class PostProgressUpdateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var postIcon: ImageUnify? = null
    private var processingText: Typography? = null
    private var retryText: Typography? = null
    private var progressBar: ProgressBarUnify? = null
    private var mCreatePostViewModel: CreatePostViewModel? = null
    private var mPostUpdateSwipe: PostUpdateSwipe? = null

    init {
        View.inflate(this.context, R.layout.view_post_progress_update, this)
        postIcon = findViewById(R.id.product_img)
        processingText = findViewById(R.id.progress_bar_title)
        retryText = findViewById(R.id.retry_text)
        progressBar = findViewById(R.id.progress_bar)
    }

    fun setCreatePostData(createPostViewModel: CreatePostViewModel) {
        mCreatePostViewModel = createPostViewModel
    }

    fun setFirstIcon(productImage: String?) {
        if (productImage != null)
            postIcon?.setImageUrl(productImage)
    }

    fun setIconVisibility(isEditPost: Boolean) {
        if (isEditPost)
            postIcon?.setImageDrawable(context.getDrawable((R.drawable.cp_common_rect_white_round)))
    }

    fun setIcon(iconUrl: String) {
        postIcon?.setImageUrl(iconUrl)
    }

    fun setProgress(progress: Int) {
        processingText?.text = context.getString(R.string.cp_common_progress_bar_text)
        processingText?.setTextColor(
            MethodChecker.getColor(context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            )
        progressBar?.progressBarColorType = ProgressBarUnify.COLOR_GREEN
        retryText?.gone()

        setProgressUpdate(progress, MAX_PROGRESS_VALUE)
    }

    fun setProgressUpdate(progress: Int, maxCount: Int) {
        if (maxCount != 0)
            progressBar?.setValue(progress * MAX_PROGRESS_VALUE / maxCount, true)
        else
            progressBar?.setValue(progress, true)
    }

    fun setPostUpdateListener(postUpdateSwipe: PostUpdateSwipe) {
        mPostUpdateSwipe = postUpdateSwipe
    }

    fun handleShortsUploadFailed(
        uploadData: PlayShortsUploadModel,
        uploader: PlayShortsUploader
    ) {
        mPostUpdateSwipe?.updateVisibility(true)
        progressBar?.progressBarColorType = ProgressBarUnify.COLOR_RED
        retryText?.show()
        processingText?.text = context.getString(R.string.cp_common_progress_bar_failed_text)
        processingText?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500))
        retryText?.setOnClickListener {
            retryPostShorts(uploadData, uploader)
            /** TODO: attach tracker */
        }
    }

    fun handleFailedState(draftId: String) {
        mPostUpdateSwipe?.updateVisibility(true)
        progressBar?.progressBarColorType = ProgressBarUnify.COLOR_RED
        retryText?.show()
        processingText?.text = context.getString(R.string.cp_common_progress_bar_failed_text)
        processingText?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500))
        retryText?.setOnClickListener {
            mPostUpdateSwipe?.onRetryCLicked()
            retryPostingOnFeed(draftId)
        }
    }

    private fun retryPostingOnFeed(draftId: String){
        processingText?.text = context.getString(R.string.cp_common_progress_bar_text)
        processingText?.setTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
        )

        val cacheManager = SaveInstanceCacheManager(this.context, draftId)
        val viewModel: CreatePostViewModel = cacheManager.get(
            CreatePostViewModel.TAG,
            CreatePostViewModel::class.java
        ) ?: CreatePostViewModel()
        setCreatePostData(viewModel)
        cacheManager.put(
            CreatePostViewModel.TAG,
            viewModel, TimeUnit.DAYS.toMillis(7)
        )
        cacheManager.id?.let { draftId -> SubmitPostService.startService(this.context, draftId) }
        retryText?.gone()

    }

    private fun retryPostShorts(
        uploadData: PlayShortsUploadModel,
        uploader: PlayShortsUploader
    ){
        processingText?.text = context.getString(R.string.cp_common_progress_bar_text)
        processingText?.setTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
        )
        progressBar?.progressBarColorType = ProgressBarUnify.COLOR_GREEN
        retryText?.gone()

        setProgressUpdate(0, 0)

        uploader.upload(uploadData)
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
                    intent.extras?.getString(DRAFT_ID, "")?.let { handleFailedState(it) }
                }
            }
        }
    }

    private val submitUpdateReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) {
                    return
                }
                if (intent.action == UPLOAD_POST_NEW
                    && intent.extras?.getBoolean(UPLOAD_POST_SUCCESS_NEW) == true
                ) {
                    val progress = intent.getIntExtra(UPLOAD_POST_PROGRESS, 0)
                    val maxCount = intent.getIntExtra(MAX_FILE_UPLOAD, 0)
                    val firstIcon = intent.getStringExtra(UPLOAD_FIRST_IMAGE)
                    val isEditPost = intent.getBooleanExtra(IS_EDIT_POST, false)
                    progressBar?.progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    if (firstIcon != null)
                        setFirstIcon(firstIcon)
                    setIconVisibility(isEditPost)
                    setProgressUpdate(progress, maxCount)
                } else if (intent.action == UPLOAD_POST_NEW
                    && intent.extras?.getBoolean(UPLOAD_POST_SUCCESS_NEW) == false
                ) {
                    intent.extras?.getString(DRAFT_ID,"")?.let { handleFailedState(it) }
                }
            }
        }
    }
    fun resetProgressBarState(isEditPost: Boolean) {
        processingText?.text =
            context.getString(R.string.cp_common_progress_bar_text)
        processingText?.setTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
        )
        progressBar?.progressBarColorType = ProgressBarUnify.COLOR_GREEN
        retryText?.gone()
        setIconVisibility(isEditPost)
        setProgressUpdate(0, 0)
    }

    fun registerBroadcastReceiver() {
        context?.applicationContext?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_SUBMIT_POST_NEW)
            LocalBroadcastManager
                .getInstance(it)
                .registerReceiver(submitPostReceiver, intentFilter)
        }
    }

    fun registerBroadcastReceiverProgress() {
        context?.applicationContext?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(UPLOAD_POST_NEW)

            LocalBroadcastManager
                .getInstance(it)
                .registerReceiver(submitUpdateReceiver, intentFilter)
        }
    }

    fun unregisterBroadcastReceiver() {
        context?.applicationContext?.let {
            LocalBroadcastManager
                .getInstance(it)
                .unregisterReceiver(submitPostReceiver)
        }
    }

    fun unregisterBroadcastReceiverProgress() {
        context?.applicationContext?.let {
            LocalBroadcastManager
                .getInstance(it)
                .unregisterReceiver(submitUpdateReceiver)
        }
    }

    interface PostUpdateSwipe {
        fun updateVisibility(flag: Boolean)
        fun swipeOnPostUpdate()
        fun onRetryCLicked()
    }

    companion object{
        private const val MAX_PROGRESS_VALUE = 100
    }
}

