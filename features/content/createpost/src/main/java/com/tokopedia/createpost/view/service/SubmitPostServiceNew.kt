package com.tokopedia.createpost.view.service

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST_NEW
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS_NEW
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.Content
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.DRAFT_ID
import com.tokopedia.createpost.TYPE_AFFILIATE
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.domain.usecase.SubmitPostUseCase
import com.tokopedia.createpost.view.util.FeedSellerAppReviewHelper
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.twitter_share.TwitterManager
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import rx.Subscriber
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SubmitPostServiceNew : JobIntentService() {

    @Inject
    lateinit var submitPostUseCase: SubmitPostUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var twitterManager: TwitterManager

    @Inject
    lateinit var sellerAppReviewHelper: FeedSellerAppReviewHelper

    companion object {
        private const val JOB_ID = 13131313

        fun startService(context: Context, draftId: String) {
            val work = Intent(context, SubmitPostService::class.java).apply {
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
        val notifId = Random().nextInt()

        submitPostUseCase.execute(SubmitPostUseCase.createRequestParams(
            viewModel.postId,
            viewModel.authorType,
            viewModel.token,
            if (isTypeAffiliate(viewModel.authorType)) userSession.userId
            else userSession.shopId,
            viewModel.caption,
            (if (viewModel.fileImageList.isEmpty()) viewModel.urlImageList
            else viewModel.fileImageList).map { it.path to it.type },
            if (isTypeAffiliate(viewModel.authorType)) viewModel.adIdList
            else viewModel.productIdList
        ), getSubscriber())
    }

    private fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostModule(CreatePostModule(this.applicationContext))
            .build()
            .inject(this)
    }

    private fun isTypeAffiliate(authorType: String) = authorType == TYPE_AFFILIATE

    private fun getSubscriber(): Subscriber<SubmitPostData> {
        return object : Subscriber<SubmitPostData>() {
            override fun onNext(submitPostData: SubmitPostData?) {
                if (submitPostData == null
                    || submitPostData.feedContentSubmit.success != SubmitPostData.SUCCESS) {
                    sendBroadcastFail()
                    return
                } else if (!TextUtils.isEmpty(submitPostData.feedContentSubmit.error)) {
                    sendBroadcastFail()
                    return
                }
                sendBroadcast()
                postContentToOtherService(submitPostData.feedContentSubmit.meta.content)
                addFlagOnCreatePostSuccess()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                sendBroadcastFail()
            }

            private fun sendBroadcast() {
                val intent = Intent(BROADCAST_SUBMIT_POST_NEW)
                intent.putExtra(SUBMIT_POST_SUCCESS_NEW, true)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }

            private fun sendBroadcastFail() {
                val intent = Intent(BROADCAST_SUBMIT_POST_NEW)
                intent.putExtra(SUBMIT_POST_SUCCESS_NEW, false)
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
