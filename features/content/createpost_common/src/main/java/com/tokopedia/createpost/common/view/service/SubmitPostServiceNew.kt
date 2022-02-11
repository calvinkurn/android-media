package com.tokopedia.createpost.common.view.service

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST_NEW
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS_NEW
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.Content
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.DRAFT_ID
import com.tokopedia.createpost.common.TYPE_AFFILIATE
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.DaggerCreatePostCommonComponent
import com.tokopedia.createpost.common.domain.usecase.SubmitPostUseCaseNew
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

class SubmitPostServiceNew : JobIntentService() {

    @Inject
    lateinit var submitPostUseCase: SubmitPostUseCaseNew

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var twitterManager: com.tokopedia.twitter_share.TwitterManager

    @Inject
    lateinit var sellerAppReviewHelper: com.tokopedia.createpost.common.view.util.FeedSellerAppReviewHelper

    private var postUpdateProgressManager: com.tokopedia.createpost.common.view.util.PostUpdateProgressManager? = null


    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context, draftId: String) {
            val work = Intent(context, SubmitPostServiceNew::class.java).apply {
                putExtra(DRAFT_ID, draftId)
            }
            enqueueWork(context, SubmitPostServiceNew::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override fun onHandleWork(intent: Intent) {
        val id: String = intent.getStringExtra(DRAFT_ID) ?: return
        val cacheManager = SaveInstanceCacheManager(baseContext, id)
        val viewModel: CreatePostViewModel = cacheManager.get(
            CreatePostViewModel.TAG,
            CreatePostViewModel::class.java
        ) ?: return

        postUpdateProgressManager = getProgressManager(viewModel)
        postUpdateProgressManager!!.setCreatePostData(viewModel)
        if (!viewModel.isEditState)
            postUpdateProgressManager!!.setFirstIcon((viewModel.completeImageList.first().path))

        postUpdateProgressManager?.isEditPostValue(viewModel.isEditState)
        postUpdateProgressManager?.onAddProgress()

        submitPostUseCase.postUpdateProgressManager = postUpdateProgressManager

        submitPostUseCase.execute(
            SubmitPostUseCaseNew.createRequestParams(
                viewModel.postId,
                viewModel.authorType,
                viewModel.token,
                if (isTypeAffiliate(viewModel.authorType)) userSession.userId
                else userSession.shopId,
                viewModel.caption,
                viewModel.completeImageList.map {
                    getFileAbsolutePath(it.path)!! to it.type
                },
                if (isTypeAffiliate(viewModel.authorType)) viewModel.adIdList
                else viewModel.productIdList, viewModel.completeImageList,
                 viewModel.mediaWidth,
                 viewModel.mediaHeight
            ), getSubscriber())
    }

    private fun getFileAbsolutePath(path: String): String? {
        return if (path.startsWith("${ContentResolver.SCHEME_FILE}://"))
            Uri.parse(path).path
        else
            path
    }

    private fun initInjector() {
        DaggerCreatePostCommonComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(this.applicationContext))
            .build()
            .inject(this)
    }

    private fun isTypeAffiliate(authorType: String) = authorType == TYPE_AFFILIATE

    private fun getProgressManager(viewModel: CreatePostViewModel): com.tokopedia.createpost.common.view.util.PostUpdateProgressManager {
        val firstImage = ""
        try {
            com.tokopedia.createpost.common.view.util.FileUtil.createFilePathFromUri(
                applicationContext,
                Uri.parse(viewModel.completeImageList.firstOrNull()?.path ?: "")
            )
        } catch (e: Exception) {
            Timber.e("Exception")
        }
        val maxCount = viewModel.completeImageList.size
        return object : com.tokopedia.createpost.common.view.util.PostUpdateProgressManager(maxCount, firstImage, applicationContext) {
        }
    }

    private fun getSubscriber(): Subscriber<SubmitPostData> {
        return object : Subscriber<SubmitPostData>() {
            override fun onNext(submitPostData: SubmitPostData?) {
                if (submitPostData == null
                    || submitPostData.feedContentSubmit.success != SubmitPostData.SUCCESS) {
                    postUpdateProgressManager?.onFailedPost(
                        com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
                            this@SubmitPostServiceNew,
                            RuntimeException()
                        )
                    )
                    return
                } else if (!TextUtils.isEmpty(submitPostData.feedContentSubmit.error)) {
                    postUpdateProgressManager?.onFailedPost(submitPostData.feedContentSubmit.error)
                    return
                }
                postUpdateProgressManager?.onSuccessPost()
                sendBroadcast()
                postContentToOtherService(submitPostData.feedContentSubmit.meta.content)
                addFlagOnCreatePostSuccess()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                postUpdateProgressManager?.onFailedPost(
                    com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
                        this@SubmitPostServiceNew,
                        e
                    )
                )
            }

            private fun sendBroadcast() {
                val intent = Intent(BROADCAST_SUBMIT_POST_NEW)
                intent.putExtra(SUBMIT_POST_SUCCESS_NEW, true)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
    }

    private fun postContentToOtherService(content: Content) {
        if (twitterManager.shouldPostToTwitter) postToTwitter(content)
    }

    private fun postToTwitter(content: Content) {
        GlobalScope.launchCatchError(Dispatchers.IO, block = {
            twitterManager.postTweet(content.description)
        }) { Timber.d(it) }
    }


    private fun addFlagOnCreatePostSuccess() {
        sellerAppReviewHelper.savePostFeedFlag()
    }
}
